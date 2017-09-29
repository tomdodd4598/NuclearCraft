package nc.init;

import nc.Global;
import nc.handler.EnumHandler.AlloyTypes;
import nc.handler.EnumHandler.AmericiumDepletedFuelRodTypes;
import nc.handler.EnumHandler.AmericiumFuelRodTypes;
import nc.handler.EnumHandler.AmericiumFuelTypes;
import nc.handler.EnumHandler.AmericiumTypes;
import nc.handler.EnumHandler.BerkeliumDepletedFuelRodTypes;
import nc.handler.EnumHandler.BerkeliumFuelRodTypes;
import nc.handler.EnumHandler.BerkeliumFuelTypes;
import nc.handler.EnumHandler.BerkeliumTypes;
import nc.handler.EnumHandler.BoronTypes;
import nc.handler.EnumHandler.CaliforniumDepletedFuelRodTypes;
import nc.handler.EnumHandler.CaliforniumFuelRodTypes;
import nc.handler.EnumHandler.CaliforniumFuelTypes;
import nc.handler.EnumHandler.CaliforniumTypes;
import nc.handler.EnumHandler.CompoundTypes;
import nc.handler.EnumHandler.CuriumDepletedFuelRodTypes;
import nc.handler.EnumHandler.CuriumFuelRodTypes;
import nc.handler.EnumHandler.CuriumFuelTypes;
import nc.handler.EnumHandler.CuriumTypes;
import nc.handler.EnumHandler.DustOxideTypes;
import nc.handler.EnumHandler.DustTypes;
import nc.handler.EnumHandler.GemDustTypes;
import nc.handler.EnumHandler.GemTypes;
import nc.handler.EnumHandler.IngotOxideTypes;
import nc.handler.EnumHandler.IngotTypes;
import nc.handler.EnumHandler.LithiumTypes;
import nc.handler.EnumHandler.MixedOxideDepletedFuelRodTypes;
import nc.handler.EnumHandler.MixedOxideFuelRodTypes;
import nc.handler.EnumHandler.MixedOxideFuelTypes;
import nc.handler.EnumHandler.NeptuniumDepletedFuelRodTypes;
import nc.handler.EnumHandler.NeptuniumFuelRodTypes;
import nc.handler.EnumHandler.NeptuniumFuelTypes;
import nc.handler.EnumHandler.NeptuniumTypes;
import nc.handler.EnumHandler.PartTypes;
import nc.handler.EnumHandler.PlutoniumDepletedFuelRodTypes;
import nc.handler.EnumHandler.PlutoniumFuelRodTypes;
import nc.handler.EnumHandler.PlutoniumFuelTypes;
import nc.handler.EnumHandler.PlutoniumTypes;
import nc.handler.EnumHandler.ThoriumDepletedFuelRodTypes;
import nc.handler.EnumHandler.ThoriumFuelRodTypes;
import nc.handler.EnumHandler.ThoriumFuelTypes;
import nc.handler.EnumHandler.ThoriumTypes;
import nc.handler.EnumHandler.UpgradeTypes;
import nc.handler.EnumHandler.UraniumDepletedFuelRodTypes;
import nc.handler.EnumHandler.UraniumFuelRodTypes;
import nc.handler.EnumHandler.UraniumFuelTypes;
import nc.handler.EnumHandler.UraniumTypes;
import nc.handler.SoundHandler;
import nc.item.ItemAlloy;
import nc.item.ItemCompound;
import nc.item.ItemDust;
import nc.item.ItemDustOxide;
import nc.item.ItemGem;
import nc.item.ItemGemDust;
import nc.item.ItemIngot;
import nc.item.ItemIngotOxide;
import nc.item.ItemPart;
import nc.item.ItemPortableEnderChest;
import nc.item.ItemUpgrade;
import nc.item.NCItem;
import nc.item.NCItemDoor;
import nc.item.NCItemFood;
import nc.item.NCItemRecord;
import nc.item.fission.ItemDepletedFuelRodAmericium;
import nc.item.fission.ItemDepletedFuelRodBerkelium;
import nc.item.fission.ItemDepletedFuelRodCalifornium;
import nc.item.fission.ItemDepletedFuelRodCurium;
import nc.item.fission.ItemDepletedFuelRodMixedOxide;
import nc.item.fission.ItemDepletedFuelRodNeptunium;
import nc.item.fission.ItemDepletedFuelRodPlutonium;
import nc.item.fission.ItemDepletedFuelRodThorium;
import nc.item.fission.ItemDepletedFuelRodUranium;
import nc.item.fission.ItemFuelAmericium;
import nc.item.fission.ItemFuelBerkelium;
import nc.item.fission.ItemFuelCalifornium;
import nc.item.fission.ItemFuelCurium;
import nc.item.fission.ItemFuelMixedOxide;
import nc.item.fission.ItemFuelNeptunium;
import nc.item.fission.ItemFuelPlutonium;
import nc.item.fission.ItemFuelRodAmericium;
import nc.item.fission.ItemFuelRodBerkelium;
import nc.item.fission.ItemFuelRodCalifornium;
import nc.item.fission.ItemFuelRodCurium;
import nc.item.fission.ItemFuelRodMixedOxide;
import nc.item.fission.ItemFuelRodNeptunium;
import nc.item.fission.ItemFuelRodPlutonium;
import nc.item.fission.ItemFuelRodThorium;
import nc.item.fission.ItemFuelRodUranium;
import nc.item.fission.ItemFuelThorium;
import nc.item.fission.ItemFuelUranium;
import nc.item.isotope.ItemAmericium;
import nc.item.isotope.ItemBerkelium;
import nc.item.isotope.ItemBoron;
import nc.item.isotope.ItemCalifornium;
import nc.item.isotope.ItemCurium;
import nc.item.isotope.ItemLithium;
import nc.item.isotope.ItemNeptunium;
import nc.item.isotope.ItemPlutonium;
import nc.item.isotope.ItemThorium;
import nc.item.isotope.ItemUranium;
import nc.proxy.CommonProxy;
import nc.util.NCUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		ingot = new ItemIngot("ingot", "ingot");
		ingot_oxide = new ItemIngotOxide("ingot_oxide", "ingot_oxide");
		dust = new ItemDust("dust", "dust");
		dust_oxide = new ItemDustOxide("dust_oxide", "dust_oxide");
		gem = new ItemGem("gem", "gem");
		gem_dust = new ItemGemDust("gem_dust", "gem_dust");
		alloy = new ItemAlloy("alloy", "alloy");
		compound = new ItemCompound("compound", "compound");
		
		part = new ItemPart("part", "part");
		upgrade = new ItemUpgrade("upgrade", "upgrade");
		fuel_rod_empty = new NCItem("fuel_rod_empty", "fuel_rod_empty");
		tiny_dust_lead = new NCItem("tiny_dust_lead", "tiny_dust_lead");
		reactor_door = new NCItemDoor("reactor_door_item", "reactor_door_item", NCBlocks.reactor_door);
		
		thorium = new ItemThorium("thorium", "thorium");
		uranium = new ItemUranium("uranium", "uranium");
		neptunium = new ItemNeptunium("neptunium", "neptunium");
		plutonium = new ItemPlutonium("plutonium", "plutonium");
		americium = new ItemAmericium("americium", "americium");
		curium = new ItemCurium("curium", "curium");
		berkelium = new ItemBerkelium("berkelium", "berkelium");
		californium = new ItemCalifornium("californium", "californium");
		
		fuel_thorium = new ItemFuelThorium("fuel_thorium", "fuel_thorium");
		fuel_uranium = new ItemFuelUranium("fuel_uranium", "fuel_uranium");
		fuel_neptunium = new ItemFuelNeptunium("fuel_neptunium", "fuel_neptunium");
		fuel_plutonium = new ItemFuelPlutonium("fuel_plutonium", "fuel_plutonium");
		fuel_mixed_oxide = new ItemFuelMixedOxide("fuel_mixed_oxide", "fuel_mixed_oxide");
		fuel_americium = new ItemFuelAmericium("fuel_americium", "fuel_americium");
		fuel_curium = new ItemFuelCurium("fuel_curium", "fuel_curium");
		fuel_berkelium = new ItemFuelBerkelium("fuel_berkelium", "fuel_berkelium");
		fuel_californium = new ItemFuelCalifornium("fuel_californium", "fuel_californium");
		
		fuel_rod_thorium = new ItemFuelRodThorium("fuel_rod_thorium", "fuel_rod_thorium");
		fuel_rod_uranium = new ItemFuelRodUranium("fuel_rod_uranium", "fuel_rod_uranium");
		fuel_rod_neptunium = new ItemFuelRodNeptunium("fuel_rod_neptunium", "fuel_rod_neptunium");
		fuel_rod_plutonium = new ItemFuelRodPlutonium("fuel_rod_plutonium", "fuel_rod_plutonium");
		fuel_rod_mixed_oxide = new ItemFuelRodMixedOxide("fuel_rod_mixed_oxide", "fuel_rod_mixed_oxide");
		fuel_rod_americium = new ItemFuelRodAmericium("fuel_rod_americium", "fuel_rod_americium");
		fuel_rod_curium = new ItemFuelRodCurium("fuel_rod_curium", "fuel_rod_curium");
		fuel_rod_berkelium = new ItemFuelRodBerkelium("fuel_rod_berkelium", "fuel_rod_berkelium");
		fuel_rod_californium = new ItemFuelRodCalifornium("fuel_rod_californium", "fuel_rod_californium");
		
		depleted_fuel_rod_thorium = new ItemDepletedFuelRodThorium("depleted_fuel_rod_thorium", "depleted_fuel_rod_thorium");
		depleted_fuel_rod_uranium = new ItemDepletedFuelRodUranium("depleted_fuel_rod_uranium", "depleted_fuel_rod_uranium");
		depleted_fuel_rod_neptunium = new ItemDepletedFuelRodNeptunium("depleted_fuel_rod_neptunium", "depleted_fuel_rod_neptunium");
		depleted_fuel_rod_plutonium = new ItemDepletedFuelRodPlutonium("depleted_fuel_rod_plutonium", "depleted_fuel_rod_plutonium");
		depleted_fuel_rod_mixed_oxide = new ItemDepletedFuelRodMixedOxide("depleted_fuel_rod_mixed_oxide", "depleted_fuel_rod_mixed_oxide");
		depleted_fuel_rod_americium = new ItemDepletedFuelRodAmericium("depleted_fuel_rod_americium", "depleted_fuel_rod_americium");
		depleted_fuel_rod_curium = new ItemDepletedFuelRodCurium("depleted_fuel_rod_curium", "depleted_fuel_rod_curium");
		depleted_fuel_rod_berkelium = new ItemDepletedFuelRodBerkelium("depleted_fuel_rod_berkelium", "depleted_fuel_rod_berkelium");
		depleted_fuel_rod_californium = new ItemDepletedFuelRodCalifornium("depleted_fuel_rod_californium", "depleted_fuel_rod_californium");
		
		boron = new ItemBoron("boron", "boron");
		lithium = new ItemLithium("lithium", "lithium");
		
		portable_ender_chest = new ItemPortableEnderChest("portable_ender_chest", "portable_ender_chest", 1);
		
		dominos = new NCItemFood("dominos", "dominos", 16, 1.0F, false, new PotionEffect[] {new PotionEffect(Potion.getPotionById(1), 500, 2), new PotionEffect(Potion.getPotionById(3), 500, 2)}, 1);
		
		record_wanderer = new NCItemRecord("wanderer", "wanderer", SoundHandler.WANDERER, 2);
		record_end_of_the_world = new NCItemRecord("end_of_the_world", "end_of_the_world", SoundHandler.END_OF_THE_WORLD, 2);
		record_money_for_nothing = new NCItemRecord("money_for_nothing", "money_for_nothing", SoundHandler.MONEY_FOR_NOTHING, 2);
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
		for(int i = 0; i < IngotTypes.values().length; i++) {
			registerRender(ingot, i, "ingot_" + IngotTypes.values()[i].getName());
		}
		for(int i = 0; i < IngotOxideTypes.values().length; i++) {
			registerRender(ingot_oxide, i, "ingot_oxide_" + IngotOxideTypes.values()[i].getName());
		}
		
		for(int i = 0; i < DustTypes.values().length; i++) {
			registerRender(dust, i, "dust_" + DustTypes.values()[i].getName());
		}
		
		for(int i = 0; i < DustOxideTypes.values().length; i++) {
			registerRender(dust_oxide, i, "dust_oxide_" + DustOxideTypes.values()[i].getName());
		}
		
		for(int i = 0; i < GemTypes.values().length; i++) {
			registerRender(gem, i, "gem_" + GemTypes.values()[i].getName());
		}
		
		for(int i = 0; i < GemDustTypes.values().length; i++) {
			registerRender(gem_dust, i, "gem_dust_" + GemDustTypes.values()[i].getName());
		}
		
		for(int i = 0; i < AlloyTypes.values().length; i++) {
			registerRender(alloy, i, "alloy_" + AlloyTypes.values()[i].getName());
		}
		
		for(int i = 0; i < 	CompoundTypes.values().length; i++) {
			registerRender(compound, i, "compound_" + CompoundTypes.values()[i].getName());
		}
		
		for(int i = 0; i < PartTypes.values().length; i++) {
			registerRender(part, i, "part_" + PartTypes.values()[i].getName());
		}
		
		for(int i = 0; i < UpgradeTypes.values().length; i++) {
			registerRender(upgrade, i, "upgrade_" + UpgradeTypes.values()[i].getName());
		}
		
		registerRender(fuel_rod_empty);
		registerRender(tiny_dust_lead);
		registerRender(reactor_door);
		
		for(int i = 0; i < ThoriumTypes.values().length; i++) {
			registerRender(thorium, i, "thorium" + ThoriumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumTypes.values().length; i++) {
			registerRender(uranium, i, "uranium" + UraniumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumTypes.values().length; i++) {
			registerRender(neptunium, i, "neptunium" + NeptuniumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumTypes.values().length; i++) {
			registerRender(plutonium, i, "plutonium" + PlutoniumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumTypes.values().length; i++) {
			registerRender(americium, i, "americium" + AmericiumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumTypes.values().length; i++) {
			registerRender(curium, i, "curium" + CuriumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumTypes.values().length; i++) {
			registerRender(berkelium, i, "berkelium" + BerkeliumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumTypes.values().length; i++) {
			registerRender(californium, i, "californium" + CaliforniumTypes.values()[i].getName());
		}
		
		for(int i = 0; i < ThoriumFuelTypes.values().length; i++) {
			registerRender(fuel_thorium, i, "fuel_thorium_" + ThoriumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumFuelTypes.values().length; i++) {
			registerRender(fuel_uranium, i, "fuel_uranium_" + UraniumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumFuelTypes.values().length; i++) {
			registerRender(fuel_neptunium, i, "fuel_neptunium_" + NeptuniumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumFuelTypes.values().length; i++) {
			registerRender(fuel_plutonium, i, "fuel_plutonium_" + PlutoniumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < MixedOxideFuelTypes.values().length; i++) {
			registerRender(fuel_mixed_oxide, i, "fuel_mixed_oxide_" + MixedOxideFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumFuelTypes.values().length; i++) {
			registerRender(fuel_americium, i, "fuel_americium_" + AmericiumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumFuelTypes.values().length; i++) {
			registerRender(fuel_curium, i, "fuel_curium_" + CuriumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumFuelTypes.values().length; i++) {
			registerRender(fuel_berkelium, i, "fuel_berkelium_" + BerkeliumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumFuelTypes.values().length; i++) {
			registerRender(fuel_californium, i, "fuel_californium_" + CaliforniumFuelTypes.values()[i].getName());
		}
		
		for(int i = 0; i < ThoriumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_thorium, i, "fuel_rod_thorium_" + ThoriumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_uranium, i, "fuel_rod_uranium_" + UraniumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_neptunium, i, "fuel_rod_neptunium_" + NeptuniumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_plutonium, i, "fuel_rod_plutonium_" + PlutoniumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < MixedOxideFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_mixed_oxide, i, "fuel_rod_mixed_oxide_" + MixedOxideFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_americium, i, "fuel_rod_americium_" + AmericiumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_curium, i, "fuel_rod_curium_" + CuriumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_berkelium, i, "fuel_rod_berkelium_" + BerkeliumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumFuelRodTypes.values().length; i++) {
			registerRender(fuel_rod_californium, i, "fuel_rod_californium_" + CaliforniumFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < ThoriumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_thorium, i, "depleted_fuel_rod_thorium_" + ThoriumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < UraniumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_uranium, i, "depleted_fuel_rod_uranium_" + UraniumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < NeptuniumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_neptunium, i, "depleted_fuel_rod_neptunium_" + NeptuniumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < PlutoniumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_plutonium, i, "depleted_fuel_rod_plutonium_" + PlutoniumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < MixedOxideDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_mixed_oxide, i, "depleted_fuel_rod_mixed_oxide_" + MixedOxideDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < AmericiumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_americium, i, "depleted_fuel_rod_americium_" + AmericiumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CuriumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_curium, i, "depleted_fuel_rod_curium_" + CuriumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < BerkeliumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_berkelium, i, "depleted_fuel_rod_berkelium_" + BerkeliumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < CaliforniumDepletedFuelRodTypes.values().length; i++) {
			registerRender(depleted_fuel_rod_californium, i, "depleted_fuel_rod_californium_" + CaliforniumDepletedFuelRodTypes.values()[i].getName());
		}
		
		for(int i = 0; i < BoronTypes.values().length; i++) {
			registerRender(boron, i, "boron" + BoronTypes.values()[i].getName());
		}
		
		for(int i = 0; i < LithiumTypes.values().length; i++) {
			registerRender(lithium, i, "lithium" + LithiumTypes.values()[i].getName());
		}
		
		registerRender(portable_ender_chest);
		
		registerRender(dominos);
		
		registerRender(record_wanderer);
		registerRender(record_end_of_the_world);
		registerRender(record_money_for_nothing);
	}
	
	public static void registerItem(Item item, CreativeTabs tab) {
		item.setCreativeTab(tab);
		GameRegistry.register(item);
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
