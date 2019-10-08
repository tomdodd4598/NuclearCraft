package nc.multiblock.fission.tile;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import nc.config.NCConfig;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.FissionReactorType;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IFissionFuelComponent extends IFissionComponent {
	
	public void tryPriming(FissionReactor sourceReactor);
	
	public boolean isPrimed();
	
	public void unprime();
	
	public void refreshIsProcessing(boolean checkCluster);
	
	public default boolean isProducingFlux() {
		return isPrimed() || isFunctional();
	}
	
	public boolean isFluxSearched();
	
	public void setFluxSearched(boolean fluxSearched);
	
	public void incrementHeatMultiplier();
	
	public double getSourceEfficiency();
	
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize);
	
	public void addFlux(int flux);
	
	public Double[] getModeratorLineEfficiencies();
	
	public IFissionFuelComponent[] getAdjacentFuelComponents();
	
	public LongSet[] getPassiveModeratorCaches();
	
	public Long[] getActiveModeratorCache();
	
	public LongSet getPassiveReflectorModeratorCache();
	
	public Long getActiveReflectorModeratorCache();
	
	public void setActiveReflectorModeratorCache(long posLong);
	
	public LongSet getActiveReflectorCache();
	
	public long getRawHeating();
	
	public double getEffectiveHeating();
	
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
	
	public double getEfficiency();
	
	public void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor);
	
	public default void fluxSearch() {
		if (!isFluxSearched() && isProducingFlux()) {
			setFluxSearched(true);
		}
		else return;
		
		if (getMultiblock().type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
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
							IFissionFuelComponent fuelComponent = getMultiblock().type == FissionReactorType.SOLID_FUEL ? getMultiblock().getCellMap().get(offPos.toLong()) : getMultiblock().getVesselMap().get(offPos.toLong());
							if (fuelComponent != null) {
								fuelComponent.addFlux(moderatorFlux);
								fuelComponent.getModeratorLineEfficiencies()[dir.getOpposite().getIndex()] = moderatorEfficiency/(i - 1);
								fuelComponent.incrementHeatMultiplier();
								
								fuelComponent.refreshIsProcessing(false);
								if (isFunctional() && fuelComponent.isFunctional()) {
									getMultiblock().passiveModeratorCache.addAll(passiveModeratorCache);
									getMultiblock().activeModeratorCache.add(activeModeratorPos);
									getMultiblock().activeModeratorCache.add(offPos.offset(dir.getOpposite()).toLong());
								}
								else {
									getPassiveModeratorCaches()[dir.getIndex()].addAll(passiveModeratorCache);
									getActiveModeratorCache()[dir.getIndex()] = activeModeratorPos;
								}
								getAdjacentFuelComponents()[dir.getIndex()] = fuelComponent;
								fuelComponent.fluxSearch();
							}
							else if (i - 1 <= NCConfig.fission_neutron_reach/2) {
								recipe = blockRecipe(NCRecipes.fission_reflector, offPos);
								if (recipe != null) {
									addFlux((int) Math.round(2D*moderatorFlux*recipe.getFissionReflectorReflectivity()));
									getModeratorLineEfficiencies()[dir.getIndex()] = recipe.getFissionReflectorEfficiency()*moderatorEfficiency/(i - 1);
									incrementHeatMultiplier();
									
									if (isFunctional()) {
										getMultiblock().passiveModeratorCache.addAll(passiveModeratorCache);
										getMultiblock().activeModeratorCache.add(activeModeratorPos);
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
									else {
										getPassiveReflectorModeratorCache().addAll(passiveModeratorCache);
										setActiveReflectorModeratorCache(activeModeratorPos);
										getActiveReflectorCache().add(offPos.toLong());
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
	}
	
	public default void refreshPrimed() {
		if (!isFunctional()) return;
		
		if (getMultiblock().type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionFuelComponent fuelComponent = getAdjacentFuelComponents()[dir.getIndex()];
				if (fuelComponent != null && fuelComponent.isFunctional()) {
					getMultiblock().passiveModeratorCache.addAll(getPassiveModeratorCaches()[dir.getIndex()]);
					Long posLong = getActiveModeratorCache()[dir.getIndex()];
					if (posLong != null) {
						getMultiblock().activeModeratorCache.add(posLong.longValue());
					}
				}
			}
			getMultiblock().passiveModeratorCache.addAll(getPassiveReflectorModeratorCache());
			Long posLong = getActiveReflectorModeratorCache();
			if (posLong != null) {
				getMultiblock().activeModeratorCache.add(posLong.longValue());
			}
			getMultiblock().activeReflectorCache.addAll(getActiveReflectorCache());
		}
	}
	
	public void doMeltdown();
}
