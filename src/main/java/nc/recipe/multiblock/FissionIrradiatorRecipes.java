package nc.recipe.multiblock;

import com.google.common.collect.Lists;

import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;

public class FissionIrradiatorRecipes extends ProcessorRecipeHandler {
	
	public FissionIrradiatorRecipes() {
		super("fission_irradiator", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("ingotThorium", "dustThorium"), "dustTBP", 160000, 0D, RadSources.THORIUM);
		addRecipe(Lists.newArrayList("ingotTBP", "dustTBP"), "dustProtactinium233", 2720000, 0D, RadSources.PROTACTINIUM_TBU);
		addRecipe(Lists.newArrayList("ingotBismuth", "dustBismuth"), "dustPolonium", 1920000, 0D, RadSources.BISMUTH);
	}
}
