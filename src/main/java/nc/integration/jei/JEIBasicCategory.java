package nc.integration.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.*;
import nc.Global;
import nc.integration.jei.NCJEI.IJEIHandler;

public abstract class JEIBasicCategory<WRAPPER extends JEIBasicRecipe<WRAPPER>> extends BlankRecipeCategory<WRAPPER> implements IRecipeHandler<WRAPPER> {
	
	protected final IJEIHandler<WRAPPER> jeiHandler;
	
	public JEIBasicCategory(IJEIHandler<WRAPPER> jeiHandler) {
		this.jeiHandler = jeiHandler;
	}
	
	@Override
	public String getModName() {
		return Global.MOD_NAME;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, WRAPPER recipeWrapper, IIngredients ingredients) {
		recipeWrapper.getIngredients(ingredients);
	}
	
	@Override
	public String getUid() {
		return jeiHandler.getUid();
	}
	
	@Override
	public Class<WRAPPER> getRecipeClass() {
		return jeiHandler.getRecipeWrapperClass();
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(WRAPPER recipeWrapper) {
		return recipeWrapper;
	}
	
	@Override
	public boolean isRecipeValid(WRAPPER recipeWrapper) {
		return getUid().equals(Global.MOD_ID + "_" + recipeWrapper.recipeHandler.getName());
	}
	
	@Override
	public String getRecipeCategoryUid(WRAPPER recipeWrapper) {
		return getUid();
	}
}
