package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;

public class RockCrusherRecipes extends ProcessorRecipeHandler {
	
	public RockCrusherRecipes() {
		super("rock_crusher", 1, 0, 3, 0);
	}

	@Override
	public void addRecipes() {
		addRecipe(oreStackList(Lists.newArrayList("stoneGranite", "stoneGranitePolished"), 1), chanceOreStack("dustRhodochrosite", 2, 40), chanceOreStack("dustSulfur", 2, 30), chanceOreStack("dustVilliaumite", 1, 35), 1D, 1D);
		addRecipe(oreStackList(Lists.newArrayList("stoneDiorite", "stoneDioritePolished"), 1), chanceOreStack("dustFluorite", 2, 45), chanceOreStack("dustCarobbiite", 2, 35), chanceOreStack("dustZirconium", 1, 40), 1D, 1D);
		addRecipe(oreStackList(Lists.newArrayList("stoneAndesite", "stoneAndesitePolished"), 1), chanceOreStack("dustBeryllium", 2, 50), chanceOreStack("dustAlugentum", 2, 25), chanceOreStack("dustArsenic", 1, 30), 1D, 1D);
	}
}
