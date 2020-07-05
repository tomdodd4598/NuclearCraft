package nc.container.slot;

import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotProcessorInput extends Slot {
	
	protected final ProcessorRecipeHandler recipeHandler;
	
	public SlotProcessorInput(IInventory tile, ProcessorRecipeHandler recipeHandler, int index, int xPosition, int yPosition) {
		super(tile, index, xPosition, yPosition);
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return recipeHandler.isValidItemInput(stack);
	}
}
