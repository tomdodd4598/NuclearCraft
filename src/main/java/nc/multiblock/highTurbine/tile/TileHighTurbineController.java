package nc.multiblock.highTurbine.tile;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.highTurbine.HighTurbine;
import nc.multiblock.highTurbine.block.BlockHighTurbineController;

public class TileHighTurbineController extends TileHighTurbinePartBase {
	
	public TileHighTurbineController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HighTurbine controller) {
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
		if (shouldTileCheck()) if (getBlock(pos) instanceof BlockHighTurbineController) {
			((BlockHighTurbineController) getBlock(pos)).setActiveState(getBlockState(pos), world, pos, world.isBlockPowered(pos) && isMultiblockAssembled());
		}
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 4;
	}
	
	public boolean isPowered() {
		return world.isBlockPowered(pos);
	}
}
