package nc.tile.radiation;

import nc.radiation.environment.RadiationEnvironmentInfo;

public interface IRadiationEnvironmentHandler {
	
	public void checkRadiationEnvironmentInfo();
	
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info);
	
	public double getChunkBufferContribution();
}
