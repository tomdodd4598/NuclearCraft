package nc.container.accelerator;

import nc.tile.accelerator.TileSynchrotron;
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

public class ContainerSynchrotron extends Container {
    private TileSynchrotron entity;
    public int lastEnergy;
    public int lastLength;
    public int lastEfficiency;
    public int lastFuel;
    public int lastParticleEnergy;
    public int lastPercentageOn;
    public int lastRadiationPower;
    public int lastComplete;

    public int lastAntimatter;

    public ContainerSynchrotron(InventoryPlayer inventory, TileSynchrotron entity)
    {
        this.entity = entity;
        
        this.addSlotToContainer(new Slot(entity, 0, 48, 99));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 1, 100, 99));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 2, 152, 99));
        

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 121 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 179));
        }
    }

    public void addCraftingToCrafters(ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            icrafting.sendProgressBarUpdate(this, 1, this.entity.storage.getEnergyStored());
            icrafting.sendProgressBarUpdate(this, 2, this.entity.length);
            icrafting.sendProgressBarUpdate(this, 3, (int) this.entity.efficiency);
            icrafting.sendProgressBarUpdate(this, 4, (int) this.entity.fuel);
            icrafting.sendProgressBarUpdate(this, 5, (int) this.entity.particleEnergy);
            icrafting.sendProgressBarUpdate(this, 6, (int) this.entity.percentageOn);
            
            icrafting.sendProgressBarUpdate(this, 7, this.entity.storage.getEnergyStored() >> 16);
            icrafting.sendProgressBarUpdate(this, 8, this.entity.length >> 16);
            icrafting.sendProgressBarUpdate(this, 9, (int) this.entity.efficiency >> 16);
            icrafting.sendProgressBarUpdate(this, 10, (int) this.entity.fuel >> 16);
            icrafting.sendProgressBarUpdate(this, 11, (int) this.entity.particleEnergy >> 16);
            icrafting.sendProgressBarUpdate(this, 12, (int) this.entity.percentageOn >> 16);
            
            icrafting.sendProgressBarUpdate(this, 13, (int) this.entity.radiationPower);
            icrafting.sendProgressBarUpdate(this, 14, (int) this.entity.radiationPower >> 16);
            
            icrafting.sendProgressBarUpdate(this, 15, (int) this.entity.complete);
            icrafting.sendProgressBarUpdate(this, 16, (int) this.entity.complete >> 16);
            
            icrafting.sendProgressBarUpdate(this, 17, (int) this.entity.antimatter);
            icrafting.sendProgressBarUpdate(this, 18, (int) this.entity.antimatter >> 16);
        }    
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int value) {
        if (slot == 1){ this.lastEnergy = this.upcastShort(value); }
        if (slot == 2){ this.lastLength = this.upcastShort(value); }
        if (slot == 3){ this.lastEfficiency = this.upcastShort(value); }
        if (slot == 4){ this.lastFuel = this.upcastShort(value); }
        if (slot == 5){ this.lastParticleEnergy = this.upcastShort(value); }
        if (slot == 6){ this.lastPercentageOn = this.upcastShort(value); }
        
        if (slot == 7){ this.entity.energy = this.lastEnergy | value << 16; }
        if (slot == 8){ this.entity.length = this.lastLength | value << 16; }
        if (slot == 9){ this.entity.efficiency = this.lastEfficiency | value << 16; }
        if (slot == 10){ this.entity.fuel = this.lastFuel | value << 16; }
        if (slot == 11){ this.entity.particleEnergy = this.lastParticleEnergy | value << 16; }
        if (slot == 12){ this.entity.percentageOn = this.lastPercentageOn | value << 16; }
        
        if (slot == 13){ this.lastRadiationPower = this.upcastShort(value); }
        if (slot == 14){ this.entity.radiationPower = this.lastRadiationPower | value << 16; }
        
        if (slot == 15){ this.lastComplete = this.upcastShort(value); }
        if (slot == 16){ this.entity.complete = this.lastComplete | value << 16; }
        
        if (slot == 17){ this.lastAntimatter = this.upcastShort(value); }
        if (slot == 18){ this.entity.antimatter = this.lastAntimatter | value << 16; }
    }
    
    private int upcastShort(int input) {
      if (input < 0) input += 65536;
      return input;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(clickedSlotNumber);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (clickedSlotNumber != 1 && clickedSlotNumber != 0) {
                if (TileSynchrotron.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (clickedSlotNumber >= 3 && clickedSlotNumber < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (clickedSlotNumber >= 30 && clickedSlotNumber < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    public boolean canInteractWith(EntityPlayer var1) {
        return true;
    }

    public int dischargeValue(Item item) {
        return 0;
    }
}
