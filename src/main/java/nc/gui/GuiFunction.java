package nc.gui;

import nc.container.ContainerFunction;
import nc.gui.processor.BasicProcessorGuiFunction;
import nc.tile.processor.*;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface GuiFunction<TILE extends TileEntity> {
	
	Object apply(EntityPlayer player, TILE tile);
	
	public static <T extends TileEntity & IProcessor<T, INFO>, INFO extends ProcessorContainerInfo<T, INFO>> GuiFunction<T> of(String modId, String name, ContainerFunction<T> containerFunction, BasicProcessorGuiFunction<T> guiFunction) {
		return (player, tile) -> guiFunction.apply(containerFunction.apply(player, tile), player, tile, modId + ":textures/gui/container/" + name + ".png");
	}
}
