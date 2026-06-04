package nc.tile.machine;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.machine.Machine;

public class TileDecayPoolContainer extends TileMachinePart {
	
	public TileDecayPoolContainer() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(Machine multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
}
