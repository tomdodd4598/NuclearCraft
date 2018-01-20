package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class ElectrolyserRecipes extends BaseRecipeHandler {
	
	public ElectrolyserRecipes() {
		super("electrolyser", 0, 1, 0, 4, false);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("hydrogen", 475), fluidStack("hydrogen", 475), fluidStack("deuterium", 50), fluidStack("oxygen", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[8]);
		addRecipe(fluidStack("hydrofluoric_acid", FluidHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidHelper.BUCKET_VOLUME/4), fluidStack("hydrogen", FluidHelper.BUCKET_VOLUME/4), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/4), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/4), NCConfig.processor_time[8]/2);
		
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
		
		// Mekanism
		addRecipe(fluidStack("heavywater", FluidHelper.BUCKET_VOLUME), fluidStack("deuterium", 475), fluidStack("deuterium", 475), fluidStack("tritium", 50), fluidStack("oxygen", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[8]);
	}
	
	public void addIsotopeFluorideRecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME), fluidStack(element + "_" + type, FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_" + type, FluidHelper.INGOT_VOLUME/2), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/2), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[8]/4);
	}
	
	public void addFissionFuelFluorideRecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME), fluidStack(form + suffix + "_" + type, FluidHelper.INGOT_VOLUME/2), fluidStack(form + suffix + "_" + type, FluidHelper.INGOT_VOLUME/2), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/2), fluidStack("fluorine", FluidHelper.BUCKET_VOLUME/2), NCConfig.processor_time[8]/4);
		}
	}
}
