package nc.tile.processor;

import java.util.List;

import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;

public interface IProcessor extends IInterfaceable, IBufferable {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ProcessorRecipe getRecipe();
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public static double maxPowerMultiplier(NCRecipes.Type recipeType) {
		double max = 1D;
		List<ProcessorRecipe> recipes = recipeType.getRecipeHandler().getRecipes();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null || recipe.extras().size() < 2) continue;
			else if (recipe.extras().get(1) instanceof Double) max = Math.max(max, (double) recipe.extras().get(1));
		}
		return max;
	}
	
	public static int getBaseCapacity(NCRecipes.Type recipeType) {
		return (int) (32000D*IProcessor.maxPowerMultiplier(recipeType));
	}
}
