package nc.recipe.processor;

import com.google.common.collect.Lists;
import nc.ModCheck;

import static nc.config.NCConfig.*;

public class RockCrusherRecipes extends BasicProcessorRecipeHandler {
	
	public RockCrusherRecipes() {
		super("rock_crusher", 1, 0, 3, 0);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[18]) {
			return;
		}
		
		if (rock_crusher_alternate) {
			addRecipe(oreStackList(Lists.newArrayList("stoneGranite", "stoneGranitePolished"), 1), chanceOreStack("dustRhodochrosite", 2, 20), chanceOreStack("dustRhodochrosite", 2, 20), chanceOreStack("dustVilliaumite", 1, 35), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneDiorite", "stoneDioritePolished"), 1), chanceOreStack("dustZirconium", 2, 50), chanceOreStack("dustFluorite", 2, 45), chanceOreStack("dustCarobbiite", 1, 70), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneAndesite", "stoneAndesitePolished"), 1), chanceOreStack("dustBeryllium", 2, 20), chanceOreStack("dustBeryllium", 2, 20), chanceOreStack("dustArsenic", 1, 30), 1D, 1D);
		}
		else {
			addRecipe(oreStackList(Lists.newArrayList("stoneGranite", "stoneGranitePolished"), 1), chanceOreStack("dustRhodochrosite", 2, 40), chanceOreStack("dustSulfur", 2, 30), chanceOreStack("dustVilliaumite", 1, 35), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneDiorite", "stoneDioritePolished"), 1), chanceOreStack("dustZirconium", 2, 50), chanceOreStack("dustFluorite", 2, 45), chanceOreStack("dustCarobbiite", 1, 70), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneAndesite", "stoneAndesitePolished"), 1), chanceOreStack("dustBeryllium", 2, 40), chanceOreStack("dustAlugentum", 2, 30), chanceOreStack("dustArsenic", 1, 30), 1D, 1D);
		}
		
		if (ModCheck.qmdLoaded()) {
			addRecipe("blockSoullessSandstone", chanceOreStack("dustBarite", 1, 80), chanceOreStack("dustCoal", 1, 60), chanceOreStack("dustDysprholminite", 1, 40), 1D, 1D);
		}
		else {
			addRecipe("blockSoullessSandstone", chanceOreStack("dustBarite", 1, 80), chanceOreStack("dustNichromite", 1, 60), chanceOreStack("dustDysprholminite", 1, 40), 1D, 1D);
		}
	}
}
