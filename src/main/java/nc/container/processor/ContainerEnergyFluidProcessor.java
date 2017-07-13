package nc.container.processor;

import nc.container.ContainerTile;
import nc.handler.ProcessorRecipeHandler;
import nc.init.NCItems;
import nc.tile.processor.TileEnergyFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEnergyFluidProcessor extends ContainerTile {
	
	public final TileEnergyFluidProcessor tile;
	public final ProcessorRecipeHandler recipes;
	
	protected int time;
	protected int energy;
	protected int baseTime;
	
	protected ItemStack speedUpgrade = new ItemStack(NCItems.upgrade, 1, 0);
	
	public ContainerEnergyFluidProcessor(TileEnergyFluidProcessor tileEntity, ProcessorRecipeHandler recipes) {
		super(tileEntity);
		tile = tileEntity;
		this.recipes = recipes;
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			for (int j = 0; j <= 2; j++) {
				icontainerlistener.sendProgressBarUpdate(this, j, tile.getField(j) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 100 + j, tile.getField(j));
			}
			
			//for (int j = 2; j <= 2 + tile.tanks.length; j++) icontainerlistener.sendProgressBarUpdate(this, j, tile.getField(j));
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
		else if (id == 102) baseTime = upcast(data);
		
		else if (id == 0) tile.setField(id, time | data << 16);
		else if (id == 1) tile.setField(id, energy | data << 16);
		else if (id == 2) tile.setField(id, baseTime | data << 16);
		
		//else if (id > 2 && id <= 2 + tile.tanks.length) tile.setField(id, data);
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
	
	@SuppressWarnings("unused")
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(index);
		int upgrades = tile.hasUpgrades? 2 : 0;
		int invStart = upgrades;
		int speedUpgradeSlot = 0;
		int otherUpgradeSlot = 1;
		int invEnd = 36 + upgrades;
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
				if (tile.isItemValidForSlot(speedUpgradeSlot, itemstack1) && tile.hasUpgrades && itemstack1.getItem() == NCItems.upgrade) {
					if (!mergeItemStack(itemstack1, speedUpgradeSlot, speedUpgradeSlot + 1, false)) {
						return null;
					}
				}
				else if (tile.isItemValidForSlot(otherUpgradeSlot, itemstack1) && tile.hasUpgrades && itemstack1.getItem() == NCItems.upgrade) {
					if (!mergeItemStack(itemstack1, otherUpgradeSlot, otherUpgradeSlot + 1, false)) {
						return null;
					}
				}
				
				else if (recipes.validInput(itemstack1)) {
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
