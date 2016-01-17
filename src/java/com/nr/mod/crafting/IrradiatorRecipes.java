package com.nr.mod.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.nr.mod.items.NRItems;

public class IrradiatorRecipes extends NRRecipeHelper {

	private static final IrradiatorRecipes recipes = new IrradiatorRecipes();

	public IrradiatorRecipes(){
		super(2, 3);
	}
	public static final NRRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		recipeNeutron(NRItems.material, 1, 46, NRItems.fuel, 1, 38, NRItems.fuel, 1, 40);
		recipeNeutron2(NRItems.material, 1, 47, NRItems.fuel, 1, 38, NRItems.fuel, 1, 40);
		recipeNeutron(NRItems.material, 1, 48, NRItems.fuel, 1, 38, NRItems.fuel, 2, 40);
	}
	
	public void recipeNeutron(Item In, int noIn, int metaIn, Item Out, int noOut, int metaOut, Item Out2, int noOut2, int metaOut2) {
		addRecipe(new ItemStack(In, noIn, metaIn), new ItemStack(NRItems.fuel, 1, 47), new ItemStack(Out, noOut, metaOut), new ItemStack(Out2, noOut2, metaOut2), new ItemStack(NRItems.fuel, 1, 48));
	}
	
	public void recipeNeutron2(Item In, int noIn, int metaIn, Item Out, int noOut, int metaOut, Item Out2, int noOut2, int metaOut2) {
		addRecipe(new ItemStack(In, noIn, metaIn), new ItemStack(NRItems.fuel, 1, 47), new ItemStack(Out, noOut, metaOut), new ItemStack(Out2, noOut2, metaOut2), new ItemStack(NRItems.fuel, 1, 47));
	}
}