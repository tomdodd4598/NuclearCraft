package nc.multiblock.fission.tile;

import nc.multiblock.ILogicMultiblockController;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.FissionReactorLogic;

public interface IFissionController extends IFissionPart, ILogicMultiblockController<FissionReactor, FissionReactorLogic> {
	
	public void updateBlockState(boolean isActive);
	
	public void doMeltdown();
}
