package nc.block.fluid;

import nc.fluid.FluidSugar;
import nc.util.PotionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidSugar extends NCBlockFluid {
	
	public BlockFluidSugar(FluidSugar fluid) {
		super(fluid, Material.WATER);
		setQuantaPerBlock(6);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(PotionHelper.newEffect(2, 3, 100));
		}
	}
}
