package nc.tile.energy.generator;

import net.minecraft.util.EnumFacing;

public abstract class TileSolarPanel extends TileEnergyGeneratorContinuous {
	
	protected final int maxPower;
	
	public TileSolarPanel(int maxPower) {
		super(maxPower);
		this.maxPower = maxPower;
	}
	
	public int getGenerated() {
		if (world.canBlockSeeSky(pos.offset(EnumFacing.UP))) return (int) (world.getSunBrightnessFactor(1.0F) * (float) maxPower);
		return 0;
	}
}
