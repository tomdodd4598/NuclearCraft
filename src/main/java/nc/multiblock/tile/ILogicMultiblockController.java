package nc.multiblock.tile;

import nc.multiblock.*;
import nc.network.multiblock.MultiblockUpdatePacket;

public interface ILogicMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends ILogicMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER>> extends IMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER> {
	
	public String getLogicID();
}
