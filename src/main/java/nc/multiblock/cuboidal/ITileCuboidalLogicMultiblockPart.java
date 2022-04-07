package nc.multiblock.cuboidal;

import nc.multiblock.*;
import nc.multiblock.tile.ITileLogicMultiblockPart;

public interface ITileCuboidalLogicMultiblockPart<MULTIBLOCK extends CuboidalMultiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileCuboidalLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> extends ITileCuboidalMultiblockPart<MULTIBLOCK, T>, ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
}
