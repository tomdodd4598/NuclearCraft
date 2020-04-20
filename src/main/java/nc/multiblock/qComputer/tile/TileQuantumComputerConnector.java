package nc.multiblock.qComputer.tile;

import nc.multiblock.qComputer.QuantumComputer;

public class TileQuantumComputerConnector extends TileQuantumComputerPart {
	
	public TileQuantumComputerConnector() {}
	
	@Override
	public void onMachineAssembled(QuantumComputer multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
}
