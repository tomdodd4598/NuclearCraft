package nc.tile.processor;

import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;

public interface IProcessor extends IInterfaceable, IBufferable {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ProcessorRecipe getRecipe();
	
	public void refreshRecipe();
	
	public void refreshActivity();
}
