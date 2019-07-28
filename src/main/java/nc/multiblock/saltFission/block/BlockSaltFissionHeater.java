package nc.multiblock.saltFission.block;

import nc.block.property.ISidedProperty;
import nc.block.property.PropertySidedEnum;
import nc.multiblock.saltFission.SaltFissionHeaterSetting;
import nc.multiblock.saltFission.tile.TileSaltFissionHeater;
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

public class BlockSaltFissionHeater extends BlockSaltFissionPartBase implements ISidedProperty<SaltFissionHeaterSetting> {

	private static EnumFacing placementSide = null;
	
	public BlockSaltFissionHeater() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionHeater();
	}
	
	private static final PropertySidedEnum<SaltFissionHeaterSetting> DOWN = PropertySidedEnum.create("down", SaltFissionHeaterSetting.class, EnumFacing.DOWN);
	private static final PropertySidedEnum<SaltFissionHeaterSetting> UP = PropertySidedEnum.create("up", SaltFissionHeaterSetting.class, EnumFacing.UP);
	private static final PropertySidedEnum<SaltFissionHeaterSetting> NORTH = PropertySidedEnum.create("north", SaltFissionHeaterSetting.class, EnumFacing.NORTH);
	private static final PropertySidedEnum<SaltFissionHeaterSetting> SOUTH = PropertySidedEnum.create("south", SaltFissionHeaterSetting.class, EnumFacing.SOUTH);
	private static final PropertySidedEnum<SaltFissionHeaterSetting> WEST = PropertySidedEnum.create("west", SaltFissionHeaterSetting.class, EnumFacing.WEST);
	private static final PropertySidedEnum<SaltFissionHeaterSetting> EAST = PropertySidedEnum.create("east", SaltFissionHeaterSetting.class, EnumFacing.EAST);
	
	@Override
	public SaltFissionHeaterSetting getProperty(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		if (world.getTileEntity(pos) instanceof TileSaltFissionHeater) {
			return ((TileSaltFissionHeater) world.getTileEntity(pos)).getHeaterSetting(facing);
		}
		return SaltFissionHeaterSetting.DISABLED;
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
		
		if (player.getHeldItemMainhand().isEmpty() && world.getTileEntity(pos) instanceof TileSaltFissionHeater) {
			TileSaltFissionHeater heater = (TileSaltFissionHeater) world.getTileEntity(pos);
			EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
			heater.toggleHeaterSetting(side);
			if (!world.isRemote) player.sendMessage(getToggleMessage(player, heater, side));
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	private static TextComponentString getToggleMessage(EntityPlayer player, TileSaltFissionHeater heater, EnumFacing side) {
		SaltFissionHeaterSetting setting = heater.getHeaterSetting(side);
		String message = player.isSneaking() ? "nc.block.fluid_toggle_opposite" : "nc.block.fluid_toggle";
		TextFormatting color = setting == SaltFissionHeaterSetting.HOT_COOLANT_OUT ? TextFormatting.RED : (setting == SaltFissionHeaterSetting.COOLANT_SPREAD ? TextFormatting.AQUA : (setting == SaltFissionHeaterSetting.DEFAULT ? TextFormatting.WHITE : TextFormatting.GRAY));
		return new TextComponentString(Lang.localise(message) + " " + color + Lang.localise("nc.block.salt_heater_fluid_side." + setting.getName()));
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
		if (world.getTileEntity(pos) instanceof TileSaltFissionHeater && world.getTileEntity(from) instanceof TileSaltFissionHeater) {
			TileSaltFissionHeater heater = (TileSaltFissionHeater) world.getTileEntity(pos);
			TileSaltFissionHeater other = (TileSaltFissionHeater) world.getTileEntity(from);
			heater.setFluidConnections(FluidConnection.cloneArray(other.getFluidConnections()));
			heater.setHeaterSettings(other.getHeaterSettings().clone());
			heater.markAndRefresh();
		}
	}
}
