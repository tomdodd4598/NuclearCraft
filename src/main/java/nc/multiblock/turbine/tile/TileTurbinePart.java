package nc.multiblock.turbine.tile;

import nc.multiblock.Multiblock;
import nc.multiblock.cuboidal.*;
import nc.multiblock.turbine.Turbine;

public abstract class TileTurbinePart extends TileCuboidalMultiblockPart<Turbine> implements ITurbinePart {
	
	public TileTurbinePart(CuboidalPartPositionType positionType) {
		super(Turbine.class, positionType);
	}
	
	@Override
	public Turbine createNewMultiblock() {
		return new Turbine(world);
	}
	
	public boolean isTransparent() {
		return false;
	}
	
	@Override
	public boolean isGoodForFrame(Multiblock multiblock) {
		if (getPartPositionType().isGoodForFrame()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForSides(Multiblock multiblock) {
		if (getPartPositionType().isGoodForWall()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForTop(Multiblock multiblock) {
		if (getPartPositionType().isGoodForWall()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForBottom(Multiblock multiblock) {
		if (getPartPositionType().isGoodForWall()) {
			if (isTransparent() && getMultiblock() != null) {
				getMultiblock().shouldRenderRotor = true;
			}
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
}
