package nc.tile.internal.fluid;

import mekanism.api.gas.*;
import nc.tile.fluid.ITileFluid;
import nc.util.GasHelper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "mekanism"), @Optional.Interface(iface = "mekanism.api.gas.IGasHandler", modid = "mekanism")})
public class GasTileWrapper implements ITubeConnection, IGasHandler {
	
	public final ITileFluid tile;
	
	public GasTileWrapper(ITileFluid tile) {
		this.tile = tile;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public boolean canTubeConnect(EnumFacing side) {
		return tile.canConnectFluid(side);
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
		int amount = tile.fill(side, GasHelper.getFluidFromGas(stack), doTransfer);
		tile.onWrapperReceiveGas(amount, doTransfer);
		return amount;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
		GasStack stack = GasHelper.getGasFromFluid(tile.drain(side, amount, doTransfer));
		tile.onWrapperDrawGas(stack, doTransfer);
		return stack;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public boolean canReceiveGas(EnumFacing side, Gas type) {
		Fluid fluid = type.getFluid();
		if (fluid == null) {
			return false;
		}
		for (Tank tank : tile.getTanks()) {
			if (tank.canFillFluidType(fluid)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	@Optional.Method(modid = "mekanism")
	public boolean canDrawGas(EnumFacing side, Gas type) {
		return type.getFluid() != null && FluidRegistry.getFluid(type.getFluid().getName()) != null;
	}
}
