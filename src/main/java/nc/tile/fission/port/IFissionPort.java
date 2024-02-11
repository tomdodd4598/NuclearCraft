package nc.tile.fission.port;

import nc.multiblock.fission.*;
import nc.tile.fission.IFissionPart;
import nc.tile.multiblock.port.ITilePort;

public interface IFissionPort<PORT extends IFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends ITilePort<FissionReactor, FissionReactorLogic, IFissionPart, PORT, TARGET> {
	
}
