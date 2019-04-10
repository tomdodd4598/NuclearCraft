package nc.multiblock.cuboidal;

public enum CuboidalPartPositionType {
	WALL,
	FRAME,
	INTERIOR,
	ALL;
	
	public boolean isGoodForWalls() {
		return this == WALL || this == ALL;
	}
	
	public boolean isGoodForFrame() {
		return this == FRAME || this == ALL;
	}
	
	public boolean isGoodForInterior() {
		return this == INTERIOR || this == ALL;
	}
}
