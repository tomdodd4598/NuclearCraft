package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.JEIProcessorRecipeCategory;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.BasicRecipe;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface JEIProcessorRecipeWrapperFunction<TILE extends TileEntity & IProcessor<TILE, PACKET, CONTAINER_INFO>, PACKET extends ProcessorUpdatePacket, CONTAINER_INFO extends ProcessorContainerInfo<TILE, PACKET, CONTAINER_INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY extends JEIProcessorRecipeCategory<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEIProcessorCategoryInfo<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>> {
	
	WRAPPER apply(IGuiHelper guiHelper, CATEGORY_INFO categoryInfo, BasicRecipe recipe);
}
