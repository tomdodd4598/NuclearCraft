package nc.integration.jei;

import nc.recipe.IRecipe;
import nc.recipe.BaseRecipeHandler;;

public interface IJEIRecipeBuilder {
	
	public Object buildRecipe(IRecipe recipe, BaseRecipeHandler<IRecipe> methods);
}
