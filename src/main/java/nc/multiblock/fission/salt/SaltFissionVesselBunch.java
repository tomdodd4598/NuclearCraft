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
						--i;
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
	
	public long getCriticalityFactor(long criticalityFactor) {
		return getSurfaceFactor() * criticalityFactor;
	}
	
	public long getRawHeating() {
		long rawHeating = 0L;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (vessel.isProcessing) {
				rawHeating += vessel.baseProcessHeat * vessel.heatMult;
			}
		}
		return getBunchingFactor() * rawHeating;
	}
	
	public long getRawHeatingIgnoreCoolingPenalty() {
		long rawHeatingIgnoreCoolingPenalty = 0L;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (!vessel.isProcessing) {
				rawHeatingIgnoreCoolingPenalty += vessel.getDecayHeating();
			}
		}
		return getBunchingFactor() * rawHeatingIgnoreCoolingPenalty;
	}
	
	public double getEffectiveHeating() {
		double effectiveHeating = 0D;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (vessel.isProcessing) {
				effectiveHeating += vessel.baseProcessHeat * vessel.heatMult * vessel.baseProcessEfficiency * vessel.getSourceEfficiency() * vessel.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(vessel.getFloatingPointCriticality());
			}
		}
		return getBunchingFactor() * effectiveHeating;
	}
	
	public double getEffectiveHeatingIgnoreCoolingPenalty() {
		double effectiveHeatingIgnoreCoolingPenalty = 0D;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (!vessel.isProcessing) {
				effectiveHeatingIgnoreCoolingPenalty += vessel.getFloatingPointDecayHeating();
			}
		}
		return getBunchingFactor() * effectiveHeatingIgnoreCoolingPenalty;
	}
	
	public long getHeatMultiplier() {
		long rawHeatMult = 0L;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (vessel.isProcessing) {
				rawHeatMult += vessel.heatMult;
			}
		}
		return getBunchingFactor() * rawHeatMult;
	}
	
	public double getFluxEfficiencyFactor(double floatingPointCriticalityFactor) {
		return (1D + Math.exp(-2D * floatingPointCriticalityFactor)) / (1D + Math.exp(2D * ((double) flux / (double) getSurfaceFactor() - 2D * floatingPointCriticalityFactor)));
	}
	
	public double getEfficiency() {
		double efficiency = 0D;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (vessel.isProcessing) {
				efficiency += vessel.heatMult * vessel.baseProcessEfficiency * vessel.getSourceEfficiency() * vessel.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(vessel.getFloatingPointCriticality());
			}
		}
		return getBunchingFactor() * efficiency;
	}
	
	public double getEfficiencyIgnoreCoolingPenalty() {
		double efficiencyIgnoreCoolingPenalty = 0D;
		for (TileSaltFissionVessel vessel : partMap.values()) {
			if (!vessel.isProcessing) {
				++efficiencyIgnoreCoolingPenalty;
			}
		}
		return getBunchingFactor() * efficiencyIgnoreCoolingPenalty;
	}
}
