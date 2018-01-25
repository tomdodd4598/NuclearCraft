package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class SaltMixerRecipes extends BaseRecipeHandler {
	
	public SaltMixerRecipes() {
		super("salt_mixer", 0, 2, 0, 1, true);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("lif", FluidHelper.INGOT_VOLUME*2), fluidStack("bef2", FluidHelper.INGOT_VOLUME), fluidStack("flibe", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		
		addElementFLIBERecipes("thorium", "uranium", "plutonium");
		
		addIsotopeFLIBERecipes("thorium", 230, 232);
		addIsotopeFLIBERecipes("uranium", 233, 235, 238);
		addIsotopeFLIBERecipes("neptunium", 236, 237);
		addIsotopeFLIBERecipes("plutonium", 238, 239, 241, 242);
		addIsotopeFLIBERecipes("americium", 241, 242, 243);
		addIsotopeFLIBERecipes("curium", 243, 245, 246, 247);
		addIsotopeFLIBERecipes("berkelium", 247, 248);
		addIsotopeFLIBERecipes("californium", 249, 250, 251, 252);
		
		addFissionFuelFLIBERecipes("eu", 233, 235);
		addFissionFuelFLIBERecipes("en", 236);
		addFissionFuelFLIBERecipes("ep", 239, 241);
		addFissionFuelFLIBERecipes("ea", 242);
		addFissionFuelFLIBERecipes("ecm", 243, 245, 247);
		addFissionFuelFLIBERecipes("eb", 248);
		addFissionFuelFLIBERecipes("ecf", 249, 251);
	}
	
	public void addElementFLIBERecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element + "_fluoride", FluidHelper.INGOT_VOLUME), fluidStack("flibe", FluidHelper.INGOT_VOLUME), fluidStack(element + "_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), NCConfig.processor_time[13]);
	}
	
	public void addIsotopeFLIBERecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME), fluidStack("flibe", FluidHelper.INGOT_VOLUME), fluidStack(element + "_" + type + "_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), NCConfig.processor_time[13]);
	}
	
	public void addFissionFuelFLIBERecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME), fluidStack("flibe", FluidHelper.INGOT_VOLUME), fluidStack(form + suffix + "_" + type + "_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), NCConfig.processor_time[13]);
		}
	}
}
