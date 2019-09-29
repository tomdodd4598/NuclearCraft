package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class ElectrolyserRecipes extends ProcessorRecipeHandler {
	
	public ElectrolyserRecipes() {
		super("electrolyser", 0, 1, 0, 4);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStackList(Lists.newArrayList("heavywater", "heavy_water"), FluidStackHelper.BUCKET_VOLUME), fluidStack("deuterium", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 0.5D);
		
		addRecipe(fluidStack("naoh", FluidStackHelper.GEM_VOLUME), fluidStack("sodium", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME/2), emptyFluidStack(), 1.5D, 1.5D);
		addRecipe(fluidStack("koh", FluidStackHelper.GEM_VOLUME), fluidStack("potassium", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME/2), emptyFluidStack(), 1.5D, 1.5D);
		
		addRecipe(fluidStack("alumina", FluidStackHelper.INGOT_VOLUME), fluidStack("aluminum", 2*FluidStackHelper.INGOT_VOLUME), fluidStack("oxygen", 3*FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 2D, 1D);
		
		addElementFluorideRecipes("thorium", "uranium", "plutonium");
		
		addIsotopeFluorideRecipes("thorium", 230);
		addIsotopeFluorideRecipes("uranium", 233, 235, 238);
		addIsotopeFluorideRecipes("neptunium", 236, 237);
		addIsotopeFluorideRecipes("plutonium", 238, 239, 241, 242);
		addIsotopeFluorideRecipes("americium", 241, 242, 243);
		addIsotopeFluorideRecipes("curium", 243, 245, 246, 247);
		addIsotopeFluorideRecipes("berkelium", 247, 248);
		addIsotopeFluorideRecipes("californium", 249, 250, 251, 252);
		
		addRecipe(fluidStack("fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("fuel_tbu", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		addRecipe(fluidStack("depleted_fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("depleted_fuel_tbu", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		addFissionFuelFluorideRecipes("eu", 233, 235);
		addFissionFuelFluorideRecipes("en", 236);
		addFissionFuelFluorideRecipes("ep", 239, 241);
		addFissionFuelFluorideRecipes("ea", 242);
		addFissionFuelFluorideRecipes("ecm", 243, 245, 247);
		addFissionFuelFluorideRecipes("eb", 248);
		addFissionFuelFluorideRecipes("ecf", 249, 251);
	}
	
	public void addElementFluorideRecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack(element, FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
	}
	
	public void addIsotopeFluorideRecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + type, FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
	}
	
	public void addFissionFuelFluorideRecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack(form + suffix + "_" + type, FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		}
	}
}
