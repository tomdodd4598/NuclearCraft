package nc.tile.radiation;

import nc.radiation.environment.RadiationEnvironmentInfo;
import nc.tile.ITile;
import nc.util.FourPos;

public interface ITileRadiationEnvironment extends ITile {
	
	public default FourPos getFourPos() {
		return new FourPos(getTilePos(), getTileWorld().provider.getDimension());
	}
	
	public void checkRadiationEnvironmentInfo();
	
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info);
	
	public double getContributionFraction();
	
	public double getCurrentChunkBuffer();
	
	public void setCurrentChunkBuffer(double buffer);
}
