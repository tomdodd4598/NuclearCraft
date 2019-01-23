package nc.multiblock.condenser.tile;

import nc.multiblock.condenser.Condenser;
import nc.multiblock.condenser.block.BlockCondenserController;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		if (getMultiblock() != null) getMultiblock().setIsCondenserOn();
	}
	
	public void updateBlock(boolean active) {
		if (getBlockType() instanceof BlockCondenserController) {
			((BlockCondenserController)getBlockType()).setActiveState(getBlockState(pos), world, pos, active);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
}
