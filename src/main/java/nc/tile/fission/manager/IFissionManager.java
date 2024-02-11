package nc.tile.fission.manager;

import nc.multiblock.fission.*;
import nc.tile.fission.IFissionPart;
import nc.tile.multiblock.manager.ITileManager;

public interface IFissionManager<MANAGER extends IFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends ITileManager<FissionReactor, FissionReactorLogic, IFissionPart, MANAGER, LISTENER> {
	
}
