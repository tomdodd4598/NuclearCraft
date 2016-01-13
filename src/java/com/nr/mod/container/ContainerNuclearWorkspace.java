package com.nr.mod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.nr.mod.blocks.tileentities.TileEntityNuclearWorkspace;
import com.nr.mod.crafting.NuclearWorkspaceCraftingManager;

public class ContainerNuclearWorkspace extends Container {

	public InventoryCraftingKeep craftMatrix;
	public InventoryCraftResult craftResult;
	private World worldObj;
	
	public ContainerNuclearWorkspace(InventoryPlayer invPlayer, TileEntityNuclearWorkspace entity) {
		craftMatrix = new InventoryCraftingKeep(this, entity, 5, 5);
		craftResult = new InventoryCraftResult();
		
		this.addSlotToContainer(new SlotCrafting(invPlayer.player, craftMatrix, craftResult, 0, 140, 44));
		
		for (int i = 0; i < 5; i++) {
			for (int k = 0; k < 5; k++) {
				this.addSlotToContainer(new Slot(craftMatrix, k + i * 5, 8 + k * 18, 8 + i * 18));
			}
		}
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i*18, 160));
		}
		
		onCraftMatrixChanged(null);
	}

	public void onCraftMatrixChanged(IInventory iinventory) {
		craftResult.setInventorySlotContents(0, NuclearWorkspaceCraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}
	
	public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }
	
	/*public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);

        if (!this.worldObj.isRemote)
        {
            for (int i = 0; i < 25; ++i)
            {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }*/
	
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ == 0)
            {
                if (!this.mergeItemStack(itemstack1, 26, 62, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (p_82846_2_ >= 26 && p_82846_2_ < 53)
            {
                if (!this.mergeItemStack(itemstack1, 53, 62, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 53 && p_82846_2_ < 62)
            {
                if (!this.mergeItemStack(itemstack1, 26, 53, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 26, 62, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }
}
