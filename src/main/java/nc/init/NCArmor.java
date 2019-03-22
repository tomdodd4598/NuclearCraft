package nc.init;

import nc.Global;
import nc.config.NCConfig;
import nc.item.armor.ItemHazmatSuit;
import nc.item.armor.NCItemArmor;
import nc.tab.NCTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCArmor {
	
	public static final ArmorMaterial BORON = armorMaterial("boron", 0, NCConfig.armor_boron, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new ItemStack(NCBlocks.ore, 1, 5));
	public static final ArmorMaterial TOUGH = armorMaterial("tough", 1, NCConfig.armor_tough, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new ItemStack(NCItems.alloy, 1, 1));
	public static final ArmorMaterial HARD_CARBON = armorMaterial("hard_carbon", 2, NCConfig.armor_hard_carbon, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new ItemStack(NCItems.alloy, 1, 2));
	public static final ArmorMaterial BORON_NITRIDE = armorMaterial("boron_nitride", 3, NCConfig.armor_boron_nitride, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, new ItemStack(NCItems.gem, 1, 1));
	public static final ArmorMaterial HAZMAT = EnumHelper.addArmorMaterial("hazmat", Global.MOD_ID + ":hazmat", 0, NCConfig.armor_tough, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);
	
	public static ItemArmor helm_boron;
	public static ItemArmor chest_boron;
	public static ItemArmor legs_boron;
	public static ItemArmor boots_boron;
	
	public static ItemArmor helm_tough;
	public static ItemArmor chest_tough;
	public static ItemArmor legs_tough;
	public static ItemArmor boots_tough;

	public static ItemArmor helm_hard_carbon;
	public static ItemArmor chest_hard_carbon;
	public static ItemArmor legs_hard_carbon;
	public static ItemArmor boots_hard_carbon;

	public static ItemArmor helm_boron_nitride;
	public static ItemArmor chest_boron_nitride;
	public static ItemArmor legs_boron_nitride;
	public static ItemArmor boots_boron_nitride;

	public static ItemArmor helm_hazmat;
	public static ItemArmor chest_hazmat;
	public static ItemArmor legs_hazmat;
	public static ItemArmor boots_hazmat;

	public static void init() {
		if (NCConfig.register_armor[0]) {
			helm_boron = new NCItemArmor("helm_boron", BORON, 1, EntityEquipmentSlot.HEAD);
			chest_boron = new NCItemArmor("chest_boron", BORON, 1, EntityEquipmentSlot.CHEST);
			legs_boron = new NCItemArmor("legs_boron", BORON, 2, EntityEquipmentSlot.LEGS);
			boots_boron = new NCItemArmor("boots_boron", BORON, 1, EntityEquipmentSlot.FEET);
		}
		
		if (NCConfig.register_armor[1]) {
			helm_tough = new NCItemArmor("helm_tough", TOUGH, 1, EntityEquipmentSlot.HEAD);
			chest_tough = new NCItemArmor("chest_tough", TOUGH, 1, EntityEquipmentSlot.CHEST);
			legs_tough = new NCItemArmor("legs_tough", TOUGH, 2, EntityEquipmentSlot.LEGS);
			boots_tough = new NCItemArmor("boots_tough", TOUGH, 1, EntityEquipmentSlot.FEET);
		}
		
		if (NCConfig.register_armor[2]) {
			helm_hard_carbon = new NCItemArmor("helm_hard_carbon", HARD_CARBON, 1, EntityEquipmentSlot.HEAD);
			chest_hard_carbon = new NCItemArmor("chest_hard_carbon", HARD_CARBON, 1, EntityEquipmentSlot.CHEST);
			legs_hard_carbon = new NCItemArmor("legs_hard_carbon", HARD_CARBON, 2, EntityEquipmentSlot.LEGS);
			boots_hard_carbon = new NCItemArmor("boots_hard_carbon", HARD_CARBON, 1, EntityEquipmentSlot.FEET);
		}
		
		if (NCConfig.register_armor[3]) {
			helm_boron_nitride = new NCItemArmor("helm_boron_nitride", BORON_NITRIDE, 1, EntityEquipmentSlot.HEAD);
			chest_boron_nitride = new NCItemArmor("chest_boron_nitride", BORON_NITRIDE, 1, EntityEquipmentSlot.CHEST);
			legs_boron_nitride = new NCItemArmor("legs_boron_nitride", BORON_NITRIDE, 2, EntityEquipmentSlot.LEGS);
			boots_boron_nitride = new NCItemArmor("boots_boron_nitride", BORON_NITRIDE, 1, EntityEquipmentSlot.FEET);
		}
		
		helm_hazmat = new ItemHazmatSuit("helm_hazmat", HAZMAT, 1, EntityEquipmentSlot.HEAD, 0.2D);
		chest_hazmat = new ItemHazmatSuit("chest_hazmat", HAZMAT, 1, EntityEquipmentSlot.CHEST, 0.4D);
		legs_hazmat = new ItemHazmatSuit("legs_hazmat", HAZMAT, 2, EntityEquipmentSlot.LEGS, 0.2D);
		boots_hazmat = new ItemHazmatSuit("boots_hazmat", HAZMAT, 1, EntityEquipmentSlot.FEET, 0.2D);
	}
		
	public static void register() {
		if (NCConfig.register_armor[0]) {
			registerItem(helm_boron);
			registerItem(chest_boron);
			registerItem(legs_boron);
			registerItem(boots_boron);
		}
		
		if (NCConfig.register_armor[1]) {
			registerItem(helm_tough);
			registerItem(chest_tough);
			registerItem(legs_tough);
			registerItem(boots_tough);
		}
		
		if (NCConfig.register_armor[2]) {
			registerItem(helm_hard_carbon);
			registerItem(chest_hard_carbon);
			registerItem(legs_hard_carbon);
			registerItem(boots_hard_carbon);
		}
		
		if (NCConfig.register_armor[3]) {
			registerItem(helm_boron_nitride);
			registerItem(chest_boron_nitride);
			registerItem(legs_boron_nitride);
			registerItem(boots_boron_nitride);
		}
		
		registerItem(helm_hazmat, NCTabs.RADIATION);
		registerItem(chest_hazmat, NCTabs.RADIATION);
		registerItem(legs_hazmat, NCTabs.RADIATION);
		registerItem(boots_hazmat, NCTabs.RADIATION);
	}
		
	public static void registerRenders() {
		if (NCConfig.register_armor[0]) {
			registerRender(helm_boron);
			registerRender(chest_boron);
			registerRender(legs_boron);
			registerRender(boots_boron);
		}
		
		if (NCConfig.register_armor[1]) {
			registerRender(helm_tough);
			registerRender(chest_tough);
			registerRender(legs_tough);
			registerRender(boots_tough);
		}
		
		if (NCConfig.register_armor[2]) {
			registerRender(helm_hard_carbon);
			registerRender(chest_hard_carbon);
			registerRender(legs_hard_carbon);
			registerRender(boots_hard_carbon);
		}
		
		if (NCConfig.register_armor[3]) {
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
	
	public static void registerItem(Item item, CreativeTabs tab) {
		item.setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item);
	}
		
	public static void registerItem(Item item) {
		registerItem(item, NCTabs.MISC);
	}
		
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	private static ArmorMaterial armorMaterial(String name, int id, int[] durability, SoundEvent equipSound, ItemStack repairStack) {
		return EnumHelper.addArmorMaterial(name, Global.MOD_ID + ":" + name, NCConfig.armor_durability[id], new int[] {durability[0], durability[1], durability[2], durability[3]}, NCConfig.armor_enchantability[id], equipSound, (float) NCConfig.armor_toughness[id]).setRepairItem(repairStack);
	}
}
