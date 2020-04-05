package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import crafttweaker.annotations.ZenRegister;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import nc.Global;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.integration.gtce.GTCERecipeHelper;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.util.NCMath;
import nc.util.NCUtil;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.nuclearcraft.ProcessorRecipeHandler")
@ZenRegister
public abstract class ProcessorRecipeHandler extends AbstractRecipeHandler<ProcessorRecipe> {
	
	private final String recipeName;
	protected final int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	protected final boolean isShapeless;
	
	public ProcessorRecipeHandler(@Nonnull String recipeName, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
		this(recipeName, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, true);
	}
	
	public ProcessorRecipeHandler(@Nonnull String recipeName, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, boolean isShapeless) {
		this.recipeName = recipeName;
		this.itemInputSize = itemInputSize;
		this.fluidInputSize = fluidInputSize;
		this.itemOutputSize = itemOutputSize;
		this.fluidOutputSize = fluidOutputSize;
		this.isShapeless = isShapeless;
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
		ProcessorRecipe recipe = buildRecipe(itemInputs, fluidInputs, itemOutputs, fluidOutputs, fixExtras(extras), isShapeless);
		
		if (ModCheck.gregtechLoaded() && GTCE_INTEGRATION.getBoolean(recipeName) && recipe != null) {
			GTCERecipeHelper.addGTCERecipe(recipeName, recipe);
		}
		
		addRecipe(NCConfig.factor_recipes ? factorRecipe(recipe) : recipe);
	}
	
	public abstract List fixExtras(List extras);
	
	public ProcessorRecipe factorRecipe(ProcessorRecipe recipe) {
		if (recipe == null) return null;
		if (recipe.getItemIngredients().size() != 0 || recipe.getItemProducts().size() != 0) {
			return recipe;
		}
		
		IntList stackSizes = new IntArrayList();
		for (IFluidIngredient ingredient : recipe.getFluidIngredients()) {
			stackSizes.addAll(ingredient.getFactors());
		}
		for (IFluidIngredient ingredient : recipe.getFluidProducts()) {
			stackSizes.addAll(ingredient.getFactors());
		}
		stackSizes.addAll(getExtraFactors(recipe.getExtras()));
		
		int hcf = NCMath.hcf(stackSizes.toIntArray());
		if (hcf == 1) return recipe;
		
		List<IFluidIngredient> fluidIngredients = new ArrayList<>(), fluidProducts = new ArrayList<>();
		
		for (IFluidIngredient ingredient : recipe.getFluidIngredients()) {
			fluidIngredients.add(ingredient.getFactoredIngredient(hcf));
		}
		for (IFluidIngredient ingredient : recipe.getFluidProducts()) {
			fluidProducts.add(ingredient.getFactoredIngredient(hcf));
		}
		
		return new ProcessorRecipe(recipe.getItemIngredients(), fluidIngredients, recipe.getItemProducts(), fluidProducts, getFactoredExtras(recipe.getExtras(), hcf), recipe.isShapeless());
	}
	
	public IntList getExtraFactors(List extras) {
		return new IntArrayList();
	}
	
	public List getFactoredExtras(List extras, int factor) {
		return extras;
	}
	
	private static final Object2BooleanMap<String> GTCE_INTEGRATION = new Object2BooleanOpenHashMap<>();
	
	public static void initGTCEIntegration() {
		boolean[] arr = NCConfig.gtce_recipe_integration;
		GTCE_INTEGRATION.put("manufactory", arr[0]);
		GTCE_INTEGRATION.put("separator", arr[1]);
		GTCE_INTEGRATION.put("decay_hastener", arr[2]);
		GTCE_INTEGRATION.put("fuel_reprocessor", arr[3]);
		GTCE_INTEGRATION.put("alloy_furnace", arr[4]);
		GTCE_INTEGRATION.put("infuser", arr[5]);
		GTCE_INTEGRATION.put("melter", arr[6]);
		GTCE_INTEGRATION.put("supercooler", arr[7]);
		GTCE_INTEGRATION.put("electrolyzer", arr[8]);
		GTCE_INTEGRATION.put("assembler", arr[9]);
		GTCE_INTEGRATION.put("ingot_former", arr[10]);
		GTCE_INTEGRATION.put("pressurizer", arr[11]);
		GTCE_INTEGRATION.put("chemical_reactor", arr[12]);
		GTCE_INTEGRATION.put("salt_mixer", arr[13]);
		GTCE_INTEGRATION.put("crystallizer", arr[14]);
		GTCE_INTEGRATION.put("enricher", arr[15]);
		GTCE_INTEGRATION.put("extractor", arr[16]);
		GTCE_INTEGRATION.put("centrifuge", arr[17]);
		GTCE_INTEGRATION.put("rock_crusher", arr[18]);
	}
	
	@Nullable
	public ProcessorRecipe buildRecipe(List itemInputs, List fluidInputs, List itemOutputs, List fluidOutputs, List extras, boolean shapeless) {
		List<IItemIngredient> itemIngredients = new ArrayList<>(), itemProducts = new ArrayList<>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<>(), fluidProducts = new ArrayList<>();
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
			NCUtil.getLogger().info(getRecipeName() + " - a recipe failed to be registered: " + RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
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
	
	@Override
	@ZenMethod
	public List<ProcessorRecipe> getRecipeList() {
		return recipeList;
	}
	
	@ZenMethod
	public int getItemInputSize() {
		return itemInputSize;
	}
	
	@ZenMethod
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@ZenMethod
	public int getItemOutputSize() {
		return itemOutputSize;
	}
	
	@ZenMethod
	public int getFluidOutputSize() {
		return fluidOutputSize;
	}
	
	@ZenMethod
	public boolean isShapeless() {
		return isShapeless;
	}
}
