package nc.tile.generator;

public abstract class TileRTG extends TilePassiveGenerator {
	
	public TileRTG(int power) {
		super(power);
	}
	
	@Override
	public int getGenerated() {
		return power;
	}
}
