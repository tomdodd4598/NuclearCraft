package nc.tile.energy.generator;

import nc.config.NCConfig;

public abstract class TileRTGs {
	
	public static class UraniumRTG extends TileRTG {

		public UraniumRTG() {
			super(NCConfig.rtg_power[0]);
		}
	}
	
	public static class PlutoniumRTG extends TileRTG {

		public PlutoniumRTG() {
			super(NCConfig.rtg_power[1]);
		}
	}
	
	public static class AmericiumRTG extends TileRTG {

		public AmericiumRTG() {
			super(NCConfig.rtg_power[2]);
		}
	}
	
	public static class CaliforniumRTG extends TileRTG {

		public CaliforniumRTG() {
			super(NCConfig.rtg_power[3]);
		}
	}
}
