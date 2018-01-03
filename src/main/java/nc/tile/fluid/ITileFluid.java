package nc.tile.fluid;

import nc.fluid.EnumTank.FluidConnection;
import nc.fluid.Tank;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public interface ITileFluid {
	public Tank[] getTanks();
	public FluidConnection[] getFluidConnections();
	public boolean canFill(FluidStack resource, int tankNumber);
	//public boolean canReceiveGas(EnumFacing side, Gas gas);
	public boolean getTanksShared();
	public void setTanksShared(boolean shared);
	public void clearTank(int tankNo);
	public BlockPos getBlockPos();
}
