package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nonnull;

import nc.tile.ITileFiltered;
import nc.tile.internal.fluid.Tank;

public interface ITileFilteredFluid extends ITileFiltered, ITileFluid {
	
	@Nonnull List<Tank> getTanksInternal();
	
	@Nonnull List<Tank> getFilterTanks();
	
	default void clearFilterTank(int tankNumber) {
		getFilterTanks().get(tankNumber).setFluidStored(null);
	}
}
