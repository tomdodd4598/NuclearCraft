package nc.recipe.processor;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class CentrifugeRecipes extends ProcessorRecipeHandler {
	
	public CentrifugeRecipes() {
		super("centrifuge", 0, 1, 0, 4);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("boron", FluidStackHelper.INGOT_VOLUME), fluidStack("boron11", FluidStackHelper.INGOT_VOLUME), fluidStack("boron10", 3*FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("lithium", FluidStackHelper.INGOT_VOLUME), fluidStack("lithium7", FluidStackHelper.INGOT_VOLUME), fluidStack("lithium6", 3*FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		
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
		
		//addElementFLIBERecipes("thorium", "uranium", "plutonium");
		
		addRecipe(fluidStack("thorium", FluidStackHelper.INGOT_VOLUME), fluidStack("fuel_tbu", FluidStackHelper.INGOT_VOLUME), fluidStack("thorium_230", FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("thorium_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("thorium_230_fluoride", FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("thorium_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), fluidStack("fuel_tbu_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), fluidStack("thorium_230_fluoride_flibe", FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		
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
		
		addRecipe(fluidStack("fuel_tbu_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), fluidStack("fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("depleted_fuel_tbu_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), fluidStack("depleted_fuel_tbu_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
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
		addRecipe(fluidStack(name + "_nak", FluidStackHelper.INGOT_VOLUME*4), fluidStack(name, amount), fluidStack("nak", FluidStackHelper.INGOT_VOLUME*4), emptyFluidStack(), emptyFluidStack(), 2D, 1D);
	}
	
	public void addFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (String type : new String[] {"", "_fluoride"/*, "_fluoride_flibe"*/}) for (int fissile : fissiles) {
			addRecipe(fluidStack("fuel_l" + suffix + "_" + fissile + type, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + fertile + type, FluidStackHelper.NUGGET_VOLUME*8), fluidStack(element + "_" + fissile + type, FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
			addRecipe(fluidStack("fuel_h" + suffix + "_" + fissile + type, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + fertile + type, FluidStackHelper.NUGGET_VOLUME*5), fluidStack(element + "_" + fissile + type, FluidStackHelper.NUGGET_VOLUME*4), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
	}
	
	public void addElementFLIBERecipes(String... elements) {
		for (String element : elements) addRecipe(fluidStack(element + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), fluidStack(element + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
	}
	
	public void addIsotopeMixRecipes(String element, int major, int minor) {
		for (String type : new String[] {"", "_fluoride", "_fluoride_flibe"}) {
			addRecipe(fluidStack(element + type, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + major + type, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + minor + type, FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
	}
	
	public void addIsotopeFLIBERecipes(String element, int... types) {
		for (int type : types) addRecipe(fluidStack(element + "_" + type + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), fluidStack(element + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
	}
	
	public void addFissionFuelFLIBERecipes(String suffix, int... types) {
		for (int type : types) for (String form : new String[] {"fuel_l", "fuel_h", "depleted_fuel_l", "depleted_fuel_h"}) {
			addRecipe(fluidStack(form + suffix + "_" + type + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME*2), fluidStack(form + suffix + "_" + type + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
	}
	
	public void addReprocessingRecipes(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4) {
		for (String type : new String[] {""/*, "_fluoride", "_fluoride_flibe"*/}) {
			addRecipe(fluidStack("depleted_fuel_" + fuel + type, FluidStackHelper.INGOT_BLOCK_VOLUME/4), fluidStack(out1 + type, 16*n1), fluidStack(out2 + type, 16*n2), fluidStack(out3 + type, 16*n3), fluidStack(out4 + type, 16*n4), 0.5D, 0.5D);
		}
	}
}
