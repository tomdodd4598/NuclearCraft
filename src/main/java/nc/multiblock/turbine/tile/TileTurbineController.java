package nc.multiblock.turbine.tile;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.block.BlockTurbineController;

public abstract class TileTurbineController<TURBINE extends Turbine, CONTROLLER extends BlockTurbineController> extends TileTurbinePartBase<TURBINE> {
	
	protected final Class<CONTROLLER> blockControllerClass;
	
	public TileTurbineController(Class<TURBINE> tClass, Class<CONTROLLER> blockControllerClass) {
		super(tClass, CuboidalPartPositionType.WALL);
		this.blockControllerClass = blockControllerClass;
	}
	
	@Override
	public void onMachineAssembled(TURBINE controller) {
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
		tickTile();
		if (shouldTileCheck()) if (blockControllerClass.isInstance(getBlock(pos))) {
			if (getMultiblock() != null) ((CONTROLLER) getBlock(pos)).setActiveState(getBlockState(pos), world, pos, getMultiblock().isTurbineOn);
		}
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 4;
	}
}
