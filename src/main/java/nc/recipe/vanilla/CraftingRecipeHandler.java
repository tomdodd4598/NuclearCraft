package nc.recipe.vanilla;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nc.Global;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.enumm.MetaEnums.IngotType;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.init.NCTools;
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
			if (!NCConfig.ore_dict_raw_material_recipes) {
				blockCompress(NCBlocks.ingot_block, i, "block" + type, new ItemStack(NCItems.ingot, 1, i));
			}
			else for (ItemStack ingot : OreDictionary.getOres("ingot" + type)) {
				blockCompress(NCBlocks.ingot_block, i, "block" + type, ingot);
			}
			
			if (!NCConfig.ore_dict_raw_material_recipes) {
				blockOpen(NCItems.ingot, i, "ingot" + type, new ItemStack(NCBlocks.ingot_block, 1, i));
			}
			else for (ItemStack block : OreDictionary.getOres("block" + type)) {
				blockOpen(NCItems.ingot, i, "ingot" + type, block);
			}
		}
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_block, 4, 0), new Object[] {" P ", "PTP", " P ", 'P', "plateBasic", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCBlocks.reactor_casing_transparent, 4), new Object[] {"GPG", "PTP", "GPG", 'P', "plateBasic", 'T', "ingotTough", 'G', "blockGlass"});
		addShapelessOreRecipe(new ItemStack(NCBlocks.fission_block, 1, 0), new Object[] {NCBlocks.reactor_casing_transparent});
		addShapelessOreRecipe(NCBlocks.reactor_casing_transparent, new Object[] {new ItemStack(NCBlocks.fission_block, 1, 0), "blockGlass"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.fission_block, 4, 1), new Object[] {" P ", "POP", " P ", 'P', "plateBasic", 'O', "obsidian"});
		addShapedOreRecipe(NCBlocks.cell_block, new Object[] {"TGT", "G G", "TGT", 'T', "ingotTough", 'G', "blockGlass"});
		
		addShapedOreRecipe(new ItemStack(NCBlocks.cooler, 2, 0), new Object[] {"TIT", "I I", "TIT", 'T', "ingotTough", 'I', "ingotSteel"});
		addShapelessFluidRecipe(new ItemStack(NCBlocks.cooler, 1, 1), new Object[] {new ItemStack(NCBlocks.cooler, 1, 0), new BucketIngredient("water")});
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
		addShapelessFluidRecipe(new ItemStack(NCBlocks.cooler, 1, 8), new Object[] {new ItemStack(NCBlocks.cooler, 1, 0), new BucketIngredient("liquidhelium")});
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
		
		blockCompress(NCBlocks.block_depleted_thorium, 0, "blockThorium230", "ingotThorium230");
		blockCompress(NCBlocks.block_depleted_uranium, 0, "blockUranium238", "ingotUranium238");
		blockCompress(NCBlocks.block_depleted_neptunium, 0, "blockNeptunium237", "ingotNeptunium237");
		blockCompress(NCBlocks.block_depleted_plutonium, 0, "blockPlutonium242", "ingotPlutonium242");
		blockCompress(NCBlocks.block_depleted_americium, 0, "blockAmericium243", "ingotAmericium243");
		blockCompress(NCBlocks.block_depleted_curium, 0, "blockCurium246", "ingotCurium246");
		blockCompress(NCBlocks.block_depleted_berkelium, 0, "blockBerkelium247", "ingotBerkelium247");
		blockCompress(NCBlocks.block_depleted_californium, 0, "blockCalifornium252", "ingotCalifornium252");
		
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
		addShapedEnergyRecipe(NCBlocks.voltaic_pile_advanced, new Object[] {"PMP", "VVV", "PCP", 'V', NCBlocks.voltaic_pile_basic, 'P', "plateAdvanced", 'M', "ingotMagnesium", 'C', "ingotCopper"});
		addShapedEnergyRecipe(NCBlocks.voltaic_pile_du, new Object[] {"PMP", "VVV", "PCP", 'V', NCBlocks.voltaic_pile_advanced, 'P', "plateDU", 'M', "ingotMagnesium", 'C', "ingotCopper"});
		addShapedEnergyRecipe(NCBlocks.voltaic_pile_elite, new Object[] {"PMP", "VVV", "PCP", 'V', NCBlocks.voltaic_pile_du, 'P', "plateElite", 'M', "ingotMagnesium", 'C', "ingotCopper"});
		
		addShapedOreRecipe(NCItems.lithium_ion_cell, new Object[] {"CCC", "FLF", "DDD", 'C', "ingotHardCarbon", 'F', "ingotFerroboron", 'L', "ingotLithium", 'D', "ingotLithiumManganeseDioxide"});
		addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_basic, new Object[] {"PCP", "CSC", "PCP", 'C', NCItems.lithium_ion_cell, 'P', "plateElite", 'S', "solenoidMagnesiumDiboride"});
		addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_advanced, new Object[] {"PDP", "LLL", "PSP", 'L', NCBlocks.lithium_ion_battery_basic, 'P', "plateAdvanced", 'D', "ingotLithiumManganeseDioxide", 'S', "solenoidMagnesiumDiboride"});
		addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_du, new Object[] {"PDP", "LLL", "PSP", 'L', NCBlocks.lithium_ion_battery_advanced, 'P', "plateDU", 'D', "ingotLithiumManganeseDioxide", 'S', "solenoidMagnesiumDiboride"});
		addShapedEnergyRecipe(NCBlocks.lithium_ion_battery_elite, new Object[] {"PDP", "LLL", "PSP", 'L', NCBlocks.lithium_ion_battery_du, 'P', "plateElite", 'D', "ingotLithiumManganeseDioxide", 'S', "solenoidMagnesiumDiboride"});
		
		addShapedOreRecipe(NCBlocks.bin, new Object[] {"PZP", "Z Z", "PZP", 'P', "plateBasic", 'Z', "ingotZirconium"});
		
		addShapedOreRecipe(NCBlocks.accelerator_electromagnet_idle, new Object[] {"SPS", "P P", "SPS", 'P', "plateElite", 'S', "solenoidMagnesiumDiboride"});
		addShapedOreRecipe(NCBlocks.electromagnet_supercooler_idle, new Object[] {"TIT", "IEI", "TIT", 'T', "ingotTin", 'I', NCBlocks.block_ice, 'E', NCBlocks.accelerator_electromagnet_idle});
		
		addShapedOreRecipe(NCBlocks.helium_collector, new Object[] {"PIP", "ITI", "PIP", 'I', "ingotZirconium", 'P', "plateBasic", 'T', "blockThorium230"});
		addShapedOreRecipe(NCBlocks.helium_collector_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.helium_collector, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.helium_collector_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.helium_collector_compact, 'I', "ingotGold"});
		
		addShapedFluidRecipe(NCBlocks.cobblestone_generator, new Object[] {"PIP", "L W", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'L', new BucketIngredient("lava"), 'W', new BucketIngredient("water")});
		addShapedFluidRecipe(NCBlocks.cobblestone_generator, new Object[] {"PIP", "W L", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'L', new BucketIngredient("lava"), 'W', new BucketIngredient("water")});
		addShapedOreRecipe(NCBlocks.cobblestone_generator_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.cobblestone_generator, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.cobblestone_generator_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.cobblestone_generator_compact, 'I', "ingotGold"});
		
		addShapedFluidRecipe(NCBlocks.water_source, new Object[] {"PIP", "W W", "PIP", 'I', "ingotTin", 'P', "plateBasic", 'W', new BucketIngredient("water")});
		addShapedOreRecipe(NCBlocks.water_source_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.water_source, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.water_source_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.water_source_compact, 'I', "ingotGold"});
		
		addShapedOreRecipe(NCBlocks.nitrogen_collector, new Object[] {"PIP", "B B", "PIP", 'I', "ingotBeryllium", 'P', "plateAdvanced", 'B', Items.BUCKET});
		addShapedOreRecipe(NCBlocks.nitrogen_collector_compact, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.nitrogen_collector, 'I', "ingotBronze"});
		addShapedOreRecipe(NCBlocks.nitrogen_collector_dense, new Object[] {"CCC", "CIC", "CCC", 'C', NCBlocks.nitrogen_collector_compact, 'I', "ingotGold"});
		
		addShapedOreRecipe(NCBlocks.salt_fission_controller, new Object[] {"PEP", "HFH", "PEP", 'P', "plateElite", 'E', "ingotExtreme", 'H', "ingotHardCarbon", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_wall, 4), new Object[] {"STS", "TFT", "STS", 'S', "ingotSteel", 'T', "ingotTough", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.salt_fission_wall, new Object[] {NCBlocks.salt_fission_glass});
		addShapelessOreRecipe(NCBlocks.salt_fission_glass, new Object[] {NCBlocks.salt_fission_wall, "blockGlass"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_frame, 4), new Object[] {"SWS", "W W", "SWS", 'S', "ingotSteel", 'W', NCBlocks.salt_fission_wall});
		addShapelessOreRecipe(NCBlocks.salt_fission_beam, new Object[] {NCBlocks.salt_fission_wall});
		addShapelessOreRecipe(NCBlocks.salt_fission_wall, new Object[] {NCBlocks.salt_fission_beam});
		addShapedOreRecipe(NCBlocks.salt_fission_moderator, new Object[] {"PSP", "SGS", "PSP", 'S', "ingotSteel", 'P', "plateElite", 'G', "blockGraphite"});
		addShapedOreRecipe(NCBlocks.salt_fission_moderator, new Object[] {"PSP", "SBS", "PSP", 'S', "ingotSteel", 'P', "plateElite", 'B', "blockBeryllium"});
		addShapedOreRecipe(new ItemStack(NCBlocks.salt_fission_vent, 4), new Object[] {"STS", "VFV", "STS", 'S', "ingotSteel", 'T', "ingotTough", 'V', "servo", 'F', "steelFrame"});
		addShapedOreRecipe(NCBlocks.salt_fission_vessel, new Object[] {"PTP", "ZFZ", "PSP", 'P', "plateElite", 'T', "ingotTough", 'Z', "ingotZircaloy", 'F', "steelFrame", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.salt_fission_heater, new Object[] {"PEP", "TFT", "PSP", 'P', "plateElite", 'T', "ingotThermoconducting", 'E', "ingotExtreme", 'F', "steelFrame", 'S', "servo"});
		addShapedOreRecipe(NCBlocks.salt_fission_distributor, new Object[] {"PEP", "NVN", "PEP", 'P', "plateElite", 'E', "ingotExtreme", 'N', Items.ENDER_EYE, 'V', NCBlocks.salt_fission_vent});
		addShapedOreRecipe(NCBlocks.salt_fission_retriever, new Object[] {"PTP", "NVN", "PTP", 'P', "plateElite", 'T', "ingotTough", 'N', Items.ENDER_EYE, 'V', NCBlocks.salt_fission_vent});
		addShapedOreRecipe(NCBlocks.salt_fission_redstone_port, new Object[] {"SCS", "RFR", "SCS", 'S', "ingotSteel", 'C', Items.COMPARATOR, 'R', Items.REPEATER, 'F', "steelFrame"});
		if (ModCheck.openComputersLoaded()) {
			addShapedOreRecipe(NCBlocks.salt_fission_computer_port, new Object[] {" M ", "CWC", " P ", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'P', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'W', NCBlocks.salt_fission_wall});
		}
		
		addShapedOreRecipe(NCBlocks.heat_exchanger_controller, new Object[] {"PTP", "SFS", "PTP", 'P', "plateAdvanced", 'S', "ingotSteel", 'T', "ingotThermoconducting", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_wall, 4), new Object[] {"SNS", "NFN", "SNS", 'S', "ingotSteel", 'N', "stone", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_wall, new Object[] {NCBlocks.heat_exchanger_glass});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_glass, new Object[] {NCBlocks.heat_exchanger_wall, "blockGlass"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_frame, 4), new Object[] {"SWS", "W W", "SWS", 'S', "ingotSteel", 'W', NCBlocks.heat_exchanger_wall});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_vent, 4), new Object[] {"SNS", "VFV", "SNS", 'S', "ingotSteel", 'N', "stone", 'V', "servo", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_tube_copper, 4), new Object[] {"PCP", "CFC", "PSP", 'P', "plateBasic", 'C', "ingotCopper", 'F', "steelFrame", 'S', "servo"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_tube_hard_carbon, 4), new Object[] {"PHP", "HFH", "PSP", 'P', "plateAdvanced", 'H', "ingotHardCarbon", 'F', "steelFrame", 'S', "servo"});
		addShapedOreRecipe(new ItemStack(NCBlocks.heat_exchanger_tube_thermoconducting, 4), new Object[] {"PTP", "TFT", "PSP", 'P', "plateElite", 'T', "ingotThermoconducting", 'F', "steelFrame", 'S', "servo"});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_tube_copper, new Object[] {NCBlocks.heat_exchanger_condenser_tube_copper});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_tube_hard_carbon, new Object[] {NCBlocks.heat_exchanger_condenser_tube_hard_carbon});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_tube_thermoconducting, new Object[] {NCBlocks.heat_exchanger_condenser_tube_thermoconducting});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_condenser_tube_copper, new Object[] {NCBlocks.heat_exchanger_tube_copper});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_condenser_tube_hard_carbon, new Object[] {NCBlocks.heat_exchanger_tube_hard_carbon});
		addShapelessOreRecipe(NCBlocks.heat_exchanger_condenser_tube_thermoconducting, new Object[] {NCBlocks.heat_exchanger_tube_thermoconducting});
		if (ModCheck.openComputersLoaded()) {
			addShapedOreRecipe(NCBlocks.heat_exchanger_computer_port, new Object[] {" M ", "CWC", " P ", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'P', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'W', NCBlocks.heat_exchanger_wall});
		}
		
		addShapedOreRecipe(NCBlocks.turbine_controller, new Object[] {"PSP", "SFS", "PSP", 'P', "plateAdvanced", 'S', "ingotHSLASteel", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_wall, 4), new Object[] {"SSS", "SFS", "SSS", 'S', "ingotHSLASteel", 'F', "steelFrame"});
		addShapelessOreRecipe(NCBlocks.turbine_wall, new Object[] {NCBlocks.turbine_glass});
		addShapelessOreRecipe(NCBlocks.turbine_glass, new Object[] {NCBlocks.turbine_wall, "blockGlass"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_frame, 4), new Object[] {"SWS", "W W", "SWS", 'S', "ingotHSLASteel", 'W', NCBlocks.turbine_wall});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_shaft, 4), new Object[] {"STS", "STS", "STS", 'S', "ingotHSLASteel", 'T', "ingotTough"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_blade_steel, 4), new Object[] {"SSS", "HHH", "SSS", 'S', "ingotSteel", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_blade_extreme, 4), new Object[] {"EEE", "HHH", "EEE", 'E', "ingotExtreme", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_blade_sic_sic_cmc, 4), new Object[] {"SSS", "HHH", "SSS", 'S', "ingotSiCSiCCMC", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_rotor_stator, 4), new Object[] {"SSS", "SSS", 'S', "ingotHSLASteel"});
		addShapedOreRecipe(NCBlocks.turbine_rotor_bearing, new Object[] {"SGS", "GFG", "SGS", 'G', "nuggetGold", 'S', "ingotHSLASteel", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, 0), new Object[] {"MMM", "HTH", "MMM", 'M', "ingotMagnesium", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, 1), new Object[] {"BBB", "HTH", "BBB", 'B', "ingotBeryllium", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, 2), new Object[] {"AAA", "HTH", "AAA", 'A', "ingotAluminum", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, 3), new Object[] {"GGG", "HTH", "GGG", 'G', "ingotGold", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, 4), new Object[] {"CCC", "HTH", "CCC", 'C', "ingotCopper", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, 5), new Object[] {"SSS", "HTH", "SSS", 'S', "ingotSilver", 'T', "ingotTough", 'H', "ingotHSLASteel"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_inlet, 4), new Object[] {"SSS", "TFT", "SVS", 'S', "ingotHSLASteel", 'T', "ingotTough", 'V', "servo", 'F', "steelFrame"});
		addShapedOreRecipe(new ItemStack(NCBlocks.turbine_outlet, 4), new Object[] {"SSS", "VFV", "SSS", 'S', "ingotHSLASteel", 'V', "servo", 'F', "steelFrame"});
		if (ModCheck.openComputersLoaded()) {
			addShapedOreRecipe(NCBlocks.turbine_computer_port, new Object[] {" M ", "CWC", " P ", 'M', RegistryHelper.itemStackFromRegistry("opencomputers:material:7"), 'C', RegistryHelper.blockStackFromRegistry("opencomputers:cable:0"), 'P', RegistryHelper.itemStackFromRegistry("opencomputers:material:4"), 'W', NCBlocks.turbine_wall});
		}
		
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
		addShapedOreRecipe(new ItemStack(NCItems.part, 1, 12), new Object[] {"STS", "TBT", "STS", 'S', "ingotSteel", 'B', "ingotBronze", 'T', "ingotTough"});
		
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 0), new Object[] {"LRL", "RPR", "LRL", 'L', "gemLapis", 'R', "dustRedstone", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE});
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 1), new Object[] {"OQO", "QPQ", "OQO", 'O', "dustObsidian", 'Q', "dustQuartz", 'P', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE});
		addShapedOreRecipe(new ItemStack(NCItems.upgrade, 1, 1), new Object[] {"OQO", "QPQ", "OQO", 'O', "dustObsidian", 'Q', "dustNetherQuartz", 'P', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE});
		
		itemCompress(NCItems.dust, 2, "dustLead", "tinyDustLead");
		//blockOpen(NCItems.tiny_dust_lead, "tinyDustLead", "dustLead");
		
		tools("ingotBoron", NCTools.sword_boron, NCTools.pickaxe_boron, NCTools.shovel_boron, NCTools.axe_boron, NCTools.hoe_boron, NCTools.spaxelhoe_boron);
		tools("ingotTough", NCTools.sword_tough, NCTools.pickaxe_tough, NCTools.shovel_tough, NCTools.axe_tough, NCTools.hoe_tough, NCTools.spaxelhoe_tough);
		tools("ingotHardCarbon", NCTools.sword_hard_carbon, NCTools.pickaxe_hard_carbon, NCTools.shovel_hard_carbon, NCTools.axe_hard_carbon, NCTools.hoe_hard_carbon, NCTools.spaxelhoe_hard_carbon);
		tools("gemBoronNitride", NCTools.sword_boron_nitride, NCTools.pickaxe_boron_nitride, NCTools.shovel_boron_nitride, NCTools.axe_boron_nitride, NCTools.hoe_boron_nitride, NCTools.spaxelhoe_boron_nitride);
		
		armor("ingotBoron", NCArmor.helm_boron, NCArmor.chest_boron, NCArmor.legs_boron, NCArmor.boots_boron);
		armor("ingotTough", NCArmor.helm_tough, NCArmor.chest_tough, NCArmor.legs_tough, NCArmor.boots_tough);
		armor("ingotHardCarbon", NCArmor.helm_hard_carbon, NCArmor.chest_hard_carbon, NCArmor.legs_hard_carbon, NCArmor.boots_hard_carbon);
		armor("gemBoronNitride", NCArmor.helm_boron_nitride, NCArmor.chest_boron_nitride, NCArmor.legs_boron_nitride, NCArmor.boots_boron_nitride);
		
		itemCompress(NCItems.fuel_thorium, 0, "fuelTBU", "ingotThorium232");
		itemCompress(NCItems.fuel_thorium, 1, "fuelTBUOxide", "ingotThorium232Oxide");
		
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_mixed_oxide, 1, 0), new Object[] {"ingotPlutonium239Oxide", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238"});
		addShapelessOreRecipe(new ItemStack(NCItems.fuel_mixed_oxide, 1, 1), new Object[] {"ingotPlutonium241Oxide", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238", "ingotUranium238"});
		
		fissionClumpRecipes("Thorium", NCItems.thorium, 230, 232);
		fissionClumpRecipes("Uranium", NCItems.uranium, 233, 235, 238);
		fissionClumpRecipes("Neptunium", NCItems.neptunium, 236, 237);
		fissionClumpRecipes("Plutonium", NCItems.plutonium, 238, 239, 241, 242);
		fissionClumpRecipes("Americium", NCItems.americium, 241, 242, 243);
		fissionClumpRecipes("Curium", NCItems.curium, 243, 245, 246, 247);
		fissionClumpRecipes("Berkelium", NCItems.berkelium, 247, 248);
		fissionClumpRecipes("Californium", NCItems.californium, 249, 250, 251, 252);
		
		fissionFuelRecipes("Uranium", "U", NCItems.fuel_uranium, 238, 233, 235);
		fissionFuelRecipes("Neptunium", "N", NCItems.fuel_neptunium, 237, 236);
		fissionFuelRecipes("Plutonium", "P", NCItems.fuel_plutonium, 242, 239, 241);
		fissionFuelRecipes("Americium", "A", NCItems.fuel_americium, 243, 242);
		fissionFuelRecipes("Curium", "Cm", NCItems.fuel_curium, 246, 243, 245, 247);
		fissionFuelRecipes("Berkelium", "B", NCItems.fuel_berkelium, 247, 248);
		fissionFuelRecipes("Californium", "Cf", NCItems.fuel_californium, 252, 249, 251);
		
		tinyClumpRecipes("Boron", NCItems.boron, 10, 11);
		tinyClumpRecipes("Lithium", NCItems.lithium, 6, 7);
		
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 1), new Object[] {"dustRhodochrosite", "dustCalciumSulfate", "dustObsidian", "dustMagnesium"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 2), new Object[] {"dustRedstone", "dustGlowstone"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 8), new Object[] {"dustObsidian", "dustObsidian", "dustObsidian", "dustObsidian", "dustEndstone"});
		addShapelessOreRecipe(new ItemStack(NCItems.compound, 2, 9), new Object[] {"dustGraphite", "dustManganese"});
		
		addShapedOreRecipe(NCItems.portable_ender_chest, new Object[] {" S ", "WCW", "LWL", 'C', "chestEnder", 'W', new ItemStack(Blocks.WOOL, 1, 10), 'S', "string", 'L', "ingotTough"});
		addShapedOreRecipe(NCItems.portable_ender_chest, new Object[] {" S ", "WCW", "LWL", 'C', "chestEnder", 'W', new ItemStack(Blocks.WOOL, 1, 15), 'S', "string", 'L', "ingotTough"});
		
		addShapelessOreRecipe(new ItemStack(NCItems.dominos, 4), new Object[] {Items.BREAD, Items.BREAD, Items.BREAD, Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM});
		addShapelessOreRecipe(Blocks.BROWN_MUSHROOM, new Object[] {NCBlocks.glowing_mushroom});
		addShapelessOreRecipe(NCBlocks.glowing_mushroom, new Object[] {Blocks.BROWN_MUSHROOM, "dustGlowstone"});
		
		addShapedOreRecipe(new ItemStack(Items.COOKIE, 12), new Object[] {"FCF", 'F', "dustWheat", 'C', "dustCocoa"});
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
		
		addShapelessOreRecipe(NCItems.record_wanderer, new Object[] {"record", "ingotTough"});
		addShapelessOreRecipe(NCItems.record_end_of_the_world, new Object[] {"record", "ingotUranium235"});
		addShapelessOreRecipe(NCItems.record_money_for_nothing, new Object[] {"record", "ingotSilver"});
		addShapelessOreRecipe(NCItems.record_hyperspace, new Object[] {"record", "dustDimensional"});
		
		addShapedOreRecipe(NCArmor.helm_hazmat, new Object[] {"YWY", "SLS", "BIB", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_HELMET, 'B', "bioplastic", 'I', "ingotSteel", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		addShapedOreRecipe(NCArmor.chest_hazmat, new Object[] {"WSW", "YLY", "SWS", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_CHESTPLATE, 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		addShapedOreRecipe(NCArmor.legs_hazmat, new Object[] {"YBY", "SLS", "W W", 'Y', "dyeYellow", 'W', "wool", 'L', Items.LEATHER_LEGGINGS, 'B', "bioplastic", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});
		addShapedOreRecipe(NCArmor.boots_hazmat, new Object[] {"SDS", "BLB", 'D', "dyeBlack", 'L', Items.LEATHER_BOOTS, 'B', "bioplastic", 'S', new ItemStack(NCItems.rad_shielding, 1, 2)});

		addShapelessOreRecipe(NCItems.configuration_blueprint_empty, "gemLapis", Items.PAPER, "dustGraphite");
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
	
	public static void fissionFuelRecipes(String element, String fuelLetter, Item fuelType, int fertileNo, int... fissileNo) {
		for (int i = 0; i < fissileNo.length; i++) {
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 4*i), new Object[] {"ingot" + element + fissileNo[i], "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 1 + 4*i), new Object[] {"ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 2 + 4*i), new Object[] {"ingot" + element + fissileNo[i], "ingot" + element + fissileNo[i], "ingot" + element + fissileNo[i], "ingot" + element + fissileNo[i], "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
			addShapelessOreRecipe(new ItemStack(fuelType, 1, 3 + 4*i), new Object[] {"ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fissileNo[i] + "Oxide", "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo, "ingot" + element + fertileNo});
		}	
	}
	
	public static void fissionClumpRecipes(String element, Item material, int... types) {
		for (int i = 0; i < types.length; i++) {
			itemCompress(material, 4*i, "ingot" + element + types[i] + "Base", "nugget" + element + types[i]);
			itemCompress(material, 1 + 4*i, "ingot" + element + types[i] + "Oxide", "nugget" + element + types[i] + "Oxide");
			blockOpen(material, 2 + 4*i, "nugget" + element + types[i], "ingot" + element + types[i] + "Base");
			blockOpen(material, 3 + 4*i, "nugget" + element + types[i] + "Oxide", "ingot" + element + types[i] + "Oxide");
		}
	}
	
	public static void tinyClumpRecipes(String element, Item material, int... types) {
		for (int i = 0; i < types.length; i++) {
			itemCompress(material, 2*i, "ingot" + element + types[i], "nugget" + element + types[i]);
			blockOpen(material, 1 + 2*i, "nugget" + element + types[i], "ingot" + element + types[i]);
		}
	}
	
	public static void itemCompress(Item itemOut, int metaOut, String itemOutOreName, Object itemIn) {
		addShapedOreRecipe(OreDictHelper.getPrioritisedCraftingStack(new ItemStack(itemOut, 1, metaOut), itemOutOreName), new Object[] {"III", "III", "III", 'I', itemIn});
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
	
	private static final Map<String, Integer> RECIPE_COUNT_MAP = new HashMap<String, Integer>();
	
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
		if (out == null || Arrays.asList(inputs).contains(null)) return;
		ItemStack outStack = ItemStackHelper.fixItemStack(out);
		if (!outStack.isEmpty() && inputs != null) {
			String outName = outStack.getTranslationKey();
			if (RECIPE_COUNT_MAP.containsKey(outName)) {
				int count = RECIPE_COUNT_MAP.get(outName);
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
