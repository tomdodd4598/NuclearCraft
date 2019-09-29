package nc.tile.processor;

import java.util.List;

import nc.recipe.ingredient.IItemIngredient;
import net.minecraft.item.ItemStack;

public interface IItemProcessor extends IProcessor {
	
	public List<ItemStack> getItemInputs();
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
}
