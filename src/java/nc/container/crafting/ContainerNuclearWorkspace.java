package nc.container.crafting;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.crafting.workspace.NuclearWorkspaceCraftingManager;
import nc.tile.crafting.TileNuclearWorkspace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerNuclearWorkspace extends Container {

	public InventoryCrafting craftMatrix;
	public IInventory craftResult;
	private World worldObj;
	private int posX;
    private int posY;
    private int posZ;
	
	public ContainerNuclearWorkspace(InventoryPlayer invPlayer, World world, int x, int y, int z, TileNuclearWorkspace tile) {
		worldObj = world;
		this.posX = x;
        this.posY = y;
        this.posZ = z;
        craftMatrix = new HoldCrafting(this, tile);
        craftResult = new HoldCraftingResult(tile);
        
		this.addSlotToContainer(new SlotCrafting(invPlayer.player, craftMatrix, craftResult, 0, 140, 53));
		
		for (int i = 0; i < 5; i++) {
			for (int k = 0; k < 5; k++) {
				this.addSlotToContainer(new Slot(craftMatrix, k + i * 5, 8 + k * 18, 17 + i * 18));
			}
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i*18, 169));
		}
		onCraftMatrixChanged(this.craftMatrix);
	}

	public void onCraftMatrixChanged(IInventory iinventory) {
		craftResult.setInventorySlotContents(0, NuclearWorkspaceCraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}
	
	public boolean canInteractWith(EntityPlayer player) {
        return this.worldObj.getBlock(this.posX, this.posY, this.posZ) != NCBlocks.nuclearWorkspace ? false : player.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
    }
	
	public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
    }
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!this.mergeItemStack(itemstack1, 26, 62, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber >= 26 && slotNumber < 53) {
                if (NuclearCraft.workspaceShiftClick ? (!this.mergeItemStack(itemstack1, 1, 26, false) && !this.mergeItemStack(itemstack1, 53, 62, false)) : !this.mergeItemStack(itemstack1, 53, 62, false)) {
                    return null;
                }
            } else if (slotNumber >= 53 && slotNumber < 62) {
                if (!this.mergeItemStack(itemstack1, NuclearCraft.workspaceShiftClick ? 1 : 26, 53, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 26, 62, false)) {
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
	
	/*public boolean func_94530_a(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResult && super.func_94530_a(stack, slot);
    }*/
}
