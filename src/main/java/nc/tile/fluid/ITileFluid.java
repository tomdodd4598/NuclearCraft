package nc.tile.fluid;

import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public interface ITileFluid {
	
	public BlockPos getFluidTilePos();
	
	public Tank[] getTanks();
	
	public FluidConnection[] getFluidConnections();
	
	public boolean canFill(FluidStack resource, int tankNumber);
	
	//public boolean canReceiveGas(EnumFacing side, Gas gas);
	
	public boolean getTanksShared();
	
	public void setTanksShared(boolean shared);
	
	public boolean getTanksEmptyUnusable();
	
	public void setTanksEmptyUnusable(boolean emptyUnusable);
	
	public boolean getVoidExcessOutputs();
	
	public void setVoidExcessOutputs(boolean voidExcessOutputs);
	
	public void clearTank(int tankNo);
}
