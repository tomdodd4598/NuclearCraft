package nc.multiblock.saltFission.block;

import nc.block.property.ISidedProperty;
import nc.block.property.PropertySidedEnum;
import nc.multiblock.saltFission.SaltFissionVesselSetting;
import nc.multiblock.saltFission.tile.TileSaltFissionVessel;
import nc.tile.internal.fluid.FluidConnection;
import nc.util.Lang;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSaltFissionVessel extends BlockSaltFissionPartBase implements ISidedProperty<SaltFissionVesselSetting> {

	private static EnumFacing placementSide = null;
	
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
		if (world.getTileEntity(pos) instanceof TileSaltFissionVessel) {
			return ((TileSaltFissionVessel) world.getTileEntity(pos)).getVesselSetting(facing);
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
		if (hand != EnumHand.MAIN_HAND || player == null) return false;
		
		if (player.getHeldItemMainhand().isEmpty() && world.getTileEntity(pos) instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel) world.getTileEntity(pos);
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
		if (world.getTileEntity(pos) instanceof TileSaltFissionVessel && world.getTileEntity(from) instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel) world.getTileEntity(pos);
			TileSaltFissionVessel other = (TileSaltFissionVessel) world.getTileEntity(from);
			vessel.setFluidConnections(FluidConnection.cloneArray(other.getFluidConnections()));
			vessel.setVesselSettings(other.getVesselSettings().clone());
			vessel.markDirtyAndNotify();
		}
	}
}
