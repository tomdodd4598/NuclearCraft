package nc.multiblock.tile;

import nc.multiblock.ILogicMultiblock;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.network.MultiblockUpdatePacket;

public interface ITileLogicMultiblockPart<MULTIBLOCK extends Multiblock<T, ? extends MultiblockUpdatePacket> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, ? extends MultiblockUpdatePacket>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> extends ITileMultiblockPart<MULTIBLOCK> {
	
	public default LOGIC getLogic() {
		return getMultiblock().getLogic();
	}
}
