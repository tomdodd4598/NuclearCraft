package nc.multiblock.tile;

import nc.multiblock.*;

public interface ITileLogicMultiblockPart<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> extends ITileMultiblockPart<MULTIBLOCK, T> {
	
	public default LOGIC getLogic() {
		MULTIBLOCK multiblock = getMultiblock();
		return multiblock == null ? null : multiblock.getLogic();
	}
}
