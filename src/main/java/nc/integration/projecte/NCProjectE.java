package nc.integration.projecte;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import moze_intel.projecte.api.ProjectEAPI;
import nc.init.NCBlocks;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class NCProjectE {
	
	private static final Object2LongMap<String> INGOT_MAP = new Object2LongOpenHashMap<>();
	private static final Object2LongMap<String> GEM_MAP = new Object2LongOpenHashMap<>();
	
	public static void addEMCValues() {
		INGOT_MAP.put("Copper", 128);
		INGOT_MAP.put("Tin", 256);
		INGOT_MAP.put("Lead", 512);
		INGOT_MAP.put("Thorium", 2048);
		INGOT_MAP.put("ThoriumOxide", 2048);
		INGOT_MAP.put("Uranium", 4096);
		INGOT_MAP.put("UraniumOxide", 4096);
		INGOT_MAP.put("Boron", 512);
		INGOT_MAP.put("Lithium", 512);
		INGOT_MAP.put("Magnesium", 512);
		INGOT_MAP.put("Graphite", 64);
		INGOT_MAP.put("Beryllium", 128);
		INGOT_MAP.put("Zirconium", 128);
		INGOT_MAP.put("Manganese", 128);
		INGOT_MAP.put("ManganeseOxide", 128);
		INGOT_MAP.put("ManganeseDioxide", 128);
		INGOT_MAP.put("Aluminum", 128);
		INGOT_MAP.put("Silver", 512);
		
		GEM_MAP.put("Diamond", 8192);
		GEM_MAP.put("Rhodochrosite", 64);
		GEM_MAP.put("Quartz", 256);
		GEM_MAP.put("Fluorite", 64);
		GEM_MAP.put("Villiaumite", 64);
		GEM_MAP.put("Carobbiite", 64);
		GEM_MAP.put("Arsenic", 64);
		
		for (Entry<String, Long> entry : INGOT_MAP.entrySet()) {
			addIngotEMCValues(entry.getKey(), entry.getValue());
		}
		
		for (Entry<String, Long> entry : GEM_MAP.entrySet()) {
			addGemEMCValues(entry.getKey(), entry.getValue());
		}
		
		ProjectEAPI.getEMCProxy().registerCustomEMC(new ItemStack(NCBlocks.dry_earth), 1);
		
		for (ItemStack stack : OreDictionary.getOres("dustObsidian")) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, 16);
		for (ItemStack stack : OreDictionary.getOres("dustEndstone")) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, 1);
	}
	
	private static final List<String> BLOCK_BLACKLIST = Arrays.asList("Quartz");
	
	private static void addIngotEMCValues(String type, long emc) {
		for (String prefix : OreDictHelper.INGOT_VOLUME_TYPES) {
			for (ItemStack stack : OreDictionary.getOres(prefix + type)) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, emc);
		}
		if (!BLOCK_BLACKLIST.contains(type)) for (String prefix : OreDictHelper.BLOCK_VOLUME_TYPES) {
			for (ItemStack stack : OreDictionary.getOres(prefix + type)) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, emc*9);
		}
	}
	
	private static void addGemEMCValues(String type, long emc) {
		for (String prefix : OreDictHelper.GEM_VOLUME_TYPES) {
			for (ItemStack stack : OreDictionary.getOres(prefix + type)) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, emc);
		}
		if (!BLOCK_BLACKLIST.contains(type)) for (String prefix : OreDictHelper.BLOCK_VOLUME_TYPES) {
			for (ItemStack stack : OreDictionary.getOres(prefix + type)) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, emc*9);
		}
	}
}
