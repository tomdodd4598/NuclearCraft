package nc.container.crafting;

import nc.block.NCBlocks;
import nc.crafting.workspace.NuclearWorkspaceCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerNuclearWorkspace extends Container {

	public InventoryCrafting/*Keep*/ craftMatrix = new InventoryCrafting(this, /*entity,*/ 5, 5);
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	private World worldObj;
	private int posX;
    private int posY;
    private int posZ;
	
	public ContainerNuclearWorkspace(InventoryPlayer invPlayer, World world, int x, int y, int z) {
		worldObj = world;
		this.posX = x;
        this.posY = y;
        this.posZ = z;
		
		this.addSlotToContainer(new SlotCrafting(invPlayer.player, craftMatrix, craftResult, 0, 140, 53));
		
		for (int i = 0; i < 5; i++) {
			for (int k = 0; k < 5; k++) {
				this.addSlotToContainer(new Slot(craftMatrix, k + i * 5, 8 + k * 18, 17 + i * 18));
			}
		}
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i*18, 169));
		}
		
		onCraftMatrixChanged(this.craftMatrix);
	}

	public void onCraftMatrixChanged(IInventory iinventory) {
		craftResult.setInventorySlotContents(0, NuclearWorkspaceCraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}
	
	public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.worldObj.getBlock(this.posX, this.posY, this.posZ) != NCBlocks.nuclearWorkspace ? false : p_75145_1_.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
    }
	
	public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        super.onContainerClosed(p_75134_1_);

        if (!this.worldObj.isRemote)
        {
            for (int i = 0; i < 25; ++i)
            {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
	
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ == 0)
            {
                if (!this.mergeItemStack(itemstack1, 26, 62, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (p_82846_2_ >= 26 && p_82846_2_ < 53)
            {
                if (!this.mergeItemStack(itemstack1, 53, 62, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 53 && p_82846_2_ < 62)
            {
                if (!this.mergeItemStack(itemstack1, 26, 53, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 26, 62, false))
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

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }
	
	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
    {
        return p_94530_2_.inventory != this.craftResult && super.func_94530_a(p_94530_1_, p_94530_2_);
    }
}
