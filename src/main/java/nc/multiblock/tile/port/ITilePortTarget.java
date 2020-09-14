package nc.multiblock.tile.port;

import nc.multiblock.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITilePortTarget<MULTIBLOCK extends Multiblock<T, PACKET> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET, PACKET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET, PACKET>, PACKET extends MultiblockUpdatePacket> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET> {
	
	public BlockPos getMasterPortPos();
	
	public void setMasterPortPos(BlockPos pos);
	
	public void clearMasterPort();
	
	public void refreshMasterPort();
	
	public boolean onPortRefresh();
}
