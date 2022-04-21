package nc.multiblock.tile.manager;

import it.unimi.dsi.fastutil.longs.LongSet;
import nc.multiblock.*;
import nc.multiblock.tile.ITileLogicMultiblockPart;

public interface ITileManager<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public LongSet getListenerPosSet();
	
	public void refreshManager();
	
	public void refreshListeners(boolean refreshPosSet);
}
