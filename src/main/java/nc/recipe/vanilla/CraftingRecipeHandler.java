package nc.recipe.vanilla;

import static nc.config.NCConfig.ore_dict_raw_material_recipes;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import nc.Global;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.enumm.MetaEnums.IngotType;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.init.NCTools;
import nc.multiblock.qComputer.QuantumComputerGateEnums;
import nc.radiation.RadArmor;
import nc.recipe.vanilla.ingredient.BucketIngredient;
import nc.recipe.vanilla.recipe.ShapedEnergyRecipe;
import nc.recipe.vanilla.recipe.ShapedFluidRecipe;
import nc.recipe.vanilla.recipe.ShapelessArmorRadShieldingRecipe;
import nc.recipe.vanilla.recipe.ShapelessFluidRecipe;
import nc.util.ArmorHelper;
import nc.util.ItemStackHelper;
import nc.util.NCUtil;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
import nc.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CraftingRecipeHandler {
	
	public static void registerCraftingRecipes() {
		for (int i = 0; i < IngotType.values().length; i++) {
			String type = StringHelper.capitalize(IngotType.values()[i].getName());
			if (!ore_dict_raw_material_recipes) {
				blockCompress(NCBlocks.ingot_block, i, "block" + type, new ItemStack(NCItems.ingot, 1, i));
			}
			else for (ItemStack ingot : OreDictionary.getOres("ingot" + type)) {
				blockCompress(NCBlocks.ingot_block, i, "block" + type, ingot);
			}
			
			if (!ore_dict_raw_material_recipes) {
				blockOpen(NCItems.ingot, i, "ingot" + type, new ItemStack(NCBlocks.ingot_block, 1, i));
			}
			else for (ItemStack block : OreDictionary.getOres("block" + type)) {
				blockOpen(NCItems.ingot, i, "ingot" + type, block);
			}
		}
		
		blockCompress(NCBlocks.fertile_isotope, 0, "blockUranium238", "ingotUranium238");
		blockCompress(NCBlocks.fertile_isotope, 1, "blockNeptunium237", "ingotNeptunium237");
		blockCompress(NCBlocks.fertile_isotope, 2, "blockPlutonium242", "ingotPlutonium242");
		blockCompress(NCBlocks.fertile_isotope, 3, "blockAmericium243", "ingotAmericium243");
		blockCompress(NCBlocks.fertile_isotope, 4, "blockCurium246", "ingotCurium246");
		blockCompress(NCBlocks.fertile_isotope, 5, "blockBerkelium247", "ingotBerkelium247");
		blockCompress(NCBlocks.fertile_isotope, 6, "blockCalifornium252", "ingotCalifornium252");
		
		blockOpen(NCItems.uranium, 10, "ingotUranium238", "blockUranium238");
		blockOpen(NCItems.neptunium, 5, "ingotNeptunium237", "blockNeptunium237");
		blockOpen(NCItems.plutonium, 15, "ingotPlutonium242", "blockPlutonium242");
		blockOpen(NCItems.americium, 10, "ingotAmericium243", "blockAmericium243");
		blockOpen(NCItems.curium, 10, "ingotCurium246", "blockCurium246");
		blockOpen(NCItems.berkelium, 0, "ingotBerkelium247", "blockBerkelium247");
		blockOpen(NCItems.californium, 15, "ingotCalifornium252", "blockCalifornium252");
		
		if (NCConfig.register_processor[0]) addShapedOreRecipe(NCBlocks.nuclear_furnace, new Object[] {"PTP", "TFT", "PTP", 'T', "ingotTough", 'P', "plateBasic", 'F', Blocks.FURNACE});
		if (NCConfig.register_processor[1]) addShapedOreRecipe(NCBlocks.manufactory, new Object[] {"LRL", "FPF", "LSL", 'P', Blocks.PISTON, 'L', "ingotLead", 'S', "solenoidCopper", 'R', "dustRedstone", 'F', Items.FLINT});
		if (NCConfig.register_processor[2]) addShapedOreRecipe(NCBlocks.alloy_furnace, new Object[] {"PRP", "BFB", "PSP", 'F', Blocks.FURNACE, 'P', "plateBasic", 'S', "solenoidCopper", 'R', "dustRedstone", 'B', Items.BRICK});
		if (NCConfig.register_processor[3]) addShapedOreRecipe(NCBlocks.decay_hastener, new Object[] {"PGP", "ECE", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'S', "solenoidCopper", 'G', "dustGlowstone", 'E', Items.ENDER_PEARL});
		if (NCConfig.register_processor[4]) addShapedOreRecipe(NCBlocks.fuel_reprocessor, new Object[] {"PBP", "TCT", "PAP", 'C', "chassis", 'P', "plateBasic", 'A', "actuator", 'T', "ingotTough", 'B', "ingotBoron"});
		if (NCConfig.register_processor[5]) addShapedOreRecipe(NCBlocks.separator, new Object[] {"PMP", "RCR", "PMP", 'C', "chassis", 'P', "plateBasic", 'M', "motor", 'R', "dustRedstone"});
		if (NCConfig.register_processor[6]) addShapedOreRecipe(NCBlocks.pressurizer, new Object[] {"PTP", "ACA", "PTP", 'C', "chassis", 'P', "plateAdvanced", 'T', "ingotTough", 'A', "actuator"});
		if (NCConfig.register_processor[7]) addShapedOreRecipe(NCBlocks.salt_mixer, new Object[] {"PSP", "BCB", "PMP", 'C', "chassis", 'P', "plateBasic", 'B', Items.BUCKET, 'M', "motor", 'S', "ingotSteel"});
		if (NCConfig.register_processor[8]) addShapedOreRecipe(NCBlocks.enricher, new Object[] {"PHP", "LCL", "PMP", 'C', "chassis", 'P', "plateAdvanced", 'L', "gemLapis", 'M', "motor", 'H', Blocks.HOPPER});
		if (NCConfig.register_processor[9]) addShapedOreRecipe(NCBlocks.chemical_reactor, new Object[] {"PMP", "GCG", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'G', "dustGlowstone", 'M', "motor", 'S', "servo"});
		if (NCConfig.register_processor[10]) addShapedOreRecipe(NCBlocks.electrolyzer, new Object[] {"PGP", "SCS", "PMP", 'C', "chassis", 'P', "plateAdvanced", 'S', "solenoidCopper", 'G', "ingotGraphite", 'M', "motor"});
		if (NCConfig.register_processor[11]) addShapedOreRecipe(NCBlocks.assembler, new Object[] {"PHP", "ACA", "PMP", 'C', "chassis", 'P', "plateBasic", 'H', "ingotHardCarbon", 'A', "actuator", 'M', "motor"});
		if (NCConfig.register_processor[12]) addShapedOreRecipe(NCBlocks.supercooler, new Object[] {"PDP", "HCH", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'D', "ingotMagnesiumDiboride", 'H', "ingotHardCarbon", 'S', "servo"});
		if (NCConfig.register_processor[13]) addShapedOreRecipe(NCBlocks.ingot_former, new Object[] {"PHP", "FCF", "PTP", 'C', "chassis", 'P', "plateBasic", 'F', "ingotFerroboron", 'T', "ingotTough", 'H', Blocks.HOPPER});
		if (NCConfig.register_processor[14]) addShapedOreRecipe(NCBlocks.melter, new Object[] {"PNP", "NCN", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'N', "ingotBrickNether", 'S', "servo"});
		if (NCConfig.register_processor[15]) addShapedOreRecipe(NCBlocks.crystallizer, new Object[] {"PSP", "SCS", "PUP", 'C', "chassis", 'P', "plateAdvanced", 'S', "solenoidCopper", 'U', Items.CAULDRON});
		if (NCConfig.register_processor[16]) addShapedOreRecipe(NCBlocks.infuser, new Object[] {"PBP", "GCG", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'G', "ingotGold", 'S', "servo", 'B', Items.BUCKET});
		if (NCConfig.register_processor[17]) addShapedOreRecipe(NCBlocks.extractor, new Object[] {"PMP", "BCB", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'M', "ingotMagnesium", 'S', "servo", 'B', Items.BUCKET});
		if (NCConfig.register_processor[18]) addShapedOreRecipe(NCBlocks.centrifuge, new Object[] {"PFP", "MCM", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'M', "motor", 'F', "ingotFerroboron", 'S', "servo"});
		if (NCConfig.register_processor[19]) addShapedOreRecipe(NCBlocks.rock_crusher, new Object[] {"PMP", "ACA", "PTP", 'C', "chassis", 'P', "plateAdvanced", 'A', "actuator", 'T', "ingotTough", 'M', "motor"});
		
		addShapedOreRecipe(NCBlocks.machine_interface, new Object[] {" A ", "MCM", " S ", 'C', "chassis", 'A', "actuator", 'M', "motor", 'S', "servo"});
		
		addShapedOreRecipe(NCBlocks.rtg_uranium, new Object[] {"PGP", "GUG", "PGP", 'G', "ingotGraphite", 'P', "plateBasic", 'U', "blockUranium238"});
		addShapedOreRecipe(NCBlocks.rtg_plutonium, new Object[] {"PGP", "GUG", "PGP", 'G', "ingotGraphite", 'P', "plateAdvanced", 'U', "ingotPlutonium238All"});
		addShapedOreRecipe(NCBlocks.rtg_americium, new Object[] {"PGP", "GAG", "PGP", 'G', "ingotGraphite", 'P', "plateAdvanced", 'A', "ingotAmericium241All"});
		addShapedOreRecipe(NCBlocks.rtg_californium, new Object[] {"PGP", "GCG", "PGP", 'G', "ingotGraphite", 'P', "plateAdvanced", 'C', "ingotCalifornium250All"});
		
		addShapedOreRecipe(NCBlocks.solar_panel_basic, new Object[] {"GQG", "PLP", "CPC", 'G', "dustGraphite", 'Q', "dustQuartz", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'L', "gemLapis", 'C', "solenoidCopper"});
		addShapedOreRecipe(NCBlocks.solar_panel_basic, new Object[] {"GQG", "PLP", "CPC", 'G', "dustGraphite", 'Q', "dustNetherQuartz", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'L', "gemLapis", 'C', "solenoidCopper"});
		addShapedOreRecipe(NCBlocks.solar_panel_advanced, new Object[] {"PGP", "SSS", "PCP", 'S', NCBlocks.solar_panel_basic, 'G', "dustGraphite", 'P', "plateAdvanced", 'C', "solenoidCopper"});
		addShapedOreRecipe(NCBlocks.solar_panel_du, new Object[] {"PGP", "SSS", "PMP", 'S', NCBlocks.solar_panel_advanced, 'G', "dustGraphite", 'P', "plateDU", 'M', "solenoidMagnesiumDiboride"});
		addShapedOreRecipe(NCBlocks.solar_panel_elite, new Object[] {"PBP", "SSS", "PMP", 'S', NCBlocks.solar_panel_du, 'B', "gemBoronArsenide", 'P', "plateElite", 'M', "solenoidMagnesiumDiboride"});
		
		addShapedOreRecipe(NCBlocks.decay_generator, new Object[] {"LCL", "CRC", "LCL", 'C', "cobblestone", 'L', "ingotLead", 'R', "dustRedstone"});
		
		if (NCConfig.register_battery[0]) {
			addShapedOreRecipe(NCBlocks.voltaic_pile_basic, new Object[] {"PSP", "SMS", "PSP", 'P', "plateBasic", 'S', "solenoidCopper", 'M', "blockMagnesium"});
			addShapedEnergyRecipe(NCBlocks.voltaic_pile_advanced, new Object[] {"PMP", "VVV", "PCP", 'V', NCBlocks.voltaic_pile_basic, 'P', "plateAdvanced", 'M', "ingotMagnesium", 'C', "ingotCopper"});
			addShapedEnergyRecipe(NCBlocks.voltaic_pile_du, new Object[] {"PMP", "VVV", "PCP", 'V', NCBlocks.voltaic_pile_advanced, 'P', "plateDU", 'M', "ingotMagnesium", 'C', "ingotCopper"});
			addShapedEnergyRecipe(NCBlocks.voltaic_pile_elite, new Object[] {"PMP", "VVV", "PCP", 'V', NCBlocks.voltaic_pile_du, 'P', "plateElite", 'M', "ingotMagnesium", 'C', "ingotCopper"});
		}
		
		addShapedOreRecipe(NCItems.lithium_ion_cell, new Object[] {"CCC", "FLF", "DDD", 'C', "ingotHardCarbon", 'F', "ingotFerroboron", 'L', "ingotLithium", 'D', "ingotLithiumManganeseDioxide"});
		if (NCConfig.register_battery[1]) {
			addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_basic, new Object[] {"PCP", "CSC", "PCP", 'C', NCItems.lithium_ion_cell, 'P', "plateElite", 'S', "solenoidMagnesiumDiboride"});
			addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_advanced, new Object[] {"PDP", "LLL", "PSP", 'L', NCBlocks.lithium_ion_battery_basic, 'P', "plateAdvanced", 'D', "ingotLithiumManganeseDioxide", 'S', "solenoidMagnesiumDiboride"});
			addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_du, new Object[] {"PDP", "LLL", "PSP", 'L', NCBlocks.lithium_ion_battery_advanced, 'P', "plateDU", 'D', "ingotLithiumManganeseDioxide", 'S', "solenoidMagnesiumDiboride"});
			addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_elite, new Object[] {"PDP", "LLL", "PSP", 'L', NCBlocks.lithium_ion_battery_du, 'P', "plateElite", 'D', "ingotLithiumManganeseDioxide", 'S', "solenoidMagnesiumDiboride"});
		}
		
		addShapedOreRecipe(NCBlocks.bin, new Object[] {"PZP", "Z Z", "PZP", 'P', "plateBasic", 'Z', "ingotSiliconCarbide"});
		
		if (NCConfig.register_passive[0]) {
			addShapedFluidRecipe(NCBlocks.cobblestone_generator, new Object[] {"PIP", "L W", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'L', new BucketIngredient("lava"), 'W', new BucketIngredient("water")});
			addShapedFluidRecipe(NCBlocks.cobblestone_generator, new Object[] {"PIP", "W L", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'L', new BucketIngredient("lava"), 'W', new BucketIngredient("water")});
			addShapedOreRecipe(NCBlocks.cobblestone_generator_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.cobblestone_generator, 'I', "ingotBronze"});
			addShapedOreRecipe(NCBlocks.cobblestone_generator_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.cobblestone_generator_compact, 'I', "ingotGold"});
		}
		
		if (NCConfig.register_passive[1]) {
			addShapedFluidRecipe(NCBlocks.water_source, new Object[] {"PIP", "W W", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'W', new BucketIngredient("water")});
			addShapedOreRecipe(NCBlocks.water_source_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.water_source, 'I', "ingotBronze"});
			addShapedOreRecipe(NCBlocks.water_source_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.water_source_compact, 'I', "ingotGold"});
		}
		
		if (NCConfig.register_passive[2]) {
			addShapedOreRecipe(NCBlocks.nitrogen_collector, new Object[] {"PIP", "B B", "PIP", 'I', "ingotBeryllium", 'P', "plateAdvanced", 'B', Items.BUCKET});
			addShapedOreRecipe(NCBlocks.nitrogen_collector_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.nitrogen_collector, 'I', "ingotBronze"});
			addShapedOreRecipe(NCBlocks.nitrogen_collector_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.nitrogen_collector_compact, 'I', "ingotGold"});
		}
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_casing, 8), new Object[] {" P ", "PFP", " P ", 'P', "plateBasic", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.fission_casing, new Object[] {NCBlocks.fission_glass});
		addShapelessOreRecipe(NCBlocks.fission_casing, new Object[] {NCBlocks.fission_conductor});
		addShapelessOreRecipe(NCBlocks.fission_glass, new Object[] {NCBlocks.fission_casing, "blockGlass"});
		addShapelessOreRecipe(NCBlocks.fission_conductor, new Object[] {NCBlocks.fission_casing});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_monitor, 4), new Object[] {"PGP", "TFT", "PGP", 'P', "plateBasic", 'G', "dustGlowstone", 'T', "ingotTough", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_power_port, 4), new Object[] {"PTP", "RFR", "PTP", 'P', "plateBasic", 'T', "ingotTough", 'R', "dustRedstone", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_vent, 4), new Object[] {"PTP", "VFV", "PTP", 'P', "plateBasic", 'T', "ingotTough", 'V', "servo", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_irradiator, 4), new Object[] {"PZP", "AFA", "PZP", 'P', "plateBasic", 'Z', "ingotZirconium", 'A', "ingotZircaloy", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_source, 2, 0), new Object[] {"PRP", "BFB", "PRP", 'P', "plateBasic", 'R', "dustRadium", 'B', "dustBeryllium", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_source, 2, 1), new Object[] {"PLP", "BFB", "PLP", 'P', "plateBasic", 'L', "dustPolonium", 'B', "dustBeryllium", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_source, 2, 2), new Object[] {"PCP", "CFC", "PCP", 'P', "plateBasic", 'C', "ingotCalifornium252All", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_shield, 4, 0), new Object[] {"PBP", "IFI", "PBP", 'P', "plateBasic", 'B', "ingotBoron", 'I', "ingotSilver", 'F', "steelFrame"});
		if (ModCheck.openComputersLoaded()) {
			addShapedOreRecipe(NCBlocks.fission_computer_port, new Object[] {"PMP", "CFC", "PBP", 'P', "plateBasic", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'B', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'F', "steelFrame"});
		}
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_reflector, 2, 0), new Object[] {"BGB", "GFG", "BGB", 'B', "ingotBeryllium", 'G', "ingotGraphite", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_reflector, 2, 1), new Object[] {"LSL", "SFS", "LSL", 'L', "ingotLead", 'S', "ingotSteel", 'F', "steelFrame"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_irradiator_port, 4), new Object[] {"PHP", "ZFZ", "PHP", 'P', "plateBasic", 'H', Blocks.HOPPER, 'Z', "ingotZirconium", 'F', "steelFrame"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_cell_port, 4), new Object[] {"PHP", "ZFZ", "PHP", 'P', "plateAdvanced", 'H', Blocks.HOPPER, 'Z', "ingotZircaloy", 'F', "steelFrame"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_vessel_port, 4), new Object[] {"PSP", "ZFZ", "PSP", 'P', "plateElite", 'S', "servo", 'Z', "ingotZircaloy", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 4, 0), new Object[] {"PSP", "TFT", "PSP", 'P', "plateElite", 'S', "servo", 'T', "ingotThermoconducting", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 1), new Object[] {" I ", "IPI", " I ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'I', "ingotIron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 2), new Object[] {"RRR", "RPR", "RRR", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'R', "dustRedstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 3), new Object[] {"QQQ", "QPQ", "QQQ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'Q', "gemQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 4), new Object[] {"DOD", "OPO", "DOD", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'O', "obsidian", 'D', "dustObsidian"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 5), new Object[] {"INI", "NPN", "INI", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'N', Blocks.NETHER_BRICK, 'I', "ingotBrickNether"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 6), new Object[] {"GGG", "GPG", "GGG", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'G', "dustGlowstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 7), new Object[] {"LLL", "LPL", "LLL", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'L', "gemLapis"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 8), new Object[] {" G ", "GPG", " G ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'G', "ingotGold"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 9), new Object[] {" R ", "RPR", " R ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'R', "gemPrismarine"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 10), new Object[] {"BBB", "BPB", "BBB", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'B', "slimeball"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 11), new Object[] {"DED", "EPE", "DED", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'E', "endstone", 'D', "dustEndstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 12), new Object[] {"CBC", "BPB", "CBC", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'B', Blocks.PURPUR_BLOCK, 'C', Items.CHORUS_FRUIT_POPPED});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 13), new Object[] {" D ", "DPD", " D ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'D', "gemDiamond"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 14), new Object[] {" E ", "EPE", " E ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'E', "gemEmerald"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 15), new Object[] {" C ", "CPC", " C ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'C', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 0), new Object[] {" T ", "TPT", " T ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'T', "ingotTin"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 1), new Object[] {" L ", "LPL", " L ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'L', "ingotLead"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 2), new Object[] {" B ", "BPB", " B ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'B', "ingotBoron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 3), new Object[] {" L ", "LPL", " L ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'L', "ingotLithium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 4), new Object[] {" M ", "MPM", " M ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'M', "ingotMagnesium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 5), new Object[] {" M ", "MPM", " M ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'M', "ingotManganese"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 6), new Object[] {" A ", "APA", " A ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'A', "ingotAluminum"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 7), new Object[] {" I ", "IPI", " I ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'I', "ingotSilver"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 8), new Object[] {"FFF", "FPF", "FFF", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'F', "gemFluorite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 9), new Object[] {"VVV", "VPV", "VVV", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'V', "gemVilliaumite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 10), new Object[] {"CCC", "CPC", "CCC", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'C', "gemCarobbiite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 11), new Object[] {"AAA", "APA", "AAA", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'A', "dustArsenic"});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 12), new Object[] {new ItemStack(NCBlocks.fission_heater_port, 1, 0), new BucketIngredient("liquid_nitrogen")});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 13), new Object[] {new ItemStack(NCBlocks.fission_heater_port, 1, 0), new BucketIngredient("liquid_helium")});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 14), new Object[] {" E ", "EPE", " E ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'E', "ingotEnderium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_heater_port2, 1, 15), new Object[] {" C ", "CPC", " C ", 'P', new ItemStack(NCBlocks.fission_heater_port, 1, 0), 'C', "dustCryotheum"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_shield_manager, 4), new Object[] {"PTP", "RFR", "PTP", 'P', "plateBasic", 'T', "ingotTough", 'R', Items.REPEATER, 'F', "steelFrame"});
		
		addShapedOreRecipe(NCBlocks.solid_fission_controller, new Object[] {"PTP", "HFH", "PTP", 'P', "plateAdvanced", 'T', "ingotTough", 'H', "ingotHardCarbon", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_cell, 4), new Object[] {"PTP", "ZFZ", "PTP", 'P', "plateAdvanced", 'T', "ingotTough", 'Z', "ingotZircaloy", 'F', "steelFrame"});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 0), new Object[] {"emptyHeatSink", new BucketIngredient("water")});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 1), new Object[] {" I ", "ISI", " I ", 'S', "emptyHeatSink", 'I', "ingotIron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 2), new Object[] {"RRR", "RSR", "RRR", 'S', "emptyHeatSink", 'R', "dustRedstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 3), new Object[] {"QQQ", "QSQ", "QQQ", 'S', "emptyHeatSink", 'Q', "gemQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 4), new Object[] {"DOD", "OSO", "DOD", 'S', "emptyHeatSink", 'O', "obsidian", 'D', "dustObsidian"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 5), new Object[] {"INI", "NSN", "INI", 'S', "emptyHeatSink", 'N', Blocks.NETHER_BRICK, 'I', "ingotBrickNether"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 6), new Object[] {"GGG", "GSG", "GGG", 'S', "emptyHeatSink", 'G', "dustGlowstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 7), new Object[] {"LLL", "LSL", "LLL", 'S', "emptyHeatSink", 'L', "gemLapis"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 8), new Object[] {" G ", "GSG", " G ", 'S', "emptyHeatSink", 'G', "ingotGold"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 9), new Object[] {" P ", "PSP", " P ", 'S', "emptyHeatSink", 'P', "gemPrismarine"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 10), new Object[] {"BBB", "BSB", "BBB", 'S', "emptyHeatSink", 'B', "slimeball"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 11), new Object[] {"DED", "ESE", "DED", 'S', "emptyHeatSink", 'E', "endstone", 'D', "dustEndstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 12), new Object[] {"CPC", "PSP", "CPC", 'S', "emptyHeatSink", 'P', Blocks.PURPUR_BLOCK, 'C', Items.CHORUS_FRUIT_POPPED});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 13), new Object[] {" D ", "DSD", " D ", 'S', "emptyHeatSink", 'D', "gemDiamond"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 14), new Object[] {" E ", "ESE", " E ", 'S', "emptyHeatSink", 'E', "gemEmerald"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink, 1, 15), new Object[] {" C ", "CSC", " C ", 'S', "emptyHeatSink", 'C', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 0), new Object[] {" T ", "TST", " T ", 'S', "emptyHeatSink", 'T', "ingotTin"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 1), new Object[] {" L ", "LSL", " L ", 'S', "emptyHeatSink", 'L', "ingotLead"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 2), new Object[] {" B ", "BSB", " B ", 'S', "emptyHeatSink", 'B', "ingotBoron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 3), new Object[] {" L ", "LSL", " L ", 'S', "emptyHeatSink", 'L', "ingotLithium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 4), new Object[] {" M ", "MSM", " M ", 'S', "emptyHeatSink", 'M', "ingotMagnesium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 5), new Object[] {" M ", "MSM", " M ", 'S', "emptyHeatSink", 'M', "ingotManganese"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 6), new Object[] {" A ", "ASA", " A ", 'S', "emptyHeatSink", 'A', "ingotAluminum"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 7), new Object[] {" I ", "ISI", " I ", 'S', "emptyHeatSink", 'I', "ingotSilver"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 8), new Object[] {"FFF", "FSF", "FFF", 'S', "emptyHeatSink", 'F', "gemFluorite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 9), new Object[] {"VVV", "VSV", "VVV", 'S', "emptyHeatSink", 'V', "gemVilliaumite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 10), new Object[] {"CCC", "CSC", "CCC", 'S', "emptyHeatSink", 'C', "gemCarobbiite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 11), new Object[] {"AAA", "ASA", "AAA", 'S', "emptyHeatSink", 'A', "dustArsenic"});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 12), new Object[] {"emptyHeatSink", new BucketIngredient("liquid_nitrogen")});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 13), new Object[] {"emptyHeatSink", new BucketIngredient("liquid_helium")});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 14), new Object[] {" E ", "ESE", " E ", 'S', "emptyHeatSink", 'E', "ingotEnderium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.solid_fission_sink2, 1, 15), new Object[] {" C ", "CSC", " C ", 'S', "emptyHeatSink", 'C', "dustCryotheum"});
		
		addShapedOreRecipe(NCBlocks.salt_fission_controller, new Object[] {"PEP", "HFH", "PEP", 'P', "plateElite", 'E', "ingotExtreme", 'H', "ingotHardCarbon", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_vessel, 4), new Object[] {"PEP", "ZFZ", "PEP", 'P', "plateElite", 'E', "ingotExtreme", 'Z', "ingotZircaloy", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 8, 0), new Object[] {"PEP", "TFT", "PEP", 'P', "plateElite", 'E', "ingotExtreme", 'T', "ingotThermoconducting", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 1), new Object[] {" I ", "IHI", " I ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'I', "ingotIron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 2), new Object[] {"RRR", "RHR", "RRR", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'R', "dustRedstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 3), new Object[] {"QQQ", "QHQ", "QQQ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'Q', "gemQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 4), new Object[] {"DOD", "OHO", "DOD", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'O', "obsidian", 'D', "dustObsidian"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 5), new Object[] {"INI", "NHN", "INI", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'N', Blocks.NETHER_BRICK, 'I', "ingotBrickNether"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 6), new Object[] {"GGG", "GHG", "GGG", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'G', "dustGlowstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 7), new Object[] {"LLL", "LHL", "LLL", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'L', "gemLapis"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 8), new Object[] {" G ", "GHG", " G ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'G', "ingotGold"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 9), new Object[] {" P ", "PHP", " P ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'P', "gemPrismarine"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 10), new Object[] {"BBB", "BHB", "BBB", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'B', "slimeball"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 11), new Object[] {"DED", "EHE", "DED", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'E', "endstone", 'D', "dustEndstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 12), new Object[] {"CPC", "PHP", "CPC", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'P', Blocks.PURPUR_BLOCK, 'C', Items.CHORUS_FRUIT_POPPED});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 13), new Object[] {" D ", "DHD", " D ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'D', "gemDiamond"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 14), new Object[] {" E ", "EHE", " E ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'E', "gemEmerald"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 15), new Object[] {" C ", "CHC", " C ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'C', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 0), new Object[] {" T ", "THT", " T ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'T', "ingotTin"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 1), new Object[] {" L ", "LHL", " L ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'L', "ingotLead"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 2), new Object[] {" B ", "BHB", " B ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'B', "ingotBoron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 3), new Object[] {" L ", "LHL", " L ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'L', "ingotLithium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 4), new Object[] {" M ", "MHM", " M ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'M', "ingotMagnesium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 5), new Object[] {" M ", "MHM", " M ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'M', "ingotManganese"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 6), new Object[] {" A ", "AHA", " A ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'A', "ingotAluminum"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 7), new Object[] {" I ", "IHI", " I ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'I', "ingotSilver"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 8), new Object[] {"FFF", "FHF", "FFF", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'F', "gemFluorite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 9), new Object[] {"VVV", "VHV", "VVV", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'V', "gemVilliaumite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 10), new Object[] {"CCC", "CHC", "CCC", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'C', "gemCarobbiite"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 11), new Object[] {"AAA", "AHA", "AAA", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'A', "dustArsenic"});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 12), new Object[] {new ItemStack(NCBlocks.salt_fission_heater, 1, 0), new BucketIngredient("liquid_nitrogen")});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 13), new Object[] {new ItemStack(NCBlocks.salt_fission_heater, 1, 0), new BucketIngredient("liquid_helium")});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 14), new Object[] {" E ", "EHE", " E ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'E', "ingotEnderium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_heater2, 1, 15), new Object[] {" C ", "CHC", " C ", 'H', new ItemStack(NCBlocks.salt_fission_heater, 1, 0), 'C', "dustCryotheum"});
		
		addShapedOreRecipe(NCBlocks.heat_exchanger_controller, new Object[] {"SES", "TFT", "SES", 'S', "stone", 'E', "ingotExtreme", 'T', "ingotThermoconducting", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_casing, 8), new Object[] {" S ", "SFS", " S ", 'S', "stone", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_casing, new Object[] {NCBlocks.heat_exchanger_glass});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_glass, new Object[] {NCBlocks.heat_exchanger_casing, "blockGlass"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_vent, 4), new Object[] {"SIS", "VFV", "SIS", 'S', "stone", 'I', "ingotSteel", 'V', "servo", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_tube_copper, 4), new Object[] {"SCS", "CFC", "SVS", 'S', "stone", 'C', "ingotCopper", 'F', "steelFrame", 'V', "servo"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_tube_hard_carbon, 4), new Object[] {"SHS", "HFH", "SVS", 'S', "stone", 'H', "ingotHardCarbon", 'F', "steelFrame", 'V', "servo"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_tube_thermoconducting, 4), new Object[] {"STS", "TFT", "SVS", 'S', "stone", 'T', "ingotThermoconducting", 'F', "steelFrame", 'V', "servo"});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_tube_copper, new Object[] {NCBlocks.condenser_tube_copper});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_tube_hard_carbon, new Object[] {NCBlocks.condenser_tube_hard_carbon});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_tube_thermoconducting, new Object[] {NCBlocks.condenser_tube_thermoconducting});
		if (ModCheck.openComputersLoaded()) {
			addShapedOreRecipe(NCBlocks.heat_exchanger_computer_port, new Object[] {"SMS", "CFC", "SPS", 'S', "stone", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'P', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'F', "steelFrame"});
		}
		
		addShapedOreRecipe(NCBlocks.condenser_controller, new Object[] {"STS", "CFC", "STS", 'S', "stone", 'T', "ingotTough", 'C', "ingotThermoconducting", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.condenser_tube_copper, new Object[] {NCBlocks.heat_exchanger_tube_copper});
		addShapelessOreRecipe(NCBlocks.condenser_tube_hard_carbon, new Object[] {NCBlocks.heat_exchanger_tube_hard_carbon});
		addShapelessOreRecipe(NCBlocks.condenser_tube_thermoconducting, new Object[] {NCBlocks.heat_exchanger_tube_thermoconducting});
		
		addShapedOreRecipe(NCBlocks.turbine_controller, new Object[] {"STS", "TFT", "STS", 'S', "ingotHSLASteel", 'T', "ingotTough", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_casing, 8), new Object[] {" S ", "SFS", " S ", 'S', "ingotHSLASteel", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.turbine_casing, new Object[] {NCBlocks.turbine_glass});
		addShapelessOreRecipe(NCBlocks.turbine_glass, new Object[] {NCBlocks.turbine_casing, "blockGlass"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_shaft, 4), new Object[] {"SSS", "TTT", "SSS", 'S', "ingotHSLASteel", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_blade_steel, 4), new Object[] {"SHS", "SHS", "SHS", 'S', "ingotSteel", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_blade_extreme, 4), new Object[] {"EHE", "EHE", "EHE", 'E', "ingotExtreme", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_blade_sic_sic_cmc, 4), new Object[] {"SHS", "SHS", "SHS", 'S', "ingotSiCSiCCMC", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_stator, 4), new Object[] {"SS", "SS", "SS", 'S', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_bearing, 4), new Object[] {"SGS", "GFG", "SGS", 'G', "nuggetGold", 'S', "ingotHSLASteel", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 2, 0), new Object[] {"MMM", "HTH", "MMM", 'M', "ingotMagnesium", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 2, 1), new Object[] {"BBB", "HTH", "BBB", 'B', "ingotBeryllium", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 2, 2), new Object[] {"AAA", "HTH", "AAA", 'A', "ingotAluminum", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 2, 3), new Object[] {"GGG", "HTH", "GGG", 'G', "ingotGold", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 2, 4), new Object[] {"CCC", "HTH", "CCC", 'C', "ingotCopper", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 2, 5), new Object[] {"SSS", "HTH", "SSS", 'S', "ingotSilver", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_coil_connector, 4), new Object[] {"HHH", "HTH", "HHH", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_inlet, 4), new Object[] {"STS", "VFV", "STS", 'S', "ingotHSLASteel", 'T', "ingotTough", 'V', "servo", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_outlet, 4), new Object[] {"SSS", "VFV", "SSS", 'S', "ingotHSLASteel", 'V', "servo", 'F', "steelFrame"});
		if (ModCheck.openComputersLoaded()) {
			addShapedOreRecipe(NCBlocks.turbine_computer_port, new Object[] {"SMS", "CFC", "SPS", 'S', "ingotHSLASteel", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'P', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'F', "steelFrame"});
		}
		
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 0), new Object[] {"LG", "GL", 'L', "ingotLead", 'G', "dustGraphite"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 0), new Object[] {"GL", "LG", 'L', "ingotLead", 'G', "dustGraphite"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 1), new Object[] {" R ", "TPT", " R ", 'R', "dustRedstone", 'T', "ingotTough", 'P', "plateBasic"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 2), new Object[] {"SUS", "UPU", "SUS", 'S', "dustSulfur", 'U', "ingotUranium238", 'P', "plateAdvanced"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 3), new Object[] {"RBR", "BPB", "RBR", 'R', "dustCrystalBinder", 'B', "ingotBoron", 'P', "plateDU"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 4), new Object[] {"CC", "II", "CC", 'C', "ingotCopper", 'I', "ingotIron"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 5), new Object[] {"MM", "TT", "MM", 'M', "ingotMagnesiumDiboride", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 7), new Object[] {"F F", "RSR", "SCS", 'F', "ingotFerroboron", 'S', "ingotSteel", 'C', "ingotCopper", 'R', "dustRedstone"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 8), new Object[] {"SSG", "CCI", "SSG", 'G', "nuggetGold", 'S', "ingotSteel", 'I', "ingotIron", 'C', "solenoidCopper"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 9), new Object[] {"  S", "FP ", "CF ", 'F', "ingotFerroboron", 'S', "ingotSteel", 'P', Blocks.PISTON, 'C', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 10), new Object[] {"LSL", "STS", "LSL", 'L', "ingotLead", 'T', "ingotTough", 'S', "ingotSteel"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 11), new Object[] {"PTP", "I I", "PTP", 'P', "plateBasic", 'I', "ingotIron", 'T', "ingotTin"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 12), new Object[] {"STS", "TBT", "STS", 'S', "ingotSteel", 'B', "ingotBronze", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 8, 14), new Object[] {"PSP", "T T", "PSP", 'P', "plateAdvanced", 'S', "ingotSteel", 'T', "ingotTough"});
		
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 0), new Object[] {"LRL", "RPR", "LRL", 'L', "gemLapis", 'R', "dustRedstone", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE});
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 1), new Object[] {"OQO", "QPQ", "OQO", 'O', "dustObsidian", 'Q', "dustQuartz", 'P', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE});
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 1), new Object[] {"OQO", "QPQ", "OQO", 'O', "dustObsidian", 'Q', "dustNetherQuartz", 'P', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE});
		
		tools("ingotBoron", NCTools.sword_boron, NCTools.pickaxe_boron, NCTools.shovel_boron, NCTools.axe_boron, NCTools.hoe_boron, NCTools.spaxelhoe_boron);
		tools("ingotTough", NCTools.sword_tough, NCTools.pickaxe_tough, NCTools.shovel_tough, NCTools.axe_tough, NCTools.hoe_tough, NCTools.spaxelhoe_tough);
		tools("ingotHardCarbon", NCTools.sword_hard_carbon, NCTools.pickaxe_hard_carbon, NCTools.shovel_hard_carbon, NCTools.axe_hard_carbon, NCTools.hoe_hard_carbon, NCTools.spaxelhoe_hard_carbon);
		tools("gemBoronNitride", NCTools.sword_boron_nitride, NCTools.pickaxe_boron_nitride, NCTools.shovel_boron_nitride, NCTools.axe_boron_nitride, NCTools.hoe_boron_nitride, NCTools.spaxelhoe_boron_nitride);
		
		armor("ingotBoron", NCArmor.helm_boron, NCArmor.chest_boron, NCArmor.legs_boron, NCArmor.boots_boron);
		armor("ingotTough", NCArmor.helm_tough, NCArmor.chest_tough, NCArmor.legs_tough, NCArmor.boots_tough);
		armor("ingotHardCarbon", NCArmor.helm_hard_carbon, NCArmor.chest_hard_carbon, NCArmor.legs_hard_carbon, NCArmor.boots_hard_carbon);
		armor("gemBoronNitride", NCArmor.helm_boron_nitride, NCArmor.chest_boron_nitride, NCArmor.legs_boron_nitride, NCArmor.boots_boron_nitride);
		
		fissionFuelRecipes("Uranium", NCItems.pellet_uranium, NCItems.fuel_uranium, 238, 233, 235);
		fissionFuelRecipes("Neptunium", NCItems.pellet_neptunium, NCItems.fuel_neptunium, 237, 236);
		fissionFuelRecipes("Plutonium", NCItems.pellet_plutonium, NCItems.fuel_plutonium, 242, 239, 241);
		fissionFuelLowEnrichedRecipeAll(NCItems.pellet_mixed, 0, "Uranium238", "Plutonium239", new int[] {0, 1}, "", "Carbide");
		fissionFuelLowEnrichedRecipeAll(NCItems.fuel_mixed, 0, "Uranium238", "Plutonium239", new int[] {1, 2, 3}, "Oxide", "Nitride", "ZA");
		fissionFuelLowEnrichedRecipeAll(NCItems.pellet_mixed, 2, "Uranium238", "Plutonium241", new int[] {0, 1}, "", "Carbide");
		fissionFuelLowEnrichedRecipeAll(NCItems.fuel_mixed, 4, "Uranium238", "Plutonium241", new int[] {1, 2, 3}, "Oxide", "Nitride", "ZA");
		fissionFuelRecipes("Americium", NCItems.pellet_americium, NCItems.fuel_americium, 243, 242);
		fissionFuelRecipes("Curium", NCItems.pellet_curium, NCItems.fuel_curium, 246, 243, 245, 247);
		fissionFuelRecipes("Berkelium", NCItems.pellet_berkelium, NCItems.fuel_berkelium, 247, 248);
		fissionFuelRecipes("Californium", NCItems.pellet_californium, NCItems.fuel_californium, 252, 249, 251);
		
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 1), new Object[] {"dustRhodochrosite", "dustCalciumSulfate", "dustObsidian", "dustMagnesium"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 2), new Object[] {"dustRedstone", "dustGlowstone"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 9), new Object[] {"dustObsidian", "dustObsidian", "dustObsidian", "dustObsidian", "dustEndstone"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 10), new Object[] {"dustGraphite", "dustManganese"});
		
		addShapedOreRecipe(NCItems.portable_ender_chest, new Object[] {" S ", "WCW", "LWL", 'C', "chestEnder", 'W', new ItemStack(Blocks.WOOL, 1, 10), 'S', "string", 'L', "ingotTough"});
		addShapedOreRecipe(NCItems.portable_ender_chest, new Object[] {" S ", "WCW", "LWL", 'C', "chestEnder", 'W', new ItemStack(Blocks.WOOL, 1, 15), 'S', "string", 'L', "ingotTough"});
		
		addShapelessOreRecipe(new ItemStack(NCItems.dominos, 4), new Object[] {Items.BREAD, Items.BREAD, Items.BREAD, Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM});
		addShapelessOreRecipe(Blocks.BROWN_MUSHROOM, new Object[] {NCBlocks.glowing_mushroom});
		addShapelessOreRecipe(NCBlocks.glowing_mushroom, new Object[] {Blocks.BROWN_MUSHROOM, "dustGlowstone"});
		
		addShapedOreRecipe(new ItemStack(Items.COOKIE, 8), new Object[] {"FCF", 'F', "dustWheat", 'C', "dustCocoa"});
		addShapelessOreRecipe(NCItems.smore, new Object[] {NCItems.graham_cracker, "ingotChocolate", "ingotMarshmallow", NCItems.graham_cracker});
		addShapelessOreRecipe(NCItems.moresmore, new Object[] {NCItems.smore, "ingotChocolate", "ingotMarshmallow", NCItems.smore});
		addShapelessOreRecipe(NCItems.foursmore, new Object[] {NCItems.moresmore, "ingotChocolate", "ingotMarshmallow", NCItems.moresmore});
		
		addShapedOreRecipe(NCItems.geiger_counter, new Object[] {"SFF", "CRR", "BFF", 'S', "ingotSteel", 'F', "ingotFerroboron", 'C', "ingotCopper", 'R', "dustRedstone", 'B', "bioplastic"});
		addShapedOreRecipe(NCItems.radiation_badge, new Object[] {" C ", "SRS", " L ", 'C', "ingotCopper", 'S', "string", 'R', "dustRedstone", 'L', "ingotLead"});
		
		//addShapedOreRecipe(NCItems.radaway_slow, new Object[] {" D ", "DRD", " D ", 'R', NCItems.radaway, 'D', "dustRedstone"});
		addShapedOreRecipe(NCItems.rad_x, new Object[] {"EPE", "PRP", "PBP", 'E', "dustEnergetic", 'P', "bioplastic", 'R', NCItems.radaway, 'B', Items.BLAZE_POWDER});
		
		addShapedOreRecipe(NCBlocks.radiation_scrubber, new Object[] {"PCP", "CEC", "PCP", 'P', "plateElite", 'E', "ingotExtreme", 'C', "dustBorax"});
		
		addShapedOreRecipe(NCBlocks.geiger_block, new Object[] {" P ", "PGP", " P ", 'P', "plateBasic", 'G', NCItems.geiger_counter});
		
		addShapedOreRecipe(new ItemStack(NCItems.rad_shielding, 1, 0), new Object[] {"III", "CCC", "LLL", 'I', "ingotIron", 'C', "coal", 'L', "ingotLead"});
		addShapedOreRecipe(new ItemStack(NCItems.rad_shielding, 1, 1), new Object[] {"BBB", "RFR", "PPP", 'B', "bioplastic", 'F', "ingotFerroboron", 'P', "plateBasic", 'R', new ItemStack(NCItems.rad_shielding, 1, 0)});
		addShapedOreRecipe(new ItemStack(NCItems.rad_shielding, 1, 2), new Object[] {"BBB", "RHR", "PPP", 'B', "ingotBeryllium", 'H', "ingotHardCarbon", 'P', "plateDU", 'R', new ItemStack(NCItems.rad_shielding, 1, 1)});
		
		addShapedOreRecipe(new ItemStack(NCItems.multitool), new Object[] {" F ", "HSF", "SB ", 'F', "ingotFerroboron", 'H', "ingotHardCarbon", 'S', "ingotSteel", 'B', "ingotBronze"});
		
		addShapelessOreRecipe(NCItems.record_wanderer, new Object[] {"record", "ingotTough"});
		addShapelessOreRecipe(NCItems.record_end_of_the_world, new Object[] {"record", "ingotUranium235"});
		addShapelessOreRecipe(NCItems.record_money_for_nothing, new Object[] {"record", "ingotSilver"});
		addShapelessOreRecipe(NCItems.record_hyperspace, new Object[] {"record", "dustDimensional"});
		
		addShapedOreRecipe(NCArmor.helm_hazmat, new Object[] {"YWY", "SLS", "BIB", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_HELMET, 'B', "bioplastic", 'I', "ingotSteel", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		addShapedOreRecipe(NCArmor.chest_hazmat, new Object[] {"WSW", "YLY", "SWS", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_CHESTPLATE, 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		addShapedOreRecipe(NCArmor.legs_hazmat, new Object[] {"YBY", "SLS", "W W", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_LEGGINGS, 'B', "bioplastic", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		addShapedOreRecipe(NCArmor.boots_hazmat, new Object[] {"SDS", "BLB", 'D', "dyeBlack", 'L', Items.LEATHER_BOOTS, 'B', "bioplastic", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		
		if (NCConfig.register_quantum) {
			addShapedOreRecipe(NCBlocks.quantum_computer_controller, new Object[] {"EPE", "PFP", "EPE", 'E', "ingotExtreme", 'P', Items.ENDER_PEARL, 'F', "steelFrame"});
			addShapedOreRecipe(NCBlocks.quantum_computer_qubit, new Object[] {"ESE", "PRP", "ESE", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL, 'R', "blockRedstone"});
			
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 0), new Object[] {"SES", "ECE", "SES", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 1), new Object[] {"SES", "ECE", "ESE", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 2), new Object[] {"SSS", "ECE", "SSS", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 3), new Object[] {"SES", "SCS", "SES", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 4), new Object[] {"ESS", "ECE", "SSE", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 6), new Object[] {"SSS", "ECE", "ESE", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 8), new Object[] {"SSS", "SCS", "SEE", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 0), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 9), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 1), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 10), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 2), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 11), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 4), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 5), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 5), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 4), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 6), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 7), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 7), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 6), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 9), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 0), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 10), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 1), Blocks.REDSTONE_TORCH});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 11), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 2), Blocks.REDSTONE_TORCH});
			
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 0), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 9), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 1), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 10), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 2), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 11), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 4), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 5), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 5), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 4), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 6), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 7), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 7), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 6), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 9), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 0), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 10), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 1), "dustRedstone"});
			addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, 11), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, 2), "dustRedstone"});
			
			for (int i = 0; i < QuantumComputerGateEnums.SingleType.values().length; i++) {
				addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_single, 1, i), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_control, 1, i)});
				addShapelessOreRecipe(new ItemStack(NCBlocks.quantum_computer_gate_control, 1, i), new Object[] {new ItemStack(NCBlocks.quantum_computer_gate_single, 1, i)});
			}
			
			addShapedOreRecipe(new ItemStack(NCBlocks.quantum_computer_connector, 8), new Object[] {"ESE", "SPS", "ESE", 'E', "ingotExtreme", 'S', "ingotSteel", 'P', Items.ENDER_PEARL});
			/*if (ModCheck.openComputersLoaded()) {
				addShapedOreRecipe(NCBlocks.quantum_computer_port, new Object[] {"EME", "CFC", "EPE", 'E', "ingotExtreme", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'P', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'F', "steelFrame"});
			}*/
		}
	}
	
	public static void registerRadShieldingCraftingRecipes() {
		if (NCConfig.radiation_shielding_default_recipes) {
			for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
				if (ArmorHelper.isArmor(item, NCConfig.radiation_horse_armor_public)) {
					NonNullList<ItemStack> stacks = NonNullList.create();
					item.getSubItems(CreativeTabs.SEARCH, stacks);
					for (ItemStack stack : stacks) {
						int packed = RecipeItemHelper.pack(stack);
						if (!RadArmor.ARMOR_STACK_SHIELDING_BLACKLIST.contains(packed)) {
							RadArmor.addArmorShieldingRecipes(stack);
						}
					}
				}
			}
		}
		
		for (int packed : RadArmor.ARMOR_STACK_SHIELDING_LIST) {
			RadArmor.addArmorShieldingRecipes(RecipeItemHelper.unpack(packed));
		}
	}
	
	public static void fissionFuelRecipes(String element, Item pellet, Item fuel, int fertileNo, int... fissileNo) {
		for (int i = 0; i < fissileNo.length; i++) {
			fissionFuelLowEnrichedRecipeAll(pellet, 4*i, element + fertileNo, element + fissileNo[i], new int[] {0, 1}, "", "Carbide");
			fissionFuelLowEnrichedRecipeAll(fuel, 8*i, element + fertileNo, element + fissileNo[i], new int[] {1, 2, 3}, "Oxide", "Nitride", "ZA");
			fissionFuelHighlyEnrichedRecipeAll(pellet, 4*i + 2, element + fertileNo, element + fissileNo[i], new int[] {0, 1}, "", "Carbide");
			fissionFuelHighlyEnrichedRecipeAll(fuel, 8*i + 4, element + fertileNo, element + fissileNo[i], new int[] {1, 2, 3}, "Oxide", "Nitride", "ZA");
		}
	}
	
	public static void fissionFuelLowEnrichedRecipeAll(Item fuel, int startMeta, String fertile, String fissile, int[] offsets, String... types) {
		for (int i = 0; i < offsets.length; i++) {
			fissionFuelLowEnrichedRecipe(fuel, startMeta + offsets[i], fertile + types[i], fissile + types[i]);
		}
	}
	
	public static void fissionFuelHighlyEnrichedRecipeAll(Item fuel, int startMeta, String fertile, String fissile, int[] offsets, String... types) {
		for (int i = 0; i < offsets.length; i++) {
			fissionFuelHighlyEnrichedRecipe(fuel, startMeta + offsets[i], fertile + types[i], fissile + types[i]);
		}
	}
	
	public static void fissionFuelLowEnrichedRecipe(Item fuel, int meta, String fertile, String fissile) {
		fertile = "ingot" + fertile;
		fissile = "ingot" + fissile;
		addShapelessOreRecipe(new ItemStack(fuel, 9, meta), new Object[] {fissile, fertile, fertile, fertile, fertile, fertile, fertile, fertile, fertile});
	}
	
	public static void fissionFuelHighlyEnrichedRecipe(Item fuel, int meta, String fertile, String fissile) {
		fertile = "ingot" + fertile;
		fissile = "ingot" + fissile;
		addShapelessOreRecipe(new ItemStack(fuel, 9, meta), new Object[] {fissile, fissile, fissile, fertile, fertile, fertile, fertile, fertile, fertile});
	}
	
	public static void blockCompress(Block blockOut, int metaOut, String itemOutOreName, Object itemIn) {
		addShapedOreRecipe(OreDictHelper.getPrioritisedCraftingStack(new ItemStack(blockOut, 1, metaOut), itemOutOreName), new Object[] {"III", "III", "III", 'I', itemIn});
	}
	
	public static void blockOpen(Item itemOut, int metaOut, String itemOutOreName, Object itemIn) {
		addShapelessOreRecipe(OreDictHelper.getPrioritisedCraftingStack(new ItemStack(itemOut, 9, metaOut), itemOutOreName), new Object[] {itemIn});
	}
	
	public static void tools(Object material, Item sword, Item pick, Item shovel, Item axe, Item hoe, Item spaxelhoe) {
		addShapedOreRecipe(sword, new Object[] {"M", "M", "S", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(pick, new Object[] {"MMM", " S ", " S ", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(shovel, new Object[] {"M", "S", "S", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(axe, new Object[] {"MM", "MS", " S", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(axe, new Object[] {"MM", "SM", "S ", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(hoe, new Object[] {"MM", " S", " S", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(hoe, new Object[] {"MM", "S ", "S ", 'M', material, 'S', "stickWood"});
		addShapedOreRecipe(spaxelhoe, new Object[] {"ASP", "HIW", " I ", 'A', axe, 'S', shovel, 'P', pick, 'H', hoe, 'W', sword, 'I', "ingotIron"});
	}
	
	public static void armor(Object material, Item helm, Item chest, Item legs, Item boots) {
		addShapedOreRecipe(helm, new Object[] {"MMM", "M M", 'M', material});
		addShapedOreRecipe(chest, new Object[] {"M M", "MMM", "MMM", 'M', material});
		addShapedOreRecipe(legs, new Object[] {"MMM", "M M", "M M", 'M', material});
		addShapedOreRecipe(boots, new Object[] {"M M", "M M", 'M', material});
	}
	
	private static final Object2IntMap<String> RECIPE_COUNT_MAP = new Object2IntOpenHashMap<>();
	
	public static void addShapedOreRecipe(Object out, Object... inputs) {
		registerRecipe(ShapedOreRecipe.class, out, inputs);
	}
	
	public static void addShapedEnergyRecipe(Object out, Object... inputs) {
		registerRecipe(ShapedEnergyRecipe.class, out, inputs);
	}
	
	public static void addShapedFluidRecipe(Object out, Object... inputs) {
		registerRecipe(ShapedFluidRecipe.class, out, inputs);
	}
	
	public static void addShapelessOreRecipe(Object out, Object... inputs) {
		registerRecipe(ShapelessOreRecipe.class, out, inputs);
	}
	
	public static void addShapelessFluidRecipe(Object out, Object... inputs) {
		registerRecipe(ShapelessFluidRecipe.class, out, inputs);
	}
	
	public static void addShapelessArmorUpgradeRecipe(Object out, Object... inputs) {
		registerRecipe(ShapelessArmorRadShieldingRecipe.class, out, inputs);
	}
	
	public static void registerRecipe(Class<? extends IRecipe> clazz, Object out, Object... inputs) {
		if (out == null || Lists.newArrayList(inputs).contains(null)) return;
		ItemStack outStack = ItemStackHelper.fixItemStack(out);
		if (!outStack.isEmpty() && inputs != null) {
			String outName = outStack.getTranslationKey();
			if (RECIPE_COUNT_MAP.containsKey(outName)) {
				int count = RECIPE_COUNT_MAP.getInt(outName);
				RECIPE_COUNT_MAP.put(outName, count + 1);
				outName = outName + "_" + count;	
			} else RECIPE_COUNT_MAP.put(outName, 1);
			ResourceLocation location = new ResourceLocation(Global.MOD_ID, outName);
			try {
				IRecipe recipe = NCUtil.newInstance(clazz, location, outStack, inputs);
				recipe.setRegistryName(location);
				ForgeRegistries.RECIPES.register(recipe);
			} catch (Exception e) {
				
			}
		}
	}
}
