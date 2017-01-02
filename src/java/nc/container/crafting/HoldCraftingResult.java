package nc.container.crafting;

import nc.tile.crafting.TileNuclearWorkspace;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class HoldCraftingResult extends InventoryCraftResult {

    private TileNuclearWorkspace craft;

    public HoldCraftingResult(TileNuclearWorkspace table){
        craft = table;
    }

    public ItemStack getStackInSlot (int par1) {
        return craft.getStackInSlot(0);
    }

    public ItemStack decrStackSize (int par1, int par2) {
        ItemStack stack = craft.getStackInSlot(0);
        if (stack != null) {
            ItemStack itemstack = stack;
            craft.setInventorySlotContents(0, null);
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing (int par1) {
        return null;
    }

    public void setInventorySlotContents (int par1, ItemStack par2ItemStack) {
        craft.setInventorySlotContents(0, par2ItemStack);
    }
}