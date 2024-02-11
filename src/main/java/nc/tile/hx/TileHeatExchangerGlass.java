package nc.tile.hx;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.HeatExchanger;

public class TileHeatExchangerGlass extends TileHeatExchangerPart {
	
	public TileHeatExchangerGlass() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
}
