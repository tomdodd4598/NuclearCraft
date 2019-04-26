package nc.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemInfo {
	
	private final Item item;
	private final int meta;
	private final int hash;
	
	public ItemInfo(ItemStack stack) {
		item = stack.getItem();
		meta = item.getHasSubtypes() ? 0 : stack.getMetadata();
		hash = Item.REGISTRY.getIDForObject(item) << 16 | meta & 65535;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemStack) {
			ItemStack stack = (ItemStack)obj;
			return stack.getItem() == item && stack.getMetadata() == meta;
		}
		else if (obj instanceof ItemInfo) {
			ItemInfo other = (ItemInfo)obj;
			return other.hash == hash;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return item.getRegistryName().toString() + ":" + meta;
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
}
