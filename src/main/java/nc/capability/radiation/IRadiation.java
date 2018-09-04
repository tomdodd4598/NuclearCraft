package nc.capability.radiation;

import nc.config.NCConfig;

public interface IRadiation {
	
	public double getRadiationLevel();
	
	public void setRadiationLevel(double newRadiationLevel);
	
	public default boolean isRadiationNegligible() {
		return getRadiationLevel() < NCConfig.radiation_lowest_rate;
	}
	
	public default boolean isRadiationUndetectable() {
		return getRadiationLevel() < NCConfig.radiation_lowest_rate*NCConfig.max_player_rads;
	}
}
