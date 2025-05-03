package nc.recipe.processor;

import nc.util.NCMath;

import java.util.*;

import static nc.config.NCConfig.*;
import static nc.util.FissionHelper.FISSION_FLUID;
import static nc.util.FluidStackHelper.*;

public class CentrifugeRecipes extends BasicProcessorRecipeHandler {
	
	public CentrifugeRecipes() {
		super("centrifuge", 0, 1, 0, 6);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[17]) {
			return;
		}
		
		addRecipe(fluidStack("boron", NUGGET_VOLUME * 12), fluidStack("boron_11", INGOT_VOLUME), fluidStack("boron_10", NUGGET_VOLUME * 3), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 4D / 3D, 1D);
		addRecipe(fluidStack("lithium", NUGGET_VOLUME * 10), fluidStack("lithium_7", INGOT_VOLUME), fluidStack("lithium_6", NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 10D / 9D, 1D);
		addRecipe(fluidStack("redstone_ethanol", BUCKET_VOLUME / 2), fluidStack("ethanol", BUCKET_VOLUME / 2), fluidStack("redstone", REDSTONE_DUST_VOLUME * 4), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 0.5D);
		
		addCoolantNAKRecipe("iron", INGOT_VOLUME);
		addCoolantNAKRecipe("redstone", REDSTONE_DUST_VOLUME * 2);
		addCoolantNAKRecipe("quartz", GEM_VOLUME * 2);
		addCoolantNAKRecipe("obsidian", SEARED_MATERIAL_VOLUME * 5);
		addCoolantNAKRecipe("nether_brick", SEARED_MATERIAL_VOLUME * 5);
		addCoolantNAKRecipe("glowstone", GLOWSTONE_DUST_VOLUME * 2);
		addCoolantNAKRecipe("lapis", GEM_VOLUME * 2);
		addCoolantNAKRecipe("gold", INGOT_VOLUME);
		addCoolantNAKRecipe("prismarine", INGOT_VOLUME);
		addCoolantNAKRecipe("slime", INGOT_VOLUME * 2);
		addCoolantNAKRecipe("end_stone", SEARED_MATERIAL_VOLUME * 5);
		addCoolantNAKRecipe("purpur", SEARED_MATERIAL_VOLUME * 5);
		addCoolantNAKRecipe("diamond", GEM_VOLUME);
		addCoolantNAKRecipe("emerald", GEM_VOLUME);
		addCoolantNAKRecipe("copper", INGOT_VOLUME);
		addCoolantNAKRecipe("tin", INGOT_VOLUME);
		addCoolantNAKRecipe("lead", INGOT_VOLUME);
		addCoolantNAKRecipe("boron", INGOT_VOLUME);
		addCoolantNAKRecipe("lithium", INGOT_VOLUME);
		addCoolantNAKRecipe("magnesium", INGOT_VOLUME);
		addCoolantNAKRecipe("manganese", INGOT_VOLUME);
		addCoolantNAKRecipe("aluminum", INGOT_VOLUME);
		addCoolantNAKRecipe("silver", INGOT_VOLUME);
		addCoolantNAKRecipe("fluorite", GEM_VOLUME * 2);
		addCoolantNAKRecipe("villiaumite", GEM_VOLUME * 2);
		addCoolantNAKRecipe("carobbiite", GEM_VOLUME * 2);
		addCoolantNAKRecipe("arsenic", GEM_VOLUME * 2);
		addCoolantNAKRecipe("liquid_nitrogen", BUCKET_VOLUME / 4);
		addCoolantNAKRecipe("liquid_helium", BUCKET_VOLUME / 4);
		addCoolantNAKRecipe("enderium", INGOT_VOLUME);
		addCoolantNAKRecipe("cryotheum", EUM_DUST_VOLUME);
		
		// Fission Materials
		
		addRecipe(fluidStack("uranium", NUGGET_VOLUME * 10), fluidStack("uranium_238", INGOT_VOLUME), fluidStack("uranium_235", NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 10D / 9D, 1D);
		
		addFissionFuelIsotopeRecipes("u", "uranium", 238, 233, 235);
		addFissionFuelIsotopeRecipes("n", "neptunium", 237, 236);
		addFissionFuelIsotopeRecipes("p", "plutonium", 242, 239, 241);
		for (int fissile : new int[] {239, 241}) {
			addRecipe(fluidStack("mix_" + fissile, INGOT_VOLUME), fluidStack("uranium_238", NUGGET_VOLUME * 8), fluidStack("plutonium_" + fissile, NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
		addFissionFuelIsotopeRecipes("a", "americium", 243, 242);
		addFissionFuelIsotopeRecipes("cm", "curium", 246, 243, 245, 247);
		addFissionFuelIsotopeRecipes("b", "berkelium", 247, 248);
		addFissionFuelIsotopeRecipes("cf", "californium", 252, 249, 251);
		
		addFissionFLIBERecipes();
		
		// Fuel Reprocessing
		
		addReprocessingRecipe("tbu", "uranium_233", 1, "uranium_238", 5, "neptunium_236", 1, "neptunium_237", 1, "strontium_90", "caesium_137", 0.5D, 50);
		
		addReprocessingRecipe("leu_233", "uranium_238", 5, "plutonium_241", 1, "plutonium_242", 1, "americium_243", 1, "strontium_90", "caesium_137", 0.5D, 50);
		addReprocessingRecipe("heu_233", "uranium_235", 1, "uranium_238", 2, "plutonium_242", 3, "americium_243", 1, "strontium_90", "caesium_137", 1.5D, 50);
		addReprocessingRecipe("leu_235", "uranium_238", 4, "plutonium_239", 1, "plutonium_242", 2, "americium_243", 1, "molybdenum", "caesium_137", 0.5D, 50);
		addReprocessingRecipe("heu_235", "uranium_238", 3, "neptunium_236", 1, "plutonium_242", 2, "americium_243", 1, "molybdenum", "caesium_137", 1.5D, 50);
		
		addReprocessingRecipe("len_236", "uranium_238", 4, "neptunium_237", 1, "plutonium_241", 1, "plutonium_242", 2, "molybdenum", "caesium_137", 0.5D, 50);
		addReprocessingRecipe("hen_236", "uranium_238", 4, "plutonium_238", 1, "plutonium_241", 1, "plutonium_242", 1, "molybdenum", "caesium_137", 1.5D, 50);
		
		addReprocessingRecipe("lep_239", "plutonium_242", 5, "americium_242", 1, "americium_243", 1, "curium_246", 1, "strontium_90", "promethium_147", 0.5D, 50);
		addReprocessingRecipe("hep_239", "plutonium_241", 1, "americium_242", 1, "americium_243", 4, "curium_243", 1, "strontium_90", "promethium_147", 1.5D, 50);
		addReprocessingRecipe("lep_241", "plutonium_242", 5, "americium_243", 1, "curium_246", 1, "berkelium_247", 1, "strontium_90", "promethium_147", 0.5D, 50);
		addReprocessingRecipe("hep_241", "americium_241", 1, "americium_242", 1, "americium_243", 3, "curium_246", 2, "strontium_90", "promethium_147", 1.5D, 50);
		
		addReprocessingRecipe("mix_239", "uranium_238", 4, "plutonium_241", 1, "plutonium_242", 2, "americium_243", 1, "strontium_90", "promethium_147", 0.5D, 50);
		addReprocessingRecipe("mix_241", "uranium_238", 3, "plutonium_241", 1, "plutonium_242", 3, "americium_243", 1, "strontium_90", "promethium_147", 0.5D, 50);
		
		addReprocessingRecipe("lea_242", "americium_243", 3, "curium_245", 1, "curium_246", 3, "berkelium_248", 1, "molybdenum", "promethium_147", 0.5D, 50);
		addReprocessingRecipe("hea_242", "americium_243", 3, "curium_243", 1, "curium_246", 2, "berkelium_247", 1, "molybdenum", "promethium_147", 1.5D, 50);
		
		addReprocessingRecipe("lecm_243", "curium_246", 4, "curium_247", 1, "berkelium_247", 2, "berkelium_248", 1, "molybdenum", "promethium_147", 0.5D, 50);
		addReprocessingRecipe("hecm_243", "curium_245", 1, "curium_246", 3, "berkelium_247", 2, "berkelium_248", 1, "molybdenum", "promethium_147", 1.5D, 50);
		addReprocessingRecipe("lecm_245", "curium_246", 4, "curium_247", 1, "berkelium_247", 2, "californium_249", 1, "molybdenum", "europium_155", 0.5D, 60);
		addReprocessingRecipe("hecm_245", "curium_246", 3, "curium_247", 1, "berkelium_247", 2, "californium_249", 1, "molybdenum", "europium_155", 1.5D, 60);
		addReprocessingRecipe("lecm_247", "curium_246", 5, "berkelium_247", 1, "berkelium_248", 1, "californium_249", 1, "molybdenum", "europium_155", 0.5D, 60);
		addReprocessingRecipe("hecm_247", "berkelium_247", 4, "berkelium_248", 1, "californium_249", 1, "californium_251", 1, "molybdenum", "europium_155", 1.5D, 60);
		
		addReprocessingRecipe("leb_248", "berkelium_247", 5, "berkelium_248", 1, "californium_249", 1, "californium_251", 1, "ruthenium_106", "promethium_147", 0.5D, 60);
		addReprocessingRecipe("heb_248", "berkelium_248", 1, "californium_249", 1, "californium_251", 2, "californium_252", 3, "ruthenium_106", "promethium_147", 1.5D, 60);
		
		addReprocessingRecipe("lecf_249", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 2, "ruthenium_106", "promethium_147", 0.5D, 60);
		addReprocessingRecipe("hecf_249", "californium_250", 1, "californium_252", 2, "californium_252", 2, "californium_252", 2, "ruthenium_106", "promethium_147", 1.5D, 60);
		addReprocessingRecipe("lecf_251", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 2, "ruthenium_106", "europium_155", 0.5D, 60);
		addReprocessingRecipe("hecf_251", "californium_252", 2, "californium_252", 2, "californium_252", 2, "californium_252", 1, "ruthenium_106", "europium_155", 1.5D, 60);
	}
	
	public void addCoolantNAKRecipe(String fluidName, int amount) {
		addRecipe(fluidStack(fluidName + "_nak", INGOT_VOLUME), fluidStack(fluidName, amount), fluidStack("nak", INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
	}
	
	public void addFissionFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (int fissile : fissiles) {
			addRecipe(fluidStack("le" + suffix + "_" + fissile, INGOT_VOLUME), fluidStack(element + "_" + fertile, NUGGET_VOLUME * 8), fluidStack(element + "_" + fissile, NUGGET_VOLUME), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
			addRecipe(fluidStack("he" + suffix + "_" + fissile, INGOT_VOLUME), fluidStack(element + "_" + fertile, NUGGET_VOLUME * 6), fluidStack(element + "_" + fissile, NUGGET_VOLUME * 3), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		}
	}
	
	public void addFissionFLIBERecipes() {
		for (String element : FISSION_FLUID) {
			addRecipe(fluidStack(element + "_fluoride_flibe", INGOT_VOLUME / 2), fluidStack(element + "_fluoride", INGOT_VOLUME / 2), fluidStack("flibe", INGOT_VOLUME / 2), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
			addRecipe(fluidStack("depleted_" + element + "_fluoride_flibe", INGOT_VOLUME / 2), fluidStack("depleted_" + element + "_fluoride", INGOT_VOLUME / 2), fluidStack("flibe", INGOT_VOLUME / 2), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		}
	}
	
	public void addReprocessingRecipe(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4, String waste1, String waste2, double w, int r) {
		addRecipe(fluidStack("depleted_" + fuel, INGOT_VOLUME), fluidStack(out1, NUGGET_VOLUME * n1), fluidStack(out2, NUGGET_VOLUME * n2), chanceFluidStack(waste1, NUGGET_VOLUME, NCMath.toInt(w * r), NUGGET_VOLUME), fluidStack(out3, NUGGET_VOLUME * n3), fluidStack(out4, NUGGET_VOLUME * n4), chanceFluidStack(waste2, NUGGET_VOLUME, (int) (w * (100 - r)), NUGGET_VOLUME), 1D, 1D);
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
