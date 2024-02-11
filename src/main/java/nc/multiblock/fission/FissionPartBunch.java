package nc.multiblock.fission;

import nc.multiblock.internal.PartBunch;
import nc.tile.fission.IFissionPart;

public abstract class FissionPartBunch<T extends IFissionPart> extends PartBunch<T, FissionReactor, IFissionPart> {
	
	public FissionPartBunch(FissionReactor reactor) {
		super(reactor);
	}
}
