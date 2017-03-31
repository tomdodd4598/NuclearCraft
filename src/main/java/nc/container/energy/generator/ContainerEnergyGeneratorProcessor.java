package nc.container.energy.generator;

import nc.container.ContainerTile;
import nc.handlers.ProcessorRecipeHandler;
import nc.tile.energy.generator.TileEnergyGeneratorProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEnergyGeneratorProcessor extends ContainerTile {
	
	public final TileEnergyGeneratorProcessor tile;
	public final ProcessorRecipeHandler recipes;
	
	protected int time;
	protected int energy;
	
	public ContainerEnergyGeneratorProcessor(TileEnergyGeneratorProcessor tileEntity, ProcessorRecipeHandler recipes) {
		super(tileEntity);
		tile = tileEntity;
		this.recipes = recipes;
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			if (time != tile.getField(0)) {
				icontainerlistener.sendProgressBarUpdate(this, 0, tile.getField(0));
			}
			
			if (energy != tile.getField(1)) {
				icontainerlistener.sendProgressBarUpdate(this, 1, tile.getField(1));
			}
		}
		
		time = tile.getField(0);
		energy = tile.getField(1);
	}
	
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tile);
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		tile.setField(id, data);
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
	
	@SuppressWarnings("unused")
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(index);
		//int otherSlots = tile.otherSlotsSize;
		int invStart = 2*tile.inputSize + tile.outputSize + tile.otherSlotsSize;
		int invEnd = 2*tile.inputSize + tile.outputSize + 36 + tile.otherSlotsSize;
		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= tile.inputSize && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(index >= invStart) {
				if (recipes.validInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, tile.inputSize, false)) {
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
