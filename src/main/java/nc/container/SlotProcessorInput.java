package nc.container;

import nc.recipe.NCRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotProcessorInput extends Slot {
	
	public final NCRecipes.Type recipeType;
	
	public SlotProcessorInput(IInventory inventoryIn, NCRecipes.Type recipeType, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.recipeType = recipeType;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return recipeType.getRecipeHandler().isValidInput(stack);
	}
}
