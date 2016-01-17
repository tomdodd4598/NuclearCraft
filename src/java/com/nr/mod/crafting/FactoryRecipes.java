package com.nr.mod.crafting;

import com.nr.mod.items.NRItems;
import net.minecraft.item.ItemStack;

public class FactoryRecipes extends NRRecipeHelper {

	private static final FactoryRecipes recipes = new FactoryRecipes();

	public FactoryRecipes(){
		super(1, 1);
	}
	public static final NRRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
    	addRecipe(oreStack("ingotIron", 4), new ItemStack(NRItems.parts, 1, 10));
    	addRecipe(oreStack("ingotGold", 1), new ItemStack(NRItems.parts, 1, 11));
    	addRecipe(oreStack("ingotCopper", 2), new ItemStack(NRItems.parts, 1, 12));
    	addRecipe(oreStack("ingotTin", 2), new ItemStack(NRItems.parts, 1, 13));
    	addRecipe(oreStack("ingotLead", 1), new ItemStack(NRItems.parts, 1, 14));
    	addRecipe(oreStack("ingotSilver", 2), new ItemStack(NRItems.parts, 1, 15));
    	addRecipe(oreStack("ingotBronze", 1), new ItemStack(NRItems.parts, 1, 16));
    }
}