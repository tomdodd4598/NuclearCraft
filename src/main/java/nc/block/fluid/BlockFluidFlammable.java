package nc.block.fluid;

import nc.fluid.FluidFlammable;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockFluidFlammable extends BlockFluid {

	public BlockFluidFlammable(FluidFlammable fluid) {
		super(fluid, Material.WATER);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 250;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 2000;
	}
}
