package nc.multiblock.fission;

import nc.multiblock.PartBunch;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.network.FissionUpdatePacket;

public abstract class FissionPartBunch<T extends IFissionPart> extends PartBunch<T, FissionReactor, IFissionPart, FissionUpdatePacket> {
	
	public FissionPartBunch(FissionReactor reactor) {
		super(reactor);
	}
}
