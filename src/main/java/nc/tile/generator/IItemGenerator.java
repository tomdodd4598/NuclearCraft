package nc.tile.generator;

import java.util.List;

import nc.recipe.ingredient.IItemIngredient;
import nc.tile.processor.IProcessor;
import net.minecraft.item.ItemStack;

public interface IItemGenerator extends IProcessor {
	
	public List<ItemStack> getItemInputs(boolean consumed);
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
	
	public List<Integer> getItemInputOrder();
}
