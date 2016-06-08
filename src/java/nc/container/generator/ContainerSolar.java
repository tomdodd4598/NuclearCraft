/*package nc.container.generator;

import nc.tile.generator.TileSolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerSolar extends Container
{
    private TileSolarPanel entity;
    public int lastEnergy;

    public ContainerSolar(InventoryPlayer inventory, TileSolarPanel entity) {
        this.entity = entity;
        this.addSlotToContainer(new Slot(entity, 0, 56, 26));     
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }
    public void addCraftingToCrafters(ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            icrafting.sendProgressBarUpdate(this, 1, this.entity.energy);
            icrafting.sendProgressBarUpdate(this, 4, this.entity.energy >> 16);
        }        
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int value) {
        if (slot == 1) this.lastEnergy = this.upcastShort(value);
        if (slot == 4) this.entity.energy = this.lastEnergy | value << 16;
    }
    
    private int upcastShort(int input) {
    	if (input < 0) input += 65536;
    	return input;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber) {
        return null;
    }

    public boolean canInteractWith(EntityPlayer var1) {
        return false;
    }

    public int dischargeValue(Item item) {
        return 0;
    }
}*/
