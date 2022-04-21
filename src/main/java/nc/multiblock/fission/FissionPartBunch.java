package nc.multiblock.fission;

import nc.multiblock.PartBunch;
import nc.multiblock.fission.tile.IFissionPart;

public abstract class FissionPartBunch<T extends IFissionPart> extends PartBunch<T, FissionReactor, IFissionPart> {
	
	public FissionPartBunch(FissionReactor reactor) {
		super(reactor);
	}
}
