package nc.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface ContainerFunction<TILE extends TileEntity> {
	
	Object apply(EntityPlayer player, TILE tile);
}
