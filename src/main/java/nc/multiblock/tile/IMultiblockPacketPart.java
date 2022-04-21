package nc.multiblock.tile;

import nc.multiblock.Multiblock;
import nc.network.multiblock.MultiblockUpdatePacket;

public interface IMultiblockPacketPart<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket> extends ITileMultiblockPart<MULTIBLOCK, T> {
	
}
