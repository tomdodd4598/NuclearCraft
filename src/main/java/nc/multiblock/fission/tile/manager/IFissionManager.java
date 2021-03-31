package nc.multiblock.fission.tile.manager;

import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.tile.manager.ITileManager;
import nc.network.multiblock.FissionUpdatePacket;

public interface IFissionManager<MANAGER extends IFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends ITileManager<FissionReactor, FissionReactorLogic, IFissionPart, MANAGER, LISTENER, FissionUpdatePacket> {
	
}
