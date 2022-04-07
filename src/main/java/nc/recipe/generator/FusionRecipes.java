package nc.recipe.generator;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class FusionRecipes extends BasicRecipeHandler {
	
	public FusionRecipes() {
		super("fusion", 0, 2, 0, 4);
	}
	
	@Override
	public void addRecipes() {
		/*
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "hydrogen", BUCKET_VOLUME, "deuterium", BUCKET_VOLUME/4, "deuterium", BUCKET_VOLUME/4, "deuterium", BUCKET_VOLUME/4, "deuterium", BUCKET_VOLUME/4, 0);
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "deuterium", BUCKET_VOLUME, "helium_3", BUCKET_VOLUME/4, "helium_3", BUCKET_VOLUME/4, "helium_3", BUCKET_VOLUME/4, "helium_3", BUCKET_VOLUME/4, 1);
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "tritium", BUCKET_VOLUME, "helium_3", BUCKET_VOLUME/2, "helium_3", BUCKET_VOLUME/2, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 2);
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "helium_3", BUCKET_VOLUME, "helium", BUCKET_VOLUME/4, "helium", BUCKET_VOLUME/4, "helium", BUCKET_VOLUME/4, "helium", BUCKET_VOLUME/4, 3);
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "lithium_6", INGOT_VOLUME, "tritium", BUCKET_VOLUME/2, "tritium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, 4);
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "lithium_7", INGOT_VOLUME, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, 5);
		addFusionRecipe("hydrogen", BUCKET_VOLUME, "boron_11", INGOT_VOLUME, "helium", BUCKET_VOLUME*3/4, "helium", BUCKET_VOLUME*3/4, "helium", BUCKET_VOLUME*3/4, "helium", BUCKET_VOLUME*3/4, 6);
		
		addFusionRecipe("deuterium", BUCKET_VOLUME, "deuterium", BUCKET_VOLUME, "hydrogen", BUCKET_VOLUME/2, "tritium", BUCKET_VOLUME/2, "helium_3", BUCKET_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 7);
		addFusionRecipe("deuterium", BUCKET_VOLUME, "tritium", BUCKET_VOLUME, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 8);
		addFusionRecipe("deuterium", BUCKET_VOLUME, "helium_3", BUCKET_VOLUME, "hydrogen", BUCKET_VOLUME/2, "hydrogen", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, 9);
		addFusionRecipe("deuterium", BUCKET_VOLUME, "lithium_6", INGOT_VOLUME, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, 10);
		addFusionRecipe("deuterium", BUCKET_VOLUME, "lithium_7", INGOT_VOLUME, "helium", BUCKET_VOLUME, "helium", BUCKET_VOLUME, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 11);
		addFusionRecipe("deuterium", BUCKET_VOLUME, "boron_11", INGOT_VOLUME, "helium", BUCKET_VOLUME*6/4, "helium", BUCKET_VOLUME*6/4, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 12);
		
		addFusionRecipe("tritium", BUCKET_VOLUME, "tritium", BUCKET_VOLUME, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, "neutron", PARTICLE_VOLUME, "neutron", PARTICLE_VOLUME, 13);
		addFusionRecipe("tritium", BUCKET_VOLUME, "helium_3", BUCKET_VOLUME, "hydrogen", BUCKET_VOLUME, "helium", BUCKET_VOLUME, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 14);
		addFusionRecipe("tritium", BUCKET_VOLUME, "lithium_6", INGOT_VOLUME, "helium", BUCKET_VOLUME, "helium", BUCKET_VOLUME, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 15);
		addFusionRecipe("tritium", BUCKET_VOLUME, "lithium_7", INGOT_VOLUME, "helium", BUCKET_VOLUME, "helium", BUCKET_VOLUME, "neutron", PARTICLE_VOLUME, "neutron", PARTICLE_VOLUME, 16);
		addFusionRecipe("tritium", BUCKET_VOLUME, "boron_11", INGOT_VOLUME, "helium", BUCKET_VOLUME*6/4, "helium", BUCKET_VOLUME*6/4, "neutron", PARTICLE_VOLUME, "neutron", PARTICLE_VOLUME, 17);
		
		addFusionRecipe("helium_3", BUCKET_VOLUME, "helium_3", BUCKET_VOLUME, "hydrogen", BUCKET_VOLUME, "hydrogen", BUCKET_VOLUME, "helium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME/2, 18);
		addFusionRecipe("helium_3", BUCKET_VOLUME, "lithium_6", INGOT_VOLUME, "hydrogen", BUCKET_VOLUME/2, "hydrogen", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME, "helium", BUCKET_VOLUME, 19);
		addFusionRecipe("helium_3", BUCKET_VOLUME, "lithium_7", INGOT_VOLUME, "deuterium", BUCKET_VOLUME/2, "deuterium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME, "helium", BUCKET_VOLUME, 20);
		addFusionRecipe("helium_3", BUCKET_VOLUME, "boron_11", INGOT_VOLUME, "deuterium", BUCKET_VOLUME/2, "deuterium", BUCKET_VOLUME/2, "helium", BUCKET_VOLUME*6/4, "helium", BUCKET_VOLUME*6/4, 21);
		
		addFusionRecipe("lithium_6", INGOT_VOLUME, "lithium_6", INGOT_VOLUME, "helium", BUCKET_VOLUME*3/4, "helium", BUCKET_VOLUME*3/4, "helium", BUCKET_VOLUME*3/4, "helium", BUCKET_VOLUME*3/4, 22);
		addFusionRecipe("lithium_6", INGOT_VOLUME, "lithium_7", INGOT_VOLUME, "helium", BUCKET_VOLUME*6/4, "helium", BUCKET_VOLUME*6/4, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 23);
		addFusionRecipe("lithium_6", INGOT_VOLUME, "boron_11", INGOT_VOLUME, "helium", BUCKET_VOLUME*2, "helium", BUCKET_VOLUME*2, "neutron", PARTICLE_VOLUME/2, "neutron", PARTICLE_VOLUME/2, 24);
		
		addFusionRecipe("lithium_7", INGOT_VOLUME, "lithium_7", INGOT_VOLUME, "helium", BUCKET_VOLUME*6/4, "helium", BUCKET_VOLUME*6/4, "neutron", PARTICLE_VOLUME, "neutron", PARTICLE_VOLUME, 25);
		addFusionRecipe("lithium_7", INGOT_VOLUME, "boron_11", INGOT_VOLUME, "helium", BUCKET_VOLUME*2, "helium", BUCKET_VOLUME*2, "neutron", PARTICLE_VOLUME, "neutron", PARTICLE_VOLUME, 26);
		
		addFusionRecipe("boron_11", INGOT_VOLUME, "boron_11", INGOT_VOLUME, "helium", BUCKET_VOLUME*10/4, "helium", BUCKET_VOLUME*10/4, "neutron", PARTICLE_VOLUME, "neutron", PARTICLE_VOLUME, 27);
		*/
	}
	
	/*public void addFusionRecipe(String in1, int amountIn1, String in2, int amountIn2, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int combo) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(in2, amountIn2), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), fusion_fuel_time[combo], fusion_power[combo], fusion_heat_variable[combo], fusion_radiation[combo]);
	}*/
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(4);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 0D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 1000D);
		fixed.add(extras.size() > 3 && extras.get(3) instanceof Double ? (double) extras.get(3) : 0D);
		return fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
