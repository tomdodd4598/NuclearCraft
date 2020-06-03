package nc.container.slot;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class SlotSpecificInput extends Slot {
	
	public final Object[] inputs;
	
	public SlotSpecificInput(IInventory inv, int index, int xPosition, int yPosition, Object... inputs) {
		super(inv, index, xPosition, yPosition);
		this.inputs = inputs;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		for (Object input : inputs) {
			if (input instanceof String) {
				NonNullList<ItemStack> ores = OreDictionary.getOres((String) input, false);
				for (ItemStack ore : ores) {
					if (ItemStack.areItemsEqual(ore, stack)) {
						return true;
					}
				}
			}
			else if (input instanceof ItemStack) {
				if (ItemStack.areItemsEqual((ItemStack) input, stack)) {
					return true;
				}
			}
			else if (input instanceof Item) {
				if ((Item) input == stack.getItem()) {
					return true;
				}
			}
		}
		return false;
	}
}
