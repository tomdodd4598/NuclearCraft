package nc.tile.energy.generator;

import nc.config.NCConfig;

public abstract class TileSolarPanels {
	
	public static class SolarPanelBasic extends TileSolarPanel {

		public SolarPanelBasic() {
			super(NCConfig.solar_power[0]);
		}
	}
}
