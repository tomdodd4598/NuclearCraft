package nc.tile.processor;

import java.util.List;

import nc.recipe.*;
import nc.tile.dummy.IInterfaceable;
import nc.util.NCMath;
import net.minecraft.util.ITickable;

public abstract interface IProcessor extends ITickable, IInterfaceable {
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();
	
	public static double maxStat(ProcessorRecipeHandler recipeHandler, int extraIndex) {
		double max = 1D;
		List<ProcessorRecipe> recipes = recipeHandler.getRecipeList();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null || recipe.getExtras().size() <= extraIndex) {
				continue;
			}
			else if (recipe.getExtras().get(extraIndex) instanceof Double) {
				max = Math.max(max, (double) recipe.getExtras().get(extraIndex));
			}
		}
		return max;
	}
	
	public static double maxBaseProcessTime(ProcessorRecipeHandler recipeHandler, int defaultProcessTime) {
		return Math.ceil(maxStat(recipeHandler, 0) * defaultProcessTime);
	}
	
	public static double maxBaseProcessPower(ProcessorRecipeHandler recipeHandler, int defaultProcessPower) {
		return Math.ceil(maxStat(recipeHandler, 1) * defaultProcessPower);
	}
	
	public static int getCapacity(ProcessorRecipeHandler recipeHandler, int defaultProcessTime, double speedMultiplier, int defaultProcessPower, double powerMultiplier) {
		return NCMath.toInt(Math.ceil(maxBaseProcessTime(recipeHandler, defaultProcessTime) / speedMultiplier) * Math.ceil(maxBaseProcessPower(recipeHandler, defaultProcessPower) * powerMultiplier));
	}
}
