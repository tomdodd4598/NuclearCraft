package nc.integration.jei.category.info;

import java.util.List;

import nc.integration.jei.category.JEIProcessorRecipeCategoryFunction;
import nc.integration.jei.category.JEIRecipeCategoryImpl.*;
import nc.integration.jei.wrapper.JEIProcessorRecipeWrapperFunction;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl.*;
import nc.network.tile.ProcessorUpdatePacket;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;

public class JEICategoryInfoImpl {
	
	public static class JEIBasicProcessorCategoryInfo<TILE extends TileBasicProcessor<TILE>, WRAPPER extends JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorCategoryInfo<TILE, ProcessorUpdatePacket, BasicProcessorContainerInfo<TILE>, WRAPPER, JEIBasicProcessorRecipeCategory<TILE, WRAPPER>, JEIBasicProcessorCategoryInfo<TILE, WRAPPER>> {
		
		public JEIBasicProcessorCategoryInfo(String name, JEIProcessorRecipeCategoryFunction<TILE, ProcessorUpdatePacket, BasicProcessorContainerInfo<TILE>, WRAPPER, JEIBasicProcessorRecipeCategory<TILE, WRAPPER>, JEIBasicProcessorCategoryInfo<TILE, WRAPPER>> jeiCategoryFunction, Class<WRAPPER> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, ProcessorUpdatePacket, BasicProcessorContainerInfo<TILE>, WRAPPER, JEIBasicProcessorRecipeCategory<TILE, WRAPPER>, JEIBasicProcessorCategoryInfo<TILE, WRAPPER>> jeiRecipeFunction, List<Object> jeiCrafters) {
			super(name, jeiCategoryFunction, jeiRecipeClass, jeiRecipeFunction, jeiCrafters);
		}
	}
	
	public static class JEIBasicUpgradableProcessorCategoryInfo<TILE extends TileBasicUpgradableProcessor<TILE>, WRAPPER extends JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorCategoryInfo<TILE, ProcessorUpdatePacket, BasicUpgradableProcessorContainerInfo<TILE>, WRAPPER, JEIBasicUpgradableProcessorRecipeCategory<TILE, WRAPPER>, JEIBasicUpgradableProcessorCategoryInfo<TILE, WRAPPER>> {
		
		public JEIBasicUpgradableProcessorCategoryInfo(String name, JEIProcessorRecipeCategoryFunction<TILE, ProcessorUpdatePacket, BasicUpgradableProcessorContainerInfo<TILE>, WRAPPER, JEIBasicUpgradableProcessorRecipeCategory<TILE, WRAPPER>, JEIBasicUpgradableProcessorCategoryInfo<TILE, WRAPPER>> jeiCategoryFunction, Class<WRAPPER> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, ProcessorUpdatePacket, BasicUpgradableProcessorContainerInfo<TILE>, WRAPPER, JEIBasicUpgradableProcessorRecipeCategory<TILE, WRAPPER>, JEIBasicUpgradableProcessorCategoryInfo<TILE, WRAPPER>> jeiRecipeFunction, List<Object> jeiCrafters) {
			super(name, jeiCategoryFunction, jeiRecipeClass, jeiRecipeFunction, jeiCrafters);
		}
	}
}
