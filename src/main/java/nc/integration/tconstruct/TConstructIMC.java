package nc.integration.tconstruct;

import org.apache.commons.lang3.tuple.Pair;

import nc.util.StringHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TConstructIMC {
	
	public static void sendIMCs() {
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
		
		sendTiCSmelteryInfo("sodium", false);
		sendTiCSmelteryInfo("potassium", false);
		sendTiCSmelteryInfo("plutonium", false);
		
		sendTiCSmelteryInfo("manganese_dioxide", "manganeseDioxide", true);
		
		sendTiCSmelteryInfo("ferroboron", true);
		sendTiCSmelteryInfo("tough", true);
		sendTiCSmelteryInfo("hard_carbon", "hardCarbon", true);
		
		sendTiCAlloyInfo("steel", 18, fluid("iron", 18), fluid("coal", 25));
		sendTiCAlloyInfo("ferroboron", 2, fluid("steel", 1), fluid("boron", 1));
		sendTiCAlloyInfo("tough", 2, fluid("ferroboron", 1), fluid("lithium", 1));
		sendTiCAlloyInfo("hard_carbon", 144, fluid("coal", 100), fluid("diamond", 333));
		sendTiCAlloyInfo("magnesium_diboride", 3, fluid("magnesium", 1), fluid("boron", 2));
		sendTiCAlloyInfo("lithium_manganese_dioxide", 2, fluid("lithium", 1), fluid("manganese_dioxide", 1));
		sendTiCAlloyInfo("extreme", 1, fluid("tough", 1), fluid("hard_carbon", 1));
		sendTiCAlloyInfo("thermoconducting", 16, fluid("extreme", 8), fluid("boron_arsenide", 37));
	}
	
	public static void sendTiCSmelteryInfo(String fluidName, String oreName, boolean toolForge) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("fluid", fluidName);
		tag.setString("ore", StringHelper.capitalize(oreName));
		tag.setBoolean("toolforge", toolForge);
		FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
	}
	
	public static void sendTiCSmelteryInfo(String name, boolean toolForge) {
		sendTiCSmelteryInfo(name, name, toolForge);
	}
	
	public static void sendTiCAlloyInfo(String alloyName, int alloyAmount, Pair<String, Integer>... components) {
		NBTTagList tagList = new NBTTagList();
		NBTTagCompound fluid = new NBTTagCompound();
		fluid.setString("FluidName", alloyName);
		fluid.setInteger("Amount", alloyAmount);
		tagList.appendTag(fluid);
		
		for (Pair<String, Integer> component : components) {
			fluid = new NBTTagCompound();
			fluid.setString("FluidName", component.getLeft());
			fluid.setInteger("Amount", component.getRight());
			tagList.appendTag(fluid);
		}
		
		NBTTagCompound message = new NBTTagCompound();
		message.setTag("alloy", tagList);
		FMLInterModComms.sendMessage("tconstruct", "alloy", message);
	}
	
	private static Pair<String, Integer> fluid(String fluidName, int amount) {
		return Pair.of(fluidName, amount);
	}
}
