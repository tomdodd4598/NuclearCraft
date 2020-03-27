package nc.multiblock.container;

import nc.container.ContainerTile;
import nc.container.slot.SlotFiltered;
import nc.container.slot.SlotFurnace;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import nc.recipe.NCRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFissionCellPort extends ContainerTile<TileFissionCellPort> {
	
	protected final TileFissionCellPort port;
	
	public ContainerFissionCellPort(EntityPlayer player, TileFissionCellPort port) {
		super(port);
		this.port = port;
		
		port.beginUpdatingPlayer(player);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(port, NCRecipes.solid_fission, 0, 44, 35));
		
		addSlotToContainer(new SlotFurnace(player, port, 1, 116, 35));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return port.isUsableByPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		port.stopUpdatingPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		int invStart = 2, invEnd = 38;
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= 1 && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= invStart) {
				if (NCRecipes.solid_fission.isValidItemInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				}
				else {
					if (port.canModifyFilter(0) && !port.getFilterStacks().get(0).isEmpty()) {
						port.getFilterStacks().set(0, ItemStack.EMPTY);
						port.onFilterChanged(0);
						inventorySlots.get(0).onSlotChanged();
						return ItemStack.EMPTY;
					}
					else if (index >= invStart && index < invEnd - 9) {
						if (!mergeItemStack(itemstack1, invEnd - 9, invEnd, false)) {
							return ItemStack.EMPTY;
						}
					}
					else if (index >= invEnd - 9 && index < invEnd && !mergeItemStack(itemstack1, invStart, invEnd - 9, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			else if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
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
