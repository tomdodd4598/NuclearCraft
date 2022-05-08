package nc.init;

import static nc.config.NCConfig.*;

import nc.*;
import nc.enumm.MetaEnums;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCItems {
	
	public static Item ingot;
	public static Item dust;
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
	
	public static void init() {
		ingot = withName(new NCItemMeta<>(MetaEnums.IngotType.class), "ingot");
		dust = withName(new NCItemMeta<>(MetaEnums.DustType.class/*, NCInfo.dustOreDropInfo()*/), "dust");
		gem = withName(new NCItemMeta<>(MetaEnums.GemType.class/*, NCInfo.gemOreDropInfo()*/), "gem");
		gem_dust = withName(new NCItemMeta<>(MetaEnums.GemDustType.class/*, NCInfo.gemDustOreDropInfo()*/), "gem_dust");
		alloy = withName(new NCItemMeta<>(MetaEnums.AlloyType.class), "alloy");
		compound = withName(new NCItemMeta<>(MetaEnums.CompoundType.class), "compound");
		
		part = withName(new NCItemMeta<>(MetaEnums.PartType.class), "part");
		upgrade = withName(new ItemUpgrade(MetaEnums.UpgradeType.class, NCInfo.upgradeInfo()), "upgrade");
		
		fission_dust = withName(new NCItemMeta<>(MetaEnums.FissionDustType.class), "fission_dust");
		
		uranium = withName(new NCItemMeta<>(MetaEnums.UraniumType.class), "uranium");
		neptunium = withName(new NCItemMeta<>(MetaEnums.NeptuniumType.class), "neptunium");
		plutonium = withName(new NCItemMeta<>(MetaEnums.PlutoniumType.class), "plutonium");
		americium = withName(new NCItemMeta<>(MetaEnums.AmericiumType.class), "americium");
		curium = withName(new NCItemMeta<>(MetaEnums.CuriumType.class), "curium");
		berkelium = withName(new NCItemMeta<>(MetaEnums.BerkeliumType.class), "berkelium");
		californium = withName(new NCItemMeta<>(MetaEnums.CaliforniumType.class), "californium");
		
		pellet_thorium = withName(new NCItemMeta<>(MetaEnums.ThoriumPelletType.class), "pellet_thorium");
		pellet_uranium = withName(new NCItemMeta<>(MetaEnums.UraniumPelletType.class), "pellet_uranium");
		pellet_neptunium = withName(new NCItemMeta<>(MetaEnums.NeptuniumPelletType.class), "pellet_neptunium");
		pellet_plutonium = withName(new NCItemMeta<>(MetaEnums.PlutoniumPelletType.class), "pellet_plutonium");
		pellet_mixed = withName(new NCItemMeta<>(MetaEnums.MixedPelletType.class), "pellet_mixed");
		pellet_americium = withName(new NCItemMeta<>(MetaEnums.AmericiumPelletType.class), "pellet_americium");
		pellet_curium = withName(new NCItemMeta<>(MetaEnums.CuriumPelletType.class), "pellet_curium");
		pellet_berkelium = withName(new NCItemMeta<>(MetaEnums.BerkeliumPelletType.class), "pellet_berkelium");
		pellet_californium = withName(new NCItemMeta<>(MetaEnums.CaliforniumPelletType.class), "pellet_californium");
		
		fuel_thorium = withName(new ItemFissionFuel<>(MetaEnums.ThoriumFuelType.class), "fuel_thorium");
		fuel_uranium = withName(new ItemFissionFuel<>(MetaEnums.UraniumFuelType.class), "fuel_uranium");
		fuel_neptunium = withName(new ItemFissionFuel<>(MetaEnums.NeptuniumFuelType.class), "fuel_neptunium");
		fuel_plutonium = withName(new ItemFissionFuel<>(MetaEnums.PlutoniumFuelType.class), "fuel_plutonium");
		fuel_mixed = withName(new ItemFissionFuel<>(MetaEnums.MixedFuelType.class), "fuel_mixed");
		fuel_americium = withName(new ItemFissionFuel<>(MetaEnums.AmericiumFuelType.class), "fuel_americium");
		fuel_curium = withName(new ItemFissionFuel<>(MetaEnums.CuriumFuelType.class), "fuel_curium");
		fuel_berkelium = withName(new ItemFissionFuel<>(MetaEnums.BerkeliumFuelType.class), "fuel_berkelium");
		fuel_californium = withName(new ItemFissionFuel<>(MetaEnums.CaliforniumFuelType.class), "fuel_californium");
		
		depleted_fuel_thorium = withName(new NCItemMeta<>(MetaEnums.ThoriumDepletedFuelType.class), "depleted_fuel_thorium");
		depleted_fuel_uranium = withName(new NCItemMeta<>(MetaEnums.UraniumDepletedFuelType.class), "depleted_fuel_uranium");
		depleted_fuel_neptunium = withName(new NCItemMeta<>(MetaEnums.NeptuniumDepletedFuelType.class), "depleted_fuel_neptunium");
		depleted_fuel_plutonium = withName(new NCItemMeta<>(MetaEnums.PlutoniumDepletedFuelType.class), "depleted_fuel_plutonium");
		depleted_fuel_mixed = withName(new NCItemMeta<>(MetaEnums.MixedDepletedFuelType.class), "depleted_fuel_mixed");
		depleted_fuel_americium = withName(new NCItemMeta<>(MetaEnums.AmericiumDepletedFuelType.class), "depleted_fuel_americium");
		depleted_fuel_curium = withName(new NCItemMeta<>(MetaEnums.CuriumDepletedFuelType.class), "depleted_fuel_curium");
		depleted_fuel_berkelium = withName(new NCItemMeta<>(MetaEnums.BerkeliumDepletedFuelType.class), "depleted_fuel_berkelium");
		depleted_fuel_californium = withName(new NCItemMeta<>(MetaEnums.CaliforniumDepletedFuelType.class), "depleted_fuel_californium");
		
		depleted_fuel_ic2 = withName(new NCItemMeta<>(MetaEnums.IC2DepletedFuelType.class), "depleted_fuel_ic2");
		
		boron = withName(new NCItemMeta<>(MetaEnums.BoronType.class), "boron");
		lithium = withName(new NCItemMeta<>(MetaEnums.LithiumType.class), "lithium");
		
		lithium_ion_cell = withName(new ItemBattery(BatteryItemType.LITHIUM_ION_CELL), "lithium_ion_cell");
		
		multitool = withName(new ItemMultitool(), "multitool");
		
		geiger_counter = withName(new ItemGeigerCounter(), "geiger_counter");
		rad_shielding = withName(new ItemRadShielding(NCInfo.radShieldingInfo()), "rad_shielding");
		radiation_badge = withName(new ItemRadiationBadge(InfoHelper.formattedInfo(infoLine("radiation_badge"), UnitHelper.prefix(radiation_badge_durability * radiation_badge_info_rate, 3, "Rad"), UnitHelper.prefix(radiation_badge_durability, 3, "Rad"))), "radiation_badge");
		
		radaway = withName(new ItemRadaway(false, InfoHelper.formattedInfo(infoLine("radaway"), RadiationHelper.radsPrefix(radiation_radaway_amount, false), NCMath.pcDecimalPlaces(radiation_radaway_amount / max_player_rads, 1), RadiationHelper.radsPrefix(radiation_radaway_rate, true))), "radaway");
		radaway_slow = withName(new ItemRadaway(true, InfoHelper.formattedInfo(infoLine("radaway"), RadiationHelper.radsPrefix(radiation_radaway_slow_amount, false), NCMath.pcDecimalPlaces(radiation_radaway_slow_amount / max_player_rads, 1), RadiationHelper.radsPrefix(radiation_radaway_slow_rate, true))), "radaway_slow");
		rad_x = withName(new ItemRadX(InfoHelper.formattedInfo(infoLine("rad_x"), RadiationHelper.resistanceSigFigs(radiation_rad_x_amount), UnitHelper.applyTimeUnit(radiation_rad_x_lifetime, 2))), "rad_x");
		
		portable_ender_chest = withName(new ItemPortableEnderChest(), "portable_ender_chest");
		
		dominos = withName(new NCItemFood(16, 1.8F, true, new PotionEffect[] {PotionHelper.newEffect(1, 2, 600), PotionHelper.newEffect(3, 2, 600)}), "dominos");
		
		flour = withName(new NCItem(), "flour");
		graham_cracker = withName(new NCItemFood(1, 0.2F, false, new PotionEffect[] {}), "graham_cracker");
		
		roasted_cocoa_beans = withName(new NCItem(), "roasted_cocoa_beans");
		ground_cocoa_nibs = withName(new NCItemFood(1, 0.2F, false, new PotionEffect[] {}), "ground_cocoa_nibs");
		cocoa_butter = withName(new NCItemFood(2, 0.2F, false, new PotionEffect[] {PotionHelper.newEffect(22, 1, 300)}), "cocoa_butter");
		cocoa_solids = withName(new NCItem(), "cocoa_solids");
		unsweetened_chocolate = withName(new NCItemFood(2, 0.2F, false, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300)}), "unsweetened_chocolate");
		dark_chocolate = withName(new NCItemFood(3, 0.4F, false, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300), PotionHelper.newEffect(1, 1, 300)}), "dark_chocolate");
		milk_chocolate = withName(new NCItemFood(4, 0.6F, false, new PotionEffect[] {PotionHelper.newEffect(3, 1, 300), PotionHelper.newEffect(1, 1, 300), PotionHelper.newEffect(22, 1, 300)}), "milk_chocolate");
		
		gelatin = withName(new NCItem(), "gelatin");
		marshmallow = withName(new NCItemFood(1, 0.4F, false, new PotionEffect[] {PotionHelper.newEffect(1, 1, 300)}), "marshmallow");
		
		smore = withName(new NCItemFood(8, 1.4F, false, new PotionEffect[] {PotionHelper.newEffect(3, 2, 300), PotionHelper.newEffect(1, 2, 300), PotionHelper.newEffect(22, 2, 300)}), "smore");
		moresmore = withName(new NCItemFood(20, 3.8F, false, new PotionEffect[] {PotionHelper.newEffect(3, 2, 600), PotionHelper.newEffect(1, 2, 600), PotionHelper.newEffect(22, 2, 600)}), "moresmore");
		foursmore = withName(new NCItemFood(44, 8.6F, false, new PotionEffect[] {PotionHelper.newEffect(3, 2, 1200), PotionHelper.newEffect(1, 2, 1200), PotionHelper.newEffect(22, 2, 1200)}), "foursmore");
		
		record_wanderer = withName(new NCItemRecord("record_wanderer", NCSounds.wanderer), "record_wanderer");
		record_end_of_the_world = withName(new NCItemRecord("record_end_of_the_world", NCSounds.end_of_the_world), "record_end_of_the_world");
		record_money_for_nothing = withName(new NCItemRecord("record_money_for_nothing", NCSounds.money_for_nothing), "record_money_for_nothing");
		record_hyperspace = withName(new NCItemRecord("record_hyperspace", NCSounds.hyperspace), "record_hyperspace");
	}
	
	public static void register() {
		registerItem(ingot, NCTabs.material());
		registerItem(dust, NCTabs.material());
		registerItem(gem, NCTabs.material());
		registerItem(gem_dust, NCTabs.material());
		registerItem(alloy, NCTabs.material());
		registerItem(compound, NCTabs.material());
		
		registerItem(part, NCTabs.material());
		registerItem(upgrade, NCTabs.machine());
		
		registerItem(fission_dust, NCTabs.material());
		
		registerItem(uranium, NCTabs.material());
		registerItem(neptunium, NCTabs.material());
		registerItem(plutonium, NCTabs.material());
		registerItem(americium, NCTabs.material());
		registerItem(curium, NCTabs.material());
		registerItem(berkelium, NCTabs.material());
		registerItem(californium, NCTabs.material());
		
		registerItem(pellet_thorium, NCTabs.material());
		registerItem(pellet_uranium, NCTabs.material());
		registerItem(pellet_neptunium, NCTabs.material());
		registerItem(pellet_plutonium, NCTabs.material());
		registerItem(pellet_mixed, NCTabs.material());
		registerItem(pellet_americium, NCTabs.material());
		registerItem(pellet_curium, NCTabs.material());
		registerItem(pellet_berkelium, NCTabs.material());
		registerItem(pellet_californium, NCTabs.material());
		
		registerItem(fuel_thorium, NCTabs.material());
		registerItem(fuel_uranium, NCTabs.material());
		registerItem(fuel_neptunium, NCTabs.material());
		registerItem(fuel_plutonium, NCTabs.material());
		registerItem(fuel_mixed, NCTabs.material());
		registerItem(fuel_americium, NCTabs.material());
		registerItem(fuel_curium, NCTabs.material());
		registerItem(fuel_berkelium, NCTabs.material());
		registerItem(fuel_californium, NCTabs.material());
		
		registerItem(depleted_fuel_thorium, NCTabs.material());
		registerItem(depleted_fuel_uranium, NCTabs.material());
		registerItem(depleted_fuel_neptunium, NCTabs.material());
		registerItem(depleted_fuel_plutonium, NCTabs.material());
		registerItem(depleted_fuel_mixed, NCTabs.material());
		registerItem(depleted_fuel_americium, NCTabs.material());
		registerItem(depleted_fuel_curium, NCTabs.material());
		registerItem(depleted_fuel_berkelium, NCTabs.material());
		registerItem(depleted_fuel_californium, NCTabs.material());
		
		registerItem(depleted_fuel_ic2, NCTabs.material());
		
		registerItem(boron, NCTabs.material());
		registerItem(lithium, NCTabs.material());
		
		registerItem(lithium_ion_cell, NCTabs.machine());
		
		registerItem(multitool, NCTabs.machine());
		
		registerItem(geiger_counter, NCTabs.radiation());
		registerItem(rad_shielding, NCTabs.radiation());
		registerItem(radiation_badge, NCTabs.radiation());
		
		registerItem(radaway, NCTabs.radiation());
		registerItem(radaway_slow, NCTabs.radiation());
		registerItem(rad_x, NCTabs.radiation());
		
		registerItem(portable_ender_chest, NCTabs.misc());
		
		registerItem(dominos, NCTabs.misc());
		
		registerItem(flour, NCTabs.misc());
		registerItem(graham_cracker, NCTabs.misc());
		
		registerItem(roasted_cocoa_beans, NCTabs.misc());
		registerItem(ground_cocoa_nibs, NCTabs.misc());
		registerItem(cocoa_butter, NCTabs.misc());
		registerItem(cocoa_solids, NCTabs.misc());
		registerItem(unsweetened_chocolate, NCTabs.misc());
		registerItem(dark_chocolate, NCTabs.misc());
		registerItem(milk_chocolate, NCTabs.misc());
		
		registerItem(gelatin, NCTabs.misc());
		registerItem(marshmallow, NCTabs.misc());
		
		registerItem(smore, NCTabs.misc());
		registerItem(moresmore, NCTabs.misc());
		registerItem(foursmore, null);
		
		registerItem(record_wanderer, NCTabs.misc());
		registerItem(record_end_of_the_world, NCTabs.misc());
		registerItem(record_money_for_nothing, NCTabs.misc());
		registerItem(record_hyperspace, NCTabs.misc());
	}
	
	public static void registerRenders() {
		for (int i = 0; i < MetaEnums.IngotType.values().length; ++i) {
			registerRender(ingot, i, MetaEnums.IngotType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.DustType.values().length; ++i) {
			registerRender(dust, i, MetaEnums.DustType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.GemType.values().length; ++i) {
			registerRender(gem, i, MetaEnums.GemType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.GemDustType.values().length; ++i) {
			registerRender(gem_dust, i, MetaEnums.GemDustType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.AlloyType.values().length; ++i) {
			registerRender(alloy, i, MetaEnums.AlloyType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CompoundType.values().length; ++i) {
			registerRender(compound, i, MetaEnums.CompoundType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.PartType.values().length; ++i) {
			registerRender(part, i, MetaEnums.PartType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.UpgradeType.values().length; ++i) {
			registerRender(upgrade, i, MetaEnums.UpgradeType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.FissionDustType.values().length; ++i) {
			registerRender(fission_dust, i, MetaEnums.FissionDustType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.UraniumType.values().length; ++i) {
			registerRender(uranium, i, MetaEnums.UraniumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.NeptuniumType.values().length; ++i) {
			registerRender(neptunium, i, MetaEnums.NeptuniumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.PlutoniumType.values().length; ++i) {
			registerRender(plutonium, i, MetaEnums.PlutoniumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.AmericiumType.values().length; ++i) {
			registerRender(americium, i, MetaEnums.AmericiumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CuriumType.values().length; ++i) {
			registerRender(curium, i, MetaEnums.CuriumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.BerkeliumType.values().length; ++i) {
			registerRender(berkelium, i, MetaEnums.BerkeliumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CaliforniumType.values().length; ++i) {
			registerRender(californium, i, MetaEnums.CaliforniumType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.ThoriumPelletType.values().length; ++i) {
			registerRender(pellet_thorium, i, MetaEnums.ThoriumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.UraniumPelletType.values().length; ++i) {
			registerRender(pellet_uranium, i, MetaEnums.UraniumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.NeptuniumPelletType.values().length; ++i) {
			registerRender(pellet_neptunium, i, MetaEnums.NeptuniumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.PlutoniumPelletType.values().length; ++i) {
			registerRender(pellet_plutonium, i, MetaEnums.PlutoniumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.MixedPelletType.values().length; ++i) {
			registerRender(pellet_mixed, i, MetaEnums.MixedPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.AmericiumPelletType.values().length; ++i) {
			registerRender(pellet_americium, i, MetaEnums.AmericiumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CuriumPelletType.values().length; ++i) {
			registerRender(pellet_curium, i, MetaEnums.CuriumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.BerkeliumPelletType.values().length; ++i) {
			registerRender(pellet_berkelium, i, MetaEnums.BerkeliumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CaliforniumPelletType.values().length; ++i) {
			registerRender(pellet_californium, i, MetaEnums.CaliforniumPelletType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.ThoriumFuelType.values().length; ++i) {
			registerRender(fuel_thorium, i, MetaEnums.ThoriumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.UraniumFuelType.values().length; ++i) {
			registerRender(fuel_uranium, i, MetaEnums.UraniumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.NeptuniumFuelType.values().length; ++i) {
			registerRender(fuel_neptunium, i, MetaEnums.NeptuniumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.PlutoniumFuelType.values().length; ++i) {
			registerRender(fuel_plutonium, i, MetaEnums.PlutoniumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.MixedFuelType.values().length; ++i) {
			registerRender(fuel_mixed, i, MetaEnums.MixedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.AmericiumFuelType.values().length; ++i) {
			registerRender(fuel_americium, i, MetaEnums.AmericiumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CuriumFuelType.values().length; ++i) {
			registerRender(fuel_curium, i, MetaEnums.CuriumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.BerkeliumFuelType.values().length; ++i) {
			registerRender(fuel_berkelium, i, MetaEnums.BerkeliumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CaliforniumFuelType.values().length; ++i) {
			registerRender(fuel_californium, i, MetaEnums.CaliforniumFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.ThoriumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_thorium, i, MetaEnums.ThoriumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.UraniumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_uranium, i, MetaEnums.UraniumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.NeptuniumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_neptunium, i, MetaEnums.NeptuniumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.PlutoniumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_plutonium, i, MetaEnums.PlutoniumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.MixedDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_mixed, i, MetaEnums.MixedDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.AmericiumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_americium, i, MetaEnums.AmericiumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CuriumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_curium, i, MetaEnums.CuriumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.BerkeliumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_berkelium, i, MetaEnums.BerkeliumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.CaliforniumDepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_californium, i, MetaEnums.CaliforniumDepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.IC2DepletedFuelType.values().length; ++i) {
			registerRender(depleted_fuel_ic2, i, MetaEnums.IC2DepletedFuelType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.BoronType.values().length; ++i) {
			registerRender(boron, i, MetaEnums.BoronType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.LithiumType.values().length; ++i) {
			registerRender(lithium, i, MetaEnums.LithiumType.values()[i].getName());
		}
		
		registerRender(lithium_ion_cell);
		
		registerRender(multitool);
		
		registerRender(geiger_counter);
		for (int i = 0; i < MetaEnums.RadShieldingType.values().length; ++i) {
			registerRender(rad_shielding, i, MetaEnums.RadShieldingType.values()[i].getName());
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
	}
	
	public static <T extends Item & IInfoItem> Item withName(T item, String name) {
		item.setTranslationKey(Global.MOD_ID + "." + name).setRegistryName(new ResourceLocation(Global.MOD_ID, name));
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
	
	public static void registerRender(Item item, int meta, String type) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, "items/" + item.getRegistryName().getPath()), "type=" + type));
	}
}
