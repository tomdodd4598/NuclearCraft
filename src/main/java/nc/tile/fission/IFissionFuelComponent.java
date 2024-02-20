package nc.tile.fission;

import static nc.config.NCConfig.fission_neutron_reach;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.fission.FissionReactor;
import nc.recipe.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IFissionFuelComponent extends IFissionFluxSink, IFissionHeatingComponent {
	
	void tryPriming(FissionReactor sourceReactor, boolean fromSource);
	
	boolean isPrimed();
	
	void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache);
	
	void unprime();
	
	@Override
    default boolean isNullifyingSources(EnumFacing side) {
		return false;
	}
	
	default boolean isProducingFlux() {
		return isPrimed() || isFunctional();
	}
	
	boolean isFluxSearched();
	
	void setFluxSearched(boolean fluxSearched);
	
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
	
	long getHeatMultiplier();
	
	default double getModeratorEfficiencyFactor() {
		byte count = 0;
		double efficiency = 0D;
		for (EnumFacing dir : EnumFacing.VALUES) {
			Double lineEfficiency = getModeratorLineEfficiencies()[dir.getIndex()];
			if (lineEfficiency != null) {
				++count;
				efficiency += getModeratorLineFluxes()[dir.getIndex()] == 0 ? 0D : lineEfficiency;
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
    default boolean canSupportActiveModerator(boolean activeModeratorPos) {
		return activeModeratorPos;
	}
	
	double getEfficiency();
	
	double getEfficiencyIgnoreCoolingPenalty();
	
	void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor);
	
	long getCriticality();
	
	double getFloatingPointCriticality();
	
	boolean isSelfPriming();
	
	default void fluxSearch(final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (!isFluxSearched() && isProducingFlux()) {
			setFluxSearched(true);
		}
		else {
			return;
		}
		
		getLogic().distributeFluxFromFuelComponent(this, fluxSearchCache, componentFailCache, assumedValidCache);
	}
	
	default void defaultDistributeFlux(final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		dirLoop: for (EnumFacing dir : EnumFacing.VALUES) {
			BlockPos offPos = getTilePos().offset(dir);
			ModeratorBlockInfo activeInfo = componentFailCache.containsKey(offPos.toLong()) ? null : getModeratorBlockInfo(offPos, dir, canSupportActiveModerator(true));
			
			if (activeInfo != null && !activeInfo.blockingFlux) {
				final ModeratorLine line = new ModeratorLine(new ObjectArrayList<>(), this);
				line.info.add(activeInfo);
				
				line.flux = activeInfo.fluxFactor;
				double moderatorEfficiency = activeInfo.efficiency;
				
				for (int i = 2; i <= fission_neutron_reach + 1; ++i) {
					offPos = getTilePos().offset(dir, i);
					ModeratorBlockInfo info = componentFailCache.containsKey(offPos.toLong()) ? null : getModeratorBlockInfo(offPos, dir, canSupportActiveModerator(false));
					if (info != null) {
						if (info.blockingFlux) {
							continue dirLoop;
						}
						line.info.add(info);
						
						line.flux += info.fluxFactor;
						moderatorEfficiency += info.efficiency;
					}
					else {
						IFissionFuelComponent fuelComponent = getLogic().getNextFuelComponent(this, offPos);
						if (fuelComponent != null) {
							line.fluxSink = fuelComponent;
							fuelComponent.addFlux(line.flux);
							fuelComponent.getModeratorLineFluxes()[dir.getOpposite().getIndex()] = line.flux;
							fuelComponent.getModeratorLineEfficiencies()[dir.getOpposite().getIndex()] = moderatorEfficiency / (i - 1);
							fuelComponent.incrementHeatMultiplier();
							
							updateModeratorLine(fuelComponent, dir, line, componentFailCache, assumedValidCache);
							
							fluxSearchCache.add(fuelComponent);
						}
						else {
							IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
							if (component instanceof IFissionFluxSink fluxSink) {
                                if (fluxSink.isAcceptingFlux(dir.getOpposite())) {
									line.fluxSink = fluxSink;
									fluxSink.addFlux(line.flux);
									getModeratorLineFluxes()[dir.getIndex()] = line.flux;
									getModeratorLineEfficiencies()[dir.getIndex()] = fluxSink.moderatorLineEfficiencyFactor() * moderatorEfficiency / (i - 1);
									incrementHeatMultiplier();
									
									updateModeratorLine(fluxSink, dir, line, componentFailCache, assumedValidCache);
								}
							}
							else if (i - 1 <= fission_neutron_reach / 2) {
								BasicRecipe recipe = RecipeHelper.blockRecipe(NCRecipes.fission_reflector, getTileWorld(), offPos);
								if (recipe != null) {
									line.reflectorRecipe = recipe;
									line.flux = (long) Math.floor(2D * line.flux * recipe.getFissionReflectorReflectivity());
									addFlux(line.flux);
									getModeratorLineFluxes()[dir.getIndex()] = line.flux;
									getModeratorLineEfficiencies()[dir.getIndex()] = recipe.getFissionReflectorEfficiency() * moderatorEfficiency / (i - 1);
									incrementHeatMultiplier();
									
									if (isFunctional()) {
										onModeratorLineComplete(line, dir);
										addToModeratorCache(line, getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
									else {
										getModeratorLineCaches()[dir.getIndex()] = line;
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
		
		public final ObjectList<ModeratorBlockInfo> info;
		public final IFissionFuelComponent fuelComponent;
		public IFissionFluxSink fluxSink = null;
		public BasicRecipe reflectorRecipe = null;
		public long flux = 0;
		
		public ModeratorLine(ObjectList<ModeratorBlockInfo> info, IFissionFuelComponent fuelComponent) {
			this.info = new ObjectArrayList<>(info);
			this.fuelComponent = fuelComponent;
		}
		
		public boolean hasValidEndpoint() {
			return fluxSink != null && fluxSink.isFunctional() || reflectorRecipe != null;
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
	
	/** Adds to the full reactor cache, not the local cache! */
	static void addToModeratorCache(ModeratorLine line, LongSet activeCache, LongSet passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		for (ModeratorBlockInfo info : line.info) {
			addToModeratorCache(info, activeCache, passiveCache, componentFailCache, assumedValidCache);
		}
	}
	
	/** Adds to the full reactor cache, not the local cache! */
	static void addToModeratorCache(ModeratorBlockInfo info, LongSet activeCache, LongSet passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (info.component != null && !componentFailCache.containsKey(info.posLong)) {
			assumedValidCache.put(info.posLong, info.component);
			info.component.onAddedToModeratorCache(info);
		}
		else {
			(info.validActiveModeratorPos ? activeCache : passiveCache).add(info.posLong);
		}
	}
	
	/** Adds to local cache, not the reactor cache! */
	static void addToModeratorCache(ModeratorLine line, EnumFacing dir, LongSet[] activeCache, LongSet[] passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		for (ModeratorBlockInfo info : line.info) {
			addToModeratorCache(info, dir, activeCache, passiveCache, componentFailCache, assumedValidCache);
		}
	}
	
	/** Adds to local cache, not the reactor cache! */
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
	
	default void updateModeratorLine(IFissionFluxSink fluxSink, EnumFacing dir, ModeratorLine line, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		fluxSink.refreshIsProcessing(false);
		line.info.get(line.info.size() - 1).updateModeratorPosValidity(fluxSink.canSupportActiveModerator(true));
		if (isFunctional() && fluxSink.isFunctional()) {
			onModeratorLineComplete(line, dir);
			addToModeratorCache(line, getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
		}
		else {
			getModeratorLineCaches()[dir.getIndex()] = line;
			addToModeratorCache(line, dir, getActiveModeratorCaches(), getPassiveModeratorCaches(), componentFailCache, assumedValidCache);
		}
		getAdjacentFluxSinks()[dir.getIndex()] = fluxSink;
	}
	
	default void defaultRefreshLocal() {
		if (isFunctional()) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				ModeratorLine line = getModeratorLineCaches()[dir.getIndex()];
				if (line != null && line.hasValidEndpoint()) {
					onModeratorLineComplete(line, dir);
					getMultiblock().passiveModeratorCache.addAll(getPassiveModeratorCaches()[dir.getIndex()]);
					getMultiblock().activeModeratorCache.addAll(getActiveModeratorCaches()[dir.getIndex()]);
				}
				getMultiblock().passiveModeratorCache.addAll(getPassiveReflectorModeratorCaches()[dir.getIndex()]);
				getMultiblock().activeModeratorCache.addAll(getActiveReflectorModeratorCaches()[dir.getIndex()]);
				getMultiblock().activeReflectorCache.addAll(getActiveReflectorCache());
			}
		}
	}
	
	/** Fix to force adjacent moderators to be active */
	default void defaultRefreshModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (isFunctional()) {
			defaultRefreshAdjacentActiveModerators(componentFailCache, assumedValidCache);
		}
	}
	
	default void defaultRefreshAdjacentActiveModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			IFissionFluxSink fluxSink = getAdjacentFluxSinks()[dir.getIndex()];
			if (fluxSink != null && fluxSink.isFunctional()) {
				BlockPos adjPos = getTilePos().offset(dir);
				if (getMultiblock().passiveModeratorCache.contains(adjPos.toLong())) {
					addToModeratorCache(getModeratorBlockInfo(adjPos, dir, canSupportActiveModerator(true)), getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
				}
			}
		}
	}
	
	default void defaultForceAdjacentActiveModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			ModeratorBlockInfo info = getModeratorBlockInfo(getTilePos().offset(dir), dir, true);
			if (info != null) {
				addToModeratorCache(info, getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache, componentFailCache, assumedValidCache);
			}
		}
	}
}
