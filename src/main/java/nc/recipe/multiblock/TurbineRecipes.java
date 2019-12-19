package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class TurbineRecipes extends ProcessorRecipeHandler {
	
	public TurbineRecipes() {
		super("high_turbine", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("high_pressure_steam", 1), fluidStack("exhaust_steam", 4), NCConfig.turbine_power_per_mb[0], NCConfig.turbine_expansion_level[0], "cloud", 1D/23.2D);
		addRecipe(fluidStack("low_pressure_steam", 1), fluidStack("low_quality_steam", 2), NCConfig.turbine_power_per_mb[1], NCConfig.turbine_expansion_level[1], "cloud", 1D/23.2D);
		addRecipe(fluidStack("steam", 1), fluidStack("low_quality_steam", 2), NCConfig.turbine_power_per_mb[2], NCConfig.turbine_expansion_level[2], "cloud", 1D/23.2D);
	}
}
