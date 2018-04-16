package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class CentrifugeRecipes extends BaseRecipeHandler {
	
	public CentrifugeRecipes() {
		super("centrifuge", 0, 1, 0, 4);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("boron", FluidHelper.INGOT_VOLUME), fluidStack("boron11", FluidHelper.INGOT_VOLUME/2), fluidStack("boron11", FluidHelper.INGOT_VOLUME/2), fluidStack("boron10", 3*FluidHelper.NUGGET_VOLUME/2), fluidStack("boron10", 3*FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[17]);
		addRecipe(fluidStack("lithium", FluidHelper.INGOT_VOLUME), fluidStack("lithium7", FluidHelper.INGOT_VOLUME/2), fluidStack("lithium7", FluidHelper.INGOT_VOLUME/2), fluidStack("lithium6", 3*FluidHelper.NUGGET_VOLUME/2), fluidStack("lithium6", 3*FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[17]);
		
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
		
		addFuelIsotopeRecipes("eu", "uranium", 238, 233, 235);
		addFuelIsotopeRecipes("en", "neptunium", 237, 236);
		addFuelIsotopeRecipes("ep", "plutonium", 242, 239, 241);
		addFuelIsotopeRecipes("ea", "americium", 243, 242);
		addFuelIsotopeRecipes("ecm", "curium", 246, 243, 245, 247);
		addFuelIsotopeRecipes("eb", "berkelium", 247, 248);
		addFuelIsotopeRecipes("ecf", "californium", 252, 249, 251);
		
		addElementFLIBERecipes("thorium", "uranium", "plutonium");
		
		addRecipe(fluidStack("thorium", FluidHelper.INGOT_VOLUME), fluidStack("fuel_tbu", FluidHelper.INGOT_VOLUME/2), fluidStack("fuel_tbu", FluidHelper.INGOT_VOLUME/2), fluidStack("thorium_230", FluidHelper.NUGGET_VOLUME/2), fluidStack("thorium_230", FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[13]);
		addRecipe(fluidStack("thorium_fluoride", FluidHelper.INGOT_VOLUME), fluidStack("fuel_tbu_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("fuel_tbu_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("thorium_230_fluoride", FluidHelper.NUGGET_VOLUME/2), fluidStack("thorium_230_fluoride", FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[13]);
		addRecipe(fluidStack("thorium_fluoride_flibe", FluidHelper.INGOT_VOLUME), fluidStack("fuel_tbu_fluoride_flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("fuel_tbu_fluoride_flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("thorium_230_fluoride_flibe", FluidHelper.NUGGET_VOLUME/2), fluidStack("thorium_230_fluoride_flibe", FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[13]);
		
		addIsotopeMixRecipes("uranium", 238, 235);
		addIsotopeMixRecipes("plutonium", 239, 242);
		
		addIsotopeFLIBERecipes("thorium", 230);
		addIsotopeFLIBERecipes("uranium", 233, 235, 238);
		addIsotopeFLIBERecipes("neptunium", 236, 237);
		addIsotopeFLIBERecipes("plutonium", 238, 239, 241, 242);
		addIsotopeFLIBERecipes("americium", 241, 242, 243);
		addIsotopeFLIBERecipes("curium", 243, 245, 246, 247);
		addIsotopeFLIBERecipes("berkelium", 247, 248);
		addIsotopeFLIBERecipes("californium", 249, 250, 251, 252);
		
		addRecipe(fluidStack("fuel_tbu_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), fluidStack("fuel_tbu_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("fuel_tbu_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[17]);
		addRecipe(fluidStack("depleted_fuel_tbu_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), fluidStack("depleted_fuel_tbu_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("depleted_fuel_tbu_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[17]);
		addFissionFuelFLIBERecipes("eu", 233, 235);
		addFissionFuelFLIBERecipes("en", 236);
		addFissionFuelFLIBERecipes("ep", 239, 241);
		addFissionFuelFLIBERecipes("ea", 242);
		addFissionFuelFLIBERecipes("ecm", 243, 245, 247);
		addFissionFuelFLIBERecipes("eb", 248);
		addFissionFuelFLIBERecipes("ecf", 249, 251);
		
		// Fuel Reprocessing
		
		addReprocessingRecipes("tbu", "uranium_233", 4, "uranium_235", 2, "neptunium_236", 2, "neptunium_237", 8);
		
		addReprocessingRecipes("leu_235", "uranium_238", 10, "neptunium_237", 2, "plutonium_239", 2, "plutonium_241", 2);
		addReprocessingRecipes("heu_235", "uranium_238", 5, "neptunium_237", 4, "plutonium_239", 1, "plutonium_242", 6);
		
		addReprocessingRecipes("leu_233", "plutonium_239", 1, "plutonium_241", 1, "plutonium_242", 8, "americium_243", 6);
		addReprocessingRecipes("heu_233", "neptunium_236", 8, "neptunium_237", 2, "plutonium_242", 4, "americium_243", 2);
		
		addReprocessingRecipes("len_236", "neptunium_237", 1, "plutonium_242", 8, "americium_242", 2, "americium_243", 5);
		addReprocessingRecipes("hen_236", "uranium_238", 4, "plutonium_238", 2, "plutonium_239", 2, "plutonium_242", 8);
		
		addReprocessingRecipes("lep_239", "plutonium_239", 2, "plutonium_242", 6, "curium_243", 1, "curium_246", 7);
		addReprocessingRecipes("hep_239", "americium_241", 2, "americium_242", 6, "curium_245", 2, "curium_246", 6);
		
		addReprocessingRecipes("lep_241", "plutonium_242", 1, "americium_242", 1, "americium_243", 2, "curium_246", 12);
		addReprocessingRecipes("hep_241", "americium_241", 2, "curium_245", 2, "curium_246", 6, "curium_247", 6);
		
		addReprocessingRecipes("lea_242", "curium_243", 2, "curium_245", 2, "curium_246", 10, "curium_247", 2);
		addReprocessingRecipes("hea_242", "curium_245", 4, "curium_246", 8, "curium_247", 2, "berkelium_247", 2);
		
		addReprocessingRecipes("lecm_243", "curium_246", 8, "berkelium_247", 4, "berkelium_248", 2, "californium_249", 2);
		addReprocessingRecipes("hecm_243", "curium_246", 6, "berkelium_247", 6, "berkelium_248", 2, "californium_249", 2);
		
		addReprocessingRecipes("lecm_245", "berkelium_247", 10, "berkelium_248", 2, "californium_249", 1, "californium_252", 3);
		addReprocessingRecipes("hecm_245", "berkelium_247", 12, "berkelium_248", 1, "californium_249", 1, "californium_251", 2);
		
		addReprocessingRecipes("lecm_247", "berkelium_247", 5, "berkelium_248", 1, "californium_251", 2, "californium_252", 8);
		addReprocessingRecipes("hecm_247", "berkelium_248", 2, "californium_249", 2, "californium_251", 6, "californium_252", 6);
		
		addReprocessingRecipes("leb_248", "californium_249", 1, "californium_251", 1, "californium_252", 7, "californium_252", 7);
		addReprocessingRecipes("heb_248", "californium_250", 2, "californium_251", 2, "californium_252", 6, "californium_252", 6);
		
		addReprocessingRecipes("lecf_249", "californium_250", 4, "californium_251", 2, "californium_252", 5, "californium_252", 5);
		addReprocessingRecipes("hecf_249", "californium_250", 8, "californium_251", 4, "californium_252", 2, "californium_252", 2);
		
		addReprocessingRecipes("lecf_251", "californium_251", 1, "californium_252", 5, "californium_252", 5, "californium_252", 5);
		addReprocessingRecipes("hecf_251", "californium_251", 4, "californium_252", 4, "californium_252", 4, "californium_252", 4);
	}
	
	public void addCoolantNAKRecipe(String name, int amount) {
		addRecipe(fluidStack(name + "_nak", FluidHelper.INGOT_VOLUME*4), fluidStack(name, amount/2), fluidStack(name, amount/2), fluidStack("nak", FluidHelper.INGOT_VOLUME*2), fluidStack("nak", FluidHelper.INGOT_VOLUME*2), NCConfig.processor_time[17]*4);
	}
	
	public void addFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (String type : new String[] {"", "_fluoride", "_fluoride_flibe"}) for (int fissile : fissiles) {
			addRecipe(fluidStack("fuel_l" + suffix + "_" + fissile + type, FluidHelper.INGOT_VOLUME), fluidStack(element + "_" + fertile + type, FluidHelper.NUGGET_VOLUME*4), fluidStack(element + "_" + fertile + type, FluidHelper.NUGGET_VOLUME*4), fluidStack(element + "_" + fissile + type, FluidHelper.NUGGET_VOLUME/2), fluidStack(element + "_" + fissile + type, FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[17]);
			addRecipe(fluidStack("fuel_h" + suffix + "_" + fissile + type, FluidHelper.INGOT_VOLUME), fluidStack(element + "_" + fertile + type, 5*FluidHelper.NUGGET_VOLUME/2), fluidStack(element + "_" + fertile + type, 5*FluidHelper.NUGGET_VOLUME/2), fluidStack(element + "_" + fissile + type, FluidHelper.NUGGET_VOLUME*2), fluidStack(element + "_" + fissile + type, FluidHelper.NUGGET_VOLUME*2), NCConfig.processor_time[17]);
		}
	}
	
	public void addElementFLIBERecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element + "_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), fluidStack(element + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[17]);
	}
	
	public void addIsotopeMixRecipes(String element, int major, int minor) {
		for (String type : new String[] {"", "_fluoride", "_fluoride_flibe"}) {
			addRecipe(fluidStack(element + type, FluidHelper.INGOT_VOLUME), fluidStack(element + "_" + major + type, FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_" + major + type, FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_" + minor + type, FluidHelper.NUGGET_VOLUME/2), fluidStack(element + "_" + minor + type, FluidHelper.NUGGET_VOLUME/2), NCConfig.processor_time[17]);
		}
	}
	
	public void addIsotopeFLIBERecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type + "_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), fluidStack(element + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack(element + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[17]);
	}
	
	public void addFissionFuelFLIBERecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type + "_fluoride_flibe", FluidHelper.INGOT_VOLUME*2), fluidStack(form + suffix + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack(form + suffix + "_" + type + "_fluoride", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), fluidStack("flibe", FluidHelper.INGOT_VOLUME/2), NCConfig.processor_time[17]);
		}
	}
	
	public void addReprocessingRecipes(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4) {
		for (String type : new String[] {"", "_fluoride", "_fluoride_flibe"}) {
			addRecipe(fluidStack("depleted_fuel_" + fuel + type, FluidHelper.INGOT_BLOCK_VOLUME/4), fluidStack(out1 + type, FluidHelper.NUGGET_VOLUME*n1), fluidStack(out2 + type, FluidHelper.NUGGET_VOLUME*n2), fluidStack(out3 + type, FluidHelper.NUGGET_VOLUME*n3), fluidStack(out4 + type, FluidHelper.NUGGET_VOLUME*n4), NCConfig.processor_time[17]);
		}
	}
}
