package nc.multiblock.turbine.tile;

import nc.multiblock.turbine.*;

public class TileTurbineCoilConnector extends TileTurbineDynamoPart {
	
	public TileTurbineCoilConnector() {
		super("connector", null, "connector");
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
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
