package nc.container.processor;

import nc.container.ContainerTile;
import nc.init.NCItems;
import nc.recipe.BasicRecipeHandler;
import nc.tile.ITileGui;
import nc.tile.inventory.ITileInventory;
import nc.tile.processor.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerFluidProcessor<PROCESSOR extends IFluidProcessor & ITileGui<?>> extends ContainerTile<PROCESSOR> {
	
	protected final PROCESSOR tile;
	protected final BasicRecipeHandler recipeHandler;
	
	protected static final ItemStack SPEED_UPGRADE = new ItemStack(NCItems.upgrade, 1, 0);
	protected static final ItemStack ENERGY_UPGRADE = new ItemStack(NCItems.upgrade, 1, 1);
	
	public ContainerFluidProcessor(EntityPlayer player, PROCESSOR tileEntity, BasicRecipeHandler recipeHandler) {
		super(tileEntity);
		tile = tileEntity;
		this.recipeHandler = recipeHandler;
		
		tileEntity.addTileUpdatePacketListener(player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.removeTileUpdatePacketListener(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		final boolean hasUpgrades = tile instanceof IUpgradable && ((IUpgradable) tile).hasUpgrades();
		int upgrades = hasUpgrades ? ((IUpgradable) tile).getNumberOfUpgrades() : 0;
		int invStart = upgrades;
		int speedUpgradeSlot = 0;
		int otherUpgradeSlot = 1;
		int invEnd = 36 + upgrades;
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= 0 && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= invStart) {
				if (tile instanceof ITileInventory && hasUpgrades && itemstack1.getItem() == NCItems.upgrade) {
					if (((ITileInventory) tile).isItemValidForSlot(speedUpgradeSlot, itemstack1)) {
						if (!mergeItemStack(itemstack1, speedUpgradeSlot, speedUpgradeSlot + 1, false)) {
							return ItemStack.EMPTY;
						}
					}
					else if (((ITileInventory) tile).isItemValidForSlot(otherUpgradeSlot, itemstack1)) {
						if (!mergeItemStack(itemstack1, otherUpgradeSlot, otherUpgradeSlot + 1, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
				if (recipeHandler.isValidItemInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, 0, false)) {
						return ItemStack.EMPTY;
					}
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
