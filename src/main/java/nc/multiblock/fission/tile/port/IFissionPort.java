package nc.multiblock.fission.tile.port;

import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.tile.port.ITilePort;
import nc.network.multiblock.FissionUpdatePacket;

public interface IFissionPort<PORT extends IFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends ITilePort<FissionReactor, FissionReactorLogic, IFissionPart, PORT, TARGET, FissionUpdatePacket> {
	
}
