package nc.init;

import nc.Global;
import nc.enumm.MetaEnums;
import nc.enumm.MetaEnums.AlloyType;
import nc.enumm.MetaEnums.AmericiumDepletedFuelRodType;
import nc.enumm.MetaEnums.AmericiumFuelRodType;
import nc.enumm.MetaEnums.AmericiumFuelType;
import nc.enumm.MetaEnums.AmericiumType;
import nc.enumm.MetaEnums.BerkeliumDepletedFuelRodType;
import nc.enumm.MetaEnums.BerkeliumFuelRodType;
import nc.enumm.MetaEnums.BerkeliumFuelType;
import nc.enumm.MetaEnums.BerkeliumType;
import nc.enumm.MetaEnums.BoronType;
import nc.enumm.MetaEnums.CaliforniumDepletedFuelRodType;
import nc.enumm.MetaEnums.CaliforniumFuelRodType;
import nc.enumm.MetaEnums.CaliforniumFuelType;
import nc.enumm.MetaEnums.CaliforniumType;
import nc.enumm.MetaEnums.CompoundType;
import nc.enumm.MetaEnums.CuriumDepletedFuelRodType;
import nc.enumm.MetaEnums.CuriumFuelRodType;
import nc.enumm.MetaEnums.CuriumFuelType;
import nc.enumm.MetaEnums.CuriumType;
import nc.enumm.MetaEnums.DustOxideType;
import nc.enumm.MetaEnums.DustType;
import nc.enumm.MetaEnums.GemDustType;
import nc.enumm.MetaEnums.GemType;
import nc.enumm.MetaEnums.IngotOxideType;
import nc.enumm.MetaEnums.IngotType;
import nc.enumm.MetaEnums.LithiumType;
import nc.enumm.MetaEnums.MixedOxideDepletedFuelRodType;
import nc.enumm.MetaEnums.MixedOxideFuelRodType;
import nc.enumm.MetaEnums.MixedOxideFuelType;
import nc.enumm.MetaEnums.NeptuniumDepletedFuelRodType;
import nc.enumm.MetaEnums.NeptuniumFuelRodType;
import nc.enumm.MetaEnums.NeptuniumFuelType;
import nc.enumm.MetaEnums.NeptuniumType;
import nc.enumm.MetaEnums.PartType;
import nc.enumm.MetaEnums.PlutoniumDepletedFuelRodType;
import nc.enumm.MetaEnums.PlutoniumFuelRodType;
import nc.enumm.MetaEnums.PlutoniumFuelType;
import nc.enumm.MetaEnums.PlutoniumType;
import nc.enumm.MetaEnums.ThoriumDepletedFuelRodType;
import nc.enumm.MetaEnums.ThoriumFuelRodType;
import nc.enumm.MetaEnums.ThoriumFuelType;
import nc.enumm.MetaEnums.ThoriumType;
import nc.enumm.MetaEnums.UpgradeType;
import nc.enumm.MetaEnums.UraniumDepletedFuelRodType;
import nc.enumm.MetaEnums.UraniumFuelRodType;
import nc.enumm.MetaEnums.UraniumFuelType;
import nc.enumm.MetaEnums.UraniumType;
import nc.handler.SoundHandler;
import nc.item.ItemFissionFuel;
import nc.item.ItemPortableEnderChest;
import nc.item.NCItem;
import nc.item.NCItemDoor;
import nc.item.NCItemFood;
import nc.item.NCItemMeta;
import nc.item.NCItemRecord;
import nc.proxy.CommonProxy;
import nc.util.NCUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
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
		fuel_rod_empty = new NCItem("fuel_rod_empty");
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
		
		depleted_fuel_rod_thorium = new NCItemMeta("depleted_fuel_rod_thorium", MetaEnums.ThoriumDepletedFuelRodType.class);
		depleted_fuel_rod_uranium = new NCItemMeta("depleted_fuel_rod_uranium", MetaEnums.UraniumDepletedFuelRodType.class);
		depleted_fuel_rod_neptunium = new NCItemMeta("depleted_fuel_rod_neptunium", MetaEnums.NeptuniumDepletedFuelRodType.class);
		depleted_fuel_rod_plutonium = new NCItemMeta("depleted_fuel_rod_plutonium", MetaEnums.PlutoniumDepletedFuelRodType.class);
		depleted_fuel_rod_mixed_oxide = new NCItemMeta("depleted_fuel_rod_mixed_oxide", MetaEnums.MixedOxideDepletedFuelRodType.class);
		depleted_fuel_rod_americium = new NCItemMeta("depleted_fuel_rod_americium", MetaEnums.AmericiumDepletedFuelRodType.class);
		depleted_fuel_rod_curium = new NCItemMeta("depleted_fuel_rod_curium", MetaEnums.CuriumDepletedFuelRodType.class);
		depleted_fuel_rod_berkelium = new NCItemMeta("depleted_fuel_rod_berkelium", MetaEnums.BerkeliumDepletedFuelRodType.class);
		depleted_fuel_rod_californium = new NCItemMeta("depleted_fuel_rod_californium", MetaEnums.CaliforniumDepletedFuelRodType.class);
		
		boron = new NCItemMeta("boron", MetaEnums.BoronType.class);
		lithium = new NCItemMeta("lithium", MetaEnums.LithiumType.class);
		
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
		registerItem(fuel_rod_empty, CommonProxy.TAB_FISSION_FUEL_RODS);
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
		
		registerItem(fuel_thorium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_uranium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_neptunium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_plutonium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_mixed_oxide, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_americium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_curium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_berkelium, CommonProxy.TAB_FISSION_MATERIALS);
		registerItem(fuel_californium, CommonProxy.TAB_FISSION_MATERIALS);
		
		registerItem(fuel_rod_thorium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_uranium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_neptunium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_plutonium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_mixed_oxide, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_americium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_curium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_berkelium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(fuel_rod_californium, CommonProxy.TAB_FISSION_FUEL_RODS);
		
		registerItem(depleted_fuel_rod_thorium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_uranium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_neptunium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_plutonium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_mixed_oxide, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_americium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_curium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_berkelium, CommonProxy.TAB_FISSION_FUEL_RODS);
		registerItem(depleted_fuel_rod_californium, CommonProxy.TAB_FISSION_FUEL_RODS);
		
		registerItem(boron, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		registerItem(lithium, CommonProxy.TAB_BASE_ITEM_MATERIALS);
		
		registerItem(portable_ender_chest, CommonProxy.TAB_MISC);
		
		registerItem(dominos, CommonProxy.TAB_MISC);
		
		registerItem(record_wanderer, CommonProxy.TAB_MISC);
		registerItem(record_end_of_the_world, CommonProxy.TAB_MISC);
		registerItem(record_money_for_nothing, CommonProxy.TAB_MISC);
	}
	
	public static void registerRenders() {
		for(int i = 0; i < IngotType.values().length; i++) {
			registerRender(ingot, i, "ingot_" + IngotType.values()[i].getName());
		}
		for(int i = 0; i < IngotOxideType.values().length; i++) {
			registerRender(ingot_oxide, i, "ingot_oxide_" + IngotOxideType.values()[i].getName());
		}
		
		for(int i = 0; i < DustType.values().length; i++) {
			registerRender(dust, i, "dust_" + DustType.values()[i].getName());
		}
		
		for(int i = 0; i < DustOxideType.values().length; i++) {
			registerRender(dust_oxide, i, "dust_oxide_" + DustOxideType.values()[i].getName());
		}
		
		for(int i = 0; i < GemType.values().length; i++) {
			registerRender(gem, i, "gem_" + GemType.values()[i].getName());
		}
		
		for(int i = 0; i < GemDustType.values().length; i++) {
			registerRender(gem_dust, i, "gem_dust_" + GemDustType.values()[i].getName());
		}
		
		for(int i = 0; i < AlloyType.values().length; i++) {
			registerRender(alloy, i, "alloy_" + AlloyType.values()[i].getName());
		}
		
		for(int i = 0; i < 	CompoundType.values().length; i++) {
			registerRender(compound, i, "compound_" + CompoundType.values()[i].getName());
		}
		
		for(int i = 0; i < PartType.values().length; i++) {
			registerRender(part, i, "part_" + PartType.values()[i].getName());
		}
		
		for(int i = 0; i < UpgradeType.values().length; i++) {
			registerRender(upgrade, i, "upgrade_" + UpgradeType.values()[i].getName());
		}
		
		registerRender(fuel_rod_empty);
		registerRender(tiny_dust_lead);
		registerRender(reactor_door);
		
		for(int i = 0; i < ThoriumType.values().length; i++) {
			registerRender(thorium, i, "thorium" + ThoriumType.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumType.values().length; i++) {
			registerRender(uranium, i, "uranium" + UraniumType.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumType.values().length; i++) {
			registerRender(neptunium, i, "neptunium" + NeptuniumType.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumType.values().length; i++) {
			registerRender(plutonium, i, "plutonium" + PlutoniumType.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumType.values().length; i++) {
			registerRender(americium, i, "americium" + AmericiumType.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumType.values().length; i++) {
			registerRender(curium, i, "curium" + CuriumType.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumType.values().length; i++) {
			registerRender(berkelium, i, "berkelium" + BerkeliumType.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumType.values().length; i++) {
			registerRender(californium, i, "californium" + CaliforniumType.values()[i].getName());
		}
		
		for(int i = 0; i < ThoriumFuelType.values().length; i++) {
			registerRender(fuel_thorium, i, "fuel_thorium_" + ThoriumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumFuelType.values().length; i++) {
			registerRender(fuel_uranium, i, "fuel_uranium_" + UraniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumFuelType.values().length; i++) {
			registerRender(fuel_neptunium, i, "fuel_neptunium_" + NeptuniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumFuelType.values().length; i++) {
			registerRender(fuel_plutonium, i, "fuel_plutonium_" + PlutoniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < MixedOxideFuelType.values().length; i++) {
			registerRender(fuel_mixed_oxide, i, "fuel_mixed_oxide_" + MixedOxideFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumFuelType.values().length; i++) {
			registerRender(fuel_americium, i, "fuel_americium_" + AmericiumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumFuelType.values().length; i++) {
			registerRender(fuel_curium, i, "fuel_curium_" + CuriumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumFuelType.values().length; i++) {
			registerRender(fuel_berkelium, i, "fuel_berkelium_" + BerkeliumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumFuelType.values().length; i++) {
			registerRender(fuel_californium, i, "fuel_californium_" + CaliforniumFuelType.values()[i].getName());
		}
		
		for(int i = 0; i < ThoriumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_thorium, i, "fuel_rod_thorium_" + ThoriumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_uranium, i, "fuel_rod_uranium_" + UraniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_neptunium, i, "fuel_rod_neptunium_" + NeptuniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_plutonium, i, "fuel_rod_plutonium_" + PlutoniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MixedOxideFuelRodType.values().length; i++) {
			registerRender(fuel_rod_mixed_oxide, i, "fuel_rod_mixed_oxide_" + MixedOxideFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_americium, i, "fuel_rod_americium_" + AmericiumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_curium, i, "fuel_rod_curium_" + CuriumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_berkelium, i, "fuel_rod_berkelium_" + BerkeliumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumFuelRodType.values().length; i++) {
			registerRender(fuel_rod_californium, i, "fuel_rod_californium_" + CaliforniumFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < ThoriumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_thorium, i, "depleted_fuel_rod_thorium_" + ThoriumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_uranium, i, "depleted_fuel_rod_uranium_" + UraniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_neptunium, i, "depleted_fuel_rod_neptunium_" + NeptuniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_plutonium, i, "depleted_fuel_rod_plutonium_" + PlutoniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < MixedOxideDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_mixed_oxide, i, "depleted_fuel_rod_mixed_oxide_" + MixedOxideDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_americium, i, "depleted_fuel_rod_americium_" + AmericiumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_curium, i, "depleted_fuel_rod_curium_" + CuriumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_berkelium, i, "depleted_fuel_rod_berkelium_" + BerkeliumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumDepletedFuelRodType.values().length; i++) {
			registerRender(depleted_fuel_rod_californium, i, "depleted_fuel_rod_californium_" + CaliforniumDepletedFuelRodType.values()[i].getName());
		}
		
		for(int i = 0; i < BoronType.values().length; i++) {
			registerRender(boron, i, "boron" + BoronType.values()[i].getName());
		}
		
		for(int i = 0; i < LithiumType.values().length; i++) {
			registerRender(lithium, i, "lithium" + LithiumType.values()[i].getName());
		}
		
		registerRender(portable_ender_chest);
		
		registerRender(dominos);
		
		registerRender(record_wanderer);
		registerRender(record_end_of_the_world);
		registerRender(record_money_for_nothing);
	}
	
	public static void registerItem(Item item, CreativeTabs tab) {
		item.setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item);
		NCUtil.getLogger().info("Registered item " + item.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
		NCUtil.getLogger().info("Registered render for item " + item.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Item item, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fileName), "inventory"));
		NCUtil.getLogger().info("Registered render for item " + fileName);
	}
}
