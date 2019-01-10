package nc.block.fluid;

import nc.fluid.FluidFission;
import nc.util.PotionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidFission extends BlockFluidMolten {
	
	protected static DamageSource corium_burn = new DamageSource("corium_burn");
	
	public BlockFluidFission(Fluid fluid) {
		super(fluid);
	}
	
	public BlockFluidFission(FluidFission fluid) {
		super(fluid);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(corium_burn, 4.0F);
		entityIn.setFire(10);
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(18, 1, 100));
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(19, 1, 100));
		}
	}
}
