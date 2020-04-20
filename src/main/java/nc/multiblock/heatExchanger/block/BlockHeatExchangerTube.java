package nc.multiblock.heatExchanger.block;

import nc.block.property.ISidedProperty;
import nc.block.property.PropertySidedEnum;
import nc.item.ItemMultitool;
import nc.multiblock.heatExchanger.HeatExchangerTubeSetting;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
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

public class BlockHeatExchangerTube extends BlockHeatExchangerPart implements ISidedProperty<HeatExchangerTubeSetting> {
	
	private static EnumFacing placementSide = null;
	
	private final HeatExchangerTubeType tubeType;

	public BlockHeatExchangerTube(HeatExchangerTubeType tubeType) {
		super();
		this.tubeType = tubeType;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch (tubeType) {
		case COPPER:
			return new TileHeatExchangerTube.Copper();
		case HARD_CARBON:
			return new TileHeatExchangerTube.HardCarbon();
		case THERMOCONDUCTING:
			return new TileHeatExchangerTube.Thermoconducting();
		default:
			return null;
		}
	}
	
	private static final PropertySidedEnum<HeatExchangerTubeSetting> DOWN = PropertySidedEnum.create("down", HeatExchangerTubeSetting.class, EnumFacing.DOWN);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> UP = PropertySidedEnum.create("up", HeatExchangerTubeSetting.class, EnumFacing.UP);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> NORTH = PropertySidedEnum.create("north", HeatExchangerTubeSetting.class, EnumFacing.NORTH);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> SOUTH = PropertySidedEnum.create("south", HeatExchangerTubeSetting.class, EnumFacing.SOUTH);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> WEST = PropertySidedEnum.create("west", HeatExchangerTubeSetting.class, EnumFacing.WEST);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> EAST = PropertySidedEnum.create("east", HeatExchangerTubeSetting.class, EnumFacing.EAST);
	
	@Override
	public HeatExchangerTubeSetting getProperty(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileHeatExchangerTube) {
			return ((TileHeatExchangerTube) tile).getTubeSetting(facing);
		}
		return HeatExchangerTubeSetting.DISABLED;
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
		
		if (ItemMultitool.isMultitool(player.getHeldItem(hand))) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileHeatExchangerTube) {
				TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
				EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
				tube.toggleTubeSetting(side);
				if (!world.isRemote) {
					player.sendMessage(getToggleMessage(player, tube, side));
				}
				return true;
			}
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	private static TextComponentString getToggleMessage(EntityPlayer player, TileHeatExchangerTube tube, EnumFacing side) {
		HeatExchangerTubeSetting setting = tube.getTubeSetting(side);
		String message = player.isSneaking() ? "nc.block.fluid_toggle_opposite" : "nc.block.fluid_toggle";
		TextFormatting color = setting == HeatExchangerTubeSetting.PRODUCT_OUT ? TextFormatting.LIGHT_PURPLE : (setting == HeatExchangerTubeSetting.INPUT_SPREAD ? TextFormatting.GREEN : (setting == HeatExchangerTubeSetting.DEFAULT ? TextFormatting.WHITE : TextFormatting.GRAY));
		return new TextComponentString(Lang.localise(message) + " " + color + Lang.localise("nc.block.exchanger_tube_fluid_side." + setting.getName()));
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
		if (tile instanceof TileHeatExchangerTube && otherTile instanceof TileHeatExchangerTube) {
			TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
			TileHeatExchangerTube other = (TileHeatExchangerTube) otherTile;
			tube.setFluidConnections(FluidConnection.cloneArray(other.getFluidConnections()));
			tube.setTubeSettings(other.getTubeSettings().clone());
			tube.markDirtyAndNotify();
		}
	}
}
