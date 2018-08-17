package nc.integration.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;

public abstract class JEICategory extends BlankRecipeCategory implements IRecipeHandler<JEIProcessorRecipeWrapper> {

	protected final IJEIHandler jeiHandler;

	public JEICategory(IJEIHandler jeiHandler) {
		this.jeiHandler = jeiHandler;
	}
	
	@Override
	public String getModName() {
		return Global.MOD_NAME;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		recipeWrapper.getIngredients(ingredients);
		setRecipe(recipeLayout, recipeWrapper, ingredients);
	}

	@Override
	public String getUid() {
		return jeiHandler.getUUID();
	}

	@Override
	public Class getRecipeClass() {
		return jeiHandler.getJEIRecipeWrapper();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(JEIProcessorRecipeWrapper recipeWrapper) {
		return recipeWrapper;
	}

	@Override
	public boolean isRecipeValid(JEIProcessorRecipeWrapper recipeWrapper) {
		return recipeWrapper.recipeHandler.getRecipeName().equals(getUid());
	}
	
	@Override
	public String getRecipeCategoryUid(JEIProcessorRecipeWrapper recipeWrapper) {
		return getUid();
	}
}
