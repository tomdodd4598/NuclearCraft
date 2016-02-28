package nc.container.generator;

import nc.tile.generator.TileFissionReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerFissionReactor extends Container {
    private TileFissionReactor entity;
    public int lastEnergy;
    public int lastheat;
    public int lastFueltime;
    public int lastE;
    public int lastH;
    public int lastOff;

    public ContainerFissionReactor(InventoryPlayer inventory, TileFissionReactor entity)
    {
        this.entity = entity;
        
        this.addSlotToContainer(new Slot(entity, 0, 68, 60));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 1, 148, 60));
        this.addSlotToContainer(new Slot(entity, 2, 152, 36));
        

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

            icrafting.sendProgressBarUpdate(this, 1, this.entity.storage.getEnergyStored());
            icrafting.sendProgressBarUpdate(this, 2, this.entity.heat);
            icrafting.sendProgressBarUpdate(this, 3, this.entity.fueltime);
            
            icrafting.sendProgressBarUpdate(this, 4, this.entity.storage.getEnergyStored() >> 16);
            icrafting.sendProgressBarUpdate(this, 5, this.entity.heat >> 16);
            icrafting.sendProgressBarUpdate(this, 6, this.entity.fueltime >> 16);
            
            icrafting.sendProgressBarUpdate(this, 7, this.entity.E);
            icrafting.sendProgressBarUpdate(this, 8, this.entity.E >> 16);
            icrafting.sendProgressBarUpdate(this, 9, this.entity.H);
            icrafting.sendProgressBarUpdate(this, 10, this.entity.H >> 16);
            
            icrafting.sendProgressBarUpdate(this, 11, this.entity.off);
            icrafting.sendProgressBarUpdate(this, 12, this.entity.off >> 16);
        }    
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int value)
    {
        if (slot == 1){ this.lastEnergy = this.upcastShort(value); }
        if (slot == 2){ this.lastheat = this.upcastShort(value); }
        if (slot == 3){ this.lastFueltime = this.upcastShort(value); }
        
        if (slot == 4){ this.entity.energy = this.lastEnergy | value << 16; }
        if (slot == 5){ this.entity.heat = this.lastheat | value << 16; }
        if (slot == 6){ this.entity.fueltime = this.lastFueltime | value << 16; }
        
        if (slot == 7){ this.lastE = this.upcastShort(value); }
        if (slot == 8){ this.entity.E = this.lastE | value << 16; }
        if (slot == 9){ this.lastH = this.upcastShort(value); }
        if (slot == 10){ this.entity.H = this.lastH | value << 16; }
        
        if (slot == 11){ this.lastOff = this.upcastShort(value); }
        if (slot == 12){ this.entity.off = this.lastOff | value << 16; }
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

            if (clickedSlotNumber != 2 && clickedSlotNumber != 1 && clickedSlotNumber != 0)
            {
                if (TileFissionReactor.isFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileFissionReactor.isUpgrade(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                        return null;
                    }
                }
                else if (clickedSlotNumber >= 3 && clickedSlotNumber < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (clickedSlotNumber >= 30 && clickedSlotNumber < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
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