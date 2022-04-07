package nc.recipe.processor;

import static nc.config.NCConfig.rock_crusher_alternate;

import java.util.*;

import com.google.common.collect.Lists;

import nc.recipe.BasicRecipeHandler;

public class RockCrusherRecipes extends BasicRecipeHandler {
	
	public RockCrusherRecipes() {
		super("rock_crusher", 1, 0, 3, 0);
	}
	
	@Override
	public void addRecipes() {
		if (rock_crusher_alternate) {
			addRecipe(oreStackList(Lists.newArrayList("stoneGranite", "stoneGranitePolished"), 1), chanceOreStack("dustRhodochrosite", 2, 20), chanceOreStack("dustRhodochrosite", 2, 20), chanceOreStack("dustVilliaumite", 1, 35), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneDiorite", "stoneDioritePolished"), 1), chanceOreStack("dustZirconium", 2, 50), chanceOreStack("dustFluorite", 2, 45), chanceOreStack("dustCarobbiite", 1, 70), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneAndesite", "stoneAndesitePolished"), 1), chanceOreStack("dustBeryllium", 2, 25), chanceOreStack("dustBeryllium", 2, 25), chanceOreStack("dustArsenic", 1, 30), 1D, 1D);
		}
		else {
			addRecipe(oreStackList(Lists.newArrayList("stoneGranite", "stoneGranitePolished"), 1), chanceOreStack("dustRhodochrosite", 2, 40), chanceOreStack("dustSulfur", 2, 30), chanceOreStack("dustVilliaumite", 1, 35), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneDiorite", "stoneDioritePolished"), 1), chanceOreStack("dustZirconium", 2, 50), chanceOreStack("dustFluorite", 2, 45), chanceOreStack("dustCarobbiite", 1, 70), 1D, 1D);
			addRecipe(oreStackList(Lists.newArrayList("stoneAndesite", "stoneAndesitePolished"), 1), chanceOreStack("dustBeryllium", 2, 50), chanceOreStack("dustAlugentum", 2, 30), chanceOreStack("dustArsenic", 1, 30), 1D, 1D);
		}
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
