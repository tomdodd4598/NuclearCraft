package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.fluid.FluidSteam;
import nc.util.DamageSources;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidSteam extends NCBlockFluid {
	
	private static final Material GAS = new MaterialLiquid(MapColor.AIR);
	
	public BlockFluidSteam(FluidSteam fluid) {
		super(fluid, GAS);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.STEAM_BURN, 3F);
		entityIn.setFire(1);
	}
	
	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (isSourceBlock(worldIn, pos)) worldIn.setBlockToAir(pos);
	}
}
