package nc.multiblock.fission.moltensalt.tile;

import nc.multiblock.MultiblockControllerBase;

public class TileSaltFissionController extends TileSaltFissionPartBase {
	
	public TileSaltFissionController() {
		super(PartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
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
