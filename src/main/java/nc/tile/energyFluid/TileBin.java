package nc.tile.energyFluid;

import java.util.Arrays;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileBin extends TileEnergyFluidSidedInventory implements IInterfaceable {
	
	public TileBin() {
		super("bin", 4, 16777216, ITileEnergy.energyConnectionAll(EnergyConnection.IN), Arrays.asList(256000, 256000, 256000, 256000), null, ITileFluid.fluidConnectionAll(Arrays.asList(TankSorption.IN, TankSorption.IN, TankSorption.IN, TankSorption.IN)));
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			for (int i = 0; i < inventoryStacks.size(); i++) {
				if (inventoryStacks.get(i) != ItemStack.EMPTY) inventoryStacks.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : getTanks()) if (tank.getFluidAmount() > 0) tank.setFluid(null);
			if (getEnergyStorage().getEnergyStored() > 0) getEnergyStorage().setEnergyStored(0);
		}
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
	public int getEUSourceTier() {
		return 1;
	}

	@Override
	public int getEUSinkTier() {
		return 10;
	}
}
