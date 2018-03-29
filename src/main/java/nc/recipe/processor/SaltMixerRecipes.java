package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class SaltMixerRecipes extends BaseRecipeHandler {
	
	public SaltMixerRecipes() {
		super("salt_mixer", 0, 2, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("lif", FluidHelper.INGOT_VOLUME*2), fluidStack("bef2", FluidHelper.INGOT_VOLUME), fluidStack("flibe", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		addRecipe(fluidStack("sodium", FluidHelper.INGOT_VOLUME/2), fluidStack("potassium", FluidHelper.INGOT_VOLUME*2), fluidStack("nak", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		
		addRecipe(fluidStack("boron11", FluidHelper.INGOT_VOLUME), fluidStack("boron10", FluidHelper.NUGGET_VOLUME*3), fluidStack("boron", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		addRecipe(fluidStack("lithium7", FluidHelper.INGOT_VOLUME), fluidStack("lithium6", FluidHelper.NUGGET_VOLUME*3), fluidStack("lithium", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		
		addCoolantNAKRecipe("redstone", FluidHelper.REDSTONE_DUST_VOLUME*10);
		addCoolantNAKRecipe("quartz", FluidHelper.GEM_VOLUME*7);
		addCoolantNAKRecipe("gold", FluidHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("glowstone", FluidHelper.GLOWSTONE_DUST_VOLUME*7);
		addCoolantNAKRecipe("lapis", FluidHelper.GEM_BLOCK_VOLUME);
		addCoolantNAKRecipe("diamond", FluidHelper.GEM_VOLUME*4);
		addCoolantNAKRecipe("liquidhelium", FluidHelper.BUCKET_VOLUME/2);
		addCoolantNAKRecipe("ender", FluidHelper.EUM_DUST_VOLUME*4);
		addCoolantNAKRecipe("cryotheum", FluidHelper.EUM_DUST_VOLUME*4);
		addCoolantNAKRecipe("iron", FluidHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("emerald", FluidHelper.GEM_VOLUME*3);
		addCoolantNAKRecipe("copper", FluidHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("tin", FluidHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("magnesium", FluidHelper.INGOT_VOLUME*4);
		
		addElementFLIBERecipes("thorium", "uranium", "plutonium");
		
		addRecipe(fluidStack("thorium_232", FluidHelper.INGOT_VOLUME), fluidStack("thorium_230", FluidHelper.NUGGET_VOLUME), fluidStack("thorium", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		addRecipe(fluidStack("uranium_238", FluidHelper.INGOT_VOLUME), fluidStack("uranium_235", FluidHelper.NUGGET_VOLUME), fluidStack("uranium", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		addRecipe(fluidStack("plutonium_239", FluidHelper.INGOT_VOLUME), fluidStack("plutonium_242", FluidHelper.NUGGET_VOLUME), fluidStack("plutonium", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[13]);
		
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
	
	public void addCoolantNAKRecipe(String name, int amount) {
		addRecipe(fluidStack(name, amount), fluidStack("nak", FluidHelper.INGOT_VOLUME*4), fluidStack(name + "_nak", FluidHelper.INGOT_VOLUME*4), NCConfig.processor_time[13]*4);
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
