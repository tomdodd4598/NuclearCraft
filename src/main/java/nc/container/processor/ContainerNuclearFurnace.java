package nc.container.processor;

import nc.container.SlotFuel;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerNuclearFurnace extends Container {
	private final IInventory tileFurnace;
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;
	
	public ContainerNuclearFurnace(EntityPlayer player, IInventory inventory) {
		tileFurnace = inventory;
		addSlotToContainer(new Slot(inventory, 0, 56, 17));
		addSlotToContainer(new SlotFuel(inventory, 1, 56, 53));
		addSlotToContainer(new SlotFurnaceOutput(player, inventory, 2, 116, 35));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142));
		}
	}
	
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tileFurnace);
	}
	
	/** Looks for changes made in the container, sends them to every listener */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			if (furnaceBurnTime != tileFurnace.getField(0)) {
				icontainerlistener.sendProgressBarUpdate(this, 0, tileFurnace.getField(0));
			}
			
			if (currentItemBurnTime != tileFurnace.getField(1)) {
				icontainerlistener.sendProgressBarUpdate(this, 1, tileFurnace.getField(1));
			}
			
			if (cookTime != tileFurnace.getField(2)) {
				icontainerlistener.sendProgressBarUpdate(this, 2, tileFurnace.getField(2));
			}
			
			if (totalCookTime != tileFurnace.getField(3)) {
				icontainerlistener.sendProgressBarUpdate(this, 3, tileFurnace.getField(3));
			}
		}
		
		furnaceBurnTime = tileFurnace.getField(0);
		currentItemBurnTime = tileFurnace.getField(1);
		cookTime = tileFurnace.getField(2);
		totalCookTime = tileFurnace.getField(3);
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		tileFurnace.setField(id, data);
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return tileFurnace.isUsableByPlayer(player);
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot)inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, false)) {
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty()) {
					if (!mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (TileNuclearFurnace.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!mergeItemStack(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
}
