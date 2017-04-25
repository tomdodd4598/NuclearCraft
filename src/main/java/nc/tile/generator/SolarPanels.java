package nc.tile.generator;

import nc.config.NCConfig;

public class SolarPanels {
	
	public static class SolarPanelBasic extends TileSolarPanel {

		public SolarPanelBasic() {
			super(NCConfig.solar_power[0]);
		}
	}
}
