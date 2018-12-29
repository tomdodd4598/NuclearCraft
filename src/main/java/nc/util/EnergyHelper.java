package nc.util;

import nc.config.NCConfig;

public class EnergyHelper {
	
	public static int getEUTier(double powerRF) {
		double euPerTick = powerRF / (double) NCConfig.rf_per_eu;
		for (int i = 1; i < 10; i++) if (euPerTick <= Math.pow(2, 2*i + 3)) return i;
		return 10;
	}
	
	public static int getMaxEUFromTier(int tier) {
		return (int) Math.pow(2, 2*tier + 3);
	}
}
