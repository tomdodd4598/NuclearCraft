package nc.crafting.generator;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;

public class FusionRecipes extends ProcessorRecipeHandler {
	
	private static final FusionRecipes RECIPES = new FusionRecipes();

	public FusionRecipes() {
		super(0, 2, 0, 4, true, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		fusion("hydrogen", 1000, "hydrogen", 1000, "deuterium", 250, "deuterium", 250, "deuterium", 250, "deuterium", 250, 0);
		fusion("hydrogen", 1000, "deuterium", 1000, "helium3", 250, "helium3", 250, "helium3", 250, "helium3", 250, 1);
		fusion("hydrogen", 1000, "tritium", 1000, "helium3", 500, "helium3", 500, "neutron", 50, "neutron", 50, 2);
		fusion("hydrogen", 1000, "helium3", 1000, "helium", 250, "helium", 250, "helium", 250, "helium", 250, 3);
		fusion("hydrogen", 1000, "lithium6", 108, "tritium", 500, "tritium", 500, "helium", 500, "helium", 500, 4);
		fusion("hydrogen", 1000, "lithium7", 108, "helium", 500, "helium", 500, "helium", 500, "helium", 500, 5);
		fusion("hydrogen", 1000, "boron11", 108, "helium", 750, "helium", 750, "helium", 750, "helium", 750, 6);
		
		fusion("deuterium", 1000, "deuterium", 1000, "hydrogen", 500, "tritium", 500, "helium3", 500, "neutron", 50, 7);
		fusion("deuterium", 1000, "tritium", 1000, "helium", 500, "helium", 500, "neutron", 50, "neutron", 50, 8);
		fusion("deuterium", 1000, "helium3", 1000, "hydrogen", 500, "hydrogen", 500, "helium", 500, "helium", 500, 9);
		fusion("deuterium", 1000, "lithium6", 108, "helium", 500, "helium", 500, "helium", 500, "helium", 500, 10);
		fusion("deuterium", 1000, "lithium7", 108, "helium", 1000, "helium", 1000, "neutron", 50, "neutron", 50, 11);
		fusion("deuterium", 1000, "boron11", 108, "helium", 1500, "helium", 1500, "neutron", 50, "neutron", 50, 12);
		
		fusion("tritium", 1000, "tritium", 1000, "helium", 500, "helium", 500, "neutron", 100, "neutron", 100, 13);
		fusion("tritium", 1000, "helium3", 1000, "hydrogen", 1000, "helium", 1000, "neutron", 50, "neutron", 50, 14);
		fusion("tritium", 1000, "lithium6", 108, "helium", 1000, "helium", 1000, "neutron", 50, "neutron", 50, 15);
		fusion("tritium", 1000, "lithium7", 108, "helium", 1000, "helium", 1000, "neutron", 100, "neutron", 100, 16);
		fusion("tritium", 1000, "boron11", 108, "helium", 1500, "helium", 1500, "neutron", 100, "neutron", 100, 17);
		
		fusion("helium3", 1000, "helium3", 1000, "hydrogen", 1000, "hydrogen", 1000, "helium", 500, "helium", 500, 18);
		fusion("helium3", 1000, "lithium6", 108, "hydrogen", 500, "hydrogen", 500, "helium", 1000, "helium", 1000, 19);
		fusion("helium3", 1000, "lithium7", 108, "deuterium", 500, "deuterium", 500, "helium", 1000, "helium", 1000, 20);
		fusion("helium3", 1000, "boron11", 108, "deuterium", 500, "deuterium", 500, "helium", 1500, "helium", 1500, 21);
		
		fusion("lithium6", 108, "lithium6", 108, "helium", 750, "helium", 750, "helium", 750, "helium", 750, 22);
		fusion("lithium6", 108, "lithium7", 108, "helium", 1500, "helium", 1500, "neutron", 50, "neutron", 50, 23);
		fusion("lithium6", 108, "boron11", 108, "helium", 2000, "helium", 2000, "neutron", 50, "neutron", 50, 24);
		
		fusion("lithium7", 108, "lithium7", 108, "helium", 1500, "helium", 1500, "neutron", 100, "neutron", 100, 25);
		fusion("lithium7", 108, "boron11", 108, "helium", 2000, "helium", 2000, "neutron", 100, "neutron", 100, 26);
		
		fusion("boron11", 108, "boron11", 108, "helium", 2500, "helium", 2500, "neutron", 100, "neutron", 100, 27);
	}
	
	public void fusion(String in1, int amountIn1, String in2, int amountIn2, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int combo) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(in2, amountIn2), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), new double[] {NCConfig.fusion_fuel_time[combo], NCConfig.fusion_power[combo], NCConfig.fusion_heat_variable[combo]});
	}
}
