package nc.tile.generator;

import nc.config.NCConfig;
import net.minecraft.util.EnumFacing;

public class TileSolarPanel extends TilePassiveGenerator {
	
	public static class Basic extends TileSolarPanel {

		public Basic() {
			super(NCConfig.solar_power[0]);
		}
	}
	
	public TileSolarPanel(int power) {
		super(power);
	}
	
	@Override
	public int getGenerated() {
		if (world.canBlockSeeSky(pos.offset(EnumFacing.UP))) return (int) (world.getSunBrightnessFactor(1.0F) * (float) power);
		return 0;
	}
}
