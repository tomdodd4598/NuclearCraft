package nc.util;

import cofh.core.item.tool.ItemHorseArmorCore;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import net.minecraft.init.Items;
import net.minecraft.item.*;

public class ArmorHelper {
	
	public static boolean isArmor(Item item, boolean includeHorse) {
		return item instanceof ItemArmor || item instanceof ItemElytra || (includeHorse && isHorseArmor(item));
	}
	
	private static final Object2BooleanMap<Item> HORSE_ARMOR_CACHE = new Object2BooleanOpenHashMap<>();
	
	public static boolean isHorseArmor(Item item) {
		if (item == Items.IRON_HORSE_ARMOR || item == Items.GOLDEN_HORSE_ARMOR || item == Items.DIAMOND_HORSE_ARMOR) {
			return true;
		}
		if (!HORSE_ARMOR_CACHE.containsKey(item)) {
			HORSE_ARMOR_CACHE.put(item, isModHorseArmor(item));
		}
		return HORSE_ARMOR_CACHE.get(item);
	}
	
	private static boolean isModHorseArmor(Item item) {
		return ModCheck.cofhCoreLoaded() && item instanceof ItemHorseArmorCore;
	}
}
