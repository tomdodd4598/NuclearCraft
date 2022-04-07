package nc.recipe.other;

import static nc.config.NCConfig.*;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class RadiationScrubberRecipes extends BasicRecipeHandler {
	
	public RadiationScrubberRecipes() {
		super("radiation_scrubber", 1, 1, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(oreStack("dustBorax", 1), emptyFluidStack(), oreStack("dustIrradiatedBorax", 1), emptyFluidStack(), radiation_scrubber_time[0], radiation_scrubber_power[0], radiation_scrubber_efficiency[0]);
		
		addRecipe(emptyItemStack(), fluidStack("radaway", 250), emptyItemStack(), fluidStack("ethanol", 250), radiation_scrubber_time[1], radiation_scrubber_power[1], radiation_scrubber_efficiency[1]);
		addRecipe(emptyItemStack(), fluidStack("radaway_slow", 250), emptyItemStack(), fluidStack("redstone_ethanol", 250), radiation_scrubber_time[2], radiation_scrubber_power[2], radiation_scrubber_efficiency[2]);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(4);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 1);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 0);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
