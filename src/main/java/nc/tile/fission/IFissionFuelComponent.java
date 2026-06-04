package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.fission.FissionReactor;
import nc.recipe.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.*;

import static nc.config.NCConfig.fission_neutron_reach;

public interface IFissionFuelComponent extends IFissionFluxSink, IFissionHeatingComponent {
	
	void tryPriming(FissionReactor sourceReactor, boolean fromSource, boolean simulate);
	
	boolean isPrimed(boolean simulate);
	
	void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache);
	
	void addToPrimedFailCache(final Long2ObjectMap<IFissionFuelComponent> primedFailCache);
	
	void unprime(boolean simulate);
	
	@Override
	default boolean isNullifyingSources(EnumFacing side, boolean simulate) {
		return false;
	}
	
	default boolean isProducingFlux(boolean simulate) {
		return isPrimed(simulate) || isFunctional(simulate);
	}
	
	boolean isFluxSearched();
	
	void setFluxSearched(boolean fluxSearched);
	
	void addToFluxSearchCache(final ObjectSet<IFissionFuelComponent> fluxSearchCache);
	
	void incrementHeatMultiplier();
	
	double getSourceEfficiency();
	
	void setSourceEfficiency(double sourceEfficiency, boolean maximize);
	
	long[] getModeratorLineFluxes();
	
	Double[] getModeratorLineEfficiencies();
	
	IFissionFluxSink[] getAdjacentFluxSinks();
	
	LongSet[] getPassiveModeratorCaches();
	
	LongSet[] getActiveModeratorCaches();
	
	ModeratorLine[] getModeratorLineCaches();
	
	LongSet[] getPassiveReflectorModeratorCaches();
	
	LongSet[] getActiveReflectorModeratorCaches();
	
	LongSet getActiveReflectorCache();
	
	long getBaseProcessHeat();
	
	double getBaseProcessEfficiency();
	
	long getHeatMultiplier(boolean simulate);
	
	long getIntrinsicFlux();
	
	double getIntrinsicFluxEfficiencyFactor();
	
	default double getModeratorEfficiencyFactor() {
		int count = 0;
		double efficiency = 0D;
		for (EnumFacing dir : EnumFacing.VALUES) {
			int index = dir.getIndex();
			Double lineEfficiency = getModeratorLineEfficiencies()[index];
			if (lineEfficiency != null) {
				++count;
				efficiency += getModeratorLineFluxes()[index] == 0 ? 0D : lineEfficiency;
			}
		}
		return count == 0 ? 0D : efficiency / count;
	}
	
	double getFluxEfficiencyFactor();
	
	@Override
	default double moderatorLineEfficiencyFactor() {
		return 1D;
	}
	
	@Override
	default boolean canSupportActiveModerator(boolean activeModeratorPos, boolean simulate) {
		return activeModeratorPos;
	}
	
	double getEfficiency(boolean simulate);
	
	double getEfficiencyIgnoreCoolingPenalty(boolean simulate);
	
	void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor);
	
	long getCriticality();
	
	double getFloatingPointCriticality();
	
	boolean isRunning(boolean simulate);
	
	int getDecayHeating();
	
	double getFloatingPointDecayHeating();
	
	boolean isInitiallyPrimed(boolean simulate);
	
	default void fluxSearch(final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		if (!isFluxSearched() && isProducingFlux(simulate)) {
			setFluxSearched(true);
		}
		else {
			return;
		}
		
		getLogic().distributeFluxFromFuelComponent(this, fluxSearchCache, componentFailCache, assumedValidCache, simulate);
	}
	
	default void defaultDistributeFlux(final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		dirLoop:
		for (EnumFacing dir : EnumFacing.VALUES) {
			int index = dir.getIndex(), oppositeIndex = dir.getOpposite().getIndex();
			BlockPos offPos = getTilePos().offset(dir);
			
			long intrinsicFlux = getIntrinsicFlux();
			
			if (intrinsicFlux > 0L) {
				final IFissionFuelComponent fuelComponent = getLogic().getNextFuelComponent(this, offPos);
				if (fuelComponent != null) {
					final ModeratorLine line = new ModeratorLine(new ArrayList<>(), this);
					line.flux = intrinsicFlux;
					line.fluxSink = fuelComponent;
					
					fuelComponent.addFlux(line.flux);
					fuelComponent.getModeratorLineFluxes()[oppositeIndex] = line.flux;
					fuelComponent.getModeratorLineEfficiencies()[oppositeIndex] = getIntrinsicFluxEfficiencyFactor();
					fuelComponent.incrementHeatMultiplier();
					
					updateModeratorLine(fuelComponent, dir, line, componentFailCache, assumedValidCache, simulate);
					
					fuelComponent.addToFluxSearchCache(fluxSearchCache);
					continue;
				}
				else {
					final IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
					if (component instanceof IFissionFluxSink fluxSink) {
						if (fluxSink.isAcceptingFlux(dir.getOpposite(), simulate)) {
							final ModeratorLine line = new ModeratorLine(new ArrayList<>(), this);
							line.flux = intrinsicFlux;
							line.fluxSink = fluxSink;
							
							fluxSink.addFlux(line.flux);
							getModeratorLineFluxes()[index] = line.flux;
							getModeratorLineEfficiencies()[index] = fluxSink.moderatorLineEfficiencyFactor() * getIntrinsicFluxEfficiencyFactor();
							incrementHeatMultiplier();
							
							updateModeratorLine(fluxSink, dir, line, componentFailCache, assumedValidCache, simulate);
							continue;
						}
					}
				}
			}
			
			final ModeratorBlockInfo activeInfo = componentFailCache.containsKey(offPos.toLong()) ? null : getModeratorBlockInfo(offPos, dir, canSupportActiveModerator(true, simulate));
			
			if (activeInfo != null && !activeInfo.blockingFlux) {
				final ModeratorLine line = new ModeratorLine(new ArrayList<>(), this);
				line.info.add(activeInfo);
				
				line.flux = intrinsicFlux + activeInfo.fluxFactor;
				double moderatorEfficiency = activeInfo.efficiency;
				
				for (int i = 2; i <= fission_neutron_reach + 1; ++i) {
					offPos = getTilePos().offset(dir, i);
					final ModeratorBlockInfo info = componentFailCache.containsKey(offPos.toLong()) ? null : getModeratorBlockInfo(offPos, dir, canSupportActiveModerator(false, simulate));
					if (info != null) {
						if (info.blockingFlux) {
							continue dirLoop;
						}
						line.info.add(info);
						
						line.flux += info.fluxFactor;
						moderatorEfficiency += info.efficiency;
					}
					else {
						final IFissionFuelComponent fuelComponent = getLogic().getNextFuelComponent(this, offPos);
						if (fuelComponent != null) {
							line.fluxSink = fuelComponent;
							fuelComponent.addFlux(line.flux);
							fuelComponent.getModeratorLineFluxes()[oppositeIndex] = line.flux;
							fuelComponent.getModeratorLineEfficiencies()[oppositeIndex] = moderatorEfficiency / (i - 1);
							fuelComponent.incrementHeatMultiplier();
							
							updateModeratorLine(fuelComponent, dir, line, componentFailCache, assumedValidCache, simulate);
							
							fuelComponent.addToFluxSearchCache(fluxSearchCache);
						}
						else {
							final IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
							if (component instanceof IFissionFluxSink fluxSink) {
								if (fluxSink.isAcceptingFlux(dir.getOpposite(), simulate)) {
									line.fluxSink = fluxSink;
									fluxSink.addFlux(line.flux);
									getModeratorLineFluxes()[index] = line.flux;
									getModeratorLineEfficiencies()[index] = fluxSink.moderatorLineEfficiencyFactor() * moderatorEfficiency / (i - 1);
									incrementHeatMultiplier();
									
									updateModeratorLine(fluxSink, dir, line, componentFailCache, assumedValidCache, simulate);
								}
							}
							else if (i - 1 <= fission_neutron_reach / 2) {
								final BasicRecipe recipe = RecipeHelper.blockRecipe(NCRecipes.fission_reflector, getTileWorld(), offPos);
								if (recipe != null) {
									line.reflectorRecipe = recipe;
									line.flux = (long) Math.floor(2D * line.flux * recipe.getFissionReflectorReflectivity());
									addFlux(line.flux);
									getModeratorLineFluxes()[index] = line.flux;
									getModeratorLineEfficiencies()[index] = recipe.getFissionReflectorEfficiency() * moderatorEfficiency / (i - 1);
									incrementHeatMultiplier();
									
									if (isFunctional(simulate)) {
										onModeratorLineComplete(line, dir);
										addToModeratorCache(line, getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
									else {
										getModeratorLineCaches()[index] = line;
										addToModeratorCache(line, dir, getActiveReflectorModeratorCaches(), getPassiveReflectorModeratorCaches(), componentFailCache, assumedValidCache);
										getActiveReflectorCache().add(offPos.toLong());
									}
								}
							}
						}
						continue dirLoop;
					}
				}
			}
		}
	}
	
	class ModeratorLine {
		
		public final List<ModeratorBlockInfo> info;
		public final IFissionFuelComponent fuelComponent;
		public IFissionFluxSink fluxSink = null;
		public BasicRecipe reflectorRecipe = null;
		public long flux = 0;
		
		public ModeratorLine(List<ModeratorBlockInfo> info, IFissionFuelComponent fuelComponent) {
			this.info = new ArrayList<>(info);
			this.fuelComponent = fuelComponent;
		}
		
		public boolean hasValidEndpoint(boolean simulate) {
			return fluxSink != null && fluxSink.isFunctional(simulate) || reflectorRecipe != null;
		}
	}
	
	default ModeratorBlockInfo getModeratorBlockInfo(BlockPos pos, EnumFacing dir, boolean validActiveModeratorPos) {
		IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(pos.toLong());
		if (component != null) {
			return component.getModeratorBlockInfo(dir, validActiveModeratorPos);
		}
		
		BasicRecipe recipe = RecipeHelper.blockRecipe(NCRecipes.fission_moderator, getTileWorld(), pos);
		if (recipe != null) {
			return new ModeratorBlockInfo(pos, null, false, validActiveModeratorPos, recipe.getFissionModeratorFluxFactor(), recipe.getFissionModeratorEfficiency());
		}
		
		return null;
	}
	
	class ModeratorBlockInfo {
		
		public final long posLong;
		public final IFissionComponent component;
		public final boolean blockingFlux;
		public boolean validActiveModeratorPos;
		public final long fluxFactor;
		public final double efficiency;
		
		public ModeratorBlockInfo(BlockPos pos, IFissionComponent component, boolean blockingFlux, boolean validActiveModeratorPos, long fluxFactor, double efficiency) {
			posLong = pos.toLong();
			this.component = component;
			this.blockingFlux = blockingFlux;
			this.validActiveModeratorPos = validActiveModeratorPos;
			this.fluxFactor = fluxFactor;
			this.efficiency = efficiency;
		}
		
		public void updateModeratorPosValidity(boolean validActiveModeratorPosIn) {
			validActiveModeratorPos |= validActiveModeratorPosIn;
		}
	}
	
	/**
	 * Adds to the full reactor cache, not the local cache!
	 */
	static void addToModeratorCache(ModeratorLine line, LongSet activeCache, LongSet passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		for (ModeratorBlockInfo info : line.info) {
			addToModeratorCache(info, activeCache, passiveCache, componentFailCache, assumedValidCache);
		}
	}
	
	/**
	 * Adds to the full reactor cache, not the local cache!
	 */
	static void addToModeratorCache(ModeratorBlockInfo info, LongSet activeCache, LongSet passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (info.component != null && !componentFailCache.containsKey(info.posLong)) {
			assumedValidCache.put(info.posLong, info.component);
			info.component.onAddedToModeratorCache(info);
		}
		else {
			(info.validActiveModeratorPos ? activeCache : passiveCache).add(info.posLong);
		}
	}
	
	/**
	 * Adds to local cache, not the reactor cache!
	 */
	static void addToModeratorCache(ModeratorLine line, EnumFacing dir, LongSet[] activeCache, LongSet[] passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		for (ModeratorBlockInfo info : line.info) {
			addToModeratorCache(info, dir, activeCache, passiveCache, componentFailCache, assumedValidCache);
		}
	}
	
	/**
	 * Adds to local cache, not the reactor cache!
	 */
	static void addToModeratorCache(ModeratorBlockInfo info, EnumFacing dir, LongSet[] activeCache, LongSet[] passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (info.component != null && !componentFailCache.containsKey(info.posLong)) {
			assumedValidCache.put(info.posLong, info.component);
			info.component.onAddedToModeratorCache(info);
		}
		else {
			if (info.validActiveModeratorPos) {
				activeCache[dir.getIndex()].add(info.posLong);
			}
			else {
				passiveCache[dir.getIndex()].add(info.posLong);
			}
		}
	}
	
	static void onModeratorLineComplete(ModeratorLine line, EnumFacing dir) {
		for (ModeratorBlockInfo info : line.info) {
			if (info.component != null) {
				info.component.onModeratorLineComplete(line, info, dir);
			}
		}
	}
	
	default void updateModeratorLine(IFissionFluxSink fluxSink, EnumFacing dir, ModeratorLine line, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		fluxSink.onEndModeratorLine(simulate);
		if (!line.info.isEmpty()) {
			line.info.get(line.info.size() - 1).updateModeratorPosValidity(fluxSink.canSupportActiveModerator(true, simulate));
		}
		
		int index = dir.getIndex();
		if (isFunctional(simulate) && fluxSink.isFunctional(simulate)) {
			onModeratorLineComplete(line, dir);
			addToModeratorCache(line, getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
		}
		else {
			getModeratorLineCaches()[index] = line;
			addToModeratorCache(line, dir, getActiveModeratorCaches(), getPassiveModeratorCaches(), componentFailCache, assumedValidCache);
		}
		getAdjacentFluxSinks()[index] = fluxSink;
	}
	
	default void defaultRefreshLocal(boolean simulate) {
		if (isFunctional(simulate)) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				int index = dir.getIndex();
				ModeratorLine line = getModeratorLineCaches()[index];
				if (line != null && line.hasValidEndpoint(simulate)) {
					onModeratorLineComplete(line, dir);
					getMultiblock().passiveModeratorCache.addAll(getPassiveModeratorCaches()[index]);
					getMultiblock().activeModeratorCache.addAll(getActiveModeratorCaches()[index]);
				}
				getMultiblock().passiveModeratorCache.addAll(getPassiveReflectorModeratorCaches()[index]);
				getMultiblock().activeModeratorCache.addAll(getActiveReflectorModeratorCaches()[index]);
				getMultiblock().activeReflectorCache.addAll(getActiveReflectorCache());
			}
		}
	}
	
	/**
	 * Fix to force adjacent moderators to be active
	 */
	default void defaultRefreshModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		if (isFunctional(simulate)) {
			defaultRefreshAdjacentActiveModerators(componentFailCache, assumedValidCache, simulate);
		}
	}
	
	default void defaultRefreshAdjacentActiveModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			IFissionFluxSink fluxSink = getAdjacentFluxSinks()[dir.getIndex()];
			if (fluxSink != null && fluxSink.isFunctional(simulate)) {
				BlockPos adjPos = getTilePos().offset(dir);
				if (getMultiblock().passiveModeratorCache.contains(adjPos.toLong())) {
					addToModeratorCache(getModeratorBlockInfo(adjPos, dir, canSupportActiveModerator(true, simulate)), getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
				}
			}
		}
	}
	
	default void defaultForceAdjacentActiveModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			ModeratorBlockInfo info = getModeratorBlockInfo(getTilePos().offset(dir), dir, true);
			if (info != null) {
				addToModeratorCache(info, getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
			}
		}
	}
}
