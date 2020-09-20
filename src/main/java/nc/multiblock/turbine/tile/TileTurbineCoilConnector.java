package nc.multiblock.turbine.tile;

import nc.multiblock.turbine.Turbine;

public class TileTurbineCoilConnector extends TileTurbineDynamoPart {
	
	public TileTurbineCoilConnector() {
		super("connector", null, "connector");
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
}
