package nc.container.processor;

import nc.container.ContainerTile;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMachineConfig<T extends ITileGui> extends ContainerTile<T> {
	
	public ContainerMachineConfig(EntityPlayer player, T tile) {
		super(tile);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + tile.getSideConfigYOffset() + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142 + tile.getSideConfigYOffset()));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < 0) {
				if (!mergeItemStack(itemstack1, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= 0) {
				if (index >= 0 && index < 27) {
					if (!mergeItemStack(itemstack1, 27, 36, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 27 && index < 36 && !mergeItemStack(itemstack1, 0, 27, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, 0, 36, false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
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
