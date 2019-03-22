package nc.util;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemInfo {
	
	private final boolean isEmpty;
	private final Item item;
	private final int meta;
	
	public ItemInfo(ItemStack stack) {
		isEmpty = stack.isEmpty();
		item = stack.getItem();
		meta = stack.getMetadata();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemStack) {
			ItemStack stack = (ItemStack)obj;
			return !stack.isEmpty() && stack.getItem() == item && stack.getMetadata() == meta;
		}
		else if (obj instanceof ItemInfo) {
			ItemInfo other = (ItemInfo)obj;
			return !other.isEmpty && other.item == item && other.meta == meta;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return isEmpty ? "empty" : item.getRegistryName().toString() + ":" + meta;
		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isEmpty, item, meta);
	}
}
