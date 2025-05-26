package nc.multiblock.fission;

import it.unimi.dsi.fastutil.longs.*;
import nc.tile.fission.TileSaltFissionVessel;
import net.minecraft.util.EnumFacing;

public class SaltFissionVesselBunch {
	
	public boolean initialized = false;
	
	public final Long2ObjectMap<TileSaltFissionVessel> vesselMap = new Long2ObjectOpenHashMap<>();
	
	public long sources = 0L, flux = 0L;
	public boolean primed = false, statsRetrieved = false;
	
	public long openFaces = 0L;
	
	// TODO
	public void init() {
		if (!initialized) {
			for (TileSaltFissionVessel vessel : vesselMap.values()) {
				int i = 6;
				for (EnumFacing dir : EnumFacing.VALUES) {
					if (vesselMap.containsKey(vessel.getPos().offset(dir).toLong())) {
						--i;
					}
				}
				openFaces += i;
			}
		}
		initialized = true;
	}
	
	public long getBunchingFactor() {
		return 6L * vesselMap.size() / openFaces;
	}
	
	public long getSurfaceFactor() {
		return openFaces / 6L;
	}
	
	public long getCriticalityFactor(long criticalityFactor) {
		return getSurfaceFactor() * criticalityFactor;
	}
	
	public long getRawHeating() {
		long rawHeating = 0L;
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
			if (vessel.isProcessing) {
				rawHeating += (long) vessel.baseProcessHeat * vessel.heatMult;
			}
		}
		return getBunchingFactor() * rawHeating;
	}
	
	public long getRawHeatingIgnoreCoolingPenalty() {
		long rawHeatingIgnoreCoolingPenalty = 0L;
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
			if (!vessel.isProcessing) {
				rawHeatingIgnoreCoolingPenalty += vessel.getDecayHeating();
			}
		}
		return getBunchingFactor() * rawHeatingIgnoreCoolingPenalty;
	}
	
	public double getEffectiveHeating() {
		double effectiveHeating = 0D;
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
			if (vessel.isProcessing) {
				effectiveHeating += vessel.baseProcessHeat * vessel.heatMult * vessel.baseProcessEfficiency * vessel.getSourceEfficiency() * vessel.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(vessel.getFloatingPointCriticality());
			}
		}
		return getBunchingFactor() * effectiveHeating;
	}
	
	public double getEffectiveHeatingIgnoreCoolingPenalty() {
		double effectiveHeatingIgnoreCoolingPenalty = 0D;
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
			if (!vessel.isProcessing) {
				effectiveHeatingIgnoreCoolingPenalty += vessel.getFloatingPointDecayHeating();
			}
		}
		return getBunchingFactor() * effectiveHeatingIgnoreCoolingPenalty;
	}
	
	public long getHeatMultiplier() {
		long rawHeatMult = 0L;
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
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
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
			if (vessel.isProcessing) {
				efficiency += vessel.heatMult * vessel.baseProcessEfficiency * vessel.getSourceEfficiency() * vessel.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(vessel.getFloatingPointCriticality());
			}
		}
		return getBunchingFactor() * efficiency;
	}
	
	public double getEfficiencyIgnoreCoolingPenalty() {
		double efficiencyIgnoreCoolingPenalty = 0D;
		for (TileSaltFissionVessel vessel : vesselMap.values()) {
			if (!vessel.isProcessing) {
				++efficiencyIgnoreCoolingPenalty;
			}
		}
		return getBunchingFactor() * efficiencyIgnoreCoolingPenalty;
	}
}
