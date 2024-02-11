package nc.tile.quantum;

import nc.multiblock.quantum.QuantumComputer;

public class TileQuantumComputerConnector extends TileQuantumComputerPart {
	
	public TileQuantumComputerConnector() {}
	
	@Override
	public void onMachineAssembled(QuantumComputer multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
}
