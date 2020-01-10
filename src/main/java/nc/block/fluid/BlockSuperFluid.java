package nc.block.fluid;

import nc.fluid.SuperFluid;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSuperFluid extends NCBlockFluid {
	
	public BlockSuperFluid(SuperFluid fluid) {
		super(fluid, Material.WATER);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.SUPERFLUID_FREEZE, 6F);
	}
}
