package nc.recipe.multiblock;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;

public class FissionIrradiatorRecipes extends ProcessorRecipeHandler {
	
	public FissionIrradiatorRecipes() {
		super("fission_irradiator", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("ingotThorium", "dustThorium"), "dustTBP", 160000, NCConfig.fission_irradiator_heat_per_flux[0], NCConfig.fission_irradiator_efficiency[0], RadSources.THORIUM);
		addRecipe(Lists.newArrayList("ingotTBP", "dustTBP"), "dustProtactinium233", 2720000, NCConfig.fission_irradiator_heat_per_flux[1], NCConfig.fission_irradiator_efficiency[1], RadSources.TBP);
		addRecipe(Lists.newArrayList("ingotBismuth", "dustBismuth"), "dustPolonium", 1920000, NCConfig.fission_irradiator_heat_per_flux[2], NCConfig.fission_irradiator_efficiency[2], RadSources.BISMUTH);
	}
}
