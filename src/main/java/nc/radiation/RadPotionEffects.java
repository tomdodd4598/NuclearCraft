package nc.radiation;

import static nc.config.NCConfig.*;

import java.util.*;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import nc.util.PotionHelper;
import net.minecraft.potion.*;

public class RadPotionEffects {
	
	public static final List<Double> PLAYER_RAD_LEVEL_LIST = new DoubleArrayList();
	public static final List<List<PotionEffect>> PLAYER_DEBUFF_LIST = new ArrayList<>();
	
	public static final List<Double> ENTITY_RAD_LEVEL_LIST = new DoubleArrayList();
	public static final List<List<PotionEffect>> ENTITY_DEBUFF_LIST = new ArrayList<>();
	
	public static final List<Double> MOB_RAD_LEVEL_LIST = new DoubleArrayList();
	public static final List<List<PotionEffect>> MOB_EFFECTS_LIST = new ArrayList<>();
	
	public static void init() {
		parseEffects(radiation_player_debuff_lists, PLAYER_RAD_LEVEL_LIST, PLAYER_DEBUFF_LIST, Math.max(radiation_player_tick_rate, 19));
		parseEffects(radiation_passive_debuff_lists, ENTITY_RAD_LEVEL_LIST, ENTITY_DEBUFF_LIST, Math.max(100 / radiation_world_chunks_per_tick, 39));
		parseEffects(radiation_mob_buff_lists, MOB_RAD_LEVEL_LIST, MOB_EFFECTS_LIST, Math.max(100 / radiation_world_chunks_per_tick, 39));
	}
	
	private static void parseEffects(String[] effectsArray, List<Double> radLevelList, List<List<PotionEffect>> potionList, int effectTime) {
		List<Double> radLevelListUnordered = new ArrayList<>();
		List<List<PotionEffect>> potionListUnordered = new ArrayList<>();
		
		main: for (String effects : effectsArray) {
			int puncPos = effects.indexOf('_');
			if (puncPos == -1) {
				continue;
			}
			
			double health = Double.parseDouble(effects.substring(0, puncPos));
			effects = effects.substring(puncPos + 1);
			
			List<PotionEffect> effectList = new ArrayList<>();
			do {
				puncPos = effects.indexOf('@');
				if (puncPos == -1) {
					continue main;
				}
				
				String potionName = effects.substring(0, puncPos);
				
				effects = effects.substring(puncPos + 1);
				puncPos = effects.indexOf(',');
				
				int strength = Integer.parseInt(puncPos == -1 ? effects : effects.substring(0, puncPos));
				effects = effects.substring(puncPos == -1 ? Integer.toString(strength).length() : Integer.toString(strength).length() + 1);
				
				int modifiedTime = getModifiedTime(potionName, effectTime, strength - 1);
				
				Potion potion = Potion.getPotionFromResourceLocation(potionName);
				if (potion != null) {
					effectList.add(PotionHelper.newEffect(potion, strength, modifiedTime));
				}
			}
			while (puncPos != -1);
			
			if (!effectList.isEmpty()) {
				radLevelListUnordered.add(health);
				potionListUnordered.add(effectList);
			}
		}
		
		Double[] healthArray = radLevelListUnordered.toArray(new Double[0]);
		int[] orderedIndices = IntStream.range(0, healthArray.length).boxed().sorted(Comparator.comparing(i -> healthArray[i])).mapToInt(e -> e).toArray();
		
		for (int index : orderedIndices) {
			radLevelList.add(radLevelListUnordered.get(index));
			potionList.add(potionListUnordered.get(index));
		}
		
		// Descending Order
		radLevelList = Lists.reverse(radLevelList);
		potionList = Lists.reverse(potionList);
	}
	
	private static int getModifiedTime(String potionName, int effectTime, int amplifier) {
        return switch (potionName) {
            case "regeneration", "minecraft:regeneration" -> Math.max(effectTime, 50 >> amplifier);
            case "wither", "minecraft:wither" -> Math.max(effectTime, 40 >> amplifier);
            case "poison", "minecraft:poison" -> Math.max(effectTime, 25 >> amplifier);
            case "blindness", "minecraft:blindness" -> effectTime + 25;
            default -> effectTime;
        };
	}
}
