package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEISimpleCategoryInfo;
import nc.recipe.BasicRecipe;

public abstract class JEISimpleRecipeWrapper<WRAPPER extends JEISimpleRecipeWrapper<WRAPPER>> extends JEIRecipeWrapper {
	
	protected final JEISimpleCategoryInfo<WRAPPER> categoryInfo;
	
	protected final int tooltipX, tooltipY, tooltipW, tooltipH;
	
	protected JEISimpleRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<WRAPPER> categoryInfo, BasicRecipe recipe) {
		super(guiHelper, categoryInfo.getRecipeHandler(), recipe, categoryInfo.jeiTexture, categoryInfo.jeiBackgroundX, categoryInfo.jeiBackgroundY, categoryInfo.progressBarGuiX, categoryInfo.progressBarGuiY, categoryInfo.progressBarGuiW, categoryInfo.progressBarGuiH, categoryInfo.progressBarGuiU, categoryInfo.progressBarGuiV);
		this.categoryInfo = categoryInfo;
		
		tooltipX = categoryInfo.jeiTooltipX - categoryInfo.jeiBackgroundX;
		tooltipY = categoryInfo.jeiTooltipY - categoryInfo.jeiBackgroundY;
		tooltipW = categoryInfo.jeiTooltipW;
		tooltipH = categoryInfo.jeiTooltipH;
	}
	
	protected boolean showTooltip(int mouseX, int mouseY) {
		return mouseX >= tooltipX && mouseY >= tooltipY && mouseX <= tooltipX + tooltipW && mouseY <= tooltipY + tooltipH;
	}
}
