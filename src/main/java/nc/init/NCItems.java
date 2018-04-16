package nc.init;

import nc.Global;
import nc.enumm.MetaEnums;
import nc.handler.SoundHandler;
import nc.item.ItemDepletedFissionFuel;
import nc.item.ItemFissionFuel;
import nc.item.ItemPortableEnderChest;
import nc.item.NCItem;
import nc.item.NCItemDoor;
import nc.item.NCItemFood;
import nc.item.NCItemMeta;
import nc.item.NCItemRecord;
import nc.item.energy.ItemBattery;
import nc.proxy.CommonProxy;
import nc.tile.energy.battery.BatteryType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
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
	public static Item fuel_rod_empty;
	public static Item tiny_dust_lead;
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
	
	public static Item fuel_rod_thorium;
	public static Item fuel_rod_uranium;
	public static Item fuel_rod_neptunium;
	public static Item fuel_rod_plutonium;
	public static Item fuel_rod_mixed_oxide;
	public static Item fuel_rod_americium;
	public static Item fuel_rod_curium;
	public static Item fuel_rod_berkelium;
	public static Item fuel_rod_californium;
	
	public static Item depleted_fuel_thorium;
	public static Item depleted_fuel_uranium;
	public static Item depleted_fuel_neptunium;
	public static Item depleted_fuel_plutonium;
	public static Item depleted_fuel_mixed_oxide;
	public static Item depleted_fuel_americium;
	public static Item depleted_fuel_curium;
	public static Item depleted_fuel_berkelium;
	public static Item depleted_fuel_californium;
	
	public static Item depleted_fuel_rod_thorium;
	public static Item depleted_fuel_rod_uranium;
	public static Item depleted_fuel_rod_neptunium;
	public static Item depleted_fuel_rod_plutonium;
	public static Item depleted_fuel_rod_mixed_oxide;
	public static Item depleted_fuel_rod_americium;
	public static Item depleted_fuel_rod_curium;
	public static Item depleted_fuel_rod_berkelium;
	public static Item depleted_fuel_rod_californium;
	
	public static Item boron;
	public static Item lithium;
	
	public static Item lithium_ion_cell;
	
	public static Item portable_ender_chest;
	
	public static Item dominos;
	
	public static Item record_wanderer;
	public static Item record_end_of_the_world;
	public static Item record_money_for_nothing;
	
	public static void init() {
		ingot = new NCItemMeta("ingot", MetaEnums.IngotType.class);
		ingot_oxide = new NCItemMeta("ingot_oxide", MetaEnums.IngotOxideType.class);
		dust = new NCItemMeta("dust", MetaEnums.DustType.class);
		dust_oxide = new NCItemMeta("dust_oxide", MetaEnums.DustOxideType.class);
		gem = new NCItemMeta("gem", MetaEnums.GemType.class);
		gem_dust = new NCItemMeta("gem_dust", MetaEnums.GemDustType.class);
		alloy = new NCItemMeta("alloy", MetaEnums.AlloyType.class);
		compound = new NCItemMeta("compound", MetaEnums.CompoundType.class);
		
		part = new NCItemMeta("part", MetaEnums.PartType.class);
		upgrade = new NCItemMeta("upgrade", MetaEnums.UpgradeType.class);
		fuel_rod_empty = new NCItem("fuel_rod_empty", TextFormatting.GREEN);
		tiny_dust_lead = new NCItem("tiny_dust_lead");
		reactor_door = new NCItemDoor("reactor_door_item", NCBlocks.reactor_door);
		
		thorium = new NCItemMeta("thorium", MetaEnums.ThoriumType.class);
		uranium = new NCItemMeta("uranium", MetaEnums.UraniumType.class);
		neptunium = new NCItemMeta("neptunium", MetaEnums.NeptuniumType.class);
		plutonium = new NCItemMeta("plutonium", MetaEnums.PlutoniumType.class);
		americium = new NCItemMeta("americium", MetaEnums.AmericiumType.class);
		curium = new NCItemMeta("curium", MetaEnums.CuriumType.class);
		berkelium = new NCItemMeta("berkelium", MetaEnums.BerkeliumType.class);
		californium = new NCItemMeta("californium", MetaEnums.CaliforniumType.class);
		
		fuel_thorium = new ItemFissionFuel("fuel_thorium", MetaEnums.ThoriumFuelType.class);
		fuel_uranium = new ItemFissionFuel("fuel_uranium", MetaEnums.UraniumFuelType.class);
		fuel_neptunium = new ItemFissionFuel("fuel_neptunium", MetaEnums.NeptuniumFuelType.class);
		fuel_plutonium = new ItemFissionFuel("fuel_plutonium", MetaEnums.PlutoniumFuelType.class);
		fuel_mixed_oxide = new ItemFissionFuel("fuel_mixed_oxide", MetaEnums.MixedOxideFuelType.class);
		fuel_americium = new ItemFissionFuel("fuel_americium", MetaEnums.AmericiumFuelType.class);
		fuel_curium = new ItemFissionFuel("fuel_curium", MetaEnums.CuriumFuelType.class);
		fuel_berkelium = new ItemFissionFuel("fuel_berkelium", MetaEnums.BerkeliumFuelType.class);
		fuel_californium = new ItemFissionFuel("fuel_californium", MetaEnums.CaliforniumFuelType.class);
		
		fuel_rod_thorium = new ItemFissionFuel("fuel_rod_thorium", MetaEnums.ThoriumFuelRodType.class);
		fuel_rod_uranium = new ItemFissionFuel("fuel_rod_uranium", MetaEnums.UraniumFuelRodType.class);
		fuel_rod_neptunium = new ItemFissionFuel("fuel_rod_neptunium", MetaEnums.NeptuniumFuelRodType.class);
		fuel_rod_plutonium = new ItemFissionFuel("fuel_rod_plutonium", MetaEnums.PlutoniumFuelRodType.class);
		fuel_rod_mixed_oxide = new ItemFissionFuel("fuel_rod_mixed_oxide", MetaEnums.MixedOxideFuelRodType.class);
		fuel_rod_americium = new ItemFissionFuel("fuel_rod_americium", MetaEnums.AmericiumFuelRodType.class);
		fuel_rod_curium = new ItemFissionFuel("fuel_rod_curium", MetaEnums.CuriumFuelRodType.class);
		fuel_rod_berkelium = new ItemFissionFuel("fuel_rod_berkelium", MetaEnums.BerkeliumFuelRodType.class);
		fuel_rod_californium = new ItemFissionFuel("fuel_rod_californium", MetaEnums.CaliforniumFuelRodType.class);
		
		depleted_fuel_thorium = new ItemDepletedFissionFuel("depleted_fuel_thorium", MetaEnums.ThoriumDepletedFuelType.class);
		depleted_fuel_uranium = new ItemDepletedFissionFuel("depleted_fuel_uranium", MetaEnums.UraniumDepletedFuelType.class);
		depleted_fuel_neptunium = new ItemDepletedFissionFuel("depleted_fuel_neptunium", MetaEnums.NeptuniumDepletedFuelType.class);
		depleted_fuel_plutonium = new ItemDepletedFissionFuel("depleted_fuel_plutonium", MetaEnums.PlutoniumDepletedFuelType.class);
		depleted_fuel_mixed_oxide = new ItemDepletedFissionFuel("depleted_fuel_mixed_oxide", MetaEnums.MixedOxideDepletedFuelType.class);
		depleted_fuel_americium = new ItemDepletedFissionFuel("depleted_fuel_americium", MetaEnums.AmericiumDepletedFuelType.class);
		depleted_fuel_curium = new ItemDepletedFissionFuel("depleted_fuel_curium", MetaEnums.CuriumDepletedFuelType.class);
		depleted_fuel_berkelium = new ItemDepletedFissionFuel("depleted_fuel_berkelium", MetaEnums.BerkeliumDepletedFuelType.class);
		depleted_fuel_californium = new ItemDepletedFissionFuel("depleted_fuel_californium", MetaEnums.CaliforniumDepletedFuelType.class);
		
		depleted_fuel_rod_thorium = new ItemDepletedFissionFuel("depleted_fuel_rod_thorium", MetaEnums.ThoriumDepletedFuelRodType.class);
		depleted_fuel_rod_uranium = new ItemDepletedFissionFuel("depleted_fuel_rod_uranium", MetaEnums.UraniumDepletedFuelRodType.class);
		depleted_fuel_rod_neptunium = new ItemDepletedFissionFuel("depleted_fuel_rod_neptunium", MetaEnums.NeptuniumDepletedFuelRodType.class);
		depleted_fuel_rod_plutonium = new ItemDepletedFissionFuel("depleted_fuel_rod_plutonium", MetaEnums.PlutoniumDepletedFuelRodType.class);
		depleted_fuel_rod_mixed_oxide = new ItemDepletedFissionFuel("depleted_fuel_rod_mixed_oxide", MetaEnums.MixedOxideDepletedFuelRodType.class);
		depleted_fuel_rod_americium = new ItemDepletedFissionFuel("depleted_fuel_rod_americium", MetaEnums.AmericiumDepletedFuelRodType.class);
		depleted_fuel_rod_curium = new ItemDepletedFissionFuel("depleted_fuel_rod_curium", MetaEnums.CuriumDepletedFuelRodType.class);
		depleted_fuel_rod_berkelium = new ItemDepletedFissionFuel("depleted_fuel_rod_berkelium", MetaEnums.BerkeliumDepletedFuelRodType.class);
		depleted_fuel_rod_californium = new ItemDepletedFissionFuel("depleted_fuel_rod_californium", MetaEnums.CaliforniumDepletedFuelRodType.class);
		
		boron = new NCItemMeta("boron", MetaEnums.BoronType.class);
		lithium = new NCItemMeta("lithium", MetaEnums.LithiumType.class);
		
		lithium_ion_cell = new ItemBattery("lithium_ion_cell", BatteryType.LITHIUM_ION_BATTERY_BASIC);
		
		portable_ender_chest = new ItemPortableEnderChest("portable_ender_chest");
		
		dominos = new NCItemFood("dominos", 16, 1.0F, false, new PotionEffect[] {new PotionEffect(Potion.getPotionById(1), 500, 2), new PotionEffect(Potion.getPotionById(3), 500, 2)});
		
		record_wanderer = new NCItemRecord("wanderer", SoundHandler.wanderer);
		record_end_of_the_world = new NCItemRecord("end_of_the_world", SoundHandler.end_of_the_world);
		record_money_for_nothing = new NCItemRecord("money_for_nothing", SoundHandler.money_for_nothing);
	}
	
	public static void register() {
		registerItem(ingot, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(ingot_oxide, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(dust, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(dust_oxide, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(gem, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(gem_dust, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(alloy, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(compound, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		
		registerItem(part, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(upgrade, CommonProxy.TAB_MACHINES);
		registerItem(fuel_rod_empty, null);
		registerItem(tiny_dust_lead, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(reactor_door, CommonProxy.TAB_FISSION_BLOCKS);
		
		registerItem(thorium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(uranium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(neptunium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(plutonium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(americium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(curium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(berkelium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(californium, CommonProxy.TAB_FISSION_MATERIALS);
		
		registerItem(fuel_thorium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_uranium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_neptunium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_plutonium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_mixed_oxide, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_americium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_curium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_berkelium, CommonProxy.TAB_FISSION_FUELS);
		registerItem(fuel_californium, CommonProxy.TAB_FISSION_FUELS);
		
		registerItem(fuel_rod_thorium, null);
		registerItem(fuel_rod_uranium, null);
		registerItem(fuel_rod_neptunium, null);
		registerItem(fuel_rod_plutonium, null);
		registerItem(fuel_rod_mixed_oxide, null);
		registerItem(fuel_rod_americium, null);
		registerItem(fuel_rod_curium, null);
		registerItem(fuel_rod_berkelium, null);
		registerItem(fuel_rod_californium, null);
		
		registerItem(depleted_fuel_thorium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_uranium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_neptunium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_plutonium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_mixed_oxide, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_americium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_curium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_berkelium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(depleted_fuel_californium, CommonProxy.TAB_FISSION_MATERIALS);
		
		registerItem(depleted_fuel_rod_thorium, null);
		registerItem(depleted_fuel_rod_uranium, null);
		registerItem(depleted_fuel_rod_neptunium, null);
		registerItem(depleted_fuel_rod_plutonium, null);
		registerItem(depleted_fuel_rod_mixed_oxide, null);
		registerItem(depleted_fuel_rod_americium, null);
		registerItem(depleted_fuel_rod_curium, null);
		registerItem(depleted_fuel_rod_berkelium, null);
		registerItem(depleted_fuel_rod_californium, null);
		
		registerItem(boron, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(lithium, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		
		registerItem(lithium_ion_cell, CommonProxy.TAB_MACHINES);
		
		registerItem(portable_ender_chest, CommonProxy.TAB_MISC);
		
		registerItem(dominos, CommonProxy.TAB_MISC);
		
		registerItem(record_wanderer, CommonProxy.TAB_MISC);
		registerItem(record_end_of_the_world, CommonProxy.TAB_MISC);
		registerItem(record_money_for_nothing, CommonProxy.TAB_MISC);
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
		
		registerRender(fuel_rod_empty);
		registerRender(tiny_dust_lead);
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
		
		for(int i = 0; i < MetaEnums.ThoriumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_thorium, i, "fuel_rod_thorium_" + MetaEnums.ThoriumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.UraniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_uranium, i, "fuel_rod_uranium_" + MetaEnums.UraniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.NeptuniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_neptunium, i, "fuel_rod_neptunium_" + MetaEnums.NeptuniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.PlutoniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_plutonium, i, "fuel_rod_plutonium_" + MetaEnums.PlutoniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.MixedOxideFuelRodType.values().length; i++) {
			registerRender(fuel_rod_mixed_oxide, i, "fuel_rod_mixed_oxide_" + MetaEnums.MixedOxideFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.AmericiumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_americium, i, "fuel_rod_americium_" + MetaEnums.AmericiumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CuriumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_curium, i, "fuel_rod_curium_" + MetaEnums.CuriumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BerkeliumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_berkelium, i, "fuel_rod_berkelium_" + MetaEnums.BerkeliumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CaliforniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_californium, i, "fuel_rod_californium_" + MetaEnums.CaliforniumFuelRodType.values()[i].getName());
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
		
		for(int i = 0; i < MetaEnums.ThoriumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_thorium, i, "depleted_fuel_rod_thorium_" + MetaEnums.ThoriumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.UraniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_uranium, i, "depleted_fuel_rod_uranium_" + MetaEnums.UraniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.NeptuniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_neptunium, i, "depleted_fuel_rod_neptunium_" + MetaEnums.NeptuniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.PlutoniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_plutonium, i, "depleted_fuel_rod_plutonium_" + MetaEnums.PlutoniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.MixedOxideDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_mixed_oxide, i, "depleted_fuel_rod_mixed_oxide_" + MetaEnums.MixedOxideDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.AmericiumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_americium, i, "depleted_fuel_rod_americium_" + MetaEnums.AmericiumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CuriumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_curium, i, "depleted_fuel_rod_curium_" + MetaEnums.CuriumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BerkeliumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_berkelium, i, "depleted_fuel_rod_berkelium_" + MetaEnums.BerkeliumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.CaliforniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_californium, i, "depleted_fuel_rod_californium_" + MetaEnums.CaliforniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.BoronType.values().length; i++) {
			registerRender(boron, i, "boron" + MetaEnums.BoronType.values()[i].getName());
		}
		
		for(int i = 0; i < MetaEnums.LithiumType.values().length; i++) {
			registerRender(lithium, i, "lithium" + MetaEnums.LithiumType.values()[i].getName());
		}
		
		registerRender(lithium_ion_cell);
		
		registerRender(portable_ender_chest);
		
		registerRender(dominos);
		
		registerRender(record_wanderer);
		registerRender(record_end_of_the_world);
		registerRender(record_money_for_nothing);
	}
	
	private static String infoLine(String name) {
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
