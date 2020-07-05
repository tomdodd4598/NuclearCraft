package nc.util;

import net.minecraft.item.*;

public class RarityHelper {
	
	public static EnumRarity nextRarity(EnumRarity rarity) {
		if (rarity == EnumRarity.COMMON) {
			return EnumRarity.UNCOMMON;
		}
		else if (rarity == EnumRarity.UNCOMMON) {
			return EnumRarity.RARE;
		}
		return EnumRarity.EPIC;
	}
	
	public static EnumRarity enchantRarity(ItemStack stack, EnumRarity rarity) {
		return stack.isItemEnchanted() ? nextRarity(rarity) : rarity;
	}
}
