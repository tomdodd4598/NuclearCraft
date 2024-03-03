package nc.recipe.radiation;

import static nc.config.NCConfig.*;

import java.util.List;

import nc.recipe.*;

public class RadiationScrubberRecipes extends BasicRecipeHandler {
	
	public RadiationScrubberRecipes() {
		super("radiation_scrubber", 1, 1, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(oreStack("dustBorax", 1), emptyFluidStack(), oreStack("dustIrradiatedBorax", 1), emptyFluidStack(), (long) radiation_scrubber_time[0], (long) radiation_scrubber_power[0], radiation_scrubber_efficiency[0]);
		
		addRecipe(emptyItemStack(), fluidStack("radaway", 250), emptyItemStack(), fluidStack("ethanol", 250), (long) radiation_scrubber_time[1], (long) radiation_scrubber_power[1], radiation_scrubber_efficiency[1]);
		addRecipe(emptyItemStack(), fluidStack("radaway_slow", 250), emptyItemStack(), fluidStack("redstone_ethanol", 250), (long) radiation_scrubber_time[2], (long) radiation_scrubber_power[2], radiation_scrubber_efficiency[2]);
	}
	
	@Override
	public List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Long.class, 1L);
		fixer.add(Long.class, 0L);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
}
