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
		addRecipe(new ItemStack(NRItems.fuel, 12, 34), new ItemStack(NRItems.fuel, 4, 35), new ItemStack(NRItems.fuel, 4, 36), new ItemStack(NRItems.fuel, 3, 36), new ItemStack(NRItems.fuel, 1, 37));
	}
}
