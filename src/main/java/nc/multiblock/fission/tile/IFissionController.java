package nc.multiblock.fission.tile;

import nc.multiblock.ILogicMultiblockController;
import nc.multiblock.fission.FissionReactor;

public interface IFissionController extends IFissionPart, ILogicMultiblockController<FissionReactor> {
	
	public void updateBlockState(boolean isActive);
	
	public void doMeltdown();
}
