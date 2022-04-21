package nc.tile;

import static nc.config.NCConfig.*;

import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import ic2.api.energy.event.*;
import ic2.api.energy.tile.*;
import nc.*;
import nc.tile.dummy.IInterfaceable;
import nc.tile.internal.energy.*;
import nc.tile.internal.fluid.TankVoid;
import nc.util.CapabilityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2")
public class TileBin extends NCTile implements IInventory, IEnergySink, IInterfaceable {
	
	private final EnergyStorageVoid energyStorage = new EnergyStorageVoid();
	private final EnergyStorageVoidGT energyStorageGT = new EnergyStorageVoidGT();
	private final TankVoid tank = new TankVoid();
	
	private boolean ic2reg = false;
	
	public TileBin() {
		super();
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (ModCheck.ic2Loaded()) {
			addTileToENet();
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
	}
	
	@Optional.Method(modid = "ic2")
	public void addTileToENet() {
		if (!world.isRemote && enable_ic2_eu && !ic2reg) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			ic2reg = true;
		}
	}
	
	@Optional.Method(modid = "ic2")
	public void removeTileFromENet() {
		if (!world.isRemote && ic2reg) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			ic2reg = false;
		}
	}
	
	@Override
	public String getName() {
		return Global.MOD_ID + ".container.bin";
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return true;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return Double.MAX_VALUE;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		return 0D;
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		
	}
	
	@Override
	public int getInventoryStackLimit() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	@Override
	public int getField(int id) {
		return 0;
	}
	
	@Override
	public void setField(int id, int value) {
		
	}
	
	@Override
	public int getFieldCount() {
		return 0;
	}
	
	@Override
	public void clear() {
		
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			return true;
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new InvWrapper(this));
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(tank);
			}
			return null;
		}
		else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (enable_gtce_eu) {
				return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(energyStorageGT);
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
