package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.integration.gtce.GTCERecipeHelper;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.util.NCUtil;

public abstract class ProcessorRecipeHandler extends AbstractRecipeHandler<ProcessorRecipe> {
	
	public final int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	public final boolean shapeless;
	private final String recipeName;
	
	public ProcessorRecipeHandler(String recipeName, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
		this(recipeName, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, true);
	}
	
	public ProcessorRecipeHandler(String recipeName, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, boolean shapeless) {
		this.itemInputSize = itemInputSize;
		this.fluidInputSize = fluidInputSize;
		this.itemOutputSize = itemOutputSize;
		this.fluidOutputSize = fluidOutputSize;
		this.shapeless = shapeless;
		this.recipeName = recipeName;
		addRecipes();
	}
	
	@Override
	public void addRecipe(Object... objects) {
		List itemInputs = new ArrayList(), fluidInputs = new ArrayList(), itemOutputs = new ArrayList(), fluidOutputs = new ArrayList(), extras = new ArrayList();
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if (i < itemInputSize) {
				itemInputs.add(object);
			} else if (i < itemInputSize + fluidInputSize) {
				fluidInputs.add(object);
			} else if (i < itemInputSize + fluidInputSize + itemOutputSize) {
				itemOutputs.add(object);
			} else if (i < itemInputSize + fluidInputSize + itemOutputSize + fluidOutputSize) {
				fluidOutputs.add(object);
			} else {
				extras.add(object);
			}
		}
		ProcessorRecipe recipe = buildRecipe(itemInputs, fluidInputs, itemOutputs, fluidOutputs, extras, shapeless);
		addRecipe(recipe);
		
		if (ModCheck.gregtechLoaded() && NCConfig.gtce_recipes && recipe != null) {
			GTCERecipeHelper.addGTCERecipe(recipeName, recipe);
		}
	}
	
	@Nullable
	public ProcessorRecipe buildRecipe(List itemInputs, List fluidInputs, List itemOutputs, List fluidOutputs, List extras, boolean shapeless) {
		List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>(), itemProducts = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>(), fluidProducts = new ArrayList<IFluidIngredient>();
		for (Object obj : itemInputs) {
			if (obj != null && isValidItemInputType(obj)) {
				IItemIngredient input = RecipeHelper.buildItemIngredient(obj);
				if (input == null) return null;
				itemIngredients.add(input);
			} else return null;
		}
		for (Object obj : fluidInputs) {
			if (obj != null && isValidFluidInputType(obj)) {
				IFluidIngredient input = RecipeHelper.buildFluidIngredient(obj);
				if (input == null) return null;
				fluidIngredients.add(input);
			} else return null;
		}
		for (Object obj : itemOutputs) {
			if (obj != null && isValidItemOutputType(obj)) {
				IItemIngredient output = RecipeHelper.buildItemIngredient(obj);
				if (output == null) return null;
				itemProducts.add(output);
			} else return null;
		}
		for (Object obj : fluidOutputs) {
			if (obj != null && isValidFluidOutputType(obj)) {
				IFluidIngredient output = RecipeHelper.buildFluidIngredient(obj);
				if (output == null) return null;
				fluidProducts.add(output);
			} else return null;
		}
		if (!isValidRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts)) {
			NCUtil.getLogger().info(getRecipeName() + " - a recipe was removed: " + RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
		}
		return new ProcessorRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
	}
	
	public boolean isValidRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		return itemIngredients.size() == itemInputSize && fluidIngredients.size() == fluidInputSize && itemProducts.size() == itemOutputSize && fluidProducts.size() == fluidOutputSize;
	}
	
	@Override
	public String getRecipeName() {
		return Global.MOD_ID + "_" + recipeName;
	}
}
