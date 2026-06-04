package nc.util;

public class IntPair {
	
	public int x;
	public int y;
	
	public IntPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof IntPair other && (x == other.x && y == other.y);
	}
	
	@Override
	public int hashCode() {
		return x ^ y;
	}
}
