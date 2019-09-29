package nc.util;

import java.util.HashMap;
import java.util.Map;

import cofh.core.item.tool.ItemHorseArmorCore;
import nc.ModCheck;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class ArmorHelper {
	
	public static boolean isArmor(Item item, boolean includeHorse) {
		if (item instanceof ItemArmor) return true;
		else if (includeHorse && isHorseArmor(item)) return true;
		return false;
	}
	
	private static final Map<Item, Boolean> HORSE_ARMOR_CACHE = new HashMap<Item, Boolean>();
	
	public static boolean isHorseArmor(Item item) {
		if (item == Items.IRON_HORSE_ARMOR || item == Items.GOLDEN_HORSE_ARMOR || item == Items.DIAMOND_HORSE_ARMOR) return true;
		if (!HORSE_ARMOR_CACHE.containsKey(item)) {
			HORSE_ARMOR_CACHE.put(item, isModHorseArmor(item));
		}
		return HORSE_ARMOR_CACHE.get(item);
	}
	
	private static boolean isModHorseArmor(Item item) {
		if (ModCheck.cofhCoreLoaded()) {
			if (item instanceof ItemHorseArmorCore) return true;
		}
		return false;
	}
}
