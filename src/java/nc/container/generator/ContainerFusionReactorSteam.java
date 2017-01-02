package nc.container.generator;

import nc.tile.generator.TileFusionReactorSteam;
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

public class ContainerFusionReactorSteam extends Container {
    private TileFusionReactorSteam entity;
    public int lastEnergy;
    public int lastHLevel;
    public int lastDLevel;
    public int lastTLevel;
    public int lastHeLevel;
    public int lastBLevel;
    public int lastLi6Level;
    public int lastLi7Level;
    public int lastHLevel2;
    public int lastDLevel2;
    public int lastTLevel2;
    public int lastHeLevel2;
    public int lastBLevel2;
    public int lastLi6Level2;
    public int lastLi7Level2;
    
    public int lastFluid;
    public int lastSteam;
    public int lastSteamType;
    
    public int lastHOut;
    public int lastDOut;
    public int lastTOut;
    public int lastHE3Out;
    public int lastHE4Out;
    public int lastnOut;
    
    public int lastSShown;
    public int lastSize;
    public int lastBelow;
    
    public int lastFlagNumber;
    
    public double lastHeatVar;
    public double lastHeat;
    public double lastEfficiency;
    
    public int lastComplete;

    public ContainerFusionReactorSteam(InventoryPlayer inventory, TileFusionReactorSteam entity) {
        this.entity = entity;
        
        this.addSlotToContainer(new Slot(entity, 0, 8, 153));
        this.addSlotToContainer(new Slot(entity, 1, 26, 153));
        this.addSlotToContainer(new Slot(entity, 2, 172, 153));
        
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 3, 196, 180));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 4, 220, 180));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 5, 172, 204));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 6, 196, 204));
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 7, 220, 204));
        
        this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 8, 172, 180));
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 173 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 231));
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
            icrafting.sendProgressBarUpdate(this, 2, this.entity.HLevel);
            icrafting.sendProgressBarUpdate(this, 3, this.entity.DLevel);
            icrafting.sendProgressBarUpdate(this, 4, this.entity.TLevel);
            icrafting.sendProgressBarUpdate(this, 5, this.entity.HeLevel);
            icrafting.sendProgressBarUpdate(this, 6, this.entity.BLevel);
            icrafting.sendProgressBarUpdate(this, 7, this.entity.Li6Level);
            icrafting.sendProgressBarUpdate(this, 8, this.entity.Li7Level);
            icrafting.sendProgressBarUpdate(this, 9, this.entity.HLevel2);
            icrafting.sendProgressBarUpdate(this, 10, this.entity.DLevel2);
            icrafting.sendProgressBarUpdate(this, 11, this.entity.TLevel2);
            icrafting.sendProgressBarUpdate(this, 12, this.entity.HeLevel2);
            icrafting.sendProgressBarUpdate(this, 13, this.entity.BLevel2);
            icrafting.sendProgressBarUpdate(this, 14, this.entity.Li6Level2);
            icrafting.sendProgressBarUpdate(this, 15, this.entity.Li7Level2);
            
            icrafting.sendProgressBarUpdate(this, 16, this.entity.storage.getEnergyStored() >> 16);
            icrafting.sendProgressBarUpdate(this, 17, this.entity.HLevel >> 16);
            icrafting.sendProgressBarUpdate(this, 18, this.entity.DLevel >> 16);
            icrafting.sendProgressBarUpdate(this, 19, this.entity.TLevel >> 16);
            icrafting.sendProgressBarUpdate(this, 20, this.entity.HeLevel >> 16);
            icrafting.sendProgressBarUpdate(this, 21, this.entity.BLevel >> 16);
            icrafting.sendProgressBarUpdate(this, 22, this.entity.Li6Level >> 16);
            icrafting.sendProgressBarUpdate(this, 23, this.entity.Li7Level >> 16);
            icrafting.sendProgressBarUpdate(this, 24, this.entity.HLevel2 >> 16);
            icrafting.sendProgressBarUpdate(this, 25, this.entity.DLevel2 >> 16);
            icrafting.sendProgressBarUpdate(this, 26, this.entity.TLevel2 >> 16);
            icrafting.sendProgressBarUpdate(this, 27, this.entity.HeLevel2 >> 16);
            icrafting.sendProgressBarUpdate(this, 28, this.entity.BLevel2 >> 16);
            icrafting.sendProgressBarUpdate(this, 29, this.entity.Li6Level2 >> 16);
            icrafting.sendProgressBarUpdate(this, 30, this.entity.Li7Level2 >> 16);
            
            icrafting.sendProgressBarUpdate(this, 31, this.entity.SShown);
            icrafting.sendProgressBarUpdate(this, 32, this.entity.SShown >> 16);
            icrafting.sendProgressBarUpdate(this, 33, this.entity.size);
            icrafting.sendProgressBarUpdate(this, 34, this.entity.size >> 16);
            
            icrafting.sendProgressBarUpdate(this, 37, (int) this.entity.efficiency);
            icrafting.sendProgressBarUpdate(this, 38, (int) this.entity.efficiency >> 16);
            icrafting.sendProgressBarUpdate(this, 39, (int) this.entity.heat);
            icrafting.sendProgressBarUpdate(this, 40, (int) this.entity.heat >> 16);
            icrafting.sendProgressBarUpdate(this, 41, (int) this.entity.heatVar);
            icrafting.sendProgressBarUpdate(this, 42, (int) this.entity.heatVar >> 16);
            
            icrafting.sendProgressBarUpdate(this, 43, (int) this.entity.HOut);
            icrafting.sendProgressBarUpdate(this, 44, (int) this.entity.HOut >> 16);
            icrafting.sendProgressBarUpdate(this, 45, (int) this.entity.DOut);
            icrafting.sendProgressBarUpdate(this, 46, (int) this.entity.DOut >> 16);
            icrafting.sendProgressBarUpdate(this, 47, (int) this.entity.TOut);
            icrafting.sendProgressBarUpdate(this, 48, (int) this.entity.TOut >> 16);
            icrafting.sendProgressBarUpdate(this, 49, (int) this.entity.HE3Out);
            icrafting.sendProgressBarUpdate(this, 50, (int) this.entity.HE3Out >> 16);
            icrafting.sendProgressBarUpdate(this, 51, (int) this.entity.HE4Out);
            icrafting.sendProgressBarUpdate(this, 52, (int) this.entity.HE4Out >> 16);
            icrafting.sendProgressBarUpdate(this, 53, (int) this.entity.nOut);
            icrafting.sendProgressBarUpdate(this, 54, (int) this.entity.nOut >> 16);
            
            icrafting.sendProgressBarUpdate(this, 55, this.entity.complete);
            icrafting.sendProgressBarUpdate(this, 56, this.entity.complete >> 16);
            
            icrafting.sendProgressBarUpdate(this, 57, this.entity.tank.getFluidAmount());
            icrafting.sendProgressBarUpdate(this, 58, this.entity.tank.getFluidAmount() >> 16);
            
            icrafting.sendProgressBarUpdate(this, 59, (int) this.entity.steam);
            icrafting.sendProgressBarUpdate(this, 60, (int) this.entity.steam >> 16);
            
            icrafting.sendProgressBarUpdate(this, 61, this.entity.steamType);
            icrafting.sendProgressBarUpdate(this, 62, this.entity.steamType >> 16);
        }        
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int value) {
        if (slot == 1){ this.lastEnergy = this.upcastShort(value); }
        if (slot == 2){ this.lastHLevel = this.upcastShort(value); }
        if (slot == 3){ this.lastDLevel = this.upcastShort(value); }
        if (slot == 4){ this.lastTLevel = this.upcastShort(value); }
        if (slot == 5){ this.lastHeLevel = this.upcastShort(value); }
        if (slot == 6){ this.lastBLevel = this.upcastShort(value); }
        if (slot == 7){ this.lastLi6Level = this.upcastShort(value); }
        if (slot == 8){ this.lastLi7Level = this.upcastShort(value); }
        if (slot == 9){ this.lastHLevel2 = this.upcastShort(value); }
        if (slot == 10){ this.lastDLevel2 = this.upcastShort(value); }
        if (slot == 11){ this.lastTLevel2 = this.upcastShort(value); }
        if (slot == 12){ this.lastHeLevel2 = this.upcastShort(value); }
        if (slot == 13){ this.lastBLevel2 = this.upcastShort(value); }
        if (slot == 14){ this.lastLi6Level2 = this.upcastShort(value); }
        if (slot == 15){ this.lastLi7Level2 = this.upcastShort(value); }
        
        if (slot == 16){ this.entity.energy = this.lastEnergy | value << 16; }
        if (slot == 17){ this.entity.HLevel = this.lastHLevel | value << 16; }
        if (slot == 18){ this.entity.DLevel = this.lastDLevel | value << 16; }
        if (slot == 19){ this.entity.TLevel = this.lastTLevel | value << 16; }
        if (slot == 20){ this.entity.HeLevel = this.lastHeLevel | value << 16; }
        if (slot == 21){ this.entity.BLevel = this.lastBLevel | value << 16; }
        if (slot == 22){ this.entity.Li6Level = this.lastLi6Level | value << 16; }
        if (slot == 23){ this.entity.Li7Level = this.lastLi7Level | value << 16; }
        if (slot == 24){ this.entity.HLevel2 = this.lastHLevel2 | value << 16; }
        if (slot == 25){ this.entity.DLevel2 = this.lastDLevel2 | value << 16; }
        if (slot == 26){ this.entity.TLevel2 = this.lastTLevel2 | value << 16; }
        if (slot == 27){ this.entity.HeLevel2 = this.lastHeLevel2 | value << 16; }
        if (slot == 28){ this.entity.BLevel2 = this.lastBLevel2 | value << 16; }
        if (slot == 29){ this.entity.Li6Level2 = this.lastLi6Level2 | value << 16; }
        if (slot == 30){ this.entity.Li7Level2 = this.lastLi7Level2 | value << 16; }
        
        if (slot == 31){ this.lastSShown = this.upcastShort(value); }
        if (slot == 32){ this.entity.SShown = this.lastSShown | value << 16; }
        if (slot == 33){ this.lastSize = this.upcastShort(value); }
        if (slot == 34){ this.entity.size = this.lastSize | value << 16; }
        if (slot == 35){ this.lastBelow = this.upcastShort(value); }
        
        if (slot == 37){ this.lastEfficiency = this.upcastShort(value); }
        if (slot == 38){ this.entity.efficiency = (int) this.lastEfficiency | value << 16; }
        if (slot == 39){ this.lastHeat = this.upcastShort(value); }
        if (slot == 40){ this.entity.heat = (int) this.lastHeat | value << 16; }
        if (slot == 41){ this.lastHeatVar = this.upcastShort(value); }
        if (slot == 42){ this.entity.heatVar = (int) this.lastHeatVar | value << 16; }
        
        if (slot == 43){ this.lastHOut = this.upcastShort(value); }
        if (slot == 44){ this.entity.HOut = (int) this.lastHOut | value << 16; }
        if (slot == 45){ this.lastDOut = this.upcastShort(value); }
        if (slot == 46){ this.entity.DOut = (int) this.lastDOut | value << 16; }
        if (slot == 47){ this.lastTOut = this.upcastShort(value); }
        if (slot == 48){ this.entity.TOut = (int) this.lastTOut | value << 16; }
        if (slot == 49){ this.lastHE3Out = this.upcastShort(value); }
        if (slot == 50){ this.entity.HE3Out = (int) this.lastHE3Out | value << 16; }
        if (slot == 51){ this.lastHE4Out = this.upcastShort(value); }
        if (slot == 52){ this.entity.HE4Out = (int) this.lastHE4Out | value << 16; }
        if (slot == 53){ this.lastnOut = this.upcastShort(value); }
        if (slot == 54){ this.entity.nOut = (int) this.lastnOut | value << 16; }
        
        if (slot == 55){ this.lastComplete = this.upcastShort(value); }
        if (slot == 56){ this.entity.complete = this.lastComplete | value << 16; }
        
        if (slot == 57){ this.lastFluid = this.upcastShort(value); }
        if (slot == 58){ this.entity.fluid = this.lastFluid | value << 16; }
        
        if (slot == 59){ this.lastSteam = this.upcastShort(value); }
        if (slot == 60){ this.entity.steam = (int) this.lastSteam | value << 16; }
        
        if (slot == 61){ this.lastSteamType = this.upcastShort(value); }
        if (slot == 62){ this.entity.steamType = this.lastSteamType | value << 16; }
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
            
            if (clickedSlotNumber > 8) {
                if (entity.isInputtableFuel1(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    	return null;
                    }                    
                } else if (entity.isInputtableFuel2(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    	return null;
                    }                    
                } else if (TileFusionReactorSteam.isCapsule(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return null;
                    }
                } else if (clickedSlotNumber >= 9 && clickedSlotNumber < 36) {
                    if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
                        return null;
                    }
                } else if (clickedSlotNumber >= 36 && clickedSlotNumber < 45 && !this.mergeItemStack(itemstack1, 9, 36, false)) {
                    return null;
                }
            }
            
            else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
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
