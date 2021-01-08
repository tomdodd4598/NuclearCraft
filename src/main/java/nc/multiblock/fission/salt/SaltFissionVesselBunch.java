package nc.multiblock.fission.salt;

import nc.multiblock.fission.*;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import net.minecraft.util.EnumFacing;

public class SaltFissionVesselBunch extends FissionPartBunch<TileSaltFissionVessel> {
	
	public long sources = 0L, flux = 0L;
	public boolean primed = false, statsRetrieved = false;
	
	protected long openFaces = 0L;
	
	public SaltFissionVesselBunch(FissionReactor reactor) {
		super(reactor);
	}
	
	// TODO
	@Override
	protected void init() {
		if (!initialized) {
			for (TileSaltFissionVessel vessel : partMap.values()) {
				int i = 6;
				for (EnumFacing dir : EnumFacing.VALUES) {
					if (partMap.containsKey(vessel.getPos().offset(dir).toLong())) {
						i--;
					}
				}
				openFaces += i;
			}
		}
		initialized = true;
	}
	
	public long getBunchingFactor() {
		return 6L * partMap.size() / openFaces;
	}
	
	public long getSurfaceFactor() {
		return openFaces / 6L;
	}
	
	public long getCriticalityFactor(int baseProcessCriticality) {
		return getSurfaceFactor() * baseProcessCriticality;
	}
	
	public long getRawHeating() {
		long rawHeating = 0L;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			rawHeating += vessel.baseProcessHeat * vessel.heatMult;
		}
		return getBunchingFactor() * rawHeating;
	}
	
	public double getEffectiveHeating() {
		double effectiveHeating = 0D;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			effectiveHeating += vessel.baseProcessHeat * vessel.heatMult * vessel.baseProcessEfficiency * vessel.getSourceEfficiency() * vessel.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(vessel.baseProcessCriticality);
		}
		return getBunchingFactor() * effectiveHeating;
	}
	
	public long getHeatMultiplier() {
		long rawHeatMult = 0L;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			rawHeatMult += vessel.heatMult;
		}
		return getBunchingFactor() * rawHeatMult;
	}
	
	public double getFluxEfficiencyFactor(int baseProcessCriticality) {
		return (1D + Math.exp(-2D * baseProcessCriticality)) / (1D + Math.exp(2D * ((double) flux / (double) getSurfaceFactor() - 2D * baseProcessCriticality)));
	}
	
	public double getEfficiency() {
		double efficiency = 0D;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			efficiency += vessel.heatMult * vessel.baseProcessEfficiency * vessel.getSourceEfficiency() * vessel.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(vessel.baseProcessCriticality);
		}
		return getBunchingFactor() * efficiency;
	}
}
