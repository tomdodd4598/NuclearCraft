package nc.tile.internal.fluid;

import javax.annotation.Nullable;

import mekanism.api.gas.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "mekanism"), @Optional.Interface(iface = "mekanism.api.gas.IGasHandler", modid = "mekanism")})
public class TankVoid implements IFluidTank, IFluidHandler, ITubeConnection, IGasHandler {
	
	public TankVoid() {}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		return new IFluidTankProperties[] {new TankVoidProperties(this)};
	}
	
	private static class TankVoidProperties implements IFluidTankProperties {
		
		protected final TankVoid tank;
		
		public TankVoidProperties(TankVoid tank) {
			this.tank = tank;
		}
		
		@Override
		public @Nullable FluidStack getContents() {
			return tank.getFluid();
		}
		
		@Override
		public int getCapacity() {
			return tank.getCapacity();
		}
		
		@Override
		public boolean canFill() {
			return true;
		}
		
		@Override
		public boolean canDrain() {
			return false;
		}
		
		@Override
		public boolean canFillFluidType(FluidStack fluidStack) {
			return true;
		}
		
		@Override
		public boolean canDrainFluidType(FluidStack fluidStack) {
			return false;
		}
	}
	
	@Override
	public @Nullable FluidStack drain(FluidStack resource, boolean doDrain) {
		return null;
	}
	
	@Override
	public @Nullable FluidStack getFluid() {
		return null;
	}
	
	@Override
	public int getFluidAmount() {
		return 0;
	}
	
	@Override
	public int getCapacity() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return resource == null ? 0 : resource.amount;
	}
	
	@Override
	public @Nullable FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public boolean canDrawGas(EnumFacing side, Gas gas) {
		return false;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public boolean canReceiveGas(EnumFacing side, Gas gas) {
		return true;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public GasStack drawGas(EnumFacing side, int var2, boolean doTransfer) {
		return null;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
		return stack == null ? 0 : stack.amount;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public boolean canTubeConnect(EnumFacing side) {
		return true;
	}
}
