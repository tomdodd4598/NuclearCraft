package nc.multiblock.fission.solid.block;

import nc.NuclearCraft;
import nc.block.property.ISidedProperty;
import nc.block.property.PropertySidedEnum;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.solid.SolidFissionCellSetting;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.util.Lang;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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

public class BlockSolidFissionCell extends BlockFissionPart implements ISidedProperty<SolidFissionCellSetting> {

	//private static EnumFacing placementSide = null;
	
	public BlockSolidFissionCell() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSolidFissionCell();
	}
	
	private static final PropertySidedEnum<SolidFissionCellSetting> DOWN = PropertySidedEnum.create("down", SolidFissionCellSetting.class, EnumFacing.DOWN);
	private static final PropertySidedEnum<SolidFissionCellSetting> UP = PropertySidedEnum.create("up", SolidFissionCellSetting.class, EnumFacing.UP);
	private static final PropertySidedEnum<SolidFissionCellSetting> NORTH = PropertySidedEnum.create("north", SolidFissionCellSetting.class, EnumFacing.NORTH);
	private static final PropertySidedEnum<SolidFissionCellSetting> SOUTH = PropertySidedEnum.create("south", SolidFissionCellSetting.class, EnumFacing.SOUTH);
	private static final PropertySidedEnum<SolidFissionCellSetting> WEST = PropertySidedEnum.create("west", SolidFissionCellSetting.class, EnumFacing.WEST);
	private static final PropertySidedEnum<SolidFissionCellSetting> EAST = PropertySidedEnum.create("east", SolidFissionCellSetting.class, EnumFacing.EAST);
	
	@Override
	public SolidFissionCellSetting getProperty(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileSolidFissionCell) {
			return ((TileSolidFissionCell) tile).getCellSetting(facing);
		}
		return SolidFissionCellSetting.DISABLED;
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
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSolidFissionCell) {
				TileSolidFissionCell cell = (TileSolidFissionCell) tile;
				FissionReactor reactor = cell.getMultiblock();
				if (reactor != null) {
					ItemStack heldStack = player.getHeldItem(hand);
					if (cell.canModifyFilter(0) && cell.getInventoryStacks().get(0).isEmpty() && !heldStack.isItemEqual(cell.getFilterStacks().get(0)) && cell.isItemValidForSlotInternal(0, heldStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(heldStack.getTranslationKey() + ".name")));
						ItemStack filter = heldStack.copy();
						filter.setCount(1);
						cell.getFilterStacks().set(0, filter);
						cell.onFilterChanged(0);
					}
					else {
						player.openGui(NuclearCraft.instance, 201, world, pos.getX(), pos.getY(), pos.getZ());
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSolidFissionCell) {
				TileSolidFissionCell cell = (TileSolidFissionCell) tile;
				dropItems(world, pos, cell.getInventoryStacksInternal());
				//world.updateComparatorOutputLevel(pos, this);
				//FissionReactor reactor = cell.getMultiblock();
				//world.removeTileEntity(pos);
				/*if (reactor != null) {
					reactor.getLogic().refreshPorts();
				}*/
			}
		}
		//super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
	
	/*@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player == null) return false;
		
		if (ItemMultitool.isMultitool(player.getHeldItem(hand))) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSolidFissionCell) {
				TileSolidFissionCell vessel = (TileSolidFissionCell) tile;
				EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
				vessel.toggleCellSetting(side);
				if (!world.isRemote) player.sendMessage(getToggleMessage(player, vessel, side));
				return true;
			}
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	private static TextComponentString getToggleMessage(EntityPlayer player, TileSolidFissionCell vessel, EnumFacing side) {
		SolidFissionCellSetting setting = vessel.getCellSetting(side);
		String message = player.isSneaking() ? "nc.block.fluid_toggle_opposite" : "nc.block.fluid_toggle";
		TextFormatting color = setting == SolidFissionCellSetting.DEPLETED_OUT ? TextFormatting.LIGHT_PURPLE : (setting == SolidFissionCellSetting.FUEL_SPREAD ? TextFormatting.GREEN : (setting == SolidFissionCellSetting.DEFAULT ? TextFormatting.WHITE : TextFormatting.GRAY));
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
		if (tile instanceof TileSolidFissionCell && otherTile instanceof TileSolidFissionCell) {
			TileSolidFissionCell vessel = (TileSolidFissionCell) tile;
			TileSolidFissionCell other = (TileSolidFissionCell) otherTile;
			vessel.setInventoryConnections(InventoryConnection.cloneArray(other.getInventoryConnections()));
			vessel.setCellSettings(other.getCellSettings().clone());
			vessel.markDirtyAndNotify();
		}
	}*/
}
