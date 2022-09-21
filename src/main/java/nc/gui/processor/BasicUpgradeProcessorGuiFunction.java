package nc.gui.processor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface BasicUpgradeProcessorGuiFunction<TILE extends TileEntity> {
	
	Object apply(Container inventory, EntityPlayer player, TILE tile, String textureLocation);
}
