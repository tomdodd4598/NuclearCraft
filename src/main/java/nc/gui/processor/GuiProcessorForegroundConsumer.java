package nc.gui.processor;

import nc.tile.processor.*;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface GuiProcessorForegroundConsumer<TILE extends TileEntity & IProcessorGui<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> {
	
	void accept(GuiProcessor<TILE, INFO> gui, int mouseX, int mouseY);
}
