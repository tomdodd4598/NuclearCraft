package nc.container.slot;

import nc.recipe.ProcessorRecipeHandler;
import nc.tile.inventory.ITileFilteredInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot {
	
	protected final int slotIndex;
	protected final ITileFilteredInventory tile;
	
	public SlotFiltered(ITileFilteredInventory tile, int index, int x, int y) {
		super(tile.getInventory(), index, x, y);
		slotIndex = index;
		this.tile = tile;
	}
	
	public ItemStack getFilterStack() {
		return tile.getFilterStacks().get(slotIndex);
	}
	
	public void setFilterStack(ItemStack stack) {
		tile.getFilterStacks().set(slotIndex, stack);
		onSlotChanged();
		tile.onFilterChanged(slotIndex);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack.isEmpty()) return false;
		boolean itemValidRaw = isItemValidRaw(stack);
		
		if (tile.canModifyFilter(slotIndex)) {
			if (!itemValidRaw) {
				setFilterStack(ItemStack.EMPTY);
				return false;
			}
			else if (!stack.isItemEqual(getFilterStack())) {
				ItemStack filter = stack.copy();
				filter.setCount(1);
				setFilterStack(filter);
				return false;
			}
		}
		
		return itemValidRaw && (getFilterStack().isEmpty() || stack.isItemEqual(getFilterStack()));
	}
	
	public boolean isItemValidRaw(ItemStack stack) {
		return true;
	}
	
	@Override
	public void putStack(ItemStack stack) {
		if (stack.isEmpty() && tile.canModifyFilter(slotIndex)) {
			setFilterStack(ItemStack.EMPTY);
		}
		super.putStack(stack);
	}
	
	public boolean hasStackForRender() {
		return !getStack().isEmpty() || !getFilterStack().isEmpty();
	}
	
	public ItemStack getStackForRender() {
		return !getStack().isEmpty() ? getStack() : getFilterStack();
	}
	
	public static class ProcessorInput extends SlotFiltered {
		
		protected final ProcessorRecipeHandler recipeHandler;
		
		public ProcessorInput(ITileFilteredInventory tile, ProcessorRecipeHandler recipeHandler, int index, int xPosition, int yPosition) {
			super(tile, index, xPosition, yPosition);
			this.recipeHandler = recipeHandler;
		}
		
		@Override
		public boolean isItemValidRaw(ItemStack stack) {
			return recipeHandler.isValidItemInput(stack);
		}
	}
}
