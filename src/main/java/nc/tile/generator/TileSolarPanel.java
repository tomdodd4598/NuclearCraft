package nc.tile.generator;

import net.minecraft.util.EnumFacing;

public abstract class TileSolarPanel extends TileEnergyGeneratorContinuous {
	
	public TileSolarPanel(int power) {
		super(power);
	}
	
	public int getGenerated() {
		if (worldObj.canBlockSeeSky(pos.offset(EnumFacing.UP))) return (int) (worldObj.getSunBrightnessFactor(1.0F) * (float) power);
		return 0;
	}
}
