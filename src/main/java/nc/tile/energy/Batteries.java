package nc.tile.energy;

import nc.config.NCConfig;

public class Batteries {
	
	public static class VoltaicPileBasic extends TileBattery {

		public VoltaicPileBasic() {
			super(NCConfig.battery_capacity[0]);
		}
		
		@Override
		public int getSourceTier() {
			return 2;
		}
		
		@Override
		public int getSinkTier() {
			return 2;
		}
	}
	
	public static class LithiumIonBatteryBasic extends TileBattery {

		public LithiumIonBatteryBasic() {
			super(NCConfig.battery_capacity[1]);
		}
		
		@Override
		public int getSourceTier() {
			return 4;
		}
		
		@Override
		public int getSinkTier() {
			return 4;
		}
	}
}
