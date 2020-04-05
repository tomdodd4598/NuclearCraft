package nc.multiblock.tile;

import nc.multiblock.Multiblock;

public interface ILogicMultiblockController<MULTIBLOCK extends Multiblock> extends IMultiblockController<MULTIBLOCK> {
	
	public String getLogicID();
}
