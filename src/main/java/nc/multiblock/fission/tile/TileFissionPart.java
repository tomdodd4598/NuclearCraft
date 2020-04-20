package nc.multiblock.fission.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPart;
import nc.multiblock.fission.FissionReactor;

public abstract class TileFissionPart extends TileCuboidalMultiblockPart<FissionReactor> implements IFissionPart {
	
	public TileFissionPart(CuboidalPartPositionType positionType) {
		super(FissionReactor.class, positionType);
	}
	
	@Override
	public FissionReactor createNewMultiblock() {
		return new FissionReactor(world);
	}
}
