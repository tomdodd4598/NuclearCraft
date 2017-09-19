package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class ElectrolyserRecipes extends BaseRecipeHandler {
	
	private static final ElectrolyserRecipes RECIPES = new ElectrolyserRecipes();
	
	public ElectrolyserRecipes() {
		super(0, 1, 0, 4, false);
	}

	public static final ElectrolyserRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		electrolyse("water", 150, "hydrogen", 380, "hydrogen", 380, "deuterium", 40, "oxygen", 400, NCConfig.processor_time[8]);
		
		// Mekanism
		electrolyse("heavywater", 150, "deuterium", 380, "deuterium", 380, "tritium", 40, "oxygen", 400, NCConfig.processor_time[8]);
	}
	
	public void electrolyse(String in1, int amountIn1, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int time) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), time);
	}

	public String getRecipeName() {
		return "electrolyser";
	}
}
