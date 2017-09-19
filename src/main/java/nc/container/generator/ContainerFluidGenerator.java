package nc.container.generator;

import nc.container.ContainerTile;
import nc.recipe.BaseRecipeHandler;
import nc.tile.generator.TileFluidGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFluidGenerator extends ContainerTile {
	
	public final TileFluidGenerator tile;
	public final BaseRecipeHandler recipes;
	
	protected int time;
	protected int energy;
	
	public ContainerFluidGenerator(TileFluidGenerator tileEntity, BaseRecipeHandler recipes) {
		super(tileEntity);
		tile = tileEntity;
		this.recipes = recipes;
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			icontainerlistener.sendProgressBarUpdate(this, 0, tile.getField(0) >> 16);
			icontainerlistener.sendProgressBarUpdate(this, 100, tile.getField(0));

			icontainerlistener.sendProgressBarUpdate(this, 1, tile.getField(1) >> 16);
			icontainerlistener.sendProgressBarUpdate(this, 101, tile.getField(1));
		}
	}
	
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tile);
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (id == 100) time = upcast(data);
		else if (id == 101) energy = upcast(data);
		
		else if (id == 0) tile.setField(id, time | data << 16);
		else if (id == 1) tile.setField(id, energy | data << 16);
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
	
	@SuppressWarnings("unused")
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(index);
		//int otherSlots = tile.otherSlotsSize;
		int invStart = tile.otherSlotsSize;
		int invEnd = 36 + tile.otherSlotsSize;
		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= 0 && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(index >= invStart) {
				if (recipes.isValidManualInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, 0, false)) {
						return null;
					}
				}
				else if ((index >= invStart) && (index < invEnd - 9)) {
					if (!mergeItemStack(itemstack1, invEnd - 9, invEnd, false)) {
						return null;
					}
				}
				else if ((index >= invEnd - 9) && (index < invEnd) && (!mergeItemStack(itemstack1, invStart, invEnd - 9, false))) {
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
				return null;
			}
			if (itemstack1 == null) {
				slot.putStack(null);
			}
			else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}
}
