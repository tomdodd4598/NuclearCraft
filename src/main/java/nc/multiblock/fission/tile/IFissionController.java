package nc.multiblock.fission.tile;

import nc.multiblock.IMultiblockPart;
import nc.multiblock.fission.FissionReactor;

public interface IFissionController extends IMultiblockPart<FissionReactor> {
	
	public void updateBlockState(boolean isActive);
	
	public void doMeltdown();
}
