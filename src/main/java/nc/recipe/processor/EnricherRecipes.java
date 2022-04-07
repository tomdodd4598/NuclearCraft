package nc.recipe.processor;

import static nc.util.FluidStackHelper.*;

import java.util.*;

import nc.ModCheck;
import nc.init.NCBlocks;
import nc.recipe.BasicRecipeHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EnricherRecipes extends BasicRecipeHandler {
	
	public EnricherRecipes() {
		super("enricher", 1, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("dustBoronNitride", fluidStack("water", BUCKET_VOLUME), fluidStack("boron_nitride_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustFluorite", fluidStack("water", BUCKET_VOLUME), fluidStack("fluorite_water", GEM_VOLUME), 1D, 1D);
		addRecipe("dustCalciumSulfate", fluidStack("water", BUCKET_VOLUME), fluidStack("calcium_sulfate_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumFluoride", fluidStack("water", BUCKET_VOLUME), fluidStack("sodium_fluoride_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustPotassiumFluoride", fluidStack("water", BUCKET_VOLUME), fluidStack("potassium_fluoride_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumHydroxide", fluidStack("water", BUCKET_VOLUME), fluidStack("sodium_hydroxide_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustPotassiumHydroxide", fluidStack("water", BUCKET_VOLUME), fluidStack("potassium_hydroxide_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustBorax", fluidStack("water", BUCKET_VOLUME), fluidStack("borax_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustIrradiatedBorax", fluidStack("water", BUCKET_VOLUME), fluidStack("irradiated_borax_solution", GEM_VOLUME), 0.5D, 0.5D);
		
		addRecipe(new ItemStack(NCBlocks.glowing_mushroom, 3), fluidStack("ethanol", BUCKET_VOLUME / 4), fluidStack("radaway", BUCKET_VOLUME / 4), 1D, 0.5D);
		addRecipe(new ItemStack(NCBlocks.glowing_mushroom, 3), fluidStack("redstone_ethanol", BUCKET_VOLUME / 4), fluidStack("radaway_slow", BUCKET_VOLUME / 4), 1D, 0.5D);
		
		if (!ModCheck.thermalFoundationLoaded()) {
			addRecipe(new ItemStack(Items.SNOWBALL, 4), fluidStack("liquid_helium", 25), fluidStack("cryotheum", 25), 0.5D, 1D);
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
}
