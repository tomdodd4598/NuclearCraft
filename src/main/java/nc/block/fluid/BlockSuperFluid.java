package nc.block.fluid;

import nc.fluid.SuperFluid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockSuperFluid extends BlockFluid {
	
	public static DamageSource superfluid_freeze = (new DamageSource("superfluid_freeze")).setDamageBypassesArmor();

	public BlockSuperFluid(Fluid fluid) {
		super(fluid, Material.WATER);
		setQuantaPerBlock(16);
	}
	
	public BlockSuperFluid(SuperFluid fluid) {
		super(fluid, Material.WATER);
		setQuantaPerBlock(16);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(superfluid_freeze, 6.0F);
	}
}
