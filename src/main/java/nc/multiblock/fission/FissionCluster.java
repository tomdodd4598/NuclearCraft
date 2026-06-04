package nc.multiblock.fission;

import it.unimi.dsi.fastutil.longs.*;
import nc.tile.fission.IFissionComponent;
import nc.tile.internal.heat.HeatBuffer;

public class FissionCluster {
	
	protected final FissionReactor reactor;
	protected int id;
	
	protected final Long2ObjectMap<IFissionComponent> componentMap = new Long2ObjectOpenHashMap<>();
	
	public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);
	
	public boolean connectedToWall = false;
	public int componentCount = 0, fuelComponentCount = 0;
	public long cooling = 0L, rawHeating = 0L, rawHeatingIgnoreCoolingPenalty = 0L;
	public double totalBaseFuelHeating = 0D, effectiveHeating = 0D, effectiveHeatingIgnoreCoolingPenalty = 0D, meanHeatMult = 0D, meanEfficiency = 0D, overcoolingEfficiencyFactor = 0D, undercoolingLifetimeFactor = 0D;
	
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
	
	public long getNetHeating() {
		return rawHeating - cooling;
	}
	
	public void distributeHeatToComponents(boolean simulate) {
		if (simulate || componentMap.isEmpty()) {
			return;
		}
		
		long distributedHeat = (long) Math.ceil((double) heatBuffer.getHeatStored() / (double) componentMap.size());
		for (IFissionComponent component : componentMap.values()) {
			component.setHeatStored(heatBuffer.removeHeat(distributedHeat, false));
			if (heatBuffer.isEmpty()) {
				return;
			}
		}
		heatBuffer.setHeatStored(0L);
	}
	
	public void recoverHeatFromComponents(boolean simulate) {
		if (simulate) {
			return;
		}
		
		heatBuffer.setHeatStored(0L);
		for (IFissionComponent component : componentMap.values()) {
			heatBuffer.addHeat(component.getHeatStored(), false);
			component.setHeatStored(0L);
		}
	}
	
	public double getTemperature() {
		return reactor.ambientTemp + (FissionReactor.MAX_TEMP - reactor.ambientTemp) * (double) heatBuffer.getHeatStored() / (double) heatBuffer.getHeatCapacity();
	}
	
	public float getBurnDamage() {
		double temp = getTemperature();
		return temp < 353D ? 0F : (float) (1D + (temp - 353D) / 200D);
	}
}
