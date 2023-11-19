package nc.integration.jei.category.info;

import java.util.List;

import nc.integration.jei.category.JEIProcessorRecipeCategoryFunction;
import nc.integration.jei.wrapper.JEIProcessorRecipeWrapperFunction;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl.*;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;

public class JEICategoryInfoImpl {
	
	public static class JEIBasicProcessorCategoryInfo<TILE extends TileBasicProcessor<TILE>, WRAPPER extends JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorCategoryInfo<TILE, BasicProcessorContainerInfo<TILE>, JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> {
		
		public JEIBasicProcessorCategoryInfo(String name, JEIProcessorRecipeCategoryFunction<TILE, BasicProcessorContainerInfo<TILE>, JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> jeiCategoryFunction, Class<JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, BasicProcessorContainerInfo<TILE>, JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> jeiRecipeFunction, List<Object> jeiCrafters) {
			super(name, jeiCategoryFunction, jeiRecipeClass, jeiRecipeFunction, jeiCrafters);
		}
	}
	
	public static class JEIBasicUpgradableProcessorCategoryInfo<TILE extends TileBasicUpgradableProcessor<TILE>, WRAPPER extends JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorCategoryInfo<TILE, BasicUpgradableProcessorContainerInfo<TILE>, JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> {
		
		public JEIBasicUpgradableProcessorCategoryInfo(String name, JEIProcessorRecipeCategoryFunction<TILE, BasicUpgradableProcessorContainerInfo<TILE>, JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> jeiCategoryFunction, Class<JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, BasicUpgradableProcessorContainerInfo<TILE>, JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> jeiRecipeFunction, List<Object> jeiCrafters) {
			super(name, jeiCategoryFunction, jeiRecipeClass, jeiRecipeFunction, jeiCrafters);
		}
	}
}
