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
	
	public void unprime();
	
	public boolean isPrimed();
	
	public void refreshIsProcessing(boolean checkCluster);
	
	public default boolean isProducingFlux() {
		return isPrimed() || isFunctional();
	}
	
	public boolean isFluxSearched();
	
	public void setFluxSearched(boolean fluxSearched);
	
	public void incrementHeatMultiplier();
	
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize);
	
	public void resetFlux();
	
	public void addFlux(int flux);
	
	public Double[] getModeratorLineEfficiencies();
	
	public long getHeating();
	
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
									passiveModeratorCache.clear();
									getMultiblock().activeModeratorCache.add(activeModeratorPos);
								}
								fuelComponent.fluxSearch();
							}
							else if (i - 1 <= NCConfig.fission_neutron_reach/2) {
								recipe = blockRecipe(NCRecipes.fission_reflector, offPos);
								if (recipe != null) {
									addFlux((int) Math.round(2*moderatorFlux*recipe.getFissionReflectorReflectivity()));
									getModeratorLineEfficiencies()[dir.getIndex()] = recipe.getFissionReflectorEfficiency()*moderatorEfficiency/(i - 1);
									incrementHeatMultiplier();
									if (isFunctional()) {
										getMultiblock().passiveModeratorCache.addAll(passiveModeratorCache);
										passiveModeratorCache.clear();
										getMultiblock().activeReflectorCache.add(offPos.toLong());
									}
								}
							}
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
				BlockPos offPos = getTilePos().offset(dir);
				if (blockRecipe(NCRecipes.fission_moderator, offPos) != null) getMultiblock().activeModeratorCache.add(offPos.toLong());
			}
		}
	}
	
	public void doMeltdown();
}
