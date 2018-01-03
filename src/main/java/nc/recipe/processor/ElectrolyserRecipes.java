package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class ElectrolyserRecipes extends BaseRecipeHandler {
	
	public ElectrolyserRecipes() {
		super(0, 1, 0, 4, false);
	}

	@Override
	public void addRecipes() {
		electrolyse("water", 1000, "hydrogen", 475, "hydrogen", 475, "deuterium", 50, "oxygen", 500, NCConfig.processor_time[8]);
		electrolyse("hydrofluoric_acid", 1000, "hydrogen", 250, "hydrogen", 250, "fluorine", 250, "fluorine", 250, NCConfig.processor_time[8]);
		
		// Mekanism
		electrolyse("heavywater", 1000, "deuterium", 475, "deuterium", 475, "tritium", 50, "oxygen", 500, NCConfig.processor_time[8]);
	}
	
	public void electrolyse(String in1, int amountIn1, String out1, int amountOut1, String out2, int amountOut2, String out3, int amountOut3, String out4, int amountOut4, int time) {
		addRecipe(fluidStack(in1, amountIn1), fluidStack(out1, amountOut1), fluidStack(out2, amountOut2), fluidStack(out3, amountOut3), fluidStack(out4, amountOut4), time);
	}

	@Override
	public String getRecipeName() {
		return "electrolyser";
	}
}
