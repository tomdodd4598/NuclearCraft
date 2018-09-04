package nc.multiblock.highTurbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.highTurbine.HighTurbine;

public class TileHighTurbineGlass extends TileHighTurbinePartBase {
	
	public TileHighTurbineGlass() {
		super(CuboidalPartPositionType.WALL);
	}

	@Override
	public void onMachineAssembled(HighTurbine controller) {
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
