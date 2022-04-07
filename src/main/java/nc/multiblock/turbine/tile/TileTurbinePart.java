package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.*;
import nc.multiblock.turbine.Turbine;

public abstract class TileTurbinePart extends TileCuboidalMultiblockPart<Turbine, ITurbinePart> implements ITurbinePart {
	
	public TileTurbinePart(CuboidalPartPositionType positionType) {
		super(Turbine.class, ITurbinePart.class, positionType);
	}
	
	@Override
	public Turbine createNewMultiblock() {
		return new Turbine(world);
	}
	
	public boolean isTransparent() {
		return false;
	}
	
	@Override
	public boolean isGoodForFrame(Turbine multiblock) {
		if (getPartPositionType().isGoodForFrame()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldSpecialRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForSides(Turbine multiblock) {
		if (getPartPositionType().isGoodForWall()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldSpecialRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForTop(Turbine multiblock) {
		if (getPartPositionType().isGoodForWall()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldSpecialRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForBottom(Turbine multiblock) {
		if (getPartPositionType().isGoodForWall()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldSpecialRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
}
