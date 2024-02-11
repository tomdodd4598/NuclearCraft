package nc.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.integration.jei.wrapper.JEIProcessorRecipeWrapper;
import nc.network.tile.ProcessorUpdatePacket;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface JEIProcessorRecipeCategoryFunction<TILE extends TileEntity & IProcessor<TILE, PACKET, CONTAINER_INFO>, PACKET extends ProcessorUpdatePacket, CONTAINER_INFO extends ProcessorContainerInfo<TILE, PACKET, CONTAINER_INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY extends JEIProcessorRecipeCategory<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEIProcessorCategoryInfo<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>> {
	
	CATEGORY apply(IGuiHelper guiHelper, CATEGORY_INFO categoryInfo);
}
