package nc.tile.fission.manager;

import nc.multiblock.fission.*;
import nc.tile.fission.IFissionPart;
import nc.tile.multiblock.manager.ITileManagerListener;

public interface IFissionManagerListener<MANAGER extends IFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends ITileManagerListener<FissionReactor, FissionReactorLogic, IFissionPart, MANAGER, LISTENER> {
	
}
