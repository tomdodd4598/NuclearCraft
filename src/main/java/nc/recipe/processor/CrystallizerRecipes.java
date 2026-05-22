package nc.recipe.processor;

import nc.radiation.RadSources;

import static nc.config.NCConfig.*;
import static nc.util.FluidStackHelper.*;

public class CrystallizerRecipes extends BasicProcessorRecipeHandler {
	
	public CrystallizerRecipes() {
		super("crystallizer", 0, 1, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[14]) {
			return;
		}
		
		addRecipe(fluidStack("boron_nitride_solution", GEM_VOLUME), "dustBoronNitride", 1D, 1D);
		addRecipe(fluidStack("fluorite_water", GEM_VOLUME), "dustFluorite", 1D, 1D);
		addRecipe(fluidStack("calcium_sulfate_solution", GEM_VOLUME), "dustCalciumSulfate", 1D, 1D);
		addRecipe(fluidStack("sodium_fluoride_solution", GEM_VOLUME), "dustSodiumFluoride", 1D, 1D);
		addRecipe(fluidStack("potassium_fluoride_solution", GEM_VOLUME), "dustPotassiumFluoride", 1D, 1D);
		addRecipe(fluidStack("sodium_hydroxide_solution", GEM_VOLUME), "dustSodiumHydroxide", 0.5D, 0.5D);
		addRecipe(fluidStack("potassium_hydroxide_solution", GEM_VOLUME), "dustPotassiumHydroxide", 0.5D, 0.5D);
		addRecipe(fluidStack("borax_solution", GEM_VOLUME), "dustBorax", 0.5D, 0.5D);
		addRecipe(fluidStack("irradiated_borax_solution", GEM_VOLUME), "dustBorax", 0.5D, 0.5D, RadSources.CAESIUM_137 / 4D);
		addRecipe(fluidStack("ammonium_sulfate_solution", GEM_VOLUME), "dustAmmoniumSulfate", 1D, 1D);
		addRecipe(fluidStack("ammonium_bisulfate_solution", GEM_VOLUME), "dustAmmoniumBisulfate", 1D, 1D);
		addRecipe(fluidStack("ammonium_persulfate_solution", GEM_VOLUME), "dustAmmoniumPersulfate", 1D, 1D);
		addRecipe(fluidStack("hydroquinone_solution", GEM_VOLUME), "dustHydroquinone", 1D, 1D);
		addRecipe(fluidStack("sodium_hydroquinone_solution", GEM_VOLUME), "dustSodiumHydroquinone", 1D, 1D);
		addRecipe(fluidStack("potassium_hydroquinone_solution", GEM_VOLUME), "dustPotassiumHydroquinone", 1D, 1D);
		addRecipe(fluidStack("dysprholminite_water", GEM_VOLUME), "dustDysprholminite", 1D, 1D);
		
		addRecipe(fluidStack("sic_vapor", INGOT_VOLUME), "fiberSiliconCarbide", 2D, 2D);
		
		addRecipe(fluidStack("orthosilicic_acid", BUCKET_VOLUME), "dustSilica", 1D, 1D);
	}
}
