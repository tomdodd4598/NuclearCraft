package nc.tile.generator;

public abstract class TileRTG extends TileEnergyGeneratorContinuous {
	
	public TileRTG(int power) {
		super(power);
	}
	
	public int getGenerated() {
		return power;
	}
}
