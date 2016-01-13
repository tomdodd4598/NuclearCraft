package com.nr.mod.crafting;

import java.util.Comparator;

import net.minecraft.item.crafting.IRecipe;

@SuppressWarnings("rawtypes")
public class NuclearWorkspaceRecipeSorter implements Comparator {

	final NuclearWorkspaceCraftingManager nuclearWorkspace;
	
	public NuclearWorkspaceRecipeSorter(NuclearWorkspaceCraftingManager nuclearWorkspaceCraftingManager) {
		this.nuclearWorkspace = nuclearWorkspaceCraftingManager;
	}
	
	public int compareRecipes(IRecipe irecipe1, IRecipe irecipe2) {
		return irecipe1 instanceof NuclearWorkspaceShapelessRecipes && irecipe2 instanceof NuclearWorkspaceShapedRecipes ? 1: (irecipe2 instanceof NuclearWorkspaceShapelessRecipes && irecipe1 instanceof NuclearWorkspaceShapedRecipes ? -1 : (irecipe2.getRecipeSize() < irecipe1.getRecipeSize() ? -1 : (irecipe2.getRecipeSize() > irecipe1.getRecipeSize() ? 1 : 0)));
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		return this.compareRecipes((IRecipe)o1, (IRecipe)o2);
	}

}
