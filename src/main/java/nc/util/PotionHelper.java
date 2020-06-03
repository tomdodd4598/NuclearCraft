package nc.util;

import net.minecraft.potion.*;

public class PotionHelper {
	
	public static PotionEffect newEffect(int potionID, int strength, int ticks) {
		return newEffect(Potion.getPotionById(potionID), strength, ticks);
	}
	
	public static PotionEffect newEffect(Potion potion, int strength, int ticks) {
		return new PotionEffect(potion, ticks, Math.max(0, strength - 1), false, false);
	}
}
