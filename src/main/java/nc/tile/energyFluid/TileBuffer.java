package nc.tile.energyFluid;

import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.tile.dummy.IInterfaceable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileBuffer extends TileEnergyFluidSidedInventory implements IInterfaceable {
	
	public TileBuffer() {
		super("buffer", 1, 32000, EnergyConnection.BOTH, new int[] {16000}, new FluidConnection[] {FluidConnection.BOTH});
	}
	
	// Sided Inventory
	
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return true;
	}

	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return true;
	}
	
	// IC2 EU

	public int getSourceTier() {
		return 2;
	}

	public int getSinkTier() {
		return 4;
	}
}
