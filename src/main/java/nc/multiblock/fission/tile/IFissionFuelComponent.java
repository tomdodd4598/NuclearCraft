package nc.multiblock.fission.tile;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
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
	
	public Double[] getModeratorLineEfficiencies();
	
	public IFissionFluxSink[] getAdjacentFluxSinks();
	
	public LongSet[] getPassiveModeratorCaches();
	
	public Long[] getActiveModeratorCache();
	
	public LongSet[] getPassiveReflectorModeratorCaches();
	
	public Long[] getActiveReflectorModeratorCache();
	
	public LongSet getActiveReflectorCache();
	
	public long getHeatMultiplier();
	
	public default double getModeratorEfficiencyFactor() {
		byte count = 0;
		double efficiency = 0D;
		for (Double lineEfficiency : getModeratorLineEfficiencies()) {
			if (lineEfficiency != null) {
				count++;
				efficiency += lineEfficiency;
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
			ProcessorRecipe recipe = blockRecipe(NCRecipes.fission_moderator, offPos);
			if (recipe != null) {
				final LongSet passiveModeratorCache = new LongOpenHashSet();
				long activeModeratorPos = offPos.toLong();
				int moderatorFlux = recipe.getFissionModeratorFluxFactor();
				double moderatorEfficiency = recipe.getFissionModeratorEfficiency();
				
				for (int i = 2; i <= NCConfig.fission_neutron_reach + 1; i++) {
					offPos = getTilePos().offset(dir, i);
					recipe = blockRecipe(NCRecipes.fission_moderator, offPos);
					if (recipe != null) {
						passiveModeratorCache.add(offPos.toLong());
						moderatorFlux += recipe.getFissionModeratorFluxFactor();
						moderatorEfficiency += recipe.getFissionModeratorEfficiency();
					}
					else {
						IFissionFuelComponent fuelComponent = getLogic().getNextFuelComponent(this, offPos);
						if (fuelComponent != null) {
							fuelComponent.addFlux(moderatorFlux);
							fuelComponent.getModeratorLineEfficiencies()[dir.getOpposite().getIndex()] = moderatorEfficiency/(i - 1);
							fuelComponent.incrementHeatMultiplier();
							
							updateModeratorLine(fuelComponent, offPos, dir, passiveModeratorCache, activeModeratorPos);
							
							fluxSearchCache.add(fuelComponent);
						}
						else {
							IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
							if (component instanceof IFissionFluxSink) {
								IFissionFluxSink fluxAcceptor = (IFissionFluxSink) component;
								if (fluxAcceptor.isAcceptingFlux(dir.getOpposite())) {
									fluxAcceptor.addFlux(moderatorFlux);
									getModeratorLineEfficiencies()[dir.getIndex()] = fluxAcceptor.moderatorLineEfficiencyFactor()*moderatorEfficiency/(i - 1);
									incrementHeatMultiplier();
									
									updateModeratorLine(fluxAcceptor, offPos, dir, passiveModeratorCache, activeModeratorPos);
								}
							}
							else if (i - 1 <= NCConfig.fission_neutron_reach/2) {
								recipe = blockRecipe(NCRecipes.fission_reflector, offPos);
								if (recipe != null) {
									addFlux((int)Math.floor(2D*moderatorFlux*recipe.getFissionReflectorReflectivity()));
									getModeratorLineEfficiencies()[dir.getIndex()] = recipe.getFissionReflectorEfficiency()*moderatorEfficiency/(i - 1);
									incrementHeatMultiplier();
									
									if (isFunctional()) {
										getMultiblock().passiveModeratorCache.addAll(passiveModeratorCache);
										getMultiblock().activeModeratorCache.add(activeModeratorPos);
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
									else {
										getPassiveReflectorModeratorCaches()[dir.getIndex()].addAll(passiveModeratorCache);
										getActiveReflectorModeratorCache()[dir.getIndex()] = activeModeratorPos;
										getActiveReflectorCache().add(offPos.toLong());
									}
								}
							}
						}
						passiveModeratorCache.clear();
						continue dirLoop;
					}
				}
			}
		}
	}
	
	public default void updateModeratorLine(IFissionFluxSink fluxAcceptor, BlockPos offPos, EnumFacing dir, LongSet passiveModeratorCache, long activeModeratorPos) {
		fluxAcceptor.refreshIsProcessing(false);
		if (isFunctional() && fluxAcceptor.isFunctional()) {
			getMultiblock().passiveModeratorCache.addAll(passiveModeratorCache);
			getMultiblock().activeModeratorCache.add(activeModeratorPos);
			getMultiblock().activeModeratorCache.add(offPos.offset(dir.getOpposite()).toLong());
		}
		else {
			getPassiveModeratorCaches()[dir.getIndex()].addAll(passiveModeratorCache);
			getActiveModeratorCache()[dir.getIndex()] = activeModeratorPos;
		}
		getAdjacentFluxSinks()[dir.getIndex()] = fluxAcceptor;
	}
	
	public default void defaultRefreshLocal() {
		if (!isFunctional()) return;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			IFissionFluxSink fluxAcceptor = getAdjacentFluxSinks()[dir.getIndex()];
			if (fluxAcceptor != null && fluxAcceptor.isFunctional()) {
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
				long adjPosLong = getTilePos().offset(dir).toLong();
				if (getMultiblock().passiveModeratorCache.contains(adjPosLong)) {
					getMultiblock().activeModeratorCache.add(adjPosLong);
				}
			}
		}
	}
}
