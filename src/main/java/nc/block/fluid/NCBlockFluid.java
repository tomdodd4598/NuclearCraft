package nc.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fluids.*;

public class NCBlockFluid extends BlockFluidClassic {
	
	public final String name;
	public final Fluid fluid;
	
	public NCBlockFluid(Fluid fluid, Material material) {
		super(fluid, material);
		name = "fluid_" + fluid.getName();
		this.fluid = fluid;
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid()) {
			return false;
		}
		return super.canDisplace(world, pos);
	}
	
	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, pos);
	}
}
