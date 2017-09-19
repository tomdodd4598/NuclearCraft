package nc.integration.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;

public abstract class JEICategory extends BlankRecipeCategory implements IRecipeHandler<JEIRecipe> {

	private final IJEIHandler handler;

	public JEICategory(IJEIHandler handler) {
		this.handler = handler;
	}

	public String getUid() {
		return handler.getUUID();
	}

	/*public String getTitle() {
		return handler.getTitle();
	}*/

	public Class getRecipeClass() {
		return handler.getRecipeClass();
	}

	public IRecipeWrapper getRecipeWrapper(JEIRecipe recipe) {
		return recipe;
	}

	public boolean isRecipeValid(JEIRecipe recipe) {
		return recipe.recipeHandler.getRecipeName().equals(getUid());
	}

	public String getRecipeCategoryUid() {
		return getUid();
	}
	
	public String getRecipeCategoryUid(JEIRecipe id) {
		return getUid();
	}

	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		recipeWrapper.getIngredients(ingredients);
		setRecipe(recipeLayout, recipeWrapper, ingredients);
	}
	
	public String getModName() {
		return Global.MOD_NAME;
	}
}
