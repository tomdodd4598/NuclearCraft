package nc.gui.processor;

import nc.tile.processor.*;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface GuiProcessorBackgroundConsumer<TILE extends TileEntity & IProcessorGui<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> {
	
	void accept(GuiProcessor<TILE, INFO> gui, float partialTicks, int mouseX, int mouseY);
}
