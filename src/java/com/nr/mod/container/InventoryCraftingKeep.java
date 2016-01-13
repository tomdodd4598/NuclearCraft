package com.nr.mod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCraftingKeep extends InventoryCrafting {

  private final Container eventHandler;
  private final IInventory parent;
  private int invWidth, invHeight, offset;

  public InventoryCraftingKeep(Container container, IInventory inv, int width, int height) {
    super(container, width, height);
    this.parent = inv;
    this.eventHandler = container;
    this.offset = 0;
    this.invWidth = width;
    this.invHeight = height;
  }
  
  public InventoryCraftingKeep(Container container, IInventory inv, int width, int height, int off) {
	    super(container, width, height);
	    this.parent = inv;
	    this.eventHandler = container;
	    this.offset = off;
	    this.invWidth = width;
	    this.invHeight = height;
	  }

  public int getSizeInventory() {
    return invWidth*invHeight;
  }

  public ItemStack getStackInSlot(int slot) {
    return slot >= this.getSizeInventory() ? null : parent.getStackInSlot(slot + 1 + offset);
  }
  
  public ItemStack getStackInRowAndColumn(int row, int column) {
		if (row >= 0 && row < this.invWidth) {
			int k = row + column * this.invWidth;
			return this.getStackInSlot(k);
		} else {
			return null;
		}
	}

  public ItemStack getStackInSlotOnClosing(int index) {
    return null;
  }
  
  public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

  public ItemStack decrStackSize(int slot, int size) {
		ItemStack stack = parent.getStackInSlot(slot + 1 + offset);
		if (stack != null) {
			ItemStack itemstack;

			if (stack.stackSize <= size) {
				itemstack = stack.copy();
				stack = null;
				parent.setInventorySlotContents(slot + 1 + offset, null);
				this.eventHandler.onCraftMatrixChanged(this);
				return itemstack;
			} else {
				itemstack = stack.splitStack(size);

				if (stack.stackSize == 0) {
					stack = null;
				}

				this.eventHandler.onCraftMatrixChanged(this);
				return itemstack;
			}
		} else {
			return null;
		}
	}

  public void setInventorySlotContents(int slot, ItemStack stack) {
    parent.setInventorySlotContents(slot + 1 + offset, stack);
    eventHandler.onCraftMatrixChanged(this);
  }
  
  public int getInventoryStackLimit() {
		return 64;
	}

  public void markDirty() {
    parent.markDirty();
    this.eventHandler.onCraftMatrixChanged(this);
  }
}