package nc.tile.multiblock.port;

import nc.multiblock.*;
import nc.tile.multiblock.ITileLogicMultiblockPart;
import net.minecraft.util.math.BlockPos;

public interface ITilePortTarget<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	BlockPos getMasterPortPos();
	
	void setMasterPortPos(BlockPos pos);
	
	void clearMasterPort();
	
	void refreshMasterPort();
	
	boolean onPortRefresh();
}
