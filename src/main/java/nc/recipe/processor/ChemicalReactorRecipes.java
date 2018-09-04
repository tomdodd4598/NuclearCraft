package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class ChemicalReactorRecipes extends ProcessorRecipeHandler {
	
	public ChemicalReactorRecipes() {
		super("chemical_reactor", 0, 2, 0, 2);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStackList(Lists.newArrayList("boron", "boron10", "boron11"), FluidStackHelper.INGOT_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*3), fluidStack("diborane", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1.5D, 1D);
		
		addRecipe(fluidStack("diborane", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("water", FluidStackHelper.BUCKET_VOLUME*3), fluidStack("boric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*3), 1D, 1D);
		
		addRecipe(fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*3), fluidStack("ammonia", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 0.5D);
		
		addRecipe(fluidStack("boric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("ammonia", FluidStackHelper.BUCKET_VOLUME), fluidStack("boron_nitride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), 1.5D, 1.5D);
		
		addRecipe(fluidStackList(Lists.newArrayList("lithium", "lithium6", "lithium7"), FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("lif", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("beryllium", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("bef2", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
		
		addRecipe(fluidStack("sulfur", FluidStackHelper.GEM_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_dioxide", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 0.5D);
		addRecipe(fluidStack("sulfur_dioxide", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME*2), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfuric_acid", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1.5D, 0.5D);
		
		addRecipe(fluidStack("fluorite_water", FluidStackHelper.GEM_VOLUME), fluidStack("sulfuric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("calcium_sulfate_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("sodium_fluoride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("sodium_hydroxide_solution", FluidStackHelper.GEM_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(fluidStack("potassium_fluoride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("potassium_hydroxide_solution", FluidStackHelper.GEM_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("oxygen_difluoride", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME*2), 1.5D, 1D);
		addRecipe(fluidStack("oxygen_difluoride", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_dioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), 1D, 1.5D);
		
		addRecipe(fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen_difluoride", FluidStackHelper.BUCKET_VOLUME*2), emptyFluidStack(), 1D, 1.5D);
		
		addRecipe(fluidStack("sugar", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("ethanol", FluidStackHelper.BUCKET_VOLUME*4), fluidStack("carbon_dioxide", FluidStackHelper.BUCKET_VOLUME*4), 1.5D, 0.5D);
		addRecipe(fluidStack("carbon_dioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("carbon_monoxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 2D, 0.5D);
		addRecipe(fluidStack("carbon_monoxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("methanol", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("methanol", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluoromethane", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("fluoromethane", FluidStackHelper.BUCKET_VOLUME), fluidStack("naoh", FluidStackHelper.GEM_VOLUME), fluidStack("ethene", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("sodium_fluoride_solution", FluidStackHelper.GEM_VOLUME), 1.5D, 1D);
		addRecipe(fluidStack("fluoromethane", FluidStackHelper.BUCKET_VOLUME), fluidStack("koh", FluidStackHelper.GEM_VOLUME), fluidStack("ethene", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("potassium_fluoride_solution", FluidStackHelper.GEM_VOLUME), 1.5D, 1D);
		
		addRecipe(fluidStack("ethene", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfuric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("ethanol", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStackList(Lists.newArrayList("boron", "boron10", "boron11"), FluidStackHelper.INGOT_VOLUME), fluidStack("arsenic", FluidStackHelper.GEM_VOLUME), fluidStack("bas", FluidStackHelper.GEM_VOLUME), emptyFluidStack(), 1.5D, 1.5D);
		
		addElementFluorideRecipes("thorium", "uranium", "plutonium");
		
		addIsotopeFluorideRecipes("thorium", 230);
		addIsotopeFluorideRecipes("uranium", 233, 235, 238);
		addIsotopeFluorideRecipes("neptunium", 236, 237);
		addIsotopeFluorideRecipes("plutonium", 238, 239, 241, 242);
		addIsotopeFluorideRecipes("americium", 241, 242, 243);
		addIsotopeFluorideRecipes("curium", 243, 245, 246, 247);
		addIsotopeFluorideRecipes("berkelium", 247, 248);
		addIsotopeFluorideRecipes("californium", 249, 250, 251, 252);
		
		addRecipe(fluidStack("fuel_tbu", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("depleted_fuel_tbu", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("depleted_fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
		addFissionFuelFluorideRecipes("eu", 233, 235);
		addFissionFuelFluorideRecipes("en", 236);
		addFissionFuelFluorideRecipes("ep", 239, 241);
		addFissionFuelFluorideRecipes("ea", 242);
		addFissionFuelFluorideRecipes("ecm", 243, 245, 247);
		addFissionFuelFluorideRecipes("eb", 248);
		addFissionFuelFluorideRecipes("ecf", 249, 251);
	}
	
	public void addElementFluorideRecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element, FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack(element + "_fluoride", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
	}
	
	public void addIsotopeFluorideRecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type, FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack(element + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
	}
	
	public void addFissionFuelFluorideRecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type, FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack(form + suffix + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 1D);
		}
	}
}
