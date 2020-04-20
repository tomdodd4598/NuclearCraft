package nc.multiblock.fission.block;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_ALL;

import javax.annotation.Nullable;

import nc.enumm.MetaEnums;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.util.BlockHelper;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFissionSource extends BlockFissionMetaPart<MetaEnums.NeutronSourceType> {
	
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
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionSource) {
			TileFissionSource source = (TileFissionSource) tile;
			EnumFacing facing = source.getPartPosition().getFacing();
			return state.withProperty(FACING_ALL, facing != null ? facing : source.facing).withProperty(ACTIVE, source.getIsRedstonePowered());
		}
		return state;
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
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		BlockHelper.setDefaultFacing(world, pos, state, FACING_ALL);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta).withProperty(FACING_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionSource) {
			TileFissionSource source = (TileFissionSource) tile;
			source.facing = state.getValue(FACING_ALL);
			world.setBlockState(pos, state.withProperty(FACING_ALL, source.facing).withProperty(ACTIVE, source.getIsRedstonePowered()), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return side != null;
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
