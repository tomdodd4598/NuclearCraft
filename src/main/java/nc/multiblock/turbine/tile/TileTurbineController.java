package nc.multiblock.turbine.tile;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.block.BlockTurbineController;

public class TileTurbineController extends TileTurbinePartBase {
	
	protected int controllerCount;
	
	public TileTurbineController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
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
		if (controllerCount == 0) if (getBlock(pos) instanceof BlockTurbineController) {
			if (getMultiblock() != null) ((BlockTurbineController) getBlock(pos)).setActiveState(getBlockState(pos), world, pos, getMultiblock().isTurbineOn);
		}
	}
	
	public void tickController() {
		controllerCount++; controllerCount %= NCConfig.machine_update_rate / 4;
	}
}
