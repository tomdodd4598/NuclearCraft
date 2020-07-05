package nc.multiblock.fission.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;

public class TileFissionGlass extends TileFissionPart {
	
	public TileFissionGlass() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		// if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		// if (getWorld().isRemote) return;
		// getWorld().setBlockState(getPos(),
		// getWorld().getBlockState(getPos()), 2);
	}
}
