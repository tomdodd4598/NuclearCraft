package nc.recipe;

import java.util.List;

import nc.tile.internal.fluid.Tank;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;

public class ProcessorRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;
	
	private int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	
	public List extras;
	public boolean isShapeless;
	
	public ProcessorRecipe(List<IItemIngredient> itemIngredientsList, List<IFluidIngredient> fluidIngredientsList, List<IItemIngredient> itemProductsList, List<IFluidIngredient> fluidProductsList, List extrasList, boolean shapeless) {
		itemIngredients = itemIngredientsList;
		fluidIngredients = fluidIngredientsList;
		itemProducts = itemProductsList;
		fluidProducts = fluidProductsList;
		
		itemInputSize = itemIngredientsList.size();
		fluidInputSize = fluidIngredientsList.size();
		itemOutputSize = itemProductsList.size();
		fluidOutputSize = fluidProductsList.size();
		
		extras = extrasList;
		isShapeless = shapeless;
	}
	
	@Override
	public List<IItemIngredient> itemIngredients() {
		return itemIngredients;
	}
	
	@Override
	public List<IFluidIngredient> fluidIngredients() {
		return fluidIngredients;
	}
	
	@Override
	public List<IItemIngredient> itemProducts() {
		return itemProducts;
	}
	
	@Override
	public List<IFluidIngredient> fluidProducts() {
		return fluidProducts;
	}
	
	@Override
	public List extras() {
		return extras;
	}

	@Override
	public boolean matchingInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		return RecipeHelper.matchingIngredients(SorptionType.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs, isShapeless);
	}

	@Override
	public boolean matchingOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		return RecipeHelper.matchingIngredients(SorptionType.OUTPUT, itemProducts, fluidProducts, itemOutputs, fluidOutputs, isShapeless);
	}
	
	@Override
	public boolean matchingIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients) {
		return RecipeHelper.matchingIngredients(SorptionType.INPUT, this.itemIngredients, this.fluidIngredients, itemIngredients, fluidIngredients, isShapeless);
	}

	@Override
	public boolean matchingProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		return RecipeHelper.matchingIngredients(SorptionType.OUTPUT, this.itemProducts, this.fluidProducts, itemProducts, fluidProducts, isShapeless);
	}
}
