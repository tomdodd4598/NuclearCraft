package nc.integration.jei.category.generator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.integration.jei.category.JEIMachineCategory;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl;
import nc.recipe.IngredientSorption;

public class DecayGeneratorCategory extends JEIMachineCategory<JEIRecipeWrapperImpl.DecayGenerator> {
	
	public DecayGeneratorCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipeWrapperImpl.DecayGenerator> handler) {
		super(guiHelper, handler, "decay_generator", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperImpl.DecayGenerator recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 116 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
