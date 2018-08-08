package nc.recipe;

import java.util.List;

import nc.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;

public interface IRecipe {
	
	public List<IItemIngredient> itemIngredients();
	
	public List<IFluidIngredient> fluidIngredients();
	
	public List<IItemIngredient> itemProducts();
	
	public List<IFluidIngredient> fluidProducts();
	
	public List extras();

	public boolean matchingInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs);
	
	public boolean matchingOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs);
	
	public boolean matchingIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients);
	
	public boolean matchingProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts);
}
