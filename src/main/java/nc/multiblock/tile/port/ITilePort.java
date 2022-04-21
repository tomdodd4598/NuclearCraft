package nc.multiblock.tile.port;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.*;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITilePort<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public ObjectSet<TARGET> getTargets();
	
	public BlockPos getMasterPortPos();
	
	public void setMasterPortPos(BlockPos pos);
	
	public void clearMasterPort();
	
	public void refreshMasterPort();
	
	public void refreshTargets();
	
	public void setRefreshTargetsFlag(boolean refreshTargetsFlag);
	
	public default int getInventoryStackLimitPerConnection() {
		return 2;
	}
	
	public void setInventoryStackLimit(int stackLimit);
	
	public int getTankCapacityPerConnection();
	
	public int getTankBaseCapacity();
	
	public void setTankCapacity(int capacity);
}
