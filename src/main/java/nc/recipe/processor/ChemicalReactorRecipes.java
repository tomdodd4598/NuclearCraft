package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class ChemicalReactorRecipes extends BaseRecipeHandler {
	
	//private static final ChemicalReactorRecipes RECIPES = new ChemicalReactorRecipes();
	
	public ChemicalReactorRecipes() {
		super(0, 2, 0, 2, true);
	}

	/*public static final ChemicalReactorRecipes instance() {
		return RECIPES;
	}*/

	public void addRecipes() {
		addRecipe(fluidStack("boron", 144), fluidStack("hydrogen", 3000), fluidStack("diborane", 500), fluidStack("diborane", 500), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("boron10", 144), fluidStack("hydrogen", 3000), fluidStack("diborane", 500), fluidStack("diborane", 500), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("boron11", 144), fluidStack("hydrogen", 3000), fluidStack("diborane", 500), fluidStack("diborane", 500), NCConfig.processor_time[12]*2);
		
		addRecipe(fluidStack("diborane", 125), fluidStack("water", 750), fluidStack("boric_acid", 250), fluidStack("hydrogen", 750), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("nitrogen", 500), fluidStack("hydrogen", 1500), fluidStack("ammonia", 500), fluidStack("ammonia", 500), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("boric_acid", 500), fluidStack("ammonia", 500), fluidStack("boron_nitride_solution", 333), fluidStack("water", 1000), NCConfig.processor_time[12]*2);
		
		addRecipe(fluidStack("lithium6", 144), fluidStack("fluorine", 500), fluidStack("lif", 72), fluidStack("lif", 72), NCConfig.processor_time[12]);
		addRecipe(fluidStack("lithium7", 144), fluidStack("fluorine", 500), fluidStack("lif", 72), fluidStack("lif", 72), NCConfig.processor_time[12]);
		addRecipe(fluidStack("beryllium", 144), fluidStack("fluorine", 500), fluidStack("bef2", 72), fluidStack("bef2", 72), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("sulfur", 666), fluidStack("oxygen", 1000), fluidStack("sulfur_dioxide", 500), fluidStack("sulfur_dioxide", 500), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("sulfur_dioxide", 1000), fluidStack("oxygen", 500), fluidStack("sulfur_trioxide", 500), fluidStack("sulfur_trioxide", 500), NCConfig.processor_time[12]);
		addRecipe(fluidStack("sulfur_trioxide", 1000), fluidStack("water", 1000), fluidStack("sulfuric_acid", 500), fluidStack("sulfuric_acid", 500), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("fluorite_water", 333), fluidStack("sulfuric_acid", 500), fluidStack("hydrofluoric_acid", 1000), fluidStack("calcium_sulfate_solution", 333), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("oxygen_difluoride", 250), fluidStack("water", 250), fluidStack("oxygen", 250), fluidStack("hydrofluoric_acid", 500), NCConfig.processor_time[12]);
		addRecipe(fluidStack("oxygen_difluoride", 500), fluidStack("sulfur_dioxide", 500), fluidStack("sulfur_trioxide", 500), fluidStack("fluorine", 500), NCConfig.processor_time[12]);
	}

	public String getRecipeName() {
		return "chemical_reactor";
	}
}
