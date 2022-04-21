package nc.integration.gtce;

import static nc.config.NCConfig.gtce_recipe_logging;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.recipes.*;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.common.items.MetaItems;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

public class GTCERecipeHelper {
	
	// Thanks so much to Firew0lf for the original method!
	@Optional.Method(modid = "gregtech")
	public static void addGTCERecipe(String recipeName, BasicRecipe recipe) {
		RecipeMap<?> recipeMap = null;
		RecipeBuilder<?> builder = null;
		
		switch (recipeName) {
			case "manufactory":
				recipeMap = RecipeMaps.MACERATOR_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 12, 8);
				break;
			case "separator":
				recipeMap = RecipeMaps.THERMAL_CENTRIFUGE_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 48, 160);
				break;
			case "decay_hastener":
				return;
			case "fuel_reprocessor":
				return;
			case "alloy_furnace":
				recipeMap = RecipeMaps.ALLOY_SMELTER_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 10);
				break;
			case "infuser":
				recipeMap = RecipeMaps.CHEMICAL_RECIPES;
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
			case "electrolyzer":
				recipeMap = RecipeMaps.ELECTROLYZER_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 16);
				break;
			case "assembler":
				recipeMap = RecipeMaps.ASSEMBLER_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 4, 100);
				break;
			case "ingot_former":
				recipeMap = RecipeMaps.FLUID_SOLIDFICATION_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 8, 1);
				break;
			case "pressurizer":
				if (isPlateRecipe(recipe)) {
					recipeMap = RecipeMaps.BENDER_RECIPES;
					builder = addStats(recipeMap.recipeBuilder(), recipe, 24, 10).notConsumable(new IntCircuitIngredient(0));
				}
				else {
					recipeMap = RecipeMaps.COMPRESSOR_RECIPES;
					builder = addStats(recipeMap.recipeBuilder(), recipe, 2, 20);
				}
				break;
			case "chemical_reactor":
				recipeMap = RecipeMaps.CHEMICAL_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 30).notConsumable(new IntCircuitIngredient(0));
				break;
			case "salt_mixer":
				recipeMap = RecipeMaps.MIXER_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 8, 12);
				break;
			case "crystallizer":
				recipeMap = RecipeMaps.CHEMICAL_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 30, 10).notConsumable(new IntCircuitIngredient(1));
				break;
			case "enricher":
				recipeMap = RecipeMaps.CHEMICAL_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 20, 20).notConsumable(new IntCircuitIngredient(2));
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
			default:
				break;
		}
		
		if (recipeMap == null || builder == null) {
			return;
		}
		
		List<List<ItemStack>> itemInputLists = new ArrayList<>();
		List<List<FluidStack>> fluidInputLists = new ArrayList<>();
		
		for (IItemIngredient item : recipe.getItemIngredients()) {
			itemInputLists.add(item.getInputStackList());
		}
		for (IFluidIngredient fluid : recipe.getFluidIngredients()) {
			fluidInputLists.add(fluid.getInputStackList());
		}
		
		int arrSize = recipe.getItemIngredients().size() + recipe.getFluidIngredients().size();
		int[] inputNumbers = new int[arrSize];
		Arrays.fill(inputNumbers, 0);
		
		int[] maxNumbers = new int[arrSize];
		for (int i = 0; i < itemInputLists.size(); ++i) {
			int maxNumber = itemInputLists.get(i).size() - 1;
			if (maxNumber < 0) {
				return;
			}
			maxNumbers[i] = maxNumber;
		}
		for (int i = 0; i < fluidInputLists.size(); ++i) {
			int maxNumber = fluidInputLists.get(i).size() - 1;
			if (maxNumber < 0) {
				return;
			}
			maxNumbers[i + itemInputLists.size()] = maxNumber;
		}
		
		List<Pair<List<ItemStack>, List<FluidStack>>> materialListTuples = new ArrayList<>();
		
		RecipeTupleGenerator.INSTANCE.generateMaterialListTuples(materialListTuples, maxNumbers, inputNumbers, itemInputLists, fluidInputLists, true);
		
		for (Pair<List<ItemStack>, List<FluidStack>> materials : materialListTuples) {
			if (isRecipeInvalid(recipeMap, materials.getLeft(), materials.getRight())) {
				return;
			}
		}
		
		// Holds all the recipe variants
		List<RecipeBuilder<?>> builders = new ArrayList<>();
		builders.add(builder);
		
		for (IItemIngredient input : recipe.getItemIngredients()) {
			if (input instanceof OreIngredient) {
				for (RecipeBuilder<?> builderVariant : builders) {
					builderVariant.input(((OreIngredient) input).oreName, ((OreIngredient) input).stackSize);
				}
			}
			else {
				// Hold the different ore dict names
				List<String> ingredientOreList = new ArrayList<>();
				List<RecipeBuilder<?>> newBuilders = new ArrayList<>();
				for (ItemStack inputVariant : input.getInputStackList()) {
					if (inputVariant.isEmpty()) {
						continue;
					}
					Set<String> variantOreList = OreDictHelper.getOreNames(inputVariant);
					
					// This variant has ore dict entries
					if (!variantOreList.isEmpty()) {
						if (ingredientOreList.containsAll(variantOreList)) {
							continue;
						}
						ingredientOreList.addAll(variantOreList);
						
						for (RecipeBuilder<?> recipeBuilder : builders) {
							newBuilders.add(recipeBuilder.copy().input(variantOreList.iterator().next(), inputVariant.getCount()));
						}
					}
					else {
						for (RecipeBuilder<?> recipeBuilder : builders) {
							newBuilders.add(recipeBuilder.copy().inputs(inputVariant));
						}
					}
				}
				builders = newBuilders;
			}
		}
		
		if (recipeMap == RecipeMaps.FLUID_SOLIDFICATION_RECIPES) {
			MetaItem<?>.MetaValueItem mold = getIngotFormerMold(recipe);
			for (RecipeBuilder<?> builderVariant : builders) {
				builderVariant.notConsumable(mold);
			}
		}
		
		for (IFluidIngredient input : recipe.getFluidIngredients()) {
			if (input.getInputStackList().isEmpty()) {
				continue;
			}
			for (RecipeBuilder<?> builderVariant : builders) {
				builderVariant.fluidInputs(input.getInputStackList().get(0));
			}
		}
		
		for (IItemIngredient output : recipe.getItemProducts()) {
			if (output instanceof IChanceItemIngredient) {
				return;
			}
			List<ItemStack> outputStackList = output.getOutputStackList();
			if (outputStackList.isEmpty()) {
				continue;
			}
			for (RecipeBuilder<?> builderVariant : builders) {
				builderVariant = builderVariant.outputs(outputStackList.get(0));
			}
		}
		
		for (IFluidIngredient output : recipe.getFluidProducts()) {
			if (output instanceof IChanceFluidIngredient) {
				return;
			}
			List<FluidStack> outputStackList = output.getOutputStackList();
			if (outputStackList.isEmpty()) {
				continue;
			}
			for (RecipeBuilder<?> builderVariant : builders) {
				builderVariant.fluidOutputs(outputStackList.get(0));
			}
		}
		
		boolean built = false;
		for (RecipeBuilder<?> builderVariant : builders) {
			if (!builderVariant.getInputs().isEmpty() || !builderVariant.getFluidInputs().isEmpty()) {
				builderVariant.buildAndRegister();
				built = true;
			}
		}
		
		if (built && gtce_recipe_logging) {
			NCUtil.getLogger().info("Injected GTCE " + recipeMap.unlocalizedName + " recipe: " + RecipeHelper.getRecipeString(recipe));
		}
	}
	
	@Optional.Method(modid = "gregtech")
	private static RecipeBuilder<?> addStats(RecipeBuilder<?> builder, BasicRecipe recipe, int processPower, int processTime) {
		return builder.EUt(Math.max((int) recipe.getBaseProcessPower(processPower), 1)).duration((int) recipe.getBaseProcessTime(20D * processTime));
	}
	
	// GTCE recipe matching - modified from GTCE source
	
	@Optional.Method(modid = "gregtech")
	private static boolean isRecipeInvalid(RecipeMap<?> recipeMap, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		if (fluidInputs.size() < recipeMap.getMinFluidInputs() || fluidInputs.size() > recipeMap.getMaxFluidInputs()) {
			return true;
		}
		else if (inputs.size() < recipeMap.getMinInputs() || inputs.size() > recipeMap.getMaxInputs()) {
			return true;
		}
		
		if (recipeMap.getMaxInputs() > 0) {
			return findConflictByInputs(recipeMap, inputs, fluidInputs);
		}
		else {
			return findConflictByFluidInputs(recipeMap, inputs, fluidInputs);
		}
	}
	
	@Optional.Method(modid = "gregtech")
	private static boolean findConflictByFluidInputs(RecipeMap<?> recipeMap, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		for (FluidStack fluid : fluidInputs) {
			if (fluid == null) {
				continue;
			}
			Collection<Recipe> recipes = recipeMap.getRecipesForFluid(fluid);
			if (recipes == null) {
				continue;
			}
			for (Recipe recipe : recipes) {
				if (isRecipeConflict(recipe, inputs, fluidInputs)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Optional.Method(modid = "gregtech")
	private static boolean findConflictByInputs(RecipeMap<?> recipeMap, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		for (Recipe recipe : recipeMap.getRecipeList()) {
			if (isRecipeConflict(recipe, inputs, fluidInputs)) {
				return true;
			}
		}
		return false;
	}
	
	@Optional.Method(modid = "gregtech")
	private static boolean isRecipeConflict(Recipe recipe, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
		itemLoop: for (ItemStack input : inputs) {
			for (CountableIngredient ingredient : recipe.getInputs()) {
				if (ingredient.getIngredient().apply(input)) {
					continue itemLoop;
				}
			}
			return false;
		}
		fluidLoop: for (FluidStack input : fluidInputs) {
			for (FluidStack fluid : recipe.getFluidInputs()) {
				if (input.isFluidEqual(fluid)) {
					continue fluidLoop;
				}
			}
			return false;
		}
		return true;
	}
	
	private static boolean isPlateRecipe(BasicRecipe recipe) {
		ItemStack output = recipe.getItemProducts().get(0).getStack();
		return output != null && OreDictHelper.hasOrePrefix(output, "plate", "plateDense");
	}
	
	@Optional.Method(modid = "gregtech")
	private static MetaItem<?>.MetaValueItem getIngotFormerMold(BasicRecipe recipe) {
		ItemStack output = recipe.getItemProducts().get(0).getStack();
		if (output != null) {
			if (OreDictHelper.hasOrePrefix(output, "ingot")) {
				return MetaItems.SHAPE_MOLD_INGOT;
			}
			else if (OreDictHelper.hasOrePrefix(output, "block")) {
				return MetaItems.SHAPE_MOLD_BLOCK;
			}
		}
		return MetaItems.SHAPE_MOLD_BALL;
	}
}
