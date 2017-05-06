package nc.tile.generator;

public abstract class TileRTG extends TilePassiveGenerator {
	
	public TileRTG(int power) {
		super(power);
	}
	
	public int getGenerated() {
		return power;
	}
}
