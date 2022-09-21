package nc.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface ContainerFunction<TILE extends TileEntity> {
	
	Container apply(EntityPlayer player, TILE tile);
}
