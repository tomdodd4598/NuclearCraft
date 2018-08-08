package nc.tile.generator;

import java.util.List;

import nc.recipe.IItemIngredient;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.item.ItemStack;

public interface IItemGenerator {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ProcessorRecipe getRecipe();
	
	public List<ItemStack> getItemInputs(boolean consumed);
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
	
	public List<Integer> getItemInputOrder();
}
