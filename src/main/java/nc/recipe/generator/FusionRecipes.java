package nc.recipe.generator;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.ProcessorRecipeHandler;

public class FusionRecipes extends ProcessorRecipeHandler {
	
	public FusionRecipes() {
		super("fusion", 0, 2, 0, 4);
	}
	
	@Override
	public void addRecipes() {
		/*addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME, "deuterium", FluidStackHelper.BUCKET_VOLUME/4, "deuterium", FluidStackHelper.BUCKET_VOLUME/4, "deuterium", FluidStackHelper.BUCKET_VOLUME/4, "deuterium", FluidStackHelper.BUCKET_VOLUME/4, 0);
		addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "deuterium", FluidStackHelper.BUCKET_VOLUME, "helium3", FluidStackHelper.BUCKET_VOLUME/4, "helium3", FluidStackHelper.BUCKET_VOLUME/4, "helium3", FluidStackHelper.BUCKET_VOLUME/4, "helium3", FluidStackHelper.BUCKET_VOLUME/4, 1);
		addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "tritium", FluidStackHelper.BUCKET_VOLUME, "helium3", FluidStackHelper.BUCKET_VOLUME/2, "helium3", FluidStackHelper.BUCKET_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 2);
		addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "helium3", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME/4, "helium", FluidStackHelper.BUCKET_VOLUME/4, "helium", FluidStackHelper.BUCKET_VOLUME/4, "helium", FluidStackHelper.BUCKET_VOLUME/4, 3);
		addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "lithium6", FluidStackHelper.INGOT_VOLUME, "tritium", FluidStackHelper.BUCKET_VOLUME/2, "tritium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, 4);
		addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "lithium7", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, 5);
		addFusionRecipe("hydrogen", FluidStackHelper.BUCKET_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, 6);
		
		addFusionRecipe("deuterium", FluidStackHelper.BUCKET_VOLUME, "deuterium", FluidStackHelper.BUCKET_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME/2, "tritium", FluidStackHelper.BUCKET_VOLUME/2, "helium3", FluidStackHelper.BUCKET_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 7);
		addFusionRecipe("deuterium", FluidStackHelper.BUCKET_VOLUME, "tritium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 8);
		addFusionRecipe("deuterium", FluidStackHelper.BUCKET_VOLUME, "helium3", FluidStackHelper.BUCKET_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME/2, "hydrogen", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, 9);
		addFusionRecipe("deuterium", FluidStackHelper.BUCKET_VOLUME, "lithium6", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, 10);
		addFusionRecipe("deuterium", FluidStackHelper.BUCKET_VOLUME, "lithium7", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 11);
		addFusionRecipe("deuterium", FluidStackHelper.BUCKET_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 12);
		
		addFusionRecipe("tritium", FluidStackHelper.BUCKET_VOLUME, "tritium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, 13);
		addFusionRecipe("tritium", FluidStackHelper.BUCKET_VOLUME, "helium3", FluidStackHelper.BUCKET_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 14);
		addFusionRecipe("tritium", FluidStackHelper.BUCKET_VOLUME, "lithium6", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 15);
		addFusionRecipe("tritium", FluidStackHelper.BUCKET_VOLUME, "lithium7", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, 16);
		addFusionRecipe("tritium", FluidStackHelper.BUCKET_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "neutron", FluidStackHelper.PARTICLE_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, 17);
		
		addFusionRecipe("helium3", FluidStackHelper.BUCKET_VOLUME, "helium3", FluidStackHelper.BUCKET_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME/2, 18);
		addFusionRecipe("helium3", FluidStackHelper.BUCKET_VOLUME, "lithium6", FluidStackHelper.INGOT_VOLUME, "hydrogen", FluidStackHelper.BUCKET_VOLUME/2, "hydrogen", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, 19);
		addFusionRecipe("helium3", FluidStackHelper.BUCKET_VOLUME, "lithium7", FluidStackHelper.INGOT_VOLUME, "deuterium", FluidStackHelper.BUCKET_VOLUME/2, "deuterium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME, 20);
		addFusionRecipe("helium3", FluidStackHelper.BUCKET_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "deuterium", FluidStackHelper.BUCKET_VOLUME/2, "deuterium", FluidStackHelper.BUCKET_VOLUME/2, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, 21);
		
		addFusionRecipe("lithium6", FluidStackHelper.INGOT_VOLUME, "lithium6", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, "helium", FluidStackHelper.BUCKET_VOLUME*3/4, 22);
		addFusionRecipe("lithium6", FluidStackHelper.INGOT_VOLUME, "lithium7", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 23);
		addFusionRecipe("lithium6", FluidStackHelper.INGOT_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*2, "helium", FluidStackHelper.BUCKET_VOLUME*2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, "neutron", FluidStackHelper.PARTICLE_VOLUME/2, 24);
		
		addFusionRecipe("lithium7", FluidStackHelper.INGOT_VOLUME, "lithium7", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "helium", FluidStackHelper.BUCKET_VOLUME*6/4, "neutron", FluidStackHelper.PARTICLE_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, 25);
		addFusionRecipe("lithium7", FluidStackHelper.INGOT_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*2, "helium", FluidStackHelper.BUCKET_VOLUME*2, "neutron", FluidStackHelper.PARTICLE_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, 26);
		
		addFusionRecipe("boron11", FluidStackHelper.INGOT_VOLUME, "boron11", FluidStackHelper.INGOT_VOLUME, "helium", FluidStackHelper.BUCKET_VOLUME*10/4, "helium", FluidStackHelper.BUCKET_VOLUME*10/4, "neutron", FluidStackHelper.PARTICLE_VOLUME, "neutron", FluidStackHelper.PARTICLE_VOLUME, 27);
		*/
	}
	
	/*public void addFusionRecipe(String in1, int amountIn1, String in2, int amountIn2, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int combo) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(in2, amountIn2), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), NCConfig.fusion_fuel_time[combo], NCConfig.fusion_power[combo], NCConfig.fusion_heat_variable[combo], NCConfig.fusion_radiation[combo]);
	}*/
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(4);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 0D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 1000D);
		fixed.add(extras.size() > 3 && extras.get(3) instanceof Double ? (double) extras.get(3) : 0D);
		return fixed;
	}
	
	@Override
	public List getFactoredExtras(List extras, int factor) {
		List factored = new ArrayList(extras);
		factored.set(0, (double)extras.get(0)/factor);
		return factored;
	}
}
