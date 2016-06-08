package nc;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipesHelp {
	
	public RecipesHelp() {}
	
	public static void addCentrifugeRecipe(ItemStack input, int amount, int minHeat, ItemStack... output) {
		addCentrifugeRecipe(new RecipeInputItemStack(input, amount), minHeat, output);
	}

	public static void addCentrifugeRecipe(String input, int amount, int minHeat, ItemStack... output) {
		addCentrifugeRecipe(new RecipeInputOreDict(input, amount), minHeat, output);
	}

	public static void addCentrifugeRecipe(IRecipeInput input, int minHeat, ItemStack... output) {
		NBTTagCompound metadata = new NBTTagCompound();
	metadata.setInteger("minHeat", minHeat);
	Recipes.centrifuge.addRecipe(input, metadata, output);
	}
	
	public static void addBottleRecipe(ItemStack container, int conamount, ItemStack fill, int fillamount, ItemStack output) {
		addBottleRecipe(new RecipeInputItemStack(container, conamount), new RecipeInputItemStack(fill, fillamount), output);
	}
	
	public static void addBottleRecipe(ItemStack container, ItemStack fill, int fillamount, ItemStack output) {
		addBottleRecipe(new RecipeInputItemStack(container, 1), new RecipeInputItemStack(fill, fillamount), output);
	}
	
	public static void addBottleRecipe(ItemStack container, int conamount, ItemStack fill, ItemStack output) {
		addBottleRecipe(new RecipeInputItemStack(container, conamount), new RecipeInputItemStack(fill, 1), output);
	}
	
	public static void addBottleRecipe(ItemStack container, ItemStack fill, ItemStack output) {
		addBottleRecipe(new RecipeInputItemStack(container, 1), new RecipeInputItemStack(fill, 1), output);
	}
	
	public static void addBottleRecipe(IRecipeInput container, IRecipeInput fill, ItemStack output) {
		Recipes.cannerBottle.addRecipe(container, fill, output);
	}
	
	public static void addCompressorRecipe(ItemStack input, int amount, ItemStack output) {
		addCompressorRecipe(new RecipeInputItemStack(input, amount), output);
	}

	public static void addCompressorRecipe(String input, int amount, ItemStack output) {
		addCompressorRecipe(new RecipeInputOreDict(input, amount), output);
	}

	public static void addCompressorRecipe(IRecipeInput input, ItemStack output) {
		Recipes.compressor.addRecipe(input, null, new ItemStack[] { output });
	}
	
	public static void addMFAmplifier(ItemStack input, int amount, int amplification) {
		addMFAmplifier(new ic2.api.recipe.RecipeInputItemStack(input, amount), amplification);
	}

	public static void addMFAmplifier(String input, int amount, int amplification) {
		addMFAmplifier(new RecipeInputOreDict(input, amount), amplification);
	}

	public static void addMFAmplifier(IRecipeInput input, int amplification) {
		NBTTagCompound metadata = new NBTTagCompound();
	metadata.setInteger("amplification", amplification);

	Recipes.matterAmplifier.addRecipe(input, metadata, new ItemStack[0]);
	}
	
	public static void addMaceratorRecipe(ItemStack input, int amount, ItemStack output) {
		addMaceratorRecipe(new RecipeInputItemStack(input, amount), output);
	}

	public static void addMaceratorRecipe(String input, int amount, ItemStack output) {
		addMaceratorRecipe(new RecipeInputOreDict(input, amount), output);
	}

	public static void addMaceratorRecipe(IRecipeInput input, ItemStack output) {
		Recipes.macerator.addRecipe(input, null, new ItemStack[] { output });
	}
}
