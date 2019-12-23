package nc.recipe.other;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class RadiationScrubberRecipes extends ProcessorRecipeHandler {
	
	public RadiationScrubberRecipes() {
		super("radiation_scrubber", 1, 1, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(oreStack("dustBorax", 1), emptyFluidStack(), oreStack("dustIrradiatedBorax", 1), emptyFluidStack(), NCConfig.radiation_scrubber_time[0], NCConfig.radiation_scrubber_power[0], NCConfig.radiation_scrubber_efficiency[0]);
		
		addRecipe(emptyItemStack(), fluidStack("radaway", 250), emptyItemStack(), fluidStack("ethanol", 250), NCConfig.radiation_scrubber_time[1], NCConfig.radiation_scrubber_power[1], NCConfig.radiation_scrubber_efficiency[1]);
		addRecipe(emptyItemStack(), fluidStack("radaway_slow", 250), emptyItemStack(), fluidStack("redstone_ethanol", 250), NCConfig.radiation_scrubber_time[2], NCConfig.radiation_scrubber_power[2], NCConfig.radiation_scrubber_efficiency[2]);
	}
}
