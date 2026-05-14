package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import nc.multiblock.fission.FissionFuelBunch;

import javax.annotation.Nullable;

public interface IFissionFuelBunchComponent extends IFissionFuelComponent {
	
	@Override
	default void addToComponentFailCache(final Long2ObjectMap<IFissionComponent> componentFailCache) {
		componentFailCache.putAll(getFuelBunch().fuelComponentMap);
	}
	
	@Nullable FissionFuelBunch getFuelBunch();
	
	void setFuelBunch(@Nullable FissionFuelBunch fuelBunch);
	
	default int getFuelBunchSize() {
		FissionFuelBunch fuelBunch = getFuelBunch();
		return fuelBunch == null ? 0 : fuelBunch.fuelComponentMap.size();
	}
	
	long getIndividualFlux();
	
	long getIndividualHeatMultiplier(boolean simulate);
}
