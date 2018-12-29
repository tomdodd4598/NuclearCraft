package nc.tile.energyFluid;

import javax.annotation.Nonnull;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.IFluidSpread;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.TankSorption;
import nc.util.NCInventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileBuffer extends TileEnergyFluidSidedInventory implements IInterfaceable, IEnergySpread, IFluidSpread {
	
	public TileBuffer() {
		super("buffer", 1, 64000, ITileEnergy.energyConnectionAll(EnergyConnection.BOTH), 32000, TankSorption.BOTH, null, ITileFluid.fluidConnectionAll(FluidConnection.BOTH));
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushStacks(this);
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
	public <TILE extends TileEntity & IInventory> void pushStacks(TILE thisTile) {
		if (thisTile.isEmpty()) return;
		
		EnumFacing side = getCycledSide();
		
		IItemHandler inv = thisTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
		if (inv == null) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (!(tile instanceof IBufferable)) return;
		IItemHandler adjInv = tile == null ? null : tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
		if (adjInv == null || adjInv.getSlots() < 1) return;
		
		for (int i = 0; i < getInventoryStacks().size(); i++) {
			if (getInventoryStacks().get(i).isEmpty()) continue;
			
			ItemStack initialStack = getInventoryStacks().get(i).copy();
			ItemStack inserted = NCInventoryHelper.addStackToInventory(adjInv, initialStack);
			
			if (inserted.getCount() >= initialStack.getCount()) continue;
			
			getInventoryStacks().get(i).shrink(initialStack.getCount() - inserted.getCount());
			if (getInventoryStacks().get(i).getCount() <= 0) getInventoryStacks().set(i, ItemStack.EMPTY);
		}
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canDrain()) return;
		
		TileEntity tile = world.getTileEntity(getPos().offset(side));
		if (tile == null) return;
		
		if (!(tile instanceof IBufferable)) return;
		
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) return;
		
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).getFluid() == null || !getTanks().get(i).canDrain()) return;
			
			getTanks().get(i).drainInternal(adjStorage.fill(getTanks().get(i).drainInternal(getTanks().get(i).getCapacity(), false), true), true);
		}
	}
	
	// IC2 EU

	@Override
	public int getEUSourceTier() {
		return 2;
	}

	@Override
	public int getEUSinkTier() {
		return 4;
	}
}
