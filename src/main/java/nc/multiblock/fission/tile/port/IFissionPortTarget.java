package nc.multiblock.fission.tile.port;

import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.tile.port.ITilePortTarget;
import nc.network.multiblock.FissionUpdatePacket;

public interface IFissionPortTarget<PORT extends IFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends ITilePortTarget<FissionReactor, FissionReactorLogic, IFissionPart, PORT, TARGET, FissionUpdatePacket> {
	
}
