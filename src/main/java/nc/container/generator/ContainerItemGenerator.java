package nc.container.generator;

import nc.container.ContainerTile;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.ITileGui;
import nc.tile.generator.IItemGenerator;
import nc.tile.generator.TileItemGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerItemGenerator<GENERATOR extends IItemGenerator & ITileGui> extends ContainerTile<GENERATOR> {
	
	protected final GENERATOR tile;
	protected final ProcessorRecipeHandler recipeHandler;
	
	public ContainerItemGenerator(EntityPlayer player, GENERATOR tileEntity, ProcessorRecipeHandler recipeHandler) {
		super(tileEntity);
		tile = tileEntity;
		this.recipeHandler = recipeHandler;
		
		tileEntity.beginUpdatingPlayer(player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.stopUpdatingPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		int otherSlotsSize = tile instanceof TileItemGenerator ? ((TileItemGenerator)tile).getOtherSlotsSize() : 0;
		int invStart = tile.getItemInputSize() + tile.getItemOutputSize() + otherSlotsSize;
		int invEnd = tile.getItemInputSize() + tile.getItemOutputSize() + 36 + otherSlotsSize;
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= tile.getItemInputSize() && index < invStart) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= invStart) {
				if (recipeHandler.isValidItemInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, tile.getItemInputSize(), false)) {
						return ItemStack.EMPTY;
					}
				}
				else if (index >= invStart && index < invEnd - 9) {
					if (!mergeItemStack(itemstack1, invEnd - 9, invEnd, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if (index >= invEnd - 9 && index < invEnd && !mergeItemStack(itemstack1, invStart, invEnd - 9, false)) {
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
