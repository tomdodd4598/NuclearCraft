package nc.block;

import net.minecraft.block.BlockMagma;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class NCBlockMagma extends BlockMagma {
	
	private final DamageSource damageSource;
	private final float damageAmount;
	
	public NCBlockMagma(DamageSource damageSource, float damageAmount) {
		super();
		this.damageSource = damageSource;
		this.damageAmount = damageAmount;
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return blockMapColor;
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entityIn)) {
			entityIn.attackEntityFrom(damageSource, damageAmount);
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}
}
