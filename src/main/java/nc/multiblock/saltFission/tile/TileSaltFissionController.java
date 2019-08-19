package nc.multiblock.saltFission.tile;

import nc.Global;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.block.BlockSaltFissionController;
import nc.util.RegistryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSaltFissionController extends TileSaltFissionPartBase {
	
	public TileSaltFissionController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(SaltFissionReactor controller) {
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		if (getMultiblock() != null) getMultiblock().setIsReactorOn();
	}
	
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockSaltFissionController) {
			((BlockSaltFissionController)getBlockType()).setState(isActive, this);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	public void doMeltdown() {
		IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium);
	}
}
