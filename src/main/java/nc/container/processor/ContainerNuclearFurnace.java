package nc.container.processor;

import nc.container.ContainerTile;
import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotNuclearFuel;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerNuclearFurnace extends ContainerTile<TileNuclearFurnace> {
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;
	
	public ContainerNuclearFurnace(EntityPlayer player, TileNuclearFurnace tile) {
		super(tile);
		addSlotToContainer(new Slot(tile.getInventory(), 0, 56, 17));
		addSlotToContainer(new SlotNuclearFuel(tile, 1, 56, 53));
		addSlotToContainer(new SlotFurnace(player, tile, 2, 116, 35));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142));
		}
	}
	
	/** Looks for changes made in the container, sends them to every listener */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = listeners.get(i);
			
			if (furnaceBurnTime != invWrapper.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, invWrapper.getField(0));
			}
			
			if (currentItemBurnTime != invWrapper.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, invWrapper.getField(1));
			}
			
			if (cookTime != invWrapper.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, invWrapper.getField(2));
			}
			
			if (totalCookTime != invWrapper.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, invWrapper.getField(3));
			}
		}
		
		furnaceBurnTime = invWrapper.getField(0);
		currentItemBurnTime = invWrapper.getField(1);
		cookTime = invWrapper.getField(2);
		totalCookTime = invWrapper.getField(3);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, false)) {
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty()) {
					if (!mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (TileNuclearFurnace.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!mergeItemStack(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
}
