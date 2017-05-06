package nc.tile.fluid;

import nc.fluid.EnumTank.FluidConnection;
import nc.fluid.Tank;
import net.minecraftforge.fluids.FluidStack;

public interface ITileFluid {
	public Tank[] getTanks();
	public FluidConnection[] getFluidConnections();
	public boolean canFill(FluidStack resource, int tankNumber);
}
