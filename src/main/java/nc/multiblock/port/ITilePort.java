package nc.multiblock.port;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.ILogicMultiblock;
import nc.multiblock.ITileLogicMultiblockPart;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.network.MultiblockUpdatePacket;
import net.minecraft.util.math.BlockPos;

public interface ITilePort<MULTIBLOCK extends Multiblock<? extends MultiblockUpdatePacket> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, ? extends MultiblockUpdatePacket>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public ObjectSet<TARGET> getConnectedParts();
	
	public BlockPos getMasterPortPos();
	
	public void setMasterPortPos(BlockPos pos);
	
	public void clearMasterPort();
	
	public void refreshMasterPort();
	
	public void setInventoryStackLimit(int stackLimit);
}
