package nc.container;

import nc.tile.inventory.ITileInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class SlotSpecificInput extends Slot {
	
	public final Object[] inputs;
	
	public SlotSpecificInput(ITileInventory tile, int slotIndex, int xPosition, int yPosition, Object... inputs) {
		this(tile.getInventory(), slotIndex, xPosition, yPosition, inputs);
	}
	
	public SlotSpecificInput(IInventory inv, int slotIndex, int xPosition, int yPosition, Object... inputs) {
		super(inv, slotIndex, xPosition, yPosition);
		this.inputs = inputs;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i] instanceof String) {
				NonNullList<ItemStack> ores = OreDictionary.getOres((String)inputs[i], false);
				for (ItemStack ore : ores) {
					if (ItemStack.areItemsEqual(ore, stack)) return true;
				}
			} else if (inputs[i] instanceof ItemStack) {
				if (ItemStack.areItemsEqual((ItemStack)inputs[i], stack)) return true;
			} else if (inputs[i] instanceof Item) {
				if ((Item)inputs[i] == stack.getItem()) return true;
			}
		}
		return false;
	}
}
