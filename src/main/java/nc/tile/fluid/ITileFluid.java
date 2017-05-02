package nc.tile.fluid;

import nc.fluid.EnumTank.FluidConnection;
import net.minecraftforge.fluids.FluidStack;
import nc.fluid.Tank;

public interface ITileFluid {
	public Tank[] getTanks();
	public FluidConnection[] getFluidConnections();
	public boolean canFill(FluidStack resource, int tankNumber);
}
