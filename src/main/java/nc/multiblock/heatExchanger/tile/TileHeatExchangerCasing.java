package nc.multiblock.heatExchanger.tile;

import nc.block.property.BlockProperties;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.HeatExchanger;

public class TileHeatExchangerCasing extends TileHeatExchangerPart {
	
	public TileHeatExchangerCasing() {
		super(CuboidalPartPositionType.EXTERIOR);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().isFrame()) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockProperties.FRAME, true), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		if (!getWorld().isRemote && getPartPosition().isFrame()) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockProperties.FRAME, false), 2);
		}
		super.onMachineBroken();
	}
}
