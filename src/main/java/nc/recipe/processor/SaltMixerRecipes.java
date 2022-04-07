package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_FLUID;
import static nc.util.FluidStackHelper.*;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class SaltMixerRecipes extends BasicRecipeHandler {
	
	public SaltMixerRecipes() {
		super("salt_mixer", 0, 2, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("lif", INGOT_VOLUME), fluidStack("bef2", INGOT_VOLUME / 2), fluidStack("flibe", INGOT_VOLUME / 2), 0.5D, 1D);
		addRecipe(fluidStack("sodium", INGOT_VOLUME / 2), fluidStack("potassium", INGOT_VOLUME * 2), fluidStack("nak", INGOT_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("boron_11", INGOT_VOLUME), fluidStack("boron_10", NUGGET_VOLUME * 3), fluidStack("boron", NUGGET_VOLUME * 12), 4D / 3D, 1D);
		addRecipe(fluidStack("lithium_7", INGOT_VOLUME), fluidStack("lithium_6", NUGGET_VOLUME), fluidStack("lithium", NUGGET_VOLUME * 10), 10D / 9D, 1D);
		
		addRecipe(fluidStack("steel", INGOT_VOLUME / 2), fluidStack("boron", INGOT_VOLUME / 2), fluidStack("ferroboron", INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("ferroboron", INGOT_VOLUME / 2), fluidStack("lithium", INGOT_VOLUME / 2), fluidStack("tough", INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("coal", COAL_DUST_VOLUME), fluidStack("diamond", GEM_VOLUME / 2), fluidStack("hardCarbon", INGOT_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("lead_platinum", INGOT_VOLUME), fluidStack("ender", EUM_DUST_VOLUME), fluidStack("enderium", INGOT_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("radaway", BUCKET_VOLUME / 4), fluidStack("redstone", REDSTONE_DUST_VOLUME * 2), fluidStack("radaway_slow", BUCKET_VOLUME / 4), 1D, 0.5D);
		addRecipe(fluidStack("ethanol", BUCKET_VOLUME / 4), fluidStack("redstone", REDSTONE_DUST_VOLUME * 2), fluidStack("redstone_ethanol", BUCKET_VOLUME / 4), 1D, 0.5D);
		
		addRecipe(fluidStack("ice", BUCKET_VOLUME), fluidStack("ethanol", BUCKET_VOLUME / 4), fluidStack("slurry_ice", BUCKET_VOLUME), 1D, 1D);
		addRecipe(fluidStack("slurry_ice", BUCKET_VOLUME), fluidStack("cryotheum", EUM_DUST_VOLUME), fluidStack("emergency_coolant", BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("chocolate_liquor", INGOT_VOLUME / 2), fluidStack("cocoa_butter", INGOT_VOLUME / 2), fluidStack("unsweetened_chocolate", INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe(fluidStack("unsweetened_chocolate", INGOT_VOLUME), fluidStack("sugar", INGOT_VOLUME / 2), fluidStack("dark_chocolate", INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe(fluidStack("dark_chocolate", INGOT_VOLUME), fluidStack("milk", BUCKET_VOLUME / 4), fluidStack("milk_chocolate", INGOT_VOLUME * 2), 0.5D, 0.5D);
		
		addRecipe(fluidStack("gelatin", INGOT_VOLUME / 2), fluidStack("water", BUCKET_VOLUME / 4), fluidStack("hydrated_gelatin", INGOT_VOLUME / 2), 0.5D, 0.5D);
		addRecipe(fluidStack("hydrated_gelatin", INGOT_VOLUME), fluidStack("sugar", INGOT_VOLUME / 2), fluidStack("marshmallow", INGOT_VOLUME), 1D, 0.5D);
		
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
		
		// addRecipe(fluidStack("uranium_238", INGOT_VOLUME), fluidStack("uranium_235", NUGGET_VOLUME), fluidStack("uranium", NUGGET_VOLUME*10), 10D/9D, 1D);
		
		addFissionFuelIsotopeRecipes("u", "uranium", 238, 233, 235);
		addFissionFuelIsotopeRecipes("n", "neptunium", 237, 236);
		addFissionFuelIsotopeRecipes("p", "plutonium", 242, 239, 241);
		for (int fissile : new int[] {239, 241}) {
			addRecipe(fluidStack("uranium_238", NUGGET_VOLUME * 8), fluidStack("plutonium_" + fissile, NUGGET_VOLUME), fluidStack("fuel_mix_" + fissile, INGOT_VOLUME), 1D, 1D);
		}
		addFissionFuelIsotopeRecipes("a", "americium", 243, 242);
		addFissionFuelIsotopeRecipes("cm", "curium", 246, 243, 245, 247);
		addFissionFuelIsotopeRecipes("b", "berkelium", 247, 248);
		addFissionFuelIsotopeRecipes("cf", "californium", 252, 249, 251);
		
		addFissionFLIBERecipes();
	}
	
	public void addCoolantNAKRecipe(String fluidName, int amount) {
		addRecipe(fluidStack(fluidName, amount), fluidStack("nak", INGOT_VOLUME), fluidStack(fluidName + "_nak", INGOT_VOLUME), 0.5D, 1D);
	}
	
	public void addFissionFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (int fissile : fissiles) {
			addRecipe(fluidStack(element + "_" + fertile, NUGGET_VOLUME * 8), fluidStack(element + "_" + fissile, NUGGET_VOLUME), fluidStack("le" + suffix + "_" + fissile, INGOT_VOLUME), 1D, 1D);
			addRecipe(fluidStack("le" + suffix + "_" + fissile, NUGGET_VOLUME * 3), fluidStack(element + "_" + fissile, NUGGET_VOLUME), fluidStack("he" + suffix + "_" + fissile, NUGGET_VOLUME * 4), 0.5D, 1D);
		}
	}
	
	public void addFissionFLIBERecipes() {
		for (String element : FISSION_FLUID) {
			addRecipe(fluidStack(element + "_fluoride", INGOT_VOLUME / 2), fluidStack("flibe", INGOT_VOLUME / 2), fluidStack(element + "_fluoride_flibe", INGOT_VOLUME / 2), 0.5D, 1D);
		}
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
