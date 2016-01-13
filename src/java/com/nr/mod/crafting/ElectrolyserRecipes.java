package com.nr.mod.crafting;

import com.nr.mod.items.NRItems;
import net.minecraft.item.ItemStack;

public class ElectrolyserRecipes extends NRRecipeHelper {

	private static final ElectrolyserRecipes recipes = new ElectrolyserRecipes();

	public ElectrolyserRecipes(){
		super(1, 4);
	}
	public static final NRRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		//addRecipe(new ItemStack(NRItems.fuel, 64, 45), new ItemStack(NRItems.fuel, 24, 35), new ItemStack(NRItems.fuel, 36, 36), new ItemStack(NRItems.fuel, 3, 37), new ItemStack(NRItems.fuel, 1, 38));
		addRecipe(new ItemStack(NRItems.fuel, 64, 34), new ItemStack(NRItems.fuel, 24, 35), new ItemStack(NRItems.fuel, 36, 36), new ItemStack(NRItems.fuel, 3, 37), new ItemStack(NRItems.fuel, 1, 38));
	}
}
