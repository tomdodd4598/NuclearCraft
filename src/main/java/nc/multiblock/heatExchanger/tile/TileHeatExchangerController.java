package nc.multiblock.heatExchanger.tile;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerController;

public class TileHeatExchangerController extends TileHeatExchangerPartBase {
	
	protected int controllerCount;
	
	public TileHeatExchangerController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
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
	
	@Override
	public void update() {
		super.update();
		tickController();
		if (controllerCount == 0) if (getBlock(pos) instanceof BlockHeatExchangerController) {
			if (getMultiblock() != null) ((BlockHeatExchangerController) getBlock(pos)).setActiveState(getBlockState(pos), world, pos, getMultiblock().isHeatExchangerOn);
		}
	}
	
	public void tickController() {
		controllerCount++; controllerCount %= NCConfig.machine_update_rate / 4;
	}
}
