package nc.handler;

import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
		
		OreDictionary.registerOre("blockDepletedUranium", NCBlocks.block_depleted_uranium);
		
		OreDictionary.registerOre("ingotCopper", new ItemStack(NCItems.ingot, 1, 0));
		OreDictionary.registerOre("ingotTin", new ItemStack(NCItems.ingot, 1, 1));
		OreDictionary.registerOre("ingotLead", new ItemStack(NCItems.ingot, 1, 2));
		OreDictionary.registerOre("ingotThorium", new ItemStack(NCItems.ingot, 1, 3));
		OreDictionary.registerOre("ingotUranium", new ItemStack(NCItems.ingot, 1, 4));
		OreDictionary.registerOre("ingotBoron", new ItemStack(NCItems.ingot, 1, 5));
		OreDictionary.registerOre("ingotLithium", new ItemStack(NCItems.ingot, 1, 6));
		OreDictionary.registerOre("ingotMagnesium", new ItemStack(NCItems.ingot, 1, 7));
		OreDictionary.registerOre("ingotGraphite", new ItemStack(NCItems.ingot, 1, 8));
		OreDictionary.registerOre("graphite", new ItemStack(NCItems.ingot, 1, 8));
		
		OreDictionary.registerOre("ingotThoriumOxide", new ItemStack(NCItems.ingot_oxide, 1, 0));
		OreDictionary.registerOre("ingotUraniumOxide", new ItemStack(NCItems.ingot_oxide, 1, 1));
		OreDictionary.registerOre("ingotManganeseOxide", new ItemStack(NCItems.ingot_oxide, 1, 2));
		OreDictionary.registerOre("ingotManganeseDioxide", new ItemStack(NCItems.ingot_oxide, 1, 3));
		
		OreDictionary.registerOre("dustCopper", new ItemStack(NCItems.dust, 1, 0));
		OreDictionary.registerOre("dustTin", new ItemStack(NCItems.dust, 1, 1));
		OreDictionary.registerOre("dustLead", new ItemStack(NCItems.dust, 1, 2));
		OreDictionary.registerOre("dustThorium", new ItemStack(NCItems.dust, 1, 3));
		OreDictionary.registerOre("dustUranium", new ItemStack(NCItems.dust, 1, 4));
		OreDictionary.registerOre("dustBoron", new ItemStack(NCItems.dust, 1, 5));
		OreDictionary.registerOre("dustLithium", new ItemStack(NCItems.dust, 1, 6));
		OreDictionary.registerOre("dustMagnesium", new ItemStack(NCItems.dust, 1, 7));
		OreDictionary.registerOre("dustGraphite", new ItemStack(NCItems.dust, 1, 8));
		OreDictionary.registerOre("graphite", new ItemStack(NCItems.dust, 1, 8));
		
		OreDictionary.registerOre("dustThoriumOxide", new ItemStack(NCItems.dust_oxide, 1, 0));
		OreDictionary.registerOre("dustUraniumOxide", new ItemStack(NCItems.dust_oxide, 1, 1));
		OreDictionary.registerOre("dustManganeseOxide", new ItemStack(NCItems.dust_oxide, 1, 2));
		OreDictionary.registerOre("dustManganeseDioxide", new ItemStack(NCItems.dust_oxide, 1, 3));
		
		OreDictionary.registerOre("gemRhodochrosite", new ItemStack(NCItems.gem, 1, 0));
		
		OreDictionary.registerOre("dustDiamond", new ItemStack(NCItems.gem_dust, 1, 0));
		OreDictionary.registerOre("dustRhodochrosite", new ItemStack(NCItems.gem_dust, 1, 1));
		OreDictionary.registerOre("dustQuartz", new ItemStack(NCItems.gem_dust, 1, 2));
		OreDictionary.registerOre("dustObsidian", new ItemStack(NCItems.gem_dust, 1, 3));
		
		OreDictionary.registerOre("ingotBronze", new ItemStack(NCItems.alloy, 1, 0));
		OreDictionary.registerOre("ingotTough", new ItemStack(NCItems.alloy, 1, 1));
		OreDictionary.registerOre("ingotHardCarbon", new ItemStack(NCItems.alloy, 1, 2));
		OreDictionary.registerOre("ingotMagnesiumDiboride", new ItemStack(NCItems.alloy, 1, 3));
		OreDictionary.registerOre("ingotLithiumManganeseDioxide", new ItemStack(NCItems.alloy, 1, 4));
		OreDictionary.registerOre("ingotSteel", new ItemStack(NCItems.alloy, 1, 5));
		OreDictionary.registerOre("ingotFerroboron", new ItemStack(NCItems.alloy, 1, 6));
		
		OreDictionary.registerOre("plateBasic", new ItemStack(NCItems.part, 1, 0));
		OreDictionary.registerOre("plateAdvanced", new ItemStack(NCItems.part, 1, 1));
		OreDictionary.registerOre("plateDU", new ItemStack(NCItems.part, 1, 2));
		OreDictionary.registerOre("plateElite", new ItemStack(NCItems.part, 1, 3));
		OreDictionary.registerOre("solenoidCopper", new ItemStack(NCItems.part, 1, 4));
		OreDictionary.registerOre("solenoidMagnesiumDiboride", new ItemStack(NCItems.part, 1, 5));
		
		OreDictionary.registerOre("tinyDustLead", NCItems.tiny_dust_lead);
		
		OreDictionary.registerOre("ingotThorium230Base", new ItemStack(NCItems.thorium, 1, 0));
		OreDictionary.registerOre("ingotThorium230", new ItemStack(NCItems.thorium, 1, 0));
		OreDictionary.registerOre("ingotThorium230Oxide", new ItemStack(NCItems.thorium, 1, 1));
		OreDictionary.registerOre("ingotThorium230", new ItemStack(NCItems.thorium, 1, 1));
		OreDictionary.registerOre("tinyThorium230", new ItemStack(NCItems.thorium, 1, 2));
		OreDictionary.registerOre("tinyThorium230Oxide", new ItemStack(NCItems.thorium, 1, 3));
		OreDictionary.registerOre("ingotThorium232", new ItemStack(NCItems.thorium, 1, 4));
		OreDictionary.registerOre("ingotThorium232Oxide", new ItemStack(NCItems.thorium, 1, 5));
		OreDictionary.registerOre("tinyThorium232", new ItemStack(NCItems.thorium, 1, 6));
		OreDictionary.registerOre("tinyThorium232Oxide", new ItemStack(NCItems.thorium, 1, 7));
		
		OreDictionary.registerOre("ingotUranium233", new ItemStack(NCItems.uranium, 1, 0));
		OreDictionary.registerOre("ingotUranium233Oxide", new ItemStack(NCItems.uranium, 1, 1));
		OreDictionary.registerOre("tinyUranium233", new ItemStack(NCItems.uranium, 1, 2));
		OreDictionary.registerOre("tinyUranium233Oxide", new ItemStack(NCItems.uranium, 1, 3));
		OreDictionary.registerOre("ingotUranium235", new ItemStack(NCItems.uranium, 1, 4));
		OreDictionary.registerOre("ingotUranium235Oxide", new ItemStack(NCItems.uranium, 1, 5));
		OreDictionary.registerOre("tinyUranium235", new ItemStack(NCItems.uranium, 1, 6));
		OreDictionary.registerOre("tinyUranium235Oxide", new ItemStack(NCItems.uranium, 1, 7));
		OreDictionary.registerOre("ingotUranium238Base", new ItemStack(NCItems.uranium, 1, 8));
		OreDictionary.registerOre("ingotUranium238", new ItemStack(NCItems.uranium, 1, 8));
		OreDictionary.registerOre("ingotUranium238Oxide", new ItemStack(NCItems.uranium, 1, 9));
		OreDictionary.registerOre("ingotUranium238", new ItemStack(NCItems.uranium, 1, 9));
		OreDictionary.registerOre("tinyUranium238", new ItemStack(NCItems.uranium, 1, 10));
		OreDictionary.registerOre("tinyUranium238Oxide", new ItemStack(NCItems.uranium, 1, 11));
		
		OreDictionary.registerOre("ingotNeptunium236", new ItemStack(NCItems.neptunium, 1, 0));
		OreDictionary.registerOre("ingotNeptunium236Oxide", new ItemStack(NCItems.neptunium, 1, 1));
		OreDictionary.registerOre("tinyNeptunium236", new ItemStack(NCItems.neptunium, 1, 2));
		OreDictionary.registerOre("tinyNeptunium236Oxide", new ItemStack(NCItems.neptunium, 1, 3));
		OreDictionary.registerOre("ingotNeptunium237Base", new ItemStack(NCItems.neptunium, 1, 4));
		OreDictionary.registerOre("ingotNeptunium237", new ItemStack(NCItems.neptunium, 1, 4));
		OreDictionary.registerOre("ingotNeptunium237Oxide", new ItemStack(NCItems.neptunium, 1, 5));
		OreDictionary.registerOre("ingotNeptunium237", new ItemStack(NCItems.neptunium, 1, 5));
		OreDictionary.registerOre("tinyNeptunium237", new ItemStack(NCItems.neptunium, 1, 6));
		OreDictionary.registerOre("tinyNeptunium237Oxide", new ItemStack(NCItems.neptunium, 1, 7));
		
		OreDictionary.registerOre("ingotPlutonium238Base", new ItemStack(NCItems.plutonium, 1, 0));
		OreDictionary.registerOre("ingotPlutonium238", new ItemStack(NCItems.plutonium, 1, 0));
		OreDictionary.registerOre("ingotPlutonium238Oxide", new ItemStack(NCItems.plutonium, 1, 1));
		OreDictionary.registerOre("ingotPlutonium238", new ItemStack(NCItems.plutonium, 1, 1));
		OreDictionary.registerOre("tinyPlutonium238", new ItemStack(NCItems.plutonium, 1, 2));
		OreDictionary.registerOre("tinyPlutonium238Oxide", new ItemStack(NCItems.plutonium, 1, 3));
		OreDictionary.registerOre("ingotPlutonium239", new ItemStack(NCItems.plutonium, 1, 4));
		OreDictionary.registerOre("ingotPlutonium239Oxide", new ItemStack(NCItems.plutonium, 1, 5));
		OreDictionary.registerOre("tinyPlutonium239", new ItemStack(NCItems.plutonium, 1, 6));
		OreDictionary.registerOre("tinyPlutonium239Oxide", new ItemStack(NCItems.plutonium, 1, 7));
		OreDictionary.registerOre("ingotPlutonium241", new ItemStack(NCItems.plutonium, 1, 8));
		OreDictionary.registerOre("ingotPlutonium241Oxide", new ItemStack(NCItems.plutonium, 1, 9));
		OreDictionary.registerOre("tinyPlutonium241", new ItemStack(NCItems.plutonium, 1, 10));
		OreDictionary.registerOre("tinyPlutonium241Oxide", new ItemStack(NCItems.plutonium, 1, 11));
		OreDictionary.registerOre("ingotPlutonium242Base", new ItemStack(NCItems.plutonium, 1, 12));
		OreDictionary.registerOre("ingotPlutonium242", new ItemStack(NCItems.plutonium, 1, 12));
		OreDictionary.registerOre("ingotPlutonium242Oxide", new ItemStack(NCItems.plutonium, 1, 13));
		OreDictionary.registerOre("ingotPlutonium242", new ItemStack(NCItems.plutonium, 1, 13));
		OreDictionary.registerOre("tinyPlutonium242", new ItemStack(NCItems.plutonium, 1, 14));
		OreDictionary.registerOre("tinyPlutonium242Oxide", new ItemStack(NCItems.plutonium, 1, 15));
		
		OreDictionary.registerOre("ingotAmericium241Base", new ItemStack(NCItems.americium, 1, 0));
		OreDictionary.registerOre("ingotAmericium241", new ItemStack(NCItems.americium, 1, 0));
		OreDictionary.registerOre("ingotAmericium241Oxide", new ItemStack(NCItems.americium, 1, 1));
		OreDictionary.registerOre("ingotAmericium241", new ItemStack(NCItems.americium, 1, 1));
		OreDictionary.registerOre("tinyAmericium241", new ItemStack(NCItems.americium, 1, 2));
		OreDictionary.registerOre("tinyAmericium241Oxide", new ItemStack(NCItems.americium, 1, 3));
		OreDictionary.registerOre("ingotAmericium242", new ItemStack(NCItems.americium, 1, 4));
		OreDictionary.registerOre("ingotAmericium242Oxide", new ItemStack(NCItems.americium, 1, 5));
		OreDictionary.registerOre("tinyAmericium242", new ItemStack(NCItems.americium, 1, 6));
		OreDictionary.registerOre("tinyAmericium242Oxide", new ItemStack(NCItems.americium, 1, 7));
		OreDictionary.registerOre("ingotAmericium243Base", new ItemStack(NCItems.americium, 1, 8));
		OreDictionary.registerOre("ingotAmericium243", new ItemStack(NCItems.americium, 1, 8));
		OreDictionary.registerOre("ingotAmericium243Oxide", new ItemStack(NCItems.americium, 1, 9));
		OreDictionary.registerOre("ingotAmericium243", new ItemStack(NCItems.americium, 1, 9));
		OreDictionary.registerOre("tinyAmericium243", new ItemStack(NCItems.americium, 1, 10));
		OreDictionary.registerOre("tinyAmericium243Oxide", new ItemStack(NCItems.americium, 1, 11));
		
		OreDictionary.registerOre("ingotCurium243", new ItemStack(NCItems.curium, 1, 0));
		OreDictionary.registerOre("ingotCurium243Oxide", new ItemStack(NCItems.curium, 1, 1));
		OreDictionary.registerOre("tinyCurium243", new ItemStack(NCItems.curium, 1, 2));
		OreDictionary.registerOre("tinyCurium243Oxide", new ItemStack(NCItems.curium, 1, 3));
		OreDictionary.registerOre("ingotCurium245", new ItemStack(NCItems.curium, 1, 4));
		OreDictionary.registerOre("ingotCurium245Oxide", new ItemStack(NCItems.curium, 1, 5));
		OreDictionary.registerOre("tinyCurium245", new ItemStack(NCItems.curium, 1, 6));
		OreDictionary.registerOre("tinyCurium245Oxide", new ItemStack(NCItems.curium, 1, 7));
		OreDictionary.registerOre("ingotCurium246Base", new ItemStack(NCItems.curium, 1, 8));
		OreDictionary.registerOre("ingotCurium246", new ItemStack(NCItems.curium, 1, 8));
		OreDictionary.registerOre("ingotCurium246Oxide", new ItemStack(NCItems.curium, 1, 9));
		OreDictionary.registerOre("ingotCurium246", new ItemStack(NCItems.curium, 1, 9));
		OreDictionary.registerOre("tinyCurium246", new ItemStack(NCItems.curium, 1, 10));
		OreDictionary.registerOre("tinyCurium246Oxide", new ItemStack(NCItems.curium, 1, 11));
		OreDictionary.registerOre("ingotCurium247", new ItemStack(NCItems.curium, 1, 12));
		OreDictionary.registerOre("ingotCurium247Oxide", new ItemStack(NCItems.curium, 1, 13));
		OreDictionary.registerOre("tinyCurium247", new ItemStack(NCItems.curium, 1, 14));
		OreDictionary.registerOre("tinyCurium247Oxide", new ItemStack(NCItems.curium, 1, 15));
		
		OreDictionary.registerOre("ingotBerkelium247Base", new ItemStack(NCItems.berkelium, 1, 0));
		OreDictionary.registerOre("ingotBerkelium247", new ItemStack(NCItems.berkelium, 1, 0));
		OreDictionary.registerOre("ingotBerkelium247Oxide", new ItemStack(NCItems.berkelium, 1, 1));
		OreDictionary.registerOre("ingotBerkelium247", new ItemStack(NCItems.berkelium, 1, 1));
		OreDictionary.registerOre("tinyBerkelium247", new ItemStack(NCItems.berkelium, 1, 2));
		OreDictionary.registerOre("tinyBerkelium247Oxide", new ItemStack(NCItems.berkelium, 1, 3));
		OreDictionary.registerOre("ingotBerkelium248", new ItemStack(NCItems.berkelium, 1, 4));
		OreDictionary.registerOre("ingotBerkelium248Oxide", new ItemStack(NCItems.berkelium, 1, 5));
		OreDictionary.registerOre("tinyBerkelium248", new ItemStack(NCItems.berkelium, 1, 6));
		OreDictionary.registerOre("tinyBerkelium248Oxide", new ItemStack(NCItems.berkelium, 1, 7));
		
		OreDictionary.registerOre("ingotCalifornium249", new ItemStack(NCItems.californium, 1, 0));
		OreDictionary.registerOre("ingotCalifornium249Oxide", new ItemStack(NCItems.californium, 1, 1));
		OreDictionary.registerOre("tinyCalifornium249", new ItemStack(NCItems.californium, 1, 2));
		OreDictionary.registerOre("tinyCalifornium249Oxide", new ItemStack(NCItems.californium, 1, 3));
		OreDictionary.registerOre("ingotCalifornium250Base", new ItemStack(NCItems.californium, 1, 4));
		OreDictionary.registerOre("ingotCalifornium250", new ItemStack(NCItems.californium, 1, 4));
		OreDictionary.registerOre("ingotCalifornium250Oxide", new ItemStack(NCItems.californium, 1, 5));
		OreDictionary.registerOre("ingotCalifornium250", new ItemStack(NCItems.californium, 1, 5));
		OreDictionary.registerOre("tinyCalifornium250", new ItemStack(NCItems.californium, 1, 6));
		OreDictionary.registerOre("tinyCalifornium250Oxide", new ItemStack(NCItems.californium, 1, 7));
		OreDictionary.registerOre("ingotCalifornium251", new ItemStack(NCItems.californium, 1, 8));
		OreDictionary.registerOre("ingotCalifornium251Oxide", new ItemStack(NCItems.californium, 1, 9));
		OreDictionary.registerOre("tinyCalifornium251", new ItemStack(NCItems.californium, 1, 10));
		OreDictionary.registerOre("tinyCalifornium251Oxide", new ItemStack(NCItems.californium, 1, 11));
		OreDictionary.registerOre("ingotCalifornium252Base", new ItemStack(NCItems.californium, 1, 12));
		OreDictionary.registerOre("ingotCalifornium252", new ItemStack(NCItems.californium, 1, 12));
		OreDictionary.registerOre("ingotCalifornium252Oxide", new ItemStack(NCItems.californium, 1, 13));
		OreDictionary.registerOre("ingotCalifornium252", new ItemStack(NCItems.californium, 1, 13));
		OreDictionary.registerOre("tinyCalifornium252", new ItemStack(NCItems.californium, 1, 14));
		OreDictionary.registerOre("tinyCalifornium252Oxide", new ItemStack(NCItems.californium, 1, 15));
		
		OreDictionary.registerOre("fuelTBU", new ItemStack(NCItems.fuel_thorium, 1, 0));
		OreDictionary.registerOre("fuelTBUOxide", new ItemStack(NCItems.fuel_thorium, 1, 1));
		
		OreDictionary.registerOre("fuelLEU233", new ItemStack(NCItems.fuel_uranium, 1, 0));
		OreDictionary.registerOre("fuelLEU233Oxide", new ItemStack(NCItems.fuel_uranium, 1, 1));
		OreDictionary.registerOre("fuelHEU233", new ItemStack(NCItems.fuel_uranium, 1, 2));
		OreDictionary.registerOre("fuelHEU233Oxide", new ItemStack(NCItems.fuel_uranium, 1, 3));
		OreDictionary.registerOre("fuelLEU235", new ItemStack(NCItems.fuel_uranium, 1, 4));
		OreDictionary.registerOre("fuelLEU235Oxide", new ItemStack(NCItems.fuel_uranium, 1, 5));
		OreDictionary.registerOre("fuelHEU235", new ItemStack(NCItems.fuel_uranium, 1, 6));
		OreDictionary.registerOre("fuelHEU235Oxide", new ItemStack(NCItems.fuel_uranium, 1, 7));
		
		OreDictionary.registerOre("fuelLEN236", new ItemStack(NCItems.fuel_neptunium, 1, 0));
		OreDictionary.registerOre("fuelLEN236Oxide", new ItemStack(NCItems.fuel_neptunium, 1, 1));
		OreDictionary.registerOre("fuelHEN236", new ItemStack(NCItems.fuel_neptunium, 1, 2));
		OreDictionary.registerOre("fuelHEN236Oxide", new ItemStack(NCItems.fuel_neptunium, 1, 3));
		
		OreDictionary.registerOre("fuelLEP239", new ItemStack(NCItems.fuel_plutonium, 1, 0));
		OreDictionary.registerOre("fuelLEP239Oxide", new ItemStack(NCItems.fuel_plutonium, 1, 1));
		OreDictionary.registerOre("fuelHEP239", new ItemStack(NCItems.fuel_plutonium, 1, 2));
		OreDictionary.registerOre("fuelHEP239Oxide", new ItemStack(NCItems.fuel_plutonium, 1, 3));
		OreDictionary.registerOre("fuelLEP241", new ItemStack(NCItems.fuel_plutonium, 1, 4));
		OreDictionary.registerOre("fuelLEP241Oxide", new ItemStack(NCItems.fuel_plutonium, 1, 5));
		OreDictionary.registerOre("fuelHEP241", new ItemStack(NCItems.fuel_plutonium, 1, 6));
		OreDictionary.registerOre("fuelHEP241Oxide", new ItemStack(NCItems.fuel_plutonium, 1, 7));
		
		OreDictionary.registerOre("fuelMOX239", new ItemStack(NCItems.fuel_mixed_oxide, 1, 0));
		OreDictionary.registerOre("fuelMOX241", new ItemStack(NCItems.fuel_mixed_oxide, 1, 1));
		
		OreDictionary.registerOre("fuelLEA242", new ItemStack(NCItems.fuel_americium, 1, 0));
		OreDictionary.registerOre("fuelLEA242Oxide", new ItemStack(NCItems.fuel_americium, 1, 1));
		OreDictionary.registerOre("fuelHEA242", new ItemStack(NCItems.fuel_americium, 1, 2));
		OreDictionary.registerOre("fuelHEA242Oxide", new ItemStack(NCItems.fuel_americium, 1, 3));
		
		OreDictionary.registerOre("fuelLECm243", new ItemStack(NCItems.fuel_curium, 1, 0));
		OreDictionary.registerOre("fuelLECm243Oxide", new ItemStack(NCItems.fuel_curium, 1, 1));
		OreDictionary.registerOre("fuelHECm243", new ItemStack(NCItems.fuel_curium, 1, 2));
		OreDictionary.registerOre("fuelHECm243Oxide", new ItemStack(NCItems.fuel_curium, 1, 3));
		OreDictionary.registerOre("fuelLECm245", new ItemStack(NCItems.fuel_curium, 1, 4));
		OreDictionary.registerOre("fuelLECm245Oxide", new ItemStack(NCItems.fuel_curium, 1, 5));
		OreDictionary.registerOre("fuelHECm245", new ItemStack(NCItems.fuel_curium, 1, 6));
		OreDictionary.registerOre("fuelHECm245Oxide", new ItemStack(NCItems.fuel_curium, 1, 7));
		OreDictionary.registerOre("fuelLECm247", new ItemStack(NCItems.fuel_curium, 1, 8));
		OreDictionary.registerOre("fuelLECm247Oxide", new ItemStack(NCItems.fuel_curium, 1, 9));
		OreDictionary.registerOre("fuelHECm247", new ItemStack(NCItems.fuel_curium, 1, 10));
		OreDictionary.registerOre("fuelHECm247Oxide", new ItemStack(NCItems.fuel_curium, 1, 11));
		
		OreDictionary.registerOre("fuelLEB248", new ItemStack(NCItems.fuel_berkelium, 1, 0));
		OreDictionary.registerOre("fuelLEB248Oxide", new ItemStack(NCItems.fuel_berkelium, 1, 1));
		OreDictionary.registerOre("fuelHEB248", new ItemStack(NCItems.fuel_berkelium, 1, 2));
		OreDictionary.registerOre("fuelHEB248Oxide", new ItemStack(NCItems.fuel_berkelium, 1, 3));
		
		OreDictionary.registerOre("fuelLECf249", new ItemStack(NCItems.fuel_californium, 1, 0));
		OreDictionary.registerOre("fuelLECf249Oxide", new ItemStack(NCItems.fuel_californium, 1, 1));
		OreDictionary.registerOre("fuelHECf249", new ItemStack(NCItems.fuel_californium, 1, 2));
		OreDictionary.registerOre("fuelHECf249Oxide", new ItemStack(NCItems.fuel_californium, 1, 3));
		OreDictionary.registerOre("fuelLECf251", new ItemStack(NCItems.fuel_californium, 1, 4));
		OreDictionary.registerOre("fuelLECf251Oxide", new ItemStack(NCItems.fuel_californium, 1, 5));
		OreDictionary.registerOre("fuelHECf251", new ItemStack(NCItems.fuel_californium, 1, 6));
		OreDictionary.registerOre("fuelHECf251Oxide", new ItemStack(NCItems.fuel_californium, 1, 7));
		
		OreDictionary.registerOre("fuelRodTBU", new ItemStack(NCItems.fuel_rod_thorium, 1, 0));
		OreDictionary.registerOre("fuelRodTBUOxide", new ItemStack(NCItems.fuel_rod_thorium, 1, 1));
		
		OreDictionary.registerOre("fuelRodLEU233", new ItemStack(NCItems.fuel_rod_uranium, 1, 0));
		OreDictionary.registerOre("fuelRodLEU233Oxide", new ItemStack(NCItems.fuel_rod_uranium, 1, 1));
		OreDictionary.registerOre("fuelRodHEU233", new ItemStack(NCItems.fuel_rod_uranium, 1, 2));
		OreDictionary.registerOre("fuelRodHEU233Oxide", new ItemStack(NCItems.fuel_rod_uranium, 1, 3));
		OreDictionary.registerOre("fuelRodLEU235", new ItemStack(NCItems.fuel_rod_uranium, 1, 4));
		OreDictionary.registerOre("fuelRodLEU235Oxide", new ItemStack(NCItems.fuel_rod_uranium, 1, 5));
		OreDictionary.registerOre("fuelRodHEU235", new ItemStack(NCItems.fuel_rod_uranium, 1, 6));
		OreDictionary.registerOre("fuelRodHEU235Oxide", new ItemStack(NCItems.fuel_rod_uranium, 1, 7));
		
		OreDictionary.registerOre("fuelRodLEN236", new ItemStack(NCItems.fuel_rod_neptunium, 1, 0));
		OreDictionary.registerOre("fuelRodLEN236Oxide", new ItemStack(NCItems.fuel_rod_neptunium, 1, 1));
		OreDictionary.registerOre("fuelRodHEN236", new ItemStack(NCItems.fuel_rod_neptunium, 1, 2));
		OreDictionary.registerOre("fuelRodHEN236Oxide", new ItemStack(NCItems.fuel_rod_neptunium, 1, 3));
		
		OreDictionary.registerOre("fuelRodLEP239", new ItemStack(NCItems.fuel_rod_plutonium, 1, 0));
		OreDictionary.registerOre("fuelRodLEP239Oxide", new ItemStack(NCItems.fuel_rod_plutonium, 1, 1));
		OreDictionary.registerOre("fuelRodHEP239", new ItemStack(NCItems.fuel_rod_plutonium, 1, 2));
		OreDictionary.registerOre("fuelRodHEP239Oxide", new ItemStack(NCItems.fuel_rod_plutonium, 1, 3));
		OreDictionary.registerOre("fuelRodLEP241", new ItemStack(NCItems.fuel_rod_plutonium, 1, 4));
		OreDictionary.registerOre("fuelRodLEP241Oxide", new ItemStack(NCItems.fuel_rod_plutonium, 1, 5));
		OreDictionary.registerOre("fuelRodHEP241", new ItemStack(NCItems.fuel_rod_plutonium, 1, 6));
		OreDictionary.registerOre("fuelRodHEP241Oxide", new ItemStack(NCItems.fuel_rod_plutonium, 1, 7));
		
		OreDictionary.registerOre("fuelRodMOX239", new ItemStack(NCItems.fuel_rod_mixed_oxide, 1, 0));
		OreDictionary.registerOre("fuelRodMOX241", new ItemStack(NCItems.fuel_rod_mixed_oxide, 1, 1));
		
		OreDictionary.registerOre("fuelRodLEA242", new ItemStack(NCItems.fuel_rod_americium, 1, 0));
		OreDictionary.registerOre("fuelRodLEA242Oxide", new ItemStack(NCItems.fuel_rod_americium, 1, 1));
		OreDictionary.registerOre("fuelRodHEA242", new ItemStack(NCItems.fuel_rod_americium, 1, 2));
		OreDictionary.registerOre("fuelRodHEA242Oxide", new ItemStack(NCItems.fuel_rod_americium, 1, 3));
		
		OreDictionary.registerOre("fuelRodLECm243", new ItemStack(NCItems.fuel_rod_curium, 1, 0));
		OreDictionary.registerOre("fuelRodLECm243Oxide", new ItemStack(NCItems.fuel_rod_curium, 1, 1));
		OreDictionary.registerOre("fuelRodHECm243", new ItemStack(NCItems.fuel_rod_curium, 1, 2));
		OreDictionary.registerOre("fuelRodHECm243Oxide", new ItemStack(NCItems.fuel_rod_curium, 1, 3));
		OreDictionary.registerOre("fuelRodLECm245", new ItemStack(NCItems.fuel_rod_curium, 1, 4));
		OreDictionary.registerOre("fuelRodLECm245Oxide", new ItemStack(NCItems.fuel_rod_curium, 1, 5));
		OreDictionary.registerOre("fuelRodHECm245", new ItemStack(NCItems.fuel_rod_curium, 1, 6));
		OreDictionary.registerOre("fuelRodHECm245Oxide", new ItemStack(NCItems.fuel_rod_curium, 1, 7));
		OreDictionary.registerOre("fuelRodLECm247", new ItemStack(NCItems.fuel_rod_curium, 1, 8));
		OreDictionary.registerOre("fuelRodLECm247Oxide", new ItemStack(NCItems.fuel_rod_curium, 1, 9));
		OreDictionary.registerOre("fuelRodHECm247", new ItemStack(NCItems.fuel_rod_curium, 1, 10));
		OreDictionary.registerOre("fuelRodHECm247Oxide", new ItemStack(NCItems.fuel_rod_curium, 1, 11));
		
		OreDictionary.registerOre("fuelRodLEB248", new ItemStack(NCItems.fuel_rod_berkelium, 1, 0));
		OreDictionary.registerOre("fuelRodLEB248Oxide", new ItemStack(NCItems.fuel_rod_berkelium, 1, 1));
		OreDictionary.registerOre("fuelRodHEB248", new ItemStack(NCItems.fuel_rod_berkelium, 1, 2));
		OreDictionary.registerOre("fuelRodHEB248Oxide", new ItemStack(NCItems.fuel_rod_berkelium, 1, 3));
		
		OreDictionary.registerOre("fuelRodLECf249", new ItemStack(NCItems.fuel_rod_californium, 1, 0));
		OreDictionary.registerOre("fuelRodLECf249Oxide", new ItemStack(NCItems.fuel_rod_californium, 1, 1));
		OreDictionary.registerOre("fuelRodHECf249", new ItemStack(NCItems.fuel_rod_californium, 1, 2));
		OreDictionary.registerOre("fuelRodHECf249Oxide", new ItemStack(NCItems.fuel_rod_californium, 1, 3));
		OreDictionary.registerOre("fuelRodLECf251", new ItemStack(NCItems.fuel_rod_californium, 1, 4));
		OreDictionary.registerOre("fuelRodLECf251Oxide", new ItemStack(NCItems.fuel_rod_californium, 1, 5));
		OreDictionary.registerOre("fuelRodHECf251", new ItemStack(NCItems.fuel_rod_californium, 1, 6));
		OreDictionary.registerOre("fuelRodHECf251Oxide", new ItemStack(NCItems.fuel_rod_californium, 1, 7));
		
		OreDictionary.registerOre("depletedFuelRodTBU", new ItemStack(NCItems.depleted_fuel_rod_thorium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodTBUOxide", new ItemStack(NCItems.depleted_fuel_rod_thorium, 1, 1));
		
		OreDictionary.registerOre("depletedFuelRodLEU233", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLEU233Oxide", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHEU233", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHEU233Oxide", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 3));
		OreDictionary.registerOre("depletedFuelRodLEU235", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 4));
		OreDictionary.registerOre("depletedFuelRodLEU235Oxide", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 5));
		OreDictionary.registerOre("depletedFuelRodHEU235", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 6));
		OreDictionary.registerOre("depletedFuelRodHEU235Oxide", new ItemStack(NCItems.depleted_fuel_rod_uranium, 1, 7));
		
		OreDictionary.registerOre("depletedFuelRodLEN236", new ItemStack(NCItems.depleted_fuel_rod_neptunium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLEN236Oxide", new ItemStack(NCItems.depleted_fuel_rod_neptunium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHEN236", new ItemStack(NCItems.depleted_fuel_rod_neptunium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHEN236Oxide", new ItemStack(NCItems.depleted_fuel_rod_neptunium, 1, 3));
		
		OreDictionary.registerOre("depletedFuelRodLEP239", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLEP239Oxide", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHEP239", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHEP239Oxide", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 3));
		OreDictionary.registerOre("depletedFuelRodLEP241", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 4));
		OreDictionary.registerOre("depletedFuelRodLEP241Oxide", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 5));
		OreDictionary.registerOre("depletedFuelRodHEP241", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 6));
		OreDictionary.registerOre("depletedFuelRodHEP241Oxide", new ItemStack(NCItems.depleted_fuel_rod_plutonium, 1, 7));
		
		OreDictionary.registerOre("depletedFuelRodMOX239", new ItemStack(NCItems.depleted_fuel_rod_mixed_oxide, 1, 0));
		OreDictionary.registerOre("depletedFuelRodMOX241", new ItemStack(NCItems.depleted_fuel_rod_mixed_oxide, 1, 1));
		
		OreDictionary.registerOre("depletedFuelRodLEA242", new ItemStack(NCItems.depleted_fuel_rod_americium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLEA242Oxide", new ItemStack(NCItems.depleted_fuel_rod_americium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHEA242", new ItemStack(NCItems.depleted_fuel_rod_americium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHEA242Oxide", new ItemStack(NCItems.depleted_fuel_rod_americium, 1, 3));
		
		OreDictionary.registerOre("depletedFuelRodLECm243", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLECm243Oxide", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHECm243", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHECm243Oxide", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 3));
		OreDictionary.registerOre("depletedFuelRodLECm245", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 4));
		OreDictionary.registerOre("depletedFuelRodLECm245Oxide", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 5));
		OreDictionary.registerOre("depletedFuelRodHECm245", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 6));
		OreDictionary.registerOre("depletedFuelRodHECm245Oxide", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 7));
		OreDictionary.registerOre("depletedFuelRodLECm247", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 8));
		OreDictionary.registerOre("depletedFuelRodLECm247Oxide", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 9));
		OreDictionary.registerOre("depletedFuelRodHECm247", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 10));
		OreDictionary.registerOre("depletedFuelRodHECm247Oxide", new ItemStack(NCItems.depleted_fuel_rod_curium, 1, 11));
		
		OreDictionary.registerOre("depletedFuelRodLEB248", new ItemStack(NCItems.depleted_fuel_rod_berkelium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLEB248Oxide", new ItemStack(NCItems.depleted_fuel_rod_berkelium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHEB248", new ItemStack(NCItems.depleted_fuel_rod_berkelium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHEB248Oxide", new ItemStack(NCItems.depleted_fuel_rod_berkelium, 1, 3));
		
		OreDictionary.registerOre("depletedFuelRodLECf249", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 0));
		OreDictionary.registerOre("depletedFuelRodLECf249Oxide", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 1));
		OreDictionary.registerOre("depletedFuelRodHECf249", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 2));
		OreDictionary.registerOre("depletedFuelRodHECf249Oxide", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 3));
		OreDictionary.registerOre("depletedFuelRodLECf251", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 4));
		OreDictionary.registerOre("depletedFuelRodLECf251Oxide", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 5));
		OreDictionary.registerOre("depletedFuelRodHECf251", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 6));
		OreDictionary.registerOre("depletedFuelRodHECf251Oxide", new ItemStack(NCItems.depleted_fuel_rod_californium, 1, 7));
		
		OreDictionary.registerOre("gemCoal", Items.COAL);
		OreDictionary.registerOre("blockObsidian", Blocks.OBSIDIAN);
		OreDictionary.registerOre("blockGlowstone", Blocks.GLOWSTONE);
	}

}
