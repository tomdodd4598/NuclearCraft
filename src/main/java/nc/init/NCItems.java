package nc.init;

import nc.Global;
import nc.NCInfo;
import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.item.*;
import nc.item.bauble.ItemGeigerCounter;
import nc.item.bauble.ItemRadiationBadge;
import nc.item.energy.ItemBattery;
import nc.radiation.RadiationHelper;
import nc.tab.NCTabs;
import nc.tile.energy.battery.BatteryType;
import nc.util.InfoHelper;
import nc.util.PotionHelper;
import nc.util.UnitHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCItems {

	public static Item ingot;
	public static Item ingot_oxide;
	public static Item dust;
	public static Item dust_oxide;
	public static Item gem;
	public static Item gem_dust;
	public static Item alloy;
	public static Item compound;
	
	public static Item part;
	public static Item upgrade;
	public static Item tiny_dust_lead;
	public static Item fuel_rod;
	public static Item reactor_door;
	
	public static Item thorium;
	public static Item uranium;
	public static Item neptunium;
	public static Item plutonium;
	public static Item americium;
	public static Item curium;
	public static Item berkelium;
	public static Item californium;
	
	public static Item fuel_thorium;
	public static Item fuel_uranium;
	public static Item fuel_neptunium;
	public static Item fuel_plutonium;
	public static Item fuel_mixed_oxide;
	public static Item fuel_americium;
	public static Item fuel_curium;
	public static Item fuel_berkelium;
	public static Item fuel_californium;
	
	public static Item depleted_fuel_thorium;
	public static Item depleted_fuel_uranium;
	public static Item depleted_fuel_neptunium;
	public static Item depleted_fuel_plutonium;
	public static Item depleted_fuel_mixed_oxide;
	public static Item depleted_fuel_americium;
	public static Item depleted_fuel_curium;
	public static Item depleted_fuel_berkelium;
	public static Item depleted_fuel_californium;
	
	public static Item depleted_fuel_ic2;
	
	public static Item boron;
	public static Item lithium;
	
	public static Item lithium_ion_cell;
	
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

	public static Item configuration_blueprint_empty;
	public static Item configuration_blueprint;
	
	public static void init() {
		ingot = withName(new NCItemMeta(MetaEnums.IngotType.class), "ingot");
		ingot_oxide = withName(new NCItemMeta(MetaEnums.IngotOxideType.class), "ingot_oxide");
		dust = withName(new NCItemMeta(MetaEnums.DustType.class, NCInfo.dustOreDropInfo()), "dust");
		dust_oxide = withName(new NCItemMeta(MetaEnums.DustOxideType.class), "dust_oxide");
		gem = withName(new NCItemMeta(MetaEnums.GemType.class, NCInfo.gemOreDropInfo()), "gem");
		gem_dust = withName(new NCItemMeta(MetaEnums.GemDustType.class, NCInfo.gemDustOreDropInfo()), "gem_dust");
		alloy = withName(new NCItemMeta(MetaEnums.AlloyType.class), "alloy");
		compound = withName(new NCItemMeta(MetaEnums.CompoundType.class), "compound");
		
		part = withName(new NCItemMeta(MetaEnums.PartType.class), "part");
		upgrade = withName(new NCItemMeta(MetaEnums.UpgradeType.class, NCInfo.upgradeInfo()), "upgrade");
		
		tiny_dust_lead = withName(new NCItem(), "tiny_dust_lead");
		fuel_rod = withName(new NCItem(), "fuel_rod");
		reactor_door = withName(new NCItemDoor(NCBlocks.reactor_door), "reactor_door_item");
		
		thorium = withName(new NCItemMeta(MetaEnums.ThoriumType.class), "thorium");
		uranium = withName(new NCItemMeta(MetaEnums.UraniumType.class), "uranium");
		neptunium = withName(new NCItemMeta(MetaEnums.NeptuniumType.class), "neptunium");
		plutonium = withName(new NCItemMeta(MetaEnums.PlutoniumType.class), "plutonium");
		americium = withName(new NCItemMeta(MetaEnums.AmericiumType.class), "americium");
		curium = withName(new NCItemMeta(MetaEnums.CuriumType.class), "curium");
		berkelium = withName(new NCItemMeta(MetaEnums.BerkeliumType.class), "berkelium");
		californium = withName(new NCItemMeta(MetaEnums.CaliforniumType.class), "californium");
		
		fuel_thorium = withName(new ItemFissionFuel(MetaEnums.ThoriumFuelType.class), "fuel_thorium");
		fuel_uranium = withName(new ItemFissionFuel(MetaEnums.UraniumFuelType.class), "fuel_uranium");
		fuel_neptunium = withName(new ItemFissionFuel(MetaEnums.NeptuniumFuelType.class), "fuel_neptunium");
		fuel_plutonium = withName(new ItemFissionFuel(MetaEnums.PlutoniumFuelType.class), "fuel_plutonium");
		fuel_mixed_oxide = withName(new ItemFissionFuel(MetaEnums.MixedOxideFuelType.class), "fuel_mixed_oxide");
		fuel_americium = withName(new ItemFissionFuel(MetaEnums.AmericiumFuelType.class), "fuel_americium");
		fuel_curium = withName(new ItemFissionFuel(MetaEnums.CuriumFuelType.class), "fuel_curium");
		fuel_berkelium = withName(new ItemFissionFuel(MetaEnums.BerkeliumFuelType.class), "fuel_berkelium");
		fuel_californium = withName(new ItemFissionFuel(MetaEnums.CaliforniumFuelType.class), "fuel_californium");
		
		depleted_fuel_thorium = withName(new ItemDepletedFissionFuel(MetaEnums.ThoriumDepletedFuelType.class), "depleted_fuel_thorium");
		depleted_fuel_uranium = withName(new ItemDepletedFissionFuel(MetaEnums.UraniumDepletedFuelType.class), "depleted_fuel_uranium");
		depleted_fuel_neptunium = withName(new ItemDepletedFissionFuel(MetaEnums.NeptuniumDepletedFuelType.class), "depleted_fuel_neptunium");
		depleted_fuel_plutonium = withName(new ItemDepletedFissionFuel(MetaEnums.PlutoniumDepletedFuelType.class), "depleted_fuel_plutonium");
		depleted_fuel_mixed_oxide = withName(new ItemDepletedFissionFuel(MetaEnums.MixedOxideDepletedFuelType.class), "depleted_fuel_mixed_oxide");
		depleted_fuel_americium = withName(new ItemDepletedFissionFuel(MetaEnums.AmericiumDepletedFuelType.class), "depleted_fuel_americium");
		depleted_fuel_curium = withName(new ItemDepletedFissionFuel(MetaEnums.CuriumDepletedFuelType.class), "depleted_fuel_curium");
		depleted_fuel_berkelium = withName(new ItemDepletedFissionFuel(MetaEnums.BerkeliumDepletedFuelType.class), "depleted_fuel_berkelium");
		depleted_fuel_californium = withName(new ItemDepletedFissionFuel(MetaEnums.CaliforniumDepletedFuelType.class), "depleted_fuel_californium");
		
		depleted_fuel_ic2 = withName(new ItemDepletedFissionFuel(MetaEnums.IC2DepletedFuelType.class), "depleted_fuel_ic2");
		
		boron = withName(new NCItemMeta(MetaEnums.BoronType.class), "boron");
		lithium = withName(new NCItemMeta(MetaEnums.LithiumType.class), "lithium");
		
		lithium_ion_cell = withName(new ItemBattery(BatteryType.LITHIUM_ION_BATTERY_BASIC), "lithium_ion_cell");
		
		geiger_counter = withName(new ItemGeigerCounter(), "geiger_counter");
		rad_shielding = withName(new ItemRadShielding(NCInfo.radShieldingInfo()), "rad_shielding");
		radiation_badge = withName(new ItemRadiationBadge(InfoHelper.formattedInfo(infoLine("radiation_badge"), UnitHelper.prefix(NCConfig.radiation_badge_durability*NCConfig.radiation_badge_info_rate, 3, "Rads"), UnitHelper.prefix(NCConfig.radiation_badge_durability, 3, "Rads"))), "radiation_badge");
		
		radaway = withName(new ItemRadaway(false, InfoHelper.formattedInfo(infoLine("radaway"), RadiationHelper.radsPrefix(NCConfig.radiation_radaway_amount, false), Math.round(100D*NCConfig.radiation_radaway_amount/NCConfig.max_player_rads) + "%", RadiationHelper.radsPrefix(NCConfig.radiation_radaway_rate, true))), "radaway");
		radaway_slow = withName(new ItemRadaway(true, InfoHelper.formattedInfo(infoLine("radaway"), RadiationHelper.radsPrefix(NCConfig.radiation_radaway_slow_amount, false), Math.round(100D*NCConfig.radiation_radaway_slow_amount/NCConfig.max_player_rads) + "%", RadiationHelper.radsPrefix(NCConfig.radiation_radaway_slow_rate, true))), "radaway_slow");
		rad_x = withName(new ItemRadX(InfoHelper.formattedInfo(infoLine("rad_x"), RadiationHelper.resistanceSigFigs(NCConfig.radiation_rad_x_amount), UnitHelper.applyTimeUnit(NCConfig.radiation_rad_x_lifetime, 2))), "rad_x");
		
		portable_ender_chest = withName(new ItemPortableEnderChest(), "portable_ender_chest");
		
		dominos = withName(new NCItemFood(16, 1.8F, new PotionEffect[] {PotionHelper.newEffect(1, 2, 600), PotionHelper.newEffect(3, 2, 600)}), "dominos");
		
		flour = withName(new NCItem(), "flour");
		graham_cracker = withName(new NCItemFood(1, 0.2F, new PotionEffect[] {}), "graham_cracker");
		
		roasted_cocoa_beans = withName(new NCItem(), "roasted_cocoa_beans");
		ground_cocoa_nibs = withName(new NCItemFood(1, 0.2F, new PotionEffect[] {}), "ground_cocoa_nibs");
		cocoa_butter = withName(new NCItemFood(2, 0.2F, new PotionEffect[] {PotionHelper.newEffect(22, 1, 300)}), "cocoa_butter");
		cocoa_solids = withName(new NCItem(), "cocoa_solids");
		unsweetened_chocolate = withName(new NCItemFood(2, 0.2F, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300)}), "unsweetened_chocolate");
		dark_chocolate = withName(new NCItemFood(3, 0.4F, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300), PotionHelper.newEffect(1, 1, 300)}), "dark_chocolate");
		milk_chocolate = withName(new NCItemFood(4, 0.6F, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300), PotionHelper.newEffect(1, 1, 300), PotionHelper.newEffect(22, 1, 300)}), "milk_chocolate");
		
		gelatin = withName(new NCItem(), "gelatin");
		marshmallow = withName(new NCItemFood(1, 0.4F, new PotionEffect[] {PotionHelper.newEffect(1, 1, 300)}), "marshmallow");
		
		smore = withName(new NCItemFood(8, 1.4F, new PotionEffect[] {PotionHelper.newEffect(3, 2, 300), PotionHelper.newEffect(1, 2, 300), PotionHelper.newEffect(22, 2, 300)}), "smore");
		moresmore = withName(new NCItemFood(20, 3.8F, new PotionEffect[] {PotionHelper.newEffect(3, 2, 600), PotionHelper.newEffect(1, 2, 600), PotionHelper.newEffect(22, 2, 600)}), "moresmore");
		foursmore = withName(new NCItemFood(44, 8.6F, new PotionEffect[] {PotionHelper.newEffect(3, 2, 1200), PotionHelper.newEffect(1, 2, 1200), PotionHelper.newEffect(22, 2, 1200)}), "foursmore");
		
		record_wanderer = withName(new NCItemRecord("record_wanderer", NCSounds.wanderer), "record_wanderer");
		record_end_of_the_world = withName(new NCItemRecord("record_end_of_the_world", NCSounds.end_of_the_world), "record_end_of_the_world");
		record_money_for_nothing = withName(new NCItemRecord("record_money_for_nothing", NCSounds.money_for_nothing), "record_money_for_nothing");
		record_hyperspace = withName(new NCItemRecord("record_hyperspace", NCSounds.hyperspace), "record_hyperspace");

		configuration_blueprint_empty = withName(new ItemConfigurationBlueprint(true), "configuration_blueprint_empty");
		configuration_blueprint = withName(new ItemConfigurationBlueprint(false), "configuration_blueprint");
	}
	
	public static void register() {
		registerItem(ingot, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(ingot_oxide, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(dust, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(dust_oxide, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(gem, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(gem_dust, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(alloy, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(compound, NCTabs.BASE_ITEM_MATERIALS);
		
		registerItem(part, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(upgrade, NCTabs.MACHINES);
		registerItem(tiny_dust_lead, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(fuel_rod, null);
		registerItem(reactor_door, NCTabs.FISSION_BLOCKS);
		
		registerItem(thorium, NCTabs.FISSION_MATERIALS);
		registerItem(uranium, NCTabs.FISSION_MATERIALS);
		registerItem(neptunium, NCTabs.FISSION_MATERIALS);
		registerItem(plutonium, NCTabs.FISSION_MATERIALS);
		registerItem(americium, NCTabs.FISSION_MATERIALS);
		registerItem(curium, NCTabs.FISSION_MATERIALS);
		registerItem(berkelium, NCTabs.FISSION_MATERIALS);
		registerItem(californium, NCTabs.FISSION_MATERIALS);
		
		registerItem(fuel_thorium, NCTabs.FISSION_FUELS);
		registerItem(fuel_uranium, NCTabs.FISSION_FUELS);
		registerItem(fuel_neptunium, NCTabs.FISSION_FUELS);
		registerItem(fuel_plutonium, NCTabs.FISSION_FUELS);
		registerItem(fuel_mixed_oxide, NCTabs.FISSION_FUELS);
		registerItem(fuel_americium, NCTabs.FISSION_FUELS);
		registerItem(fuel_curium, NCTabs.FISSION_FUELS);
		registerItem(fuel_berkelium, NCTabs.FISSION_FUELS);
		registerItem(fuel_californium, NCTabs.FISSION_FUELS);
		
		registerItem(depleted_fuel_thorium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_uranium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_neptunium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_plutonium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_mixed_oxide, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_americium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_curium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_berkelium, NCTabs.FISSION_MATERIALS);
		registerItem(depleted_fuel_californium, NCTabs.FISSION_MATERIALS);
		
		registerItem(depleted_fuel_ic2, NCTabs.FISSION_MATERIALS);
		
		registerItem(boron, NCTabs.BASE_ITEM_MATERIALS);
		registerItem(lithium, NCTabs.BASE_ITEM_MATERIALS);
		
		registerItem(lithium_ion_cell, NCTabs.MACHINES);
		
		registerItem(geiger_counter, NCTabs.RADIATION);
		registerItem(rad_shielding, NCTabs.RADIATION);
		registerItem(radiation_badge, NCTabs.RADIATION);
		
		registerItem(radaway, NCTabs.RADIATION);
		registerItem(radaway_slow, NCTabs.RADIATION);
		registerItem(rad_x, NCTabs.RADIATION);
		
		registerItem(portable_ender_chest, NCTabs.MISC);
		
		registerItem(dominos, NCTabs.MISC);
		
		registerItem(flour, NCTabs.MISC);
		registerItem(graham_cracker, NCTabs.MISC);
		
		registerItem(roasted_cocoa_beans, NCTabs.MISC);
		registerItem(ground_cocoa_nibs, NCTabs.MISC);
		registerItem(cocoa_butter, NCTabs.MISC);
		registerItem(cocoa_solids, NCTabs.MISC);
		registerItem(unsweetened_chocolate, NCTabs.MISC);
		registerItem(dark_chocolate, NCTabs.MISC);
		registerItem(milk_chocolate, NCTabs.MISC);
		
		registerItem(gelatin, NCTabs.MISC);
		registerItem(marshmallow, NCTabs.MISC);
		
		registerItem(smore, NCTabs.MISC);
		registerItem(moresmore, NCTabs.MISC);
		registerItem(foursmore, NCTabs.MISC);
		
		registerItem(record_wanderer, NCTabs.MISC);
		registerItem(record_end_of_the_world, NCTabs.MISC);
		registerItem(record_money_for_nothing, NCTabs.MISC);
		registerItem(record_hyperspace, NCTabs.MISC);

		registerItem(configuration_blueprint_empty, NCTabs.MISC);
		registerItem(configuration_blueprint, NCTabs.MISC);
	}
	
	public static void registerRenders() {
		for(int i = 0; i < MetaEnums.IngotType.values().length; i++) {
			registerRender(ingot, i, "ingot_" + MetaEnums.IngotType.values()[i].getName());
		}
		for(int i = 0; i < MetaEnums.IngotOxideType.values().length; i++) {
			registerRender(ingot_oxide, i, "ingot_oxide_" + MetaEnums.IngotOxideType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.DustType.values().length; i++) {
			registerRender(dust, i, "dust_" + MetaEnums.DustType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.DustOxideType.values().length; i++) {
			registerRender(dust_oxide, i, "dust_oxide_" + MetaEnums.DustOxideType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.GemType.values().length; i++) {
			registerRender(gem, i, "gem_" + MetaEnums.GemType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.GemDustType.values().length; i++) {
			registerRender(gem_dust, i, "gem_dust_" + MetaEnums.GemDustType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.AlloyType.values().length; i++) {
			registerRender(alloy, i, "alloy_" + MetaEnums.AlloyType.values()[i].getName());
		}
		
		for(int i = 0; i < 	MetaEnums.CompoundType.values().length; i++) {
			registerRender(compound, i, "compound_" + MetaEnums.CompoundType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.PartType.values().length; i++) {
			registerRender(part, i, "part_" + MetaEnums.PartType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.UpgradeType.values().length; i++) {
			registerRender(upgrade, i, "upgrade_" + MetaEnums.UpgradeType.values()[i].getName());
		}
		
		registerRender(tiny_dust_lead);
		registerRender(fuel_rod);
		registerRender(reactor_door);
		
		for(int i = 0; i < MetaEnums.ThoriumType.values().length; i++) {
			registerRender(thorium, i, "thorium" + MetaEnums.ThoriumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.UraniumType.values().length; i++) {
			registerRender(uranium, i, "uranium" + MetaEnums.UraniumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.NeptuniumType.values().length; i++) {
			registerRender(neptunium, i, "neptunium" + MetaEnums.NeptuniumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.PlutoniumType.values().length; i++) {
			registerRender(plutonium, i, "plutonium" + MetaEnums.PlutoniumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.AmericiumType.values().length; i++) {
			registerRender(americium, i, "americium" + MetaEnums.AmericiumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CuriumType.values().length; i++) {
			registerRender(curium, i, "curium" + MetaEnums.CuriumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BerkeliumType.values().length; i++) {
			registerRender(berkelium, i, "berkelium" + MetaEnums.BerkeliumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CaliforniumType.values().length; i++) {
			registerRender(californium, i, "californium" + MetaEnums.CaliforniumType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.ThoriumFuelType.values().length; i++) {
			registerRender(fuel_thorium, i, "fuel_thorium_" + MetaEnums.ThoriumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.UraniumFuelType.values().length; i++) {
			registerRender(fuel_uranium, i, "fuel_uranium_" + MetaEnums.UraniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.NeptuniumFuelType.values().length; i++) {
			registerRender(fuel_neptunium, i, "fuel_neptunium_" + MetaEnums.NeptuniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.PlutoniumFuelType.values().length; i++) {
			registerRender(fuel_plutonium, i, "fuel_plutonium_" + MetaEnums.PlutoniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.MixedOxideFuelType.values().length; i++) {
			registerRender(fuel_mixed_oxide, i, "fuel_mixed_oxide_" + MetaEnums.MixedOxideFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.AmericiumFuelType.values().length; i++) {
			registerRender(fuel_americium, i, "fuel_americium_" + MetaEnums.AmericiumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CuriumFuelType.values().length; i++) {
			registerRender(fuel_curium, i, "fuel_curium_" + MetaEnums.CuriumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BerkeliumFuelType.values().length; i++) {
			registerRender(fuel_berkelium, i, "fuel_berkelium_" + MetaEnums.BerkeliumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CaliforniumFuelType.values().length; i++) {
			registerRender(fuel_californium, i, "fuel_californium_" + MetaEnums.CaliforniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.ThoriumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_thorium, i, "depleted_fuel_thorium_" + MetaEnums.ThoriumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.UraniumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_uranium, i, "depleted_fuel_uranium_" + MetaEnums.UraniumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.NeptuniumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_neptunium, i, "depleted_fuel_neptunium_" + MetaEnums.NeptuniumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.PlutoniumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_plutonium, i, "depleted_fuel_plutonium_" + MetaEnums.PlutoniumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.MixedOxideDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_mixed_oxide, i, "depleted_fuel_mixed_oxide_" + MetaEnums.MixedOxideDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.AmericiumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_americium, i, "depleted_fuel_americium_" + MetaEnums.AmericiumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CuriumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_curium, i, "depleted_fuel_curium_" + MetaEnums.CuriumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BerkeliumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_berkelium, i, "depleted_fuel_berkelium_" + MetaEnums.BerkeliumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CaliforniumDepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_californium, i, "depleted_fuel_californium_" + MetaEnums.CaliforniumDepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.IC2DepletedFuelType.values().length; i++) {
			registerRender(depleted_fuel_ic2, i, "depleted_fuel_ic2_" + MetaEnums.IC2DepletedFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BoronType.values().length; i++) {
			registerRender(boron, i, "boron" + MetaEnums.BoronType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.LithiumType.values().length; i++) {
			registerRender(lithium, i, "lithium" + MetaEnums.LithiumType.values()[i].getName());
		}
		
		registerRender(lithium_ion_cell);
		
		registerRender(geiger_counter);
		for(int i = 0; i < MetaEnums.RadShieldingType.values().length; i++) {
			registerRender(rad_shielding, i, "rad_shielding_" + MetaEnums.RadShieldingType.values()[i].getName());
		}
		registerRender(radiation_badge);
		
		registerRender(radaway);
		registerRender(radaway_slow);
		registerRender(rad_x);
		
		registerRender(portable_ender_chest);
		
		registerRender(dominos);
		
		registerRender(flour);
		registerRender(graham_cracker);
		
		registerRender(roasted_cocoa_beans);
		registerRender(ground_cocoa_nibs);
		registerRender(cocoa_butter);
		registerRender(cocoa_solids);
		registerRender(unsweetened_chocolate);
		registerRender(dark_chocolate);
		registerRender(milk_chocolate);
		
		registerRender(gelatin);
		registerRender(marshmallow);
		
		registerRender(smore);
		registerRender(moresmore);
		registerRender(foursmore);
		
		registerRender(record_wanderer);
		registerRender(record_end_of_the_world);
		registerRender(record_money_for_nothing);
		registerRender(record_hyperspace);

		registerRender(configuration_blueprint_empty);
		registerRender(configuration_blueprint);
	}
	
	public static <T extends Item & IInfoItem> Item withName(T item, String name) {
		item.setTranslationKey(Global.MOD_ID + "." + name);
		item.setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		item.setInfo();
		return item;
	}
	
	public static String infoLine(String name) {
		return "item." + Global.MOD_ID + "." + name + ".desc";
	}
	
	public static void registerItem(Item item, CreativeTabs tab) {
		item.setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item);
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	public static void registerRender(Item item, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fileName), "inventory"));
	}
}
