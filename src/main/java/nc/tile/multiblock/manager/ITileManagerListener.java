package nc.tile.multiblock.manager;

import nc.multiblock.*;
import nc.tile.multiblock.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITileManagerListener<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	BlockPos getManagerPos();
	
	void setManagerPos(BlockPos pos);
	
	void clearManager();
	
	void refreshManager();
	
	boolean onManagerRefresh(MANAGER manager);
}
