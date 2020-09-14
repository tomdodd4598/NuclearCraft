package nc.capability.radiation;

import static nc.config.NCConfig.radiation_lowest_rate;

public interface IRadiation {
	
	public double getRadiationLevel();
	
	public void setRadiationLevel(double newRadiationLevel);
	
	public default boolean isRadiationNegligible() {
		return getRadiationLevel() < radiation_lowest_rate;
	}
}
