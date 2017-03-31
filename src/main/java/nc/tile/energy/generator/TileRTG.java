package nc.tile.energy.generator;

public abstract class TileRTG extends TileEnergyGeneratorContinuous {
	
	protected final int power;
	
	public TileRTG(int power) {
		super(power);
		this.power = power;
	}
	
	public int getGenerated() {
		return power;
	}
}
