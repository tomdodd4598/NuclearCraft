package nc.multiblock.fission;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nc.config.NCConfig;
import nc.multiblock.fission.salt.MoltenSaltFissionLogic;
import nc.multiblock.fission.solid.SolidFuelFissionLogic;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionCoolingComponent;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.tile.internal.heat.HeatBuffer;

public class FissionCluster {
	
	protected final FissionReactor reactor;
	protected int id;
	
	protected final Long2ObjectMap<IFissionComponent> componentMap = new Long2ObjectOpenHashMap<>();
	
	public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);
	
	public boolean connectedToWall = false;
	public int componentCount = 0, fuelComponentCount = 0;
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L;
	public double effectiveHeating = 0D, meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, overcoolingEfficiencyFactor = 0D, undercoolingLifetimeFactor = 0D;
	
	public FissionCluster(FissionReactor reactor, int id) {
		this.reactor = reactor;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		id = newId;
	}
	
	public Long2ObjectMap<IFissionComponent> getComponentMap() {
		return componentMap;
	}
	
	public void refreshClusterStats() {
		componentCount = fuelComponentCount = 0;
		cooling = rawHeating = totalHeatMult = 0L;
		effectiveHeating = meanHeatMult = totalEfficiency = meanEfficiency = overcoolingEfficiencyFactor = undercoolingLifetimeFactor = 0D;
		
		heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT*componentMap.size());
		
		if (reactor.logic instanceof SolidFuelFissionLogic || reactor.logic instanceof MoltenSaltFissionLogic) {
			for (IFissionComponent component : componentMap.values()) {
				if (component.isFunctional()) {
					componentCount++;
					if (component instanceof IFissionFuelComponent) {
						IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
						fuelComponentCount++;
						rawHeating += fuelComponent.getRawHeating();
						effectiveHeating += fuelComponent.getEffectiveHeating();
						totalHeatMult += fuelComponent.getHeatMultiplier();
						totalEfficiency += fuelComponent.getEfficiency();
					}
					if (component instanceof IFissionCoolingComponent) {
						cooling += ((IFissionCoolingComponent)component).getCooling();
					}
				}
			}
		}
		
		overcoolingEfficiencyFactor = cooling == 0L ? 1D : Math.min(1D, (double)(rawHeating + NCConfig.fission_cooling_efficiency_leniency)/(double)cooling);
		undercoolingLifetimeFactor = rawHeating == 0L ? 1D : Math.min(1D, (double)(cooling + NCConfig.fission_cooling_efficiency_leniency)/(double)rawHeating);
		effectiveHeating *= overcoolingEfficiencyFactor;
		totalEfficiency *= overcoolingEfficiencyFactor;
		meanHeatMult = fuelComponentCount == 0 ? 0D : (double)totalHeatMult/(double)fuelComponentCount;
		meanEfficiency = fuelComponentCount == 0 ? 0D : totalEfficiency/fuelComponentCount;
		
		if (reactor.logic instanceof SolidFuelFissionLogic || reactor.logic instanceof MoltenSaltFissionLogic) {
			for (IFissionComponent component : componentMap.values()) {
				if (component instanceof IFissionFuelComponent) {
					IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
					fuelComponent.setUndercoolingLifetimeFactor(undercoolingLifetimeFactor);
				}
			}
		}
	}
	
	public long getNetHeating() {
		return rawHeating - cooling;
	}
	
	public void distributeHeatToComponents() {
		if (componentMap.isEmpty()) return;
		long distributedHeat = (long)Math.ceil((double)heatBuffer.getHeatStored()/(double)componentMap.size());
		for (IFissionComponent component : componentMap.values()) {
			component.setHeatStored(heatBuffer.removeHeat(distributedHeat, false));
			if (heatBuffer.getHeatStored() == 0L) return;
		}
		heatBuffer.setHeatStored(0L);
	}
	
	public void recoverHeatFromComponents() {
		heatBuffer.setHeatStored(0L);
		for (IFissionComponent component : componentMap.values()) {
			heatBuffer.addHeat(component.getHeatStored(), false);
			component.setHeatStored(0L);
		}
	}
	
	public void doMeltdown() {
		if (reactor.logic instanceof SolidFuelFissionLogic || reactor.logic instanceof MoltenSaltFissionLogic) {
			Iterator<IFissionComponent> componentIterator = componentMap.values().iterator();
			while (componentIterator.hasNext()) {
				IFissionComponent component = componentIterator.next();
				componentIterator.remove();
				component.onClusterMeltdown();
			}
		}
		
		reactor.checkIfMachineIsWhole();
	}
	
	public int getTemperature() {
		return Math.round(reactor.ambientTemp + (FissionReactor.MAX_TEMP - reactor.ambientTemp)*(float)heatBuffer.getHeatStored()/heatBuffer.getHeatCapacity());
	}
	
	public float getBurnDamage() {
		return getTemperature() < 373 ? 0F : 1F + (getTemperature() - 373)/200F;
	}
}
