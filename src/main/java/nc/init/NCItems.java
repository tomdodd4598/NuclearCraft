package nc.init;

import nc.Global;
import nc.handlers.EnumHandler.AlloyTypes;
import nc.handlers.EnumHandler.AmericiumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.AmericiumFuelRodTypes;
import nc.handlers.EnumHandler.AmericiumFuelTypes;
import nc.handlers.EnumHandler.AmericiumTypes;
import nc.handlers.EnumHandler.BerkeliumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.BerkeliumFuelRodTypes;
import nc.handlers.EnumHandler.BerkeliumFuelTypes;
import nc.handlers.EnumHandler.BerkeliumTypes;
import nc.handlers.EnumHandler.CaliforniumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.CaliforniumFuelRodTypes;
import nc.handlers.EnumHandler.CaliforniumFuelTypes;
import nc.handlers.EnumHandler.CaliforniumTypes;
import nc.handlers.EnumHandler.CuriumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.CuriumFuelRodTypes;
import nc.handlers.EnumHandler.CuriumFuelTypes;
import nc.handlers.EnumHandler.CuriumTypes;
import nc.handlers.EnumHandler.DustOxideTypes;
import nc.handlers.EnumHandler.DustTypes;
import nc.handlers.EnumHandler.GemDustTypes;
import nc.handlers.EnumHandler.GemTypes;
import nc.handlers.EnumHandler.IngotOxideTypes;
import nc.handlers.EnumHandler.IngotTypes;
import nc.handlers.EnumHandler.MixedOxideDepletedFuelRodTypes;
import nc.handlers.EnumHandler.MixedOxideFuelRodTypes;
import nc.handlers.EnumHandler.MixedOxideFuelTypes;
import nc.handlers.EnumHandler.NeptuniumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.NeptuniumFuelRodTypes;
import nc.handlers.EnumHandler.NeptuniumFuelTypes;
import nc.handlers.EnumHandler.NeptuniumTypes;
import nc.handlers.EnumHandler.PartTypes;
import nc.handlers.EnumHandler.PlutoniumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.PlutoniumFuelRodTypes;
import nc.handlers.EnumHandler.PlutoniumFuelTypes;
import nc.handlers.EnumHandler.PlutoniumTypes;
import nc.handlers.EnumHandler.ThoriumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.ThoriumFuelRodTypes;
import nc.handlers.EnumHandler.ThoriumFuelTypes;
import nc.handlers.EnumHandler.ThoriumTypes;
import nc.handlers.EnumHandler.UpgradeTypes;
import nc.handlers.EnumHandler.UraniumDepletedFuelRodTypes;
import nc.handlers.EnumHandler.UraniumFuelRodTypes;
import nc.handlers.EnumHandler.UraniumFuelTypes;
import nc.handlers.EnumHandler.UraniumTypes;
import nc.items.ItemAlloy;
import nc.items.ItemDust;
import nc.items.ItemDustOxide;
import nc.items.ItemGem;
import nc.items.ItemGemDust;
import nc.items.ItemIngot;
import nc.items.ItemIngotOxide;
import nc.items.ItemPart;
import nc.items.ItemPortableEnderChest;
import nc.items.ItemUpgrade;
import nc.items.NCItem;
import nc.items.NCItemFood;
import nc.items.fission.ItemAmericium;
import nc.items.fission.ItemBerkelium;
import nc.items.fission.ItemCalifornium;
import nc.items.fission.ItemCurium;
import nc.items.fission.ItemDepletedFuelRodAmericium;
import nc.items.fission.ItemDepletedFuelRodBerkelium;
import nc.items.fission.ItemDepletedFuelRodCalifornium;
import nc.items.fission.ItemDepletedFuelRodCurium;
import nc.items.fission.ItemDepletedFuelRodMixedOxide;
import nc.items.fission.ItemDepletedFuelRodNeptunium;
import nc.items.fission.ItemDepletedFuelRodPlutonium;
import nc.items.fission.ItemDepletedFuelRodThorium;
import nc.items.fission.ItemDepletedFuelRodUranium;
import nc.items.fission.ItemFuelAmericium;
import nc.items.fission.ItemFuelBerkelium;
import nc.items.fission.ItemFuelCalifornium;
import nc.items.fission.ItemFuelCurium;
import nc.items.fission.ItemFuelMixedOxide;
import nc.items.fission.ItemFuelNeptunium;
import nc.items.fission.ItemFuelPlutonium;
import nc.items.fission.ItemFuelRodAmericium;
import nc.items.fission.ItemFuelRodBerkelium;
import nc.items.fission.ItemFuelRodCalifornium;
import nc.items.fission.ItemFuelRodCurium;
import nc.items.fission.ItemFuelRodMixedOxide;
import nc.items.fission.ItemFuelRodNeptunium;
import nc.items.fission.ItemFuelRodPlutonium;
import nc.items.fission.ItemFuelRodThorium;
import nc.items.fission.ItemFuelRodUranium;
import nc.items.fission.ItemFuelThorium;
import nc.items.fission.ItemFuelUranium;
import nc.items.fission.ItemNeptunium;
import nc.items.fission.ItemPlutonium;
import nc.items.fission.ItemThorium;
import nc.items.fission.ItemUranium;
import nc.proxy.CommonProxy;
import nc.util.NCUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
	
	public static Item part;
	public static Item upgrade;
	public static Item fuel_rod_empty;
	public static Item tiny_dust_lead;
	
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
	
	public static Item portable_ender_chest;
	
	public static Item dominos;
	
	public static void init() {
		ingot = new ItemIngot("ingot", "ingot");
		ingot_oxide = new ItemIngotOxide("ingot_oxide", "ingot_oxide");
		dust = new ItemDust("dust", "dust");
		dust_oxide = new ItemDustOxide("dust_oxide", "dust_oxide");
		gem = new ItemGem("gem", "gem");
		gem_dust = new ItemGemDust("gem_dust", "gem_dust");
		alloy = new ItemAlloy("alloy", "alloy");
		
		part = new ItemPart("part", "part");
		upgrade = new ItemUpgrade("upgrade", "upgrade");
		fuel_rod_empty = new NCItem("fuel_rod_empty", "fuel_rod_empty");
		tiny_dust_lead = new NCItem("tiny_dust_lead", "tiny_dust_lead");
		
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
		
		portable_ender_chest = new ItemPortableEnderChest("portable_ender_chest", "portable_ender_chest", 1);
		
		dominos = new NCItemFood("dominos", "dominos", 16, 1.0F, false, new PotionEffect[] {new PotionEffect(Potion.getPotionById(1), 500, 2), new PotionEffect(Potion.getPotionById(3), 500, 2)}, 1);
	}
	
	public static void register() {
		registerItem(ingot);
		registerItem(ingot_oxide);
		registerItem(dust);
		registerItem(dust_oxide);
		registerItem(gem);
		registerItem(gem_dust);
		registerItem(alloy);
		
		registerItem(part);
		registerItem(upgrade);
		registerItem(fuel_rod_empty);
		registerItem(tiny_dust_lead);
		
		registerItem(thorium);
		registerItem(uranium);
		registerItem(neptunium);
		registerItem(plutonium);
		registerItem(americium);
		registerItem(curium);
		registerItem(berkelium);
		registerItem(californium);
		
		registerItem(fuel_thorium);
		registerItem(fuel_uranium);
		registerItem(fuel_neptunium);
		registerItem(fuel_plutonium);
		registerItem(fuel_mixed_oxide);
		registerItem(fuel_americium);
		registerItem(fuel_curium);
		registerItem(fuel_berkelium);
		registerItem(fuel_californium);
		
		registerItem(fuel_rod_thorium);
		registerItem(fuel_rod_uranium);
		registerItem(fuel_rod_neptunium);
		registerItem(fuel_rod_plutonium);
		registerItem(fuel_rod_mixed_oxide);
		registerItem(fuel_rod_americium);
		registerItem(fuel_rod_curium);
		registerItem(fuel_rod_berkelium);
		registerItem(fuel_rod_californium);
		
		registerItem(depleted_fuel_rod_thorium);
		registerItem(depleted_fuel_rod_uranium);
		registerItem(depleted_fuel_rod_neptunium);
		registerItem(depleted_fuel_rod_plutonium);
		registerItem(depleted_fuel_rod_mixed_oxide);
		registerItem(depleted_fuel_rod_americium);
		registerItem(depleted_fuel_rod_curium);
		registerItem(depleted_fuel_rod_berkelium);
		registerItem(depleted_fuel_rod_californium);
		
		registerItem(portable_ender_chest);
		
		registerItem(dominos);
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
		
		for(int i = 0; i < PartTypes.values().length; i++) {
			registerRender(part, i, "part_" + PartTypes.values()[i].getName());
		}
		
		for(int i = 0; i < UpgradeTypes.values().length; i++) {
			registerRender(upgrade, i, "upgrade_" + UpgradeTypes.values()[i].getName());
		}
		
		registerRender(fuel_rod_empty);
		registerRender(tiny_dust_lead);
		
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
		
		registerRender(portable_ender_chest);
		
		registerRender(dominos);
	}
	
	public static void registerItem(Item item) {
		item.setCreativeTab(CommonProxy.NC_TAB);
		GameRegistry.register(item);
		NCUtils.getLogger().info("Registered item " + item.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
		NCUtils.getLogger().info("Registered render for item " + item.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Item item, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fileName), "inventory"));
		NCUtils.getLogger().info("Registered render for item " + fileName);
	}
}
