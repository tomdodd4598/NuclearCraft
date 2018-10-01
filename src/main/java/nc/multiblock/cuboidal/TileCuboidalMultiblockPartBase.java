package nc.multiblock.cuboidal;

import nc.multiblock.MultiblockBase;
import nc.multiblock.validation.IMultiblockValidator;

public abstract class TileCuboidalMultiblockPartBase<T extends MultiblockBase> extends CuboidalMultiblockTileBase<T> {
	
	protected final CuboidalPartPositionType positionType;
	
	public TileCuboidalMultiblockPartBase(Class<T> tClass, CuboidalPartPositionType positionType) {
		super(tClass);
		this.positionType = positionType;
	}
	
	public boolean isMultiblockAssembled() {
		if (getMultiblock() == null) return false;
		return getMultiblock().isAssembled();
	}
	
	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
	
	public CuboidalPartPositionType getPartPositionType() {
		return positionType;
	}
	
	@Override
	public boolean isGoodForFrame(IMultiblockValidator validator) {
		if (positionType.isGoodForFrame()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForSides(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForTop(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForBottom(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForInterior(IMultiblockValidator validator) {
		if (positionType.isGoodForInterior()) return true;
		setStandardLastError(validator);
		return false;
	}
}
