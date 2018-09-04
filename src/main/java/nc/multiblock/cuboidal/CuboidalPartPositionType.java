package nc.multiblock.cuboidal;

public enum CuboidalPartPositionType {
	WALL,
	FRAME,
	INTERIOR;
	
	public boolean isGoodForWalls() {
		return this == WALL;
	}
	
	public boolean isGoodForFrame() {
		return this == FRAME;
	}
	
	public boolean isGoodForInterior() {
		return this == INTERIOR;
	}
}
