package nc.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.integration.jei.wrapper.JEIProcessorRecipeWrapper;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

public class JEIProcessorRecipeCategory<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, INFO, WRAPPER>> extends JEIRecipeCategory<WRAPPER, JEIProcessorRecipeCategory<TILE, PACKET, INFO, WRAPPER>, JEIProcessorCategoryInfo<TILE, PACKET, INFO, WRAPPER>> {
	
	public JEIProcessorRecipeCategory(IGuiHelper guiHelper, JEIProcessorCategoryInfo<TILE, PACKET, INFO, WRAPPER> categoryInfo) {
		super(guiHelper, categoryInfo);
	}
}
