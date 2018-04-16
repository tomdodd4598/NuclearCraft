package nc.tile.energyFluid;

import nc.tile.dummy.IInterfaceable;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileBin extends TileEnergyFluidSidedInventory implements IInterfaceable {
	
	public TileBin() {
		super("bin", 4, 16777216, energyConnectionAll(EnergyConnection.IN), new int[] {256000, 256000, 256000, 256000}, new FluidConnection[] {FluidConnection.IN, FluidConnection.IN, FluidConnection.IN, FluidConnection.IN});
	}
	
	@Override
	public void update() {
		super.update();
		for (int i = 0; i < inventoryStacks.size(); i++) {
			if (inventoryStacks.get(i) != ItemStack.EMPTY) inventoryStacks.set(i, ItemStack.EMPTY);
		}
		for (int i = 0; i < tanks.length; i++) {
			tanks[i].setStrictlyInput(true);
			if (tanks[i].getFluidAmount() > 0) tanks[i].setFluid(null);
		}
		if (getEnergyStorage().getEnergyStored() > 0) getEnergyStorage().setEnergyStored(0);
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
