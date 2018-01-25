package nc.tile.generator;

public abstract class TileRTGAbstract extends TilePassiveGenerator {
	
	public TileRTGAbstract(int power) {
		super(power);
	}
	
	@Override
	public int getGenerated() {
		return power;
	}
}
