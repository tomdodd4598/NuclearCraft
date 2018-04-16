package nc.block.tile;

import nc.block.property.PropertySidedEnum;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.battery.IBattery;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.UnitHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBattery extends BlockSimpleTile implements INBTDrop {
	
	public static final PropertySidedEnum<EnergyConnection> UP = batterySide("up", EnumFacing.UP);
	public static final PropertySidedEnum<EnergyConnection> DOWN = batterySide("down", EnumFacing.DOWN);
	public static final PropertySidedEnum<EnergyConnection> NORTH = batterySide("north", EnumFacing.NORTH);
	public static final PropertySidedEnum<EnergyConnection> SOUTH = batterySide("south", EnumFacing.SOUTH);
	public static final PropertySidedEnum<EnergyConnection> WEST = batterySide("west", EnumFacing.WEST);
	public static final PropertySidedEnum<EnergyConnection> EAST = batterySide("east", EnumFacing.EAST);
	
	public static PropertySidedEnum<EnergyConnection> batterySide(String name, EnumFacing facing) {
		return PropertySidedEnum.create(name, EnergyConnection.class, new EnergyConnection[] {EnergyConnection.IN, EnergyConnection.OUT, EnergyConnection.NON}, facing);
	}
	
	public BlockBattery(SimpleTileType type) {
		super(type);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, DOWN, UP);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(NORTH, getEnergyConnection(world, pos, EnumFacing.NORTH)).withProperty(SOUTH, getEnergyConnection(world, pos, EnumFacing.SOUTH)).withProperty(WEST, getEnergyConnection(world, pos, EnumFacing.WEST)).withProperty(EAST, getEnergyConnection(world, pos, EnumFacing.EAST)).withProperty(UP, getEnergyConnection(world, pos, EnumFacing.UP)).withProperty(DOWN, getEnergyConnection(world, pos, EnumFacing.DOWN));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	public EnergyConnection getEnergyConnection(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		if (world.getTileEntity(pos) instanceof ITileEnergy) return ((ITileEnergy) world.getTileEntity(pos)).getEnergyConnection(facing);
		return EnergyConnection.NON;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) return false;
		
		if (player != null) {
			if (player.isSneaking() && world.getTileEntity(pos) instanceof ITileEnergy) {
				((ITileEnergy) world.getTileEntity(pos)).toggleEnergyConnection(facing);
			}
			else if (!world.isRemote && !player.isSneaking() && world.getTileEntity(pos) instanceof IBattery) {
				EnergyStorage storage = ((IBattery) world.getTileEntity(pos)).getBatteryStorage();
				player.sendMessage(new TextComponentString("Energy Stored: " + UnitHelper.prefix(storage.getEnergyStored(), storage.getMaxEnergyStored(), 5, "RF")));
			}
		}
		return true;
	}
	
	// NBT Stuff
	
	@Override
	public ItemStack getNBTDrop(IBlockAccess world, BlockPos pos, IBlockState state) {
		ItemStack stack = new ItemStack(this);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITileEnergy) {
			NBTTagCompound nbt = new NBTTagCompound();
			((ITileEnergy) tile).writeEnergy(nbt);
			((ITileEnergy) tile).writeEnergyConnections(nbt);
			stack.setTagCompound(nbt);
		}
		return stack;
	}
	
	@Override
	public void readStackData(World world, BlockPos pos, EntityLivingBase player, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ITileEnergy) {
			((ITileEnergy) tile).readEnergy(stack.getTagCompound());
			if (player != null) if (player.isSneaking()) ((ITileEnergy) tile).readEnergyConnections(stack.getTagCompound());
		}
	}
}
