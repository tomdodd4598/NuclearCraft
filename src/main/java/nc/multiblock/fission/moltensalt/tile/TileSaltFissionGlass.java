package nc.multiblock.fission.moltensalt.tile;

import nc.multiblock.MultiblockControllerBase;

public class TileSaltFissionGlass extends TileSaltFissionPartBase {
	
	public TileSaltFissionGlass() {
		super(PartPositionType.WALL);
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
	
}
