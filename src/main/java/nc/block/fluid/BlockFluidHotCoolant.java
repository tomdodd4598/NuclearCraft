package nc.block.fluid;

import nc.fluid.FluidHotCoolant;
import nc.util.DamageSources;
import nc.util.PotionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidHotCoolant extends NCBlockFluid {
	
	public BlockFluidHotCoolant(FluidHotCoolant fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
		setLightLevel(0.625F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.HOT_COOLANT_BURN, 2F);
		entityIn.setFire(3);
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(2, 1, 100));
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(18, 1, 100));
		}
	}
}
