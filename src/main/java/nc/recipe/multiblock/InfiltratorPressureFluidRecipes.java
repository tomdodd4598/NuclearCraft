package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;

import java.util.List;

import static nc.config.NCConfig.machine_infiltrator_pressure_fluid_efficiency;

public class InfiltratorPressureFluidRecipes extends BasicRecipeHandler {
	
	public InfiltratorPressureFluidRecipes() {
		super("infiltrator_pressure_fluid", 0, 1, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		for (String s : machine_infiltrator_pressure_fluid_efficiency) {
			int index = s.indexOf('@');
			addRecipe(s.substring(0, index), Double.valueOf(s.substring(1 + index)));
		}
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 1D);
		return fixer.fixed;
	}
}
