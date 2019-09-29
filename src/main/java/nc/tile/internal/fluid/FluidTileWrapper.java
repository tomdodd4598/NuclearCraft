package nc.tile.internal.fluid;

import nc.tile.fluid.ITileFluid;
import nc.tile.processor.IProcessor;
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
		int amount = tile.fill(side, resource, doFill);
		if (doFill && amount != 0) {
			if (tile instanceof IProcessor) {
				((IProcessor)tile).refreshRecipe();
				((IProcessor)tile).refreshActivity();
			}
		}
		return amount;
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		FluidStack stack = tile.drain(side, resource, doDrain);
		if (doDrain && (stack != null && stack.amount != 0)) {
			if (tile instanceof IProcessor) ((IProcessor)tile).refreshActivity();
		}
		return stack;
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		FluidStack stack = tile.drain(side, maxDrain, doDrain);
		if (doDrain && (stack != null && stack.amount != 0)) {
			if (tile instanceof IProcessor) ((IProcessor)tile).refreshActivity();
		}
		return stack;
	}
}
