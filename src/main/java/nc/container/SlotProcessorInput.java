package nc.container;

import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotProcessorInput extends Slot {
	
	public final ProcessorRecipeHandler recipeHandler;
	
	public SlotProcessorInput(IInventory inventoryIn, ProcessorRecipeHandler recipeHandler, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return recipeHandler.isValidItemInput(stack);
	}
}
