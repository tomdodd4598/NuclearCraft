package nc.tile.energyFluid;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.fluid.IFluidSpread;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileBuffer extends TileEnergyFluidSidedInventory implements IInterfaceable, IEnergySpread, IFluidSpread {
	
	public TileBuffer() {
		super("buffer", 1, 32000, energyConnectionAll(EnergyConnection.BOTH), 16000, FluidConnection.BOTH, null);
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
	
	// Item and Fluid Pushing
	
	@Override
	public void pushStacks() {
		if (isEmpty()) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			IItemHandler inv = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			if (inv == null) continue;
			for (int i = 0; i < inventoryStacks.size(); i++) {
				if (inventoryStacks.get(i).isEmpty()) continue;
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				IItemHandler adjInv = tile == null ? null : tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
				
				if (!(tile instanceof IBufferable)) continue;
				
				if (adjInv != null) {
					for (int j = 0; j < adjInv.getSlots(); j++) {
						if (!inv.extractItem(i, inventoryStacks.get(i).getCount() - adjInv.insertItem(j, inv.extractItem(i, getInventoryStackLimit(), true), false).getCount(), false).isEmpty()) {
							return;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void pushFluid() {
		if (tanks != null && !tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			if (tanks.get(i).getFluidAmount() <= 0 || !fluidConnections.get(i).canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (!(tile instanceof IBufferable)) continue;
				
				if (tile instanceof IFluidHandler) {
					tanks.get(i).drain(((IFluidHandler) tile).fill(tanks.get(i).drain(tanks.get(i).getCapacity(), false), true), true);
				}
				else if (adjStorage != null) {
					tanks.get(i).drain(adjStorage.fill(tanks.get(i).drain(tanks.get(i).getCapacity(), false), true), true);
				}
			}
		}
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
