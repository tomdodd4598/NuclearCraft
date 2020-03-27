package nc.tile.processor;

import java.util.List;

import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;

public abstract interface IProcessor extends IInterfaceable, IBufferable {
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();
	
	public static double maxStat(ProcessorRecipeHandler recipeHandler, int extraIndex) {
		double max = 1D;
		List<ProcessorRecipe> recipes = recipeHandler.getRecipeList();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null || recipe.getExtras().size() <= extraIndex) continue;
			else if (recipe.getExtras().get(extraIndex) instanceof Double) max = Math.max(max, (double) recipe.getExtras().get(extraIndex));
		}
		return max;
	}
	
	public static double maxBaseProcessTime(ProcessorRecipeHandler recipeHandler, int defaultProcessTime) {
		return maxStat(recipeHandler, 0)*defaultProcessTime;
	}
	
	public static double maxBaseProcessPower(ProcessorRecipeHandler recipeHandler, int defaultProcessPower) {
		return maxStat(recipeHandler, 1)*defaultProcessPower;
	}
	
	public static int getCapacity(ProcessorRecipeHandler recipeHandler, int defaultProcessTime, double speedMultiplier, int defaultProcessPower, double powerMultiplier) {
		return Math.max(1, (int) Math.round(Math.ceil(maxBaseProcessTime(recipeHandler, defaultProcessTime)/speedMultiplier)))*Math.min(Integer.MAX_VALUE, (int) (maxBaseProcessPower(recipeHandler, defaultProcessPower)*powerMultiplier));
	}
}
