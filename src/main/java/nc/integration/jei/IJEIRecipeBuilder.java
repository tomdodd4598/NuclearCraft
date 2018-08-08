package nc.integration.jei;

import nc.recipe.IRecipe;
import nc.recipe.ProcessorRecipeHandler;;

public interface IJEIRecipeBuilder {
	
	public Object buildRecipe(IRecipe recipe, ProcessorRecipeHandler methods);
}
