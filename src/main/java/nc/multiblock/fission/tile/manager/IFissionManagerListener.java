package nc.multiblock.fission.tile.manager;

import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.tile.manager.ITileManagerListener;

public interface IFissionManagerListener<MANAGER extends IFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends ITileManagerListener<FissionReactor, FissionReactorLogic, IFissionPart, MANAGER, LISTENER> {
	
}
