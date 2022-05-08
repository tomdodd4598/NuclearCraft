package nc.multiblock.battery.block;

import nc.block.property.ISidedEnergy;
import nc.block.tile.*;
import nc.item.ItemMultitool;
import nc.multiblock.battery.*;
import nc.multiblock.battery.tile.TileBattery;
import nc.multiblock.block.BlockMultiblockPart;
import nc.tab.NCTabs;
import nc.tile.internal.energy.*;
import nc.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;

public class BlockBattery extends BlockMultiblockPart implements IDynamicState, ISidedEnergy, INBTDrop {
	
	private final IBatteryBlockType type;
	
	public BlockBattery(IBatteryBlockType type) {
		super(Material.IRON, NCTabs.multiblock());
		this.type = type;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return createEnergyBlockState(this);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return type.getTile();
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
		if (player == null || hand != EnumHand.MAIN_HAND) {
			return false;
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileBattery) {
			TileBattery battery = (TileBattery) tile;
			if (ItemMultitool.isMultitool(player.getHeldItem(hand))) {
				EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
				battery.toggleEnergyConnection(side, EnergyConnection.Type.DEFAULT);
			}
			else if (!world.isRemote) {
				EnergyStorage storage = battery.getEnergyStorage();
				player.sendMessage(new TextComponentString(Lang.localise("gui.nc.container.energy_stored") + " " + UnitHelper.prefix(storage.getEnergyStoredLong(), storage.getMaxEnergyStoredLong(), 5, "RF")));
			}
			return true;
		}
		
		return rightClickOnPart(world, pos, player, hand, facing, false);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileBattery) {
			BatteryMultiblock multiblock = ((TileBattery) tile).getMultiblock();
			if (multiblock != null) {
				return multiblock.getComparatorStrength();
			}
		}
		return 0;
	}
	
	// NBT Stuff
	
	@Override
	public ItemStack getNBTDrop(IBlockAccess world, BlockPos pos, IBlockState state) {
		ItemStack stack = new ItemStack(this);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileBattery) {
			NBTTagCompound nbt = new NBTTagCompound();
			
			TileBattery battery = (TileBattery) tile;
			BatteryMultiblock multiblock = battery.getMultiblock();
			
			if (multiblock != null) {
				EnergyStorage storage = multiblock.getEnergyStorage();
				if (multiblock.getPartCount(TileBattery.class) < 2) {
					storage.writeToNBT(nbt, "energyStorage");
				}
				else {
					double fraction = (double) multiblock.getEnergyStorage().getEnergyStoredLong() / (double) multiblock.getEnergyStorage().getMaxEnergyStoredLong();
					long energy = (long) (fraction * battery.capacity);
					new EnergyStorage(battery.capacity, NCMath.toInt(battery.capacity), energy).writeToNBT(nbt, "energyStorage");
					storage.changeEnergyStored(-energy);
				}
			}
			
			battery.writeEnergyConnections(nbt);
			stack.setTagCompound(nbt);
		}
		return stack;
	}
	
	@Override
	public void readStackData(World world, BlockPos pos, EntityLivingBase player, ItemStack stack) {
		if (player == null || !stack.hasTagCompound()) {
			return;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileBattery) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(stack);
			TileBattery battery = (TileBattery) tile;
			
			battery.waitingEnergy += new EnergyStorage(battery.capacity, NCMath.toInt(battery.capacity)).readFromNBT(nbt, "energyStorage").getEnergyStoredLong();
			
			if (player.isSneaking()) {
				battery.readEnergyConnections(nbt);
			}
		}
	}
}
