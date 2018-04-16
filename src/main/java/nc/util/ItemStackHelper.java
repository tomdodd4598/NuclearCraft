package nc.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStackHelper {
	
	public static ItemStack fixItemStack(Object object) {
		if (object instanceof ItemStack) {
			ItemStack stack = ((ItemStack) object).copy();
			if (stack.getCount() == 0) {
				stack.setCount(1);
			}
			return stack;
		} else if (object instanceof Item) {
			return new ItemStack((Item) object, 1);
		} else {
			if (!(object instanceof Block)) {
				throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
			}
			return new ItemStack((Block) object, 1);
		}
	}
	
	public static ItemStack blockStateToStack(IBlockState state) {
		if (state == null) return ItemStack.EMPTY;
		Block block = state.getBlock();
		if (block == null) return ItemStack.EMPTY;
		int meta = block.getMetaFromState(state);
		return new ItemStack(block, 1, meta);
	}
	
	public static Block getBlockFromStack(ItemStack stack) {
		if (stack.isEmpty() || stack == null) return null;
		int meta = stack.getMetadata();
		Item item = stack.getItem();
		if (!NCUtil.isSubclassOf(item.getClass(), ItemBlock.class)) return null;
		ItemBlock itemBlock = (ItemBlock) item;
		return itemBlock.getBlock();
	}
	
	public static ItemStack changeStackSize(ItemStack stack, int size) {
		ItemStack newStack = stack.copy();
		newStack.setCount(size);
		return newStack.copy();
	}
	
	public static String stackName(ItemStack stack) {
		return stack.getItem().getUnlocalizedName() + ":" + stack.getMetadata();
	}
	
	public static String stackListNames(List<ItemStack> list) {
		String names = "";
		for (ItemStack stack : list) names += (", " + stackName(stack));
		return names.substring(2);
	}
}
