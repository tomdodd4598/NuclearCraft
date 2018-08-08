package nc.tile.generator;

import java.util.List;

import nc.recipe.IFluidIngredient;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.internal.fluid.Tank;

public interface IFluidGenerator {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ProcessorRecipe getRecipe();
	
	public List<Tank> getFluidInputs(boolean consumed);
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();
	
	public List<Integer> getFluidInputOrder();

}
