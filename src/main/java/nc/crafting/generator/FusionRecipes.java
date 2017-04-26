package nc.crafting.generator;

import nc.handler.ProcessorRecipeHandler;

public class FusionRecipes extends ProcessorRecipeHandler {
	
	private static final FusionRecipes RECIPES = new FusionRecipes();

	public FusionRecipes() {
		super(0, 2, 0, 4, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		fusion("hydrogen", 1000, "hydrogen", 1000, "deuterium", 250, "deuterium", 250, "deuterium", 250, "deuterium", 250);
		fusion("hydrogen", 1000, "deuterium", 1000, "helium3", 250, "helium3", 250, "helium3", 250, "helium3", 250);
		fusion("hydrogen", 1000, "tritium", 1000, "helium3", 500, "helium3", 500, "neutron", 50, "neutron", 50);
		fusion("hydrogen", 1000, "helium3", 1000, "helium", 250, "helium", 250, "helium", 250, "helium", 250);
		fusion("hydrogen", 1000, "lithium6", 72, "tritium", 500, "tritium", 500, "helium", 500, "helium", 500);
		fusion("hydrogen", 1000, "lithium7", 72, "helium", 500, "helium", 500, "helium", 500, "helium", 500);
		fusion("hydrogen", 1000, "boron11", 72, "helium", 750, "helium", 750, "helium", 750, "helium", 750);
		
		fusion("deuterium", 1000, "deuterium", 1000, "hydrogen", 500, "tritium", 500, "helium3", 500, "neutron", 50);
		fusion("deuterium", 1000, "tritium", 1000, "helium", 500, "helium", 500, "neutron", 50, "neutron", 50);
		fusion("deuterium", 1000, "helium3", 1000, "hydrogen", 500, "hydrogen", 500, "helium", 500, "helium", 500);
		fusion("deuterium", 1000, "lithium6", 72, "helium", 500, "helium", 500, "helium", 500, "helium", 500);
		fusion("deuterium", 1000, "lithium7", 72, "helium", 1000, "helium", 1000, "neutron", 50, "neutron", 50);
		fusion("deuterium", 1000, "boron11", 72, "helium", 1500, "helium", 1500, "neutron", 50, "neutron", 50);
		
		fusion("tritium", 1000, "tritium", 1000, "helium", 500, "helium", 500, "neutron", 100, "neutron", 100);
		fusion("tritium", 1000, "helium3", 1000, "hydrogen", 1000, "helium", 1000, "neutron", 50, "neutron", 50);
		fusion("tritium", 1000, "lithium6", 72, "helium", 1000, "helium", 1000, "neutron", 50, "neutron", 50);
		fusion("tritium", 1000, "lithium7", 72, "helium", 1000, "helium", 1000, "neutron", 100, "neutron", 100);
		fusion("tritium", 1000, "boron11", 72, "helium", 1500, "helium", 1500, "neutron", 100, "neutron", 100);
		
		fusion("helium3", 1000, "helium3", 1000, "hydrogen", 1000, "hydrogen", 1000, "helium", 500, "helium", 500);
		fusion("helium3", 1000, "lithium6", 72, "hydrogen", 500, "hydrogen", 500, "helium", 1000, "helium", 1000);
		fusion("helium3", 1000, "lithium7", 72, "deuterium", 500, "deuterium", 500, "helium", 1000, "helium", 1000);
		fusion("helium3", 1000, "boron11", 72, "deuterium", 500, "deuterium", 500, "helium", 1500, "helium", 1500);
		
		fusion("lithium6", 72, "lithium6", 72, "helium", 750, "helium", 750, "helium", 750, "helium", 750);
		fusion("lithium6", 72, "lithium7", 72, "helium", 1500, "helium", 1500, "neutron", 50, "neutron", 50);
		fusion("lithium6", 72, "boron11", 72, "helium", 2000, "helium", 2000, "neutron", 50, "neutron", 50);
		
		fusion("lithium7", 72, "lithium7", 72, "helium", 1500, "helium", 1500, "neutron", 100, "neutron", 100);
		fusion("lithium7", 72, "boron11", 72, "helium", 2000, "helium", 2000, "neutron", 100, "neutron", 100);
		
		fusion("boron11", 72, "boron11", 72, "helium", 2500, "helium", 2500, "neutron", 100, "neutron", 100);
	}
	
	public void fusion(String in1, int amountIn1, String in2, int amountIn2, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(in2, amountIn2), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4));
	}
}
