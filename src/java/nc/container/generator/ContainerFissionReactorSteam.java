package nc.container.generator;

import nc.tile.generator.TileFissionReactorSteam;
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

public class ContainerFissionReactorSteam extends Container {
    private TileFissionReactorSteam entity;
    public int lastFluid;
    public int lastheat;
    public int lastFueltime;
    public int lastS;
    public int lastH;
    public int lastOff;
    public int lastFuelType;
    public int lastSReal;
    public int lastHReal;
    public int lastHCooling;
    public int lastFReal;
    public int lastComplete;
    public int lastEfficiency;
    public int lastNumberOfCells;
    public int lastMinusH;

    public int lastlx;
    public int lastly;
    public int lastlz;

    public ContainerFissionReactorSteam(InventoryPlayer inventory, TileFissionReactorSteam entity) {
        this.entity = entity;
        
        this.addSlotToContainer(new Slot(entity, 0, 68, 71));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 1, 148, 71));
        

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 95 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 153));
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

            icrafting.sendProgressBarUpdate(this, 1, this.entity.tank.getFluidAmount());
            icrafting.sendProgressBarUpdate(this, 2, this.entity.heat);
            icrafting.sendProgressBarUpdate(this, 3, this.entity.fueltime);
            
            icrafting.sendProgressBarUpdate(this, 4, this.entity.tank.getFluidAmount() >> 16);
            icrafting.sendProgressBarUpdate(this, 5, this.entity.heat >> 16);
            icrafting.sendProgressBarUpdate(this, 6, this.entity.fueltime >> 16);
            
            icrafting.sendProgressBarUpdate(this, 7, this.entity.S);
            icrafting.sendProgressBarUpdate(this, 8, this.entity.S >> 16);
            icrafting.sendProgressBarUpdate(this, 9, this.entity.H);
            icrafting.sendProgressBarUpdate(this, 10, this.entity.H >> 16);
            
            icrafting.sendProgressBarUpdate(this, 11, this.entity.off);
            icrafting.sendProgressBarUpdate(this, 12, this.entity.off >> 16);
            
            icrafting.sendProgressBarUpdate(this, 13, this.entity.fueltype);
            icrafting.sendProgressBarUpdate(this, 14, this.entity.fueltype >> 16);
            
            icrafting.sendProgressBarUpdate(this, 15, this.entity.SReal);
            icrafting.sendProgressBarUpdate(this, 16, this.entity.SReal >> 16);
            icrafting.sendProgressBarUpdate(this, 17, this.entity.HReal);
            icrafting.sendProgressBarUpdate(this, 18, this.entity.HReal >> 16);
            icrafting.sendProgressBarUpdate(this, 19, this.entity.FReal);
            icrafting.sendProgressBarUpdate(this, 20, this.entity.FReal >> 16);
            
            icrafting.sendProgressBarUpdate(this, 21, this.entity.complete);
            icrafting.sendProgressBarUpdate(this, 22, this.entity.complete >> 16);
            
            icrafting.sendProgressBarUpdate(this, 23, this.entity.lx);
            icrafting.sendProgressBarUpdate(this, 24, this.entity.lx >> 16);
            icrafting.sendProgressBarUpdate(this, 25, this.entity.ly);
            icrafting.sendProgressBarUpdate(this, 26, this.entity.ly >> 16);
            icrafting.sendProgressBarUpdate(this, 27, this.entity.lz);
            icrafting.sendProgressBarUpdate(this, 28, this.entity.lz >> 16);
            
            icrafting.sendProgressBarUpdate(this, 29, this.entity.efficiency);
            icrafting.sendProgressBarUpdate(this, 30, this.entity.efficiency >> 16);
            
            icrafting.sendProgressBarUpdate(this, 31, this.entity.numberOfCells);
            icrafting.sendProgressBarUpdate(this, 32, this.entity.numberOfCells >> 16);
            
            icrafting.sendProgressBarUpdate(this, 33, this.entity.HCooling);
            icrafting.sendProgressBarUpdate(this, 34, this.entity.HCooling >> 16);
            
            icrafting.sendProgressBarUpdate(this, 35, this.entity.MinusH);
            icrafting.sendProgressBarUpdate(this, 36, this.entity.MinusH >> 16);
        }    
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int value) {
        if (slot == 1){ this.lastFluid = this.upcastShort(value); }
        if (slot == 2){ this.lastheat = this.upcastShort(value); }
        if (slot == 3){ this.lastFueltime = this.upcastShort(value); }
        
        if (slot == 4){ this.entity.fluid = this.lastFluid | value << 16; }
        if (slot == 5){ this.entity.heat = this.lastheat | value << 16; }
        if (slot == 6){ this.entity.fueltime = this.lastFueltime | value << 16; }
        
        if (slot == 7){ this.lastS = this.upcastShort(value); }
        if (slot == 8){ this.entity.S = this.lastS | value << 16; }
        if (slot == 9){ this.lastH = this.upcastShort(value); }
        if (slot == 10){ this.entity.H = this.lastH | value << 16; }
        
        if (slot == 11){ this.lastOff = this.upcastShort(value); }
        if (slot == 12){ this.entity.off = this.lastOff | value << 16; }
        
        if (slot == 13){ this.lastFuelType = this.upcastShort(value); }
        if (slot == 14){ this.entity.fueltype = this.lastFuelType | value << 16; }
        
        if (slot == 15){ this.lastSReal = this.upcastShort(value); }
        if (slot == 16){ this.entity.SReal = this.lastSReal | value << 16; }
        if (slot == 17){ this.lastHReal = this.upcastShort(value); }
        if (slot == 18){ this.entity.HReal = this.lastHReal | value << 16; }
        if (slot == 19){ this.lastFReal = this.upcastShort(value); }
        if (slot == 20){ this.entity.FReal = this.lastFReal | value << 16; }
        
        if (slot == 21){ this.lastComplete = this.upcastShort(value); }
        if (slot == 22){ this.entity.complete = this.lastComplete | value << 16; }
        
        if (slot == 23){ this.lastlx = this.upcastShort(value); }
        if (slot == 24){ this.entity.lx = this.lastlx | value << 16; }
        if (slot == 25){ this.lastly = this.upcastShort(value); }
        if (slot == 26){ this.entity.ly = this.lastly | value << 16; }
        if (slot == 27){ this.lastlz = this.upcastShort(value); }
        if (slot == 28){ this.entity.lz = this.lastlz | value << 16; }
        
        if (slot == 29){ this.lastEfficiency = this.upcastShort(value); }
        if (slot == 30){ this.entity.efficiency = this.lastEfficiency | value << 16; }
        
        if (slot == 31){ this.lastNumberOfCells = this.upcastShort(value); }
        if (slot == 32){ this.entity.numberOfCells = this.lastNumberOfCells | value << 16; }
        
        if (slot == 33){ this.lastHCooling = this.upcastShort(value); }
        if (slot == 34){ this.entity.HCooling = this.lastHCooling | value << 16; }
        
        if (slot == 35){ this.lastMinusH = this.upcastShort(value); }
        if (slot == 36){ this.entity.MinusH = this.lastMinusH | value << 16; }
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
                if (TileFissionReactorSteam.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (clickedSlotNumber >= 2 && clickedSlotNumber < 29) {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                        return null;
                    }
                } else if (clickedSlotNumber >= 29 && clickedSlotNumber < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
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