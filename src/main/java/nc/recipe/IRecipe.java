package nc.recipe;

import java.util.List;

import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;

public interface IRecipe {
	
	public List<IItemIngredient> itemIngredients();
	
	public List<IFluidIngredient> fluidIngredients();
	
	public List<IItemIngredient> itemProducts();
	
	public List<IFluidIngredient> fluidProducts();
	
	public List extras();

	public RecipeMatchResult matchInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs);
	
	public RecipeMatchResult matchOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs);
	
	public RecipeMatchResult matchIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients);
	
	public RecipeMatchResult matchProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts);
}
