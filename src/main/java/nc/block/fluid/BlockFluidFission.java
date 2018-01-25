package nc.block.fluid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidFission extends BlockFluidMolten {
	
	public BlockFluidFission(Fluid fluid, String name) {
		super(fluid, name);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(molten_burn, 4.0F);
		entityIn.setFire(10);
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.getPotionById(18), 50, 1));
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.getPotionById(19), 50, 1));
		}
	}
}
