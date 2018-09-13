package nc.tile.generator;

import nc.config.NCConfig;
import nc.radiation.RadSources;

public class TileRTG extends TilePassiveGenerator {
	
	public static class Uranium extends TileRTG {

		public Uranium() {
			super(NCConfig.rtg_power[0], RadSources.URANIUM_238/4D);
		}
	}
	
	public static class Plutonium extends TileRTG {

		public Plutonium() {
			super(NCConfig.rtg_power[1], RadSources.PLUTONIUM_238/4D);
		}
	}
	
	public static class Americium extends TileRTG {

		public Americium() {
			super(NCConfig.rtg_power[2], RadSources.AMERICIUM_241/4D);
		}
	}
	
	public static class Californium extends TileRTG {

		public Californium() {
			super(NCConfig.rtg_power[3], RadSources.CALIFORNIUM_250/4D);
		}
	}
	
	public TileRTG(int power, double radiation) {
		super(power);
		getRadiationSource().setRadiationLevel(radiation);
	}
	
	@Override
	public int getGenerated() {
		return power;
	}
	
	@Override
	public boolean shouldSaveRadiation() {
		return false;
	}
}
