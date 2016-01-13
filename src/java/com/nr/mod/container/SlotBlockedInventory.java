package com.nr.mod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockedInventory extends Slot {
	@SuppressWarnings("unused")
  	private int field;
	
	public SlotBlockedInventory(IInventory iinv, int si, int x, int y) {
		super(iinv, si, x, y);
	}

	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	public ItemStack decrStackSize(int size) {
		if (getHasStack()) {
			this.field += Math.min(size, getStack().stackSize);
    	}
		return super.decrStackSize(size);
	}
  
	public void onPickupFromSlot(EntityPlayer pl, ItemStack st) {
		onCrafting(st);
		super.onPickupFromSlot(pl, st);
	}

	protected void onCrafting(ItemStack st, int int1) {
		this.field += int1;
		onCrafting(st);
	}
}