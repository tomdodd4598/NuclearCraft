package nc.block.fluid;

import java.util.Random;

import nc.init.NCBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidPlasma extends BlockFluid {
	
	public static DamageSource plasma_burn = (new DamageSource("plasma_burn")).setDamageBypassesArmor();

	public BlockFluidPlasma(Fluid fluid, String name) {
		super(fluid, name, Material.LAVA);
		setQuantaPerBlock(16);
	}
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			entityIn.attackEntityFrom(plasma_burn, 8.0F);
			entityIn.setFire(10);
		}
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (rand.nextInt(500) < 1) for (EnumFacing side : EnumFacing.values()) {
			BlockPos offpos = pos.offset(side);
			if (worldIn.getBlockState(offpos).getBlock() == NCBlocks.fusion_electromagnet_idle) worldIn.setBlockState(offpos, Blocks.AIR.getDefaultState());
		}
		if (rand.nextInt(3) < 1 && !isSourceBlock(worldIn, pos)) worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState());
	}
}
