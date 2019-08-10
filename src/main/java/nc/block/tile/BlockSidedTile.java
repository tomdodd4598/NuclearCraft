package nc.block.tile;

import static nc.block.property.BlockProperties.FACING_HORIZONTAL;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockSidedTile extends BlockTile {

	public BlockSidedTile(Material material) {
		super(material);
		setDefaultState(getNewDefaultState());
	}
	
	protected IBlockState getNewDefaultState() {
		return blockState.getBaseState().withProperty(FACING_HORIZONTAL, EnumFacing.NORTH);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		setDefaultFacing(world, pos, state);
	}
	
	private static void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			IBlockState northBlock = world.getBlockState(pos.north());
			IBlockState southBlock = world.getBlockState(pos.south());
			IBlockState westBlock = world.getBlockState(pos.west());
			IBlockState eastBlock = world.getBlockState(pos.east());
			EnumFacing facing = state.getValue(FACING_HORIZONTAL);
			
			if (facing == EnumFacing.NORTH && northBlock.isFullBlock() && !southBlock.isFullBlock()) {
				facing = EnumFacing.SOUTH;
			} else if (facing == EnumFacing.SOUTH && southBlock.isFullBlock() && !northBlock.isFullBlock()) {
				facing = EnumFacing.NORTH;
			} else if (facing == EnumFacing.WEST && westBlock.isFullBlock() && !eastBlock.isFullBlock()) {
				facing = EnumFacing.EAST;
			} else if (facing == EnumFacing.EAST && eastBlock.isFullBlock() && !westBlock.isFullBlock()) {
				facing = EnumFacing.WEST;
			}
			world.setBlockState(pos, state.withProperty(FACING_HORIZONTAL, facing), 2);
		}
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta);
		
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) enumfacing = EnumFacing.NORTH;
		
		return getDefaultState().withProperty(FACING_HORIZONTAL, enumfacing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING_HORIZONTAL).getIndex();
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING_HORIZONTAL, rot.rotate(state.getValue(FACING_HORIZONTAL)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING_HORIZONTAL)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING_HORIZONTAL});
	}
}
