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
		sendTiCSmelteryInfo("boron", false);
		sendTiCSmelteryInfo("lithium", false);
		sendTiCSmelteryInfo("magnesium", false);
		sendTiCSmelteryInfo("beryllium", false);
		
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
	
	public static Pair<String, Integer> fluid(String fluidName, int amount) {
		return Pair.of(fluidName, amount);
	}
}
