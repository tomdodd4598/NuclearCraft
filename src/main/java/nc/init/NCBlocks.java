package nc.init;

import nc.Global;
import nc.NCInfo;
import nc.block.BlockMeta;
import nc.block.BlockTransparent;
import nc.block.NCBlock;
import nc.block.NCBlockDoor;
import nc.block.NCBlockIce;
import nc.block.NCBlockMushroom;
import nc.block.NCBlockTrapDoor;
import nc.block.item.ItemBlockMeta;
import nc.block.item.NCItemBlock;
import nc.block.tile.BlockActivatable;
import nc.block.tile.BlockActivatableTransparent;
import nc.block.tile.BlockBattery;
import nc.block.tile.BlockSimpleSidedTile;
import nc.block.tile.BlockSimpleTile;
import nc.block.tile.generator.BlockFissionController;
import nc.block.tile.generator.BlockFusionCore;
import nc.block.tile.generator.BlockFusionDummy;
import nc.block.tile.processor.BlockNuclearFurnace;
import nc.block.tile.processor.BlockProcessor;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ActivatableTileType;
import nc.enumm.BlockEnums.FusionDummyTileType;
import nc.enumm.BlockEnums.ProcessorType;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.enumm.MetaEnums;
import nc.enumm.MetaEnums.CoolerType;
import nc.enumm.MetaEnums.FissionBlockType;
import nc.enumm.MetaEnums.IngotType;
import nc.enumm.MetaEnums.OreType;
import nc.proxy.CommonProxy;
import nc.util.InfoHelper;
import nc.util.NCUtil;
import nc.util.UnitHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCBlocks {
	
	public static Block ore;
	public static Block ingot_block;
	
	public static Block fission_block;
	public static Block reactor_casing_transparent;
	public static Block cell_block;
	public static Block cooler;
	public static Block reactor_door;
	public static Block reactor_trapdoor;
	
	public static Block block_depleted_thorium;
	public static Block block_depleted_uranium;
	public static Block block_depleted_neptunium;
	public static Block block_depleted_plutonium;
	public static Block block_depleted_americium;
	public static Block block_depleted_curium;
	public static Block block_depleted_berkelium;
	public static Block block_depleted_californium;
	
	public static Block block_ice;
	
	public static Block nuclear_furnace_idle;
	public static Block nuclear_furnace_active;
	public static Block manufactory_idle;
	public static Block manufactory_active;
	public static Block isotope_separator_idle;
	public static Block isotope_separator_active;
	public static Block decay_hastener_idle;
	public static Block decay_hastener_active;
	public static Block fuel_reprocessor_idle;
	public static Block fuel_reprocessor_active;
	public static Block alloy_furnace_idle;
	public static Block alloy_furnace_active;
	public static Block infuser_idle;
	public static Block infuser_active;
	public static Block melter_idle;
	public static Block melter_active;
	public static Block supercooler_idle;
	public static Block supercooler_active;
	public static Block electrolyser_idle;
	public static Block electrolyser_active;
	public static Block irradiator_idle;
	public static Block irradiator_active;
	public static Block ingot_former_idle;
	public static Block ingot_former_active;
	public static Block pressurizer_idle;
	public static Block pressurizer_active;
	public static Block chemical_reactor_idle;
	public static Block chemical_reactor_active;
	public static Block salt_mixer_idle;
	public static Block salt_mixer_active;
	public static Block crystallizer_idle;
	public static Block crystallizer_active;
	public static Block dissolver_idle;
	public static Block dissolver_active;
	
	public static Block machine_interface;
	
	public static Block fission_controller_idle;
	public static Block fission_controller_active;
	public static Block fission_port;
	
	public static Block fusion_core;
	public static Block fusion_dummy_side;
	public static Block fusion_dummy_top;
	public static Block fusion_connector;
	
	public static Block rtg_uranium;
	public static Block rtg_plutonium;
	public static Block rtg_americium;
	public static Block rtg_californium;
	
	public static Block solar_panel_basic;
	
	public static Block decay_generator;
	
	public static Block voltaic_pile_basic;
	public static Block lithium_ion_battery_basic;
	
	public static Block buffer;
	public static Block active_cooler;
	public static Block bin;
	
	public static Block fusion_electromagnet_idle;
	public static Block fusion_electromagnet_active;
	public static Block fusion_electromagnet_transparent_idle;
	public static Block fusion_electromagnet_transparent_active;
	
	public static Block accelerator_electromagnet_idle;
	public static Block accelerator_electromagnet_active;
	public static Block electromagnet_supercooler_idle;
	public static Block electromagnet_supercooler_active;
	
	public static Block helium_collector;
	public static Block helium_collector_compact;
	public static Block helium_collector_dense;
	
	public static Block cobblestone_generator;
	public static Block cobblestone_generator_compact;
	public static Block cobblestone_generator_dense;
	
	public static Block water_source;
	public static Block water_source_compact;
	public static Block water_source_dense;
	
	public static Block nitrogen_collector;
	public static Block nitrogen_collector_compact;
	public static Block nitrogen_collector_dense;
	
	public static Block glowing_mushroom;
	
	//public static Block spin;
	
	public static void init() {
		ore = new BlockMeta.BlockOre("ore");
		ingot_block = new BlockMeta.BlockIngot("ingot_block");
		
		fission_block = new BlockMeta.BlockFission("fission_block");
		reactor_casing_transparent = new BlockTransparent("reactor_casing_transparent", Material.IRON, true).setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		cell_block = new BlockTransparent("cell_block", Material.IRON, false).setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		cooler = new BlockMeta.BlockCooler("cooler");
		reactor_door = new NCBlockDoor("reactor_door", Material.IRON);
		reactor_trapdoor = new NCBlockTrapDoor("reactor_trapdoor", Material.IRON);
		
		block_depleted_thorium = new NCBlock("block_depleted_thorium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_uranium = new NCBlock("block_depleted_uranium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_neptunium = new NCBlock("block_depleted_neptunium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_plutonium = new NCBlock("block_depleted_plutonium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_americium = new NCBlock("block_depleted_americium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_curium = new NCBlock("block_depleted_curium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_berkelium = new NCBlock("block_depleted_berkelium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_californium = new NCBlock("block_depleted_californium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		
		block_ice = new NCBlockIce("block_ice");
		
		nuclear_furnace_idle = new BlockNuclearFurnace(false);
		nuclear_furnace_active = new BlockNuclearFurnace(true);
		manufactory_idle = new BlockProcessor(ProcessorType.MANUFACTORY, false);
		manufactory_active = new BlockProcessor(ProcessorType.MANUFACTORY, true);
		isotope_separator_idle = new BlockProcessor(ProcessorType.ISOTOPE_SEPARATOR, false);
		isotope_separator_active = new BlockProcessor(ProcessorType.ISOTOPE_SEPARATOR, true);
		decay_hastener_idle = new BlockProcessor(ProcessorType.DECAY_HASTENER, false);
		decay_hastener_active = new BlockProcessor(ProcessorType.DECAY_HASTENER, true);
		fuel_reprocessor_idle = new BlockProcessor(ProcessorType.FUEL_REPROCESSOR, false);
		fuel_reprocessor_active = new BlockProcessor(ProcessorType.FUEL_REPROCESSOR, true);
		alloy_furnace_idle = new BlockProcessor(ProcessorType.ALLOY_FURNACE, false);
		alloy_furnace_active = new BlockProcessor(ProcessorType.ALLOY_FURNACE, true);
		infuser_idle = new BlockProcessor(ProcessorType.INFUSER, false);
		infuser_active = new BlockProcessor(ProcessorType.INFUSER, true);
		melter_idle = new BlockProcessor(ProcessorType.MELTER, false);
		melter_active = new BlockProcessor(ProcessorType.MELTER, true);
		supercooler_idle = new BlockProcessor(ProcessorType.SUPERCOOLER, false);
		supercooler_active = new BlockProcessor(ProcessorType.SUPERCOOLER, true);
		electrolyser_idle = new BlockProcessor(ProcessorType.ELECTROLYSER, false);
		electrolyser_active = new BlockProcessor(ProcessorType.ELECTROLYSER, true);
		irradiator_idle = new BlockProcessor(ProcessorType.IRRADIATOR, false);
		irradiator_active = new BlockProcessor(ProcessorType.IRRADIATOR, true);
		ingot_former_idle = new BlockProcessor(ProcessorType.INGOT_FORMER, false);
		ingot_former_active = new BlockProcessor(ProcessorType.INGOT_FORMER, true);
		pressurizer_idle = new BlockProcessor(ProcessorType.PRESSURIZER, false);
		pressurizer_active = new BlockProcessor(ProcessorType.PRESSURIZER, true);
		chemical_reactor_idle = new BlockProcessor(ProcessorType.CHEMICAL_REACTOR, false);
		chemical_reactor_active = new BlockProcessor(ProcessorType.CHEMICAL_REACTOR, true);
		salt_mixer_idle = new BlockProcessor(ProcessorType.SALT_MIXER, false);
		salt_mixer_active = new BlockProcessor(ProcessorType.SALT_MIXER, true);
		crystallizer_idle = new BlockProcessor(ProcessorType.CRYSTALLIZER, false);
		crystallizer_active = new BlockProcessor(ProcessorType.CRYSTALLIZER, true);
		dissolver_idle = new BlockProcessor(ProcessorType.DISSOLVER, false);
		dissolver_active = new BlockProcessor(ProcessorType.DISSOLVER, true);
		
		machine_interface = new BlockSimpleTile(SimpleTileType.MACHINE_INTERFACE);
		
		fission_controller_idle = new BlockFissionController(false);
		fission_controller_active = new BlockFissionController(true);
		fission_port = new BlockSimpleSidedTile(SimpleTileType.FISSION_PORT);
		
		fusion_core = new BlockFusionCore();
		fusion_dummy_side = new BlockFusionDummy(FusionDummyTileType.FUSION_DUMMY_SIDE);
		fusion_dummy_top = new BlockFusionDummy(FusionDummyTileType.FUSION_DUMMY_TOP);
		fusion_connector = new NCBlock("fusion_connector", Material.IRON).setCreativeTab(CommonProxy.TAB_FUSION);
		
		rtg_uranium = new BlockSimpleTile(SimpleTileType.RTG_URANIUM);
		rtg_plutonium = new BlockSimpleTile(SimpleTileType.RTG_PLUTONIUM);
		rtg_americium = new BlockSimpleTile(SimpleTileType.RTG_AMERICIUM);
		rtg_californium = new BlockSimpleTile(SimpleTileType.RTG_CALIFORNIUM);
		
		solar_panel_basic = new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_BASIC);
		
		decay_generator = new BlockSimpleTile(SimpleTileType.DECAY_GENERATOR);
		
		voltaic_pile_basic = new BlockBattery(SimpleTileType.VOLTAIC_PILE_BASIC);
		lithium_ion_battery_basic = new BlockBattery(SimpleTileType.LITHIUM_ION_BATTERY_BASIC);
		
		buffer = new BlockSimpleTile(SimpleTileType.BUFFER);
		active_cooler = new BlockSimpleTile(SimpleTileType.ACTIVE_COOLER);
		bin = new BlockSimpleTile(SimpleTileType.BIN);
		
		fusion_electromagnet_idle = new BlockActivatable(ActivatableTileType.FUSION_ELECTROMAGNET, false);
		fusion_electromagnet_active = new BlockActivatable(ActivatableTileType.FUSION_ELECTROMAGNET, true);
		fusion_electromagnet_transparent_idle = new BlockActivatableTransparent(ActivatableTileType.FUSION_ELECTROMAGNET_TRANSPARENT, false);
		fusion_electromagnet_transparent_active = new BlockActivatableTransparent(ActivatableTileType.FUSION_ELECTROMAGNET_TRANSPARENT, true);
		
		accelerator_electromagnet_idle = new BlockActivatable(ActivatableTileType.ACCELERATOR_ELECTROMAGNET, false);
		accelerator_electromagnet_active = new BlockActivatable(ActivatableTileType.ACCELERATOR_ELECTROMAGNET, true);
		electromagnet_supercooler_idle = new BlockActivatable(ActivatableTileType.ELECTROMAGNET_SUPERCOOLER, false);
		electromagnet_supercooler_active = new BlockActivatable(ActivatableTileType.ELECTROMAGNET_SUPERCOOLER, true);
		
		helium_collector = new BlockSimpleTile(SimpleTileType.HELIUM_COLLECTOR);
		helium_collector_compact = new BlockSimpleTile(SimpleTileType.HELIUM_COLLECTOR_COMPACT);
		helium_collector_dense = new BlockSimpleTile(SimpleTileType.HELIUM_COLLECTOR_DENSE);
		
		cobblestone_generator = new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR);
		cobblestone_generator_compact = new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR_COMPACT);
		cobblestone_generator_dense = new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR_DENSE);
		
		water_source = new BlockSimpleTile(SimpleTileType.WATER_SOURCE);
		water_source_compact = new BlockSimpleTile(SimpleTileType.WATER_SOURCE_COMPACT);
		water_source_dense = new BlockSimpleTile(SimpleTileType.WATER_SOURCE_DENSE);
		
		nitrogen_collector = new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR);
		nitrogen_collector_compact = new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR_COMPACT);
		nitrogen_collector_dense = new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR_DENSE);
		
		glowing_mushroom = new NCBlockMushroom("glowing_mushroom");
		
		//spin = new BlockSpin("spin");
	}
	
	public static void register() {
		registerBlock(ore, new ItemBlockMeta(ore, MetaEnums.OreType.class));
		registerBlock(ingot_block, new ItemBlockMeta(ingot_block, MetaEnums.IngotType.class, NCInfo.ingotBlockInfo()));
		
		registerBlock(fission_block, new ItemBlockMeta(fission_block, MetaEnums.FissionBlockType.class));
		registerBlock(reactor_casing_transparent);
		registerBlock(cell_block);
		registerBlock(cooler, new ItemBlockMeta(cooler, MetaEnums.CoolerType.class, NCInfo.coolerInfo()));
		registerBlock(reactor_door);
		registerBlock(reactor_trapdoor);
		
		registerBlock(block_depleted_thorium);
		registerBlock(block_depleted_uranium);
		registerBlock(block_depleted_neptunium);
		registerBlock(block_depleted_plutonium);
		registerBlock(block_depleted_americium);
		registerBlock(block_depleted_curium);
		registerBlock(block_depleted_berkelium);
		registerBlock(block_depleted_californium);
		
		registerBlock(block_ice);
		
		registerBlock(nuclear_furnace_idle);
		registerBlock(nuclear_furnace_active);
		registerBlock(manufactory_idle);
		registerBlock(manufactory_active);
		registerBlock(isotope_separator_idle);
		registerBlock(isotope_separator_active);
		registerBlock(decay_hastener_idle);
		registerBlock(decay_hastener_active);
		registerBlock(fuel_reprocessor_idle);
		registerBlock(fuel_reprocessor_active);
		registerBlock(alloy_furnace_idle);
		registerBlock(alloy_furnace_active);
		registerBlock(infuser_idle);
		registerBlock(infuser_active);
		registerBlock(melter_idle);
		registerBlock(melter_active);
		registerBlock(supercooler_idle);
		registerBlock(supercooler_active);
		registerBlock(electrolyser_idle);
		registerBlock(electrolyser_active);
		registerBlock(irradiator_idle);
		registerBlock(irradiator_active);
		registerBlock(ingot_former_idle);
		registerBlock(ingot_former_active);
		registerBlock(pressurizer_idle);
		registerBlock(pressurizer_active);
		registerBlock(chemical_reactor_idle);
		registerBlock(chemical_reactor_active);
		registerBlock(salt_mixer_idle);
		registerBlock(salt_mixer_active);
		registerBlock(crystallizer_idle);
		registerBlock(crystallizer_active);
		registerBlock(dissolver_idle);
		registerBlock(dissolver_active);
		
		registerBlock(machine_interface);
		
		registerBlock(fission_controller_idle);
		registerBlock(fission_controller_active);
		registerBlock(fission_port);
		
		registerBlock(fusion_core);
		registerBlock(fusion_dummy_side);
		registerBlock(fusion_dummy_top);
		registerBlock(fusion_connector);
		
		registerBlock(rtg_uranium, InfoHelper.formattedInfo("tile.rtg.desc", UnitHelper.prefix(NCConfig.rtg_power[0], 5, "RF/t")));
		registerBlock(rtg_plutonium, InfoHelper.formattedInfo("tile.rtg.desc", UnitHelper.prefix(NCConfig.rtg_power[1], 5, "RF/t")));
		registerBlock(rtg_americium, InfoHelper.formattedInfo("tile.rtg.desc", UnitHelper.prefix(NCConfig.rtg_power[2], 5, "RF/t")));
		registerBlock(rtg_californium, InfoHelper.formattedInfo("tile.rtg.desc", UnitHelper.prefix(NCConfig.rtg_power[3], 5, "RF/t")));
		
		registerBlock(solar_panel_basic, InfoHelper.formattedInfo("tile.solar_panel.desc", UnitHelper.prefix(NCConfig.solar_power[0], 5, "RF/t")));
		
		registerBlock(decay_generator);
		
		registerBlock(voltaic_pile_basic, InfoHelper.formattedInfo("tile.energy_storage.desc", UnitHelper.prefix(NCConfig.battery_capacity[0], 5, "RF")));
		registerBlock(lithium_ion_battery_basic, InfoHelper.formattedInfo("tile.energy_storage.desc", UnitHelper.prefix(NCConfig.battery_capacity[1], 5, "RF")));
		
		registerBlock(buffer);
		registerBlock(active_cooler);
		registerBlock(bin);
		
		registerBlock(fusion_electromagnet_idle, InfoHelper.formattedInfo("tile.fusion_electromagnet_idle.desc", UnitHelper.ratePrefix(NCConfig.fusion_electromagnet_power, 5, "RF")));
		registerBlock(fusion_electromagnet_active);
		registerBlock(fusion_electromagnet_transparent_idle, InfoHelper.formattedInfo("tile.fusion_electromagnet_idle.desc", UnitHelper.ratePrefix(NCConfig.fusion_electromagnet_power, 5, "RF")));
		registerBlock(fusion_electromagnet_transparent_active);
		
		registerBlock(accelerator_electromagnet_idle, InfoHelper.formattedInfo("tile.accelerator_electromagnet_idle.desc", UnitHelper.ratePrefix(NCConfig.accelerator_electromagnet_power, 5, "RF")));
		registerBlock(accelerator_electromagnet_active);
		registerBlock(electromagnet_supercooler_idle, InfoHelper.formattedInfo("tile.electromagnet_supercooler_idle.desc", UnitHelper.ratePrefix(NCConfig.accelerator_electromagnet_power, 5, "RF"), UnitHelper.ratePrefix(NCConfig.accelerator_supercooler_coolant, 5, "B", -1)));
		registerBlock(electromagnet_supercooler_active);
		
		registerBlock(helium_collector, InfoHelper.formattedInfo("tile.helium_collector.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[0], 5, "B", -1)));
		registerBlock(helium_collector_compact, InfoHelper.formattedInfo("tile.helium_collector.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[0]*8, 5, "B", -1)));
		registerBlock(helium_collector_dense, InfoHelper.formattedInfo("tile.helium_collector.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[0]*64, 5, "B", -1)));
		
		registerBlock(cobblestone_generator, InfoHelper.formattedInfo("tile.cobblestone_generator.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1], 5, "Cobblestone")));
		registerBlock(cobblestone_generator_compact, InfoHelper.formattedInfo("tile.cobblestone_generator.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1]*8, 5, "Cobblestone")));
		registerBlock(cobblestone_generator_dense, InfoHelper.formattedInfo("tile.cobblestone_generator.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1]*64, 5, "Cobblestone")));
		
		registerBlock(water_source, InfoHelper.formattedInfo("tile.water_source.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[2], 5, "B", -1)));
		registerBlock(water_source_compact, InfoHelper.formattedInfo("tile.water_source.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[2]*8, 5, "B", -1)));
		registerBlock(water_source_dense, InfoHelper.formattedInfo("tile.water_source.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[2]*64, 5, "B", -1)));
		
		registerBlock(nitrogen_collector, InfoHelper.formattedInfo("tile.nitrogen_collector.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[3], 5, "B", -1)));
		registerBlock(nitrogen_collector_compact, InfoHelper.formattedInfo("tile.nitrogen_collector.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[3]*8, 5, "B", -1)));
		registerBlock(nitrogen_collector_dense, InfoHelper.formattedInfo("tile.nitrogen_collector.desc", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[3]*64, 5, "B", -1)));
		
		registerBlock(glowing_mushroom);
		
		//registerBlock(spin);
	}
	
	public static void registerRenders() {
		for (int i = 0; i < OreType.values().length; i++) {
			registerRender(ore, i, "ore_" + OreType.values()[i].getName());
		}
		
		for (int i = 0; i < IngotType.values().length; i++) {
			registerRender(ingot_block, i, "ingot_block_" + IngotType.values()[i].getName());
		}
		
		for (int i = 0; i < FissionBlockType.values().length; i++) {
			registerRender(fission_block, i, "fission_block_" + FissionBlockType.values()[i].getName());
		}
		
		registerRender(reactor_casing_transparent);
		registerRender(cell_block);
		
		for (int i = 0; i < CoolerType.values().length; i++) {
			registerRender(cooler, i, "cooler_" + CoolerType.values()[i].getName());
		}
		
		registerRender(reactor_door);
		registerRender(reactor_trapdoor);

		registerRender(block_depleted_thorium);
		registerRender(block_depleted_uranium);
		registerRender(block_depleted_neptunium);
		registerRender(block_depleted_plutonium);
		registerRender(block_depleted_americium);
		registerRender(block_depleted_curium);
		registerRender(block_depleted_berkelium);
		registerRender(block_depleted_californium);
		
		registerRender(block_ice);
		
		registerRender(nuclear_furnace_idle);
		registerRender(nuclear_furnace_active);
		registerRender(manufactory_idle);
		registerRender(manufactory_active);
		registerRender(isotope_separator_idle);
		registerRender(isotope_separator_active);
		registerRender(decay_hastener_idle);
		registerRender(decay_hastener_active);
		registerRender(fuel_reprocessor_idle);
		registerRender(fuel_reprocessor_active);
		registerRender(alloy_furnace_idle);
		registerRender(alloy_furnace_active);
		registerRender(infuser_idle);
		registerRender(infuser_active);
		registerRender(melter_idle);
		registerRender(melter_active);
		registerRender(supercooler_idle);
		registerRender(supercooler_active);
		registerRender(electrolyser_idle);
		registerRender(electrolyser_active);
		registerRender(irradiator_idle);
		registerRender(irradiator_active);
		registerRender(ingot_former_idle);
		registerRender(ingot_former_active);
		registerRender(pressurizer_idle);
		registerRender(pressurizer_active);
		registerRender(chemical_reactor_idle);
		registerRender(chemical_reactor_active);
		registerRender(salt_mixer_idle);
		registerRender(salt_mixer_active);
		registerRender(crystallizer_idle);
		registerRender(crystallizer_active);
		registerRender(dissolver_idle);
		registerRender(dissolver_active);
		
		registerRender(machine_interface);
		
		registerRender(fission_controller_idle);
		registerRender(fission_controller_active);
		registerRender(fission_port);
		
		registerRender(fusion_core);
		registerRender(fusion_dummy_side);
		registerRender(fusion_dummy_top);
		registerRender(fusion_connector);
		
		registerRender(rtg_uranium);
		registerRender(rtg_plutonium);
		registerRender(rtg_americium);
		registerRender(rtg_californium);
		
		registerRender(solar_panel_basic);
		
		registerRender(decay_generator);
		
		registerRender(voltaic_pile_basic);
		registerRender(lithium_ion_battery_basic);
		
		registerRender(buffer);
		registerRender(active_cooler);
		registerRender(bin);
		
		registerRender(fusion_electromagnet_idle);
		registerRender(fusion_electromagnet_active);
		registerRender(fusion_electromagnet_transparent_idle);
		registerRender(fusion_electromagnet_transparent_active);
		
		registerRender(accelerator_electromagnet_idle);
		registerRender(accelerator_electromagnet_active);
		registerRender(electromagnet_supercooler_idle);
		registerRender(electromagnet_supercooler_active);
		
		registerRender(helium_collector);
		registerRender(helium_collector_compact);
		registerRender(helium_collector_dense);
		
		registerRender(cobblestone_generator);
		registerRender(cobblestone_generator_compact);
		registerRender(cobblestone_generator_dense);
		
		registerRender(water_source);
		registerRender(water_source_compact);
		registerRender(water_source_dense);
		
		registerRender(nitrogen_collector);
		registerRender(nitrogen_collector_compact);
		registerRender(nitrogen_collector_dense);
		
		registerRender(glowing_mushroom);
		
		//registerRender(spin);
	}
	
	public static void registerBlock(Block block, String... info) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, info).setRegistryName(block.getRegistryName()));
		NCUtil.getLogger().info("Registered block " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerBlock(Block block, ItemBlock itemBlock) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock.setRegistryName(block.getRegistryName()));
		NCUtil.getLogger().info("Registered block " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, block.getUnlocalizedName().substring(5)), "inventory"));
		NCUtil.getLogger().info("Registered render for block " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Block block, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fileName), "inventory"));
		NCUtil.getLogger().info("Registered render for block " + fileName);
	}
}
