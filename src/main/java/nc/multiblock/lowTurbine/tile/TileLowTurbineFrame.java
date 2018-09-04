package nc.multiblock.lowTurbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.lowTurbine.LowTurbine;

public class TileLowTurbineFrame extends TileLowTurbinePartBase {
	
	public TileLowTurbineFrame() {
		super(CuboidalPartPositionType.FRAME);
	}
	
	@Override
	public void onMachineAssembled(LowTurbine controller) {
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
