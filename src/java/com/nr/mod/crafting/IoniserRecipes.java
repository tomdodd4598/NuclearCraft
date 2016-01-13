package com.nr.mod.crafting;

import net.minecraft.item.ItemStack;

import com.nr.mod.items.NRItems;

public class IoniserRecipes extends NRRecipeHelper {

	private static final IoniserRecipes recipes = new IoniserRecipes();

	public IoniserRecipes(){
		super(2, 2);
	}
	public static final NRRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		fuelFuel(1, 48, 8, 36, 1, 49, 8, 45);
	}
	
	public void fuelFuel(int noIn, int metaIn, int noIn2, int metaIn2, int noOut, int metaOut, int noOut2, int metaOut2) {
		addRecipe(new ItemStack(NRItems.fuel, noIn, metaIn), new ItemStack(NRItems.fuel, noIn2, metaIn2), new ItemStack(NRItems.fuel, noOut, metaOut), new ItemStack(NRItems.fuel, noOut2, metaOut2));
	}
}