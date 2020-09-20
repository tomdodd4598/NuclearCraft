package nc.multiblock.tile.manager;

import nc.multiblock.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITileManagerListener<MULTIBLOCK extends Multiblock<T, PACKET> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER, PACKET>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER, PACKET>, PACKET extends MultiblockUpdatePacket> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET> {
	
	public BlockPos getManagerPos();
	
	public void setManagerPos(BlockPos pos);
	
	public void clearManager();
	
	public void refreshManager();
	
	public boolean onManagerRefresh(MANAGER manager);
}
