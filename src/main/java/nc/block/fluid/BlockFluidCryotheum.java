package nc.block.fluid;

import nc.fluid.FluidCryotheum;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidCryotheum extends NCBlockFluid {
	
	public BlockFluidCryotheum(Fluid fluid) {
		super(fluid, Material.LAVA);
		setLightLevel(0.5F);
	}
	
	public BlockFluidCryotheum(FluidCryotheum fluid) {
		super(fluid, Material.LAVA);
		setLightLevel(0.5F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(BlockSuperFluid.superfluid_freeze, 4.0F);
	}
}
