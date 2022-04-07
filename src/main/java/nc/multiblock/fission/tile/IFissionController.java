package nc.multiblock.fission.tile;

import java.util.Iterator;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.tile.ILogicMultiblockController;
import nc.network.multiblock.FissionUpdatePacket;

public interface IFissionController<CONTROLLER extends IFissionController<CONTROLLER>> extends IFissionPart, ILogicMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, CONTROLLER> {
	
	public void doMeltdown(Iterator<IFissionController<?>> controllerIterator);
}
