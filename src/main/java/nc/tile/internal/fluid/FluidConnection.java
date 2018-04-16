package nc.tile.internal.fluid;

public enum FluidConnection {
	IN, OUT, BOTH, NON;
	
	public boolean canFill() {
		return this == IN || this == BOTH;
	}
	
	public boolean canDrain() {
		return this == OUT || this == BOTH;
	}
	
	public boolean canConnect() {
		return this != NON;
	}
}
