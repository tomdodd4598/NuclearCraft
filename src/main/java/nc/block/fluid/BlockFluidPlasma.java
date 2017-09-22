package nc.block.fluid;

import java.util.Random;

import nc.init.NCBlocks;
import net.minecraft.block.Block;
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
		entityIn.attackEntityFrom(plasma_burn, 8.0F);
		entityIn.setFire(10);
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (rand.nextInt(200) < 2) {
			EnumFacing side = EnumFacing.values()[rand.nextInt(6)];
			BlockPos offpos = pos.offset(side);
			Block offBlock = worldIn.getBlockState(offpos).getBlock();
			if (offBlock == NCBlocks.fusion_electromagnet_idle || offBlock == NCBlocks.fusion_electromagnet_transparent_idle || offBlock == NCBlocks.fusion_connector) {
				worldIn.setBlockState(offpos, Blocks.AIR.getDefaultState());
			}
		}
		if (!isSourceBlock(worldIn, pos)) {
			if (rand.nextInt(5) < 1) worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
		super.updateTick(worldIn, pos, state, rand);
	}
}
