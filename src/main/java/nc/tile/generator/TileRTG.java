package nc.tile.generator;

import nc.config.NCConfig;

public class TileRTG {
	
	public static class Uranium extends TileRTGAbstract {

		public Uranium() {
			super(NCConfig.rtg_power[0]);
		}
	}
	
	public static class Plutonium extends TileRTGAbstract {

		public Plutonium() {
			super(NCConfig.rtg_power[1]);
		}
	}
	
	public static class Americium extends TileRTGAbstract {

		public Americium() {
			super(NCConfig.rtg_power[2]);
		}
	}
	
	public static class Californium extends TileRTGAbstract {

		public Californium() {
			super(NCConfig.rtg_power[3]);
		}
	}
}
