package nc.tile.distributor;

import nc.multiblock.distributor.Distributor;

public class TileDistributorBuffer extends TileDistributorPart {
	
	@Override
	public void onMachineAssembled(Distributor multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
}
