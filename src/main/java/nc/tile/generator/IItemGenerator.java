package nc.tile.generator;

import java.util.List;

import nc.recipe.ingredient.IItemIngredient;
import nc.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;

public interface IItemGenerator extends IGenerator, ITileInventory {
	
	public int getItemInputSize();
	
	public int getItemOutputSize();
	
	public List<ItemStack> getItemInputs(boolean consumed);
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
}
