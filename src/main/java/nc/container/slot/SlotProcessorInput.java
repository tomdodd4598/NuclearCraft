package nc.container.slot;

import nc.recipe.BasicRecipeHandler;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotProcessorInput extends Slot {
	
	protected final BasicRecipeHandler recipeHandler;
	
	public SlotProcessorInput(IInventory tile, BasicRecipeHandler recipeHandler, int index, int xPosition, int yPosition) {
		super(tile, index, xPosition, yPosition);
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return recipeHandler.isValidItemInput(stack);
	}
}
