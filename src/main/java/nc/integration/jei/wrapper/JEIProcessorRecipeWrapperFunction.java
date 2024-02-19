package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.BasicRecipe;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface JEIProcessorRecipeWrapperFunction<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, INFO, WRAPPER>> {
	
	WRAPPER apply(String name, IGuiHelper guiHelper, BasicRecipe recipe);
}
