package nc.multiblock.fission;

import it.unimi.dsi.fastutil.longs.*;
import nc.tile.fission.IFissionFuelBunchComponent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FissionFuelBunch {
	
	public boolean initialized = false;
	
	public final Long2ObjectMap<IFissionFuelBunchComponent> fuelComponentMap = new Long2ObjectOpenHashMap<>();
	
	public long sources = 0L, flux = 0L;
	public boolean primed = false, statsRetrieved = false;
	
	public long openFaces = 0L;
	
	public void init() {
		if (!initialized) {
			initialized = true;
			
			for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
				flux += fuelComponent.getIndividualFlux();
				BlockPos pos = fuelComponent.getTilePos();
				for (EnumFacing dir : EnumFacing.VALUES) {
					if (!fuelComponentMap.containsKey(pos.offset(dir).toLong())) {
						++openFaces;
					}
				}
			}
		}
	}
	
	protected long getSurfaceFactor() {
		return openFaces / 6L;
	}
	
	protected long getBunchingFactor() {
		return 6L * fuelComponentMap.size() / openFaces;
	}
	
	public void tryPriming(boolean fromSource, boolean simulate) {
		if (!primed) {
			if (fromSource) {
				++sources;
				if (sources >= getSurfaceFactor()) {
					primed = true;
				}
			}
			else {
				primed = true;
			}
		}
		
		if (primed) {
			sources = 0L;
		}
	}
	
	public long getCriticalityFactor(long criticalityFactor) {
		return getSurfaceFactor() * criticalityFactor;
	}
	
	public long getRawHeating(boolean simulate) {
		long rawHeating = 0L;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (fuelComponent.isRunning(simulate)) {
				rawHeating += fuelComponent.getBaseProcessHeat() * fuelComponent.getIndividualHeatMultiplier(simulate);
			}
		}
		return getBunchingFactor() * rawHeating;
	}
	
	public long getRawHeatingIgnoreCoolingPenalty(boolean simulate) {
		long rawHeatingIgnoreCoolingPenalty = 0L;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (!fuelComponent.isRunning(simulate)) {
				rawHeatingIgnoreCoolingPenalty += fuelComponent.getDecayHeating();
			}
		}
		return getBunchingFactor() * rawHeatingIgnoreCoolingPenalty;
	}
	
	public double getEffectiveHeating(boolean simulate) {
		double effectiveHeating = 0D;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (fuelComponent.isRunning(simulate)) {
				effectiveHeating += fuelComponent.getBaseProcessHeat() * fuelComponent.getIndividualHeatMultiplier(simulate) * fuelComponent.getBaseProcessEfficiency() * fuelComponent.getSourceEfficiency() * fuelComponent.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(fuelComponent.getFloatingPointCriticality());
			}
		}
		return getBunchingFactor() * effectiveHeating;
	}
	
	public double getEffectiveHeatingIgnoreCoolingPenalty(boolean simulate) {
		double effectiveHeatingIgnoreCoolingPenalty = 0D;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (!fuelComponent.isRunning(simulate)) {
				effectiveHeatingIgnoreCoolingPenalty += fuelComponent.getFloatingPointDecayHeating();
			}
		}
		return getBunchingFactor() * effectiveHeatingIgnoreCoolingPenalty;
	}
	
	public long getHeatMultiplier(boolean simulate) {
		long rawHeatMult = 0L;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (fuelComponent.isRunning(simulate)) {
				rawHeatMult += fuelComponent.getIndividualHeatMultiplier(simulate);
			}
		}
		return getBunchingFactor() * rawHeatMult;
	}
	
	public double getFluxEfficiencyFactor(double floatingPointCriticalityFactor) {
		return (1D + Math.exp(-2D * floatingPointCriticalityFactor)) / (1D + Math.exp(2D * ((double) flux / (double) getSurfaceFactor() - 2D * floatingPointCriticalityFactor)));
	}
	
	public double getEfficiency(boolean simulate) {
		double efficiency = 0D;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (fuelComponent.isRunning(simulate)) {
				efficiency += fuelComponent.getIndividualHeatMultiplier(simulate) * fuelComponent.getBaseProcessEfficiency() * fuelComponent.getSourceEfficiency() * fuelComponent.getModeratorEfficiencyFactor() * getFluxEfficiencyFactor(fuelComponent.getFloatingPointCriticality());
			}
		}
		return getBunchingFactor() * efficiency;
	}
	
	public double getEfficiencyIgnoreCoolingPenalty(boolean simulate) {
		double efficiencyIgnoreCoolingPenalty = 0D;
		for (IFissionFuelBunchComponent fuelComponent : fuelComponentMap.values()) {
			if (!fuelComponent.isRunning(simulate)) {
				++efficiencyIgnoreCoolingPenalty;
			}
		}
		return getBunchingFactor() * efficiencyIgnoreCoolingPenalty;
	}
}
