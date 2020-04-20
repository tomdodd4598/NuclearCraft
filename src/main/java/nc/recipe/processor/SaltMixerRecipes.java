package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_FLUID;

import java.util.ArrayList;
import java.util.List;

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
		
		addRecipe(fluidStack("boron11", FluidStackHelper.INGOT_VOLUME), fluidStack("boron10", FluidStackHelper.NUGGET_VOLUME*3), fluidStack("boron", FluidStackHelper.NUGGET_VOLUME*12), 4D/3D, 1D);
		addRecipe(fluidStack("lithium7", FluidStackHelper.INGOT_VOLUME), fluidStack("lithium6", FluidStackHelper.NUGGET_VOLUME), fluidStack("lithium", FluidStackHelper.NUGGET_VOLUME*10), 10D/9D, 1D);
		
		addRecipe(fluidStack("steel", FluidStackHelper.INGOT_VOLUME/2), fluidStack("boron", FluidStackHelper.INGOT_VOLUME/2), fluidStack("ferroboron", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("ferroboron", FluidStackHelper.INGOT_VOLUME/2), fluidStack("lithium", FluidStackHelper.INGOT_VOLUME/2), fluidStack("tough", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME), fluidStack("diamond", FluidStackHelper.GEM_VOLUME/2), fluidStack("hardCarbon", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("lead_platinum", FluidStackHelper.INGOT_VOLUME), fluidStack("ender", FluidStackHelper.EUM_DUST_VOLUME), fluidStack("enderium", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("radaway", FluidStackHelper.BUCKET_VOLUME/4), fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*2), fluidStack("radaway_slow", FluidStackHelper.BUCKET_VOLUME/4), 1D, 0.5D);
		addRecipe(fluidStack("ethanol", FluidStackHelper.BUCKET_VOLUME/4), fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*2), fluidStack("redstone_ethanol", FluidStackHelper.BUCKET_VOLUME/4), 1D, 0.5D);
		
		addRecipe(fluidStack("ice", FluidStackHelper.BUCKET_VOLUME), fluidStack("ethanol", FluidStackHelper.BUCKET_VOLUME/4), fluidStack("slurry_ice", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(fluidStack("slurry_ice", FluidStackHelper.BUCKET_VOLUME), fluidStack("cryotheum", FluidStackHelper.EUM_DUST_VOLUME), fluidStack("emergency_coolant", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("chocolate_liquor", FluidStackHelper.INGOT_VOLUME), fluidStack("cocoa_butter", FluidStackHelper.INGOT_VOLUME), fluidStack("unsweetened_chocolate", FluidStackHelper.INGOT_VOLUME*2), 1D, 0.5D);
		addRecipe(fluidStack("unsweetened_chocolate", FluidStackHelper.INGOT_VOLUME), fluidStack("sugar", FluidStackHelper.INGOT_VOLUME/2), fluidStack("dark_chocolate", FluidStackHelper.INGOT_VOLUME), 1D, 0.5D);
		addRecipe(fluidStack("dark_chocolate", FluidStackHelper.INGOT_VOLUME), fluidStack("milk", FluidStackHelper.BUCKET_VOLUME/4), fluidStack("milk_chocolate", FluidStackHelper.INGOT_VOLUME*2), 0.5D, 0.5D);
		
		addRecipe(fluidStack("gelatin", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("hydrated_gelatin", FluidStackHelper.INGOT_VOLUME), 1D, 0.5D);
		addRecipe(fluidStack("hydrated_gelatin", FluidStackHelper.INGOT_VOLUME), fluidStack("sugar", FluidStackHelper.INGOT_VOLUME/2), fluidStack("marshmallow", FluidStackHelper.INGOT_VOLUME), 1D, 0.5D);
		
		addCoolantNAKRecipe("iron", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*8);
		addCoolantNAKRecipe("quartz", FluidStackHelper.GEM_VOLUME*8);
		addCoolantNAKRecipe("obsidian", FluidStackHelper.SEARED_BLOCK_VOLUME*5);
		addCoolantNAKRecipe("nether_brick", FluidStackHelper.SEARED_BLOCK_VOLUME*5);
		addCoolantNAKRecipe("glowstone", FluidStackHelper.GLOWSTONE_DUST_VOLUME*8);
		addCoolantNAKRecipe("lapis", FluidStackHelper.GEM_VOLUME*8);
		addCoolantNAKRecipe("gold", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("prismarine", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("slime", FluidStackHelper.INGOT_VOLUME*8);
		addCoolantNAKRecipe("end_stone", FluidStackHelper.SEARED_BLOCK_VOLUME*5);
		addCoolantNAKRecipe("purpur", FluidStackHelper.SEARED_BLOCK_VOLUME*5);
		addCoolantNAKRecipe("diamond", FluidStackHelper.GEM_VOLUME*4);
		addCoolantNAKRecipe("emerald", FluidStackHelper.GEM_VOLUME*4);
		addCoolantNAKRecipe("copper", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("tin", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("lead", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("boron", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("lithium", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("magnesium", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("manganese", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("aluminum", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("silver", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("fluorite", FluidStackHelper.GEM_VOLUME*8);
		addCoolantNAKRecipe("villiaumite", FluidStackHelper.GEM_VOLUME*8);
		addCoolantNAKRecipe("carobbiite", FluidStackHelper.GEM_VOLUME*8);
		addCoolantNAKRecipe("arsenic", FluidStackHelper.GEM_VOLUME*8);
		addCoolantNAKRecipe("liquid_nitrogen", FluidStackHelper.BUCKET_VOLUME);
		addCoolantNAKRecipe("liquid_helium", FluidStackHelper.BUCKET_VOLUME);
		addCoolantNAKRecipe("enderium", FluidStackHelper.INGOT_VOLUME*4);
		addCoolantNAKRecipe("cryotheum", FluidStackHelper.EUM_DUST_VOLUME*4);
		
		/* ========================================= Fission Materials ========================================= */
		
		//addRecipe(fluidStack("uranium_238", FluidStackHelper.INGOT_VOLUME), fluidStack("uranium_235", FluidStackHelper.NUGGET_VOLUME), fluidStack("uranium", FluidStackHelper.NUGGET_VOLUME*10), 10D/9D, 1D);
		
		addFissionFuelIsotopeRecipes("u", "uranium", 238, 233, 235);
		addFissionFuelIsotopeRecipes("n", "neptunium", 237, 236);
		addFissionFuelIsotopeRecipes("p", "plutonium", 242, 239, 241);
		for (int fissile : new int[] {239, 241}) {
			addRecipe(fluidStack("uranium_238", FluidStackHelper.NUGGET_VOLUME*8), fluidStack("plutonium_" + fissile, FluidStackHelper.NUGGET_VOLUME), fluidStack("fuel_mix_" + fissile, FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		}
		addFissionFuelIsotopeRecipes("a", "americium", 243, 242);
		addFissionFuelIsotopeRecipes("cm", "curium", 246, 243, 245, 247);
		addFissionFuelIsotopeRecipes("b", "berkelium", 247, 248);
		addFissionFuelIsotopeRecipes("cf", "californium", 252, 249, 251);
		
		addFissionFLIBERecipes();
	}
	
	public void addCoolantNAKRecipe(String name, int amount) {
		addRecipe(fluidStack(name, amount), fluidStack("nak", FluidStackHelper.INGOT_VOLUME*4), fluidStack(name + "_nak", FluidStackHelper.INGOT_VOLUME*4), 1.5D, 1.5D);
	}
	
	public void addFissionFuelIsotopeRecipes(String suffix, String element, int fertile, int... fissiles) {
		for (int fissile : fissiles) {
			addRecipe(fluidStack(element + "_" + fertile, FluidStackHelper.NUGGET_VOLUME*8), fluidStack(element + "_" + fissile, FluidStackHelper.NUGGET_VOLUME), fluidStack("le" + suffix + "_" + fissile, FluidStackHelper.INGOT_VOLUME), 1D, 1D);
			addRecipe(fluidStack("le" + suffix + "_" + fissile, FluidStackHelper.NUGGET_VOLUME*3), fluidStack(element + "_" + fissile, FluidStackHelper.NUGGET_VOLUME), fluidStack("he" + suffix + "_" + fissile, FluidStackHelper.NUGGET_VOLUME*4), 0.5D, 1D);
		}
	}
	
	public void addFissionFLIBERecipes() {
		for (int i = 0; i < FISSION_FLUID.length; i++) {
			addRecipe(fluidStack(FISSION_FLUID[i] + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack("flibe", FluidStackHelper.INGOT_VOLUME), fluidStack(FISSION_FLUID[i] + "_fluoride_flibe", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
	
	@Override
	public List getFactoredExtras(List extras, int factor) {
		List factored = new ArrayList(extras);
		factored.set(0, (double)extras.get(0)/factor);
		return factored;
	}
}
