package nc.multiblock.saltFission.tile;

import nc.multiblock.MultiblockControllerBase;

public class TileSaltFissionFrame extends TileSaltFissionPartBase {
	
	public TileSaltFissionFrame() {
		super(PartPositionType.FRAME);
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
