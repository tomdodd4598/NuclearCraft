package nc.multiblock.fission.salt.block;

import nc.NuclearCraft;
import nc.block.property.*;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.salt.SaltFissionVesselSetting;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.util.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidStack;

public class BlockSaltFissionVessel extends BlockFissionPart implements ISidedProperty<SaltFissionVesselSetting> {
	
	// private static EnumFacing placementSide = null;
	
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
		if (player == null) {
			return false;
		}
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSaltFissionVessel) {
				TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
				FissionReactor reactor = vessel.getMultiblock();
				if (reactor != null) {
					FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
					if (vessel.canModifyFilter(0) && vessel.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(vessel.getFilterTanks().get(0).getFluid(), fluidStack) && vessel.getTanks().get(0).canFillFluidType(fluidStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(fluidStack.getUnlocalizedName())));
						FluidStack filter = fluidStack.copy();
						filter.amount = 1000;
						vessel.getFilterTanks().get(0).setFluid(filter);
						vessel.onFilterChanged(0);
					}
					else {
						player.openGui(NuclearCraft.instance, 202, world, pos.getX(), pos.getY(), pos.getZ());
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
}
