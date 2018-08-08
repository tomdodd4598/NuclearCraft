package nc.tile.generator;

import java.util.List;

import nc.recipe.IFluidIngredient;
import nc.recipe.IItemIngredient;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;

public interface IItemFluidGenerator {
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ProcessorRecipe getRecipe();
	
	public List<ItemStack> getItemInputs(boolean consumed);
	
	public List<Tank> getFluidInputs(boolean consumed);

	public List<IItemIngredient> getItemIngredients();
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IItemIngredient> getItemProducts();
	
	public List<IFluidIngredient> getFluidProducts();
	
	public List<Integer> getItemInputOrder();
	
	public List<Integer> getFluidInputOrder();

}
