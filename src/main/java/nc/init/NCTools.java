package nc.init;

import nc.Global;
import nc.config.NCConfig;
import nc.item.tool.NCAxe;
import nc.item.tool.NCHoe;
import nc.item.tool.NCPickaxe;
import nc.item.tool.NCShovel;
import nc.item.tool.NCSpaxelhoe;
import nc.item.tool.NCSword;
import nc.tab.NCTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
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
	
	public static ItemSword sword_boron;
	public static ItemPickaxe pickaxe_boron;
	public static ItemSpade shovel_boron;
	public static NCAxe axe_boron;
	public static ItemHoe hoe_boron;
	public static NCSpaxelhoe spaxelhoe_boron;
	
	public static ItemSword sword_tough;
	public static ItemPickaxe pickaxe_tough;
	public static ItemSpade shovel_tough;
	public static NCAxe axe_tough;
	public static ItemHoe hoe_tough;
	public static NCSpaxelhoe spaxelhoe_tough;
	
	public static ItemSword sword_hard_carbon;
	public static ItemPickaxe pickaxe_hard_carbon;
	public static ItemSpade shovel_hard_carbon;
	public static NCAxe axe_hard_carbon;
	public static ItemHoe hoe_hard_carbon;
	public static NCSpaxelhoe spaxelhoe_hard_carbon;
	
	public static ItemSword sword_boron_nitride;
	public static ItemPickaxe pickaxe_boron_nitride;
	public static ItemSpade shovel_boron_nitride;
	public static NCAxe axe_boron_nitride;
	public static ItemHoe hoe_boron_nitride;
	public static NCSpaxelhoe spaxelhoe_boron_nitride;
	
	public static void init() {
		if (NCConfig.register_tool[0]) {
			sword_boron = new NCSword("sword_boron", BORON);
			pickaxe_boron = new NCPickaxe("pickaxe_boron", BORON);
			shovel_boron = new NCShovel("shovel_boron", BORON);
			axe_boron = new NCAxe("axe_boron", BORON);
			hoe_boron = new NCHoe("hoe_boron", BORON);
			spaxelhoe_boron = new NCSpaxelhoe("spaxelhoe_boron", SPAXELHOE_BORON);
		}
		
		if (NCConfig.register_tool[1]) {
			sword_tough = new NCSword("sword_tough", TOUGH);
			pickaxe_tough = new NCPickaxe("pickaxe_tough", TOUGH);
			shovel_tough = new NCShovel("shovel_tough", TOUGH);
			axe_tough = new NCAxe("axe_tough", TOUGH);
			hoe_tough = new NCHoe("hoe_tough", TOUGH);
			spaxelhoe_tough = new NCSpaxelhoe("spaxelhoe_tough", SPAXELHOE_TOUGH);
		}
		
		if (NCConfig.register_tool[2]) {
			sword_hard_carbon = new NCSword("sword_hard_carbon", HARD_CARBON);
			pickaxe_hard_carbon = new NCPickaxe("pickaxe_hard_carbon", HARD_CARBON);
			shovel_hard_carbon = new NCShovel("shovel_hard_carbon", HARD_CARBON);
			axe_hard_carbon = new NCAxe("axe_hard_carbon", HARD_CARBON);
			hoe_hard_carbon = new NCHoe("hoe_hard_carbon", HARD_CARBON);
			spaxelhoe_hard_carbon = new NCSpaxelhoe("spaxelhoe_hard_carbon", SPAXELHOE_HARD_CARBON);
		}
		
		if (NCConfig.register_tool[3]) {
			sword_boron_nitride = new NCSword("sword_boron_nitride", BORON_NITRIDE);
			pickaxe_boron_nitride = new NCPickaxe("pickaxe_boron_nitride", BORON_NITRIDE);
			shovel_boron_nitride = new NCShovel("shovel_boron_nitride", BORON_NITRIDE);
			axe_boron_nitride = new NCAxe("axe_boron_nitride", BORON_NITRIDE);
			hoe_boron_nitride = new NCHoe("hoe_boron_nitride", BORON_NITRIDE);
			spaxelhoe_boron_nitride = new NCSpaxelhoe("spaxelhoe_boron_nitride", SPAXELHOE_BORON_NITRIDE);
		}
	}
		
	public static void register() {
		if (NCConfig.register_tool[0]) {
			registerItem(sword_boron);
			registerItem(pickaxe_boron);
			registerItem(shovel_boron);
			registerItem(axe_boron);
			registerItem(hoe_boron);
			registerItem(spaxelhoe_boron);
		}
		
		if (NCConfig.register_tool[1]) {
			registerItem(sword_tough);
			registerItem(pickaxe_tough);
			registerItem(shovel_tough);
			registerItem(axe_tough);
			registerItem(hoe_tough);
			registerItem(spaxelhoe_tough);
		}
		
		if (NCConfig.register_tool[2]) {
			registerItem(sword_hard_carbon);
			registerItem(pickaxe_hard_carbon);
			registerItem(shovel_hard_carbon);
			registerItem(axe_hard_carbon);
			registerItem(hoe_hard_carbon);
			registerItem(spaxelhoe_hard_carbon);
		}
		
		if (NCConfig.register_tool[3]) {
			registerItem(sword_boron_nitride);
			registerItem(pickaxe_boron_nitride);
			registerItem(shovel_boron_nitride);
			registerItem(axe_boron_nitride);
			registerItem(hoe_boron_nitride);
			registerItem(spaxelhoe_boron_nitride);
		}
	}
		
	public static void registerRenders() {
		if (NCConfig.register_tool[0]) {
			registerRender(sword_boron);
			registerRender(pickaxe_boron);
			registerRender(shovel_boron);
			registerRender(axe_boron);
			registerRender(hoe_boron);
			registerRender(spaxelhoe_boron);
		}
		
		if (NCConfig.register_tool[1]) {
			registerRender(sword_tough);
			registerRender(pickaxe_tough);
			registerRender(shovel_tough);
			registerRender(axe_tough);
			registerRender(hoe_tough);
			registerRender(spaxelhoe_tough);
		}
		
		if (NCConfig.register_tool[2]) {
			registerRender(sword_hard_carbon);
			registerRender(pickaxe_hard_carbon);
			registerRender(shovel_hard_carbon);
			registerRender(axe_hard_carbon);
			registerRender(hoe_hard_carbon);
			registerRender(spaxelhoe_hard_carbon);
		}
		
		if (NCConfig.register_tool[3]) {
			registerRender(sword_boron_nitride);
			registerRender(pickaxe_boron_nitride);
			registerRender(shovel_boron_nitride);
			registerRender(axe_boron_nitride);
			registerRender(hoe_boron_nitride);
			registerRender(spaxelhoe_boron_nitride);
		}
	}
		
	public static void registerItem(Item item) {
		item.setCreativeTab(NCTabs.MISC);
		ForgeRegistries.ITEMS.register(item);
	}
		
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	private static ToolMaterial toolMaterial(String name, int id, ItemStack repairStack) {
		return EnumHelper.addToolMaterial(Global.MOD_ID + ":" + name, NCConfig.tool_mining_level[id], NCConfig.tool_durability[id], (float) NCConfig.tool_speed[id], (float) NCConfig.tool_attack_damage[id], NCConfig.tool_enchantability[id]).setRepairItem(repairStack);
	}
}
