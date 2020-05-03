package nc.multiblock.tile.manager;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.ILogicMultiblock;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;

public interface ITileManager<MULTIBLOCK extends Multiblock<T, ? extends MultiblockUpdatePacket> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, ? extends MultiblockUpdatePacket>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public ObjectSet<LISTENER> getListeners();
	
	public void refreshManager();
	
	//public BlockPos getMasterManagerPos();
	
	//public void setMasterManagerPos(BlockPos pos);
	
	//public void clearMasterManager();
	
	//public void refreshMasterManager();
	
	public void refreshListeners();
}
