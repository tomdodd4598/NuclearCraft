package nc.tile.generator;

import nc.config.NCConfig;

public class TileRTG extends TilePassiveGenerator {
	
	public static class Uranium extends TileRTG {

		public Uranium() {
			super(NCConfig.rtg_power[0]);
		}
	}
	
	public static class Plutonium extends TileRTG {

		public Plutonium() {
			super(NCConfig.rtg_power[1]);
		}
	}
	
	public static class Americium extends TileRTG {

		public Americium() {
			super(NCConfig.rtg_power[2]);
		}
	}
	
	public static class Californium extends TileRTG {

		public Californium() {
			super(NCConfig.rtg_power[3]);
		}
	}
	
	public TileRTG(int power) {
		super(power);
	}
	
	@Override
	public int getGenerated() {
		return power;
	}
}
