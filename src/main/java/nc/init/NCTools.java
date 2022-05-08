package nc.init;

import static nc.config.NCConfig.*;

import nc.Global;
import nc.item.IInfoItem;
import nc.item.tool.*;
import nc.tab.NCTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCTools {
	
	public static final ToolMaterial BORON = toolMaterial("boron", 0, new ItemStack(NCItems.ingot, 1, 5));
	public static final ToolMaterial SPAXELHOE_BORON = toolMaterial("spaxelhoe_boron", 1, new ItemStack(NCItems.ingot, 1, 5));
	public static final ToolMaterial TOUGH = toolMaterial("tough", 2, new ItemStack(NCItems.alloy, 1, 1));
	public static final ToolMaterial SPAXELHOE_TOUGH = toolMaterial("spaxelhoe_tough", 3, new ItemStack(NCItems.alloy, 1, 1));
	public static final ToolMaterial HARD_CARBON = toolMaterial("hard_carbon", 4, new ItemStack(NCItems.alloy, 1, 2));
	public static final ToolMaterial SPAXELHOE_HARD_CARBON = toolMaterial("spaxelhoe_hard_carbon", 5, new ItemStack(NCItems.alloy, 1, 2));
	public static final ToolMaterial BORON_NITRIDE = toolMaterial("boron_nitride", 6, new ItemStack(NCItems.gem, 1, 1));
	public static final ToolMaterial SPAXELHOE_BORON_NITRIDE = toolMaterial("spaxelhoe_boron_nitride", 7, new ItemStack(NCItems.gem, 1, 1));
	
	public static Item sword_boron;
	public static Item pickaxe_boron;
	public static Item shovel_boron;
	public static Item axe_boron;
	public static Item hoe_boron;
	public static Item spaxelhoe_boron;
	
	public static Item sword_tough;
	public static Item pickaxe_tough;
	public static Item shovel_tough;
	public static Item axe_tough;
	public static Item hoe_tough;
	public static Item spaxelhoe_tough;
	
	public static Item sword_hard_carbon;
	public static Item pickaxe_hard_carbon;
	public static Item shovel_hard_carbon;
	public static Item axe_hard_carbon;
	public static Item hoe_hard_carbon;
	public static Item spaxelhoe_hard_carbon;
	
	public static Item sword_boron_nitride;
	public static Item pickaxe_boron_nitride;
	public static Item shovel_boron_nitride;
	public static Item axe_boron_nitride;
	public static Item hoe_boron_nitride;
	public static Item spaxelhoe_boron_nitride;
	
	public static void init() {
		if (register_tool[0]) {
			sword_boron = withName(new NCSword(BORON, TextFormatting.GRAY), "sword_boron");
			pickaxe_boron = withName(new NCPickaxe(BORON, TextFormatting.GRAY), "pickaxe_boron");
			shovel_boron = withName(new NCShovel(BORON, TextFormatting.GRAY), "shovel_boron");
			axe_boron = withName(new NCAxe(BORON, TextFormatting.GRAY), "axe_boron");
			hoe_boron = withName(new NCHoe(BORON, TextFormatting.GRAY), "hoe_boron");
			spaxelhoe_boron = withName(new NCSpaxelhoe(SPAXELHOE_BORON, TextFormatting.GRAY), "spaxelhoe_boron");
		}
		
		if (register_tool[1]) {
			sword_tough = withName(new NCSword(TOUGH, TextFormatting.DARK_PURPLE), "sword_tough");
			pickaxe_tough = withName(new NCPickaxe(TOUGH, TextFormatting.DARK_PURPLE), "pickaxe_tough");
			shovel_tough = withName(new NCShovel(TOUGH, TextFormatting.DARK_PURPLE), "shovel_tough");
			axe_tough = withName(new NCAxe(TOUGH, TextFormatting.DARK_PURPLE), "axe_tough");
			hoe_tough = withName(new NCHoe(TOUGH, TextFormatting.DARK_PURPLE), "hoe_tough");
			spaxelhoe_tough = withName(new NCSpaxelhoe(SPAXELHOE_TOUGH, TextFormatting.DARK_PURPLE), "spaxelhoe_tough");
		}
		
		if (register_tool[2]) {
			sword_hard_carbon = withName(new NCSword(HARD_CARBON, TextFormatting.BLUE), "sword_hard_carbon");
			pickaxe_hard_carbon = withName(new NCPickaxe(HARD_CARBON, TextFormatting.BLUE), "pickaxe_hard_carbon");
			shovel_hard_carbon = withName(new NCShovel(HARD_CARBON, TextFormatting.BLUE), "shovel_hard_carbon");
			axe_hard_carbon = withName(new NCAxe(HARD_CARBON, TextFormatting.BLUE), "axe_hard_carbon");
			hoe_hard_carbon = withName(new NCHoe(HARD_CARBON, TextFormatting.BLUE), "hoe_hard_carbon");
			spaxelhoe_hard_carbon = withName(new NCSpaxelhoe(SPAXELHOE_HARD_CARBON, TextFormatting.BLUE), "spaxelhoe_hard_carbon");
		}
		
		if (register_tool[3]) {
			sword_boron_nitride = withName(new NCSword(BORON_NITRIDE, TextFormatting.GREEN), "sword_boron_nitride");
			pickaxe_boron_nitride = withName(new NCPickaxe(BORON_NITRIDE, TextFormatting.GREEN), "pickaxe_boron_nitride");
			shovel_boron_nitride = withName(new NCShovel(BORON_NITRIDE, TextFormatting.GREEN), "shovel_boron_nitride");
			axe_boron_nitride = withName(new NCAxe(BORON_NITRIDE, TextFormatting.GREEN), "axe_boron_nitride");
			hoe_boron_nitride = withName(new NCHoe(BORON_NITRIDE, TextFormatting.GREEN), "hoe_boron_nitride");
			spaxelhoe_boron_nitride = withName(new NCSpaxelhoe(SPAXELHOE_BORON_NITRIDE, TextFormatting.GREEN), "spaxelhoe_boron_nitride");
		}
	}
	
	public static void register() {
		if (register_tool[0]) {
			registerItem(sword_boron);
			registerItem(pickaxe_boron);
			registerItem(shovel_boron);
			registerItem(axe_boron);
			registerItem(hoe_boron);
			registerItem(spaxelhoe_boron);
		}
		
		if (register_tool[1]) {
			registerItem(sword_tough);
			registerItem(pickaxe_tough);
			registerItem(shovel_tough);
			registerItem(axe_tough);
			registerItem(hoe_tough);
			registerItem(spaxelhoe_tough);
		}
		
		if (register_tool[2]) {
			registerItem(sword_hard_carbon);
			registerItem(pickaxe_hard_carbon);
			registerItem(shovel_hard_carbon);
			registerItem(axe_hard_carbon);
			registerItem(hoe_hard_carbon);
			registerItem(spaxelhoe_hard_carbon);
		}
		
		if (register_tool[3]) {
			registerItem(sword_boron_nitride);
			registerItem(pickaxe_boron_nitride);
			registerItem(shovel_boron_nitride);
			registerItem(axe_boron_nitride);
			registerItem(hoe_boron_nitride);
			registerItem(spaxelhoe_boron_nitride);
		}
	}
	
	public static void registerRenders() {
		if (register_tool[0]) {
			registerRender(sword_boron);
			registerRender(pickaxe_boron);
			registerRender(shovel_boron);
			registerRender(axe_boron);
			registerRender(hoe_boron);
			registerRender(spaxelhoe_boron);
		}
		
		if (register_tool[1]) {
			registerRender(sword_tough);
			registerRender(pickaxe_tough);
			registerRender(shovel_tough);
			registerRender(axe_tough);
			registerRender(hoe_tough);
			registerRender(spaxelhoe_tough);
		}
		
		if (register_tool[2]) {
			registerRender(sword_hard_carbon);
			registerRender(pickaxe_hard_carbon);
			registerRender(shovel_hard_carbon);
			registerRender(axe_hard_carbon);
			registerRender(hoe_hard_carbon);
			registerRender(spaxelhoe_hard_carbon);
		}
		
		if (register_tool[3]) {
			registerRender(sword_boron_nitride);
			registerRender(pickaxe_boron_nitride);
			registerRender(shovel_boron_nitride);
			registerRender(axe_boron_nitride);
			registerRender(hoe_boron_nitride);
			registerRender(spaxelhoe_boron_nitride);
		}
	}
	
	public static <T extends Item & IInfoItem> Item withName(T item, String name) {
		item.setTranslationKey(Global.MOD_ID + "." + name).setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		item.setInfo();
		return item;
	}
	
	public static void registerItem(Item item) {
		item.setCreativeTab(NCTabs.misc());
		ForgeRegistries.ITEMS.register(item);
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	public static ToolMaterial toolMaterial(String name, int id, ItemStack repairStack) {
		return EnumHelper.addToolMaterial(Global.MOD_ID + ":" + name, tool_mining_level[id], tool_durability[id], (float) tool_speed[id], (float) tool_attack_damage[id], tool_enchantability[id]).setRepairItem(repairStack);
	}
}
