package nc.recipe.generator;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class FusionRecipes extends BaseRecipeHandler {
	
	public FusionRecipes() {
		super("fusion", 0, 2, 0, 4, true);
	}
	
	@Override
	public void addRecipes() {
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME, "deuterium", FluidHelper.BUCKET_VOLUME/4, "deuterium", FluidHelper.BUCKET_VOLUME/4, "deuterium", FluidHelper.BUCKET_VOLUME/4, "deuterium", FluidHelper.BUCKET_VOLUME/4, 0);
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "deuterium", FluidHelper.BUCKET_VOLUME, "helium3", FluidHelper.BUCKET_VOLUME/4, "helium3", FluidHelper.BUCKET_VOLUME/4, "helium3", FluidHelper.BUCKET_VOLUME/4, "helium3", FluidHelper.BUCKET_VOLUME/4, 1);
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "tritium", FluidHelper.BUCKET_VOLUME, "helium3", FluidHelper.BUCKET_VOLUME/2, "helium3", FluidHelper.BUCKET_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 2);
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "helium3", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME/4, "helium", FluidHelper.BUCKET_VOLUME/4, "helium", FluidHelper.BUCKET_VOLUME/4, "helium", FluidHelper.BUCKET_VOLUME/4, 3);
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "lithium6", FluidHelper.INGOT_VOLUME, "tritium", FluidHelper.BUCKET_VOLUME/2, "tritium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, 4);
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "lithium7", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, 5);
		addFusionRecipe("hydrogen", FluidHelper.BUCKET_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*3/4, "helium", FluidHelper.BUCKET_VOLUME*3/4, "helium", FluidHelper.BUCKET_VOLUME*3/4, "helium", FluidHelper.BUCKET_VOLUME*3/4, 6);
		
		addFusionRecipe("deuterium", FluidHelper.BUCKET_VOLUME, "deuterium", FluidHelper.BUCKET_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME/2, "tritium", FluidHelper.BUCKET_VOLUME/2, "helium3", FluidHelper.BUCKET_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 7);
		addFusionRecipe("deuterium", FluidHelper.BUCKET_VOLUME, "tritium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 8);
		addFusionRecipe("deuterium", FluidHelper.BUCKET_VOLUME, "helium3", FluidHelper.BUCKET_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME/2, "hydrogen", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, 9);
		addFusionRecipe("deuterium", FluidHelper.BUCKET_VOLUME, "lithium6", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, 10);
		addFusionRecipe("deuterium", FluidHelper.BUCKET_VOLUME, "lithium7", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 11);
		addFusionRecipe("deuterium", FluidHelper.BUCKET_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*6/4, "helium", FluidHelper.BUCKET_VOLUME*6/4, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 12);
		
		addFusionRecipe("tritium", FluidHelper.BUCKET_VOLUME, "tritium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, 13);
		addFusionRecipe("tritium", FluidHelper.BUCKET_VOLUME, "helium3", FluidHelper.BUCKET_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 14);
		addFusionRecipe("tritium", FluidHelper.BUCKET_VOLUME, "lithium6", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 15);
		addFusionRecipe("tritium", FluidHelper.BUCKET_VOLUME, "lithium7", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, 16);
		addFusionRecipe("tritium", FluidHelper.BUCKET_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*6/4, "helium", FluidHelper.BUCKET_VOLUME*6/4, "neutron", FluidHelper.PARTICLE_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, 17);
		
		addFusionRecipe("helium3", FluidHelper.BUCKET_VOLUME, "helium3", FluidHelper.BUCKET_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME/2, 18);
		addFusionRecipe("helium3", FluidHelper.BUCKET_VOLUME, "lithium6", FluidHelper.INGOT_VOLUME, "hydrogen", FluidHelper.BUCKET_VOLUME/2, "hydrogen", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, 19);
		addFusionRecipe("helium3", FluidHelper.BUCKET_VOLUME, "lithium7", FluidHelper.INGOT_VOLUME, "deuterium", FluidHelper.BUCKET_VOLUME/2, "deuterium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME, "helium", FluidHelper.BUCKET_VOLUME, 20);
		addFusionRecipe("helium3", FluidHelper.BUCKET_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "deuterium", FluidHelper.BUCKET_VOLUME/2, "deuterium", FluidHelper.BUCKET_VOLUME/2, "helium", FluidHelper.BUCKET_VOLUME*6/4, "helium", FluidHelper.BUCKET_VOLUME*6/4, 21);
		
		addFusionRecipe("lithium6", FluidHelper.INGOT_VOLUME, "lithium6", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*3/4, "helium", FluidHelper.BUCKET_VOLUME*3/4, "helium", FluidHelper.BUCKET_VOLUME*3/4, "helium", FluidHelper.BUCKET_VOLUME*3/4, 22);
		addFusionRecipe("lithium6", FluidHelper.INGOT_VOLUME, "lithium7", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*6/4, "helium", FluidHelper.BUCKET_VOLUME*6/4, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 23);
		addFusionRecipe("lithium6", FluidHelper.INGOT_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*2, "helium", FluidHelper.BUCKET_VOLUME*2, "neutron", FluidHelper.PARTICLE_VOLUME/2, "neutron", FluidHelper.PARTICLE_VOLUME/2, 24);
		
		addFusionRecipe("lithium7", FluidHelper.INGOT_VOLUME, "lithium7", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*6/4, "helium", FluidHelper.BUCKET_VOLUME*6/4, "neutron", FluidHelper.PARTICLE_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, 25);
		addFusionRecipe("lithium7", FluidHelper.INGOT_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*2, "helium", FluidHelper.BUCKET_VOLUME*2, "neutron", FluidHelper.PARTICLE_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, 26);
		
		addFusionRecipe("boron11", FluidHelper.INGOT_VOLUME, "boron11", FluidHelper.INGOT_VOLUME, "helium", FluidHelper.BUCKET_VOLUME*10/4, "helium", FluidHelper.BUCKET_VOLUME*10/4, "neutron", FluidHelper.PARTICLE_VOLUME, "neutron", FluidHelper.PARTICLE_VOLUME, 27);
	}
	
	public void addFusionRecipe(String in1, int amountIn1, String in2, int amountIn2, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int combo) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(in2, amountIn2), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), NCConfig.fusion_fuel_time[combo], NCConfig.fusion_power[combo], NCConfig.fusion_heat_variable[combo]);
	}
}
