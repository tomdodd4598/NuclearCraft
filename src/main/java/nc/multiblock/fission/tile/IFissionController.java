package nc.multiblock.fission.tile;

import java.util.Iterator;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.tile.ILogicMultiblockController;

public interface IFissionController extends IFissionPart, ILogicMultiblockController<FissionReactor> {
	
	public void doMeltdown(Iterator<IFissionController> controllerIterator);
}
