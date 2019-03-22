package nc.recipe.other;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.recipe.ProcessorRecipeHandler;

public class ActiveCoolerRecipes extends ProcessorRecipeHandler {
	
	public ActiveCoolerRecipes() {
		super("active_cooler", 0, 1, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 1; i < CoolerType.values().length; i++) {
			addRecipe(fluidStack(CoolerType.values()[i].getFluidName(), NCConfig.active_cooler_max_rate), Math.round(NCConfig.fission_active_cooling_rate[i - 1]*NCConfig.active_cooler_max_rate/20), Math.round(NCConfig.fusion_active_cooling_rate[i - 1]*NCConfig.active_cooler_max_rate/20));
		}
	}
}
