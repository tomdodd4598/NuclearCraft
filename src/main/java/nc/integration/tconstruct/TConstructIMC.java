package nc.integration.tconstruct;

import org.apache.commons.lang3.tuple.Pair;

import nc.util.StringHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.fluids.*;
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
		sendTiCSmelteryInfo("manganese_oxide", "manganeseOxide", false);
		sendTiCSmelteryInfo("manganese_dioxide", "manganeseDioxide", false);
		
		sendTiCSmelteryInfo("sodium", false);
		sendTiCSmelteryInfo("potassium", false);
		
		sendTiCSmelteryInfo("ferroboron", true);
		sendTiCSmelteryInfo("tough", true);
		sendTiCSmelteryInfo("hard_carbon", "hardCarbon", true);
		sendTiCSmelteryInfo("lead_platinum", "leadPlatinum", true);
		sendTiCSmelteryInfo("enderium", true);
		
		sendTiCAlloyInfo("steel", 9, fluid("iron", 9), fluid("coal", 25));
		sendTiCAlloyInfo("ferroboron", 2, fluid("steel", 1), fluid("boron", 1));
		sendTiCAlloyInfo("tough", 2, fluid("ferroboron", 1), fluid("lithium", 1));
		sendTiCAlloyInfo("hard_carbon", 144, fluid("coal", 100), fluid("diamond", 333));
		sendTiCAlloyInfo("magnesium_diboride", 3, fluid("magnesium", 1), fluid("boron", 2));
		sendTiCAlloyInfo("lithium_manganese_dioxide", 2, fluid("lithium", 1), fluid("manganese_dioxide", 1));
		sendTiCAlloyInfo("extreme", 1, fluid("tough", 1), fluid("hard_carbon", 1));
		sendTiCAlloyInfo("thermoconducting", 16, fluid("extreme", 8), fluid("boron_arsenide", 37));
		sendTiCAlloyInfo("lead_platinum", 4, fluid("lead", 3), fluid("platinum", 1));
		sendTiCAlloyInfo("enderium", 72, fluid("lead_platinum", 72), fluid("ender", 125));
		
		sendTiCSmelteryInfo("cocoa_butter", "cocoaButter", false);
		sendTiCSmelteryInfo("unsweetened_chocolate", "unsweetenedChocolate", false);
		sendTiCSmelteryInfo("dark_chocolate", "darkChocolate", false);
		sendTiCSmelteryInfo("milk_chocolate", "chocolate", false);
		sendTiCSmelteryInfo("marshmallow", false);
		
		sendTiCAlloyInfo("unsweetened_chocolate", 2, fluid("chocolate_liquor", 1), fluid("cocoa_butter", 1));
		sendTiCAlloyInfo("dark_chocolate", 2, fluid("unsweetened_chocolate", 2), fluid("sugar", 1));
		sendTiCAlloyInfo("milk_chocolate", 144, fluid("dark_chocolate", 72), fluid("milk", 125));
		sendTiCAlloyInfo("hydrated_gelatin", 36, fluid("gelatin", 36), fluid("water", 125));
		sendTiCAlloyInfo("marshmallow", 2, fluid("hydrated_gelatin", 2), fluid("sugar", 1));
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
	
	public static void sendTiCSmelteryMeltingInfo(ItemStack input, ItemStack render, String fluidName, int fluidAmount, int temp) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound inputNBT = new NBTTagCompound();
		input.writeToNBT(inputNBT);
		tag.setTag("Item", inputNBT);
		NBTTagCompound renderNBT = new NBTTagCompound();
		render.writeToNBT(renderNBT);
		tag.setTag("Block", renderNBT);
		new FluidStack(FluidRegistry.getFluid(fluidName), fluidAmount).writeToNBT(tag);
		tag.setInteger("Temperature", temp);
		FMLInterModComms.sendMessage("tconstruct", "addSmelteryMelting", tag);
	}
	
	public static void sendTiCSmelteryMeltingInfo(ItemStack input, String fluidName, int fluidAmount, int temp) {
		sendTiCSmelteryMeltingInfo(input, input, fluidName, fluidAmount, temp);
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
