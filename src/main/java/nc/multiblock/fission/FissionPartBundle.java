package nc.multiblock.fission;

import nc.multiblock.PartBundle;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.network.FissionUpdatePacket;

public class FissionPartBundle<T extends IFissionPart> extends PartBundle<T, FissionReactor, IFissionPart, FissionUpdatePacket> {
	
	public FissionPartBundle(FissionReactor reactor, int id) {
		super(reactor, id);
	}
}
