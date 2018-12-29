package nc.tile.inventory;

import nc.tile.ITile;
import nc.util.NCInventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public interface ITileInventory extends ITile {
	
	public NonNullList<ItemStack> getInventoryStacks();
	
	public default <TILE extends TileEntity & IInventory> void pushStacks(TILE thisTile) {
		if (thisTile.isEmpty()) return;
		
		EnumFacing side = getCycledSide();
		
		IItemHandler inv = thisTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
		if (inv == null) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
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
}
