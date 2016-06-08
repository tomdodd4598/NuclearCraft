package nc.container.generator;

import nc.tile.generator.TileReactionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerReactionGenerator extends Container
{
    private TileReactionGenerator entity;
    public int lastEnergy;
    public int lastReactant;
    public int lastFuel;

    public ContainerReactionGenerator(InventoryPlayer inventory, TileReactionGenerator entity)
    {
        this.entity = entity;
        
        this.addSlotToContainer(new Slot(entity, 0, 56, 26));
        this.addSlotToContainer(new Slot(entity, 1, 104, 26));
        

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            
            icrafting.sendProgressBarUpdate(this, 1, this.entity.energy);
            icrafting.sendProgressBarUpdate(this, 2, this.entity.reactantlevel);
            icrafting.sendProgressBarUpdate(this, 3, this.entity.fuellevel);

            icrafting.sendProgressBarUpdate(this, 4, this.entity.energy >> 16);
            icrafting.sendProgressBarUpdate(this, 5, this.entity.reactantlevel >> 16);
            icrafting.sendProgressBarUpdate(this, 6, this.entity.fuellevel >> 16);
        }        
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int value)
    {
        if (slot == 1){ this.lastEnergy = this.upcastShort(value); }
        if (slot == 2){ this.lastReactant = this.upcastShort(value); }
        if (slot == 3){ this.lastFuel = this.upcastShort(value); }
        
        if (slot == 4){ this.entity.energy = this.lastEnergy | value << 16; }
        if (slot == 5){ this.entity.reactantlevel = this.lastReactant | value << 16; }
        if (slot == 6){ this.entity.fuellevel = this.lastFuel | value << 16; }
    }
    
    private int upcastShort(int input)
    {
      if (input < 0) input += 65536;
      return input;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(clickedSlotNumber);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (clickedSlotNumber != 1 && clickedSlotNumber != 0)
            {
                if (TileReactionGenerator.isFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileReactionGenerator.isReactant(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (clickedSlotNumber >= 2 && clickedSlotNumber < 29)
                {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false))
                    {
                        return null;
                    }
                }
                else if (clickedSlotNumber >= 29 && clickedSlotNumber < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 2, 38, false))
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

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    public int dischargeValue(Item item)
    {
        return 0;
    }
}
