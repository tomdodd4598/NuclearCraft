package nc.recipe.processor;

import nc.ModCheck;
import nc.init.NCBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static nc.config.NCConfig.*;
import static nc.util.FluidStackHelper.*;

public class EnricherRecipes extends BasicProcessorRecipeHandler {
	
	public EnricherRecipes() {
		super("enricher", 1, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[15]) {
			return;
		}
		
		addRecipe("dustBoronNitride", fluidStack("water", BUCKET_VOLUME), fluidStack("boron_nitride_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustFluorite", fluidStack("water", BUCKET_VOLUME), fluidStack("fluorite_water", GEM_VOLUME), 1D, 1D);
		addRecipe("dustCalciumSulfate", fluidStack("water", BUCKET_VOLUME), fluidStack("calcium_sulfate_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumFluoride", fluidStack("water", BUCKET_VOLUME), fluidStack("sodium_fluoride_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustPotassiumFluoride", fluidStack("water", BUCKET_VOLUME), fluidStack("potassium_fluoride_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumHydroxide", fluidStack("water", BUCKET_VOLUME), fluidStack("sodium_hydroxide_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustPotassiumHydroxide", fluidStack("water", BUCKET_VOLUME), fluidStack("potassium_hydroxide_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustBorax", fluidStack("water", BUCKET_VOLUME), fluidStack("borax_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustIrradiatedBorax", fluidStack("water", BUCKET_VOLUME), fluidStack("irradiated_borax_solution", GEM_VOLUME), 0.5D, 0.5D);
		addRecipe("dustAmmoniumSulfate", fluidStack("water", BUCKET_VOLUME), fluidStack("ammonium_sulfate_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustAmmoniumBisulfate", fluidStack("water", BUCKET_VOLUME), fluidStack("ammonium_bisulfate_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustAmmoniumPersulfate", fluidStack("water", BUCKET_VOLUME), fluidStack("ammonium_persulfate_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustHydroquinone", fluidStack("water", BUCKET_VOLUME), fluidStack("hydroquinone_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumHydroquinone", fluidStack("water", BUCKET_VOLUME), fluidStack("sodium_hydroquinone_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustPotassiumHydroquinone", fluidStack("water", BUCKET_VOLUME), fluidStack("potassium_hydroquinone_solution", GEM_VOLUME), 1D, 1D);
		addRecipe("dustDysprholminite", fluidStack("water", BUCKET_VOLUME), fluidStack("dysprholminite_water", GEM_VOLUME), 1D, 1D);
		
		addRecipe("dustNichromite", fluidStack("barium_oxide", INGOT_VOLUME), fluidStack("bacro_nio", INGOT_VOLUME), 2D, 2D);
		
		addRecipe(new ItemStack(NCBlocks.glowing_mushroom, 3), fluidStack("ethanol", BUCKET_VOLUME / 4), fluidStack("radaway", BUCKET_VOLUME / 4), 1D, 0.5D);
		addRecipe(new ItemStack(NCBlocks.glowing_mushroom, 3), fluidStack("redstone_ethanol", BUCKET_VOLUME / 4), fluidStack("radaway_slow", BUCKET_VOLUME / 4), 1D, 0.5D);
		
		if (!ModCheck.thermalFoundationLoaded()) {
			addRecipe(new ItemStack(Items.SNOWBALL, 4), fluidStack("liquid_helium", 25), fluidStack("cryotheum", 25), 0.5D, 1D);
		}
		
		addRecipe("dustDimensional", fluidStack("soul", 100), fluidStack("mysterious_soul", 100), 2D, 1D);
	}
}
