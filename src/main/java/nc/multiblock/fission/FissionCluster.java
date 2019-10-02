package nc.multiblock.fission;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nc.config.NCConfig;
import nc.multiblock.fission.salt.FissionReactorType;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionCoolingComponent;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.tile.internal.heat.HeatBuffer;

public class FissionCluster {
	
	private final FissionReactor reactor;
	private int id;
	
	private final Long2ObjectMap<IFissionComponent> componentMap = new Long2ObjectOpenHashMap<>();
	
	public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);
	
	public boolean connectedToWall = false;
	public int componentCount = 0, fuelComponentCount = 0;
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L, meanHeatMult = 0L;
	public double effectiveHeating = 0D, totalEfficiency = 0D, meanEfficiency = 0D, overcoolingEfficiencyFactor = 0D, undercoolingLifetimeFactor = 0D;
	
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
		cooling = rawHeating = totalHeatMult = meanHeatMult = 0L;
		effectiveHeating = totalEfficiency = meanEfficiency = overcoolingEfficiencyFactor = undercoolingLifetimeFactor = 0D;
		
		heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT*componentMap.size());
		
		if (reactor.type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
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
		
		overcoolingEfficiencyFactor = cooling == 0 ? 1D : Math.min(1D, (double)(rawHeating + NCConfig.fission_cooling_efficiency_leniency)/(double)cooling);
		undercoolingLifetimeFactor = rawHeating == 0 ? 1D : Math.min(1D, (double)(cooling + NCConfig.fission_cooling_efficiency_leniency)/(double)rawHeating);
		effectiveHeating *= overcoolingEfficiencyFactor;
		totalEfficiency *= overcoolingEfficiencyFactor;
		meanHeatMult = fuelComponentCount == 0 ? 0L : Math.round((double)totalHeatMult/fuelComponentCount);
		meanEfficiency = fuelComponentCount == 0 ? 0D : totalEfficiency/fuelComponentCount;
		
		if (reactor.type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
			for (IFissionComponent component : componentMap.values()) {
				if (component instanceof IFissionFuelComponent) {
					((IFissionFuelComponent)component).setUndercoolingLifetimeFactor(undercoolingLifetimeFactor);
				}
			}
		}
	}
	
	public long getNetHeating() {
		return connectedToWall ? -cooling : rawHeating - cooling;
	}
	
	public void doMeltdown() {
		if (reactor.type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
			Iterator<IFissionComponent> componentIterator = componentMap.values().iterator();
			while (componentIterator.hasNext()) {
				IFissionComponent component = componentIterator.next();
				componentIterator.remove();
				if (component instanceof IFissionFuelComponent) {
					((IFissionFuelComponent)component).doMeltdown();
				}
			}
		}
		
		reactor.checkIfMachineIsWhole();
	}
}
