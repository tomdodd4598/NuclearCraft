package nc.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEISimpleCategoryInfo;
import nc.integration.jei.wrapper.JEISimpleRecipeWrapper;

public class JEISimpleRecipeCategory<WRAPPER extends JEISimpleRecipeWrapper<WRAPPER>> extends JEIRecipeCategory<WRAPPER, JEISimpleRecipeCategory<WRAPPER>, JEISimpleCategoryInfo<WRAPPER>> {
	
	public JEISimpleRecipeCategory(IGuiHelper guiHelper, JEISimpleCategoryInfo<WRAPPER> categoryInfo) {
		super(guiHelper, categoryInfo);
	}
}
