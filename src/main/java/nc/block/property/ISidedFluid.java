package nc.block.property;

import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ISidedFluid {
	
	public static final PropertySidedEnum<FluidConnection> FLUID_UP = fluidSide("up", EnumFacing.UP);
	public static final PropertySidedEnum<FluidConnection> FLUID_DOWN = fluidSide("down", EnumFacing.DOWN);
	public static final PropertySidedEnum<FluidConnection> FLUID_NORTH = fluidSide("north", EnumFacing.NORTH);
	public static final PropertySidedEnum<FluidConnection> FLUID_SOUTH = fluidSide("south", EnumFacing.SOUTH);
	public static final PropertySidedEnum<FluidConnection> FLUID_WEST = fluidSide("west", EnumFacing.WEST);
	public static final PropertySidedEnum<FluidConnection> FLUID_EAST = fluidSide("east", EnumFacing.EAST);
	
	public static PropertySidedEnum<FluidConnection> fluidSide(String name, EnumFacing facing) {
		return PropertySidedEnum.create(name, FluidConnection.class, facing);
	}
	
	public default IBlockState getActualFluidState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(FLUID_NORTH, getFluidConnection(world, pos, EnumFacing.NORTH)).withProperty(FLUID_SOUTH, getFluidConnection(world, pos, EnumFacing.SOUTH)).withProperty(FLUID_WEST, getFluidConnection(world, pos, EnumFacing.WEST)).withProperty(FLUID_EAST, getFluidConnection(world, pos, EnumFacing.EAST)).withProperty(FLUID_UP, getFluidConnection(world, pos, EnumFacing.UP)).withProperty(FLUID_DOWN, getFluidConnection(world, pos, EnumFacing.DOWN));
	}
	
	public default FluidConnection getFluidConnection(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		if (world.getTileEntity(pos) instanceof ITileFluid) return ((ITileFluid) world.getTileEntity(pos)).getFluidConnection(facing);
		return FluidConnection.NON;
	}
	
	public default BlockStateContainer createFluidBlockState(Block block) {
		return new BlockStateContainer(block, FLUID_NORTH, FLUID_EAST, FLUID_SOUTH, FLUID_WEST, FLUID_DOWN, FLUID_UP);
	}
}
