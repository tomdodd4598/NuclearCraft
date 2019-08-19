package nc.init;

import nc.Global;
import nc.NCInfo;
import nc.block.BlockMeta;
import nc.block.NCBlock;
import nc.block.NCBlockDoor;
import nc.block.NCBlockIce;
import nc.block.NCBlockMushroom;
import nc.block.NCBlockTrapDoor;
import nc.block.item.ItemBlockMeta;
import nc.block.item.NCItemBlock;
import nc.block.item.energy.ItemBlockBattery;
import nc.block.tile.BlockActivatable;
import nc.block.tile.BlockBattery;
import nc.block.tile.BlockSimpleTile;
import nc.block.tile.ITileType;
import nc.block.tile.dummy.BlockFissionPort;
import nc.block.tile.dummy.BlockFusionDummy;
import nc.block.tile.dummy.BlockMachineInterface;
import nc.block.tile.generator.BlockFissionController;
import nc.block.tile.generator.BlockFissionControllerNewFixed;
import nc.block.tile.generator.BlockFusionCore;
import nc.block.tile.processor.BlockNuclearFurnace;
import nc.block.tile.processor.BlockProcessor;
import nc.block.tile.radiation.BlockGeigerCounter;
import nc.block.tile.radiation.BlockScrubber;
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
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerComputerPort;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerCondenserTube;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerController;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerFrame;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerGlass;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerTube;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerVent;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerWall;
import nc.multiblock.saltFission.block.BlockSaltFissionBeam;
import nc.multiblock.saltFission.block.BlockSaltFissionComputerPort;
import nc.multiblock.saltFission.block.BlockSaltFissionController;
import nc.multiblock.saltFission.block.BlockSaltFissionDistributor;
import nc.multiblock.saltFission.block.BlockSaltFissionFrame;
import nc.multiblock.saltFission.block.BlockSaltFissionGlass;
import nc.multiblock.saltFission.block.BlockSaltFissionHeater;
import nc.multiblock.saltFission.block.BlockSaltFissionModerator;
import nc.multiblock.saltFission.block.BlockSaltFissionRedstonePort;
import nc.multiblock.saltFission.block.BlockSaltFissionRetriever;
import nc.multiblock.saltFission.block.BlockSaltFissionVent;
import nc.multiblock.saltFission.block.BlockSaltFissionVessel;
import nc.multiblock.saltFission.block.BlockSaltFissionWall;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.block.BlockTurbineComputerPort;
import nc.multiblock.turbine.block.BlockTurbineController;
import nc.multiblock.turbine.block.BlockTurbineDynamoCoil;
import nc.multiblock.turbine.block.BlockTurbineFrame;
import nc.multiblock.turbine.block.BlockTurbineGlass;
import nc.multiblock.turbine.block.BlockTurbineInlet;
import nc.multiblock.turbine.block.BlockTurbineOutlet;
import nc.multiblock.turbine.block.BlockTurbineRotorBearing;
import nc.multiblock.turbine.block.BlockTurbineRotorBlade;
import nc.multiblock.turbine.block.BlockTurbineRotorShaft;
import nc.multiblock.turbine.block.BlockTurbineRotorStator;
import nc.multiblock.turbine.block.BlockTurbineWall;
import nc.radiation.RadiationHelper;
import nc.tab.NCTabs;
import nc.tile.energy.battery.BatteryType;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
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
	public static Block extractor_idle;
	public static Block extractor_active;
	public static Block centrifuge_idle;
	public static Block centrifuge_active;
	public static Block rock_crusher_idle;
	public static Block rock_crusher_active;
	
	public static Block machine_interface;
	
	public static Block fission_controller_idle;
	public static Block fission_controller_active;
	public static Block fission_controller_new_idle;
	public static Block fission_controller_new_active;
	public static Block fission_controller_new_fixed;
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
	public static Block solar_panel_advanced;
	public static Block solar_panel_du;
	public static Block solar_panel_elite;
	
	public static Block decay_generator;
	
	public static Block voltaic_pile_basic;
	public static Block voltaic_pile_advanced;
	public static Block voltaic_pile_du;
	public static Block voltaic_pile_elite;
	public static Block lithium_ion_battery_basic;
	public static Block lithium_ion_battery_advanced;
	public static Block lithium_ion_battery_du;
	public static Block lithium_ion_battery_elite;
	
	public static Block buffer;
	public static Block active_cooler;
	public static Block bin;
	
	public static Block fusion_electromagnet_idle;
	public static Block fusion_electromagnet_active;
	public static Block fusion_electromagnet_transparent_idle;
	public static Block fusion_electromagnet_transparent_active;
	
	public static Block salt_fission_controller;
	public static Block salt_fission_wall;
	public static Block salt_fission_glass;
	public static Block salt_fission_frame;
	public static Block salt_fission_beam;
	public static Block salt_fission_vent;
	public static Block salt_fission_vessel;
	public static Block salt_fission_heater;
	public static Block salt_fission_moderator;
	public static Block salt_fission_distributor;
	public static Block salt_fission_retriever;
	public static Block salt_fission_redstone_port;
	public static Block salt_fission_computer_port;
	
	public static Block heat_exchanger_controller;
	public static Block heat_exchanger_wall;
	public static Block heat_exchanger_glass;
	public static Block heat_exchanger_frame;
	public static Block heat_exchanger_vent;
	public static Block heat_exchanger_tube_copper;
	public static Block heat_exchanger_tube_hard_carbon;
	public static Block heat_exchanger_tube_thermoconducting;
	public static Block heat_exchanger_condenser_tube_copper;
	public static Block heat_exchanger_condenser_tube_hard_carbon;
	public static Block heat_exchanger_condenser_tube_thermoconducting;
	public static Block heat_exchanger_computer_port;
	
	public static Block turbine_controller;
	public static Block turbine_wall;
	public static Block turbine_glass;
	public static Block turbine_frame;
	public static Block turbine_rotor_shaft;
	public static Block turbine_rotor_blade_steel;
	public static Block turbine_rotor_blade_extreme;
	public static Block turbine_rotor_blade_sic_sic_cmc;
	public static Block turbine_rotor_stator;
	public static Block turbine_rotor_bearing;
	public static Block turbine_dynamo_coil;
	public static Block turbine_inlet;
	public static Block turbine_outlet;
	public static Block turbine_computer_port;
	
	public static Block condenser_controller;
	public static Block condenser_wall;
	public static Block condenser_glass;
	public static Block condenser_frame;
	public static Block condenser_computer_port;
	
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
	
	public static Block radiation_scrubber;
	
	public static Block geiger_block;
	
	public static Block glowing_mushroom;
	public static Block dry_earth;
	
	public static Block tritium_lamp;
	public static Block tritium_lantern;
	
	//public static Block spin;
	
	public static void init() {
		ore = withName(new BlockMeta.BlockOre(), "ore");
		ingot_block = withName(new BlockMeta.BlockIngot(), "ingot_block");
		
		fission_block = withName(new BlockMeta.BlockFission(), "fission_block");
		reactor_casing_transparent = withName(new NCBlock.Transparent(Material.IRON, true).setCreativeTab(NCTabs.FISSION_BLOCKS), "reactor_casing_transparent");
		cell_block = withName(new NCBlock.Transparent(Material.IRON, false).setCreativeTab(NCTabs.FISSION_BLOCKS), "cell_block");
		cooler = withName(new BlockMeta.BlockCooler(), "cooler");
		reactor_door = withName(new NCBlockDoor(Material.IRON), "reactor_door");
		reactor_trapdoor = withName(new NCBlockTrapDoor(Material.IRON), "reactor_trapdoor");
		
		block_depleted_thorium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_thorium");
		block_depleted_uranium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_uranium");
		block_depleted_neptunium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_neptunium");
		block_depleted_plutonium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_plutonium");
		block_depleted_americium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_americium");
		block_depleted_curium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_curium");
		block_depleted_berkelium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_berkelium");
		block_depleted_californium = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "block_depleted_californium");
		
		block_ice = withName(new NCBlockIce(), "block_ice");
		
		if (NCConfig.register_processor[0]) nuclear_furnace_idle = withName(new BlockNuclearFurnace(false), "nuclear_furnace_idle");
		if (NCConfig.register_processor[0]) nuclear_furnace_active = withName(new BlockNuclearFurnace(true), "nuclear_furnace_active");
		if (NCConfig.register_processor[1]) manufactory_idle = withName(new BlockProcessor(ProcessorType.MANUFACTORY, false));
		if (NCConfig.register_processor[1]) manufactory_active = withName(new BlockProcessor(ProcessorType.MANUFACTORY, true));
		if (NCConfig.register_processor[2]) isotope_separator_idle = withName(new BlockProcessor(ProcessorType.ISOTOPE_SEPARATOR, false));
		if (NCConfig.register_processor[2]) isotope_separator_active = withName(new BlockProcessor(ProcessorType.ISOTOPE_SEPARATOR, true));
		if (NCConfig.register_processor[3]) decay_hastener_idle = withName(new BlockProcessor(ProcessorType.DECAY_HASTENER, false));
		if (NCConfig.register_processor[3]) decay_hastener_active = withName(new BlockProcessor(ProcessorType.DECAY_HASTENER, true));
		if (NCConfig.register_processor[4]) fuel_reprocessor_idle = withName(new BlockProcessor(ProcessorType.FUEL_REPROCESSOR, false));
		if (NCConfig.register_processor[4]) fuel_reprocessor_active = withName(new BlockProcessor(ProcessorType.FUEL_REPROCESSOR, true));
		if (NCConfig.register_processor[5]) alloy_furnace_idle = withName(new BlockProcessor(ProcessorType.ALLOY_FURNACE, false));
		if (NCConfig.register_processor[5]) alloy_furnace_active = withName(new BlockProcessor(ProcessorType.ALLOY_FURNACE, true));
		if (NCConfig.register_processor[6]) infuser_idle = withName(new BlockProcessor(ProcessorType.INFUSER, false));
		if (NCConfig.register_processor[6]) infuser_active = withName(new BlockProcessor(ProcessorType.INFUSER, true));
		if (NCConfig.register_processor[7]) melter_idle = withName(new BlockProcessor(ProcessorType.MELTER, false));
		if (NCConfig.register_processor[7]) melter_active = withName(new BlockProcessor(ProcessorType.MELTER, true));
		if (NCConfig.register_processor[8]) supercooler_idle = withName(new BlockProcessor(ProcessorType.SUPERCOOLER, false));
		if (NCConfig.register_processor[8]) supercooler_active = withName(new BlockProcessor(ProcessorType.SUPERCOOLER, true));
		if (NCConfig.register_processor[9]) electrolyser_idle = withName(new BlockProcessor(ProcessorType.ELECTROLYSER, false));
		if (NCConfig.register_processor[9]) electrolyser_active = withName(new BlockProcessor(ProcessorType.ELECTROLYSER, true));
		if (NCConfig.register_processor[10]) irradiator_idle = withName(new BlockProcessor(ProcessorType.IRRADIATOR, false));
		if (NCConfig.register_processor[10]) irradiator_active = withName(new BlockProcessor(ProcessorType.IRRADIATOR, true));
		if (NCConfig.register_processor[11]) ingot_former_idle = withName(new BlockProcessor(ProcessorType.INGOT_FORMER, false));
		if (NCConfig.register_processor[11]) ingot_former_active = withName(new BlockProcessor(ProcessorType.INGOT_FORMER, true));
		if (NCConfig.register_processor[12]) pressurizer_idle = withName(new BlockProcessor(ProcessorType.PRESSURIZER, false));
		if (NCConfig.register_processor[12]) pressurizer_active = withName(new BlockProcessor(ProcessorType.PRESSURIZER, true));
		if (NCConfig.register_processor[13]) chemical_reactor_idle = withName(new BlockProcessor(ProcessorType.CHEMICAL_REACTOR, false));
		if (NCConfig.register_processor[13]) chemical_reactor_active = withName(new BlockProcessor(ProcessorType.CHEMICAL_REACTOR, true));
		if (NCConfig.register_processor[14]) salt_mixer_idle = withName(new BlockProcessor(ProcessorType.SALT_MIXER, false));
		if (NCConfig.register_processor[14]) salt_mixer_active = withName(new BlockProcessor(ProcessorType.SALT_MIXER, true));
		if (NCConfig.register_processor[15]) crystallizer_idle = withName(new BlockProcessor(ProcessorType.CRYSTALLIZER, false));
		if (NCConfig.register_processor[15]) crystallizer_active = withName(new BlockProcessor(ProcessorType.CRYSTALLIZER, true));
		if (NCConfig.register_processor[16]) dissolver_idle = withName(new BlockProcessor(ProcessorType.DISSOLVER, false));
		if (NCConfig.register_processor[16]) dissolver_active = withName(new BlockProcessor(ProcessorType.DISSOLVER, true));
		if (NCConfig.register_processor[17]) extractor_idle = withName(new BlockProcessor(ProcessorType.EXTRACTOR, false));
		if (NCConfig.register_processor[17]) extractor_active = withName(new BlockProcessor(ProcessorType.EXTRACTOR, true));
		if (NCConfig.register_processor[18]) centrifuge_idle = withName(new BlockProcessor(ProcessorType.CENTRIFUGE, false));
		if (NCConfig.register_processor[18]) centrifuge_active = withName(new BlockProcessor(ProcessorType.CENTRIFUGE, true));
		if (NCConfig.register_processor[19]) rock_crusher_idle = withName(new BlockProcessor(ProcessorType.ROCK_CRUSHER, false));
		if (NCConfig.register_processor[19]) rock_crusher_active = withName(new BlockProcessor(ProcessorType.ROCK_CRUSHER, true));
		
		machine_interface = withName(new BlockMachineInterface(SimpleTileType.MACHINE_INTERFACE));
		
		fission_controller_idle = withName(new BlockFissionController(false, false));
		fission_controller_active = withName(new BlockFissionController(true, false));
		fission_controller_new_idle = withName(new BlockFissionController(false, true));
		fission_controller_new_active = withName(new BlockFissionController(true, true));
		fission_controller_new_fixed = withName(new BlockFissionControllerNewFixed());
		fission_port = withName(new BlockFissionPort(SimpleTileType.FISSION_PORT));
		
		fusion_core = withName(new BlockFusionCore(), "fusion_core");
		fusion_dummy_side = withName(new BlockFusionDummy(FusionDummyTileType.FUSION_DUMMY_SIDE));
		fusion_dummy_top = withName(new BlockFusionDummy(FusionDummyTileType.FUSION_DUMMY_TOP));
		fusion_connector = withName(new NCBlock(Material.IRON).setCreativeTab(NCTabs.FUSION), "fusion_connector");
		
		rtg_uranium = withName(new BlockSimpleTile(SimpleTileType.RTG_URANIUM));
		rtg_plutonium = withName(new BlockSimpleTile(SimpleTileType.RTG_PLUTONIUM));
		rtg_americium = withName(new BlockSimpleTile(SimpleTileType.RTG_AMERICIUM));
		rtg_californium = withName(new BlockSimpleTile(SimpleTileType.RTG_CALIFORNIUM));
		
		solar_panel_basic = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_BASIC));
		solar_panel_advanced = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_ADVANCED));
		solar_panel_du = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_DU));
		solar_panel_elite = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_ELITE));
		
		decay_generator = withName(new BlockSimpleTile(SimpleTileType.DECAY_GENERATOR));
		
		voltaic_pile_basic = withName(new BlockBattery(SimpleTileType.VOLTAIC_PILE_BASIC));
		voltaic_pile_advanced = withName(new BlockBattery(SimpleTileType.VOLTAIC_PILE_ADVANCED));
		voltaic_pile_du = withName(new BlockBattery(SimpleTileType.VOLTAIC_PILE_DU));
		voltaic_pile_elite = withName(new BlockBattery(SimpleTileType.VOLTAIC_PILE_ELITE));
		
		lithium_ion_battery_basic = withName(new BlockBattery(SimpleTileType.LITHIUM_ION_BATTERY_BASIC));
		lithium_ion_battery_advanced = withName(new BlockBattery(SimpleTileType.LITHIUM_ION_BATTERY_ADVANCED));
		lithium_ion_battery_du = withName(new BlockBattery(SimpleTileType.LITHIUM_ION_BATTERY_DU));
		lithium_ion_battery_elite = withName(new BlockBattery(SimpleTileType.LITHIUM_ION_BATTERY_ELITE));
		
		buffer = withName(new BlockSimpleTile(SimpleTileType.BUFFER));
		active_cooler = withName(new BlockSimpleTile(SimpleTileType.ACTIVE_COOLER));
		bin = withName(new BlockSimpleTile(SimpleTileType.BIN));
		
		fusion_electromagnet_idle = withName(new BlockActivatable(ActivatableTileType.FUSION_ELECTROMAGNET, false));
		fusion_electromagnet_active = withName(new BlockActivatable(ActivatableTileType.FUSION_ELECTROMAGNET, true));
		fusion_electromagnet_transparent_idle = withName(new BlockActivatable.Transparent(ActivatableTileType.FUSION_ELECTROMAGNET_TRANSPARENT, false, true));
		fusion_electromagnet_transparent_active = withName(new BlockActivatable.Transparent(ActivatableTileType.FUSION_ELECTROMAGNET_TRANSPARENT, true, true));
		
		salt_fission_controller = withName(new BlockSaltFissionController(), "salt_fission_controller");
		salt_fission_wall = withName(new BlockSaltFissionWall(), "salt_fission_wall");
		salt_fission_glass = withName(new BlockSaltFissionGlass(), "salt_fission_glass");
		salt_fission_frame = withName(new BlockSaltFissionFrame(), "salt_fission_frame");
		salt_fission_beam = withName(new BlockSaltFissionBeam(), "salt_fission_beam");
		salt_fission_vent = withName(new BlockSaltFissionVent(), "salt_fission_vent");
		salt_fission_vessel = withName(new BlockSaltFissionVessel(), "salt_fission_vessel");
		salt_fission_heater = withName(new BlockSaltFissionHeater(), "salt_fission_heater");
		salt_fission_moderator = withName(new BlockSaltFissionModerator(), "salt_fission_moderator");
		salt_fission_distributor = withName(new BlockSaltFissionDistributor(), "salt_fission_distributor");
		salt_fission_retriever = withName(new BlockSaltFissionRetriever(), "salt_fission_retriever");
		salt_fission_redstone_port = withName(new BlockSaltFissionRedstonePort(), "salt_fission_redstone_port");
		salt_fission_computer_port = withName(new BlockSaltFissionComputerPort(), "salt_fission_computer_port");
		
		heat_exchanger_controller = withName(new BlockHeatExchangerController(), "heat_exchanger_controller");
		heat_exchanger_wall = withName(new BlockHeatExchangerWall(), "heat_exchanger_wall");
		heat_exchanger_glass = withName(new BlockHeatExchangerGlass(), "heat_exchanger_glass");
		heat_exchanger_frame = withName(new BlockHeatExchangerFrame(), "heat_exchanger_frame");
		heat_exchanger_vent = withName(new BlockHeatExchangerVent(), "heat_exchanger_vent");
		heat_exchanger_tube_copper = withName(new BlockHeatExchangerTube(HeatExchangerTubeType.COPPER), "heat_exchanger_tube_copper");
		heat_exchanger_tube_hard_carbon = withName(new BlockHeatExchangerTube(HeatExchangerTubeType.HARD_CARBON), "heat_exchanger_tube_hard_carbon");
		heat_exchanger_tube_thermoconducting = withName(new BlockHeatExchangerTube(HeatExchangerTubeType.THERMOCONDUCTING), "heat_exchanger_tube_thermoconducting");
		heat_exchanger_condenser_tube_copper = withName(new BlockHeatExchangerCondenserTube(HeatExchangerTubeType.COPPER), "heat_exchanger_condenser_tube_copper");
		heat_exchanger_condenser_tube_hard_carbon = withName(new BlockHeatExchangerCondenserTube(HeatExchangerTubeType.HARD_CARBON), "heat_exchanger_condenser_tube_hard_carbon");
		heat_exchanger_condenser_tube_thermoconducting = withName(new BlockHeatExchangerCondenserTube(HeatExchangerTubeType.THERMOCONDUCTING), "heat_exchanger_condenser_tube_thermoconducting");
		heat_exchanger_computer_port = withName(new BlockHeatExchangerComputerPort(), "heat_exchanger_computer_port");
		
		turbine_controller = withName(new BlockTurbineController(), "turbine_controller");
		turbine_wall = withName(new BlockTurbineWall(), "turbine_wall");
		turbine_glass = withName(new BlockTurbineGlass(), "turbine_glass");
		turbine_frame = withName(new BlockTurbineFrame(), "turbine_frame");
		turbine_rotor_shaft = withName(new BlockTurbineRotorShaft(), "turbine_rotor_shaft");
		turbine_rotor_blade_steel = withName(new BlockTurbineRotorBlade(TurbineRotorBladeType.STEEL), "turbine_rotor_blade_steel");
		turbine_rotor_blade_extreme = withName(new BlockTurbineRotorBlade(TurbineRotorBladeType.EXTREME), "turbine_rotor_blade_extreme");
		turbine_rotor_blade_sic_sic_cmc = withName(new BlockTurbineRotorBlade(TurbineRotorBladeType.SIC_SIC_CMC), "turbine_rotor_blade_sic_sic_cmc");
		turbine_rotor_stator = withName(new BlockTurbineRotorStator(), "turbine_rotor_stator");
		turbine_rotor_bearing = withName(new BlockTurbineRotorBearing(), "turbine_rotor_bearing");
		turbine_dynamo_coil = withName(new BlockTurbineDynamoCoil(), "turbine_dynamo_coil");
		turbine_inlet = withName(new BlockTurbineInlet(), "turbine_inlet");
		turbine_outlet = withName(new BlockTurbineOutlet(), "turbine_outlet");
		turbine_computer_port = withName(new BlockTurbineComputerPort(), "turbine_computer_port");
		
		accelerator_electromagnet_idle = withName(new BlockActivatable(ActivatableTileType.ACCELERATOR_ELECTROMAGNET, false));
		accelerator_electromagnet_active = withName(new BlockActivatable(ActivatableTileType.ACCELERATOR_ELECTROMAGNET, true));
		electromagnet_supercooler_idle = withName(new BlockActivatable(ActivatableTileType.ELECTROMAGNET_SUPERCOOLER, false));
		electromagnet_supercooler_active = withName(new BlockActivatable(ActivatableTileType.ELECTROMAGNET_SUPERCOOLER, true));
		
		if (NCConfig.register_passive[0]) {
			helium_collector = withName(new BlockSimpleTile(SimpleTileType.HELIUM_COLLECTOR));
			helium_collector_compact = withName(new BlockSimpleTile(SimpleTileType.HELIUM_COLLECTOR_COMPACT));
			helium_collector_dense = withName(new BlockSimpleTile(SimpleTileType.HELIUM_COLLECTOR_DENSE));
		}
		
		if (NCConfig.register_passive[1]) {
			cobblestone_generator = withName(new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR));
			cobblestone_generator_compact = withName(new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR_COMPACT));
			cobblestone_generator_dense = withName(new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR_DENSE));
		}
		
		if (NCConfig.register_passive[2]) {
			water_source = withName(new BlockSimpleTile(SimpleTileType.WATER_SOURCE));
			water_source_compact = withName(new BlockSimpleTile(SimpleTileType.WATER_SOURCE_COMPACT));
			water_source_dense = withName(new BlockSimpleTile(SimpleTileType.WATER_SOURCE_DENSE));
		}
		
		if (NCConfig.register_passive[3]) {
			nitrogen_collector = withName(new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR));
			nitrogen_collector_compact = withName(new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR_COMPACT));
			nitrogen_collector_dense = withName(new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR_DENSE));
		}
		
		radiation_scrubber = withName(new BlockScrubber(), "radiation_scrubber");
		
		geiger_block = withName(new BlockGeigerCounter(), "geiger_block");
		
		glowing_mushroom = withName(new NCBlockMushroom().setLightLevel(1F), "glowing_mushroom");
		dry_earth = withName(new NCBlock(Material.ROCK).setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS), "dry_earth");
		
		tritium_lamp = withName(new NCBlock(Material.GLASS).setCreativeTab(NCTabs.MISC).setLightLevel(1F), "tritium_lamp");
		
		//spin = withName(new BlockSpin(), "spin");
	}
	
	public static void register() {
		registerBlock(ore, new ItemBlockMeta(ore, MetaEnums.OreType.class));
		registerBlock(ingot_block, new ItemBlockMeta(ingot_block, MetaEnums.IngotType.class, NCInfo.ingotBlockFixedInfo(), NCInfo.ingotBlockInfo()));
		
		registerBlock(fission_block, new ItemBlockMeta(fission_block, MetaEnums.FissionBlockType.class));
		registerBlock(reactor_casing_transparent);
		registerBlock(cell_block, InfoHelper.formattedInfo(infoLine("cell_block"), Lang.localise("info.moderator.cell", NCConfig.fission_neutron_reach)));
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
		
		if (NCConfig.register_processor[0]) registerBlock(nuclear_furnace_idle);
		if (NCConfig.register_processor[0]) registerBlock(nuclear_furnace_active);
		if (NCConfig.register_processor[1]) registerBlock(manufactory_idle);
		if (NCConfig.register_processor[1]) registerBlock(manufactory_active);
		if (NCConfig.register_processor[2]) registerBlock(isotope_separator_idle);
		if (NCConfig.register_processor[2]) registerBlock(isotope_separator_active);
		if (NCConfig.register_processor[3]) registerBlock(decay_hastener_idle);
		if (NCConfig.register_processor[3]) registerBlock(decay_hastener_active);
		if (NCConfig.register_processor[4]) registerBlock(fuel_reprocessor_idle);
		if (NCConfig.register_processor[4]) registerBlock(fuel_reprocessor_active);
		if (NCConfig.register_processor[5]) registerBlock(alloy_furnace_idle);
		if (NCConfig.register_processor[5]) registerBlock(alloy_furnace_active);
		if (NCConfig.register_processor[6]) registerBlock(infuser_idle);
		if (NCConfig.register_processor[6]) registerBlock(infuser_active);
		if (NCConfig.register_processor[7]) registerBlock(melter_idle);
		if (NCConfig.register_processor[7]) registerBlock(melter_active);
		if (NCConfig.register_processor[8]) registerBlock(supercooler_idle);
		if (NCConfig.register_processor[8]) registerBlock(supercooler_active);
		if (NCConfig.register_processor[9]) registerBlock(electrolyser_idle);
		if (NCConfig.register_processor[9]) registerBlock(electrolyser_active);
		if (NCConfig.register_processor[10]) registerBlock(irradiator_idle);
		if (NCConfig.register_processor[10]) registerBlock(irradiator_active);
		if (NCConfig.register_processor[11]) registerBlock(ingot_former_idle);
		if (NCConfig.register_processor[11]) registerBlock(ingot_former_active);
		if (NCConfig.register_processor[12]) registerBlock(pressurizer_idle);
		if (NCConfig.register_processor[12]) registerBlock(pressurizer_active);
		if (NCConfig.register_processor[13]) registerBlock(chemical_reactor_idle);
		if (NCConfig.register_processor[13]) registerBlock(chemical_reactor_active);
		if (NCConfig.register_processor[14]) registerBlock(salt_mixer_idle);
		if (NCConfig.register_processor[14]) registerBlock(salt_mixer_active);
		if (NCConfig.register_processor[15]) registerBlock(crystallizer_idle);
		if (NCConfig.register_processor[15]) registerBlock(crystallizer_active);
		if (NCConfig.register_processor[16]) registerBlock(dissolver_idle);
		if (NCConfig.register_processor[16]) registerBlock(dissolver_active);
		if (NCConfig.register_processor[17]) registerBlock(extractor_idle);
		if (NCConfig.register_processor[17]) registerBlock(extractor_active);
		if (NCConfig.register_processor[18]) registerBlock(centrifuge_idle);
		if (NCConfig.register_processor[18]) registerBlock(centrifuge_active);
		if (NCConfig.register_processor[19]) registerBlock(rock_crusher_idle);
		if (NCConfig.register_processor[19]) registerBlock(rock_crusher_active);
		
		registerBlock(machine_interface);
		
		registerBlock(fission_controller_idle, TextFormatting.RED);
		registerBlock(fission_controller_active);
		registerBlock(fission_controller_new_idle, TextFormatting.RED);
		registerBlock(fission_controller_new_active);
		registerBlock(fission_controller_new_fixed);
		registerBlock(fission_port);
		
		registerBlock(fusion_core);
		registerBlock(fusion_dummy_side);
		registerBlock(fusion_dummy_top);
		registerBlock(fusion_connector);
		
		registerBlock(rtg_uranium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[0], 5, "RF/t")));
		registerBlock(rtg_plutonium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[1], 5, "RF/t")));
		registerBlock(rtg_americium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[2], 5, "RF/t")));
		registerBlock(rtg_californium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[3], 5, "RF/t")));
		
		registerBlock(solar_panel_basic, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[0], 5, "RF/t")));
		registerBlock(solar_panel_advanced, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[1], 5, "RF/t")));
		registerBlock(solar_panel_du, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[2], 5, "RF/t")));
		registerBlock(solar_panel_elite, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[3], 5, "RF/t")));
		
		registerBlock(decay_generator);
		
		registerBlock(voltaic_pile_basic, new ItemBlockBattery(voltaic_pile_basic, BatteryType.VOLTAIC_PILE_BASIC, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		registerBlock(voltaic_pile_advanced, new ItemBlockBattery(voltaic_pile_advanced, BatteryType.VOLTAIC_PILE_ADVANCED, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		registerBlock(voltaic_pile_du, new ItemBlockBattery(voltaic_pile_du, BatteryType.VOLTAIC_PILE_DU, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		registerBlock(voltaic_pile_elite, new ItemBlockBattery(voltaic_pile_elite, BatteryType.VOLTAIC_PILE_ELITE, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		
		registerBlock(lithium_ion_battery_basic, new ItemBlockBattery(lithium_ion_battery_basic, BatteryType.LITHIUM_ION_BATTERY_BASIC, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		registerBlock(lithium_ion_battery_advanced, new ItemBlockBattery(lithium_ion_battery_advanced, BatteryType.LITHIUM_ION_BATTERY_ADVANCED, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		registerBlock(lithium_ion_battery_du, new ItemBlockBattery(lithium_ion_battery_du, BatteryType.LITHIUM_ION_BATTERY_DU, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		registerBlock(lithium_ion_battery_elite, new ItemBlockBattery(lithium_ion_battery_elite, BatteryType.LITHIUM_ION_BATTERY_ELITE, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		
		registerBlock(buffer);
		registerBlock(active_cooler);
		registerBlock(bin);
		
		registerBlock(fusion_electromagnet_idle, InfoHelper.formattedInfo(infoLine("fusion_electromagnet_idle"), UnitHelper.ratePrefix(NCConfig.fusion_electromagnet_power, 5, "RF")));
		registerBlock(fusion_electromagnet_active);
		registerBlock(fusion_electromagnet_transparent_idle, InfoHelper.formattedInfo(infoLine("fusion_electromagnet_idle"), UnitHelper.ratePrefix(NCConfig.fusion_electromagnet_power, 5, "RF")));
		registerBlock(fusion_electromagnet_transparent_active);
		
		registerBlock(salt_fission_controller);
		registerBlock(salt_fission_wall);
		registerBlock(salt_fission_glass);
		registerBlock(salt_fission_frame);
		registerBlock(salt_fission_beam);
		registerBlock(salt_fission_vent);
		registerBlock(salt_fission_vessel);
		registerBlock(salt_fission_heater);
		registerBlock(salt_fission_moderator);
		registerBlock(salt_fission_distributor);
		registerBlock(salt_fission_retriever);
		registerBlock(salt_fission_redstone_port);
		registerBlock(salt_fission_computer_port);
		
		registerBlock(heat_exchanger_controller);
		registerBlock(heat_exchanger_wall);
		registerBlock(heat_exchanger_glass);
		registerBlock(heat_exchanger_frame);
		registerBlock(heat_exchanger_vent);
		registerBlock(heat_exchanger_tube_copper, TextFormatting.AQUA, InfoHelper.formattedInfo(fixedLine("heat_exchanger_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[0]) + "%"), InfoHelper.formattedInfo(infoLine("heat_exchanger_tube")));
		registerBlock(heat_exchanger_tube_hard_carbon, TextFormatting.AQUA, InfoHelper.formattedInfo(fixedLine("heat_exchanger_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[1]) + "%"), InfoHelper.formattedInfo(infoLine("heat_exchanger_tube")));
		registerBlock(heat_exchanger_tube_thermoconducting, TextFormatting.AQUA, InfoHelper.formattedInfo(fixedLine("heat_exchanger_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[2]) + "%"), InfoHelper.formattedInfo(infoLine("heat_exchanger_tube")));
		registerBlock(heat_exchanger_condenser_tube_copper, TextFormatting.AQUA, InfoHelper.formattedInfo(fixedLine("heat_exchanger_condenser_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[0]) + "%"), InfoHelper.formattedInfo(infoLine("heat_exchanger_condenser_tube")));
		registerBlock(heat_exchanger_condenser_tube_hard_carbon, TextFormatting.AQUA, InfoHelper.formattedInfo(fixedLine("heat_exchanger_condenser_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[1]) + "%"), InfoHelper.formattedInfo(infoLine("heat_exchanger_condenser_tube")));
		registerBlock(heat_exchanger_condenser_tube_thermoconducting, TextFormatting.AQUA, InfoHelper.formattedInfo(fixedLine("heat_exchanger_condenser_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[2]) + "%"), InfoHelper.formattedInfo(infoLine("heat_exchanger_condenser_tube")));
		registerBlock(heat_exchanger_computer_port);
		
		registerBlock(turbine_controller);
		registerBlock(turbine_wall);
		registerBlock(turbine_glass);
		registerBlock(turbine_frame);
		registerBlock(turbine_rotor_shaft);
		registerBlock(turbine_rotor_blade_steel, TextFormatting.AQUA, new String[] {Lang.localise(fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D*NCConfig.turbine_blade_efficiency[0]) + "%"), Lang.localise(fixedLine("turbine_rotor_blade_expansion"), Math.round(100D*NCConfig.turbine_blade_expansion[0]) + "%")}, InfoHelper.formattedInfo(infoLine("turbine_rotor_blade"), UnitHelper.prefix(NCConfig.turbine_mb_per_blade, 5, "B/t", -1)));
		registerBlock(turbine_rotor_blade_extreme, TextFormatting.AQUA, new String[] {Lang.localise(fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D*NCConfig.turbine_blade_efficiency[1]) + "%"), Lang.localise(fixedLine("turbine_rotor_blade_expansion"), Math.round(100D*NCConfig.turbine_blade_expansion[1]) + "%")}, InfoHelper.formattedInfo(infoLine("turbine_rotor_blade"), UnitHelper.prefix(NCConfig.turbine_mb_per_blade, 5, "B/t", -1)));
		registerBlock(turbine_rotor_blade_sic_sic_cmc, TextFormatting.AQUA, new String[] {Lang.localise(fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D*NCConfig.turbine_blade_efficiency[2]) + "%"), Lang.localise(fixedLine("turbine_rotor_blade_expansion"), Math.round(100D*NCConfig.turbine_blade_expansion[2]) + "%")}, InfoHelper.formattedInfo(infoLine("turbine_rotor_blade"), UnitHelper.prefix(NCConfig.turbine_mb_per_blade, 5, "B/t", -1)));
		registerBlock(turbine_rotor_stator, TextFormatting.AQUA, new String[] {Lang.localise(fixedLine("turbine_rotor_stator_expansion"), Math.round(100D*NCConfig.turbine_stator_expansion) + "%")}, InfoHelper.formattedInfo(infoLine("turbine_rotor_stator")));
		registerBlock(turbine_rotor_bearing);
		registerBlock(turbine_dynamo_coil, new ItemBlockMeta(turbine_dynamo_coil, TurbineDynamoCoilType.class, NCInfo.dynamoCoilInfo()));
		registerBlock(turbine_inlet);
		registerBlock(turbine_outlet);
		registerBlock(turbine_computer_port);
		
		registerBlock(accelerator_electromagnet_idle, InfoHelper.formattedInfo(infoLine("accelerator_electromagnet_idle"), UnitHelper.ratePrefix(NCConfig.accelerator_electromagnet_power, 5, "RF")));
		registerBlock(accelerator_electromagnet_active);
		registerBlock(electromagnet_supercooler_idle, InfoHelper.formattedInfo(infoLine("electromagnet_supercooler_idle"), UnitHelper.ratePrefix(NCConfig.accelerator_electromagnet_power, 5, "RF"), UnitHelper.ratePrefix(NCConfig.accelerator_supercooler_coolant, 5, "B", -1)));
		registerBlock(electromagnet_supercooler_active);
		
		if (NCConfig.register_passive[0]) {
			registerBlock(helium_collector, InfoHelper.formattedInfo(infoLine("helium_collector"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[0], 5, "B", -1)));
			registerBlock(helium_collector_compact, InfoHelper.formattedInfo(infoLine("helium_collector"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[0]*8, 5, "B", -1)));
			registerBlock(helium_collector_dense, InfoHelper.formattedInfo(infoLine("helium_collector"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[0]*64, 5, "B", -1)));
		}
		
		if (NCConfig.register_passive[1]) {
			registerBlock(cobblestone_generator, NCConfig.cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1], 5, Lang.localise("nuclearcraft.cobblestone")), UnitHelper.ratePrefix(NCConfig.cobble_gen_power, 5, "RF")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1], 5, Lang.localise("nuclearcraft.cobblestone"))));
			registerBlock(cobblestone_generator_compact, NCConfig.cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1]*8, 5, Lang.localise("nuclearcraft.cobblestone")), UnitHelper.ratePrefix(NCConfig.cobble_gen_power*8, 5, "RF")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1]*8, 5, Lang.localise("nuclearcraft.cobblestone"))));
			registerBlock(cobblestone_generator_dense, NCConfig.cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1]*64, 5, Lang.localise("nuclearcraft.cobblestone")), UnitHelper.ratePrefix(NCConfig.cobble_gen_power*64, 5, "RF")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", UnitHelper.ratePrefix(NCConfig.processor_passive_rate[1]*64, 5, Lang.localise("nuclearcraft.cobblestone"))));
		}
		
		if (NCConfig.register_passive[2]) {
			registerBlock(water_source, InfoHelper.formattedInfo(infoLine("water_source"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[2], 5, "B", -1)));
			registerBlock(water_source_compact, InfoHelper.formattedInfo(infoLine("water_source"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[2]*8, 5, "B", -1)));
			registerBlock(water_source_dense, InfoHelper.formattedInfo(infoLine("water_source"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[2]*64, 5, "B", -1)));
		}
		
		if (NCConfig.register_passive[3]) {
			registerBlock(nitrogen_collector, InfoHelper.formattedInfo(infoLine("nitrogen_collector"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[3], 5, "B", -1)));
			registerBlock(nitrogen_collector_compact, InfoHelper.formattedInfo(infoLine("nitrogen_collector"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[3]*8, 5, "B", -1)));
			registerBlock(nitrogen_collector_dense, InfoHelper.formattedInfo(infoLine("nitrogen_collector"), UnitHelper.ratePrefix(NCConfig.processor_passive_rate[3]*64, 5, "B", -1)));
		}
		
		registerBlock(radiation_scrubber, NCConfig.radiation_scrubber_alt ? InfoHelper.formattedInfo(infoLine("radiation_scrubber_alt"), NCMath.decimalPlaces(100D*RadiationHelper.getAltScrubbingFraction(1D), 1) + "%", NCConfig.radiation_scrubber_borax_rate > 0 ? Lang.localise("info.nuclearcraft.radiation_scrubber_req_borax", NCConfig.radiation_scrubber_power/20, NCConfig.radiation_scrubber_borax_rate) : Lang.localise("info.nuclearcraft.radiation_scrubber_no_req_borax", NCConfig.radiation_scrubber_power/20)) : InfoHelper.formattedInfo(infoLine("radiation_scrubber"), NCMath.decimalPlaces(100D*NCConfig.radiation_scrubber_fraction, 1) + "%", NCConfig.radiation_scrubber_borax_rate > 0 ? Lang.localise("info.nuclearcraft.radiation_scrubber_req_borax", NCConfig.radiation_scrubber_power/20, NCConfig.radiation_scrubber_borax_rate) : Lang.localise("info.nuclearcraft.radiation_scrubber_no_req_borax", NCConfig.radiation_scrubber_power/20)));
		
		registerBlock(geiger_block);
		
		registerBlock(glowing_mushroom);
		registerBlock(dry_earth);
		
		registerBlock(tritium_lamp);
		
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
		
		if (NCConfig.register_processor[0]) registerRender(nuclear_furnace_idle);
		if (NCConfig.register_processor[0]) registerRender(nuclear_furnace_active);
		if (NCConfig.register_processor[1]) registerRender(manufactory_idle);
		if (NCConfig.register_processor[1]) registerRender(manufactory_active);
		if (NCConfig.register_processor[2]) registerRender(isotope_separator_idle);
		if (NCConfig.register_processor[2]) registerRender(isotope_separator_active);
		if (NCConfig.register_processor[3]) registerRender(decay_hastener_idle);
		if (NCConfig.register_processor[3]) registerRender(decay_hastener_active);
		if (NCConfig.register_processor[4]) registerRender(fuel_reprocessor_idle);
		if (NCConfig.register_processor[4]) registerRender(fuel_reprocessor_active);
		if (NCConfig.register_processor[5]) registerRender(alloy_furnace_idle);
		if (NCConfig.register_processor[5]) registerRender(alloy_furnace_active);
		if (NCConfig.register_processor[6]) registerRender(infuser_idle);
		if (NCConfig.register_processor[6]) registerRender(infuser_active);
		if (NCConfig.register_processor[7]) registerRender(melter_idle);
		if (NCConfig.register_processor[7]) registerRender(melter_active);
		if (NCConfig.register_processor[8]) registerRender(supercooler_idle);
		if (NCConfig.register_processor[8]) registerRender(supercooler_active);
		if (NCConfig.register_processor[9]) registerRender(electrolyser_idle);
		if (NCConfig.register_processor[9]) registerRender(electrolyser_active);
		if (NCConfig.register_processor[10]) registerRender(irradiator_idle);
		if (NCConfig.register_processor[10]) registerRender(irradiator_active);
		if (NCConfig.register_processor[11]) registerRender(ingot_former_idle);
		if (NCConfig.register_processor[11]) registerRender(ingot_former_active);
		if (NCConfig.register_processor[12]) registerRender(pressurizer_idle);
		if (NCConfig.register_processor[12]) registerRender(pressurizer_active);
		if (NCConfig.register_processor[13]) registerRender(chemical_reactor_idle);
		if (NCConfig.register_processor[13]) registerRender(chemical_reactor_active);
		if (NCConfig.register_processor[14]) registerRender(salt_mixer_idle);
		if (NCConfig.register_processor[14]) registerRender(salt_mixer_active);
		if (NCConfig.register_processor[15]) registerRender(crystallizer_idle);
		if (NCConfig.register_processor[15]) registerRender(crystallizer_active);
		if (NCConfig.register_processor[16]) registerRender(dissolver_idle);
		if (NCConfig.register_processor[16]) registerRender(dissolver_active);
		if (NCConfig.register_processor[17]) registerRender(extractor_idle);
		if (NCConfig.register_processor[17]) registerRender(extractor_active);
		if (NCConfig.register_processor[18]) registerRender(centrifuge_idle);
		if (NCConfig.register_processor[18]) registerRender(centrifuge_active);
		if (NCConfig.register_processor[19]) registerRender(rock_crusher_idle);
		if (NCConfig.register_processor[19]) registerRender(rock_crusher_active);
		
		registerRender(machine_interface);
		
		registerRender(fission_controller_idle);
		registerRender(fission_controller_active);
		registerRender(fission_controller_new_idle);
		registerRender(fission_controller_new_active);
		registerRender(fission_controller_new_fixed, 0, "fission_controller_new_fixed_off");
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
		registerRender(solar_panel_advanced);
		registerRender(solar_panel_du);
		registerRender(solar_panel_elite);
		
		registerRender(decay_generator);
		
		registerRender(voltaic_pile_basic);
		registerRender(voltaic_pile_advanced);
		registerRender(voltaic_pile_du);
		registerRender(voltaic_pile_elite);
		
		registerRender(lithium_ion_battery_basic);
		registerRender(lithium_ion_battery_advanced);
		registerRender(lithium_ion_battery_du);
		registerRender(lithium_ion_battery_elite);
		
		registerRender(buffer);
		registerRender(active_cooler);
		registerRender(bin);
		
		registerRender(fusion_electromagnet_idle);
		registerRender(fusion_electromagnet_active);
		registerRender(fusion_electromagnet_transparent_idle);
		registerRender(fusion_electromagnet_transparent_active);
		
		registerRender(salt_fission_controller, 0, "salt_fission_controller_off");
		registerRender(salt_fission_wall);
		registerRender(salt_fission_glass);
		registerRender(salt_fission_frame);
		registerRender(salt_fission_beam);
		registerRender(salt_fission_vent);
		registerRender(salt_fission_vessel);
		registerRender(salt_fission_heater);
		registerRender(salt_fission_moderator);
		registerRender(salt_fission_distributor);
		registerRender(salt_fission_retriever);
		registerRender(salt_fission_redstone_port, 0, "salt_fission_redstone_port_off");
		registerRender(salt_fission_computer_port);
		
		registerRender(heat_exchanger_controller, 0, "heat_exchanger_controller_off");
		registerRender(heat_exchanger_wall);
		registerRender(heat_exchanger_glass);
		registerRender(heat_exchanger_frame);
		registerRender(heat_exchanger_vent);
		registerRender(heat_exchanger_tube_copper);
		registerRender(heat_exchanger_tube_hard_carbon);
		registerRender(heat_exchanger_tube_thermoconducting);
		registerRender(heat_exchanger_condenser_tube_copper);
		registerRender(heat_exchanger_condenser_tube_hard_carbon);
		registerRender(heat_exchanger_condenser_tube_thermoconducting);
		registerRender(heat_exchanger_computer_port);
		
		registerRender(turbine_controller, 0, "turbine_controller_off");
		registerRender(turbine_wall);
		registerRender(turbine_glass);
		registerRender(turbine_frame);
		registerRender(turbine_rotor_shaft);
		registerRender(turbine_rotor_blade_steel);
		registerRender(turbine_rotor_blade_extreme);
		registerRender(turbine_rotor_blade_sic_sic_cmc);
		registerRender(turbine_rotor_stator);
		registerRender(turbine_rotor_bearing);
		for (int i = 0; i < TurbineDynamoCoilType.values().length; i++) {
			registerRender(turbine_dynamo_coil, i, "turbine_dynamo_coil_" + TurbineDynamoCoilType.values()[i].getName());
		}
		registerRender(turbine_inlet);
		registerRender(turbine_outlet);
		registerRender(turbine_computer_port);
		
		registerRender(accelerator_electromagnet_idle);
		registerRender(accelerator_electromagnet_active);
		registerRender(electromagnet_supercooler_idle);
		registerRender(electromagnet_supercooler_active);
		
		if (NCConfig.register_passive[0]) {
			registerRender(helium_collector);
			registerRender(helium_collector_compact);
			registerRender(helium_collector_dense);
		}
		
		if (NCConfig.register_passive[1]) {
			registerRender(cobblestone_generator);
			registerRender(cobblestone_generator_compact);
			registerRender(cobblestone_generator_dense);
		}
		
		if (NCConfig.register_passive[2]) {
			registerRender(water_source);
			registerRender(water_source_compact);
			registerRender(water_source_dense);
		}
		
		if (NCConfig.register_passive[3]) {
			registerRender(nitrogen_collector);
			registerRender(nitrogen_collector_compact);
			registerRender(nitrogen_collector_dense);
		}
		
		registerRender(radiation_scrubber);
		
		registerRender(geiger_block);
		
		registerRender(glowing_mushroom);
		registerRender(dry_earth);
		
		registerRender(tritium_lamp);
		
		//registerRender(spin);
	}
	
	public static Block withName(Block block, String name) {
		return block.setTranslationKey(Global.MOD_ID + "." + name).setRegistryName(new ResourceLocation(Global.MOD_ID, name));
	}
	
	public static <T extends Block & ITileType> Block withName(T block) {
		return block.setTranslationKey(Global.MOD_ID + "." + block.getTileName()).setRegistryName(new ResourceLocation(Global.MOD_ID, block.getTileName()));
	}
	
	public static String fixedLine(String name) {
		return "tile." + Global.MOD_ID + "." + name + ".fixd";
	}
	
	public static String infoLine(String name) {
		return "tile." + Global.MOD_ID + "." + name + ".desc";
	}
	
	public static void registerBlock(Block block, String... info) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, info).setRegistryName(block.getRegistryName()));
	}
	
	public static void registerBlock(Block block, TextFormatting fixedColor, String... info) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, fixedColor, info).setRegistryName(block.getRegistryName()));
	}
	
	public static void registerBlock(Block block, TextFormatting fixedColor, String[] fixedTooltip, String... info) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, fixedColor, fixedTooltip, info).setRegistryName(block.getRegistryName()));
	}
	
	public static void registerBlock(Block block, ItemBlock itemBlock) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock.setRegistryName(block.getRegistryName()));
	}
	
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	public static void registerRender(Block block, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fileName), "inventory"));
	}
}
