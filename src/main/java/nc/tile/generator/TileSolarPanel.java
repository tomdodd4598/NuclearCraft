package nc.tile.generator;

import net.minecraft.util.EnumFacing;

public abstract class TileSolarPanel extends TileEnergyGeneratorContinuous {
	
	public TileSolarPanel(int power) {
		super(power);
	}
	
	public int getGenerated() {
		if (world.canBlockSeeSky(pos.offset(EnumFacing.UP))) return (int) (world.getSunBrightnessFactor(1.0F) * (float) power);
		return 0;
	}
}
