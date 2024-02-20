package nc.tile.multiblock.manager;

import it.unimi.dsi.fastutil.longs.LongSet;
import nc.multiblock.*;
import nc.tile.multiblock.ITileLogicMultiblockPart;

public interface ITileManager<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	LongSet getListenerPosSet();
	
	void refreshManager();
	
	void refreshListeners(boolean refreshPosSet);
}
