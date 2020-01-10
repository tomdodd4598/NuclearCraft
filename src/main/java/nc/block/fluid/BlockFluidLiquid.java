package nc.block.fluid;

import nc.fluid.FluidLiquid;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidLiquid extends NCBlockFluid {
	
	public BlockFluidLiquid(FluidLiquid fluid) {
		super(fluid, Material.WATER);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		int temp = getFluid().getTemperature();
		if (temp < 250) {
			entityIn.attackEntityFrom(DamageSources.HYPOTHERMIA, 0.024F*(250 - temp));
		}
		else if (temp > 350) {
			entityIn.attackEntityFrom(DamageSources.FLUID_BURN, Math.min(6F, (float)Math.log10(temp - 350)));
		}
	}
}
