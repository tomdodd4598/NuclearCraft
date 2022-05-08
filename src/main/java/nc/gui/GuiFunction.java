package nc.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface GuiFunction<TILE extends TileEntity> {
	
	Object apply(EntityPlayer player, TILE tile);
}
