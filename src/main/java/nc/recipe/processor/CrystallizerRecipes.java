package nc.recipe.processor;

import static nc.util.FluidStackHelper.GEM_VOLUME;

import java.util.*;

import nc.radiation.RadSources;
import nc.recipe.BasicRecipeHandler;

public class CrystallizerRecipes extends BasicRecipeHandler {
	
	public CrystallizerRecipes() {
		super("crystallizer", 0, 1, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("boron_nitride_solution", GEM_VOLUME), "dustBoronNitride", 1D, 1D);
		addRecipe(fluidStack("fluorite_water", GEM_VOLUME), "dustFluorite", 1D, 1D);
		addRecipe(fluidStack("calcium_sulfate_solution", GEM_VOLUME), "dustCalciumSulfate", 1D, 1D);
		addRecipe(fluidStack("sodium_fluoride_solution", GEM_VOLUME), "dustSodiumFluoride", 1D, 1D);
		addRecipe(fluidStack("potassium_fluoride_solution", GEM_VOLUME), "dustPotassiumFluoride", 1D, 1D);
		addRecipe(fluidStack("sodium_hydroxide_solution", GEM_VOLUME), "dustSodiumHydroxide", 0.5D, 0.5D);
		addRecipe(fluidStack("potassium_hydroxide_solution", GEM_VOLUME), "dustPotassiumHydroxide", 0.5D, 0.5D);
		addRecipe(fluidStack("borax_solution", GEM_VOLUME), "dustBorax", 0.5D, 0.5D);
		addRecipe(fluidStack("irradiated_borax_solution", GEM_VOLUME), "dustBorax", 0.5D, 0.5D, RadSources.CAESIUM_137 / 4D);
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
