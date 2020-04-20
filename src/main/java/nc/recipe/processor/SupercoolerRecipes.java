package nc.recipe.processor;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class SupercoolerRecipes extends ProcessorRecipeHandler {
	
	public SupercoolerRecipes() {
		super("supercooler", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("helium", FluidStackHelper.BUCKET_VOLUME*8), fluidStack("liquid_helium", 25), 1D, 1D);
		addRecipe(fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME*8), fluidStack("liquid_nitrogen", 25), 0.5D, 0.5D);
		addRecipe(fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("ice", FluidStackHelper.BUCKET_VOLUME), 0.25D, 0.5D);
		addRecipe(fluidStack("emergency_coolant_heated", FluidStackHelper.BUCKET_VOLUME), fluidStack("emergency_coolant", FluidStackHelper.BUCKET_VOLUME), 0.5D, 1D);
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
	
	@Override
	public List getFactoredExtras(List extras, int factor) {
		List factored = new ArrayList(extras);
		factored.set(0, (double)extras.get(0)/factor);
		return factored;
	}
}
