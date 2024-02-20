package nc.gui;

import nc.container.ContainerFunction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface GuiFunction<TILE extends TileEntity> {
	
	GuiContainer apply(EntityPlayer player, TILE tile);
	
	static <T extends TileEntity> GuiFunction<T> of(String modId, String name, ContainerFunction<T> containerFunction, GuiInfoTileFunction<T> guiFunction) {
		return (player, tile) -> guiFunction.apply(containerFunction.apply(player, tile), player, tile, modId + ":textures/gui/container/" + name + ".png");
	}
}
