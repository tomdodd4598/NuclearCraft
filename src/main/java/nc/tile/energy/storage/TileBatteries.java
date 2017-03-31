package nc.tile.energy.storage;

import nc.config.NCConfig;

public abstract class TileBatteries {
	
	public static class VoltaicPileBasic extends TileBattery {

		public VoltaicPileBasic() {
			super(NCConfig.battery_capacity[0]);
		}
	}
	
	public static class LithiumIonBatteryBasic extends TileBattery {

		public LithiumIonBatteryBasic() {
			super(NCConfig.battery_capacity[1]);
		}
	}
}
