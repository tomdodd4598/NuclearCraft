package nc.tile.energyFluid;

import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.fluid.IFluidSpread;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileBuffer extends TileEnergyFluidSidedInventory implements IInterfaceable, IEnergySpread, IFluidSpread {
	
	public TileBuffer() {
		super("buffer", 1, 32000, EnergyConnection.BOTH, 16000, FluidConnection.BOTH);
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushStacks();
			pushEnergy();
			pushFluid();
		}
	}
	
	// Sided Inventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return true;
	}
	
	// IC2 EU

	@Override
	public int getSourceTier() {
		return 2;
	}

	@Override
	public int getSinkTier() {
		return 4;
	}
}
