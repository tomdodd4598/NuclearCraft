package nc.block.fluid;

import java.util.Random;

import nc.fluid.FluidMolten;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMolten extends NCBlockFluid {
	
	public BlockFluidMolten(Fluid fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
		setLightLevel(0.625F);
	}
	
	public BlockFluidMolten(FluidMolten fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
		setLightLevel(0.625F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.MOLTEN_BURN, 4F);
		entityIn.setFire(10);
	}
	
	@Override
	protected boolean canMixWithFluids(World world, BlockPos pos, IBlockState state) {
		return true;
	}
	
	@Override
	protected boolean shouldMixWithAdjacentFluid(World world, BlockPos pos, IBlockState state, IBlockState otherState) {
		return otherState.getMaterial() == Material.WATER;
	}
	
	@Override
	protected IBlockState getSourceMixingState(World world, BlockPos pos, IBlockState state) {
		return Blocks.STONE.getDefaultState();
	}
	
	@Override
	protected IBlockState getFlowingMixingState(World world, BlockPos pos, IBlockState state) {
		return Blocks.COBBLESTONE.getDefaultState();
	}
	
	@Override
	protected boolean canSetFireToSurroundings(World world, BlockPos pos, IBlockState state, Random rand) {
		return true;
	}
	
	@Override
	protected IBlockState getFlowingIntoWaterState(World world, BlockPos pos, IBlockState state, Random rand) {
		return Blocks.STONE.getDefaultState();
	}
}
