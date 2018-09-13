package nc.recipe.vanilla;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nc.Global;
import nc.config.NCConfig;
import nc.enumm.MetaEnums.IngotType;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.init.NCTools;
import nc.radiation.RadiationArmor;
import nc.util.FluidHelper;
import nc.util.ItemStackHelper;
import nc.util.OreDictHelper;
import nc.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CraftingRecipeHandler {
	
	public static void registerCraftingRecipes() {
		for (int i = 0; i < IngotType.values().length; i++) {
			blockCompress(new ItemStack(NCBlocks.ingot_block, 1, i), "block" + StringHelper.capitalize(IngotType.values()[i].getName()), new ItemStack(NCItems.ingot, 1, i));
			blockOpen(NCItems.ingot, i, "ingot" + StringHelper.capitalize(IngotType.values()[i].getName()), new ItemStack(NCBlocks.ingot_block, 1, i));
		}
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_block, 4, 0), new Object[] {" P ", "PTP", " P ", 'P', "plateBasic", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCBlocks.reactor_casing_transparent, 4), new Object[] {"GPG", "PTP", "GPG", 'P', "plateBasic", 'T', "ingotTough", 'G', "blockGlass"});
		addShapelessOreRecipe(new ItemStack(NCBlocks.fission_block, 1, 0), new Object[] {NCBlocks.reactor_casing_transparent});
		addShapelessOreRecipe(NCBlocks.reactor_casing_transparent, new Object[] {new ItemStack(NCBlocks.fission_block, 1, 0), "blockGlass"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_block, 4, 1), new Object[] {" P ", "POP", " P ", 'P', "plateBasic", 'O', "obsidian"});
		addShapedOreRecipe(NCBlocks.cell_block, new Object[] {"TGT", "G G", "TGT", 'T', "ingotTough", 'G', "blockGlass"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 2, 0), new Object[] {"TIT", "I I", "TIT", 'T', "ingotTough", 'I', "ingotSteel"});
		addShapelessOreRecipe(new ItemStack(NCBlocks.cooler, 1, 1), new Object[] {new ItemStack(NCBlocks.cooler, 1, 0), Items.WATER_BUCKET});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 2), new Object[] {" B ", "RCR", " B ", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'R', "dustRedstone", 'B', "blockRedstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 2), new Object[] {" R ", "BCB", " R ", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'R', "dustRedstone", 'B', "blockRedstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 3), new Object[] {"DQD", "DCD", "DQD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'Q', "blockQuartz", 'D', "dustQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 3), new Object[] {"DDD", "QCQ", "DDD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'Q', "blockQuartz", 'D', "dustQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 3), new Object[] {"DQD", "DCD", "DQD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'Q', "blockQuartz", 'D', "dustNetherQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 3), new Object[] {"DDD", "QCQ", "DDD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'Q', "blockQuartz", 'D', "dustNetherQuartz"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 4), new Object[] {"GGG", "GCG", "GGG", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'G', "ingotGold"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 5), new Object[] {"DGD", "DCD", "DGD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'G', "glowstone", 'D', "dustGlowstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 5), new Object[] {"DDD", "GCG", "DDD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'G', "glowstone", 'D', "dustGlowstone"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 6), new Object[] {"BCB", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'B', "blockLapis"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 6), new Object[] {"B", "C", "B", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'B', "blockLapis"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 7), new Object[] {"DDD", "DCD", "DDD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'D', "gemDiamond"});
		addShapelessOreRecipe(new ItemStack(NCBlocks.cooler, 1, 8), new Object[] {new ItemStack(NCBlocks.cooler, 1, 0), FluidHelper.getBucket("liquidhelium")});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 9), new Object[] {"EEE", "ECE", "EEE", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'E', "ingotEnderium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 10), new Object[] {"DDD", "DCD", "DDD", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'D', "dustCryotheum"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 11), new Object[] {"III", "ICI", "III", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'I', "ingotIron"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 12), new Object[] {"EEE", " C ", "EEE", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'E', "gemEmerald"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 12), new Object[] {"E E", "ECE", "E E", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'E', "gemEmerald"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 13), new Object[] {"III", "ICI", "III", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'I', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 14), new Object[] {"TTT", "TCT", "TTT", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'T', "ingotTin"});
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 1, 15), new Object[] {"MMM", "MCM", "MMM", 'C', new ItemStack(NCBlocks.cooler, 1, 0), 'M', "ingotMagnesium"});
		
		addShapedOreRecipe(NCItems.reactor_door, new Object[] {"CC", "CC", "CC", 'C', new ItemStack(NCBlocks.fission_block, 1, 0)});
		addShapedOreRecipe(NCBlocks.reactor_trapdoor, new Object[] {"CCC", "CCC", 'C', new ItemStack(NCBlocks.fission_block, 1, 0)});
		
		blockCompress(NCBlocks.block_depleted_thorium, "blockThorium230", "ingotThorium230");
		blockCompress(NCBlocks.block_depleted_uranium, "blockUranium238", "ingotUranium238");
		blockCompress(NCBlocks.block_depleted_neptunium, "blockNeptunium237", "ingotNeptunium237");
		blockCompress(NCBlocks.block_depleted_plutonium, "blockPlutonium242", "ingotPlutonium242");
		blockCompress(NCBlocks.block_depleted_americium, "blockAmericium243", "ingotAmericium243");
		blockCompress(NCBlocks.block_depleted_curium, "blockCurium246", "ingotCurium246");
		blockCompress(NCBlocks.block_depleted_berkelium, "blockBerkelium247", "ingotBerkelium247");
		blockCompress(NCBlocks.block_depleted_californium, "blockCalifornium252", "ingotCalifornium252");
		
		blockOpen(NCItems.thorium, 0, "ingotThorium230", "blockThorium230");
		blockOpen(NCItems.uranium, 8, "ingotUranium238", "blockUranium238");
		blockOpen(NCItems.neptunium, 4, "ingotNeptunium237", "blockNeptunium237");
		blockOpen(NCItems.plutonium, 12, "ingotPlutonium242", "blockPlutonium242");
		blockOpen(NCItems.americium, 8, "ingotAmericium243", "blockAmericium243");
		blockOpen(NCItems.curium, 8, "ingotCurium246", "blockCurium246");
		blockOpen(NCItems.berkelium, 0, "ingotBerkelium247", "blockBerkelium247");
		blockOpen(NCItems.californium, 12, "ingotCalifornium252", "blockCalifornium252");
		
		addShapedOreRecipe(NCBlocks.nuclear_furnace_idle, new Object[] {"PTP", "TFT", "PTP", 'T', "ingotTough", 'P', "plateBasic", 'F', Blocks.FURNACE});
		
		addShapedOreRecipe(NCBlocks.manufactory_idle, new Object[] {"LRL", "FPF", "LCL", 'C', "solenoidCopper", 'R', "dustRedstone", 'L', "ingotLead", 'P', Blocks.PISTON, 'F', Items.FLINT});
		addShapedOreRecipe(NCBlocks.alloy_furnace_idle, new Object[] {"PRP", "BFB", "PCP", 'C', "solenoidCopper", 'R', "dustRedstone", 'P', "plateBasic", 'F', Blocks.FURNACE, 'B', Items.BRICK});
		addShapedOreRecipe(NCBlocks.decay_hastener_idle, new Object[] {"PGP", "EME", "PCP", 'M', "chassis", 'C', "solenoidCopper", 'P', "plateAdvanced", 'G', "dustGlowstone", 'E', Items.ENDER_PEARL});
		addShapedOreRecipe(NCBlocks.fuel_reprocessor_idle, new Object[] {"PBP", "TCT", "PAP", 'C', "chassis", 'A', "actuator", 'P', "plateBasic", 'T', "ingotTough", 'B', "ingotBoron"});
		addShapedOreRecipe(NCBlocks.isotope_separator_idle, new Object[] {"PMP", "RCR", "PMP", 'C', "chassis", 'M', "motor", 'P', "plateBasic", 'R', "dustRedstone"});
		addShapedOreRecipe(NCBlocks.pressurizer_idle, new Object[] {"PTP", "ACA", "PTP", 'C', "chassis", 'P', "plateAdvanced", 'T', "ingotTough", 'A', "actuator"});
		addShapedOreRecipe(NCBlocks.salt_mixer_idle, new Object[] {"PSP", "BCB", "PMP", 'C', "chassis", 'P', "plateBasic", 'B', Items.BUCKET, 'M', "motor", 'S', "ingotSteel"});
		addShapedOreRecipe(NCBlocks.dissolver_idle, new Object[] {"PHP", "LCL", "PMP", 'C', "chassis", 'P', "plateAdvanced", 'L', "gemLapis", 'M', "motor", 'H', Blocks.HOPPER});
		addShapedOreRecipe(NCBlocks.chemical_reactor_idle, new Object[] {"PMP", "GCG", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'G', "dustGlowstone", 'M', "motor", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.electrolyser_idle, new Object[] {"PGP", "SCS", "PMP", 'C', "chassis", 'S', "solenoidCopper", 'P', "plateAdvanced", 'G', "ingotGraphite", 'M', "motor"});
		addShapedOreRecipe(NCBlocks.irradiator_idle, new Object[] {"PEP", "LCL", "PSP", 'C', "chassis", 'P', "plateAdvanced", 'E', Items.ENDER_PEARL, 'L', "solenoidCopper", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.supercooler_idle, new Object[] {"PDP", "HCH", "PSP", 'C', "chassis", 'D', "ingotMagnesiumDiboride", 'H', "ingotHardCarbon", 'P', "plateAdvanced", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.ingot_former_idle, new Object[] {"PHP", "FCF", "PTP", 'C', "chassis", 'P', "plateBasic", 'F', "ingotFerroboron", 'T', "ingotTough", 'H', Blocks.HOPPER});
		addShapedOreRecipe(NCBlocks.melter_idle, new Object[] {"PNP", "NCN", "PSP", 'C', "chassis", 'N', "ingotBrickNether", 'P', "plateAdvanced", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.crystallizer_idle, new Object[] {"PSP", "SCS", "PUP", 'C', "chassis", 'S', "solenoidCopper", 'U', Items.CAULDRON, 'P', "plateAdvanced"});
		addShapedOreRecipe(NCBlocks.infuser_idle, new Object[] {"PBP", "GCG", "PSP", 'C', "chassis", 'G', "ingotGold", 'S', "servo", 'P', "plateAdvanced", 'B', Items.BUCKET});
		addShapedOreRecipe(NCBlocks.extractor_idle, new Object[] {"PMP", "BCB", "PSP", 'C', "chassis", 'M', "ingotMagnesium", 'S', "servo", 'P', "plateAdvanced", 'B', Items.BUCKET});
		addShapedOreRecipe(NCBlocks.centrifuge_idle, new Object[] {"PFP", "MCM", "PSP", 'C', "chassis", 'M', "motor", 'P', "plateAdvanced", 'F', "ingotFerroboron", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.rock_crusher_idle, new Object[] {"PMP", "ACA", "PTP", 'C', "chassis", 'A', "actuator", 'P', "plateAdvanced", 'T', "ingotTough", 'M', "motor"});
		
		addShapedOreRecipe(NCBlocks.machine_interface, new Object[] {" A ", "MCM", " S ", 'C', "chassis", 'A', "actuator", 'M', "motor", 'S', "servo"});
		
		addShapedOreRecipe(NCBlocks.fission_controller_new_fixed, new Object[] {"PSP", "FCF", "PSP", 'C', "chassis", 'S', "solenoidMagnesiumDiboride", 'P', "plateAdvanced", 'F', NCBlocks.nuclear_furnace_idle});
		addShapelessOreRecipe(NCBlocks.fission_controller_new_fixed, new Object[] {NCBlocks.fission_controller_idle});
		
		addShapedOreRecipe(NCBlocks.buffer, new Object[] {"PSP", "BHB", "PSP", 'S', "solenoidCopper", 'P', "plateBasic", 'H', Blocks.HOPPER, 'B', Items.BUCKET});
		addShapedOreRecipe(NCBlocks.fission_port, new Object[] {" S ", "RHR", " S ", 'S', "solenoidCopper", 'R', new ItemStack(NCBlocks.fission_block, 1, 0), 'H', Blocks.HOPPER});
		addShapedOreRecipe(NCBlocks.active_cooler, new Object[] {"PSP", "BCB", "PSP", 'S', "ingotTin", 'P', "plateBasic", 'C', Items.CAULDRON, 'B', "ingotCopper"});
		
		addShapedOreRecipe(NCBlocks.fusion_core, new Object[] {"PSP", "RCR", "PSP", 'C', "chassis", 'S', "solenoidMagnesiumDiboride", 'P', "plateElite", 'R', NCBlocks.chemical_reactor_idle});
		
		addShapedOreRecipe(NCBlocks.fusion_connector, new Object[] {"TPT", "PCP", "TPT", 'T', "ingotTough", 'P', "plateBasic", 'C', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fusion_electromagnet_idle, 1), new Object[] {"SPS", "P P", "SPS", 'P', "plateAdvanced", 'S', "solenoidCopper"});
		addShapedOreRecipe(new ItemStack(NCBlocks.fusion_electromagnet_transparent_idle, 1), new Object[] {"SPS", "PGP", "SPS", 'P', "plateAdvanced", 'S', "solenoidCopper", 'G', "blockGlass"});
		
		addShapelessOreRecipe(NCBlocks.fusion_electromagnet_transparent_idle, new Object[] {NCBlocks.fusion_electromagnet_idle, "blockGlass"});
		addShapelessOreRecipe(NCBlocks.fusion_electromagnet_idle, new Object[] {NCBlocks.fusion_electromagnet_transparent_idle});
		
		addShapedOreRecipe(NCBlocks.rtg_uranium, new Object[] {"PGP", "GUG", "PGP", 'G', "ingotGraphite", 'P', "plateBasic", 'U', "blockUranium238"});
		addShapedOreRecipe(NCBlocks.rtg_plutonium, new Object[] {"PGP", "GUG", "PGP", 'G', "ingotGraphite", 'P', "plateAdvanced", 'U', "ingotPlutonium238"});
		addShapedOreRecipe(NCBlocks.rtg_americium, new Object[] {"PGP", "GAG", "PGP", 'G', "ingotGraphite", 'P', "plateAdvanced", 'A', "ingotAmericium241"});
		addShapedOreRecipe(NCBlocks.rtg_californium, new Object[] {"PGP", "GCG", "PGP", 'G', "ingotGraphite", 'P', "plateAdvanced", 'C', "ingotCalifornium250"});
		
		addShapedOreRecipe(NCBlocks.solar_panel_basic, new Object[] {"GQG", "PLP", "CPC", 'G', "dustGraphite", 'Q', "dustQuartz", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'L', "gemLapis", 'C', "solenoidCopper"});
		addShapedOreRecipe(NCBlocks.solar_panel_basic, new Object[] {"GQG", "PLP", "CPC", 'G', "dustGraphite", 'Q', "dustNetherQuartz", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'L', "gemLapis", 'C', "solenoidCopper"});
		
		addShapedOreRecipe(NCBlocks.solar_panel_advanced, new Object[] {"PGP", "SSS", "PCP", 'S', NCBlocks.solar_panel_basic, 'G', "dustGraphite", 'P', "plateAdvanced", 'C', "solenoidCopper"});
		addShapedOreRecipe(NCBlocks.solar_panel_du, new Object[] {"PGP", "SSS", "PMP", 'S', NCBlocks.solar_panel_advanced, 'G', "dustGraphite", 'P', "plateDU", 'M', "solenoidMagnesiumDiboride"});
		addShapedOreRecipe(NCBlocks.solar_panel_elite, new Object[] {"PBP", "SSS", "PMP", 'S', NCBlocks.solar_panel_du, 'B', "gemBoronArsenide", 'P', "plateElite", 'M', "solenoidMagnesiumDiboride"});
		
		addShapedOreRecipe(NCBlocks.decay_generator, new Object[] {"LCL", "CRC", "LCL", 'C', "cobblestone", 'L', "ingotLead", 'R', "dustRedstone"});
		
		addShapedOreRecipe(NCBlocks.voltaic_pile_basic, new Object[] {"PSP", "SMS", "PSP", 'P', "plateBasic", 'S', "solenoidCopper", 'M', "blockMagnesium"});
		
		addShapedOreRecipe(NCItems.lithium_ion_cell, new Object[] {"CCC", "FLF", "DDD", 'C', "ingotHardCarbon", 'F', "ingotFerroboron", 'L', "ingotLithium", 'D', "ingotLithiumManganeseDioxide"});
		addShapedOreRecipe(NCBlocks.lithium_ion_battery_basic, new Object[] {"PCP", "CSC", "PCP", 'C', NCItems.lithium_ion_cell, 'P', "plateElite", 'S', "solenoidMagnesiumDiboride"});
		
		addShapedOreRecipe(NCBlocks.bin, new Object[] {"PZP", "Z Z", "PZP", 'P', "plateBasic", 'Z', "ingotZirconium"});
		
		addShapedOreRecipe(NCBlocks.accelerator_electromagnet_idle, new Object[] {"SPS", "P P", "SPS", 'P', "plateElite", 'S', "solenoidMagnesiumDiboride"});
		addShapedOreRecipe(NCBlocks.electromagnet_supercooler_idle, new Object[] {"TIT", "IEI", "TIT", 'T', "ingotTin", 'I', NCBlocks.block_ice, 'E', NCBlocks.accelerator_electromagnet_idle});
		
		addShapedOreRecipe(NCBlocks.helium_collector, new Object[] {"PIP", "ITI", "PIP", 'I', "ingotZirconium", 'P', "plateBasic", 'T', "blockThorium230"});
		addShapedOreRecipe(NCBlocks.helium_collector_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.helium_collector, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.helium_collector_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.helium_collector_compact, 'I', "ingotGold"});
		
		addShapedOreRecipe(NCBlocks.cobblestone_generator, new Object[] {"PIP", "L W", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'L', Items.LAVA_BUCKET, 'W', Items.WATER_BUCKET});
		addShapedOreRecipe(NCBlocks.cobblestone_generator, new Object[] {"PIP", "W L", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'L', Items.LAVA_BUCKET, 'W', Items.WATER_BUCKET});
		addShapedOreRecipe(NCBlocks.cobblestone_generator_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.cobblestone_generator, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.cobblestone_generator_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.cobblestone_generator_compact, 'I', "ingotGold"});
		
		addShapedOreRecipe(NCBlocks.water_source, new Object[] {"PIP", "W W", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'W', Items.WATER_BUCKET});
		addShapedOreRecipe(NCBlocks.water_source_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.water_source, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.water_source_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.water_source_compact, 'I', "ingotGold"});
		
		addShapedOreRecipe(NCBlocks.nitrogen_collector, new Object[] {"PIP", "B B", "PIP", 'I', "ingotBeryllium", 'P', "plateAdvanced", 'B', Items.BUCKET});
		addShapedOreRecipe(NCBlocks.nitrogen_collector_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.nitrogen_collector, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.nitrogen_collector_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.nitrogen_collector_compact, 'I', "ingotGold"});
		
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 0), new Object[] {"LG", "GL", 'L', "ingotLead", 'G', "dustGraphite"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 0), new Object[] {"GL", "LG", 'L', "ingotLead", 'G', "dustGraphite"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 1), new Object[] {"RTR", "TPT", "RTR", 'R', "dustRedstone", 'T', "ingotTough", 'P', "plateBasic"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 2), new Object[] {"SUS", "UPU", "SUS", 'S', "dustSulfur", 'U', "ingotUranium238", 'P', "plateAdvanced"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 3), new Object[] {"RBR", "BPB", "RBR", 'R', "dustCrystalBinder", 'B', "ingotBoron", 'P', "plateDU"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 4), new Object[] {"CC", "II", "CC", 'C', "ingotCopper", 'I', "ingotIron"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 2, 5), new Object[] {"MM", "TT", "MM", 'M', "ingotMagnesiumDiboride", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 7), new Object[] {"F F", "RSR", "SCS", 'F', "ingotFerroboron", 'S', "ingotSteel", 'C', "ingotCopper", 'R', "dustRedstone"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 8), new Object[] {"SSG", "CCI", "SSG", 'G', "nuggetGold", 'S', "ingotSteel", 'I', "ingotIron", 'C', "solenoidCopper"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 9), new Object[] {"  S", "FP ", "CF ", 'F', "ingotFerroboron", 'S', "ingotSteel", 'P', Blocks.PISTON, 'C', "ingotCopper"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 10), new Object[] {"LSL", "STS", "LSL", 'L', "ingotLead", 'T', "ingotTough", 'S', "ingotSteel"});
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 11), new Object[] {"PTP", "I I", "PTP", 'P', "plateBasic", 'I', "ingotIron", 'T', "ingotTin"});
		
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 0), new Object[] {"LRL", "RPR", "LRL", 'L', "gemLapis", 'R', "dustRedstone", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE});
		
		addShapedOreRecipe(new ItemStack(NCItems.fuel_rod_empty, 32), new Object[] {" S ", "T T", " S ", 'S', "ingotSteel", 'T', "ingotTin"});
		
		blockCompress(new ItemStack(NCItems.dust, 1, 2), "dustLead", "tinyDustLead");
		//blockOpen(NCItems.tiny_dust_lead, "tinyDustLead", "dustLead");
		
		tools("ingotBoron", NCTools.sword_boron, NCTools.pickaxe_boron, NCTools.shovel_boron, NCTools.axe_boron, NCTools.hoe_boron, NCTools.spaxelhoe_boron);
		tools("ingotTough", NCTools.sword_tough, NCTools.pickaxe_tough, NCTools.shovel_tough, NCTools.axe_tough, NCTools.hoe_tough, NCTools.spaxelhoe_tough);
		tools("ingotHardCarbon", NCTools.sword_hard_carbon, NCTools.pickaxe_hard_carbon, NCTools.shovel_hard_carbon, NCTools.axe_hard_carbon, NCTools.hoe_hard_carbon, NCTools.spaxelhoe_hard_carbon);
		tools("gemBoronNitride", NCTools.sword_boron_nitride, NCTools.pickaxe_boron_nitride, NCTools.shovel_boron_nitride, NCTools.axe_boron_nitride, NCTools.hoe_boron_nitride, NCTools.spaxelhoe_boron_nitride);
		
		armor("ingotBoron", NCArmor.helm_boron, NCArmor.chest_boron, NCArmor.legs_boron, NCArmor.boots_boron);
		armor("ingotTough", NCArmor.helm_tough, NCArmor.chest_tough, NCArmor.legs_tough, NCArmor.boots_tough);
		armor("ingotHardCarbon", NCArmor.helm_hard_carbon, NCArmor.chest_hard_carbon, NCArmor.legs_hard_carbon, NCArmor.boots_hard_carbon);
		armor("gemBoronNitride", NCArmor.helm_boron_nitride, NCArmor.chest_boron_nitride, NCArmor.legs_boron_nitride, NCArmor.boots_boron_nitride);
		
		blockCompress(new ItemStack(NCItems.fuel_thorium, 1, 0), "fuelTBU", "ingotThorium232");
		blockCompress(new ItemStack(NCItems.fuel_thorium, 1, 1), "fuelTBUOxide", "ingotThorium232Oxide");
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_rod_thorium, 1, 0), new Object[] {NCItems.fuel_rod_empty, "fuelTBU"});
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_rod_thorium, 1, 1), new Object[] {NCItems.fuel_rod_empty, "fuelTBUOxide"});
		
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_mixed_oxide, 1, 0), new Object[] {"ingotPlutonium239Oxide", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238"});
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_mixed_oxide, 1, 1), new Object[] {"ingotPlutonium241Oxide", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238"});
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_rod_mixed_oxide, 1, 0), new Object[] {NCItems.fuel_rod_empty, "fuelMOX239"});
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_rod_mixed_oxide, 1, 1), new Object[] {NCItems.fuel_rod_empty, "fuelMOX241"});
		
		fissionClumpRecipes("Thorium", NCItems.thorium, 230, 232);
		fissionClumpRecipes("Uranium", NCItems.uranium, 233, 235, 238);
		fissionClumpRecipes("Neptunium", NCItems.neptunium, 236, 237);
		fissionClumpRecipes("Plutonium", NCItems.plutonium, 238, 239, 241, 242);
		fissionClumpRecipes("Americium", NCItems.americium, 241, 242, 243);
		fissionClumpRecipes("Curium", NCItems.curium, 243, 245, 246, 247);
		fissionClumpRecipes("Berkelium", NCItems.berkelium, 247, 248);
		fissionClumpRecipes("Californium", NCItems.californium, 249, 250, 251, 252);
		
		fissionFuelRecipes("Uranium", "U", NCItems.fuel_uranium, NCItems.fuel_rod_uranium, 238, 233, 235);
		fissionFuelRecipes("Neptunium", "N", NCItems.fuel_neptunium, NCItems.fuel_rod_neptunium, 237, 236);
		fissionFuelRecipes("Plutonium", "P", NCItems.fuel_plutonium, NCItems.fuel_rod_plutonium, 242, 239, 241);
		fissionFuelRecipes("Americium", "A", NCItems.fuel_americium, NCItems.fuel_rod_americium, 243, 242);
		fissionFuelRecipes("Curium", "Cm", NCItems.fuel_curium, NCItems.fuel_rod_curium, 246, 243, 245, 247);
		fissionFuelRecipes("Berkelium", "B", NCItems.fuel_berkelium, NCItems.fuel_rod_berkelium, 247, 248);
		fissionFuelRecipes("Californium", "Cf", NCItems.fuel_californium, NCItems.fuel_rod_californium, 252, 249, 251);
		
		tinyClumpRecipes("Boron", NCItems.boron, 10, 11);
		tinyClumpRecipes("Lithium", NCItems.lithium, 6, 7);
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 1), new Object[] {"dustRhodochrosite", "dustCalciumSulfate", "dustObsidian", "dustMagnesium"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 2), new Object[] {"dustRedstone", "dustGlowstone"});
		
		addShapedOreRecipe(NCItems.portable_ender_chest, new Object[] {" S ", "WCW", "LWL", 'C', "chestEnder", 'W', new ItemStack(Blocks.WOOL, 1, 10), 'S', "string", 'L', "ingotTough"});
		addShapedOreRecipe(NCItems.portable_ender_chest, new Object[] {" S ", "WCW", "LWL", 'C', "chestEnder", 'W', new ItemStack(Blocks.WOOL, 1, 15), 'S', "string", 'L', "ingotTough"});
		
		addShapelessOreRecipe(new ItemStack(NCItems.dominos, 4), new Object[] {Items.BREAD, Items.BREAD, Items.BREAD, Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM});
		addShapelessOreRecipe(Blocks.BROWN_MUSHROOM, new Object[] {NCBlocks.glowing_mushroom});
		addShapelessOreRecipe(NCBlocks.glowing_mushroom, new Object[] {Blocks.BROWN_MUSHROOM, "dustGlowstone"});
		
		addShapedOreRecipe(new ItemStack(Items.COOKIE, 12), new Object[] {"FCF", 'F', "dustWheat", 'C', "dustCocoa"});
		addShapelessOreRecipe(NCItems.smore, new Object[] {NCItems.graham_cracker, NCItems.milk_chocolate, NCItems.marshmallow, NCItems.graham_cracker});
		addShapelessOreRecipe(NCItems.moresmore, new Object[] {NCItems.smore, NCItems.milk_chocolate, NCItems.marshmallow, NCItems.smore});
		
		if (NCConfig.radiation_enabled) {
			addShapedOreRecipe(NCItems.geiger_counter, new Object[] {"SFF", "CRR", "BFF", 'S', "ingotSteel", 'F', "ingotFerroboron", 'C', "ingotCopper", 'R', "dustRedstone", 'B', "bioplastic"});
			
			addShapedOreRecipe(NCItems.rad_x, new Object[] {"EPE", "PRP", "PBP", 'E', "dustEnergetic", 'P', "bioplastic", 'R', NCItems.radaway, 'B', Items.BLAZE_POWDER});
			
			addShapedOreRecipe(NCBlocks.radiation_scrubber, new Object[] {"PCP", "CEC", "PCP", 'P', "plateElite", 'E', "ingotExtreme", 'C', "dustCalciumSulfate"});
			
			addShapedOreRecipe(new ItemStack(NCItems.rad_shielding, 1, 0), new Object[] {"III", "CCC", "LLL", 'I', "ingotIron", 'C', "coal", 'L', "ingotLead"});
			addShapedOreRecipe(new ItemStack(NCItems.rad_shielding, 1, 1), new Object[] {"BBB", "RFR", "PPP", 'B', "bioplastic", 'F', "ingotFerroboron", 'P', "plateBasic", 'R', new ItemStack(NCItems.rad_shielding, 1, 0)});
			addShapedOreRecipe(new ItemStack(NCItems.rad_shielding, 1, 2), new Object[] {"BBB", "RHR", "PPP", 'B', "ingotBeryllium", 'H', "ingotHardCarbon", 'P', "plateDU", 'R', new ItemStack(NCItems.rad_shielding, 1, 1)});
		}
		
		addShapelessOreRecipe(NCItems.record_wanderer, new Object[] {"record", "ingotTough"});
		addShapelessOreRecipe(NCItems.record_end_of_the_world, new Object[] {"record", "ingotUranium"});
		addShapelessOreRecipe(NCItems.record_money_for_nothing, new Object[] {"record", "ingotBronze"});
		addShapelessOreRecipe(NCItems.record_hyperspace, new Object[] {"record", "ingotZirconium"});
		
		if (NCConfig.radiation_enabled) {
			if (NCConfig.radiation_shielding_default_recipes) for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
				if (item instanceof ItemArmor && !RadiationArmor.ARMOR_ITEM_SHIELDING_BLACKLIST.contains(item)) {
					RadiationArmor.addArmorShieldingRecipes(item);
				}
			}
			
			for (ItemStack stack : RadiationArmor.ARMOR_STACK_SHIELDING_LIST) {
				RadiationArmor.addArmorShieldingRecipes(stack);
			}
			
			addShapedOreRecipe(NCArmor.helm_hazmat, new Object[] {"YWY", "SLS", "BIB", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_HELMET, 'B', "bioplastic", 'I', "ingotSteel", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
			addShapedOreRecipe(NCArmor.chest_hazmat, new Object[] {"WSW", "YLY", "SWS", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_CHESTPLATE, 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
			addShapedOreRecipe(NCArmor.legs_hazmat, new Object[] {"YBY", "SLS", "W W", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_LEGGINGS, 'B', "bioplastic", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
			addShapedOreRecipe(NCArmor.boots_hazmat, new Object[] {"SDS", "BLB", 'D', "dyeBlack", 'L', Items.LEATHER_BOOTS, 'B', "bioplastic", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		}
	}
	
	public static void fissionFuelRecipes(String element, String fuelLetter, Item fuelType, Item rodType, int fertileNo, int... fissileNo) {
		for (int i = 0; i < fissileNo.length; i++) {
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 4*i), new Object[] {"ingot" + element + fissileNo[i], "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 1 + 4*i), new Object[] {"ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			addShapelessOreRecipe(new ItemStack(rodType, 1, 4*i), new Object[] {NCItems.fuel_rod_empty, "fuelLE" + fuelLetter + fissileNo[i]});
			addShapelessOreRecipe(new ItemStack(rodType, 1, 1 + 4*i), new Object[] {NCItems.fuel_rod_empty, "fuelLE" + fuelLetter + fissileNo[i] + "Oxide"});
			
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 2 + 4*i), new Object[] {"ingot" + element + fissileNo[i], "ingot" + element + fissileNo[i], "ingot" + element + fissileNo[i], "ingot" + element + fissileNo[i], "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 3 + 4*i), new Object[] {"ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			addShapelessOreRecipe(new ItemStack(rodType, 1, 2 + 4*i), new Object[] {NCItems.fuel_rod_empty, "fuelHE" + fuelLetter + fissileNo[i]});
			addShapelessOreRecipe(new ItemStack(rodType, 1, 3 + 4*i), new Object[] {NCItems.fuel_rod_empty, "fuelHE" + fuelLetter + fissileNo[i] + "Oxide"});
		}	
	}
	
	public static void fissionClumpRecipes(String element, Item material, int... types) {
		for (int i = 0; i < types.length; i++) {
			blockCompress(new ItemStack(material, 1, 4*i), "ingot" + element + types[i] + "Base", "nugget" + element + types[i]);
			blockCompress(new ItemStack(material, 1, 1 + 4*i), "ingot" + element + types[i] + "Oxide", "nugget" + element + types[i] + "Oxide");
			blockOpen(material, 2 + 4*i, "nugget" + element + types[i], "ingot" + element + types[i] + "Base");
			blockOpen(material, 3 + 4*i, "nugget" + element + types[i] + "Oxide", "ingot" + element + types[i] + "Oxide");
		}
	}
	
	public static void tinyClumpRecipes(String element, Item material, int... types) {
		for (int i = 0; i < types.length; i++) {
			blockCompress(new ItemStack(material, 1, 2*i), "ingot" + element + types[i], "nugget" + element + types[i]);
			blockOpen(material, 1 + 2*i, "nugget" + element + types[i], "ingot" + element + types[i]);
		}
	}
	
	public static void blockCompress(ItemStack itemOut, String itemOutOreName, Object itemIn) {
		addShapedOreRecipe(OreDictHelper.getPrioritisedCraftingStack(itemOut, itemOutOreName), new Object[] {"III", "III", "III", 'I', itemIn});
	}
	
	public static void blockCompress(Item itemOut, String itemOutOreName, Object itemIn) {
		addShapedOreRecipe(OreDictHelper.getPrioritisedCraftingStack(itemOut, itemOutOreName), new Object[] {"III", "III", "III", 'I', itemIn});
	}
	
	public static void blockCompress(Block itemOut, String itemOutOreName, Object itemIn) {
		addShapedOreRecipe(OreDictHelper.getPrioritisedCraftingStack(itemOut, itemOutOreName), new Object[] {"III", "III", "III", 'I', itemIn});
	}
	
	public static void blockOpen(Item itemOut, int metaOut, String itemOutOreName, Object itemIn) {
		addShapelessOreRecipe(OreDictHelper.getPrioritisedCraftingStack(new ItemStack(itemOut, 9, metaOut), itemOutOreName), new Object[] {itemIn});
	}
	
	public static void blockOpen(Item itemOut, String itemOutOreName, Object itemIn) {
		addShapelessOreRecipe(OreDictHelper.getPrioritisedCraftingStack(new ItemStack(itemOut, 9, 0), itemOutOreName), new Object[] {itemIn});
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
	
	private static final Map<String, Integer> RECIPE_COUNT_MAP = new HashMap<String, Integer>();
	
	public static void addShapedOreRecipe(Object out, Object... inputs) {
		if (out == null || Arrays.asList(inputs).contains(null)) return;
		ItemStack outStack = ItemStackHelper.fixItemStack(out);
		if (!outStack.isEmpty() && inputs != null) {
			String outName = outStack.getUnlocalizedName();
			if (RECIPE_COUNT_MAP.containsKey(outName)) {
				int count = RECIPE_COUNT_MAP.get(outName);
				RECIPE_COUNT_MAP.put(outName, count + 1);
				outName = outName + "_" + count;	
			} else RECIPE_COUNT_MAP.put(outName, 1);
			ResourceLocation location = new ResourceLocation(Global.MOD_ID, outName);
			ShapedOreRecipe oreRecipe = new ShapedOreRecipe(location, outStack, inputs);
			oreRecipe.setRegistryName(location);
			ForgeRegistries.RECIPES.register(oreRecipe);
		}
	}
	
	public static void addShapelessOreRecipe(Object out, Object... inputs) {
		if (out == null || Arrays.asList(inputs).contains(null)) return;
		ItemStack outStack = ItemStackHelper.fixItemStack(out);
		if (!outStack.isEmpty() && inputs != null) {
			String outName = outStack.getUnlocalizedName();
			if (RECIPE_COUNT_MAP.containsKey(outName)) {
				int count = RECIPE_COUNT_MAP.get(outName);
				RECIPE_COUNT_MAP.put(outName, count + 1);
				outName = outName + "_" + count;	
			} else RECIPE_COUNT_MAP.put(outName, 1);
			ResourceLocation location = new ResourceLocation(Global.MOD_ID, outName);
			ShapelessOreRecipe oreRecipe = new ShapelessOreRecipe(location, outStack, inputs);
			oreRecipe.setRegistryName(location);
			ForgeRegistries.RECIPES.register(oreRecipe);
		}
	}
	
	public static void addShapelessArmorUpgradeOreRecipe(Object out, Object... inputs) {
		if (out == null || Arrays.asList(inputs).contains(null)) return;
		ItemStack outStack = ItemStackHelper.fixItemStack(out);
		if (!outStack.isEmpty() && inputs != null) {
			String outName = outStack.getUnlocalizedName();
			if (RECIPE_COUNT_MAP.containsKey(outName)) {
				int count = RECIPE_COUNT_MAP.get(outName);
				RECIPE_COUNT_MAP.put(outName, count + 1);
				outName = outName + "_" + count;	
			} else RECIPE_COUNT_MAP.put(outName, 1);
			ResourceLocation location = new ResourceLocation(Global.MOD_ID, outName);
			ShapelessArmorUpgradeOreRecipe oreRecipe = new ShapelessArmorUpgradeOreRecipe(location, outStack, inputs);
			oreRecipe.setRegistryName(location);
			ForgeRegistries.RECIPES.register(oreRecipe);
		}
	}
}
