package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.JEIRecipeCategory;
import nc.integration.jei.category.info.JEICategoryInfo;
import nc.recipe.BasicRecipe;

@FunctionalInterface
public interface JEIRecipeWrapperFunction<WRAPPER extends JEIRecipeWrapper, CATEGORY extends JEIRecipeCategory<WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEICategoryInfo<WRAPPER, CATEGORY, CATEGORY_INFO>> {
	
	WRAPPER apply(IGuiHelper guiHelper, CATEGORY_INFO categoryInfo, BasicRecipe recipe);
}
