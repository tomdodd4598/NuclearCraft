package nc.block.tile;

import nc.block.property.ISidedEnergy;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.battery.IBattery;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBattery extends BlockSimpleTile implements ISidedEnergy, INBTDrop {
	
	public BlockBattery(SimpleTileType type) {
		super(type);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return createEnergyBlockState(this);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getActualEnergyState(state, world, pos);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) return false;
		
		TileEntity tile = world.getTileEntity(pos);
		if (player != null && tile instanceof ITileEnergy) {
			ITileEnergy battery = (ITileEnergy) tile;
			if (player.isSneaking()) {
				battery.toggleEnergyConnection(facing, EnergyConnection.Type.DEFAULT);
			}
			else if (!world.isRemote && !player.isSneaking()) {
				EnergyStorage storage = battery.getEnergyStorage();
				player.sendMessage(new TextComponentString(Lang.localise("gui.container.energy_stored") + " " + UnitHelper.prefix(storage.getEnergyStored(), storage.getMaxEnergyStored(), 5, "RF")));
			}
		}
		return true;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IBattery) {
			return ((IBattery)tile).getComparatorStrength();
		}
		return Container.calcRedstone(world.getTileEntity(pos));
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
