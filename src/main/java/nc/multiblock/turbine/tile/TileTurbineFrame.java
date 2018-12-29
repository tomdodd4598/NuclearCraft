package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;

public class TileTurbineFrame extends TileTurbinePartBase {
	
	public TileTurbineFrame() {
		super(CuboidalPartPositionType.FRAME);
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
}
