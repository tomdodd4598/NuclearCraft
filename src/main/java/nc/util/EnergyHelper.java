package nc.util;

import nc.config.NCConfig;

public class EnergyHelper {
	
	public static int getEUTier(double powerRF) {
		double euPerTick = (double) powerRF / (double) NCConfig.rf_per_eu;
		return euPerTick < 32.0D ? 1 : (euPerTick < 128.0D ? 2 : (euPerTick < 512.0D ? 3 : 4));
	}
	
	public static int getMaxEUFromTier(int tier) {
		return (int) Math.pow(2, 2*tier + 3);
	}
}
