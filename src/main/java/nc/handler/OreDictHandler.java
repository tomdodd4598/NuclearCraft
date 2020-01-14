package nc.handler;

import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHandler {
	
	public static void registerOres() {
		OreDictionary.registerOre("oreCopper", new ItemStack(NCBlocks.ore, 1, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(NCBlocks.ore, 1, 1));
		OreDictionary.registerOre("oreLead", new ItemStack(NCBlocks.ore, 1, 2));
		OreDictionary.registerOre("oreThorium", new ItemStack(NCBlocks.ore, 1, 3));
		OreDictionary.registerOre("oreUranium", new ItemStack(NCBlocks.ore, 1, 4));
		OreDictionary.registerOre("oreBoron", new ItemStack(NCBlocks.ore, 1, 5));
		OreDictionary.registerOre("oreLithium", new ItemStack(NCBlocks.ore, 1, 6));
		OreDictionary.registerOre("oreMagnesium", new ItemStack(NCBlocks.ore, 1, 7));
		
		OreDictionary.registerOre("blockCopper", new ItemStack(NCBlocks.ingot_block, 1, 0));
		OreDictionary.registerOre("blockTin", new ItemStack(NCBlocks.ingot_block, 1, 1));
		OreDictionary.registerOre("blockLead", new ItemStack(NCBlocks.ingot_block, 1, 2));
		OreDictionary.registerOre("blockThorium", new ItemStack(NCBlocks.ingot_block, 1, 3));
		OreDictionary.registerOre("blockUranium", new ItemStack(NCBlocks.ingot_block, 1, 4));
		OreDictionary.registerOre("blockBoron", new ItemStack(NCBlocks.ingot_block, 1, 5));
		OreDictionary.registerOre("blockLithium", new ItemStack(NCBlocks.ingot_block, 1, 6));
		OreDictionary.registerOre("blockMagnesium", new ItemStack(NCBlocks.ingot_block, 1, 7));
		OreDictionary.registerOre("blockGraphite", new ItemStack(NCBlocks.ingot_block, 1, 8));
		OreDictionary.registerOre("blockBeryllium", new ItemStack(NCBlocks.ingot_block, 1, 9));
		OreDictionary.registerOre("blockZirconium", new ItemStack(NCBlocks.ingot_block, 1, 10));
		OreDictionary.registerOre("blockManganese", new ItemStack(NCBlocks.ingot_block, 1, 11));
		OreDictionary.registerOre("blockAluminum", new ItemStack(NCBlocks.ingot_block, 1, 12));
		OreDictionary.registerOre("blockSilver", new ItemStack(NCBlocks.ingot_block, 1, 13));
		
		OreDictionary.registerOre("blockFissionModerator", new ItemStack(NCBlocks.ingot_block, 1, 8));
		OreDictionary.registerOre("blockFissionModerator", new ItemStack(NCBlocks.ingot_block, 1, 9));
		
		OreDictionary.registerOre("blockUranium238", new ItemStack(NCBlocks.fertile_isotope, 1, 0));
		OreDictionary.registerOre("blockNeptunium237", new ItemStack(NCBlocks.fertile_isotope, 1, 1));
		OreDictionary.registerOre("blockPlutonium242", new ItemStack(NCBlocks.fertile_isotope, 1, 2));
		OreDictionary.registerOre("blockAmericium243", new ItemStack(NCBlocks.fertile_isotope, 1, 3));
		OreDictionary.registerOre("blockCurium246", new ItemStack(NCBlocks.fertile_isotope, 1, 4));
		OreDictionary.registerOre("blockBerkelium247", new ItemStack(NCBlocks.fertile_isotope, 1, 5));
		OreDictionary.registerOre("blockCalifornium252", new ItemStack(NCBlocks.fertile_isotope, 1, 6));
		
		OreDictionary.registerOre("ingotCopper", new ItemStack(NCItems.ingot, 1, 0));
		OreDictionary.registerOre("ingotTin", new ItemStack(NCItems.ingot, 1, 1));
		OreDictionary.registerOre("ingotLead", new ItemStack(NCItems.ingot, 1, 2));
		OreDictionary.registerOre("ingotThorium", new ItemStack(NCItems.ingot, 1, 3));
		OreDictionary.registerOre("ingotUranium", new ItemStack(NCItems.ingot, 1, 4));
		OreDictionary.registerOre("ingotBoron", new ItemStack(NCItems.ingot, 1, 5));
		OreDictionary.registerOre("ingotLithium", new ItemStack(NCItems.ingot, 1, 6));
		OreDictionary.registerOre("ingotMagnesium", new ItemStack(NCItems.ingot, 1, 7));
		OreDictionary.registerOre("ingotGraphite", new ItemStack(NCItems.ingot, 1, 8));
		OreDictionary.registerOre("ingotBeryllium", new ItemStack(NCItems.ingot, 1, 9));
		OreDictionary.registerOre("ingotZirconium", new ItemStack(NCItems.ingot, 1, 10));
		OreDictionary.registerOre("ingotManganese", new ItemStack(NCItems.ingot, 1, 11));
		OreDictionary.registerOre("ingotAluminum", new ItemStack(NCItems.ingot, 1, 12));
		OreDictionary.registerOre("ingotSilver", new ItemStack(NCItems.ingot, 1, 13));
		OreDictionary.registerOre("ingotManganeseOxide", new ItemStack(NCItems.ingot, 1, 14));
		OreDictionary.registerOre("ingotManganeseDioxide", new ItemStack(NCItems.ingot, 1, 15));
		
		OreDictionary.registerOre("dustCopper", new ItemStack(NCItems.dust, 1, 0));
		OreDictionary.registerOre("dustTin", new ItemStack(NCItems.dust, 1, 1));
		OreDictionary.registerOre("dustLead", new ItemStack(NCItems.dust, 1, 2));
		OreDictionary.registerOre("dustThorium", new ItemStack(NCItems.dust, 1, 3));
		OreDictionary.registerOre("dustUranium", new ItemStack(NCItems.dust, 1, 4));
		OreDictionary.registerOre("dustBoron", new ItemStack(NCItems.dust, 1, 5));
		OreDictionary.registerOre("dustLithium", new ItemStack(NCItems.dust, 1, 6));
		OreDictionary.registerOre("dustMagnesium", new ItemStack(NCItems.dust, 1, 7));
		OreDictionary.registerOre("dustGraphite", new ItemStack(NCItems.dust, 1, 8));
		OreDictionary.registerOre("dustBeryllium", new ItemStack(NCItems.dust, 1, 9));
		OreDictionary.registerOre("dustZirconium", new ItemStack(NCItems.dust, 1, 10));
		OreDictionary.registerOre("dustManganese", new ItemStack(NCItems.dust, 1, 11));
		OreDictionary.registerOre("dustAluminum", new ItemStack(NCItems.dust, 1, 12));
		OreDictionary.registerOre("dustSilver", new ItemStack(NCItems.dust, 1, 13));
		OreDictionary.registerOre("dustManganeseOxide", new ItemStack(NCItems.dust, 1, 14));
		OreDictionary.registerOre("dustManganeseDioxide", new ItemStack(NCItems.dust, 1, 15));
		
		OreDictionary.registerOre("gemRhodochrosite", new ItemStack(NCItems.gem, 1, 0));
		OreDictionary.registerOre("gemBoronNitride", new ItemStack(NCItems.gem, 1, 1));
		OreDictionary.registerOre("gemFluorite", new ItemStack(NCItems.gem, 1, 2));
		OreDictionary.registerOre("gemVilliaumite", new ItemStack(NCItems.gem, 1, 3));
		OreDictionary.registerOre("gemCarobbiite", new ItemStack(NCItems.gem, 1, 4));
		OreDictionary.registerOre("gemBoronArsenide", new ItemStack(NCItems.gem, 1, 5));
		OreDictionary.registerOre("itemSilicon", new ItemStack(NCItems.gem, 1, 6));
		
		OreDictionary.registerOre("dustDiamond", new ItemStack(NCItems.gem_dust, 1, 0));
		OreDictionary.registerOre("dustRhodochrosite", new ItemStack(NCItems.gem_dust, 1, 1));
		OreDictionary.registerOre("dustQuartz", new ItemStack(NCItems.gem_dust, 1, 2));
		OreDictionary.registerOre("dustNetherQuartz", new ItemStack(NCItems.gem_dust, 1, 2));
		OreDictionary.registerOre("dustObsidian", new ItemStack(NCItems.gem_dust, 1, 3));
		OreDictionary.registerOre("dustBoronNitride", new ItemStack(NCItems.gem_dust, 1, 4));
		OreDictionary.registerOre("dustFluorite", new ItemStack(NCItems.gem_dust, 1, 5));
		OreDictionary.registerOre("dustSulfur", new ItemStack(NCItems.gem_dust, 1, 6));
		OreDictionary.registerOre("dustCoal", new ItemStack(NCItems.gem_dust, 1, 7));
		OreDictionary.registerOre("dustVilliaumite", new ItemStack(NCItems.gem_dust, 1, 8));
		OreDictionary.registerOre("dustCarobbiite", new ItemStack(NCItems.gem_dust, 1, 9));
		OreDictionary.registerOre("dustArsenic", new ItemStack(NCItems.gem_dust, 1, 10));
		OreDictionary.registerOre("dustEndstone", new ItemStack(NCItems.gem_dust, 1, 11));
		
		OreDictionary.registerOre("ingotBronze", new ItemStack(NCItems.alloy, 1, 0));
		OreDictionary.registerOre("ingotTough", new ItemStack(NCItems.alloy, 1, 1));
		OreDictionary.registerOre("ingotHardCarbon", new ItemStack(NCItems.alloy, 1, 2));
		OreDictionary.registerOre("ingotMagnesiumDiboride", new ItemStack(NCItems.alloy, 1, 3));
		OreDictionary.registerOre("ingotLithiumManganeseDioxide", new ItemStack(NCItems.alloy, 1, 4));
		OreDictionary.registerOre("ingotSteel", new ItemStack(NCItems.alloy, 1, 5));
		OreDictionary.registerOre("ingotFerroboron", new ItemStack(NCItems.alloy, 1, 6));
		OreDictionary.registerOre("ingotShibuichi", new ItemStack(NCItems.alloy, 1, 7));
		OreDictionary.registerOre("ingotTinSilver", new ItemStack(NCItems.alloy, 1, 8));
		OreDictionary.registerOre("ingotLeadPlatinum", new ItemStack(NCItems.alloy, 1, 9));
		OreDictionary.registerOre("ingotExtreme", new ItemStack(NCItems.alloy, 1, 10));
		OreDictionary.registerOre("ingotThermoconducting", new ItemStack(NCItems.alloy, 1, 11));
		OreDictionary.registerOre("ingotZircaloy", new ItemStack(NCItems.alloy, 1, 12));
		OreDictionary.registerOre("ingotSiliconCarbide", new ItemStack(NCItems.alloy, 1, 13));
		OreDictionary.registerOre("ingotSiCSiCCMC", new ItemStack(NCItems.alloy, 1, 14));
		OreDictionary.registerOre("ingotHSLASteel", new ItemStack(NCItems.alloy, 1, 15));
		
		OreDictionary.registerOre("dustCalciumSulfate", new ItemStack(NCItems.compound, 1, 0));
		OreDictionary.registerOre("dustCrystalBinder", new ItemStack(NCItems.compound, 1, 1));
		OreDictionary.registerOre("dustEnergetic", new ItemStack(NCItems.compound, 1, 2));
		OreDictionary.registerOre("dustSodiumFluoride", new ItemStack(NCItems.compound, 1, 3));
		OreDictionary.registerOre("dustPotassiumFluoride", new ItemStack(NCItems.compound, 1, 4));
		OreDictionary.registerOre("dustSodiumHydroxide", new ItemStack(NCItems.compound, 1, 5));
		OreDictionary.registerOre("dustPotassiumHydroxide", new ItemStack(NCItems.compound, 1, 6));
		OreDictionary.registerOre("dustBorax", new ItemStack(NCItems.compound, 1, 7));
		OreDictionary.registerOre("dustIrradiatedBorax", new ItemStack(NCItems.compound, 1, 8));
		OreDictionary.registerOre("dustDimensional", new ItemStack(NCItems.compound, 1, 9));
		OreDictionary.registerOre("dustCarbonManganese", new ItemStack(NCItems.compound, 1, 10));
		OreDictionary.registerOre("dustAlugentum", new ItemStack(NCItems.compound, 1, 11));
		
		OreDictionary.registerOre("plateBasic", new ItemStack(NCItems.part, 1, 0));
		OreDictionary.registerOre("plateAdvanced", new ItemStack(NCItems.part, 1, 1));
		OreDictionary.registerOre("plateDU", new ItemStack(NCItems.part, 1, 2));
		OreDictionary.registerOre("plateElite", new ItemStack(NCItems.part, 1, 3));
		OreDictionary.registerOre("solenoidCopper", new ItemStack(NCItems.part, 1, 4));
		OreDictionary.registerOre("solenoidMagnesiumDiboride", new ItemStack(NCItems.part, 1, 5));
		OreDictionary.registerOre("bioplastic", new ItemStack(NCItems.part, 1, 6));
		OreDictionary.registerOre("servo", new ItemStack(NCItems.part, 1, 7));
		OreDictionary.registerOre("motor", new ItemStack(NCItems.part, 1, 8));
		OreDictionary.registerOre("actuator", new ItemStack(NCItems.part, 1, 9));
		OreDictionary.registerOre("chassis", new ItemStack(NCItems.part, 1, 10));
		OreDictionary.registerOre("emptyFrame", new ItemStack(NCItems.part, 1, 11));
		OreDictionary.registerOre("steelFrame", new ItemStack(NCItems.part, 1, 12));
		OreDictionary.registerOre("fiberSiliconCarbide", new ItemStack(NCItems.part, 1, 13));
		OreDictionary.registerOre("emptyHeatSink", new ItemStack(NCItems.part, 1, 14));
		OreDictionary.registerOre("ingotPyrolyticCarbon", new ItemStack(NCItems.part, 1, 15));
		
		OreDictionary.registerOre("dustBismuth", new ItemStack(NCItems.fission_dust, 1, 0));
		OreDictionary.registerOre("dustRadium", new ItemStack(NCItems.fission_dust, 1, 1));
		OreDictionary.registerOre("dustPolonium", new ItemStack(NCItems.fission_dust, 1, 2));
		OreDictionary.registerOre("dustTBP", new ItemStack(NCItems.fission_dust, 1, 3));
		OreDictionary.registerOre("dustProtactinium233", new ItemStack(NCItems.fission_dust, 1, 4));
		
		registerIsotopes(NCItems.uranium, "Uranium", 233, 235, 238);
		registerIsotopes(NCItems.neptunium, "Neptunium", 236, 237);
		registerIsotopes(NCItems.plutonium, "Plutonium", 238, 239, 241, 242);
		registerIsotopes(NCItems.americium, "Americium", 241, 242, 243);
		registerIsotopes(NCItems.curium, "Curium", 243, 245, 246, 247);
		registerIsotopes(NCItems.berkelium, "Berkelium", 247, 248);
		registerIsotopes(NCItems.californium, "Californium", 249, 250, 251, 252);
		
		registerFuels(NCItems.pellet_thorium, NCItems.fuel_thorium, NCItems.depleted_fuel_thorium, "TBU");
		registerFuels(NCItems.pellet_uranium, NCItems.fuel_uranium, NCItems.depleted_fuel_uranium, "LEU233", "HEU233", "LEU235", "HEU235");
		registerFuels(NCItems.pellet_neptunium, NCItems.fuel_neptunium, NCItems.depleted_fuel_neptunium, "LEN236", "HEN236");
		registerFuels(NCItems.pellet_plutonium, NCItems.fuel_plutonium, NCItems.depleted_fuel_plutonium, "LEP239", "HEP239", "LEP241", "HEP241");
		registerFuels(NCItems.pellet_mixed, NCItems.fuel_mixed, NCItems.depleted_fuel_mixed, "MIX239", "MIX241");
		registerFuels(NCItems.pellet_americium, NCItems.fuel_americium, NCItems.depleted_fuel_americium, "LEA242", "HEA242");
		registerFuels(NCItems.pellet_curium, NCItems.fuel_curium, NCItems.depleted_fuel_curium, "LECm243", "HECm243", "LECm245", "HECm245", "LECm247", "HECm247");
		registerFuels(NCItems.pellet_berkelium, NCItems.fuel_berkelium, NCItems.depleted_fuel_berkelium, "LEB248", "HEB248");
		registerFuels(NCItems.pellet_californium, NCItems.fuel_californium, NCItems.depleted_fuel_californium, "LECf249", "HECf249", "LECf251", "HECf251");
		
		OreDictionary.registerOre("depletedFuelIC2U", new ItemStack(NCItems.depleted_fuel_ic2, 1, 0));
		OreDictionary.registerOre("depletedFuelIC2MOX", new ItemStack(NCItems.depleted_fuel_ic2, 1, 1));
		
		OreDictionary.registerOre("ingotBoron10", new ItemStack(NCItems.boron, 1, 0));
		OreDictionary.registerOre("ingotBoron11", new ItemStack(NCItems.boron, 1, 1));
		
		OreDictionary.registerOre("ingotLithium6", new ItemStack(NCItems.lithium, 1, 0));
		OreDictionary.registerOre("ingotLithium7", new ItemStack(NCItems.lithium, 1, 1));
		
		OreDictionary.registerOre("gearDominos", NCItems.dominos);
		
		OreDictionary.registerOre("dustWheat", NCItems.flour);
		OreDictionary.registerOre("dyeBrown", NCItems.cocoa_solids);
		OreDictionary.registerOre("dustCocoa", NCItems.cocoa_solids);
		OreDictionary.registerOre("ingotCocoaButter", NCItems.cocoa_butter);
		OreDictionary.registerOre("ingotUnsweetenedChocolate", NCItems.unsweetened_chocolate);
		OreDictionary.registerOre("ingotDarkChocolate", NCItems.dark_chocolate);
		OreDictionary.registerOre("ingotChocolate", NCItems.milk_chocolate);
		OreDictionary.registerOre("ingotMarshmallow", NCItems.marshmallow);
		
		OreDictionary.registerOre("record", NCItems.record_wanderer);
		OreDictionary.registerOre("record", NCItems.record_end_of_the_world);
		OreDictionary.registerOre("record", NCItems.record_money_for_nothing);
		OreDictionary.registerOre("record", NCItems.record_hyperspace);
		
		// Vanilla
		OreDictionary.registerOre("coal", Items.COAL);
		OreDictionary.registerOre("charcoal", new ItemStack(Items.COAL, 1, 1));
		OreDictionary.registerOre("wool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	public static void registerOre(Item item, int meta, String... names) {
		if (meta >= 0) for (String name : names) {
			OreDictionary.registerOre(name, new ItemStack(item, 1, meta));
		}
	}
	
	public static void registerIsotope(Item item, int meta, String name, String type) {
		registerOre(item, meta, "ingot" + name + "All", "ingot" + name + type);
	}
	
	public static void registerIsotopeAll(Item item, int startMeta, String name) {
		registerIsotope(item, startMeta, name, "");
		registerIsotope(item, startMeta + 1, name, "Carbide");
		registerIsotope(item, startMeta + 2, name, "Oxide");
		registerIsotope(item, startMeta + 3, name, "Nitride");
		registerIsotope(item, startMeta + 4, name, "ZA");
	}
	
	public static void registerIsotopes(Item item, String name, int... types) {
		for (int i = 0; i < types.length; i++) {
			registerIsotopeAll(item, 5*i, name + types[i]);
		}
	}
	
	public static void registerFuels(Item pellet, Item fuel, Item depleted, String... types) {
		for (int i = 0; i < types.length; i++) {
			registerOre(pellet, 2*i, "ingot" + types[i]);
			registerOre(pellet, 2*i + 1, "ingot" + types[i] + "Carbide");
			registerOre(fuel, 4*i, "ingot" + types[i] + "TRISO");
			registerOre(fuel, 4*i + 1, "ingot" + types[i] + "Oxide");
			registerOre(fuel, 4*i + 2, "ingot" + types[i] + "Nitride");
			registerOre(fuel, 4*i + 3, "ingot" + types[i] + "ZA");
			registerOre(depleted, 4*i, "ingotDepleted" + types[i] + "TRISO");
			registerOre(depleted, 4*i + 1, "ingotDepleted" + types[i] + "Oxide");
			registerOre(depleted, 4*i + 2, "ingotDepleted" + types[i] + "Nitride");
			registerOre(depleted, 4*i + 3, "ingotDepleted" + types[i] + "ZA");
		}
	}
}
