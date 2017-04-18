package nc.container.crafting;

import nc.tile.crafting.TileNuclearWorkspace;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class HoldCrafting extends InventoryCrafting {

    private TileNuclearWorkspace craft;
    private Container container;

    public HoldCrafting(Container cont, TileNuclearWorkspace table){
        super(cont, 5, 5);
        craft = table;
        container = cont;
    }

    public ItemStack getStackInSlot (int slot) {
        return slot >= this.getSizeInventory() ? null : craft.getStackInSlot(slot+1);
    }

    public ItemStack getStackInRowAndColumn (int row, int column) {
        if (row >= 0 && row < 5) {
            int x = row + column * 5;
            return this.getStackInSlot(x);
        } else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing (int par1) {
        return null;
    }

    public ItemStack decrStackSize (int slot, int decrement) {
        ItemStack stack = craft.getStackInSlot(slot + 1);
        if (stack != null) {
            ItemStack itemstack;
            if (stack.stackSize <= decrement) {
                itemstack = stack.copy();
                stack = null;
                craft.setInventorySlotContents(slot + 1, null);
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.splitStack(decrement);
                if (stack.stackSize == 0) {
                    stack = null;
                }
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setInventorySlotContents (int slot, ItemStack itemstack) {
        craft.setInventorySlotContents(slot + 1, itemstack);
        this.container.onCraftMatrixChanged(this);
    }

}