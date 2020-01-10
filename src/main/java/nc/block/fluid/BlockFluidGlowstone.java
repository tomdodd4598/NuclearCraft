package nc.block.fluid;

import nc.fluid.FluidGlowstone;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidGlowstone extends NCBlockFluid {
	
	public BlockFluidGlowstone(FluidGlowstone fluid) {
		super(fluid, Material.LAVA);
		setLightLevel(1F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.MOLTEN_BURN, 4F);
		entityIn.setFire(10);
	}
}
