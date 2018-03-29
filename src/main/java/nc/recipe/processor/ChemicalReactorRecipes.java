package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class ChemicalReactorRecipes extends BaseRecipeHandler {
	
	public ChemicalReactorRecipes() {
		super("chemical_reactor", 0, 2, 0, 2);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStackList(Lists.newArrayList("boron", "boron10", "boron11"), FluidHelper.INGOT_VOLUME), fluidStack("hydrogen", FluidHelper.BUCKET_VOLUME*3), fluidStack("diborane", FluidHelper.BUCKET_VOLUME/2), fluidStack("diborane", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[12]*2);
		
		addRecipe(fluidStack("diborane", FluidHelper.BUCKET_VOLUME/4), fluidStack("water", FluidHelper.BUCKET_VOLUME*3/2), fluidStack("boric_acid", FluidHelper.BUCKET_VOLUME/2), fluidStack("hydrogen", FluidHelper.BUCKET_VOLUME*3/2), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("nitrogen", FluidHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidHelper.BUCKET_VOLUME*3), fluidStack("ammonia", FluidHelper.BUCKET_VOLUME), fluidStack("ammonia", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("boric_acid", FluidHelper.BUCKET_VOLUME), fluidStack("ammonia", FluidHelper.BUCKET_VOLUME), fluidStack("boron_nitride_solution", FluidHelper.GEM_VOLUME), fluidStack("water", FluidHelper.BUCKET_VOLUME*2), NCConfig.processor_time[12]*2);
		
		addRecipe(fluidStackList(Lists.newArrayList("lithium", "lithium6", "lithium7"), FluidHelper.INGOT_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/2), fluidStack("lif", FluidHelper.INGOT_VOLUME/2), fluidStack("lif", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[12]);
		addRecipe(fluidStack("beryllium", FluidHelper.INGOT_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/2), fluidStack("bef2", FluidHelper.INGOT_VOLUME/2), fluidStack("bef2", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("sulfur", FluidHelper.GEM_VOLUME), fluidStack("oxygen", FluidHelper.BUCKET_VOLUME), fluidStack("sulfur_dioxide", FluidHelper.BUCKET_VOLUME/2), fluidStack("sulfur_dioxide", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("sulfur_dioxide", FluidHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[12]);
		addRecipe(fluidStack("sulfur_trioxide", FluidHelper.BUCKET_VOLUME), fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("sulfuric_acid", FluidHelper.BUCKET_VOLUME/2), fluidStack("sulfuric_acid", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("fluorite_water", FluidHelper.GEM_VOLUME), fluidStack("sulfuric_acid", FluidHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidHelper.BUCKET_VOLUME*2), fluidStack("calcium_sulfate_solution", FluidHelper.GEM_VOLUME), NCConfig.processor_time[12]);
		
		addRecipe(fluidStack("oxygen_difluoride", FluidHelper.BUCKET_VOLUME), fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidHelper.BUCKET_VOLUME*2), NCConfig.processor_time[12]*2);
		addRecipe(fluidStack("oxygen_difluoride", FluidHelper.BUCKET_VOLUME), fluidStack("sulfur_dioxide", FluidHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[12]*2);
		
		addRecipe(fluidStack("oxygen", FluidHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME*2), fluidStack("oxygen_difluoride", FluidHelper.BUCKET_VOLUME), fluidStack("oxygen_difluoride", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[12]);
		
		addElementFluorideRecipes("thorium", "uranium", "plutonium");
		
		addIsotopeFluorideRecipes("thorium", 230, 232);
		addIsotopeFluorideRecipes("uranium", 233, 235, 238);
		addIsotopeFluorideRecipes("neptunium", 236, 237);
		addIsotopeFluorideRecipes("plutonium", 238, 239, 241, 242);
		addIsotopeFluorideRecipes("americium", 241, 242, 243);
		addIsotopeFluorideRecipes("curium", 243, 245, 246, 247);
		addIsotopeFluorideRecipes("berkelium", 247, 248);
		addIsotopeFluorideRecipes("californium", 249, 250, 251, 252);
		
		addFissionFuelFluorideRecipes("eu", 233, 235);
		addFissionFuelFluorideRecipes("en", 236);
		addFissionFuelFluorideRecipes("ep", 239, 241);
		addFissionFuelFluorideRecipes("ea", 242);
		addFissionFuelFluorideRecipes("ecm", 243, 245, 247);
		addFissionFuelFluorideRecipes("eb", 248);
		addFissionFuelFluorideRecipes("ecf", 249, 251);
	}
	
	public void addElementFluorideRecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element, FluidHelper.INGOT_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME), fluidStack(element + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_fluoride", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[12]);
	}
	
	public void addIsotopeFluorideRecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type, FluidHelper.INGOT_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME), fluidStack(element + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[12]);
	}
	
	public void addFissionFuelFluorideRecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type, FluidHelper.INGOT_VOLUME), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME), fluidStack(form + suffix + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack(form + suffix + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[12]);
		}
	}
}
