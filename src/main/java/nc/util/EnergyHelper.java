package nc.util;

import nc.config.NCConfig;

public class EnergyHelper {
	
	public static int getEUSourceTier(double powerRF) {
		double euPerTick = (double) powerRF / (double) NCConfig.generator_rf_per_eu;
		return euPerTick < 32.0D ? 1 : (euPerTick < 128.0D ? 2 : (euPerTick < 512.0D ? 3 : 4));
	}
	
	public static int getEUSinkTier(double powerRF) {
		double euPerTick = (double) powerRF / (double) NCConfig.processor_rf_per_eu;
		return euPerTick < 32.0D ? 1 : (euPerTick < 128.0D ? 2 : (euPerTick < 512.0D ? 3 : 4));
	}
}
