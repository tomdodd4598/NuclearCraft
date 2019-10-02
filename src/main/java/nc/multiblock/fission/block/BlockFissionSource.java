package nc.multiblock.fission.block;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_ALL;

import nc.enumm.MetaEnums;
import nc.multiblock.fission.tile.TileFissionSource;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFissionSource extends BlockMetaFissionPartBase<MetaEnums.NeutronSourceType> {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.NeutronSourceType.class);
	
	public BlockFissionSource() {
		super(MetaEnums.NeutronSourceType.class, TYPE);
		setDefaultState(getDefaultState().withProperty(FACING_ALL, EnumFacing.NORTH).withProperty(ACTIVE, Boolean.valueOf(false)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, FACING_ALL, ACTIVE);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(FACING_ALL, getFacing(state, world, pos)).withProperty(ACTIVE, world.getTileEntity(pos) instanceof TileFissionSource && ((TileFissionSource)world.getTileEntity(pos)).getIsRedstonePowered());
	}
	
	public EnumFacing getFacing(IBlockState state, IBlockAccess world, BlockPos pos) {
		EnumFacing facing = null;
		if (world.getTileEntity(pos) instanceof TileFissionSource) {
			facing = ((TileFissionSource) world.getTileEntity(pos)).getPartPosition().getFacing();
		}
		return facing == null ? state.getValue(FACING_ALL) : facing;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch(metadata) {
		case 0:
			return new TileFissionSource.RadiumBeryllium();
		case 1:
			return new TileFissionSource.PoloniumBeryllium();
		case 2:
			return new TileFissionSource.Californium();
		}
		return new TileFissionSource.RadiumBeryllium();
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(meta).withProperty(FACING_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		setDefaultDirection(world, pos, state);
	}
	
	private static void setDefaultDirection(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			EnumFacing enumfacing = state.getValue(FACING_ALL);
			boolean flag = world.getBlockState(pos.north()).isFullBlock();
			boolean flag1 = world.getBlockState(pos.south()).isFullBlock();

			if (enumfacing == EnumFacing.NORTH && flag && !flag1) enumfacing = EnumFacing.SOUTH;
			else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag) enumfacing = EnumFacing.NORTH;
			
			else {
				boolean flag2 = world.getBlockState(pos.west()).isFullBlock();
				boolean flag3 = world.getBlockState(pos.east()).isFullBlock();

				if (enumfacing == EnumFacing.WEST && flag2 && !flag3) enumfacing = EnumFacing.EAST;
				else if (enumfacing == EnumFacing.EAST && flag3 && !flag2) enumfacing = EnumFacing.WEST;
			}
			world.setBlockState(pos, state.withProperty(FACING_ALL, enumfacing).withProperty(ACTIVE, Boolean.valueOf(false)), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	public void setState(boolean isActive, TileEntity tile) {
		World world = tile.getWorld();
		BlockPos pos = tile.getPos();
		IBlockState state = world.getBlockState(pos);
		if (!world.isRemote && isActive != state.getValue(ACTIVE)) {
			world.setBlockState(pos, state.withProperty(ACTIVE, isActive), 2);
		}
	}
}
