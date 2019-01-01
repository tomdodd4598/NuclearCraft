package nc.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class ArmorHelper {
	
	public static boolean isArmor(Item item, boolean includeHorse) {
		if (item instanceof ItemArmor) return true;
		else if (includeHorse && isHorseArmor(item)) return true;
		return false;
	}
	
	public static boolean isHorseArmor(Item item) {
		if (NCUtil.isInstance(item, "cofh.core.item.tool.ItemHorseArmorCore")) return true;
		return item == Items.IRON_HORSE_ARMOR || item == Items.GOLDEN_HORSE_ARMOR || item == Items.DIAMOND_HORSE_ARMOR;
	}
}
