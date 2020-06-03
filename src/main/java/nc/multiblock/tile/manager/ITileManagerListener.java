package nc.multiblock.tile.manager;

import nc.multiblock.*;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITileManagerListener<MULTIBLOCK extends Multiblock<T, ? extends MultiblockUpdatePacket> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, ? extends MultiblockUpdatePacket>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public BlockPos getManagerPos();
	
	public void setManagerPos(BlockPos pos);
	
	public void clearManager();
	
	public void refreshManager();
	
	public boolean onManagerRefresh(MANAGER manager);
}
