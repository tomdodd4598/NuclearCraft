package nc.multiblock.tile.manager;

import nc.multiblock.*;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITileManagerListener<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	public BlockPos getManagerPos();
	
	public void setManagerPos(BlockPos pos);
	
	public void clearManager();
	
	public void refreshManager();
	
	public boolean onManagerRefresh(MANAGER manager);
}
