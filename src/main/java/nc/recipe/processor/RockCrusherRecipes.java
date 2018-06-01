package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class RockCrusherRecipes extends BaseRecipeHandler {
	
	public RockCrusherRecipes() {
		super("rock_crusher", 1, 0, 3, 0);
	}

	@Override
	public void addRecipes() {
		addRecipe(oreStackList(Lists.newArrayList("stoneGranite", "stoneGranitePolished"), 8), oreStack("dustRhodochrosite", 6), oreStack("dustSulfur", 5), "dustVilliaumite", NCConfig.processor_time[18]);
		addRecipe(oreStackList(Lists.newArrayList("stoneDiorite", "stoneDioritePolished"), 8), oreStack("dustZirconium", 5), oreStack("dustFluorite", 4), oreStack("dustCarobbiite", 3), NCConfig.processor_time[18]);
		addRecipe(oreStackList(Lists.newArrayList("stoneAndesite", "stoneAndesitePolished"), 8), oreStack("dustBeryllium", 8), oreStack("dustFluorite", 3), "dustCarobbiite", NCConfig.processor_time[18]);
	}
}
