package nc.recipe.processor;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class SaltMixerRecipes extends ProcessorRecipeHandler {
	
	public SaltMixerRecipes() {
		super("salt_mixer", 0, 2, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("lif", FluidStackHelper.INGOT_VOLUME*2), fluidStack("bef2", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("sodium", FluidStackHelper.INGOT_VOLUME/2), fluidStack("potassium", FluidStackHelper.INGOT_VOLUME*2), fluidStack("nak", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("boron11", FluidStackHelper.INGOT_VOLUME), fluidStack("boron10", FluidStackHelper.NUGGET_VOLUME*3), fluidStack("boron", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("lithium7", FluidStackHelper.INGOT_VOLUME), fluidStack("lithium6", FluidStackHelper.NUGGET_VOLUME*3), fluidStack("lithium", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addCoolantNAKRecipe("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*10);
		addCoolantNAKRecipe("quartz", FluidStackHelper.GEM_VOLUME*7);
		addCoolantNAKRecipe("gold", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("glowstone", FluidStackHelper.GLOWSTONE_DUST_VOLUME*7);
		addCoolantNAKRecipe("lapis", FluidStackHelper.GEM_BLOCK_VOLUME);
		addCoolantNAKRecipe("diamond", FluidStackHelper.GEM_VOLUME*4);
		addCoolantNAKRecipe("liquidhelium", FluidStackHelper.BUCKET_VOLUME/2);
		addCoolantNAKRecipe("ender", FluidStackHelper.EUM_DUST_VOLUME*4);
		addCoolantNAKRecipe("cryotheum", FluidStackHelper.EUM_DUST_VOLUME*4);
		addCoolantNAKRecipe("iron", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("emerald", FluidStackHelper.GEM_VOLUME*3);
		addCoolantNAKRecipe("copper", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("tin", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("magnesium", FluidStackHelper.INGOT_VOLUME*4);
		
		addFuelIsotopeRecipes("eu", "uranium", 238, 233, 235);
		addFuelIsotopeRecipes("en", "neptunium", 237, 236);
		addFuelIsotopeRecipes("ep", "plutonium", 242, 239, 241);
		addFuelIsotopeRecipes("ea", "americium", 243, 242);
		addFuelIsotopeRecipes("ecm", "curium", 246, 243, 245, 247);
		addFuelIsotopeRecipes("eb", "berkelium", 247, 248);
		addFuelIsotopeRecipes("ecf", "californium", 252, 249, 251);
		
		addElementFLIBERecipes("thorium", "uranium", "plutonium");
		
		addRecipe(fluidStack("fuel_tbu", FluidStackHelper.INGOT_VOLUME), fluidStack("thorium_230", FluidStackHelper.NUGGET_VOLUME), fluidStack("thorium", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("thorium_230_fluoride", FluidStackHelper.NUGGET_VOLUME), fluidStack("thorium_fluoride", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("fuel_tbu_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), fluidStack("thorium_230_fluoride_flibe", FluidStackHelper.NUGGET_VOLUME), fluidStack("thorium_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), 1D, 1D);

		//addIsotopeMixRecipes("uranium", 238, 235);
		//addIsotopeMixRecipes("plutonium", 239, 242);
		
		addIsotopeFLIBERecipes("thorium", 230);
		addIsotopeFLIBERecipes("uranium", 233, 235, 238);
		addIsotopeFLIBERecipes("neptunium", 236, 237);
		addIsotopeFLIBERecipes("plutonium", 238, 239, 241, 242);
		addIsotopeFLIBERecipes("americium", 241, 242, 243);
		addIsotopeFLIBERecipes("curium", 243, 245, 246, 247);
		addIsotopeFLIBERecipes("berkelium", 247, 248);
		addIsotopeFLIBERecipes("californium", 249, 250, 251, 252);
		
		addRecipe(fluidStack("fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), fluidStack("fuel_tbu_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), 1D, 1D);
		addRecipe(fluidStack("depleted_fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), fluidStack("depleted_fuel_tbu_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), 1D, 1D);
		addFissionFuelFLIBERecipes("eu", 233, 235);
		addFissionFuelFLIBERecipes("en", 236);
		addFissionFuelFLIBERecipes("ep", 239, 241);
		addFissionFuelFLIBERecipes("ea", 242);
		addFissionFuelFLIBERecipes("ecm", 243, 245, 247);
		addFissionFuelFLIBERecipes("eb", 248);
		addFissionFuelFLIBERecipes("ecf", 249, 251);
	}
	
	public void addCoolantNAKRecipe(String name, int amount) {
		addRecipe(fluidStack(name, amount), fluidStack("nak", FluidStackHelper.INGOT_VOLUME*4), fluidStack(name + "_nak", FluidStackHelper.INGOT_VOLUME*4), 1.5D, 1.5D);
	}
	
	public void addFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (String type : new String[] {"", "_fluoride", "_fluoride_flibe"}) for (int fissile : fissiles) {
			addRecipe(fluidStack(element + "_" + fertile + type, FluidStackHelper.NUGGET_VOLUME*8), fluidStack(element + "_" + fissile + type, FluidStackHelper.NUGGET_VOLUME), fluidStack("fuel_l" + suffix + "_" + fissile + type, FluidStackHelper.INGOT_VOLUME), 1D, 1D);
			addRecipe(fluidStack("fuel_l" + suffix + "_" + fissile + type, FluidStackHelper.NUGGET_VOLUME*5), fluidStack(element + "_" + fissile + type, FluidStackHelper.NUGGET_VOLUME*3), fluidStack("fuel_h" + suffix + "_" + fissile + type, FluidStackHelper.NUGGET_VOLUME*8), 1D, 1D);
		}
	}
	
	public void addElementFLIBERecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), 1D, 1D);
	}
	
	public void addIsotopeMixRecipes(String element, int major, int minor) {
		addRecipe(fluidStack(element + "_" + major, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + minor, FluidStackHelper.NUGGET_VOLUME), fluidStack(element, FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack(element + "_" + major + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + minor + "_fluoride", FluidStackHelper.NUGGET_VOLUME), fluidStack(element + "_fluoride", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack(element + "_" + major + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + minor + "_fluoride_flibe", FluidStackHelper.NUGGET_VOLUME), fluidStack(element + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
	}
	
	public void addIsotopeFLIBERecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + type + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), 1D, 1D);
	}
	
	public void addFissionFuelFLIBERecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), fluidStack(form + suffix + "_" + type + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), 1D, 1D);
		}
	}
}
