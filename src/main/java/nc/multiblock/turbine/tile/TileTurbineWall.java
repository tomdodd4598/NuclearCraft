package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;

public abstract class TileTurbineWall<TURBINE extends Turbine> extends TileTurbinePartBase<TURBINE> {
	
	public TileTurbineWall(Class<TURBINE> tClass) {
		super(tClass, CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(TURBINE controller) {
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
