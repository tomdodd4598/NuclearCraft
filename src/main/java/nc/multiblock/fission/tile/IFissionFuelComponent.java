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
	
	public ModeratorLineComponentCache[] getModeratorLineComponentCaches();
	
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
			ModeratorLineBlockInfo activeInfo = getModeratorComponentInfo(offPos, true);
			if (activeInfo != null && !activeInfo.blockingFlux) {
				final LongSet passiveModeratorCache = new LongOpenHashSet();
				final ObjectSet<ModeratorLineBlockInfo> passiveComponentInfoCache = new ObjectOpenHashSet<>();
				int moderatorFlux = activeInfo.fluxFactor;
				double moderatorEfficiency = activeInfo.efficiency;
				for (int i = 2; i <= NCConfig.fission_neutron_reach + 1; i++) {
					offPos = getTilePos().offset(dir, i);
					ModeratorLineBlockInfo info = getModeratorComponentInfo(offPos, false);
					if (info != null) {
						if (info.blockingFlux) {
							passiveModeratorCache.clear();
							passiveComponentInfoCache.clear();
							continue dirLoop;
						}
						if (info.component != null) {
							passiveComponentInfoCache.add(info);
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
							
							updateModeratorLine(fuelComponent, offPos, dir, moderatorFlux, passiveModeratorCache, passiveComponentInfoCache, activeInfo);
							
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
									
									updateModeratorLine(fluxAcceptor, offPos, dir, moderatorFlux, passiveModeratorCache, passiveComponentInfoCache, activeInfo);
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
										onModeratorLineComplete(passiveComponentInfoCache, reflectedFlux);
										addToPassiveModeratorCache(getMultiblock().passiveModeratorCache, passiveModeratorCache, passiveComponentInfoCache);
										activeInfo.addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
									else {
										getModeratorLineComponentCaches()[dir.getIndex()] = new ModeratorLineComponentCache(passiveComponentInfoCache, reflectedFlux);
										addToPassiveModeratorCache(getPassiveReflectorModeratorCaches()[dir.getIndex()], passiveModeratorCache, passiveComponentInfoCache);
										activeInfo.addToModeratorCache(dir, getActiveReflectorModeratorCache(), getPassiveReflectorModeratorCaches()[dir.getIndex()]);
										getActiveReflectorCache().add(offPos.toLong());
									}
								}
							}
						}
						passiveModeratorCache.clear();
						passiveComponentInfoCache.clear();
						continue dirLoop;
					}
				}
			}
		}
	}
	
	public static class ModeratorLineComponentCache {
		
		public final ObjectSet<ModeratorLineBlockInfo> infoCache;
		public final int flux;
		
		public ModeratorLineComponentCache(ObjectSet<ModeratorLineBlockInfo> infoCache, int flux) {
			this.infoCache = infoCache;
			this.flux = flux;
		}
	}
	
	public default ModeratorLineBlockInfo getModeratorComponentInfo(BlockPos pos, boolean activeModeratorPos) {
		IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(pos.toLong());
		if (component != null) {
			return component.getModeratorComponentInfo(activeModeratorPos);
		}
		
		ProcessorRecipe recipe = blockRecipe(NCRecipes.fission_moderator, pos);
		if (recipe != null) {
			return new ModeratorLineBlockInfo(pos, null, false, activeModeratorPos, recipe.getFissionModeratorFluxFactor(), recipe.getFissionModeratorEfficiency());
		}
		
		return null;
	}
	
	public static class ModeratorLineBlockInfo {
		
		public final long posLong;
		public final IFissionComponent component;
		public final boolean blockingFlux;
		public final boolean validActiveModerator;
		public final int fluxFactor;
		public final double efficiency;
		
		public ModeratorLineBlockInfo(BlockPos pos, IFissionComponent component, boolean blockingFlux, boolean validActiveModerator, int fluxFactor, double efficiency) {
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
		
		public void onModeratorLineComplete(int flux) {
			if (component != null) {
				component.onModeratorLineComplete(this, flux);
			}
		}
	}
	
	// Don't iterate if there are no fission components
	public static void addToPassiveModeratorCache(LongSet passiveModeratorCache, LongSet newPassiveModerators, ObjectSet<ModeratorLineBlockInfo> newPassiveComponentInfo) {
		if (newPassiveComponentInfo.isEmpty()) {
			passiveModeratorCache.addAll(newPassiveModerators);
		}
		else for (ModeratorLineBlockInfo info : newPassiveComponentInfo) {
			info.addToPassiveModeratorCache(passiveModeratorCache);
		}
	}
	
	public static void onModeratorLineComplete(ObjectSet<ModeratorLineBlockInfo> newPassiveComponentInfo, int flux) {
		for (ModeratorLineBlockInfo info : newPassiveComponentInfo) {
			info.onModeratorLineComplete(flux);
		}
	}
	
	public default void updateModeratorLine(IFissionFluxSink fluxAcceptor, BlockPos fluxAcceptorPos, EnumFacing dir, int flux, LongSet newPassiveModerators, ObjectSet<ModeratorLineBlockInfo> newPassiveComponentInfo, ModeratorLineBlockInfo activeInfo) {
		fluxAcceptor.refreshIsProcessing(false);
		if (isFunctional() && fluxAcceptor.isFunctional()) {
			onModeratorLineComplete(newPassiveComponentInfo, flux);
			addToPassiveModeratorCache(getMultiblock().passiveModeratorCache, newPassiveModerators, newPassiveComponentInfo);
			activeInfo.addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
			getModeratorComponentInfo(fluxAcceptorPos.offset(dir.getOpposite()), true).addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
		}
		else {
			getModeratorLineComponentCaches()[dir.getIndex()] = new ModeratorLineComponentCache(newPassiveComponentInfo, flux);
			addToPassiveModeratorCache(getPassiveModeratorCaches()[dir.getIndex()], newPassiveModerators, newPassiveComponentInfo);
			activeInfo.addToModeratorCache(dir, getActiveModeratorCache(), getPassiveModeratorCaches()[dir.getIndex()]);
		}
		getAdjacentFluxSinks()[dir.getIndex()] = fluxAcceptor;
	}
	
	// No need to create ModeratorLineBlockInfo here, as no new active moderator positions are being found
	public default void defaultRefreshLocal() {
		if (!isFunctional()) return;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			IFissionFluxSink fluxAcceptor = getAdjacentFluxSinks()[dir.getIndex()];
			if (fluxAcceptor != null && fluxAcceptor.isFunctional()) {
				ModeratorLineComponentCache componentCache = getModeratorLineComponentCaches()[dir.getIndex()];
				if (componentCache != null) {
					onModeratorLineComplete(componentCache.infoCache, componentCache.flux);
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
					getModeratorComponentInfo(adjPos, true).addToModeratorCache(getMultiblock().activeModeratorCache, getMultiblock().passiveModeratorCache);
				}
			}
		}
	}
}
