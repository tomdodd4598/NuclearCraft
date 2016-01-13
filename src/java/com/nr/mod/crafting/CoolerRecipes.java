package com.nr.mod.crafting;

import com.nr.mod.items.NRItems;
import net.minecraft.item.ItemStack;

public class CoolerRecipes extends NRRecipeHelper {

	private static final CoolerRecipes recipes = new CoolerRecipes();

	public CoolerRecipes(){
		super(1, 1);
	}
	public static final NRRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(new ItemStack(NRItems.fuel, 1, 40), new ItemStack(NRItems.fuel, 1, 75));
	}
}