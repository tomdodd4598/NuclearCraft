package nc.recipe.generator;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class FusionRecipes extends BaseRecipeHandler {
	
	//private static final FusionRecipes RECIPES = new FusionRecipes();
	
	public FusionRecipes() {
		super(0, 2, 0, 4, true);
	}

	/*public static final FusionRecipes instance() {
		return RECIPES;
	}*/
	
	public void addRecipes() {
		fusion("hydrogen", 1000, "hydrogen", 1000, "deuterium", 250, "deuterium", 250, "deuterium", 250, "deuterium", 250, 0);
		fusion("hydrogen", 1000, "deuterium", 1000, "helium3", 250, "helium3", 250, "helium3", 250, "helium3", 250, 1);
		fusion("hydrogen", 1000, "tritium", 1000, "helium3", 500, "helium3", 500, "neutron", 10, "neutron", 10, 2);
		fusion("hydrogen", 1000, "helium3", 1000, "helium", 250, "helium", 250, "helium", 250, "helium", 250, 3);
		fusion("hydrogen", 1000, "lithium6", 144, "tritium", 500, "tritium", 500, "helium", 500, "helium", 500, 4);
		fusion("hydrogen", 1000, "lithium7", 144, "helium", 500, "helium", 500, "helium", 500, "helium", 500, 5);
		fusion("hydrogen", 1000, "boron11", 144, "helium", 750, "helium", 750, "helium", 750, "helium", 750, 6);
		
		fusion("deuterium", 1000, "deuterium", 1000, "hydrogen", 500, "tritium", 500, "helium3", 500, "neutron", 10, 7);
		fusion("deuterium", 1000, "tritium", 1000, "helium", 500, "helium", 500, "neutron", 10, "neutron", 10, 8);
		fusion("deuterium", 1000, "helium3", 1000, "hydrogen", 500, "hydrogen", 500, "helium", 500, "helium", 500, 9);
		fusion("deuterium", 1000, "lithium6", 144, "helium", 500, "helium", 500, "helium", 500, "helium", 500, 10);
		fusion("deuterium", 1000, "lithium7", 144, "helium", 1000, "helium", 1000, "neutron", 10, "neutron", 10, 11);
		fusion("deuterium", 1000, "boron11", 144, "helium", 1500, "helium", 1500, "neutron", 10, "neutron", 10, 12);
		
		fusion("tritium", 1000, "tritium", 1000, "helium", 500, "helium", 500, "neutron", 20, "neutron", 20, 13);
		fusion("tritium", 1000, "helium3", 1000, "hydrogen", 1000, "helium", 1000, "neutron", 10, "neutron", 10, 14);
		fusion("tritium", 1000, "lithium6", 144, "helium", 1000, "helium", 1000, "neutron", 10, "neutron", 10, 15);
		fusion("tritium", 1000, "lithium7", 144, "helium", 1000, "helium", 1000, "neutron", 20, "neutron", 20, 16);
		fusion("tritium", 1000, "boron11", 144, "helium", 1500, "helium", 1500, "neutron", 20, "neutron", 20, 17);
		
		fusion("helium3", 1000, "helium3", 1000, "hydrogen", 1000, "hydrogen", 1000, "helium", 500, "helium", 500, 18);
		fusion("helium3", 1000, "lithium6", 144, "hydrogen", 500, "hydrogen", 500, "helium", 1000, "helium", 1000, 19);
		fusion("helium3", 1000, "lithium7", 144, "deuterium", 500, "deuterium", 500, "helium", 1000, "helium", 1000, 20);
		fusion("helium3", 1000, "boron11", 144, "deuterium", 500, "deuterium", 500, "helium", 1500, "helium", 1500, 21);
		
		fusion("lithium6", 144, "lithium6", 144, "helium", 750, "helium", 750, "helium", 750, "helium", 750, 22);
		fusion("lithium6", 144, "lithium7", 144, "helium", 1500, "helium", 1500, "neutron", 10, "neutron", 10, 23);
		fusion("lithium6", 144, "boron11", 144, "helium", 2000, "helium", 2000, "neutron", 10, "neutron", 10, 24);
		
		fusion("lithium7", 144, "lithium7", 144, "helium", 1500, "helium", 1500, "neutron", 20, "neutron", 20, 25);
		fusion("lithium7", 144, "boron11", 144, "helium", 2000, "helium", 2000, "neutron", 20, "neutron", 20, 26);
		
		fusion("boron11", 144, "boron11", 144, "helium", 2500, "helium", 2500, "neutron", 20, "neutron", 20, 27);
	}
	
	public void fusion(String in1, int amountIn1, String in2, int amountIn2, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int combo) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(in2, amountIn2), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), NCConfig.fusion_fuel_time[combo], NCConfig.fusion_power[combo], NCConfig.fusion_heat_variable[combo]);
	}
	
	public String getRecipeName() {
		return "fusion";
	}
}
