package nc.multiblock.condenser.tile;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.condenser.Condenser;
import nc.multiblock.condenser.block.BlockCondenserController;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;

public class TileCondenserController extends TileCondenserPartBase {
	
	public TileCondenserController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(Condenser controller) {
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
		if (shouldTileCheck()) if (getBlock(pos) instanceof BlockCondenserController) {
			((BlockCondenserController) getBlock(pos)).setActiveState(getBlockState(pos), world, pos, world.isBlockPowered(pos) && isMultiblockAssembled());
		}
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 4;
	}
	
	public void doMeltdown() {
		Block corium = RegistryHelper.getBlock(Global.MOD_ID, "fluid_corium");
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium.getDefaultState());
	}
	
	public boolean isPowered() {
		return world.isBlockPowered(pos);
	}
}
