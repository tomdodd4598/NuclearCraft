package nc.tile.generator;

import nc.config.NCConfig;

public class TileSolarPanel {
	
	public static class Basic extends TileSolarPanelAbstract {

		public Basic() {
			super(NCConfig.solar_power[0]);
		}
	}
}
