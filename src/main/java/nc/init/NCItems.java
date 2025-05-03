package nc.init;

import nc.*;
import nc.enumm.*;
import nc.item.*;
import nc.item.bauble.*;
import nc.item.energy.battery.*;
import nc.radiation.RadiationHelper;
import nc.tab.NCTabs;
import nc.util.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.function.Consumer;

import static nc.config.NCConfig.*;

public class NCItems {
	
	public static Item ingot;
	public static Item ingot2;
	public static Item dust;
	public static Item dust2;
	public static Item gem;
	public static Item gem_dust;
	public static Item alloy;
	public static Item compound;
	
	public static Item part;
	public static Item upgrade;
	
	public static Item fission_dust;
	
	public static Item uranium;
	public static Item neptunium;
	public static Item plutonium;
	public static Item americium;
	public static Item curium;
	public static Item berkelium;
	public static Item californium;
	
	public static Item pellet_thorium;
	public static Item pellet_uranium;
	public static Item pellet_neptunium;
	public static Item pellet_plutonium;
	public static Item pellet_mixed;
	public static Item pellet_americium;
	public static Item pellet_curium;
	public static Item pellet_berkelium;
	public static Item pellet_californium;
	
	public static Item fuel_thorium;
	public static Item fuel_uranium;
	public static Item fuel_neptunium;
	public static Item fuel_plutonium;
	public static Item fuel_mixed;
	public static Item fuel_americium;
	public static Item fuel_curium;
	public static Item fuel_berkelium;
	public static Item fuel_californium;
	
	public static Item depleted_fuel_thorium;
	public static Item depleted_fuel_uranium;
	public static Item depleted_fuel_neptunium;
	public static Item depleted_fuel_plutonium;
	public static Item depleted_fuel_mixed;
	public static Item depleted_fuel_americium;
	public static Item depleted_fuel_curium;
	public static Item depleted_fuel_berkelium;
	public static Item depleted_fuel_californium;
	
	public static Item depleted_fuel_ic2;
	
	public static Item boron;
	public static Item lithium;
	
	public static Item lithium_ion_cell;
	
	public static Item multitool;
	
	public static Item geiger_counter;
	public static Item rad_shielding;
	public static Item radiation_badge;
	
	public static Item radaway;
	public static Item radaway_slow;
	public static Item rad_x;
	
	public static Item portable_ender_chest;
	
	public static Item dominos;
	
	public static Item flour;
	public static Item graham_cracker;
	
	public static Item roasted_cocoa_beans;
	public static Item ground_cocoa_nibs;
	public static Item cocoa_butter;
	public static Item cocoa_solids;
	public static Item unsweetened_chocolate;
	public static Item dark_chocolate;
	public static Item milk_chocolate;
	
	public static Item gelatin;
	public static Item marshmallow;
	
	public static Item smore;
	public static Item moresmore;
	public static Item foursmore;
	
	public static Item record_wanderer;
	public static Item record_end_of_the_world;
	public static Item record_money_for_nothing;
	public static Item record_hyperspace;
	
	public static List<ItemRegistrationInfo<?>> registrationList = new ArrayList<>();
	
	public static void init() {
		ingot = addWithNameMeta(Global.MOD_ID, "ingot", new NCItemMeta<>(MetaEnums.IngotType.class), NCTabs.material);
		ingot2 = addWithNameMeta(Global.MOD_ID, "ingot2", new NCItemMeta<>(MetaEnums.IngotType2.class), NCTabs.material);
		dust = addWithNameMeta(Global.MOD_ID, "dust", new NCItemMeta<>(MetaEnums.IngotType.class), NCTabs.material);
		dust2 = addWithNameMeta(Global.MOD_ID, "dust2", new NCItemMeta<>(MetaEnums.IngotType2.class), NCTabs.material);
		gem = addWithNameMeta(Global.MOD_ID, "gem", new NCItemMeta<>(MetaEnums.GemType.class), NCTabs.material);
		gem_dust = addWithNameMeta(Global.MOD_ID, "gem_dust", new NCItemMeta<>(MetaEnums.GemDustType.class), NCTabs.material);
		alloy = addWithNameMeta(Global.MOD_ID, "alloy", new NCItemMeta<>(MetaEnums.AlloyType.class), NCTabs.material);
		compound = addWithNameMeta(Global.MOD_ID, "compound", new NCItemMeta<>(MetaEnums.CompoundType.class), NCTabs.material);
		
		part = addWithNameMeta(Global.MOD_ID, "part", new NCItemMeta<>(MetaEnums.PartType.class), NCTabs.material);
		upgrade = addWithNameMeta(Global.MOD_ID, "upgrade", new ItemUpgrade(MetaEnums.UpgradeType.class, NCInfo.upgradeInfo()), NCTabs.machine);
		
		fission_dust = addWithNameMeta(Global.MOD_ID, "fission_dust", new NCItemMeta<>(MetaEnums.FissionDustType.class), NCTabs.material);
		
		uranium = addWithNameMeta(Global.MOD_ID, "uranium", new NCItemMeta<>(MetaEnums.UraniumType.class), NCTabs.material);
		neptunium = addWithNameMeta(Global.MOD_ID, "neptunium", new NCItemMeta<>(MetaEnums.NeptuniumType.class), NCTabs.material);
		plutonium = addWithNameMeta(Global.MOD_ID, "plutonium", new NCItemMeta<>(MetaEnums.PlutoniumType.class), NCTabs.material);
		americium = addWithNameMeta(Global.MOD_ID, "americium", new NCItemMeta<>(MetaEnums.AmericiumType.class), NCTabs.material);
		curium = addWithNameMeta(Global.MOD_ID, "curium", new NCItemMeta<>(MetaEnums.CuriumType.class), NCTabs.material);
		berkelium = addWithNameMeta(Global.MOD_ID, "berkelium", new NCItemMeta<>(MetaEnums.BerkeliumType.class), NCTabs.material);
		californium = addWithNameMeta(Global.MOD_ID, "californium", new NCItemMeta<>(MetaEnums.CaliforniumType.class), NCTabs.material);
		
		pellet_thorium = addWithNameMeta(Global.MOD_ID, "pellet_thorium", new NCItemMeta<>(MetaEnums.ThoriumPelletType.class), NCTabs.material);
		pellet_uranium = addWithNameMeta(Global.MOD_ID, "pellet_uranium", new NCItemMeta<>(MetaEnums.UraniumPelletType.class), NCTabs.material);
		pellet_neptunium = addWithNameMeta(Global.MOD_ID, "pellet_neptunium", new NCItemMeta<>(MetaEnums.NeptuniumPelletType.class), NCTabs.material);
		pellet_plutonium = addWithNameMeta(Global.MOD_ID, "pellet_plutonium", new NCItemMeta<>(MetaEnums.PlutoniumPelletType.class), NCTabs.material);
		pellet_mixed = addWithNameMeta(Global.MOD_ID, "pellet_mixed", new NCItemMeta<>(MetaEnums.MixedPelletType.class), NCTabs.material);
		pellet_americium = addWithNameMeta(Global.MOD_ID, "pellet_americium", new NCItemMeta<>(MetaEnums.AmericiumPelletType.class), NCTabs.material);
		pellet_curium = addWithNameMeta(Global.MOD_ID, "pellet_curium", new NCItemMeta<>(MetaEnums.CuriumPelletType.class), NCTabs.material);
		pellet_berkelium = addWithNameMeta(Global.MOD_ID, "pellet_berkelium", new NCItemMeta<>(MetaEnums.BerkeliumPelletType.class), NCTabs.material);
		pellet_californium = addWithNameMeta(Global.MOD_ID, "pellet_californium", new NCItemMeta<>(MetaEnums.CaliforniumPelletType.class), NCTabs.material);
		
		fuel_thorium = addWithNameMeta(Global.MOD_ID, "fuel_thorium", new ItemFissionFuel<>(MetaEnums.ThoriumFuelType.class), NCTabs.material);
		fuel_uranium = addWithNameMeta(Global.MOD_ID, "fuel_uranium", new ItemFissionFuel<>(MetaEnums.UraniumFuelType.class), NCTabs.material);
		fuel_neptunium = addWithNameMeta(Global.MOD_ID, "fuel_neptunium", new ItemFissionFuel<>(MetaEnums.NeptuniumFuelType.class), NCTabs.material);
		fuel_plutonium = addWithNameMeta(Global.MOD_ID, "fuel_plutonium", new ItemFissionFuel<>(MetaEnums.PlutoniumFuelType.class), NCTabs.material);
		fuel_mixed = addWithNameMeta(Global.MOD_ID, "fuel_mixed", new ItemFissionFuel<>(MetaEnums.MixedFuelType.class), NCTabs.material);
		fuel_americium = addWithNameMeta(Global.MOD_ID, "fuel_americium", new ItemFissionFuel<>(MetaEnums.AmericiumFuelType.class), NCTabs.material);
		fuel_curium = addWithNameMeta(Global.MOD_ID, "fuel_curium", new ItemFissionFuel<>(MetaEnums.CuriumFuelType.class), NCTabs.material);
		fuel_berkelium = addWithNameMeta(Global.MOD_ID, "fuel_berkelium", new ItemFissionFuel<>(MetaEnums.BerkeliumFuelType.class), NCTabs.material);
		fuel_californium = addWithNameMeta(Global.MOD_ID, "fuel_californium", new ItemFissionFuel<>(MetaEnums.CaliforniumFuelType.class), NCTabs.material);
		
		depleted_fuel_thorium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_thorium", new NCItemMeta<>(MetaEnums.ThoriumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_uranium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_uranium", new NCItemMeta<>(MetaEnums.UraniumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_neptunium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_neptunium", new NCItemMeta<>(MetaEnums.NeptuniumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_plutonium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_plutonium", new NCItemMeta<>(MetaEnums.PlutoniumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_mixed = addWithNameMeta(Global.MOD_ID, "depleted_fuel_mixed", new NCItemMeta<>(MetaEnums.MixedDepletedFuelType.class), NCTabs.material);
		depleted_fuel_americium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_americium", new NCItemMeta<>(MetaEnums.AmericiumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_curium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_curium", new NCItemMeta<>(MetaEnums.CuriumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_berkelium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_berkelium", new NCItemMeta<>(MetaEnums.BerkeliumDepletedFuelType.class), NCTabs.material);
		depleted_fuel_californium = addWithNameMeta(Global.MOD_ID, "depleted_fuel_californium", new NCItemMeta<>(MetaEnums.CaliforniumDepletedFuelType.class), NCTabs.material);
		
		depleted_fuel_ic2 = addWithNameMeta(Global.MOD_ID, "depleted_fuel_ic2", new NCItemMeta<>(MetaEnums.IC2DepletedFuelType.class), NCTabs.material);
		
		boron = addWithNameMeta(Global.MOD_ID, "boron", new NCItemMeta<>(MetaEnums.BoronType.class), NCTabs.material);
		lithium = addWithNameMeta(Global.MOD_ID, "lithium", new NCItemMeta<>(MetaEnums.LithiumType.class), NCTabs.material);
		
		lithium_ion_cell = addWithName(Global.MOD_ID, "lithium_ion_cell", new ItemBattery(BatteryItemType.LITHIUM_ION_CELL), NCTabs.machine);
		
		multitool = addWithName(Global.MOD_ID, "multitool", new ItemMultitool(), NCTabs.machine);
		
		geiger_counter = addWithName(Global.MOD_ID, "geiger_counter", new ItemGeigerCounter(), NCTabs.radiation);
		rad_shielding = addWithNameMeta(Global.MOD_ID, "rad_shielding", new ItemRadShielding(NCInfo.radShieldingInfo()), NCTabs.radiation);
		radiation_badge = addWithName(Global.MOD_ID, "radiation_badge", new ItemRadiationBadge(InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "radiation_badge"), UnitHelper.prefix(radiation_badge_durability * radiation_badge_info_rate, 3, "Rad"), UnitHelper.prefix(radiation_badge_durability, 3, "Rad"))), NCTabs.radiation);
		
		radaway = addWithName(Global.MOD_ID, "radaway", new ItemRadaway(false, InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "radaway"), RadiationHelper.radsPrefix(radiation_radaway_amount, false), NCMath.pcDecimalPlaces(radiation_radaway_amount / max_player_rads, 1), RadiationHelper.radsPrefix(radiation_radaway_rate, true))), NCTabs.radiation);
		radaway_slow = addWithName(Global.MOD_ID, "radaway_slow", new ItemRadaway(true, InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "radaway"), RadiationHelper.radsPrefix(radiation_radaway_slow_amount, false), NCMath.pcDecimalPlaces(radiation_radaway_slow_amount / max_player_rads, 1), RadiationHelper.radsPrefix(radiation_radaway_slow_rate, true))), NCTabs.radiation);
		rad_x = addWithName(Global.MOD_ID, "rad_x", new ItemRadX(InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "rad_x"), RadiationHelper.resistanceSigFigs(radiation_rad_x_amount), UnitHelper.applyTimeUnit(radiation_rad_x_lifetime, 3))), NCTabs.radiation);
		
		portable_ender_chest = addWithName(Global.MOD_ID, "portable_ender_chest", new ItemPortableEnderChest(), NCTabs.misc);
		
		dominos = addWithName(Global.MOD_ID, "dominos", new NCItemFood(16, 1.8F, true, new PotionEffect[] {PotionHelper.newEffect(1, 2, 600), PotionHelper.newEffect(3, 2, 600)}), NCTabs.misc);
		
		flour = addWithName(Global.MOD_ID, "flour", new NCItem(), NCTabs.misc);
		graham_cracker = addWithName(Global.MOD_ID, "graham_cracker", new NCItemFood(1, 0.2F, false, new PotionEffect[] {}), NCTabs.misc);
		
		roasted_cocoa_beans = addWithName(Global.MOD_ID, "roasted_cocoa_beans", new NCItem(), NCTabs.misc);
		ground_cocoa_nibs = addWithName(Global.MOD_ID, "ground_cocoa_nibs", new NCItemFood(1, 0.2F, false, new PotionEffect[] {}), NCTabs.misc);
		cocoa_butter = addWithName(Global.MOD_ID, "cocoa_butter", new NCItemFood(2, 0.2F, false, new PotionEffect[] {PotionHelper.newEffect(22, 1, 300)}), NCTabs.misc);
		cocoa_solids = addWithName(Global.MOD_ID, "cocoa_solids", new NCItem(), NCTabs.misc);
		unsweetened_chocolate = addWithName(Global.MOD_ID, "unsweetened_chocolate", new NCItemFood(2, 0.2F, false, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300)}), NCTabs.misc);
		dark_chocolate = addWithName(Global.MOD_ID, "dark_chocolate", new NCItemFood(3, 0.4F, false, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300), PotionHelper.newEffect(1, 1, 300)}), NCTabs.misc);
		milk_chocolate = addWithName(Global.MOD_ID, "milk_chocolate", new NCItemFood(4, 0.6F, false, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300), PotionHelper.newEffect(1, 1, 300), PotionHelper.newEffect(22, 1, 300)}), NCTabs.misc);
		
		gelatin = addWithName(Global.MOD_ID, "gelatin", new NCItem(), NCTabs.misc);
		marshmallow = addWithName(Global.MOD_ID, "marshmallow", new NCItemFood(1, 0.4F, false, new PotionEffect[] {PotionHelper.newEffect(1, 1, 300)}), NCTabs.misc);
		
		smore = addWithName(Global.MOD_ID, "smore", new NCItemFood(8, 1.4F, false, new PotionEffect[] {PotionHelper.newEffect(3, 2, 300), PotionHelper.newEffect(1, 2, 300), PotionHelper.newEffect(22, 2, 300)}), NCTabs.misc);
		moresmore = addWithName(Global.MOD_ID, "moresmore", new NCItemFood(20, 3.8F, false, new PotionEffect[] {PotionHelper.newEffect(3, 2, 600), PotionHelper.newEffect(1, 2, 600), PotionHelper.newEffect(22, 2, 600)}), NCTabs.misc);
		foursmore = addWithName(Global.MOD_ID, "foursmore", new NCItemFood(48, 8.6F, false, new PotionEffect[] {PotionHelper.newEffect(3, 2, 1200), PotionHelper.newEffect(1, 2, 1200), PotionHelper.newEffect(22, 2, 1200)}), null);
		
		record_wanderer = addWithName(Global.MOD_ID, "record_wanderer", new NCItemRecord("record_wanderer", NCSounds.wanderer), NCTabs.misc);
		record_end_of_the_world = addWithName(Global.MOD_ID, "record_end_of_the_world", new NCItemRecord("record_end_of_the_world", NCSounds.end_of_the_world), NCTabs.misc);
		record_money_for_nothing = addWithName(Global.MOD_ID, "record_money_for_nothing", new NCItemRecord("record_money_for_nothing", NCSounds.money_for_nothing), NCTabs.misc);
		record_hyperspace = addWithName(Global.MOD_ID, "record_hyperspace", new NCItemRecord("record_hyperspace", NCSounds.hyperspace), NCTabs.misc);
	}
	
	public static void register() {
		for (ItemRegistrationInfo<?> registration : registrationList) {
			registration.registerItem.run();
		}
	}
	
	public static void registerRenders() {
		for (ItemRegistrationInfo<?> registration : registrationList) {
			registration.registerRender.run();
		}
	}
	
	// Factory
	
	public static <T extends Item & IInfoItem> T addWithName(String modId, String name, T item, Consumer<ItemRegistrationInfo<T>> registerItem, Consumer<ItemRegistrationInfo<T>> registerRender) {
		item = withName(modId, name, item);
		registrationList.add(new ItemRegistrationInfo<>(modId, name, item, registerItem, registerRender));
		return item;
	}
	
	public static <T extends Item & IInfoItem> T addWithName(String modId, String name, T item, CreativeTabs tab) {
		return addWithName(modId, name, item, x -> registerItem(x.item, tab), x -> registerRender(x.item));
	}
	
	public static <T extends Item & IInfoItem & IItemMeta<V>, V extends Enum<V> & IStringSerializable & IMetaEnum> T addWithNameMeta(String modId, String name, T item, CreativeTabs tab) {
		return addWithName(modId, name, item, x -> registerItem(x.item, tab), x -> registerRenderMeta(x.modId, x.item, x.item.getEnumClass().getEnumConstants()));
	}
	
	// Auxiliary
	
	public static String fixedLine(String modId, String name) {
		return "item." + modId + "." + name + ".fixd";
	}
	
	public static String infoLine(String modId, String name) {
		return "item." + modId + "." + name + ".desc";
	}
	
	public static <T extends Item & IInfoItem> T withName(String modId, String name, T item) {
		item.setTranslationKey(modId + "." + name).setRegistryName(new ResourceLocation(modId, name));
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
	
	public static <T extends IStringSerializable> void registerRenderMeta(String modId, Item item, T[] types) {
		registerRenderMeta(modId, item, StreamHelper.map(Arrays.asList(types), IStringSerializable::getName));
	}
	
	public static void registerRenderMeta(String modId, Item item, List<String> types) {
		ResourceLocation itemLocation = new ResourceLocation(modId, "items/" + item.getRegistryName().getPath());
		for (int i = 0; i < types.size(); ++i) {
			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(itemLocation, "type=" + types.get(i)));
		}
	}
}
