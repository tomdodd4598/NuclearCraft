package nc.recipe.multiblock;

import static nc.config.NCConfig.*;

import java.util.List;

import nc.recipe.BasicRecipeHandler;

public class TurbineRecipes extends BasicRecipeHandler {
	
	public TurbineRecipes() {
		super("turbine", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("high_pressure_steam", 1), fluidStack("exhaust_steam", 4), turbine_power_per_mb[0], turbine_expansion_level[0], turbine_spin_up_multiplier[0], "cloud", 1D / 23.2D);
		addRecipe(fluidStack("low_pressure_steam", 1), fluidStack("low_quality_steam", 2), turbine_power_per_mb[1], turbine_expansion_level[1], turbine_spin_up_multiplier[1], "cloud", 1D / 23.2D);
		addRecipe(fluidStack("steam", 1), fluidStack("low_quality_steam", 2), turbine_power_per_mb[2], turbine_expansion_level[2], turbine_spin_up_multiplier[2], "cloud", 1D / 23.2D);
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 0D);
		fixer.add(Double.class, 1D);
		fixer.add(Double.class, 1D);
		fixer.add(String.class, "cloud");
		fixer.add(Double.class, 1D / 23.2D);
		return fixer.fixed;
	}
}
