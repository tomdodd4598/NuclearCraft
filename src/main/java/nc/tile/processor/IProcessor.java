package nc.tile.processor;

import nc.recipe.RecipeStats;
import nc.tile.dummy.IInterfaceable;
import nc.util.NCMath;
import net.minecraft.util.ITickable;

public abstract interface IProcessor extends ITickable, IInterfaceable {
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();
	
	public static int getCapacity(int processorID, double speedMultiplier, double powerMultiplier) {
		return NCMath.toInt(Math.ceil(RecipeStats.getProcessorMaxBaseProcessTime(processorID) / speedMultiplier) * Math.ceil(RecipeStats.getProcessorMaxBaseProcessPower(processorID) * powerMultiplier));
	}
}
