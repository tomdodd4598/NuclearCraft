package nc.tile.fission;

import java.util.Iterator;

import nc.multiblock.fission.FissionReactor;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.multiblock.ILogicMultiblockController;

public interface IFissionController<CONTROLLER extends IFissionController<CONTROLLER>> extends IFissionPart, ILogicMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, CONTROLLER> {
	
	void doMeltdown(Iterator<IFissionController<?>> controllerIterator);
}
