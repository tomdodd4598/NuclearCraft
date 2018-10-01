package nc.block.fluid;

import nc.fluid.FluidMolten;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMolten extends BlockFluid {
	
	protected static DamageSource molten_burn = new DamageSource("molten_burn");

	public BlockFluidMolten(Fluid fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
	}
	
	public BlockFluidMolten(FluidMolten fluid) {
		super(fluid, Material.LAVA);
		setQuantaPerBlock(4);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(molten_burn, 4.0F);
		entityIn.setFire(10);
	}
}
