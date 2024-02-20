package nc.tile.multiblock;

import nc.container.multiblock.controller.ContainerMultiblockController;
import nc.multiblock.*;
import nc.network.multiblock.MultiblockUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public interface IMultiblockGuiPart<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, GUITILE extends IMultiblockGuiPart<MULTIBLOCK, T, PACKET, GUITILE>> extends IMultiblockPacketPart<MULTIBLOCK, T, PACKET> {
	
	ContainerMultiblockController<MULTIBLOCK, T, PACKET, GUITILE> getContainer(EntityPlayer player);
}
