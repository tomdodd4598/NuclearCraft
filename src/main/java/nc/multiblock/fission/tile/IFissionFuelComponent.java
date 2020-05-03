package nc.multiblock.fission.tile;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.config.NCConfig;
import nc.multiblock.fission.FissionReactor;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IFissionFuelComponent extends IFissionFluxSink, IFissionHeatingComponent {
	
	public void tryPriming(FissionReactor sourceReactor);
	
	public boolean isPrimed();
	
	public void unprime();
	
	@Override
	public default boolean isNullifyingSources(EnumFacing side) {
		return false;
	}
	
	public default boolean isProducingFlux() {
		return isPrimed() || isFunctional();
	}
	
	public boolean isFluxSearched();
	
	public void setFluxSearched(boolean fluxSearched);
	
	public void incrementHeatMultiplier();
	
	public double getSourceEfficiency();
	
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize);
	
	public int[] getModeratorLineFluxes();
	
	public Double[] getModeratorLineEfficiencies();
	
	public IFissionFluxSink[] getAdjacentFluxSinks();
	
	public LongSet[] getPassiveModeratorCaches();
	
	public Long[] getActiveModeratorCache();
	
	public ModeratorLine[] getModeratorLineCaches();
	
	public LongSet[] getPassiveReflectorModeratorCaches();
	
	public Long[] getActiveReflectorModeratorCache();
	
	public LongSet getActiveReflectorCache();
	
	public long getHeatMultiplier();
	
	public default double getModeratorEfficiencyFactor() {
		byte count = 0;
		double efficiency = 0D;
		for (EnumFacing dir : EnumFacing.VALUES) {
			Double lineEfficiency = getModeratorLineEfficiencies()[dir.getIndex()];
			if (lineEfficiency != null) {
				count++;
				efficiency += (getModeratorLineFluxes()[dir.getIndex()] == 0 ? 0D : lineEfficiency);
			}
		}
		return count == 0 ? 0D : efficiency/count;
	}
	
	public double getFluxEfficiencyFactor();
	
	@Override
	public default double moderatorLineEfficiencyFactor() {
		return 1D;
	}
	
	public double getEfficiency();
	
	public void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor);
	
	public boolean isSelfPriming();
	
	public default void fluxSearch(final ObjectSet<IFissionFuelComponent> fluxSearchCache) {
		if (!isFluxSearched() && isProducingFlux()) {
			setFluxSearched(true);
		}
		else return;
		
		getLogic().distributeFluxFromFuelComponent(this, fluxSearchCache);
	}
	
	public default void defaultDistributeFlux(final ObjectSet<IFissionFuelComponent> fluxSearchCache) {
		dirLoop: for (EnumFacing dir : EnumFacing.VALUES) {
			BlockPos offPos = getTilePos().offset(dir);
			ModeratorBlockInfo activeInfo = getModeratorBlockInfo(offPos, dir, true);
			if (activeInfo != null && !activeInfo.blockingFlux) {
				final LongSet passiveModeratorCache = new LongOpenHashSet();
				final ObjectSet<ModeratorBlockInfo> passiveInfoCache = new ObjectOpenHashSet<>();
				int moderatorFlux = activeInfo.fluxFactor;
				double moderatorEfficiency = activeInfo.efficiency;
				for (int i = 2; i <= NCConfig.fission_neutron_reach + 1; i++) {
					offPos = getTilePos().offset(dir, i);
					ModeratorBlockInfo info = getModeratorBlockInfo(offPos, dir, false);
					if (info != null) {
						if (info.blockingFlux) {
							passiveModeratorCache.clear();
							passiveInfoCache.clear();
							continue dirLoop;
						}
						if (info.component != null) {
							passiveInfoCache.add(info);
						}
						passiveModeratorCache.add(offPos.toLong());
						moderatorFlux += info.fluxFactor;
						moderatorEfficiency += info.efficiency;
					}
					else {
						IFissionFuelComponent fuelComponent = getLogic().getNextFuelComponent(this, offPos);
						if (fuelComponent != null) {
							fuelComponent.addFlux(moderatorFlux);
							fuelComponent.getModeratorLineFluxes()[dir.getOpposite().getIndex()] = moderatorFlux;
							fuelComponent.getModeratorLineEfficiencies()[dir.getOpposite().getIndex()] = moderatorEfficiency/(i - 1);
							fuelComponent.incrementHeatMultiplier();
							
							updateModeratorLine(fuelComponent, dir, moderatorFlux, passiveModeratorCache, passiveInfoCache, activeInfo);
							
							fluxSearchCache.add(fuelComponent);
						}
						else {
							IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
							if (component instanceof IFissionFluxSink) {
								IFissionFluxSink fluxAcceptor = (IFissionFluxSink) component;
								if (fluxAcceptor.isAcceptingFlux(dir.getOpposite())) {
									fluxAcceptor.addFlux(moderatorFlux);
									getModeratorLineFluxes()[dir.getIndex()] = moderatorFlux;
									getModeratorLineEfficiencies()[dir.getIndex()] = fluxAcceptor.moderatorLineEfficiencyFactor()*moderatorEfficiency/(i - 1);
									incrementHeatMultiplier();
									
									updateModeratorLine(fluxAcceptor, dir, moderatorFlux, passiveModeratorCache, passiveInfoCache, activeInfo);
								}
							}
							else if (i - 1 <= NCConfig.fission_neutron_reach/2) {
								ProcessorRecipe recipe = blockRecipe(NCRecipes.fission_reflector, offPos);
								if (recipe != null) {
									int reflectedFlux = (int) Math.floor(2D*moderatorFlux*recipe.getFissionReflectorReflectivity());
									addFlux(reflectedFlux);
									getModeratorLineFluxes()[dir.getIndex()] = reflectedFlux;
									getModeratorLineEfficiencies()[dir.getIndex()] = recipe.getFissionReflectorEfficiency()*moderatorEfficiency/(i - 1);
									incrementHeatMultiplier();
									
									if (isFunctional()) {
										onModeratorLineComplete(passiveInfoCache, dir, reflectedFlux);
										addToPassiveModeratorCache(getMultiblock().passiveModeratorCache, passiveModeratorCache, passiveInfoCache);
										activeInfo.addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
									else {
										getModeratorLineCaches()[dir.getIndex()] = new ModeratorLine(passiveInfoCache, reflectedFlux);
										addToPassiveModeratorCache(getPassiveReflectorModeratorCaches()[dir.getIndex()], passiveModeratorCache, passiveInfoCache);
										activeInfo.addToModeratorCache(dir, getActiveReflectorModeratorCache(), getPassiveReflectorModeratorCaches()[dir.getIndex()]);
										getActiveReflectorCache().add(offPos.toLong());
									}
								}
							}
						}
						passiveModeratorCache.clear();
						passiveInfoCache.clear();
						continue dirLoop;
					}
				}
			}
		}
	}
	
	public static class ModeratorLine {
		
		public final ObjectSet<ModeratorBlockInfo> infoCache;
		public final int flux;
		
		public ModeratorLine(ObjectSet<ModeratorBlockInfo> infoCache, int flux) {
			this.infoCache = new ObjectOpenHashSet<>(infoCache);
			this.flux = flux;
		}
	}
	
	public default ModeratorBlockInfo getModeratorBlockInfo(BlockPos pos, EnumFacing dir, boolean activeModeratorPos) {
		IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(pos.toLong());
		if (component != null) {
			return component.getModeratorBlockInfo(dir, activeModeratorPos);
		}
		
		ProcessorRecipe recipe = blockRecipe(NCRecipes.fission_moderator, pos);
		if (recipe != null) {
			return new ModeratorBlockInfo(pos, null, false, activeModeratorPos, recipe.getFissionModeratorFluxFactor(), recipe.getFissionModeratorEfficiency());
		}
		
		return null;
	}
	
	public static class ModeratorBlockInfo {
		
		public final long posLong;
		public final IFissionComponent component;
		public final boolean blockingFlux;
		public final boolean validActiveModerator;
		public final int fluxFactor;
		public final double efficiency;
		
		public ModeratorBlockInfo(BlockPos pos, IFissionComponent component, boolean blockingFlux, boolean validActiveModerator, int fluxFactor, double efficiency) {
			posLong = pos.toLong();
			this.component = component;
			this.blockingFlux = blockingFlux;
			this.validActiveModerator = validActiveModerator;
			this.fluxFactor = fluxFactor;
			this.efficiency = efficiency;
		}
		
		public void addToModeratorCache(LongSet activeCache, LongSet passiveCache) {
			if (component != null) {
				component.onAddedToModeratorCache(this);
			}
			else {
				(validActiveModerator ? activeCache : passiveCache).add(posLong);
			}
		}
		
		public void addToModeratorCache(EnumFacing dir, Long[] activeCache, LongSet passiveCache) {
			if (component != null) {
				component.onAddedToModeratorCache(this);
			}
			else {
				if (validActiveModerator) activeCache[dir.getIndex()] = posLong;
				else passiveCache.add(posLong);
			}
		}
		
		public void addToPassiveModeratorCache(LongSet passiveCache) {
			if (component != null) {
				component.onAddedToModeratorCache(this);
			}
			else {
				passiveCache.add(posLong);
			}
		}
	}
	
	public static void addToPassiveModeratorCache(LongSet passiveModeratorCache, LongSet newPassiveModerators, ObjectSet<ModeratorBlockInfo> newPassiveInfo) {
		LongSet passiveModerators = new LongOpenHashSet(newPassiveModerators);
		for (ModeratorBlockInfo info : newPassiveInfo) {
			passiveModerators.remove(info.posLong);
			info.addToPassiveModeratorCache(passiveModeratorCache);
		}
		passiveModeratorCache.addAll(passiveModerators);
	}
	
	public static void onModeratorLineComplete(ObjectSet<ModeratorBlockInfo> newPassiveComponentInfo, EnumFacing dir, int flux) {
		for (ModeratorBlockInfo info : newPassiveComponentInfo) {
			if (info.component != null) {
				info.component.onModeratorLineComplete(info, dir, flux);
			}
		}
	}
	
	public default void updateModeratorLine(IFissionFluxSink fluxAcceptor, EnumFacing dir, int flux, LongSet newPassiveModerators, ObjectSet<ModeratorBlockInfo> newPassiveInfo, ModeratorBlockInfo activeInfo) {
		fluxAcceptor.refreshIsProcessing(false);
		if (isFunctional() && fluxAcceptor.isFunctional()) {
			onModeratorLineComplete(newPassiveInfo, dir, flux);
			addToPassiveModeratorCache(getMultiblock().passiveModeratorCache, newPassiveModerators, newPassiveInfo);
			activeInfo.addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
			getModeratorBlockInfo(fluxAcceptor.getTilePos().offset(dir.getOpposite()), dir.getOpposite(), fluxAcceptor instanceof IFissionFuelComponent).addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
		}
		else {
			getModeratorLineCaches()[dir.getIndex()] = new ModeratorLine(newPassiveInfo, flux);
			addToPassiveModeratorCache(getPassiveModeratorCaches()[dir.getIndex()], newPassiveModerators, newPassiveInfo);
			activeInfo.addToModeratorCache(dir, getActiveModeratorCache(), getPassiveModeratorCaches()[dir.getIndex()]);
		}
		getAdjacentFluxSinks()[dir.getIndex()] = fluxAcceptor;
	}
	
	public default void defaultRefreshLocal() {
		if (!isFunctional()) return;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			IFissionFluxSink fluxAcceptor = getAdjacentFluxSinks()[dir.getIndex()];
			if (fluxAcceptor != null && fluxAcceptor.isFunctional()) {
				ModeratorLine line = getModeratorLineCaches()[dir.getIndex()];
				if (line != null) {
					onModeratorLineComplete(line.infoCache, dir, line.flux);
				}
				getMultiblock().passiveModeratorCache.addAll(getPassiveModeratorCaches()[dir.getIndex()]);
				Long posLong = getActiveModeratorCache()[dir.getIndex()];
				if (posLong != null) {
					getMultiblock().activeModeratorCache.add(posLong.longValue());
				}
			}
			getMultiblock().passiveModeratorCache.addAll(getPassiveReflectorModeratorCaches()[dir.getIndex()]);
			Long posLong = getActiveReflectorModeratorCache()[dir.getIndex()];
			if (posLong != null) {
				getMultiblock().activeModeratorCache.add(posLong.longValue());
			}
			getMultiblock().activeReflectorCache.addAll(getActiveReflectorCache());
		}
	}
	
	/** Fix to force adjacent moderators to be active */
	public default void defaultRefreshModerators() {
		if (!isFunctional()) return;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			IFissionFluxSink fluxAcceptor = getAdjacentFluxSinks()[dir.getIndex()];
			if (fluxAcceptor != null && fluxAcceptor.isFunctional()) {
				BlockPos adjPos = getTilePos().offset(dir);
				if (getMultiblock().passiveModeratorCache.contains(adjPos.toLong())) {
					getModeratorBlockInfo(adjPos, dir, true).addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
				}
			}
		}
	}
}
