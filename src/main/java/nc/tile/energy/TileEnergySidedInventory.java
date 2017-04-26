package nc.tile.energy;

import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class TileEnergySidedInventory extends TileEnergyInventory implements ISidedInventory {

	public int[] topSlots;
	public int[] sideSlots;
	public int[] bottomSlots;
	
	public TileEnergySidedInventory(String name, int size, int capacity, EnergyConnection connection) {
		super(name, size, capacity, connection);
	}
	
	public TileEnergySidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection) {
		super(name, size, capacity, maxTransfer, connection);
	}
			
	public TileEnergySidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection) {
		super(name, size, capacity, maxReceive, maxExtract, connection);
	}
	
	// SidedInventory

	public abstract int[] getSlotsForFace(EnumFacing side);

	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability) {
			return (T) storage;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return super.getCapability(capability, facing);
	}
}
