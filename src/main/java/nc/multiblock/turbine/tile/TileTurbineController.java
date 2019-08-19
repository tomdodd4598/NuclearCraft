package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.block.BlockTurbineController;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileTurbineController extends TileTurbinePartBase {
	
	public boolean isRenderer = false;
	
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		if (!isRenderer || !isMultiblockAssembled()) {
			return Block.FULL_BLOCK_AABB.offset(pos);
		}
		return new AxisAlignedBB(pos.add(getMultiblock().getMinX() - pos.getX(), getMultiblock().getMinY() - pos.getY(), getMultiblock().getMinZ() - pos.getZ()), pos.add(getMultiblock().getMaxX() - pos.getX(), getMultiblock().getMaxY() - pos.getY(), getMultiblock().getMaxZ() - pos.getZ()));
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		if (getMultiblock() != null) getMultiblock().setIsTurbineOn();
	}
	
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockTurbineController) {
			((BlockTurbineController)getBlockType()).setState(isActive, this);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
}
