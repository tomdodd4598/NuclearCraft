package nc.integration.tconstruct;

import nc.util.StringHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TConstructIMC {
	
	public static void init() {
		sendTiCSmelteryInfo("thorium", false);
		sendTiCSmelteryInfo("uranium", false);
		sendTiCSmelteryInfo("boron", true);
		sendTiCSmelteryInfo("lithium", false);
		sendTiCSmelteryInfo("magnesium", true);
		sendTiCSmelteryInfo("beryllium", true);
		sendTiCSmelteryInfo("zirconium", true);
		sendTiCSmelteryInfo("manganese", true);
		sendTiCSmelteryInfo("aluminum", true);
		sendTiCSmelteryInfo("silver", true);
		sendTiCSmelteryInfo("manganese_oxide", "ManganeseOxide", false);
		sendTiCSmelteryInfo("manganese_dioxide", "ManganeseDioxide", false);
		
		sendTiCSmelteryInfo("sodium", false);
		sendTiCSmelteryInfo("potassium", false);
		
		sendTiCSmelteryInfo("ferroboron", true);
		sendTiCSmelteryInfo("tough", true);
		sendTiCSmelteryInfo("hard_carbon", "HardCarbon", true);
		sendTiCSmelteryInfo("lead_platinum", "LeadPlatinum", true);
		sendTiCSmelteryInfo("enderium", true);
		
		sendTiCSmelteryInfo("cocoa_butter", "CocoaButter", false);
		sendTiCSmelteryInfo("unsweetened_chocolate", "UnsweetenedChocolate", false);
		sendTiCSmelteryInfo("dark_chocolate", "DarkChocolate", false);
		sendTiCSmelteryInfo("milk_chocolate", "Chocolate", false);
		sendTiCSmelteryInfo("marshmallow", false);
	}
	
	public static void sendTiCSmelteryInfo(String name, boolean toolForge) {
		sendTiCSmelteryInfo(name, StringHelper.capitalize(name), toolForge);
	}
	
	public static void sendTiCSmelteryInfo(String fluidName, String oreSuffix, boolean toolForge) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("fluid", fluidName);
		tag.setString("ore", oreSuffix);
		tag.setBoolean("toolforge", toolForge);
		FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
	}
}
