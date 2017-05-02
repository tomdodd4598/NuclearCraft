package nc.crafting.processor;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;

public class MelterRecipes extends ProcessorRecipeHandler {
	
	private static final MelterRecipes RECIPES = new MelterRecipes();

	public MelterRecipes() {
		super(1, 0, 0, 1, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe("ingotLithium6", fluidStack("lithium6", 108), NCConfig.processor_time[6]);
		addRecipe("tinyLithium6", fluidStack("lithium6", 12), NCConfig.processor_time[6]/8);
		addRecipe("ingotLithium7", fluidStack("lithium7", 108), NCConfig.processor_time[6]);
		addRecipe("ingotBoron11", fluidStack("boron11", 108), NCConfig.processor_time[6]/8);
	}
}
