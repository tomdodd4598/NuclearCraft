package nc.radiation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import nc.config.NCConfig;
import nc.util.PotionHelper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class RadPotionEffects {
	
	public static final List<Double> PLAYER_RAD_LEVEL_LIST = new DoubleArrayList();
	public static final List<List<PotionEffect>> PLAYER_DEBUFF_LIST = new ArrayList<>();
	
	public static final List<Double> ENTITY_RAD_LEVEL_LIST = new DoubleArrayList();
	public static final List<List<PotionEffect>> ENTITY_DEBUFF_LIST = new ArrayList<>();
	
	public static final List<Double> MOB_RAD_LEVEL_LIST = new DoubleArrayList();
	public static final List<List<PotionEffect>> MOB_EFFECTS_LIST = new ArrayList<>();
	
	public static void init() {
		parseEffects(NCConfig.radiation_player_debuff_lists, PLAYER_RAD_LEVEL_LIST, PLAYER_DEBUFF_LIST, Math.max(NCConfig.radiation_player_tick_rate, 19));
		parseEffects(NCConfig.radiation_passive_debuff_lists, ENTITY_RAD_LEVEL_LIST, ENTITY_DEBUFF_LIST, Math.max(100/NCConfig.radiation_world_chunks_per_tick, 39));
		parseEffects(NCConfig.radiation_mob_buff_lists, MOB_RAD_LEVEL_LIST, MOB_EFFECTS_LIST, Math.max(100/NCConfig.radiation_world_chunks_per_tick, 39));
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
				
				Integer strength = Integer.parseInt(puncPos == -1 ? effects : effects.substring(0, puncPos));
				effects = effects.substring(puncPos == -1 ? strength.toString().length() : strength.toString().length() + 1);
				
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
		
		Double[] healthArray = radLevelListUnordered.toArray(new Double[radLevelListUnordered.size()]);
		int[] orderedIndices = IntStream.range(0, healthArray.length).boxed().sorted((i, j) -> healthArray[i].compareTo(healthArray[j])).mapToInt(e -> e).toArray();
		
		for (int index : orderedIndices) {
			radLevelList.add(radLevelListUnordered.get(index));
			potionList.add(potionListUnordered.get(index));
		}
		
		// Descending Order
		radLevelList = Lists.reverse(radLevelList);
		potionList = Lists.reverse(potionList);
	}
	
	private static int getModifiedTime(String potionName, int effectTime, int amplifier) {
		if (potionName.equals("regeneration") || potionName.equals("minecraft:regeneration")) {
			return Math.max(effectTime, 50 >> amplifier);
		}
		else if (potionName.equals("wither") || potionName.equals("minecraft:wither")) {
			return Math.max(effectTime, 40 >> amplifier);
		}
		else if (potionName.equals("poison") || potionName.equals("minecraft:poison")) {
			return Math.max(effectTime, 25 >> amplifier);
		}
		else if (potionName.equals("blindness") || potionName.equals("minecraft:blindness")) {
			return effectTime + 25;
		}
		else return effectTime;
	}
}
