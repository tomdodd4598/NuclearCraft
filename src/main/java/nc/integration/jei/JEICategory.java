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

	@Override
	public String getUid() {
		return handler.getUUID();
	}

	/*public String getTitle() {
		return handler.getTitle();
	}*/

	@Override
	public Class getRecipeClass() {
		return handler.getRecipeClass();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(JEIRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(JEIRecipe recipe) {
		return recipe.recipeHandler.getRecipeName().equals(getUid());
	}

	public String getRecipeCategoryUid() {
		return getUid();
	}
	
	@Override
	public String getRecipeCategoryUid(JEIRecipe id) {
		return getUid();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		recipeWrapper.getIngredients(ingredients);
		setRecipe(recipeLayout, recipeWrapper, ingredients);
	}
	
	@Override
	public String getModName() {
		return Global.MOD_NAME;
	}
}
