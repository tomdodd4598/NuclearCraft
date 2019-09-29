package nc.multiblock.fission.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import nc.multiblock.fission.FissionReactor;

public abstract class TileFissionPartBase extends TileCuboidalMultiblockPartBase<FissionReactor> {
	
	public TileFissionPartBase(CuboidalPartPositionType positionType) {
		super(FissionReactor.class, positionType);
	}
	
	@Override
	public FissionReactor createNewMultiblock() {
		return new FissionReactor(getWorld());
	}
}
