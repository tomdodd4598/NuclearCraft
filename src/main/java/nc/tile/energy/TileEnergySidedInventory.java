package nc.tile.energy;

import javax.annotation.Nullable;

import nc.tile.internal.energy.EnergyConnection;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileEnergySidedInventory extends TileEnergyInventory implements ISidedInventory {
	
	public TileEnergySidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections) {
		super(name, size, capacity, energyConnections);
	}
	
	public TileEnergySidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections) {
		super(name, size, capacity, maxTransfer, energyConnections);
	}
			
	public TileEnergySidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections);
	}
	
	// SidedInventory

	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) return (T) handlerBottom;
			else if (facing == EnumFacing.UP) return (T) handlerTop;
			else return (T) handlerSide;
		}
		return super.getCapability(capability, facing);
	}
}
