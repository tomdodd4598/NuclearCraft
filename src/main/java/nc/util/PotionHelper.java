package nc.util;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionHelper {
	
	public static PotionEffect newEffect(int potionID, int strength, int ticks) {
		return new PotionEffect(Potion.getPotionById(potionID), ticks, Math.max(0, strength - 1), false, false);
	}
}
