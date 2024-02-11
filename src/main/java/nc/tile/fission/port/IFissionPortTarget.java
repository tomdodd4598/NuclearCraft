package nc.tile.fission.port;

import nc.multiblock.fission.*;
import nc.tile.fission.IFissionPart;
import nc.tile.multiblock.port.ITilePortTarget;

public interface IFissionPortTarget<PORT extends IFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends ITilePortTarget<FissionReactor, FissionReactorLogic, IFissionPart, PORT, TARGET> {
	
}
