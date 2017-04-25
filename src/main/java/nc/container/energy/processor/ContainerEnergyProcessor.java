package nc.container.energy.processor;

import nc.container.ContainerTile;
import nc.handler.ProcessorRecipeHandler;
import nc.init.NCItems;
import nc.tile.processor.TileEnergyProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEnergyProcessor extends ContainerTile {
	
	public final TileEnergyProcessor tile;
	public final ProcessorRecipeHandler recipes;
	
	protected int time;
	protected int energy;
	
	protected ItemStack speedUpgrade = new ItemStack(NCItems.upgrade, 1, 0);
	
	public ContainerEnergyProcessor(TileEnergyProcessor tileEntity, ProcessorRecipeHandler recipes) {
		super(tileEntity);
		tile = tileEntity;
		this.recipes = recipes;
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			//if (time != tile.getField(0)) {
				icontainerlistener.sendProgressBarUpdate(this, 0, tile.getField(0) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 100, tile.getField(0));
			//}
			
			//if (energy != tile.getField(1)) {
				icontainerlistener.sendProgressBarUpdate(this, 1, tile.getField(1) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 101, tile.getField(1));
			//}
		}
		
		/*time = tile.getField(0);
		energy = tile.getField(1);*/
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
		return tile.isUsableByPlayer(player);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		int upgrades = tile.hasUpgrades? 2 : 0;
		int invStart = tile.inputSize + tile.outputSize + upgrades;
		int speedUpgradeSlot = tile.inputSize + tile.outputSize;
		int otherUpgradeSlot = tile.inputSize + tile.outputSize + 1;
		int invEnd = tile.inputSize + tile.outputSize + 36 + upgrades;
		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= tile.inputSize && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(index >= invStart) {
				if (tile.isItemValidForSlot(speedUpgradeSlot, itemstack1) && tile.hasUpgrades && itemstack1.getItem() == NCItems.upgrade) {
					if (!mergeItemStack(itemstack1, speedUpgradeSlot, speedUpgradeSlot + 1, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if (tile.isItemValidForSlot(otherUpgradeSlot, itemstack1) && tile.hasUpgrades && itemstack1.getItem() == NCItems.upgrade) {
					if (!mergeItemStack(itemstack1, otherUpgradeSlot, otherUpgradeSlot + 1, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				/*else if (tile.isOxygen(itemstack1) && tile.isOxidiser()) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				else if (tile.isNeutronCapsule(itemstack1) && tile.isIrradiator()) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				else if (tile.isHydrogen(itemstack1) && tile.isIoniser()) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				}*/
				
				else if (recipes.validInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, tile.inputSize, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if ((index >= invStart) && (index < invEnd - 9)) {
					if (!mergeItemStack(itemstack1, invEnd - 9, invEnd, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if ((index >= invEnd - 9) && (index < invEnd) && (!mergeItemStack(itemstack1, invStart, invEnd - 9, false))) {
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
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
