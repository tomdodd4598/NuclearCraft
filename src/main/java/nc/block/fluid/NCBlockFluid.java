package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.*;

public abstract class NCBlockFluid extends BlockFluidClassic {
	
	public final String name;
	public final Fluid fluid;
	
	public NCBlockFluid(Fluid fluid, Material material) {
		super(fluid, material);
		name = "fluid_" + fluid.getName();
		this.fluid = fluid;
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isLiquid()) {
			return false;
		}
		return super.canDisplace(world, pos);
	}
	
	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, pos);
	}
	
	@Override
	public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		checkForMixing(world, pos, state);
		super.onBlockAdded(world, pos, state);
	}
	
	@Override
	public void neighborChanged(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos) {
		checkForMixing(world, pos, state);
		super.neighborChanged(state, world, pos, block, fromPos);
	}
	
	protected void checkForMixing(World world, BlockPos pos, IBlockState state) {
		if (canMixWithFluids(world, pos, state)) {
			boolean flag = false;
			
			for (EnumFacing side : EnumFacing.VALUES) {
				if (side != EnumFacing.DOWN && shouldMixWithAdjacentFluid(world, pos, state, world.getBlockState(pos.offset(side)))) {
					flag = true;
					break;
				}
			}
			
			if (flag) {
				int level = state.getValue(LEVEL);
				if (level == 0) {
					world.setBlockState(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, getSourceMixingState(world, pos, state)));
					triggerMixEffects(world, pos);
					return;
				}
				else {
					world.setBlockState(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, getFlowingMixingState(world, pos, state)));
					triggerMixEffects(world, pos);
					return;
				}
			}
		}
	}
	
	protected void triggerMixEffects(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		for (int i = 0; i < 8; ++i) {
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + 1.2D, pos.getZ() + Math.random(), 0D, 0D, 0D);
		}
	}
	
	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		super.updateTick(world, pos, state, rand);
		int level = state.getValue(LEVEL);
		if (level == 0) {
			if (canSetFireToSurroundings(world, pos, state, rand) && world.getGameRules().getBoolean("doFireTick")) {
				int i = rand.nextInt(3);
				
				if (i > 0) {
					BlockPos firePos = pos;
					for (int j = 0; j < i; ++j) {
						firePos = firePos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
						
						if (firePos.getY() >= 0 && firePos.getY() < world.getHeight() && !world.isBlockLoaded(firePos)) {
							return;
						}
						
						IBlockState block = world.getBlockState(firePos);
						
						if (block.getBlock().isAir(block, world, firePos)) {
							if (isSurroundingBlockFlammable(world, firePos)) {
								world.setBlockState(firePos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, firePos, pos, Blocks.FIRE.getDefaultState()));
								return;
							}
						}
						else if (block.getMaterial().blocksMovement()) {
							return;
						}
					}
				}
				else {
					for (int k = 0; k < 3; ++k) {
						BlockPos firePos = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
						
						if (firePos.getY() >= 0 && firePos.getY() < world.getHeight() && !world.isBlockLoaded(firePos)) {
							return;
						}
						
						if (world.isAirBlock(firePos.up()) && canBlockBurn(world, firePos)) {
							world.setBlockState(firePos.up(), ForgeEventFactory.fireFluidPlaceBlockEvent(world, firePos.up(), pos, Blocks.FIRE.getDefaultState()));
						}
					}
				}
			}
		}
		else {
			IBlockState newState = getFlowingIntoWaterState(world, pos, state, rand);
			if (newState != null) {
				IBlockState stateDown = world.getBlockState(pos.down());
				if (canFlowInto(world, pos.down(), stateDown) && world.getBlockState(pos.down()).getMaterial() == Material.WATER) {
					world.setBlockState(pos.down(), ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos.down(), pos, newState));
					triggerMixEffects(world, pos.down());
				}
			}
		}
	}
	
	protected abstract boolean canMixWithFluids(World world, BlockPos pos, IBlockState state);
	
	protected abstract boolean shouldMixWithAdjacentFluid(World world, BlockPos pos, IBlockState state, IBlockState otherState);
	
	protected abstract IBlockState getSourceMixingState(World world, BlockPos pos, IBlockState state);
	
	protected abstract IBlockState getFlowingMixingState(World world, BlockPos pos, IBlockState state);
	
	protected abstract boolean canSetFireToSurroundings(World world, BlockPos pos, IBlockState state, Random rand);
	
	protected abstract IBlockState getFlowingIntoWaterState(World world, BlockPos pos, IBlockState state, Random rand);
	
	protected boolean isSurroundingBlockFlammable(World world, BlockPos pos) {
		for (EnumFacing side : EnumFacing.VALUES) {
			if (canBlockBurn(world, pos.offset(side))) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean canBlockBurn(World world, BlockPos pos) {
		return pos.getY() >= 0 && pos.getY() < world.getHeight() && !world.isBlockLoaded(pos) ? false : world.getBlockState(pos).getMaterial().getCanBurn();
	}
	
	protected boolean canFlowInto(World world, BlockPos pos, IBlockState state) {
		Material mat = state.getMaterial();
		return mat != material && mat != Material.LAVA && !isBlocked(world, pos, state);
	}
	
	protected boolean isBlocked(World world, BlockPos pos, IBlockState state) {
		Block block = state.getBlock();
		Material mat = state.getMaterial();
		
		if (!(block instanceof BlockDoor) && !(block instanceof BlockStandingSign) && !(block instanceof BlockLadder) && !(block instanceof BlockReed)) {
			return mat != Material.PORTAL && mat != Material.STRUCTURE_VOID ? mat.blocksMovement() : true;
		}
		else {
			return true;
		}
	}
}
