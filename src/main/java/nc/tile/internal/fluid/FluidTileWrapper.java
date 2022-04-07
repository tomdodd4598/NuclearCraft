package nc.tile.internal.fluid;

import javax.annotation.Nonnull;

import nc.tile.fluid.ITileFluid;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;

public class FluidTileWrapper implements IFluidHandler {
	
	public final ITileFluid tile;
	public final @Nonnull EnumFacing side;
	
	public FluidTileWrapper(ITileFluid tile, @Nonnull EnumFacing side) {
		this.tile = tile;
		this.side = side;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		return tile.getTankProperties(side);
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		int amount = tile.fill(side, resource, doFill);
		tile.onWrapperFill(amount, doFill);
		return amount;
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		FluidStack stack = tile.drain(side, resource, doDrain);
		tile.onWrapperDrain(stack, doDrain);
		return stack;
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		FluidStack stack = tile.drain(side, maxDrain, doDrain);
		tile.onWrapperDrain(stack, doDrain);
		return stack;
	}
}
