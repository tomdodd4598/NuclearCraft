package nc.tile.energyFluid;

import javax.annotation.Nonnull;

import nc.config.NCConfig;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.IFluidSpread;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.NCInventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileBuffer extends TileEnergyFluidSidedInventory implements IInterfaceable, IEnergySpread, IFluidSpread {
	
	private int pushCount;
	
	public TileBuffer() {
		super("buffer", 1, ITileInventory.inventoryConnectionAll(ItemSorption.BOTH), 64000, ITileEnergy.energyConnectionAll(EnergyConnection.BOTH), 32000, null, ITileFluid.fluidConnectionAll(TankSorption.BOTH));
	}
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			if (pushCount == 0) {
				pushStacks();
				pushEnergy();
				pushFluid();
			}
			tickPush();
		}
	}
	
	public void tickPush() {
		pushCount++; pushCount %= NCConfig.machine_update_rate;
	}
	
	// Item and Fluid Pushing
	
	@Override
	public void pushStacksToSide(@Nonnull EnumFacing side) {
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
			
			if (getInventoryStacks().get(i).getCount() <= 0) {
				getInventoryStacks().set(i, ItemStack.EMPTY);
			}
		}
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).getTankSorption(0).canDrain()) return;
		
		TileEntity tile = world.getTileEntity(getPos().offset(side));
		if (!(tile instanceof IBufferable)) return;
		
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) return;
		
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).getFluid() == null || !getTankSorption(side, i).canDrain()) return;
			
			getTanks().get(i).drainInternal(adjStorage.fill(getTanks().get(i).drainInternal(getTanks().get(i).getCapacity(), false), true), true);
		}
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
