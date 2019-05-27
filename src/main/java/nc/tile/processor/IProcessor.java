package nc.tile.processor;

import java.util.List;

import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;

public interface IProcessor extends IInterfaceable, IBufferable {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();
	
	public static double maxStat(NCRecipes.Type recipeType, int i) {
		double max = 1D;
		List<ProcessorRecipe> recipes = recipeType.getRecipeHandler().getRecipes();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null || recipe.extras().size() <= i) continue;
			else if (recipe.extras().get(i) instanceof Double) max = Math.max(max, (double) recipe.extras().get(i));
		}
		return max;
	}
	
	public static double maxBaseProcessTime(NCRecipes.Type recipeType, int defaultProcessTime) {
		return maxStat(recipeType, 0)*defaultProcessTime;
	}
	
	public static double maxBaseProcessPower(NCRecipes.Type recipeType, int defaultProcessPower) {
		return maxStat(recipeType, 1)*defaultProcessPower;
	}
	
	public static int getCapacity(NCRecipes.Type recipeType, int defaultProcessTime, double speedMultiplier, int defaultProcessPower, double powerMultiplier) {
		return Math.max(1, (int) Math.round(Math.ceil(maxBaseProcessTime(recipeType, defaultProcessTime)/speedMultiplier)))*Math.min(Integer.MAX_VALUE, (int) (maxBaseProcessPower(recipeType, defaultProcessPower)*powerMultiplier));
	}
}
