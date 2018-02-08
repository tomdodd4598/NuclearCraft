package nc.init;

import nc.Global;
import nc.config.NCConfig;
import nc.item.NCItemArmor;
import nc.proxy.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCArmor {
	
	public static final ArmorMaterial BORON = EnumHelper.addArmorMaterial("boron", Global.MOD_ID + ":boron", NCConfig.armor_durability[0], new int[] {NCConfig.armor_boron[0], NCConfig.armor_boron[1], NCConfig.armor_boron[2], NCConfig.armor_boron[3]}, NCConfig.armor_enchantability[0], SoundEvents.ITEM_ARMOR_EQUIP_IRON, (float) NCConfig.armor_toughness[0]);
	public static final ArmorMaterial TOUGH = EnumHelper.addArmorMaterial("tough", Global.MOD_ID + ":tough", NCConfig.armor_durability[1], new int[] {NCConfig.armor_tough[0], NCConfig.armor_tough[1], NCConfig.armor_tough[2], NCConfig.armor_tough[3]}, NCConfig.armor_enchantability[1], SoundEvents.ITEM_ARMOR_EQUIP_IRON, (float) NCConfig.armor_toughness[1]);
	public static final ArmorMaterial HARD_CARBON = EnumHelper.addArmorMaterial("hard_carbon", Global.MOD_ID + ":hard_carbon", NCConfig.armor_durability[2], new int[] {NCConfig.armor_hard_carbon[0], NCConfig.armor_hard_carbon[1], NCConfig.armor_hard_carbon[2], NCConfig.armor_hard_carbon[3]}, NCConfig.armor_enchantability[2], SoundEvents.ITEM_ARMOR_EQUIP_IRON, (float) NCConfig.armor_toughness[2]);
	public static final ArmorMaterial BORON_NITRIDE = EnumHelper.addArmorMaterial("boron_nitride", Global.MOD_ID + ":boron_nitride", NCConfig.armor_durability[3], new int[] {NCConfig.armor_boron_nitride[0], NCConfig.armor_boron_nitride[1], NCConfig.armor_boron_nitride[2], NCConfig.armor_boron_nitride[3]}, NCConfig.armor_enchantability[3], SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, (float) NCConfig.armor_toughness[3]);
	
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

	public static void init() {
		helm_boron = new NCItemArmor("helm_boron", BORON, 1, EntityEquipmentSlot.HEAD);
		chest_boron = new NCItemArmor("chest_boron", BORON, 1, EntityEquipmentSlot.CHEST);
		legs_boron = new NCItemArmor("legs_boron", BORON, 2, EntityEquipmentSlot.LEGS);
		boots_boron = new NCItemArmor("boots_boron", BORON, 1, EntityEquipmentSlot.FEET);
		
		helm_tough = new NCItemArmor("helm_tough", TOUGH, 1, EntityEquipmentSlot.HEAD);
		chest_tough = new NCItemArmor("chest_tough", TOUGH, 1, EntityEquipmentSlot.CHEST);
		legs_tough = new NCItemArmor("legs_tough", TOUGH, 2, EntityEquipmentSlot.LEGS);
		boots_tough = new NCItemArmor("boots_tough", TOUGH, 1, EntityEquipmentSlot.FEET);
		
		helm_hard_carbon = new NCItemArmor("helm_hard_carbon", HARD_CARBON, 1, EntityEquipmentSlot.HEAD);
		chest_hard_carbon = new NCItemArmor("chest_hard_carbon", HARD_CARBON, 1, EntityEquipmentSlot.CHEST);
		legs_hard_carbon = new NCItemArmor("legs_hard_carbon", HARD_CARBON, 2, EntityEquipmentSlot.LEGS);
		boots_hard_carbon = new NCItemArmor("boots_hard_carbon", HARD_CARBON, 1, EntityEquipmentSlot.FEET);
		
		helm_boron_nitride = new NCItemArmor("helm_boron_nitride", BORON_NITRIDE, 1, EntityEquipmentSlot.HEAD);
		chest_boron_nitride = new NCItemArmor("chest_boron_nitride", BORON_NITRIDE, 1, EntityEquipmentSlot.CHEST);
		legs_boron_nitride = new NCItemArmor("legs_boron_nitride", BORON_NITRIDE, 2, EntityEquipmentSlot.LEGS);
		boots_boron_nitride = new NCItemArmor("boots_boron_nitride", BORON_NITRIDE, 1, EntityEquipmentSlot.FEET);
	}
		
	public static void register() {
		registerItem(helm_boron);
		registerItem(chest_boron);
		registerItem(legs_boron);
		registerItem(boots_boron);
		
		registerItem(helm_tough);
		registerItem(chest_tough);
		registerItem(legs_tough);
		registerItem(boots_tough);
		
		registerItem(helm_hard_carbon);
		registerItem(chest_hard_carbon);
		registerItem(legs_hard_carbon);
		registerItem(boots_hard_carbon);
		
		registerItem(helm_boron_nitride);
		registerItem(chest_boron_nitride);
		registerItem(legs_boron_nitride);
		registerItem(boots_boron_nitride);
	}
		
	public static void registerRenders() {
		registerRender(helm_boron);
		registerRender(chest_boron);
		registerRender(legs_boron);
		registerRender(boots_boron);
		
		registerRender(helm_tough);
		registerRender(chest_tough);
		registerRender(legs_tough);
		registerRender(boots_tough);
		
		registerRender(helm_hard_carbon);
		registerRender(chest_hard_carbon);
		registerRender(legs_hard_carbon);
		registerRender(boots_hard_carbon);
		
		registerRender(helm_boron_nitride);
		registerRender(chest_boron_nitride);
		registerRender(legs_boron_nitride);
		registerRender(boots_boron_nitride);
	}
		
	public static void registerItem(Item item) {
		item.setCreativeTab(CommonProxy.TAB_MISC);
		ForgeRegistries.ITEMS.register(item);
	}
		
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
