package nc.init;

import nc.Global;
import nc.item.armor.NCItemArmor;
import nc.proxy.CommonProxy;
import nc.util.NCUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCArmor {
	
	public static final ArmorMaterial BORON = EnumHelper.addArmorMaterial("boron", Global.MOD_ID + ":boron", 22, new int[] {2, 5, 7, 3}, 6, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
	public static final ArmorMaterial TOUGH = EnumHelper.addArmorMaterial("tough", Global.MOD_ID + ":tough", 30, new int[] {3, 6, 7, 3}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
	
	public static ItemArmor helm_boron;
	public static ItemArmor chest_boron;
	public static ItemArmor legs_boron;
	public static ItemArmor boots_boron;
	
	public static ItemArmor helm_tough;
	public static ItemArmor chest_tough;
	public static ItemArmor legs_tough;
	public static ItemArmor boots_tough;

	public static void init() {
		helm_boron = new NCItemArmor("helm_boron", BORON, 1, EntityEquipmentSlot.HEAD);
		chest_boron = new NCItemArmor("chest_boron", BORON, 1, EntityEquipmentSlot.CHEST);
		legs_boron = new NCItemArmor("legs_boron", BORON, 2, EntityEquipmentSlot.LEGS);
		boots_boron = new NCItemArmor("boots_boron", BORON, 1, EntityEquipmentSlot.FEET);
		
		helm_tough = new NCItemArmor("helm_tough", TOUGH, 1, EntityEquipmentSlot.HEAD);
		chest_tough = new NCItemArmor("chest_tough", TOUGH, 1, EntityEquipmentSlot.CHEST);
		legs_tough = new NCItemArmor("legs_tough", TOUGH, 2, EntityEquipmentSlot.LEGS);
		boots_tough = new NCItemArmor("boots_tough", TOUGH, 1, EntityEquipmentSlot.FEET);
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
	}
		
	public static void registerItem(Item item) {
		item.setCreativeTab(CommonProxy.NC_TAB);
		GameRegistry.register(item);
		NCUtils.getLogger().info("Registered armor piece " + item.getUnlocalizedName().substring(5));
	}
		
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
		NCUtils.getLogger().info("Registered render for armor piece " + item.getUnlocalizedName().substring(5));
	}
}
