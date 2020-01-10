package nc.block.fluid;

import nc.fluid.FluidMolten;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMolten extends NCBlockFluid {
	
	public BlockFluidMolten(Fluid fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
		setLightLevel(0.625F);
	}
	
	public BlockFluidMolten(FluidMolten fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
		setLightLevel(0.625F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.MOLTEN_BURN, 4F);
		entityIn.setFire(10);
	}
}
