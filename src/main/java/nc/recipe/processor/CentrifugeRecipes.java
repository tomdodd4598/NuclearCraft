package nc.recipe.processor;

import static nc.util.FissionHelper.FUEL_FLUID;

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
		addCoolantNAKRecipe("liquid_helium", FluidStackHelper.BUCKET_VOLUME/2);
		addCoolantNAKRecipe("enderium", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("cryotheum", FluidStackHelper.EUM_DUST_VOLUME*4);
		addCoolantNAKRecipe("iron", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("emerald", FluidStackHelper.GEM_VOLUME*3);
		addCoolantNAKRecipe("copper", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("tin", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("magnesium", FluidStackHelper.INGOT_VOLUME*4);
		
		addRecipe(fluidStack("uranium", FluidStackHelper.NUGGET_VOLUME*10), fluidStack("uranium_238", FluidStackHelper.INGOT_VOLUME), fluidStack("uranium_235", FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 10D/9D, 1D);
		
		addFuelIsotopeRecipes("u", "uranium", 238, 233, 235);
		addFuelIsotopeRecipes("n", "neptunium", 237, 236);
		addFuelIsotopeRecipes("p", "plutonium", 242, 239, 241);
		for (int fissile : new int[] {239, 241}) {
			addRecipe(fluidStack("fuel_mix_" + fissile, FluidStackHelper.INGOT_VOLUME), fluidStack("uranium_238", FluidStackHelper.NUGGET_VOLUME*8), fluidStack("plutonium_" + fissile, FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
		addFuelIsotopeRecipes("a", "americium", 243, 242);
		addFuelIsotopeRecipes("cm", "curium", 246, 243, 245, 247);
		addFuelIsotopeRecipes("b", "berkelium", 247, 248);
		addFuelIsotopeRecipes("cf", "californium", 252, 249, 251);
		
		addFissionFuelFLIBERecipes();
		
		// Fuel Reprocessing
		
		addReprocessingRecipe("tbu", "uranium_233", 1, "uranium_238", 5, "neptunium_236", 1, "neptunium_237", 1);
		
		addReprocessingRecipe("leu_233", "uranium_238", 5, "plutonium_241", 1, "plutonium_242", 1, "americium_243", 1);
		addReprocessingRecipe("heu_233", "uranium_235", 1, "uranium_238", 2, "plutonium_242", 3, "americium_243", 1);
		addReprocessingRecipe("leu_235", "uranium_238", 4, "plutonium_239", 1, "plutonium_242", 2, "americium_243", 1);
		addReprocessingRecipe("heu_235", "uranium_238", 3, "neptunium_236", 1, "plutonium_242", 2, "americium_243", 1);
		
		addReprocessingRecipe("len_236", "uranium_238", 4, "neptunium_237", 1, "plutonium_241", 1, "plutonium_242", 2);
		addReprocessingRecipe("hen_236", "uranium_238", 4, "plutonium_238", 1, "plutonium_241", 1, "plutonium_242", 1);
		
		addReprocessingRecipe("lep_239", "plutonium_242", 5, "americium_242", 1, "americium_243", 1, "curium_246", 1);
		addReprocessingRecipe("hep_239", "plutonium_241", 1, "americium_242", 1, "americium_243", 4, "curium_243", 1);
		addReprocessingRecipe("lep_241", "plutonium_242", 5, "americium_243", 1, "curium_246", 1, "berkelium_247", 1);
		addReprocessingRecipe("hep_241", "americium_241", 1, "americium_242", 1, "americium_243", 3, "curium_246", 2);
		
		addReprocessingRecipe("mix_239", "uranium_238", 4, "plutonium_241", 1, "plutonium_242", 2, "americium_243", 1);
		addReprocessingRecipe("mix_241", "uranium_238", 4, "plutonium_241", 1, "plutonium_242", 2, "americium_243", 1);
		
		addReprocessingRecipe("lea_242", "americium_243", 4, "curium_245", 1, "curium_246", 2, "berkelium_247", 1);
		addReprocessingRecipe("hea_242", "americium_243", 3, "curium_243", 1, "curium_246", 4, "berkelium_247", 1);
		
		addReprocessingRecipe("lecm_243", "curium_246", 4, "curium_247", 1, "berkelium_247", 2, "berkelium_248", 1);
		addReprocessingRecipe("hecm_243", "curium_245", 1, "curium_246", 3, "berkelium_247", 2, "berkelium_248", 1);
		addReprocessingRecipe("lecm_245", "curium_246", 4, "curium_247", 1, "berkelium_247", 2, "californium_249", 1);
		addReprocessingRecipe("hecm_245", "curium_246", 3, "curium_247", 1, "berkelium_247", 2, "californium_249", 1);
		addReprocessingRecipe("lecm_247", "curium_246", 5, "berkelium_247", 1, "berkelium_248", 1, "californium_249", 1);
		addReprocessingRecipe("hecm_247", "berkelium_247", 4, "berkelium_248", 1, "californium_249", 1, "californium_251", 1);
		
		addReprocessingRecipe("leb_248", "berkelium_247", 5, "berkelium_248", 1, "californium_249", 1, "californium_251", 1);
		addReprocessingRecipe("heb_248", "berkelium_248", 1, "californium_249", 1, "californium_251", 2, "californium_252", 3);
		
		addReprocessingRecipe("lecf_249", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 2);
		addReprocessingRecipe("hecf_249", "californium_250", 1, "californium_252", 2, "californium_252", 2, "californium_252", 2);
		addReprocessingRecipe("lecf_251", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 2);
		addReprocessingRecipe("hecf_251", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 1);
	}
	
	public void addCoolantNAKRecipe(String name, int amount) {
		addRecipe(fluidStack(name + "_nak", FluidStackHelper.INGOT_VOLUME*4), fluidStack(name, amount), fluidStack("nak", FluidStackHelper.INGOT_VOLUME*4), emptyFluidStack(), emptyFluidStack(), 2D, 1D);
	}
	
	public void addFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (int fissile : fissiles) {
			addRecipe(fluidStack("fuel_le" + suffix + "_" + fissile, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + fertile, FluidStackHelper.NUGGET_VOLUME*8), fluidStack(element + "_" + fissile, FluidStackHelper.NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
			addRecipe(fluidStack("fuel_he" + suffix + "_" + fissile, FluidStackHelper.INGOT_VOLUME), fluidStack(element + "_" + fertile, FluidStackHelper.NUGGET_VOLUME*6), fluidStack(element + "_" + fissile, FluidStackHelper.NUGGET_VOLUME*3), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
	}
	
	public void addFissionFuelFLIBERecipes() {
		for (int i = 0; i < FUEL_FLUID.length; i++) {
			for (String form : new String[] {"fuel_", "depleted_fuel_"}) {
				addRecipe(fluidStack(form + FUEL_FLUID[i] + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), fluidStack(form + FUEL_FLUID[i] + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
			}
		}
	}
	
	public void addReprocessingRecipe(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4) {
		addRecipe(fluidStack("depleted_fuel_" + fuel, FluidStackHelper.INGOT_BLOCK_VOLUME), fluidStack(out1, FluidStackHelper.INGOT_VOLUME*n1), fluidStack(out2, FluidStackHelper.INGOT_VOLUME*n2), fluidStack(out3, FluidStackHelper.INGOT_VOLUME*n3), fluidStack(out4, FluidStackHelper.INGOT_VOLUME*n4), 1D, 1D);
	}
}
