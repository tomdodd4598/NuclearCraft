package nc.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileInventory extends TileEntity implements IInventory
{
    public String localizedName;
    public ItemStack[] slots;

    public void setGuiDisplayName(String displayName)
    {
        this.localizedName = displayName;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.isInventoryNameLocalized() ? this.localizedName : "NC Tile Entity";
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return this.localizedName != null && this.localizedName.length() > 0;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.slots.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.slots[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.slots[var1] != null)
        {
            ItemStack itemstack;

            if (this.slots[var1].stackSize <= var2)
            {
                itemstack = this.slots[var1];
                this.slots[var1] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.slots[var1].splitStack(var2);

                if (this.slots[var1].stackSize == 0)
                {
                    this.slots[var1] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.slots[i] != null)
        {
            ItemStack itemstack = this.slots[i];
            this.slots[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.slots[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        } markDirty();
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer) 
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return true;
    }

	@Override
	public boolean hasCustomInventoryName() {
		
		return false;
	}
}
