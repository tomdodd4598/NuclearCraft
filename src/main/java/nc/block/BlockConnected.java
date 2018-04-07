package nc.block;

import nc.block.property.PropertySidedBool;
import nc.config.NCConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockConnected extends NCBlock implements IBlockConnected {
	
	public static final PropertySidedBool UP = PropertySidedBool.create("up", EnumFacing.UP);
	public static final PropertySidedBool DOWN = PropertySidedBool.create("down", EnumFacing.DOWN);
	public static final PropertySidedBool NORTH = PropertySidedBool.create("north", EnumFacing.NORTH);
	public static final PropertySidedBool SOUTH = PropertySidedBool.create("south", EnumFacing.SOUTH);
	public static final PropertySidedBool WEST = PropertySidedBool.create("west", EnumFacing.WEST);
	public static final PropertySidedBool EAST = PropertySidedBool.create("east", EnumFacing.EAST);
	
	protected final boolean connected;
	
	public BlockConnected(String name, Material material, boolean connected) {
		this(name, material, false, false, connected);
	}
	
	public BlockConnected(String name, Material material, boolean smartRender, boolean connected) {
		this(name, material, true, smartRender, connected);
	}
	
	public BlockConnected(String name, Material material, boolean transparent, boolean smartRender, boolean connected) {
		super(name, material, transparent, smartRender);
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
	public boolean connectedTexturesEnabled() {
		return connected;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!transparent) return super.shouldSideBeRendered(blockState, world, pos, side);
		if (!smartRender) return true;
		
		IBlockState otherState = world.getBlockState(pos.offset(side));
		Block block = otherState.getBlock();
		
		return block == this ? false : super.shouldSideBeRendered(blockState, world, pos, side);
    }
}
