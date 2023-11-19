package nc.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEICategoryInfoImpl.*;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl.*;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;

public class JEIRecipeCategoryImpl {
	
	public static class JEIBasicProcessorRecipeCategory<TILE extends TileBasicProcessor<TILE>, WRAPPER extends JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorRecipeCategory<TILE, BasicProcessorContainerInfo<TILE>, JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> {
		
		public JEIBasicProcessorRecipeCategory(IGuiHelper guiHelper, JEIBasicProcessorCategoryInfo<TILE, WRAPPER> categoryInfo) {
			super(guiHelper, categoryInfo);
		}
	}
	
	public static class JEIBasicUpgradableProcessorRecipeCategory<TILE extends TileBasicUpgradableProcessor<TILE>, WRAPPER extends JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorRecipeCategory<TILE, BasicUpgradableProcessorContainerInfo<TILE>, JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> {
		
		public JEIBasicUpgradableProcessorRecipeCategory(IGuiHelper guiHelper, JEIBasicUpgradableProcessorCategoryInfo<TILE, WRAPPER> categoryInfo) {
			super(guiHelper, categoryInfo);
		}
	}
}
