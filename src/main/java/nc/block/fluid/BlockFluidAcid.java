package nc.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidAcid extends BlockFluid {
	
	public static DamageSource acid_burn = new DamageSource("acid_burn");

	public BlockFluidAcid(Fluid fluid, String name) {
		super(fluid, name, Material.WATER);
	}
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			entityIn.attackEntityFrom(acid_burn, 3.0F);
			if (entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.getPotionById(18), 100, 2));
				((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.getPotionById(19), 100, 2));
			}
		}
	}
}
