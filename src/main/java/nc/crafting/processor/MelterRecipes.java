package nc.crafting.processor;

import nc.handler.ProcessorRecipeHandler;

public class MelterRecipes extends ProcessorRecipeHandler {
	
	private static final MelterRecipes RECIPES = new MelterRecipes();

	public MelterRecipes() {
		super(1, 0, 0, 1, false);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe("ingotLithium6", fluidStack("lithium6", 108));
		addRecipe("tinyLithium6", fluidStack("lithium6", 12));
		addRecipe("ingotLithium7", fluidStack("lithium7", 108));
		addRecipe("ingotBoron11", fluidStack("boron11", 108));
	}
}
