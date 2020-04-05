package nc.multiblock.fission.tile.manager;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.FissionReactorLogic;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.tile.manager.ITileManager;

public interface IFissionManager<MANAGER extends IFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends ITileManager<FissionReactor, FissionReactorLogic, IFissionPart, MANAGER, LISTENER> {

}
