package nc.multiblock.tile.port;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITilePort<MULTIBLOCK extends Multiblock<T, PACKET> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET, PACKET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET, PACKET>, PACKET extends MultiblockUpdatePacket> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET> {
	
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
