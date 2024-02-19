package nc.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEICategoryInfo;
import nc.integration.jei.wrapper.JEIRecipeWrapper;

@FunctionalInterface
public interface JEIRecipeCategoryFunction<WRAPPER extends JEIRecipeWrapper, CATEGORY extends JEIRecipeCategory<WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEICategoryInfo<WRAPPER, CATEGORY, CATEGORY_INFO>> {
	
	CATEGORY apply(IGuiHelper guiHelper, CATEGORY_INFO categoryInfo);
}
