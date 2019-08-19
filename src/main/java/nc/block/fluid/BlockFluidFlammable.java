package nc.block.fluid;

import nc.fluid.FluidFlammable;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidFlammable extends NCBlockFluid {

	public BlockFluidFlammable(Fluid fluid) {
		super(fluid, Material.WATER);
	}
	
	public BlockFluidFlammable(FluidFlammable fluid) {
		super(fluid, Material.WATER);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 500;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 200;
	}
}
