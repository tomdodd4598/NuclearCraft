package nc.init;

import nc.Global;
import nc.config.NCConfig;
import nc.item.tool.NCAxe;
import nc.item.tool.NCHoe;
import nc.item.tool.NCPickaxe;
import nc.item.tool.NCShovel;
import nc.item.tool.NCSpaxelhoe;
import nc.item.tool.NCSword;
import nc.proxy.CommonProxy;
import nc.util.NCUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCTools {
	
	public static final ToolMaterial BORON = EnumHelper.addToolMaterial(Global.MOD_ID + ":boron", NCConfig.tool_mining_level[0], NCConfig.tool_durability[0], (float) NCConfig.tool_speed[0], (float) NCConfig.tool_attack_damage[0], NCConfig.tool_enchantability[0]);
	public static final ToolMaterial SPAXELHOE_BORON = EnumHelper.addToolMaterial(Global.MOD_ID + ":spaxelhoe_boron", NCConfig.tool_mining_level[1], NCConfig.tool_durability[1], (float) NCConfig.tool_speed[1], (float) NCConfig.tool_attack_damage[1], NCConfig.tool_enchantability[1]);
	public static final ToolMaterial TOUGH = EnumHelper.addToolMaterial(Global.MOD_ID + ":tough", NCConfig.tool_mining_level[2], NCConfig.tool_durability[2], (float) NCConfig.tool_speed[2], (float) NCConfig.tool_attack_damage[2], NCConfig.tool_enchantability[2]);
	public static final ToolMaterial SPAXELHOE_TOUGH = EnumHelper.addToolMaterial(Global.MOD_ID + ":spaxelhoe_tough", NCConfig.tool_mining_level[3], NCConfig.tool_durability[3], (float) NCConfig.tool_speed[3], (float) NCConfig.tool_attack_damage[3], NCConfig.tool_enchantability[3]);
	public static final ToolMaterial HARD_CARBON = EnumHelper.addToolMaterial(Global.MOD_ID + ":hard_carbon", NCConfig.tool_mining_level[4], NCConfig.tool_durability[4], (float) NCConfig.tool_speed[4], (float) NCConfig.tool_attack_damage[4], NCConfig.tool_enchantability[4]);
	public static final ToolMaterial SPAXELHOE_HARD_CARBON = EnumHelper.addToolMaterial(Global.MOD_ID + ":spaxelhoe_hard_carbon", NCConfig.tool_mining_level[5], NCConfig.tool_durability[5], (float) NCConfig.tool_speed[5], (float) NCConfig.tool_attack_damage[5], NCConfig.tool_enchantability[5]);
	public static final ToolMaterial BORON_NITRIDE = EnumHelper.addToolMaterial(Global.MOD_ID + ":boron_nitride", NCConfig.tool_mining_level[6], NCConfig.tool_durability[6], (float) NCConfig.tool_speed[6], (float) NCConfig.tool_attack_damage[6], NCConfig.tool_enchantability[6]);
	public static final ToolMaterial SPAXELHOE_BORON_NITRIDE = EnumHelper.addToolMaterial(Global.MOD_ID + ":spaxelhoe_boron_nitride", NCConfig.tool_mining_level[7], NCConfig.tool_durability[7], (float) NCConfig.tool_speed[7], (float) NCConfig.tool_attack_damage[7], NCConfig.tool_enchantability[7]);
	
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
		sword_boron = new NCSword("sword_boron", BORON);
		pickaxe_boron = new NCPickaxe("pickaxe_boron", BORON);
		shovel_boron = new NCShovel("shovel_boron", BORON);
		axe_boron = new NCAxe("axe_boron", BORON);
		hoe_boron = new NCHoe("hoe_boron", BORON);
		spaxelhoe_boron = new NCSpaxelhoe("spaxelhoe_boron", SPAXELHOE_BORON);
		
		sword_tough = new NCSword("sword_tough", TOUGH);
		pickaxe_tough = new NCPickaxe("pickaxe_tough", TOUGH);
		shovel_tough = new NCShovel("shovel_tough", TOUGH);
		axe_tough = new NCAxe("axe_tough", TOUGH);
		hoe_tough = new NCHoe("hoe_tough", TOUGH);
		spaxelhoe_tough = new NCSpaxelhoe("spaxelhoe_tough", SPAXELHOE_TOUGH);
		
		sword_hard_carbon = new NCSword("sword_hard_carbon", HARD_CARBON);
		pickaxe_hard_carbon = new NCPickaxe("pickaxe_hard_carbon", HARD_CARBON);
		shovel_hard_carbon = new NCShovel("shovel_hard_carbon", HARD_CARBON);
		axe_hard_carbon = new NCAxe("axe_hard_carbon", HARD_CARBON);
		hoe_hard_carbon = new NCHoe("hoe_hard_carbon", HARD_CARBON);
		spaxelhoe_hard_carbon = new NCSpaxelhoe("spaxelhoe_hard_carbon", SPAXELHOE_HARD_CARBON);
		
		sword_boron_nitride = new NCSword("sword_boron_nitride", BORON_NITRIDE);
		pickaxe_boron_nitride = new NCPickaxe("pickaxe_boron_nitride", BORON_NITRIDE);
		shovel_boron_nitride = new NCShovel("shovel_boron_nitride", BORON_NITRIDE);
		axe_boron_nitride = new NCAxe("axe_boron_nitride", BORON_NITRIDE);
		hoe_boron_nitride = new NCHoe("hoe_boron_nitride", BORON_NITRIDE);
		spaxelhoe_boron_nitride = new NCSpaxelhoe("spaxelhoe_boron_nitride", SPAXELHOE_BORON_NITRIDE);
	}
		
	public static void register() {
		registerItem(sword_boron);
		registerItem(pickaxe_boron);
		registerItem(shovel_boron);
		registerItem(axe_boron);
		registerItem(hoe_boron);
		registerItem(spaxelhoe_boron);
		
		registerItem(sword_tough);
		registerItem(pickaxe_tough);
		registerItem(shovel_tough);
		registerItem(axe_tough);
		registerItem(hoe_tough);
		registerItem(spaxelhoe_tough);
		
		registerItem(sword_hard_carbon);
		registerItem(pickaxe_hard_carbon);
		registerItem(shovel_hard_carbon);
		registerItem(axe_hard_carbon);
		registerItem(hoe_hard_carbon);
		registerItem(spaxelhoe_hard_carbon);
		
		registerItem(sword_boron_nitride);
		registerItem(pickaxe_boron_nitride);
		registerItem(shovel_boron_nitride);
		registerItem(axe_boron_nitride);
		registerItem(hoe_boron_nitride);
		registerItem(spaxelhoe_boron_nitride);
	}
		
	public static void registerRenders() {
		registerRender(sword_boron);
		registerRender(pickaxe_boron);
		registerRender(shovel_boron);
		registerRender(axe_boron);
		registerRender(hoe_boron);
		registerRender(spaxelhoe_boron);
		
		registerRender(sword_tough);
		registerRender(pickaxe_tough);
		registerRender(shovel_tough);
		registerRender(axe_tough);
		registerRender(hoe_tough);
		registerRender(spaxelhoe_tough);
		
		registerRender(sword_hard_carbon);
		registerRender(pickaxe_hard_carbon);
		registerRender(shovel_hard_carbon);
		registerRender(axe_hard_carbon);
		registerRender(hoe_hard_carbon);
		registerRender(spaxelhoe_hard_carbon);
		
		registerRender(sword_boron_nitride);
		registerRender(pickaxe_boron_nitride);
		registerRender(shovel_boron_nitride);
		registerRender(axe_boron_nitride);
		registerRender(hoe_boron_nitride);
		registerRender(spaxelhoe_boron_nitride);
	}
		
	public static void registerItem(Item item) {
		item.setCreativeTab(CommonProxy.TAB_MISC);
		GameRegistry.register(item);
		NCUtil.getLogger().info("Registered tool " + item.getUnlocalizedName().substring(5));
	}
		
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
		NCUtil.getLogger().info("Registered render for tool " + item.getUnlocalizedName().substring(5));
	}
}
