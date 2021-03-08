package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.fluid.FluidParticle;
import nc.util.PotionHelper;
import net.minecraft.block.material.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidParticle extends NCBlockFluid {
	
	private static final Material GAS = new MaterialLiquid(MapColor.AIR);
	
	public BlockFluidParticle(FluidParticle fluid) {
		super(fluid, GAS);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(9, 2, 200));
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(20, 2, 100));
		}
	}
	
	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (rand.nextInt(5) < 1) {
			worldIn.setBlockToAir(pos);
		}
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
