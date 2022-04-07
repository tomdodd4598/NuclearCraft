package nc.multiblock;

import nc.multiblock.tile.ITileLogicMultiblockPart;
import nc.network.multiblock.MultiblockUpdatePacket;

public interface IPacketMultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PACKET extends MultiblockUpdatePacket> extends IMultiblockLogic<MULTIBLOCK, LOGIC, T> {
	
	public PACKET getMultiblockUpdatePacket();
	
	public void onMultiblockUpdatePacket(PACKET message);
}
