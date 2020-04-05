package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_FLUID;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class ChemicalReactorRecipes extends ProcessorRecipeHandler {
	
	public ChemicalReactorRecipes() {
		super("chemical_reactor", 0, 2, 0, 2);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("boron", FluidStackHelper.INGOT_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*3), fluidStack("diborane", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 1D);
		
		addRecipe(fluidStack("diborane", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("water", FluidStackHelper.BUCKET_VOLUME*3), fluidStack("boric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*3), 0.5D, 0.5D);
		
		addRecipe(fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*3), fluidStack("ammonia", FluidStackHelper.BUCKET_VOLUME*2), emptyFluidStack(), 1D, 0.5D);
		
		addRecipe(fluidStack("boric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("ammonia", FluidStackHelper.BUCKET_VOLUME), fluidStack("boron_nitride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), 1D, 0.5D);
		
		addRecipe(fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 0.5D, 0.5D);
		addRecipe(fluidStack("deuterium", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("heavy_water", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 0.5D, 0.5D);
		addRecipe(fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 0.5D, 0.5D);
		
		addRecipe(fluidStack("lithium", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("lif", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 0.5D);
		addRecipe(fluidStack("beryllium", FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("bef2", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 0.5D);
		
		addRecipe(fluidStack("sulfur", FluidStackHelper.GEM_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_dioxide", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 0.5D);
		addRecipe(fluidStack("sulfur_dioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 0.5D, 0.5D);
		addRecipe(fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfuric_acid", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 0.5D);
		
		addRecipe(fluidStack("fluorite_water", FluidStackHelper.GEM_VOLUME), fluidStack("sulfuric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("calcium_sulfate_solution", FluidStackHelper.GEM_VOLUME), 1.5D, 0.5D);
		
		addRecipe(fluidStack("sodium_fluoride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("sodium_hydroxide_solution", FluidStackHelper.GEM_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(fluidStack("potassium_fluoride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("potassium_hydroxide_solution", FluidStackHelper.GEM_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("sodium_fluoride_solution", FluidStackHelper.GEM_VOLUME), fluidStack("boric_acid", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("borax_solution", FluidStackHelper.GEM_VOLUME/2), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), 1.5D, 1D);
		
		addRecipe(fluidStack("oxygen_difluoride", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME*2), 1D, 1D);
		addRecipe(fluidStack("oxygen_difluoride", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_dioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen_difluoride", FluidStackHelper.BUCKET_VOLUME*2), emptyFluidStack(), 1.5D, 0.5D);
		
		addRecipe(fluidStack("manganese_dioxide", FluidStackHelper.INGOT_VOLUME), fluidStack("carbon", FluidStackHelper.COAL_DUST_VOLUME*2), fluidStack("manganese", FluidStackHelper.INGOT_VOLUME), fluidStack("carbon_monoxide", FluidStackHelper.BUCKET_VOLUME*2), 1D, 1D);
		
		addRecipe(fluidStack("sugar", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("ethanol", FluidStackHelper.BUCKET_VOLUME*4), fluidStack("carbon_dioxide", FluidStackHelper.BUCKET_VOLUME*4), 1.5D, 0.5D);
		addRecipe(fluidStack("carbon_dioxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("carbon_monoxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 1D, 0.5D);
		addRecipe(fluidStack("carbon_monoxide", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("methanol", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 0.5D);
		addRecipe(fluidStack("methanol", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluoromethane", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("fluoromethane", FluidStackHelper.BUCKET_VOLUME), fluidStack("naoh", FluidStackHelper.GEM_VOLUME), fluidStack("ethene", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("sodium_fluoride_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		addRecipe(fluidStack("fluoromethane", FluidStackHelper.BUCKET_VOLUME), fluidStack("koh", FluidStackHelper.GEM_VOLUME), fluidStack("ethene", FluidStackHelper.BUCKET_VOLUME/2), fluidStack("potassium_fluoride_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("ethene", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfuric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("ethanol", FluidStackHelper.BUCKET_VOLUME), fluidStack("sulfur_trioxide", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		
		addRecipe(fluidStack("boron", FluidStackHelper.INGOT_VOLUME), fluidStack("arsenic", FluidStackHelper.GEM_VOLUME), fluidStack("bas", FluidStackHelper.GEM_VOLUME), emptyFluidStack(), 1D, 1D);
		
		addRecipe(fluidStack("alugentum", FluidStackHelper.INGOT_VOLUME/2), fluidStack("oxygen", 3*FluidStackHelper.BUCKET_VOLUME), fluidStack("alumina", FluidStackHelper.INGOT_VOLUME), fluidStack("silver", FluidStackHelper.INGOT_VOLUME/2), 1D, 0.5D);
		
		addRecipe(fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("chlorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen_chloride", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), 1D, 0.5D);
		
		// Fission Materials
		addFissionFluorideRecipes();
		addFissionZARecipes();
	}
	
	public void addFissionZARecipes() {
		for (int i = 0; i < FISSION_FLUID.length; i++) {
			addRecipe(fluidStack(FISSION_FLUID[i], FluidStackHelper.INGOT_VOLUME), fluidStack("zirconium", FluidStackHelper.INGOT_VOLUME), fluidStack(FISSION_FLUID[i] + "_za", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 0.5D);
		}
	}
	
	public void addFissionFluorideRecipes() {
		for (int i = 0; i < FISSION_FLUID.length; i++) {
			addRecipe(fluidStack(FISSION_FLUID[i], FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), fluidStack(FISSION_FLUID[i] + "_fluoride", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), 1D, 0.5D);
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
