package nc.multiblock.turbine.tile;

import static nc.block.property.BlockProperties.FACING_ALL;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.block.BlockTurbineController;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class TileTurbineController extends TileTurbinePart implements ITurbineController {
	
	public boolean isRenderer = false;
	
	public TileTurbineController() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public String getLogicID() {
		return "turbine";
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(FACING_ALL, getPartPosition().getFacing()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		// if (getWorld().isRemote) return;
		// getWorld().setBlockState(getPos(),
		// getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public int[] weakSidesToCheck(World world, BlockPos pos) {
		return new int[] {2, 3, 4, 5};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		if (!isRenderer || !isMultiblockAssembled()) {
			return Block.FULL_BLOCK_AABB.offset(pos);
		}
		return new AxisAlignedBB(getMultiblock().getMinimumCoord(), getMultiblock().getMaximumCoord());
		// return INFINITE_EXTENT_AABB;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return super.getMaxRenderDistanceSquared();
	}
	
	@Override
	public boolean hasFastRenderer() {
		return super.hasFastRenderer();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		if (getMultiblock() != null) {
			getLogic().setIsTurbineOn();
		}
	}
	
	@Override
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockTurbineController) {
			((BlockTurbineController) getBlockType()).setState(isActive, this);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	@Override
	public boolean isRenderer() {
		return isRenderer;
	}
	
	@Override
	public void setIsRenderer(boolean isRenderer) {
		this.isRenderer = isRenderer;
	}
}
