package nc.integration.gtce;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.recipes.*;
import gregtech.api.recipes.ingredients.*;
import gregtech.common.items.MetaItems;
import nc.config.NCConfig;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

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
				recipeMap = RecipeMaps.EXTRACTOR_RECIPES;
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
				recipeMap = RecipeMaps.EXTRACTOR_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 12);
				break;
			case "centrifuge":
				recipeMap = RecipeMaps.CENTRIFUGE_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 16, 80);
				break;
			case "rock_crusher":
				recipeMap = RecipeMaps.MACERATOR_RECIPES;
				builder = addStats(recipeMap.recipeBuilder(), recipe, 20, 12);
				break;
			case "electric_furnace":
				return;
			default:
				break;
		}
		
		if (builder == null) {
			return;
		}
		
		List<List<ItemStack>> itemInputLists = StreamHelper.map(recipe.getItemIngredients(), IItemIngredient::getInputStackList);
		List<List<FluidStack>> fluidInputLists = StreamHelper.map(recipe.getFluidIngredients(), IFluidIngredient::getInputStackList);
		
		int itemInputCount = itemInputLists.size(), fluidInputCount = fluidInputLists.size(), totalInputCount = itemInputCount + fluidInputCount;
		int[] inputNumbers = new int[totalInputCount];
		
		int[] maxNumbers = new int[totalInputCount];
		for (int i = 0; i < itemInputCount; ++i) {
			int maxNumber = itemInputLists.get(i).size() - 1;
			if (maxNumber < 0) {
				return;
			}
			maxNumbers[i] = maxNumber;
		}
		for (int i = 0; i < fluidInputCount; ++i) {
			int maxNumber = fluidInputLists.get(i).size() - 1;
			if (maxNumber < 0) {
				return;
			}
			maxNumbers[i + itemInputCount] = maxNumber;
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
			if (input instanceof OreIngredient oreInput) {
				for (RecipeBuilder<?> builderVariant : builders) {
					builderVariant.input(oreInput.oreName, oreInput.stackSize);
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
						if (new HashSet<>(ingredientOreList).containsAll(variantOreList)) {
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
		
		if (built && NCConfig.gtce_recipe_logging) {
			NCUtil.getLogger().info("Injected GTCEu " + recipeMap.unlocalizedName + " recipe: " + RecipeHelper.getRecipeString(recipe));
		}
	}
	
	@Optional.Method(modid = "gregtech")
	private static RecipeBuilder<?> addStats(RecipeBuilder<?> builder, BasicRecipe recipe, int processPower, int processTime) {
		return builder.EUt(NCMath.toInt(Math.max(recipe.getBaseProcessPower(processPower), 1D))).duration(NCMath.toInt(recipe.getBaseProcessTime(20D * processTime)));
	}
	
	// GTCE recipe matching - modified from GTCE source
	
	@Optional.Method(modid = "gregtech")
	private static boolean isRecipeInvalid(RecipeMap<?> recipeMap, List<ItemStack> itemInputs, List<FluidStack> fluidInputs) {
		int itemInputCount = itemInputs.size(), fluidInputCount = fluidInputs.size();
		
		if (itemInputCount > recipeMap.getMaxInputs() || fluidInputCount > recipeMap.getMaxFluidInputs() || itemInputCount + fluidInputCount < 1) {
			return true;
		}
		
		return findRecipeInputConflict(recipeMap, itemInputs, fluidInputs);
	}
	
	@Optional.Method(modid = "gregtech")
	private static boolean findRecipeInputConflict(RecipeMap<?> recipeMap, List<ItemStack> itemInputs, List<FluidStack> fluidInputs) {
		for (Recipe recipe : recipeMap.getRecipeList()) {
			if (isRecipeInputConflict(recipe, itemInputs, fluidInputs)) {
				return true;
			}
		}
		return false;
	}
	
	@Optional.Method(modid = "gregtech")
	private static boolean isRecipeInputConflict(Recipe recipe, List<ItemStack> itemInputs, List<FluidStack> fluidInputs) {
		itemLoop:
		for (ItemStack input : itemInputs) {
			for (GTRecipeInput gtInput : recipe.getInputs()) {
				if (gtInput.acceptsStack(input)) {
					continue itemLoop;
				}
			}
			return false;
		}
		
		fluidLoop:
		for (FluidStack input : fluidInputs) {
			for (GTRecipeInput gtInput : recipe.getFluidInputs()) {
				if (gtInput.acceptsFluid(input)) {
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
