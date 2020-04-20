package nc.multiblock.tile.port;

import nc.multiblock.ILogicMultiblock;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITilePortTarget<MULTIBLOCK extends Multiblock<T, ? extends MultiblockUpdatePacket> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, ? extends MultiblockUpdatePacket>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public BlockPos getMasterPortPos();
	
	public void setMasterPortPos(BlockPos pos);
	
	public void clearMasterPort();
	
	public void refreshMasterPort();
	
	public boolean onPortRefresh();
}
