package nc.multiblock.tile.manager;

import it.unimi.dsi.fastutil.longs.LongSet;
import nc.multiblock.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;

public interface ITileManager<MULTIBLOCK extends Multiblock<T, PACKET> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER, PACKET>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER, PACKET>, PACKET extends MultiblockUpdatePacket> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET> {
	
	public LongSet getListenerPosSet();
	
	public void refreshManager();
	
	public void refreshListeners(boolean refreshPosSet);
}
