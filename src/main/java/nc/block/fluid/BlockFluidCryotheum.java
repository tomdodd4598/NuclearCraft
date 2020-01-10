package nc.block.fluid;

import nc.fluid.FluidCryotheum;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidCryotheum extends NCBlockFluid {
	
	public BlockFluidCryotheum(FluidCryotheum fluid) {
		super(fluid, Material.LAVA);
		setLightLevel(0.5F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.HYPOTHERMIA, 4F);
	}
}
