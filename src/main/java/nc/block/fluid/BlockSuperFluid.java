package nc.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockSuperFluid extends BlockFluid {

	public static DamageSource superfluid_freeze = (new DamageSource("superfluid_freeze")).setDamageBypassesArmor();

	public BlockSuperFluid(Fluid fluid, String name) {
		super(fluid, name, Material.WATER);
		setQuantaPerBlock(16);
	}
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(superfluid_freeze, 6.0F);
	}
}
