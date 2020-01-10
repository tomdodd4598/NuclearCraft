package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.fluid.FluidParticle;
import nc.util.PotionHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
		if (rand.nextInt(5) < 1) worldIn.setBlockToAir(pos);
	}
}
