package nc.tile.processor;

import java.util.List;

import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank;

public interface IFluidProcessor {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ProcessorRecipe getRecipe();
	
	public List<Tank> getFluidInputs();
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();
	
	public List<Integer> getFluidInputOrder();

}
