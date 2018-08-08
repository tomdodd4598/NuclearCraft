package nc.container.processor;

import nc.container.ContainerTile;
import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFluidProcessor extends ContainerTile {
	
	public final TileFluidProcessor tile;
	public final NCRecipes.Type recipeType;
	
	protected int time;
	protected int energy;
	protected int baseTime;
	protected int basePower;
	
	protected ItemStack speedUpgrade = new ItemStack(NCItems.upgrade, 1, 0);
	
	public ContainerFluidProcessor(TileFluidProcessor tileEntity, NCRecipes.Type recipeType) {
		super(tileEntity);
		tile = tileEntity;
		this.recipeType = recipeType;
	}
	
	public ProcessorRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			for (int j = 0; j <= 3; j++) {
				icontainerlistener.sendWindowProperty(this, j, tile.getField(j) >> 16);
				icontainerlistener.sendWindowProperty(this, 100 + j, tile.getField(j));
			}
			
			//for (int j = 2; j <= 2 + tile.tanks.length; j++) icontainerlistener.sendWindowProperty(this, j, tile.getField(j));
		}
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tile);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (id == 100) time = upcast(data);
		else if (id == 101) energy = upcast(data);
		else if (id == 102) baseTime = upcast(data);
		else if (id == 103) basePower = upcast(data);
		
		else if (id == 0) tile.setField(id, time | data << 16);
		else if (id == 1) tile.setField(id, energy | data << 16);
		else if (id == 2) tile.setField(id, baseTime | data << 16);
		else if (id == 3) tile.setField(id, basePower | data << 16);
		
		//else if (id > 2 && id <= 2 + tile.tanks.length) tile.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
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
				
				else if (getRecipeHandler().isValidItemInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, 0, false)) {
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
