package nc.block.tile;

import nc.block.IBlockConnected;
import nc.block.property.PropertySidedBool;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.SimpleTileType;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSimpleTileConnected extends BlockSimpleTile implements IBlockConnected {
	
	public static final PropertySidedBool UP = PropertySidedBool.create("up", EnumFacing.UP);
	public static final PropertySidedBool DOWN = PropertySidedBool.create("down", EnumFacing.DOWN);
	public static final PropertySidedBool NORTH = PropertySidedBool.create("north", EnumFacing.NORTH);
	public static final PropertySidedBool SOUTH = PropertySidedBool.create("south", EnumFacing.SOUTH);
	public static final PropertySidedBool WEST = PropertySidedBool.create("west", EnumFacing.WEST);
	public static final PropertySidedBool EAST = PropertySidedBool.create("east", EnumFacing.EAST);
	
	protected final boolean connected;
	
	public BlockSimpleTileConnected(SimpleTileType type, int connectedInt) {
		super(type);
		connected = NCConfig.connected_textures_each[connectedInt];
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, DOWN, UP);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (!connectedTexturesEnabled()) return getDefaultState();
		return state.withProperty(NORTH, equalAdjacent(world, pos, EnumFacing.NORTH)).withProperty(SOUTH, equalAdjacent(world, pos, EnumFacing.SOUTH)).withProperty(WEST, equalAdjacent(world, pos, EnumFacing.WEST)).withProperty(EAST, equalAdjacent(world, pos, EnumFacing.EAST)).withProperty(UP, equalAdjacent(world, pos, EnumFacing.UP)).withProperty(DOWN, equalAdjacent(world, pos, EnumFacing.DOWN));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean connectedTexturesEnabled() {
		return NCConfig.connected_textures && connected;
	}
	
	public static class Transparent extends BlockSimpleTileConnected {
		
		protected final boolean smartRender;
		
		public Transparent(SimpleTileType type, int connectedInt, boolean smartRender) {
			super(type, connectedInt);
			setHardness(1.5F);
			setResistance(10F);
			this.smartRender = smartRender;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getBlockLayer() {
			return BlockRenderLayer.CUTOUT;
		}

		@Override
		public boolean isFullCube(IBlockState state) {
			return false;
		}
		
		@Override
		public boolean isOpaqueCube(IBlockState state) {
			return false;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
			if (!smartRender) return true;
			
			IBlockState otherState = world.getBlockState(pos.offset(side));
			Block block = otherState.getBlock();
			
			return block == this ? false : super.shouldSideBeRendered(blockState, world, pos, side);
	    }
	}
}
