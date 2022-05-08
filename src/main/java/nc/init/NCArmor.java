package nc.init;

import static nc.config.NCConfig.*;

import nc.Global;
import nc.item.IInfoItem;
import nc.item.armor.*;
import nc.tab.NCTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCArmor {
	
	public static final ArmorMaterial BORON = armorMaterial("boron", 0, armor_boron, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new ItemStack(NCBlocks.ore, 1, 5));
	public static final ArmorMaterial TOUGH = armorMaterial("tough", 1, armor_tough, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new ItemStack(NCItems.alloy, 1, 1));
	public static final ArmorMaterial HARD_CARBON = armorMaterial("hard_carbon", 2, armor_hard_carbon, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new ItemStack(NCItems.alloy, 1, 2));
	public static final ArmorMaterial BORON_NITRIDE = armorMaterial("boron_nitride", 3, armor_boron_nitride, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, new ItemStack(NCItems.gem, 1, 1));
	public static final ArmorMaterial HAZMAT = armorMaterial("hazmat", 4, armor_hazmat, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new ItemStack(Items.LEATHER));
	
	public static Item helm_boron;
	public static Item chest_boron;
	public static Item legs_boron;
	public static Item boots_boron;
	
	public static Item helm_tough;
	public static Item chest_tough;
	public static Item legs_tough;
	public static Item boots_tough;
	
	public static Item helm_hard_carbon;
	public static Item chest_hard_carbon;
	public static Item legs_hard_carbon;
	public static Item boots_hard_carbon;
	
	public static Item helm_boron_nitride;
	public static Item chest_boron_nitride;
	public static Item legs_boron_nitride;
	public static Item boots_boron_nitride;
	
	public static Item helm_hazmat;
	public static Item chest_hazmat;
	public static Item legs_hazmat;
	public static Item boots_hazmat;
	
	public static void init() {
		if (register_armor[0]) {
			helm_boron = withName(new NCItemArmor(BORON, 1, EntityEquipmentSlot.HEAD, TextFormatting.GRAY), "helm_boron");
			chest_boron = withName(new NCItemArmor(BORON, 1, EntityEquipmentSlot.CHEST, TextFormatting.GRAY), "chest_boron");
			legs_boron = withName(new NCItemArmor(BORON, 2, EntityEquipmentSlot.LEGS, TextFormatting.GRAY), "legs_boron");
			boots_boron = withName(new NCItemArmor(BORON, 1, EntityEquipmentSlot.FEET, TextFormatting.GRAY), "boots_boron");
		}
		
		if (register_armor[1]) {
			helm_tough = withName(new NCItemArmor(TOUGH, 1, EntityEquipmentSlot.HEAD, TextFormatting.DARK_PURPLE), "helm_tough");
			chest_tough = withName(new NCItemArmor(TOUGH, 1, EntityEquipmentSlot.CHEST, TextFormatting.DARK_PURPLE), "chest_tough");
			legs_tough = withName(new NCItemArmor(TOUGH, 2, EntityEquipmentSlot.LEGS, TextFormatting.DARK_PURPLE), "legs_tough");
			boots_tough = withName(new NCItemArmor(TOUGH, 1, EntityEquipmentSlot.FEET, TextFormatting.DARK_PURPLE), "boots_tough");
		}
		
		if (register_armor[2]) {
			helm_hard_carbon = withName(new NCItemArmor(HARD_CARBON, 1, EntityEquipmentSlot.HEAD, TextFormatting.BLUE), "helm_hard_carbon");
			chest_hard_carbon = withName(new NCItemArmor(HARD_CARBON, 1, EntityEquipmentSlot.CHEST, TextFormatting.BLUE), "chest_hard_carbon");
			legs_hard_carbon = withName(new NCItemArmor(HARD_CARBON, 2, EntityEquipmentSlot.LEGS, TextFormatting.BLUE), "legs_hard_carbon");
			boots_hard_carbon = withName(new NCItemArmor(HARD_CARBON, 1, EntityEquipmentSlot.FEET, TextFormatting.BLUE), "boots_hard_carbon");
		}
		
		if (register_armor[3]) {
			helm_boron_nitride = withName(new NCItemArmor(BORON_NITRIDE, 1, EntityEquipmentSlot.HEAD, TextFormatting.GREEN), "helm_boron_nitride");
			chest_boron_nitride = withName(new NCItemArmor(BORON_NITRIDE, 1, EntityEquipmentSlot.CHEST, TextFormatting.GREEN), "chest_boron_nitride");
			legs_boron_nitride = withName(new NCItemArmor(BORON_NITRIDE, 2, EntityEquipmentSlot.LEGS, TextFormatting.GREEN), "legs_boron_nitride");
			boots_boron_nitride = withName(new NCItemArmor(BORON_NITRIDE, 1, EntityEquipmentSlot.FEET, TextFormatting.GREEN), "boots_boron_nitride");
		}
		
		helm_hazmat = withName(new ItemHazmatSuit(HAZMAT, 1, EntityEquipmentSlot.HEAD, 0.2D, TextFormatting.YELLOW), "helm_hazmat");
		chest_hazmat = withName(new ItemHazmatSuit(HAZMAT, 1, EntityEquipmentSlot.CHEST, 0.4D, TextFormatting.YELLOW), "chest_hazmat");
		legs_hazmat = withName(new ItemHazmatSuit(HAZMAT, 2, EntityEquipmentSlot.LEGS, 0.2D, TextFormatting.YELLOW), "legs_hazmat");
		boots_hazmat = withName(new ItemHazmatSuit(HAZMAT, 1, EntityEquipmentSlot.FEET, 0.2D, TextFormatting.YELLOW), "boots_hazmat");
	}
	
	public static void register() {
		if (register_armor[0]) {
			registerItem(helm_boron, NCTabs.misc());
			registerItem(chest_boron, NCTabs.misc());
			registerItem(legs_boron, NCTabs.misc());
			registerItem(boots_boron, NCTabs.misc());
		}
		
		if (register_armor[1]) {
			registerItem(helm_tough, NCTabs.misc());
			registerItem(chest_tough, NCTabs.misc());
			registerItem(legs_tough, NCTabs.misc());
			registerItem(boots_tough, NCTabs.misc());
		}
		
		if (register_armor[2]) {
			registerItem(helm_hard_carbon, NCTabs.misc());
			registerItem(chest_hard_carbon, NCTabs.misc());
			registerItem(legs_hard_carbon, NCTabs.misc());
			registerItem(boots_hard_carbon, NCTabs.misc());
		}
		
		if (register_armor[3]) {
			registerItem(helm_boron_nitride, NCTabs.misc());
			registerItem(chest_boron_nitride, NCTabs.misc());
			registerItem(legs_boron_nitride, NCTabs.misc());
			registerItem(boots_boron_nitride, NCTabs.misc());
		}
		
		registerItem(helm_hazmat, NCTabs.radiation());
		registerItem(chest_hazmat, NCTabs.radiation());
		registerItem(legs_hazmat, NCTabs.radiation());
		registerItem(boots_hazmat, NCTabs.radiation());
	}
	
	public static void registerRenders() {
		if (register_armor[0]) {
			registerRender(helm_boron);
			registerRender(chest_boron);
			registerRender(legs_boron);
			registerRender(boots_boron);
		}
		
		if (register_armor[1]) {
			registerRender(helm_tough);
			registerRender(chest_tough);
			registerRender(legs_tough);
			registerRender(boots_tough);
		}
		
		if (register_armor[2]) {
			registerRender(helm_hard_carbon);
			registerRender(chest_hard_carbon);
			registerRender(legs_hard_carbon);
			registerRender(boots_hard_carbon);
		}
		
		if (register_armor[3]) {
			registerRender(helm_boron_nitride);
			registerRender(chest_boron_nitride);
			registerRender(legs_boron_nitride);
			registerRender(boots_boron_nitride);
		}
		
		registerRender(helm_hazmat);
		registerRender(chest_hazmat);
		registerRender(legs_hazmat);
		registerRender(boots_hazmat);
	}
	
	public static <T extends Item & IInfoItem> Item withName(T item, String name) {
		item.setTranslationKey(Global.MOD_ID + "." + name).setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		item.setInfo();
		return item;
	}
	
	public static void registerItem(Item item, CreativeTabs tab) {
		item.setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item);
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	public static ArmorMaterial armorMaterial(String name, int id, int[] durability, SoundEvent equipSound, ItemStack repairStack) {
		return EnumHelper.addArmorMaterial(name, Global.MOD_ID + ":" + name, armor_durability[id], new int[] {durability[0], durability[1], durability[2], durability[3]}, armor_enchantability[id], equipSound, (float) armor_toughness[id]).setRepairItem(repairStack);
	}
}
