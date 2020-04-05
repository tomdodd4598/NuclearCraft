package nc.multiblock.fission.salt.block;

import nc.block.property.ISidedProperty;
import nc.block.property.PropertySidedEnum;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.salt.SaltFissionVesselSetting;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSaltFissionVessel extends BlockFissionPart implements ISidedProperty<SaltFissionVesselSetting> {

	//private static EnumFacing placementSide = null;
	
	public BlockSaltFissionVessel() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionVessel();
	}
	
	private static final PropertySidedEnum<SaltFissionVesselSetting> DOWN = PropertySidedEnum.create("down", SaltFissionVesselSetting.class, EnumFacing.DOWN);
	private static final PropertySidedEnum<SaltFissionVesselSetting> UP = PropertySidedEnum.create("up", SaltFissionVesselSetting.class, EnumFacing.UP);
	private static final PropertySidedEnum<SaltFissionVesselSetting> NORTH = PropertySidedEnum.create("north", SaltFissionVesselSetting.class, EnumFacing.NORTH);
	private static final PropertySidedEnum<SaltFissionVesselSetting> SOUTH = PropertySidedEnum.create("south", SaltFissionVesselSetting.class, EnumFacing.SOUTH);
	private static final PropertySidedEnum<SaltFissionVesselSetting> WEST = PropertySidedEnum.create("west", SaltFissionVesselSetting.class, EnumFacing.WEST);
	private static final PropertySidedEnum<SaltFissionVesselSetting> EAST = PropertySidedEnum.create("east", SaltFissionVesselSetting.class, EnumFacing.EAST);
	
	@Override
	public SaltFissionVesselSetting getProperty(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileSaltFissionVessel) {
			return ((TileSaltFissionVessel) tile).getVesselSetting(facing);
		}
		return SaltFissionVesselSetting.DISABLED;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DOWN, UP, NORTH, SOUTH, WEST, EAST);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(DOWN, getProperty(world, pos, EnumFacing.DOWN)).withProperty(UP, getProperty(world, pos, EnumFacing.UP)).withProperty(NORTH, getProperty(world, pos, EnumFacing.NORTH)).withProperty(SOUTH, getProperty(world, pos, EnumFacing.SOUTH)).withProperty(WEST, getProperty(world, pos, EnumFacing.WEST)).withProperty(EAST, getProperty(world, pos, EnumFacing.EAST));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	/*@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player == null) return false;
		
		TileEntity tile = world.getTileEntity(pos);
		if (player.getHeldItemMainhand().isEmpty() && tile instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
			EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
			vessel.toggleVesselSetting(side);
			if (!world.isRemote) player.sendMessage(getToggleMessage(player, vessel, side));
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	private static TextComponentString getToggleMessage(EntityPlayer player, TileSaltFissionVessel vessel, EnumFacing side) {
		SaltFissionVesselSetting setting = vessel.getVesselSetting(side);
		String message = player.isSneaking() ? "nc.block.fluid_toggle_opposite" : "nc.block.fluid_toggle";
		TextFormatting color = setting == SaltFissionVesselSetting.DEPLETED_OUT ? TextFormatting.LIGHT_PURPLE : (setting == SaltFissionVesselSetting.FUEL_SPREAD ? TextFormatting.GREEN : (setting == SaltFissionVesselSetting.DEFAULT ? TextFormatting.WHITE : TextFormatting.GRAY));
		return new TextComponentString(Lang.localise(message) + " " + color + Lang.localise("nc.block.salt_vessel_fluid_side." + setting.getName()));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		placementSide = null;
		if (placer != null && placer.isSneaking()) placementSide = facing.getOpposite();
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (placementSide ==  null) return;
		BlockPos from = pos.offset(placementSide);
		TileEntity tile = world.getTileEntity(pos), otherTile = world.getTileEntity(from);
		if (tile instanceof TileSaltFissionVessel && otherTile instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
			TileSaltFissionVessel other = (TileSaltFissionVessel) otherTile;
			vessel.setFluidConnections(FluidConnection.cloneArray(other.getFluidConnections()));
			vessel.setVesselSettings(other.getVesselSettings().clone());
			vessel.markDirtyAndNotify();
		}
	}*/
}
