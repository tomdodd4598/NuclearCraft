package nc.multiblock.condenser.tile;

import nc.multiblock.condenser.Condenser;
import nc.multiblock.cuboidal.CuboidalPartPositionType;

public class TileCondenserFrame extends TileCondenserPartBase {
	
	public TileCondenserFrame() {
		super(CuboidalPartPositionType.FRAME);
	}
	
	@Override
	public void onMachineAssembled(Condenser controller) {
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
