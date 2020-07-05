package nc.capability.radiation;

import static nc.config.NCConfig.*;

public interface IRadiation {
	
	public double getRadiationLevel();
	
	public void setRadiationLevel(double newRadiationLevel);
	
	public default boolean isRadiationNegligible() {
		return getRadiationLevel() < radiation_lowest_rate;
	}
	
	public default boolean isRadiationUndetectable() {
		return getRadiationLevel() < radiation_lowest_rate * max_player_rads;
	}
}
