package nc.multiblock.fission.moltensalt.tile;

import nc.multiblock.MultiblockControllerBase;

public class TileSaltFissionWall extends TileSaltFissionPartBase {
	
	public TileSaltFissionWall() {
		super(PartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		super.onMachineAssembled(controller);
		doStandardNullControllerResponse(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}

}
