package nc.multiblock.fission.solid;

import nc.multiblock.fission.*;

public class SolidFuelFissionCluster extends FissionCluster {
	
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L;
	public double effectiveHeating = 0D, meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, overcoolingEfficiencyFactor = 0D, undercoolingLifetimeFactor = 0D, totalHeatingSpeedMultiplier = 0D, meanHeatingSpeedMultiplier = 0D;
	
	public SolidFuelFissionCluster(FissionReactor reactor, int id) {
		super(reactor, id);
	}
	
	@Override
	public long getNetHeating() {
		return rawHeating - cooling;
	}
}
