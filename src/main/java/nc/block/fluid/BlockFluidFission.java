package nc.block.fluid;

import nc.fluid.FluidFission;
import nc.init.NCBlocks;
import nc.util.PotionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

public class BlockFluidFission extends BlockFluidMolten {
	
	public BlockFluidFission(Fluid fluid) {
		super(fluid);
	}
	
	public BlockFluidFission(FluidFission fluid) {
		super(fluid);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.setFire(10);
		if (entityIn instanceof EntityLivingBase entityLivingBase) {
			entityLivingBase.addPotionEffect(PotionHelper.newEffect(18, 1, 100));
			entityLivingBase.addPotionEffect(PotionHelper.newEffect(19, 1, 100));
		}
	}
	
	@Override
	protected IBlockState getFlowingIntoWaterState(World world, BlockPos pos, IBlockState state, Random rand) {
		return NCBlocks.wasteland_earth.getDefaultState();
	}
}
