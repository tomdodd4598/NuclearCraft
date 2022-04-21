package nc.multiblock.tile;

import nc.multiblock.*;
import nc.network.multiblock.MultiblockUpdatePacket;

public interface IMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends IMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER>> extends IMultiblockGuiPart<MULTIBLOCK, T, PACKET, CONTROLLER> {
	
}
