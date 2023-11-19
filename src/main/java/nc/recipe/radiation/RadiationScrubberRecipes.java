package nc.recipe.radiation;

import static nc.config.NCConfig.*;

import java.util.List;

import nc.recipe.*;
import nc.util.MinMax.MinMaxInt;

public class RadiationScrubberRecipes extends BasicRecipeHandler {
	
	public int maxProcessTime = 1;
	public int maxProcessPower = 0;
	
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
	protected void setStats() {
		MinMaxInt mmProcessTime = new MinMaxInt(), mmProcessPower = new MinMaxInt();
		for (BasicRecipe recipe : recipeList) {
			mmProcessTime.update(recipe.getScrubberProcessTime());
			mmProcessPower.update(recipe.getScrubberProcessPower());
		}
		
		maxProcessTime = mmProcessTime.getMax();
		maxProcessPower = mmProcessPower.getMax();
	}
	
	@Override
	public List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 1);
		fixer.add(Integer.class, 0);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
}
