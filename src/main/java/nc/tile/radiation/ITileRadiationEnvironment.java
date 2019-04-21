package nc.tile.radiation;

import nc.radiation.environment.RadiationEnvironmentInfo;
import nc.tile.ITile;

public interface ITileRadiationEnvironment extends ITile {
	
	public void checkRadiationEnvironmentInfo();
	
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info);
	
	public double getContributionFraction();
	
	public double getCurrentChunkBuffer();
	
	public void setCurrentChunkBuffer(double buffer);
}
