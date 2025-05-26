package nc.recipe.processor;

import it.unimi.dsi.fastutil.ints.IntLists;
import nc.recipe.*;
import nc.recipe.ingredient.ItemIngredient;
import nc.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nullable;
import java.util.*;

import static nc.config.NCConfig.*;

public class ElectricFurnaceRecipes extends BasicProcessorRecipeHandler {
	
	private static final List<Object> VANILLA_FURNACE_EXTRAS = Arrays.asList(1D, 1D, 0D);
	private static final RecipeMatchResult VANILLA_FURNACE_RECIPE_MATCH_RESULT = new RecipeMatchResult(true, IntLists.singleton(0), IntLists.EMPTY_LIST, IntLists.singleton(0), IntLists.EMPTY_LIST);
	
	public ElectricFurnaceRecipes() {
		super("electric_furnace", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[19]) {
			return;
		}
	}
	
	public static BasicRecipe getVanillaFurnaceRecipe(ItemStack input, ItemStack output) {
		return new BasicRecipe(Collections.singletonList(new ItemIngredient(input)), Collections.emptyList(), Collections.singletonList(new ItemIngredient(output)), Collections.emptyList(), VANILLA_FURNACE_EXTRAS, true);
	}
	
	@Override
	public @Nullable RecipeInfo<BasicRecipe> getRecipeInfoFromInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		RecipeInfo<BasicRecipe> recipeInfo = super.getRecipeInfoFromInputs(itemInputs, fluidInputs);
		if (recipeInfo != null) {
			return recipeInfo;
		}
		
		ItemStack input = itemInputs.get(0);
		if (input.isEmpty()) {
			return null;
		}
		
		ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
		if (output.isEmpty()) {
			return null;
		}
		
		return new RecipeInfo<>(getVanillaFurnaceRecipe(input, output), VANILLA_FURNACE_RECIPE_MATCH_RESULT);
	}
	
	public boolean isValidVanillaFurnaceInput(ItemStack stack, int slot) {
		return slot == 0 && !stack.isEmpty() && !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
	}
	
	@Override
	public boolean isValidItemInput(ItemStack stack) {
		return super.isValidItemInput(stack) || isValidVanillaFurnaceInput(stack, 0);
	}
	
	@Override
	public boolean isValidItemInput(ItemStack stack, int slot) {
		return super.isValidItemInput(stack, slot) || isValidVanillaFurnaceInput(stack, slot);
	}
	
	@Override
	public boolean isValidItemInput(ItemStack stack, int slot, List<ItemStack> itemInputs, List<Tank> fluidInputs, RecipeInfo<BasicRecipe> recipeInfo) {
		return super.isValidItemInput(stack, slot, itemInputs, fluidInputs, recipeInfo) || isValidVanillaFurnaceInput(stack, slot);
	}
}
