package nc.tile.internal.fluid;

import nc.tile.fluid.ITileFluid;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidTileWrapper implements IFluidHandler {
	
	public final ITileFluid tile;
	public final EnumFacing side;
	
	public FluidTileWrapper(ITileFluid tile, EnumFacing side) {
		this.tile = tile;
		this.side = side;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		return tile.getTankProperties(side);
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return tile.fill(resource, doFill, side);
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return tile.drain(resource, doDrain, side);
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return tile.drain(maxDrain, doDrain, side);
	}
}
