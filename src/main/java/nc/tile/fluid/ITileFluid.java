package nc.tile.fluid;

import java.util.List;

import nc.tile.ITile;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import net.minecraftforge.fluids.FluidStack;

public interface ITileFluid extends ITile {
	
	public List<Tank> getTanks();
	
	public List<FluidConnection> getFluidConnections();
	
	public boolean canFill(FluidStack resource, int tankNumber);
	
	//public boolean canReceiveGas(EnumFacing side, Gas gas);
	
	public void clearTank(int tankNo);
	
	public boolean getTanksShared();
	
	public void setTanksShared(boolean shared);
	
	public boolean getEmptyUnusableTankInputs();
	
	public void setEmptyUnusableTankInputs(boolean emptyUnusableTankInputs);
	
	public boolean getVoidExcessFluidOutputs();
	
	public void setVoidExcessFluidOutputs(boolean voidExcessFluidOutputs);
}
