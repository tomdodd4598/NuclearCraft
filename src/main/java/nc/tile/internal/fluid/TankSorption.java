package nc.tile.internal.fluid;

public enum TankSorption {
	IN, OUT, BOTH, NON;
	
	public boolean canFill() {
		return this == IN || this == BOTH;
	}
		
	public boolean canDrain() {
		return this == OUT || this == BOTH;
	}
	
	public boolean canDistribute() {
		return this != NON;
	}
}
