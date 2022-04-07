package nc.block.tile;

import static nc.block.property.BlockProperties.FACING_HORIZONTAL;

import nc.util.BlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
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
		super.onBlockAdded(world, pos, state);
		BlockHelper.setDefaultFacing(world, pos, state, FACING_HORIZONTAL);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta);
		
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		
		return getDefaultState().withProperty(FACING_HORIZONTAL, enumfacing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING_HORIZONTAL).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING_HORIZONTAL);
	}
}
