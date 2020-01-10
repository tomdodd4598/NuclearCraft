package nc.integration.projecte;

import java.util.List;

import com.google.common.collect.Lists;

import moze_intel.projecte.api.ProjectEAPI;
import nc.init.NCBlocks;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class NCProjectE {
	
	public static void addEMCValues() {
		addIngotEMCValues("Iron", 256);
		addIngotEMCValues("Gold", 2048);
		addIngotEMCValues("Copper", 128);
		addIngotEMCValues("Tin", 256);
		addIngotEMCValues("Lead", 512);
		addIngotEMCValues("Thorium", 2048);
		addIngotEMCValues("Uranium", 4096);
		addIngotEMCValues("Boron", 512);
		addIngotEMCValues("Lithium", 512);
		addIngotEMCValues("Magnesium", 512);
		addIngotEMCValues("Graphite", 64);
		addIngotEMCValues("Beryllium", 128);
		addIngotEMCValues("Zirconium", 128);
		addIngotEMCValues("Manganese", 128);
		addIngotEMCValues("ManganeseOxide", 128);
		addIngotEMCValues("ManganeseDioxide", 128);
		addIngotEMCValues("Aluminum", 128);
		addIngotEMCValues("Silver", 512);
		
		addIngotEMCValues("Tough", 464);
		addIngotEMCValues("HardCarbon", 4160);
		addIngotEMCValues("MagnesiumDiboride", 512);
		addIngotEMCValues("LithiumManganeseDioxide", 320);
		addIngotEMCValues("Steel", 320);
		addIngotEMCValues("Ferroboron", 416);
		addIngotEMCValues("Shibuichi", 448);
		addIngotEMCValues("TinSilver", 640);
		addIngotEMCValues("LeadPlatinum", 2816);
		addIngotEMCValues("Extreme", 4624);
		addIngotEMCValues("Thermoconducting", 2600);
		addIngotEMCValues("Zircaloy", 144);
		addIngotEMCValues("SiliconCarbide", 32);
		//addIngotEMCValues("SiCSiCCMC");
		addIngotEMCValues("HSLASteel", 246);
		
		addGemEMCValues("Diamond", 8192);
		addGemEMCValues("Rhodochrosite", 64);
		addGemEMCValues("Quartz", 256);
		addGemEMCValues("NetherQuartz", 256);
		addGemEMCValues("BoronNitride", 256);
		addGemEMCValues("Fluorite", 64);
		addGemEMCValues("Villiaumite", 64);
		addGemEMCValues("Carobbiite", 64);
		addGemEMCValues("Arsenic", 64);
		addGemEMCValues("BoronArsenide", 576);
		
		addCompoundEMCValues("CalciumSulfate", 96);
		addCompoundEMCValues("CrystalBinder", 344);
		addCompoundEMCValues("Energetic", 224);
		addCompoundEMCValues("SodiumFluoride", 64);
		addCompoundEMCValues("PotassiumFluoride", 64);
		addCompoundEMCValues("SodiumHydroxide", 64);
		addCompoundEMCValues("PotassiumHydroxide", 64);
		addCompoundEMCValues("Borax", 1152);
		addCompoundEMCValues("Dimensional", 32);
		addCompoundEMCValues("CarbonManganese", 96);
		addCompoundEMCValues("Alugentum", 1024);
		
		ProjectEAPI.getEMCProxy().registerCustomEMC(new ItemStack(NCBlocks.wasteland_earth), 1);
		
		addEMCValues("dustObsidian", 16);
		addEMCValues("dustEndstone", 1);
		addEMCValues("itemSilicon", 1);
		addEMCValues("ingotSilicon", 1);
	}
	
	private static final List<String> BLOCK_BLACKLIST = Lists.newArrayList("Quartz", "NetherQuartz");
	
	private static void addEMCValues(String type, long emc) {
		for (ItemStack stack : OreDictionary.getOres(type, false)) ProjectEAPI.getEMCProxy().registerCustomEMC(stack, emc);
	}
	
	private static void addIngotEMCValues(String type, long emc) {
		for (String prefix : OreDictHelper.INGOT_VOLUME_TYPES) {
			addEMCValues(prefix + type, emc);
		}
		if (!BLOCK_BLACKLIST.contains(type)) {
			for (String prefix : OreDictHelper.BLOCK_VOLUME_TYPES) {
				addEMCValues(prefix + type, emc*9);
			}
		}
	}
	
	private static void addGemEMCValues(String type, long emc) {
		for (String prefix : OreDictHelper.GEM_VOLUME_TYPES) {
			addEMCValues(prefix + type, emc);
		}
		if (!BLOCK_BLACKLIST.contains(type)) {
			for (String prefix : OreDictHelper.BLOCK_VOLUME_TYPES) {
				addEMCValues(prefix + type, emc*9);
			}
		}
	}
	
	private static void addCompoundEMCValues(String type, long emc) {
		addEMCValues("dust" + type, emc);
	}
}
