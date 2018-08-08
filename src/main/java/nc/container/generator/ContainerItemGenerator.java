package nc.container.generator;

import nc.container.ContainerTile;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.NCRecipes;
import nc.tile.generator.TileItemGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerItemGenerator extends ContainerTile {
	
	public final TileItemGenerator tile;
	public final NCRecipes.Type recipeType;
	
	protected int time;
	protected int energy;
	
	public ContainerItemGenerator(TileItemGenerator tileEntity, NCRecipes.Type recipeType) {
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
			
			//if (time != tile.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, tile.getField(0) >> 16);
				icontainerlistener.sendWindowProperty(this, 100, tile.getField(0));
			//}
			
			//if (energy != tile.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, tile.getField(1) >> 16);
				icontainerlistener.sendWindowProperty(this, 101, tile.getField(1));
			//}
		}
		
		/*time = tile.getField(0);
		energy = tile.getField(1);*/
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
		
		else if (id == 0) tile.setField(id, time | data << 16);
		else if (id == 1) tile.setField(id, energy | data << 16);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		//int otherSlots = tile.otherSlotsSize;
		int invStart = 2*tile.itemInputSize + tile.itemOutputSize + tile.otherSlotsSize;
		int invEnd = 2*tile.itemInputSize + tile.itemOutputSize + 36 + tile.otherSlotsSize;
		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= tile.itemInputSize && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(index >= invStart) {
				if (getRecipeHandler().isValidItemInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, tile.itemInputSize, false)) {
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
