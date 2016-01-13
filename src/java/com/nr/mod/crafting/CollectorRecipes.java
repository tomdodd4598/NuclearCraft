package com.nr.mod.crafting;

import com.nr.mod.items.NRItems;
import net.minecraft.item.ItemStack;

public class CollectorRecipes extends NRRecipeHelper {

	private static final CollectorRecipes recipes = new CollectorRecipes();

	public CollectorRecipes(){
		super(1, 1);
	}
	public static final NRRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(new ItemStack(NRItems.fuel, 1, 45), new ItemStack(NRItems.fuel, 1, 40));
	}
}