package nc.multiblock.fission;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.tile.internal.heat.HeatBuffer;

public class FissionCluster {
	
	protected final FissionReactor reactor;
	protected int id;
	
	protected final Long2ObjectMap<IFissionComponent> componentMap = new Long2ObjectOpenHashMap<>();
	
	public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);
	
	public boolean connectedToWall = false;
	public int componentCount = 0, fuelComponentCount = 0;
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L;
	public double effectiveHeating = 0D, meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, overcoolingEfficiencyFactor = 0D, undercoolingLifetimeFactor = 0D, totalHeatingSpeedMultiplier = 0D, meanHeatingSpeedMultiplier = 0D;
	
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
	
	public int getTemperature() {
		return Math.round(reactor.ambientTemp + (FissionReactor.MAX_TEMP - reactor.ambientTemp)*(float)heatBuffer.getHeatStored()/heatBuffer.getHeatCapacity());
	}
	
	public float getBurnDamage() {
		return getTemperature() < 373 ? 0F : 1F + (getTemperature() - 373)/200F;
	}
}
