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
import nc.util.NCUtils;
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
	public static final ToolMaterial TOUGH = EnumHelper.addToolMaterial(Global.MOD_ID + ":tough", NCConfig.tool_mining_level[1], NCConfig.tool_durability[1], (float) NCConfig.tool_speed[1], (float) NCConfig.tool_attack_damage[1], NCConfig.tool_enchantability[1]);
	public static final ToolMaterial SPAXELHOE_TOUGH = EnumHelper.addToolMaterial(Global.MOD_ID + ":spaxelhoe_tough", NCConfig.tool_mining_level[2], NCConfig.tool_durability[2], (float) NCConfig.tool_speed[2], (float) NCConfig.tool_attack_damage[2], NCConfig.tool_enchantability[2]);
	
	public static ItemSword sword_boron;
	public static ItemPickaxe pickaxe_boron;
	public static ItemSpade shovel_boron;
	public static NCAxe axe_boron;
	public static ItemHoe hoe_boron;
	
	public static ItemSword sword_tough;
	public static ItemPickaxe pickaxe_tough;
	public static ItemSpade shovel_tough;
	public static NCAxe axe_tough;
	public static ItemHoe hoe_tough;
	public static NCSpaxelhoe spaxelhoe_tough;
	
	public static void init() {
		sword_boron = new NCSword("sword_boron", BORON);
		pickaxe_boron = new NCPickaxe("pickaxe_boron", BORON);
		shovel_boron = new NCShovel("shovel_boron", BORON);
		axe_boron = new NCAxe("axe_boron", BORON);
		hoe_boron = new NCHoe("hoe_boron", BORON);
		
		sword_tough = new NCSword("sword_tough", TOUGH);
		pickaxe_tough = new NCPickaxe("pickaxe_tough", TOUGH);
		shovel_tough = new NCShovel("shovel_tough", TOUGH);
		axe_tough = new NCAxe("axe_tough", TOUGH);
		hoe_tough = new NCHoe("hoe_tough", TOUGH);
		spaxelhoe_tough = new NCSpaxelhoe("spaxelhoe_tough", SPAXELHOE_TOUGH);
	}
		
	public static void register() {
		registerItem(sword_boron);
		registerItem(pickaxe_boron);
		registerItem(shovel_boron);
		registerItem(axe_boron);
		registerItem(hoe_boron);
		
		registerItem(sword_tough);
		registerItem(pickaxe_tough);
		registerItem(shovel_tough);
		registerItem(axe_tough);
		registerItem(hoe_tough);
		registerItem(spaxelhoe_tough);
	}
		
	public static void registerRenders() {
		registerRender(sword_boron);
		registerRender(pickaxe_boron);
		registerRender(shovel_boron);
		registerRender(axe_boron);
		registerRender(hoe_boron);
		
		registerRender(sword_tough);
		registerRender(pickaxe_tough);
		registerRender(shovel_tough);
		registerRender(axe_tough);
		registerRender(hoe_tough);
		registerRender(spaxelhoe_tough);
	}
		
	public static void registerItem(Item item) {
		item.setCreativeTab(CommonProxy.NC_TAB);
		GameRegistry.register(item);
		NCUtils.getLogger().info("Registered tool " + item.getUnlocalizedName().substring(5));
	}
		
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
		NCUtils.getLogger().info("Registered render for tool " + item.getUnlocalizedName().substring(5));
	}
}
