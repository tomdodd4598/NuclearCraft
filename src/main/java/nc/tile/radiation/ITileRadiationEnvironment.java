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
	
	public double getRadiationContributionFraction();
	
	public double getCurrentChunkRadiationLevel();
	
	public void setCurrentChunkRadiationLevel(double level);
	
	public double getCurrentChunkRadiationBuffer();
	
	public void setCurrentChunkRadiationBuffer(double buffer);
}
