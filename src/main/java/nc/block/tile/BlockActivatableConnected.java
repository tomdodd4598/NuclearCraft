package nc.block.tile;

import nc.block.IBlockConnected;
import nc.block.property.PropertySidedBool;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ActivatableTileType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockActivatableConnected extends BlockActivatable implements IBlockConnected {
	
	public static final PropertySidedBool UP = PropertySidedBool.create("up", EnumFacing.UP);
	public static final PropertySidedBool DOWN = PropertySidedBool.create("down", EnumFacing.DOWN);
	public static final PropertySidedBool NORTH = PropertySidedBool.create("north", EnumFacing.NORTH);
	public static final PropertySidedBool SOUTH = PropertySidedBool.create("south", EnumFacing.SOUTH);
	public static final PropertySidedBool WEST = PropertySidedBool.create("west", EnumFacing.WEST);
	public static final PropertySidedBool EAST = PropertySidedBool.create("east", EnumFacing.EAST);
	
	protected final boolean connected;
	
	public BlockActivatableConnected(ActivatableTileType type, boolean isActive, boolean connected) {
		this(type, isActive, false, false, connected);
	}
	
	public BlockActivatableConnected(ActivatableTileType type, boolean isActive, boolean smartRender, boolean connected) {
		this(type, isActive, true, smartRender, connected);
	}
	
	public BlockActivatableConnected(ActivatableTileType type, boolean isActive, boolean transparent, boolean smartRender, boolean connected) {
		super(type, isActive, transparent, smartRender);
		this.connected = connected;
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, DOWN, UP);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (!NCConfig.connected_textures) return super.getActualState(state, world, pos);
		return state.withProperty(NORTH, equalAdjacent(world, pos, EnumFacing.NORTH)).withProperty(SOUTH, equalAdjacent(world, pos, EnumFacing.SOUTH)).withProperty(WEST, equalAdjacent(world, pos, EnumFacing.WEST)).withProperty(EAST, equalAdjacent(world, pos, EnumFacing.EAST)).withProperty(UP, equalAdjacent(world, pos, EnumFacing.UP)).withProperty(DOWN, equalAdjacent(world, pos, EnumFacing.DOWN));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean equalAdjacent(IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!connectedTexturesEnabled()) return false;
		return sameTypeAdjacent(world, pos, side);
	}
	
	@Override
	public boolean connectedTexturesEnabled() {
		return connected;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!transparent) return super.shouldSideBeRendered(blockState, world, pos, side);
		if (!smartRender) return true;
		
		return sameTypeAdjacent(world, pos, side) ? false : super.shouldSideBeRendered(blockState, world, pos, side);
    }
	
	public boolean sameTypeAdjacent(IBlockAccess world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		IBlockState other = world.getBlockState(pos.offset(side));
		if (state != null && other != null) {
			if (state.getBlock() instanceof BlockActivatableConnected && other.getBlock() instanceof BlockActivatableConnected) {
				if (((BlockActivatableConnected) state.getBlock()).type == ((BlockActivatableConnected) other.getBlock()).type) return true;
			}
		}
		return false;
	}
}
