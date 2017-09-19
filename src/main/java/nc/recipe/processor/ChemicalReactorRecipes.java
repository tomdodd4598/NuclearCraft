package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class ChemicalReactorRecipes extends BaseRecipeHandler {
	
	private static final ChemicalReactorRecipes RECIPES = new ChemicalReactorRecipes();
	
	public ChemicalReactorRecipes() {
		super(0, 2, 0, 2, true);
	}

	public static final ChemicalReactorRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		addRecipe(fluidStack("nitrogen", 200), fluidStack("hydrogen", 600), fluidStack("ammonia", 100), fluidStack("ammonia", 100), NCConfig.processor_time[12]);
		addRecipe(fluidStack("boron", 72), fluidStack("hydrogen", 750), fluidStack("diborane", 125), fluidStack("diborane", 125), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("boron10", 72), fluidStack("hydrogen", 750), fluidStack("diborane", 125), fluidStack("diborane", 125), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("boron11", 72), fluidStack("hydrogen", 750), fluidStack("diborane", 125), fluidStack("diborane", 125), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("diborane", 100), fluidStack("water", 600), fluidStack("boric_acid", 200), fluidStack("hydrogen", 600), NCConfig.processor_time[12]);
		addRecipe(fluidStack("boric_acid", 200), fluidStack("ammonia", 200), fluidStack("boron_nitride_solution", 222), fluidStack("water", 250), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("lithium6", 72), fluidStack("fluorine", 500), fluidStack("lif", 250), fluidStack("lif", 250), NCConfig.processor_time[12]);
		addRecipe(fluidStack("lithium7", 72), fluidStack("fluorine", 500), fluidStack("lif", 250), fluidStack("lif", 250), NCConfig.processor_time[12]);
		addRecipe(fluidStack("beryllium", 72), fluidStack("fluorine", 1000), fluidStack("bef2", 250), fluidStack("bef2", 250), NCConfig.processor_time[12]);
	}

	public String getRecipeName() {
		return "chemical_reactor";
	}
}
