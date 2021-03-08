package nc.block.fluid;

import java.util.Random;

import nc.fluid.FluidFlammable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockFluidFlammable extends NCBlockFluid {
	
	public BlockFluidFlammable(FluidFlammable fluid) {
		super(fluid, Material.WATER);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 500;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 200;
	}
	
	@Override
	protected boolean canMixWithFluids(World world, BlockPos pos, IBlockState state) {
		return false;
	}
	
	@Override
	protected boolean shouldMixWithAdjacentFluid(World world, BlockPos pos, IBlockState state, IBlockState otherState) {
		return false;
	}
	
	@Override
	protected IBlockState getSourceMixingState(World world, BlockPos pos, IBlockState state) {
		return Blocks.OBSIDIAN.getDefaultState();
	}
	
	@Override
	protected IBlockState getFlowingMixingState(World world, BlockPos pos, IBlockState state) {
		return Blocks.COBBLESTONE.getDefaultState();
	}
	
	@Override
	protected boolean canSetFireToSurroundings(World world, BlockPos pos, IBlockState state, Random rand) {
		return false;
	}
	
	@Override
	protected IBlockState getFlowingIntoWaterState(World world, BlockPos pos, IBlockState state, Random rand) {
		return null;
	}
}
