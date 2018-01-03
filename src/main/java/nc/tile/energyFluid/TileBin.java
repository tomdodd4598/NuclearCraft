package nc.tile.energyFluid;

import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.tile.dummy.IInterfaceable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileBin extends TileEnergyFluidSidedInventory implements IInterfaceable {
	
	public TileBin() {
		super("bin", 4, 16777216, EnergyConnection.IN, new int[] {256000, 256000, 256000, 256000}, new FluidConnection[] {FluidConnection.IN, FluidConnection.IN, FluidConnection.IN, FluidConnection.IN});
	}
	
	@Override
	public void update() {
		super.update();
		for (int i = 0; i < inventoryStacks.size(); i++) {
			if (inventoryStacks.get(i) != ItemStack.EMPTY) inventoryStacks.set(i, ItemStack.EMPTY);
		}
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].getFluidAmount() > 0) tanks[i].setFluid(null);
		}
		if (storage.getEnergyStored() > 0) storage.setEnergyStored(0);
	}
	
	// Sided Inventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0, 1, 2, 3};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
	}
	
	// IC2 EU

	@Override
	public int getSourceTier() {
		return 1;
	}

	@Override
	public int getSinkTier() {
		return 4;
	}
}
