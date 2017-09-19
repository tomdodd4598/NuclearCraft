package nc.container;

import nc.recipe.BaseRecipeHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotProcessorInput extends Slot {
	
	public final BaseRecipeHandler recipes;
	
	public SlotProcessorInput(IInventory inventoryIn, BaseRecipeHandler recipes, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.recipes = recipes;
	}
	
	public boolean isItemValid(ItemStack stack) {
		return recipes.isValidManualInput(stack);
	}
}
