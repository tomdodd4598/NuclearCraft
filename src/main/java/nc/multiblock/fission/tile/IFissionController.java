package nc.multiblock.fission.tile;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.tile.ILogicMultiblockController;

public interface IFissionController extends IFissionPart, ILogicMultiblockController<FissionReactor> {
	
	public void updateBlockState(boolean isActive);
	
	public void doMeltdown();
}
