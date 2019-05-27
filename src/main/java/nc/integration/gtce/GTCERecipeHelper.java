package nc.integration.gtce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.items.MetaItems;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

public class GTCERecipeHelper {
	
	// Thanks so much to Firew0lf for the original method!
	@Optional.Method(modid = "gregtech")
	public static void addGTCERecipe(String recipeName, ProcessorRecipe recipe) {
		RecipeMap<SimpleRecipeBuilder> recipeMap = null;
		SimpleRecipeBuilder builder = null;
		
		switch (recipeName) {
		case "manufactory":
			recipeMap = RecipeMaps.MACERATOR_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 12, 8);
			break;
		case "isotope_separator":
			recipeMap = RecipeMaps.THERMAL_CENTRIFUGE_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 48, 160);
			break;
		case "decay_hastener":
			return;
		case "fuel_reprocessor":
			recipeMap = RecipeMaps.CENTRIFUGE_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 24, 60);
			break;
		case "alloy_furnace":
			recipeMap = RecipeMaps.ALLOY_SMELTER_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 10);
			break;
		case "infuser":
			recipeMap = RecipeMaps.CHEMICAL_BATH_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 10);
			break;
		case "melter":
			recipeMap = RecipeMaps.FLUID_EXTRACTION_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 32, 16);
			break;
		case "supercooler":
			recipeMap = RecipeMaps.VACUUM_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 240, 20);
			break;
		case "electrolyser":
			recipeMap = RecipeMaps.ELECTROLYZER_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 16);
			break;
		case "irradiator":
			return;
		case "ingot_former":
			recipeMap = RecipeMaps.FLUID_SOLIDFICATION_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 8, 1);
			break;
		case "pressurizer":
			recipeMap = RecipeMaps.COMPRESSOR_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 2, 20);
			break;
		case "chemical_reactor":
			recipeMap = RecipeMaps.CHEMICAL_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 30);
			break;
		case "salt_mixer":
			recipeMap = RecipeMaps.MIXER_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 8, 12);
			break;
		case "crystallizer":
			recipeMap = RecipeMaps.CHEMICAL_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 10);
			break;
		case "dissolver":
			recipeMap = RecipeMaps.CHEMICAL_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 20, 20);
			break;
		case "extractor":
			recipeMap = RecipeMaps.FLUID_EXTRACTION_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 12);
			break;
		case "centrifuge":
			recipeMap = RecipeMaps.CENTRIFUGE_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 80);
			break;
		case "rock_crusher":
			recipeMap = RecipeMaps.MACERATOR_RECIPES;
			builder = addStats(recipeMap.recipeBuilder(), recipe, 20, 12);
			return;
		}
		
		if (recipeMap == null || builder == null) {
			return;
		}
		
		List<List<ItemStack>> itemInputLists = new ArrayList<List<ItemStack>>();
		List<List<FluidStack>> fluidInputLists = new ArrayList<List<FluidStack>>();
		
		for (IItemIngredient item : recipe.itemIngredients()) itemInputLists.add(item.getInputStackList());
		for (IFluidIngredient fluid : recipe.fluidIngredients()) fluidInputLists.add(fluid.getInputStackList());
		
		List<Pair<List<ItemStack>, List<FluidStack>>> ingredientListTuples = new ArrayList<Pair<List<ItemStack>, List<FluidStack>>>();
		
		int[] inputNumbers = new int[recipe.itemIngredients().size() + recipe.fluidIngredients().size()];
		Arrays.fill(inputNumbers, 0);
		generateIngredientListTuples(ingredientListTuples, inputNumbers, 0, itemInputLists, fluidInputLists);
		
		for (Pair<List<ItemStack>, List<FluidStack>> ingredients : ingredientListTuples) {
			if (findRecipe(recipeMap, ingredients.getLeft(), ingredients.getRight()) != null) {
				return;
			}
		}
		
		List<SimpleRecipeBuilder> builders = new ArrayList<SimpleRecipeBuilder>(); // Holds all the recipe variants
		builders.add(builder);
		
		for (IItemIngredient input : recipe.itemIngredients()) {
			if (input instanceof OreIngredient) {
				for (SimpleRecipeBuilder builderVariant : builders) {
					builderVariant.input(((OreIngredient)input).oreName, ((OreIngredient)input).stackSize);
				}
			}
			else {
				List<String> ingredientOreList = new ArrayList<String>(); // Hold the different oreDict names
				List<SimpleRecipeBuilder> newBuilders = new ArrayList<SimpleRecipeBuilder>();
				for (ItemStack inputVariant : input.getInputStackList()) {
					if(inputVariant.isEmpty()) continue;
					Set<String> variantOreList = OreDictHelper.getOreNames(inputVariant);
					
					if (!variantOreList.isEmpty()) { // This variant has oreDict entries
						if (ingredientOreList.containsAll(variantOreList)) {
							continue;
						}
						ingredientOreList.addAll(variantOreList);
						
						for (SimpleRecipeBuilder recipeBuilder : builders) {
							newBuilders.add(recipeBuilder.copy().input(variantOreList.iterator().next(), inputVariant.getCount()));
						}
					}
					else {
						for (SimpleRecipeBuilder recipeBuilder : builders) {
							newBuilders.add(recipeBuilder.copy().inputs(inputVariant));
						}
					}
				}
				builders = newBuilders;
			}
		}
		
		if (recipeName.equals("ingot_former")) {
			for (SimpleRecipeBuilder builderVariant : builders) {
				builderVariant.notConsumable(MetaItems.SHAPE_MOLD_INGOT);
			}
		}
		
		for (IFluidIngredient input : recipe.fluidIngredients()) {
			if (input.getInputStackList().isEmpty()) continue;
			for (SimpleRecipeBuilder builderVariant : builders) {
				builderVariant.fluidInputs(input.getInputStackList().get(0));
			}
		}
		
		for (IItemIngredient output : recipe.itemProducts()) {
			List<ItemStack> outputStackList = output.getOutputStackList();
			if (outputStackList.isEmpty()) continue;
			for (SimpleRecipeBuilder builderVariant : builders) {
				builderVariant = builderVariant.outputs(outputStackList.get(0));
			}
		}
		
		for (IFluidIngredient output : recipe.fluidProducts()) {
			if (output.getOutputStackList().isEmpty()) continue;
			for (SimpleRecipeBuilder builderVariant : builders) {
				builderVariant.fluidOutputs(output.getOutputStackList().get(0));
			}
		}
		
		for (SimpleRecipeBuilder builderVariant : builders) {
			if(!builderVariant.getInputs().isEmpty()) builderVariant.buildAndRegister();
		}
	}
	
	@Optional.Method(modid = "gregtech")
	private static SimpleRecipeBuilder addStats(SimpleRecipeBuilder builder, ProcessorRecipe recipe, int processPower, int processTime) {
		return builder.EUt(Math.max((int) recipe.getBaseProcessPower(processPower), 8)).duration((int) recipe.getBaseProcessTime(20D*processTime));
	}
	
	@Optional.Method(modid = "gregtech")
	private static void generateIngredientListTuples(List<Pair<List<ItemStack>, List<FluidStack>>> tuples, int[] inputNumbers, int activeIndex, List<List<ItemStack>> itemInputLists, List<List<FluidStack>> fluidInputLists) {
		int itemInputSize = itemInputLists.size(), fluidInputSize = fluidInputLists.size();
		
		List<ItemStack> itemInputs = new ArrayList<ItemStack>();
		List<FluidStack> fluidInputs = new ArrayList<FluidStack>();
		
		for (int i = 0; i < itemInputSize; i++) {
			if (itemInputLists.get(i).size() > inputNumbers[i]) {
				itemInputs.add(itemInputLists.get(i).get(inputNumbers[i]));
			}
			else {
				if (activeIndex == i) {
					activeIndex++;
				}
				inputNumbers[i] = -1;
			}
		}
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (fluidInputLists.get(i).size() > inputNumbers[i + itemInputSize]) {
				fluidInputs.add(fluidInputLists.get(i).get(inputNumbers[i + itemInputSize]));
			}
			else {
				if (activeIndex == i + itemInputSize) {
					activeIndex++;
				}
				inputNumbers[i + itemInputSize] = -1;
			}
		}
		
		if (itemInputs.size() == itemInputSize && fluidInputs.size() == fluidInputSize) {
			tuples.add(Pair.of(itemInputs, fluidInputs));
		}
		
		if (activeIndex < itemInputSize + fluidInputSize) {
			for (int i = 0; i <= activeIndex; i++) {
				inputNumbers[i]++;
			}
			generateIngredientListTuples(tuples, inputNumbers, activeIndex, itemInputLists, fluidInputLists);
		}
	}
	
	// GTCE recipe matching - modified from GTCE source
	
	@Optional.Method(modid = "gregtech")
	private static Recipe findRecipe(RecipeMap<SimpleRecipeBuilder> recipeMap, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		if (recipeMap.getRecipeList().isEmpty())
			return null;
		if (recipeMap.getMinFluidInputs() > 0 && GTUtility.amountOfNonNullElements(fluidInputs) < recipeMap.getMinFluidInputs()) {
			return null;
		}
		if (recipeMap.getMinInputs() > 0 && GTUtility.amountOfNonEmptyStacks(inputs) < recipeMap.getMinInputs()) {
			return null;
		}
		if (recipeMap.getMaxInputs() > 0) {
			return findByInputs(recipeMap, inputs, fluidInputs);
		}
		else {
			return findByFluidInputs(recipeMap, inputs, fluidInputs);
		}
	}
	
	@Optional.Method(modid = "gregtech")
	private static Recipe findByFluidInputs(RecipeMap<SimpleRecipeBuilder> recipeMap, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		for (FluidStack fluid : fluidInputs) {
			if (fluid == null) continue;
			Collection<Recipe> recipes = recipeMap.getRecipesForFluid(fluid);
			if (recipes == null) continue;
			for (Recipe recipe : recipes) {
				if (recipeMatches(recipe, inputs, fluidInputs)) {
					return recipe;
				}
			}
		}
		return null;
	}
	
	@Optional.Method(modid = "gregtech")
	private static Recipe findByInputs(RecipeMap<SimpleRecipeBuilder> recipeMap, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		for (Recipe recipe : recipeMap.getRecipeList()) {
			if (recipeMatches(recipe, inputs, fluidInputs)) {
				return recipe;
			}
		}
		return null;
	}
	
	@Optional.Method(modid = "gregtech")
	private static boolean recipeMatches(Recipe recipe, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		fluidLoop : for (FluidStack fluid : recipe.getFluidInputs()) {
			for (FluidStack input : fluidInputs) {
				if (input != null && input.isFluidEqual(fluid)) {
					continue fluidLoop;
				}
			}
			return false;
		}
		itemLoop : for (CountableIngredient ingredient : recipe.getInputs()) {
			for (ItemStack input : inputs) {
				if (!input.isEmpty() && ingredient.getIngredient().apply(input)) {
					continue itemLoop;
				}
			}
			return false;
		}
		return true;
	}
}
