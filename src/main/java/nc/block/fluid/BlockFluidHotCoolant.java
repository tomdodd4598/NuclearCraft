package nc.block.fluid;

import nc.fluid.FluidHotCoolant;
import nc.util.PotionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidHotCoolant extends BlockFluid {
	
	public static DamageSource hot_coolant_burn = new DamageSource("hot_coolant_burn");

	public BlockFluidHotCoolant(Fluid fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
	}
	
	public BlockFluidHotCoolant(FluidHotCoolant fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(hot_coolant_burn, 2.0F);
		entityIn.setFire(3);
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(2, 1, 100));
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(18, 1, 100));
		}
	}
}
