package nc.util;

import nc.ModCheck;

import static nc.config.NCConfig.rf_per_eu;

public class EnergyHelper {
	
	public static int getEUTier(double powerRF) {
		double euPerTick = powerRF / rf_per_eu;
		if (euPerTick <= 32D) {
			return 1;
		}
		int maxTier = ModCheck.gregtechLoaded() ? 10 : 4;
		return NCMath.toInt(Math.min(Math.ceil((Math.log(euPerTick) / Math.log(2D) - 3D) / 2D), maxTier));
	}
	
	public static long getMaxEUFromTier(int tier) {
		return (long) Math.pow(2, 2 * tier + 3);
	}
}
