package nc.multiblock.fission.moltensalt.tile;

import nc.multiblock.MultiblockControllerBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileSaltFissionVessel extends TileSaltFissionPartBase implements IFluidHandler {
	
	public TileSaltFissionVessel() {
		super(PartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
		doStandardNullControllerResponse(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}
	
}
