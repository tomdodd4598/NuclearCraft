package nc.tile.multiblock.port;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.*;
import nc.tile.multiblock.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITilePort<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	ObjectSet<TARGET> getTargets();
	
	BlockPos getMasterPortPos();
	
	void setMasterPortPos(BlockPos pos);
	
	void clearMasterPort();
	
	void refreshMasterPort();
	
	void refreshTargets();
	
	void setRefreshTargetsFlag(boolean refreshTargetsFlag);
	
	default int getInventoryStackLimitPerConnection() {
		return 2;
	}
	
	void setInventoryStackLimit(int stackLimit);
	
	int getTankCapacityPerConnection();
	
	int getTankBaseCapacity();
	
	void setTankCapacity(int capacity);
}
