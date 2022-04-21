package nc.multiblock.cuboidal;

public enum CuboidalPartPositionType {
	
	WALL,
	FRAME,
	INTERIOR,
	EXTERIOR,
	ALL;
	
	public boolean isGoodForWall() {
		return this == WALL || this == EXTERIOR || this == ALL;
	}
	
	public boolean isGoodForFrame() {
		return this == FRAME || this == EXTERIOR || this == ALL;
	}
	
	public boolean isGoodForInterior() {
		return this == INTERIOR || this == ALL;
	}
}
