package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nonnull;

import nc.tile.ITileFiltered;
import nc.tile.internal.fluid.Tank;

public interface ITileFilteredFluid extends ITileFiltered, ITileFluid {
	
	public @Nonnull List<Tank> getTanksInternal();
	
	public @Nonnull List<Tank> getFilterTanks();
	
	//public FluidStack getFilterStack(int tank);
	
	//public FluidStack setFilterStack(int tank, FluidStack stack);
	
	public default void clearFilterTank(int tankNumber) {
		getFilterTanks().get(tankNumber).setFluidStored(null);
	}
	
	public default void clearAllFilterTanks() {
		for (Tank tank : getFilterTanks()) tank.setFluidStored(null);
	}
}
