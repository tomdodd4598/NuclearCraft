package nc.init;

import nc.Global;
import nc.NCInfo;
import nc.block.BlockMeta;
import nc.block.NCBlock;
import nc.block.NCBlockIce;
import nc.block.NCBlockMushroom;
import nc.block.item.ItemBlockMeta;
import nc.block.item.NCItemBlock;
import nc.block.item.energy.ItemBlockBattery;
import nc.block.tile.BlockSimpleTile;
import nc.block.tile.ITileType;
import nc.block.tile.dummy.BlockMachineInterface;
import nc.block.tile.processor.BlockNuclearFurnace;
import nc.block.tile.processor.BlockProcessor;
import nc.block.tile.radiation.BlockGeigerCounter;
import nc.block.tile.radiation.BlockScrubber;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ProcessorType;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.enumm.MetaEnums;
import nc.multiblock.battery.BatteryType;
import nc.multiblock.battery.block.BlockBattery;
import nc.multiblock.fission.block.BlockFissionCasing;
import nc.multiblock.fission.block.BlockFissionComputerPort;
import nc.multiblock.fission.block.BlockFissionConductor;
import nc.multiblock.fission.block.BlockFissionGlass;
import nc.multiblock.fission.block.BlockFissionIrradiator;
import nc.multiblock.fission.block.BlockFissionMonitor;
import nc.multiblock.fission.block.BlockFissionPowerPort;
import nc.multiblock.fission.block.BlockFissionShield;
import nc.multiblock.fission.block.BlockFissionSource;
import nc.multiblock.fission.block.BlockFissionVent;
import nc.multiblock.fission.block.manager.BlockFissionShieldManager;
import nc.multiblock.fission.block.port.BlockFissionCellPort;
import nc.multiblock.fission.block.port.BlockFissionHeaterPort;
import nc.multiblock.fission.block.port.BlockFissionHeaterPort2;
import nc.multiblock.fission.block.port.BlockFissionIrradiatorPort;
import nc.multiblock.fission.block.port.BlockFissionVesselPort;
import nc.multiblock.fission.salt.block.BlockSaltFissionController;
import nc.multiblock.fission.salt.block.BlockSaltFissionHeater;
import nc.multiblock.fission.salt.block.BlockSaltFissionHeater2;
import nc.multiblock.fission.salt.block.BlockSaltFissionVessel;
import nc.multiblock.fission.solid.block.BlockSolidFissionCell;
import nc.multiblock.fission.solid.block.BlockSolidFissionController;
import nc.multiblock.fission.solid.block.BlockSolidFissionSink;
import nc.multiblock.fission.solid.block.BlockSolidFissionSink2;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.multiblock.heatExchanger.block.BlockCondenserController;
import nc.multiblock.heatExchanger.block.BlockCondenserTube;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerCasing;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerComputerPort;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerController;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerGlass;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerTube;
import nc.multiblock.heatExchanger.block.BlockHeatExchangerVent;
import nc.multiblock.qComputer.QuantumComputerGateEnums;
import nc.multiblock.qComputer.block.BlockQuantumComputerConnector;
import nc.multiblock.qComputer.block.BlockQuantumComputerController;
import nc.multiblock.qComputer.block.BlockQuantumComputerGate;
import nc.multiblock.qComputer.block.BlockQuantumComputerQubit;
import nc.multiblock.rtg.RTGType;
import nc.multiblock.rtg.block.BlockRTG;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.block.BlockTurbineCasing;
import nc.multiblock.turbine.block.BlockTurbineCoilConnector;
import nc.multiblock.turbine.block.BlockTurbineComputerPort;
import nc.multiblock.turbine.block.BlockTurbineController;
import nc.multiblock.turbine.block.BlockTurbineDynamoCoil;
import nc.multiblock.turbine.block.BlockTurbineGlass;
import nc.multiblock.turbine.block.BlockTurbineInlet;
import nc.multiblock.turbine.block.BlockTurbineOutlet;
import nc.multiblock.turbine.block.BlockTurbineRotorBearing;
import nc.multiblock.turbine.block.BlockTurbineRotorBlade;
import nc.multiblock.turbine.block.BlockTurbineRotorShaft;
import nc.multiblock.turbine.block.BlockTurbineRotorStator;
import nc.radiation.RadiationHelper;
import nc.tab.NCTabs;
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
	
	public static Block fertile_isotope;
	
	public static Block supercold_ice;
	
	public static Block heavy_water_moderator;
	
	public static Block nuclear_furnace;
	public static Block manufactory;
	public static Block separator;
	public static Block decay_hastener;
	public static Block fuel_reprocessor;
	public static Block alloy_furnace;
	public static Block infuser;
	public static Block melter;
	public static Block supercooler;
	public static Block electrolyzer;
	public static Block assembler;
	public static Block ingot_former;
	public static Block pressurizer;
	public static Block chemical_reactor;
	public static Block salt_mixer;
	public static Block crystallizer;
	public static Block enricher;
	public static Block extractor;
	public static Block centrifuge;
	public static Block rock_crusher;
	
	public static Block machine_interface;
	
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
	
	public static Block bin;
	
	public static Block fission_casing;
	public static Block fission_glass;
	public static Block fission_conductor;
	public static Block fission_monitor;
	public static Block fission_reflector;
	public static Block fission_power_port;
	public static Block fission_vent;
	public static Block fission_irradiator;
	public static Block fission_source;
	public static Block fission_shield;
	public static Block fission_computer_port;
	
	public static Block fission_irradiator_port;
	
	public static Block fission_cell_port;
	
	public static Block fission_vessel_port;
	public static Block fission_heater_port;
	public static Block fission_heater_port2;
	
	public static Block fission_shield_manager;
	
	public static Block solid_fission_controller;
	public static Block solid_fission_cell;
	public static Block solid_fission_sink;
	public static Block solid_fission_sink2;
	
	public static Block salt_fission_controller;
	public static Block salt_fission_vessel;
	public static Block salt_fission_heater;
	public static Block salt_fission_heater2;
	
	public static Block heat_exchanger_controller;
	public static Block heat_exchanger_casing;
	public static Block heat_exchanger_glass;
	public static Block heat_exchanger_vent;
	public static Block heat_exchanger_tube_copper;
	public static Block heat_exchanger_tube_hard_carbon;
	public static Block heat_exchanger_tube_thermoconducting;
	public static Block heat_exchanger_computer_port;
	
	public static Block condenser_controller;
	public static Block condenser_tube_copper;
	public static Block condenser_tube_hard_carbon;
	public static Block condenser_tube_thermoconducting;
	
	public static Block turbine_controller;
	public static Block turbine_casing;
	public static Block turbine_glass;
	public static Block turbine_rotor_shaft;
	public static Block turbine_rotor_blade_steel;
	public static Block turbine_rotor_blade_extreme;
	public static Block turbine_rotor_blade_sic_sic_cmc;
	public static Block turbine_rotor_stator;
	public static Block turbine_rotor_bearing;
	public static Block turbine_dynamo_coil;
	public static Block turbine_coil_connector;
	public static Block turbine_inlet;
	public static Block turbine_outlet;
	public static Block turbine_computer_port;
	
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
	public static Block wasteland_earth;
	
	public static Block tritium_lamp;
	public static Block tritium_lantern;
	
	public static Block quantum_computer_controller;
	public static Block quantum_computer_qubit;
	
	public static Block quantum_computer_gate_single;
	public static Block quantum_computer_gate_control;
	public static Block quantum_computer_gate_swap;
	
	public static Block quantum_computer_connector;
	//public static Block quantum_computer_port;
	
	public static void init() {
		ore = withName(new BlockMeta.BlockOre(), "ore");
		ingot_block = withName(new BlockMeta.BlockIngot(), "ingot_block");
		
		fertile_isotope = withName(new BlockMeta.BlockFertileIsotope(), "fertile_isotope");
		
		supercold_ice = withName(new NCBlockIce(0.999F).setCreativeTab(NCTabs.MATERIAL), "supercold_ice");
		
		heavy_water_moderator = withName(new NCBlock(Material.IRON), "heavy_water_moderator");
		
		if (NCConfig.register_processor[0]) nuclear_furnace = withName(new BlockNuclearFurnace(), "nuclear_furnace");
		if (NCConfig.register_processor[1]) manufactory = withName(new BlockProcessor(ProcessorType.MANUFACTORY));
		if (NCConfig.register_processor[2]) separator = withName(new BlockProcessor(ProcessorType.SEPARATOR));
		if (NCConfig.register_processor[3]) decay_hastener = withName(new BlockProcessor(ProcessorType.DECAY_HASTENER));
		if (NCConfig.register_processor[4]) fuel_reprocessor = withName(new BlockProcessor(ProcessorType.FUEL_REPROCESSOR));
		if (NCConfig.register_processor[5]) alloy_furnace = withName(new BlockProcessor(ProcessorType.ALLOY_FURNACE));
		if (NCConfig.register_processor[6]) infuser = withName(new BlockProcessor(ProcessorType.INFUSER));
		if (NCConfig.register_processor[7]) melter = withName(new BlockProcessor(ProcessorType.MELTER));
		if (NCConfig.register_processor[8]) supercooler = withName(new BlockProcessor(ProcessorType.SUPERCOOLER));
		if (NCConfig.register_processor[9]) electrolyzer = withName(new BlockProcessor(ProcessorType.ELECTROLYZER));
		if (NCConfig.register_processor[10]) assembler = withName(new BlockProcessor(ProcessorType.ASSEMBLER));
		if (NCConfig.register_processor[11]) ingot_former = withName(new BlockProcessor(ProcessorType.INGOT_FORMER));
		if (NCConfig.register_processor[12]) pressurizer = withName(new BlockProcessor(ProcessorType.PRESSURIZER));
		if (NCConfig.register_processor[13]) chemical_reactor = withName(new BlockProcessor(ProcessorType.CHEMICAL_REACTOR));
		if (NCConfig.register_processor[14]) salt_mixer = withName(new BlockProcessor(ProcessorType.SALT_MIXER));
		if (NCConfig.register_processor[15]) crystallizer = withName(new BlockProcessor(ProcessorType.CRYSTALLIZER));
		if (NCConfig.register_processor[16]) enricher = withName(new BlockProcessor(ProcessorType.ENRICHER));
		if (NCConfig.register_processor[17]) extractor = withName(new BlockProcessor(ProcessorType.EXTRACTOR));
		if (NCConfig.register_processor[18]) centrifuge = withName(new BlockProcessor(ProcessorType.CENTRIFUGE));
		if (NCConfig.register_processor[19]) rock_crusher = withName(new BlockProcessor(ProcessorType.ROCK_CRUSHER));
		
		machine_interface = withName(new BlockMachineInterface(SimpleTileType.MACHINE_INTERFACE));
		
		rtg_uranium = withName(new BlockRTG(RTGType.URANIUM), "rtg_uranium");
		rtg_plutonium = withName(new BlockRTG(RTGType.PLUTONIUM), "rtg_plutonium");
		rtg_americium = withName(new BlockRTG(RTGType.AMERICIUM), "rtg_americium");
		rtg_californium = withName(new BlockRTG(RTGType.CALIFORNIUM), "rtg_californium");
		
		solar_panel_basic = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_BASIC));
		solar_panel_advanced = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_ADVANCED));
		solar_panel_du = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_DU));
		solar_panel_elite = withName(new BlockSimpleTile(SimpleTileType.SOLAR_PANEL_ELITE));
		
		decay_generator = withName(new BlockSimpleTile(SimpleTileType.DECAY_GENERATOR));
		
		if (NCConfig.register_battery[0]) {
			voltaic_pile_basic = withName(new BlockBattery(BatteryType.VOLTAIC_PILE_BASIC), "voltaic_pile_basic");
			voltaic_pile_advanced = withName(new BlockBattery(BatteryType.VOLTAIC_PILE_ADVANCED), "voltaic_pile_advanced");
			voltaic_pile_du = withName(new BlockBattery(BatteryType.VOLTAIC_PILE_DU), "voltaic_pile_du");
			voltaic_pile_elite = withName(new BlockBattery(BatteryType.VOLTAIC_PILE_ELITE), "voltaic_pile_elite");
		}
		
		if (NCConfig.register_battery[1]) {
			lithium_ion_battery_basic = withName(new BlockBattery(BatteryType.LITHIUM_ION_BATTERY_BASIC), "lithium_ion_battery_basic");
			lithium_ion_battery_advanced = withName(new BlockBattery(BatteryType.LITHIUM_ION_BATTERY_ADVANCED), "lithium_ion_battery_advanced");
			lithium_ion_battery_du = withName(new BlockBattery(BatteryType.LITHIUM_ION_BATTERY_DU), "lithium_ion_battery_du");
			lithium_ion_battery_elite = withName(new BlockBattery(BatteryType.LITHIUM_ION_BATTERY_ELITE), "lithium_ion_battery_elite");	
		}
		
		bin = withName(new BlockSimpleTile(SimpleTileType.BIN));
		
		fission_casing = withName(new BlockFissionCasing(), "fission_casing");
		fission_glass = withName(new BlockFissionGlass(), "fission_glass");
		fission_conductor = withName(new BlockFissionConductor(), "fission_conductor");
		fission_monitor = withName(new BlockFissionMonitor(), "fission_monitor");
		fission_reflector = withName(new BlockMeta.BlockFissionReflector(), "fission_reflector");
		fission_power_port = withName(new BlockFissionPowerPort(), "fission_power_port");
		fission_vent = withName(new BlockFissionVent(), "fission_vent");
		fission_irradiator = withName(new BlockFissionIrradiator(), "fission_irradiator");
		fission_source = withName(new BlockFissionSource(), "fission_source");
		fission_shield = withName(new BlockFissionShield(), "fission_shield");
		fission_computer_port = withName(new BlockFissionComputerPort(), "fission_computer_port");
		
		fission_irradiator_port = withName(new BlockFissionIrradiatorPort(), "fission_irradiator_port");
		
		fission_cell_port = withName(new BlockFissionCellPort(), "fission_cell_port");
		
		fission_vessel_port = withName(new BlockFissionVesselPort(), "fission_vessel_port");
		fission_heater_port = withName(new BlockFissionHeaterPort(), "fission_heater_port");
		fission_heater_port2 = withName(new BlockFissionHeaterPort2(), "fission_heater_port2");
		
		fission_shield_manager = withName(new BlockFissionShieldManager(), "fission_shield_manager");
		
		solid_fission_controller = withName(new BlockSolidFissionController(), "solid_fission_controller");
		solid_fission_cell = withName(new BlockSolidFissionCell(), "solid_fission_cell");
		solid_fission_sink = withName(new BlockSolidFissionSink(), "solid_fission_sink");
		solid_fission_sink2 = withName(new BlockSolidFissionSink2(), "solid_fission_sink2");
		
		salt_fission_controller = withName(new BlockSaltFissionController(), "salt_fission_controller");
		salt_fission_vessel = withName(new BlockSaltFissionVessel(), "salt_fission_vessel");
		salt_fission_heater = withName(new BlockSaltFissionHeater(), "salt_fission_heater");
		salt_fission_heater2 = withName(new BlockSaltFissionHeater2(), "salt_fission_heater2");
		
		heat_exchanger_controller = withName(new BlockHeatExchangerController(), "heat_exchanger_controller");
		heat_exchanger_casing = withName(new BlockHeatExchangerCasing(), "heat_exchanger_casing");
		heat_exchanger_glass = withName(new BlockHeatExchangerGlass(), "heat_exchanger_glass");
		heat_exchanger_vent = withName(new BlockHeatExchangerVent(), "heat_exchanger_vent");
		heat_exchanger_tube_copper = withName(new BlockHeatExchangerTube(HeatExchangerTubeType.COPPER), "heat_exchanger_tube_copper");
		heat_exchanger_tube_hard_carbon = withName(new BlockHeatExchangerTube(HeatExchangerTubeType.HARD_CARBON), "heat_exchanger_tube_hard_carbon");
		heat_exchanger_tube_thermoconducting = withName(new BlockHeatExchangerTube(HeatExchangerTubeType.THERMOCONDUCTING), "heat_exchanger_tube_thermoconducting");
		heat_exchanger_computer_port = withName(new BlockHeatExchangerComputerPort(), "heat_exchanger_computer_port");
		
		condenser_controller = withName(new BlockCondenserController(), "condenser_controller");
		condenser_tube_copper = withName(new BlockCondenserTube(HeatExchangerTubeType.COPPER), "condenser_tube_copper");
		condenser_tube_hard_carbon = withName(new BlockCondenserTube(HeatExchangerTubeType.HARD_CARBON), "condenser_tube_hard_carbon");
		condenser_tube_thermoconducting = withName(new BlockCondenserTube(HeatExchangerTubeType.THERMOCONDUCTING), "condenser_tube_thermoconducting");
		
		turbine_controller = withName(new BlockTurbineController(), "turbine_controller");
		turbine_casing = withName(new BlockTurbineCasing(), "turbine_casing");
		turbine_glass = withName(new BlockTurbineGlass(), "turbine_glass");
		turbine_rotor_shaft = withName(new BlockTurbineRotorShaft(), "turbine_rotor_shaft");
		turbine_rotor_blade_steel = withName(new BlockTurbineRotorBlade(TurbineRotorBladeType.STEEL), "turbine_rotor_blade_steel");
		turbine_rotor_blade_extreme = withName(new BlockTurbineRotorBlade(TurbineRotorBladeType.EXTREME), "turbine_rotor_blade_extreme");
		turbine_rotor_blade_sic_sic_cmc = withName(new BlockTurbineRotorBlade(TurbineRotorBladeType.SIC_SIC_CMC), "turbine_rotor_blade_sic_sic_cmc");
		turbine_rotor_stator = withName(new BlockTurbineRotorStator(), "turbine_rotor_stator");
		turbine_rotor_bearing = withName(new BlockTurbineRotorBearing(), "turbine_rotor_bearing");
		turbine_dynamo_coil = withName(new BlockTurbineDynamoCoil(), "turbine_dynamo_coil");
		turbine_coil_connector = withName(new BlockTurbineCoilConnector(), "turbine_coil_connector");
		turbine_inlet = withName(new BlockTurbineInlet(), "turbine_inlet");
		turbine_outlet = withName(new BlockTurbineOutlet(), "turbine_outlet");
		turbine_computer_port = withName(new BlockTurbineComputerPort(), "turbine_computer_port");
		
		if (NCConfig.register_passive[0]) {
			cobblestone_generator = withName(new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR));
			cobblestone_generator_compact = withName(new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR_COMPACT));
			cobblestone_generator_dense = withName(new BlockSimpleTile(SimpleTileType.COBBLESTONE_GENERATOR_DENSE));
		}
		
		if (NCConfig.register_passive[1]) {
			water_source = withName(new BlockSimpleTile(SimpleTileType.WATER_SOURCE));
			water_source_compact = withName(new BlockSimpleTile(SimpleTileType.WATER_SOURCE_COMPACT));
			water_source_dense = withName(new BlockSimpleTile(SimpleTileType.WATER_SOURCE_DENSE));
		}
		
		if (NCConfig.register_passive[2]) {
			nitrogen_collector = withName(new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR));
			nitrogen_collector_compact = withName(new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR_COMPACT));
			nitrogen_collector_dense = withName(new BlockSimpleTile(SimpleTileType.NITROGEN_COLLECTOR_DENSE));
		}
		
		radiation_scrubber = withName(new BlockScrubber(), "radiation_scrubber");
		
		geiger_block = withName(new BlockGeigerCounter(), "geiger_block");
		
		glowing_mushroom = withName(new NCBlockMushroom().setCreativeTab(NCTabs.RADIATION).setLightLevel(1F), "glowing_mushroom");
		wasteland_earth = withName(new NCBlock(Material.ROCK).setCreativeTab(NCTabs.RADIATION), "wasteland_earth");
		
		tritium_lamp = withName(new NCBlock(Material.GLASS).setCreativeTab(NCTabs.MISC).setLightLevel(1F), "tritium_lamp");
		
		if (NCConfig.register_quantum) {
			quantum_computer_controller = withName(new BlockQuantumComputerController(), "quantum_computer_controller");
			quantum_computer_qubit = withName(new BlockQuantumComputerQubit(), "quantum_computer_qubit");
			
			quantum_computer_gate_single = withName(new BlockQuantumComputerGate.Single(), "quantum_computer_gate_single");
			quantum_computer_gate_control = withName(new BlockQuantumComputerGate.Control(), "quantum_computer_gate_control");
			quantum_computer_gate_swap = withName(new BlockQuantumComputerGate.Swap(), "quantum_computer_gate_swap");
			
			quantum_computer_connector = withName(new BlockQuantumComputerConnector(), "quantum_computer_connector");
			//quantum_computer_port = withName(new BlockQuantumComputerPort(), "quantum_computer_port");
		}
	}
	
	public static void register() {
		registerBlock(ore, new ItemBlockMeta(ore, MetaEnums.OreType.class, TextFormatting.AQUA));
		registerBlock(ingot_block, new ItemBlockMeta(ingot_block, MetaEnums.IngotType.class, TextFormatting.AQUA));
		
		registerBlock(fertile_isotope, new ItemBlockMeta(fertile_isotope, MetaEnums.FertileIsotopeType.class, TextFormatting.AQUA));
		
		registerBlock(supercold_ice);
		
		registerBlock(heavy_water_moderator);
		
		if (NCConfig.register_processor[0]) registerBlock(nuclear_furnace);
		if (NCConfig.register_processor[1]) registerBlock(manufactory);
		if (NCConfig.register_processor[2]) registerBlock(separator);
		if (NCConfig.register_processor[3]) registerBlock(decay_hastener);
		if (NCConfig.register_processor[4]) registerBlock(fuel_reprocessor);
		if (NCConfig.register_processor[5]) registerBlock(alloy_furnace);
		if (NCConfig.register_processor[6]) registerBlock(infuser);
		if (NCConfig.register_processor[7]) registerBlock(melter);
		if (NCConfig.register_processor[8]) registerBlock(supercooler);
		if (NCConfig.register_processor[9]) registerBlock(electrolyzer);
		if (NCConfig.register_processor[10]) registerBlock(assembler);
		if (NCConfig.register_processor[11]) registerBlock(ingot_former);
		if (NCConfig.register_processor[12]) registerBlock(pressurizer);
		if (NCConfig.register_processor[13]) registerBlock(chemical_reactor);
		if (NCConfig.register_processor[14]) registerBlock(salt_mixer);
		if (NCConfig.register_processor[15]) registerBlock(crystallizer);
		if (NCConfig.register_processor[16]) registerBlock(enricher);
		if (NCConfig.register_processor[17]) registerBlock(extractor);
		if (NCConfig.register_processor[18]) registerBlock(centrifuge);
		if (NCConfig.register_processor[19]) registerBlock(rock_crusher);
		
		registerBlock(machine_interface);
		
		registerBlock(rtg_uranium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[0], 5, "RF/t")));
		registerBlock(rtg_plutonium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[1], 5, "RF/t")));
		registerBlock(rtg_americium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[2], 5, "RF/t")));
		registerBlock(rtg_californium, InfoHelper.formattedInfo(infoLine("rtg"), UnitHelper.prefix(NCConfig.rtg_power[3], 5, "RF/t")));
		
		registerBlock(solar_panel_basic, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[0], 5, "RF/t")));
		registerBlock(solar_panel_advanced, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[1], 5, "RF/t")));
		registerBlock(solar_panel_du, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[2], 5, "RF/t")));
		registerBlock(solar_panel_elite, InfoHelper.formattedInfo(infoLine("solar_panel"), UnitHelper.prefix(NCConfig.solar_power[3], 5, "RF/t")));
		
		registerBlock(decay_generator);
		
		if (NCConfig.register_battery[0]) {
			registerBlock(voltaic_pile_basic, new ItemBlockBattery(voltaic_pile_basic, BatteryType.VOLTAIC_PILE_BASIC, InfoHelper.formattedInfo(infoLine("energy_storage"))));
			registerBlock(voltaic_pile_advanced, new ItemBlockBattery(voltaic_pile_advanced, BatteryType.VOLTAIC_PILE_ADVANCED, InfoHelper.formattedInfo(infoLine("energy_storage"))));
			registerBlock(voltaic_pile_du, new ItemBlockBattery(voltaic_pile_du, BatteryType.VOLTAIC_PILE_DU, InfoHelper.formattedInfo(infoLine("energy_storage"))));
			registerBlock(voltaic_pile_elite, new ItemBlockBattery(voltaic_pile_elite, BatteryType.VOLTAIC_PILE_ELITE, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		}
		
		if (NCConfig.register_battery[1]) {
			registerBlock(lithium_ion_battery_basic, new ItemBlockBattery(lithium_ion_battery_basic, BatteryType.LITHIUM_ION_BATTERY_BASIC, InfoHelper.formattedInfo(infoLine("energy_storage"))));
			registerBlock(lithium_ion_battery_advanced, new ItemBlockBattery(lithium_ion_battery_advanced, BatteryType.LITHIUM_ION_BATTERY_ADVANCED, InfoHelper.formattedInfo(infoLine("energy_storage"))));
			registerBlock(lithium_ion_battery_du, new ItemBlockBattery(lithium_ion_battery_du, BatteryType.LITHIUM_ION_BATTERY_DU, InfoHelper.formattedInfo(infoLine("energy_storage"))));
			registerBlock(lithium_ion_battery_elite, new ItemBlockBattery(lithium_ion_battery_elite, BatteryType.LITHIUM_ION_BATTERY_ELITE, InfoHelper.formattedInfo(infoLine("energy_storage"))));
		}
		
		registerBlock(bin);
		
		registerBlock(fission_casing);
		registerBlock(fission_glass);
		registerBlock(fission_conductor);
		registerBlock(fission_monitor);
		registerBlock(fission_reflector, new ItemBlockMeta(fission_reflector, MetaEnums.NeutronReflectorType.class, TextFormatting.AQUA));
		registerBlock(fission_power_port);
		registerBlock(fission_vent);
		registerBlock(fission_irradiator);
		registerBlock(fission_source, new ItemBlockMeta(fission_source, MetaEnums.NeutronSourceType.class, TextFormatting.LIGHT_PURPLE, NCInfo.neutronSourceFixedInfo(), TextFormatting.AQUA, NCInfo.neutronSourceInfo()));
		registerBlock(fission_shield, new ItemBlockMeta(fission_shield, MetaEnums.NeutronShieldType.class, new TextFormatting[] {TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE}, NCInfo.neutronShieldFixedInfo(), TextFormatting.AQUA, NCInfo.neutronShieldInfo()));
		registerBlock(fission_computer_port);
		
		registerBlock(fission_irradiator_port);
		
		registerBlock(fission_cell_port);
		
		registerBlock(fission_vessel_port);
		registerBlock(fission_heater_port, new ItemBlockMeta(fission_heater_port, MetaEnums.CoolantHeaterType.class, TextFormatting.AQUA));
		registerBlock(fission_heater_port2, new ItemBlockMeta(fission_heater_port2, MetaEnums.CoolantHeaterType2.class, TextFormatting.AQUA));
		
		registerBlock(fission_shield_manager);
		
		registerBlock(solid_fission_controller);
		registerBlock(solid_fission_cell);
		registerBlock(solid_fission_sink, new ItemBlockMeta(solid_fission_sink, MetaEnums.HeatSinkType.class, TextFormatting.BLUE, NCInfo.heatSinkFixedInfo(), TextFormatting.AQUA, NCInfo.heatSinkInfo()));
		registerBlock(solid_fission_sink2, new ItemBlockMeta(solid_fission_sink2, MetaEnums.HeatSinkType2.class, TextFormatting.BLUE, NCInfo.heatSinkFixedInfo2(), TextFormatting.AQUA, NCInfo.heatSinkInfo2()));
		
		registerBlock(salt_fission_controller);
		registerBlock(salt_fission_vessel);
		registerBlock(salt_fission_heater, new ItemBlockMeta(salt_fission_heater, MetaEnums.CoolantHeaterType.class, TextFormatting.BLUE, NCInfo.coolantHeaterFixedInfo(), TextFormatting.AQUA, NCInfo.coolantHeaterInfo()));
		registerBlock(salt_fission_heater2, new ItemBlockMeta(salt_fission_heater2, MetaEnums.CoolantHeaterType2.class, TextFormatting.BLUE, NCInfo.coolantHeaterFixedInfo2(), TextFormatting.AQUA, NCInfo.coolantHeaterInfo2()));
		
		registerBlock(heat_exchanger_controller);
		registerBlock(heat_exchanger_casing);
		registerBlock(heat_exchanger_glass);
		registerBlock(heat_exchanger_vent);
		registerBlock(heat_exchanger_tube_copper, TextFormatting.BLUE, InfoHelper.formattedInfo(fixedLine("heat_exchanger_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[0]) + "%"), TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("heat_exchanger_tube")));
		registerBlock(heat_exchanger_tube_hard_carbon, TextFormatting.BLUE, InfoHelper.formattedInfo(fixedLine("heat_exchanger_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[1]) + "%"), TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("heat_exchanger_tube")));
		registerBlock(heat_exchanger_tube_thermoconducting, TextFormatting.BLUE, InfoHelper.formattedInfo(fixedLine("heat_exchanger_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[2]) + "%"), TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("heat_exchanger_tube")));
		registerBlock(heat_exchanger_computer_port);
		
		registerBlock(condenser_controller);
		registerBlock(condenser_tube_copper, TextFormatting.BLUE, InfoHelper.formattedInfo(fixedLine("condenser_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[0]) + "%"), TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("condenser_tube")));
		registerBlock(condenser_tube_hard_carbon, TextFormatting.BLUE, InfoHelper.formattedInfo(fixedLine("condenser_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[1]) + "%"), TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("condenser_tube")));
		registerBlock(condenser_tube_thermoconducting, TextFormatting.BLUE, InfoHelper.formattedInfo(fixedLine("condenser_tube"), Math.round(100D*NCConfig.heat_exchanger_conductivity[2]) + "%"), TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("condenser_tube")));
		
		registerBlock(turbine_controller);
		registerBlock(turbine_casing);
		registerBlock(turbine_glass);
		registerBlock(turbine_rotor_shaft);
		registerBlock(turbine_rotor_blade_steel, new TextFormatting[] {TextFormatting.LIGHT_PURPLE, TextFormatting.GRAY}, new String[] {Lang.localise(fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D*NCConfig.turbine_blade_efficiency[0]) + "%"), Lang.localise(fixedLine("turbine_rotor_blade_expansion"), Math.round(100D*NCConfig.turbine_blade_expansion[0]) + "%")}, TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("turbine_rotor_blade"), UnitHelper.prefix(NCConfig.turbine_mb_per_blade, 5, "B/t", -1)));
		registerBlock(turbine_rotor_blade_extreme, new TextFormatting[] {TextFormatting.LIGHT_PURPLE, TextFormatting.GRAY}, new String[] {Lang.localise(fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D*NCConfig.turbine_blade_efficiency[1]) + "%"), Lang.localise(fixedLine("turbine_rotor_blade_expansion"), Math.round(100D*NCConfig.turbine_blade_expansion[1]) + "%")}, TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("turbine_rotor_blade"), UnitHelper.prefix(NCConfig.turbine_mb_per_blade, 5, "B/t", -1)));
		registerBlock(turbine_rotor_blade_sic_sic_cmc, new TextFormatting[] {TextFormatting.LIGHT_PURPLE, TextFormatting.GRAY}, new String[] {Lang.localise(fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D*NCConfig.turbine_blade_efficiency[2]) + "%"), Lang.localise(fixedLine("turbine_rotor_blade_expansion"), Math.round(100D*NCConfig.turbine_blade_expansion[2]) + "%")}, TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("turbine_rotor_blade"), UnitHelper.prefix(NCConfig.turbine_mb_per_blade, 5, "B/t", -1)));
		registerBlock(turbine_rotor_stator, TextFormatting.GRAY, new String[] {Lang.localise(fixedLine("turbine_rotor_stator_expansion"), Math.round(100D*NCConfig.turbine_stator_expansion) + "%")}, TextFormatting.AQUA, InfoHelper.formattedInfo(infoLine("turbine_rotor_stator")));
		registerBlock(turbine_rotor_bearing);
		registerBlock(turbine_dynamo_coil, new ItemBlockMeta(turbine_dynamo_coil, TurbineDynamoCoilType.class, TextFormatting.LIGHT_PURPLE, NCInfo.dynamoCoilFixedInfo(), TextFormatting.AQUA, NCInfo.dynamoCoilInfo()));
		registerBlock(turbine_coil_connector);
		registerBlock(turbine_inlet);
		registerBlock(turbine_outlet);
		registerBlock(turbine_computer_port);
		
		if (NCConfig.register_passive[0]) {
			String cobblePerTick = " " + Lang.localise("nuclearcraft.cobblestone") + "/t";
			registerBlock(cobblestone_generator, NCConfig.cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", NCMath.sigFigs(NCConfig.processor_passive_rate[0], 5) + cobblePerTick, UnitHelper.prefix(NCConfig.cobble_gen_power, 5, "RF/t")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", NCMath.sigFigs(NCConfig.processor_passive_rate[0], 5) + cobblePerTick));
			registerBlock(cobblestone_generator_compact, NCConfig.cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", NCMath.sigFigs(NCConfig.processor_passive_rate[0]*8, 5) + cobblePerTick, UnitHelper.prefix(NCConfig.cobble_gen_power*8, 5, "RF/t")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", NCMath.sigFigs(NCConfig.processor_passive_rate[0]*8, 5) + cobblePerTick));
			registerBlock(cobblestone_generator_dense, NCConfig.cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", NCMath.sigFigs(NCConfig.processor_passive_rate[0]*64, 5) + cobblePerTick, UnitHelper.prefix(NCConfig.cobble_gen_power*64, 5, "RF/t")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", NCMath.sigFigs(NCConfig.processor_passive_rate[0]*64, 5) + cobblePerTick));
		}
		
		if (NCConfig.register_passive[1]) {
			registerBlock(water_source, InfoHelper.formattedInfo(infoLine("water_source"), UnitHelper.prefix(NCConfig.processor_passive_rate[1], 5, "B/t", -1)));
			registerBlock(water_source_compact, InfoHelper.formattedInfo(infoLine("water_source"), UnitHelper.prefix(NCConfig.processor_passive_rate[1]*8, 5, "B/t", -1)));
			registerBlock(water_source_dense, InfoHelper.formattedInfo(infoLine("water_source"), UnitHelper.prefix(NCConfig.processor_passive_rate[1]*64, 5, "B/t", -1)));
		}
		
		if (NCConfig.register_passive[2]) {
			registerBlock(nitrogen_collector, InfoHelper.formattedInfo(infoLine("nitrogen_collector"), UnitHelper.prefix(NCConfig.processor_passive_rate[2], 5, "B/t", -1)));
			registerBlock(nitrogen_collector_compact, InfoHelper.formattedInfo(infoLine("nitrogen_collector"), UnitHelper.prefix(NCConfig.processor_passive_rate[2]*8, 5, "B/t", -1)));
			registerBlock(nitrogen_collector_dense, InfoHelper.formattedInfo(infoLine("nitrogen_collector"), UnitHelper.prefix(NCConfig.processor_passive_rate[2]*64, 5, "B/t", -1)));
		}
		
		registerBlock(radiation_scrubber, NCConfig.radiation_scrubber_non_linear ? InfoHelper.formattedInfo(infoLine("radiation_scrubber_non_linear"), NCMath.decimalPlaces(100D*RadiationHelper.getAltScrubbingFraction(1D), 1) + "%", Lang.localise("nuclearcraft.one_hundred_percent")) : InfoHelper.formattedInfo(infoLine("radiation_scrubber"), NCMath.decimalPlaces(100D*NCConfig.radiation_scrubber_fraction, 1) + "%", Lang.localise("nuclearcraft.one_hundred_percent")));
		
		registerBlock(geiger_block);
		
		registerBlock(glowing_mushroom);
		registerBlock(wasteland_earth);
		
		registerBlock(tritium_lamp);
		
		if (NCConfig.register_quantum) {
			registerBlock(quantum_computer_controller);
			registerBlock(quantum_computer_qubit);
			
			registerBlock(quantum_computer_gate_single, new ItemBlockMeta(quantum_computer_gate_single, QuantumComputerGateEnums.SingleType.class, TextFormatting.AQUA));
			registerBlock(quantum_computer_gate_control, new ItemBlockMeta(quantum_computer_gate_control, QuantumComputerGateEnums.ControlType.class, TextFormatting.AQUA));
			registerBlock(quantum_computer_gate_swap, new ItemBlockMeta(quantum_computer_gate_swap, QuantumComputerGateEnums.SwapType.class, TextFormatting.AQUA));
			
			registerBlock(quantum_computer_connector);
			//registerBlock(quantum_computer_port);
		}
	}
	
	public static void registerRenders() {
		for (int i = 0; i < MetaEnums.OreType.values().length; i++) {
			registerRender(ore, i, MetaEnums.OreType.values()[i].getName());
		}
		for (int i = 0; i < MetaEnums.IngotType.values().length; i++) {
			registerRender(ingot_block, i, MetaEnums.IngotType.values()[i].getName());
		}
		
		for (int i = 0; i < MetaEnums.FertileIsotopeType.values().length; i++) {
			registerRender(fertile_isotope, i, MetaEnums.FertileIsotopeType.values()[i].getName());
		}
		
		registerRender(supercold_ice);
		
		registerRender(heavy_water_moderator);
		
		if (NCConfig.register_processor[0]) registerRender(nuclear_furnace);
		if (NCConfig.register_processor[1]) registerRender(manufactory);
		if (NCConfig.register_processor[2]) registerRender(separator);
		if (NCConfig.register_processor[3]) registerRender(decay_hastener);
		if (NCConfig.register_processor[4]) registerRender(fuel_reprocessor);
		if (NCConfig.register_processor[5]) registerRender(alloy_furnace);
		if (NCConfig.register_processor[6]) registerRender(infuser);
		if (NCConfig.register_processor[7]) registerRender(melter);
		if (NCConfig.register_processor[8]) registerRender(supercooler);
		if (NCConfig.register_processor[9]) registerRender(electrolyzer);
		if (NCConfig.register_processor[10]) registerRender(assembler);
		if (NCConfig.register_processor[11]) registerRender(ingot_former);
		if (NCConfig.register_processor[12]) registerRender(pressurizer);
		if (NCConfig.register_processor[13]) registerRender(chemical_reactor);
		if (NCConfig.register_processor[14]) registerRender(salt_mixer);
		if (NCConfig.register_processor[15]) registerRender(crystallizer);
		if (NCConfig.register_processor[16]) registerRender(enricher);
		if (NCConfig.register_processor[17]) registerRender(extractor);
		if (NCConfig.register_processor[18]) registerRender(centrifuge);
		if (NCConfig.register_processor[19]) registerRender(rock_crusher);
		
		registerRender(machine_interface);
		
		registerRender(rtg_uranium);
		registerRender(rtg_plutonium);
		registerRender(rtg_americium);
		registerRender(rtg_californium);
		
		registerRender(solar_panel_basic);
		registerRender(solar_panel_advanced);
		registerRender(solar_panel_du);
		registerRender(solar_panel_elite);
		
		registerRender(decay_generator);
		
		if (NCConfig.register_battery[0]) {
			registerRender(voltaic_pile_basic);
			registerRender(voltaic_pile_advanced);
			registerRender(voltaic_pile_du);
			registerRender(voltaic_pile_elite);
		}
		
		if (NCConfig.register_battery[1]) {
			registerRender(lithium_ion_battery_basic);
			registerRender(lithium_ion_battery_advanced);
			registerRender(lithium_ion_battery_du);
			registerRender(lithium_ion_battery_elite);
		}
		
		registerRender(bin);
		
		registerRender(fission_casing);
		registerRender(fission_glass);
		registerRender(fission_conductor);
		registerRender(fission_monitor);
		for (int i = 0; i < MetaEnums.NeutronReflectorType.values().length; i++) {
			registerRender(fission_reflector, i, MetaEnums.NeutronReflectorType.values()[i].getName());
		}
		registerRender(fission_power_port);
		registerRender(fission_vent);
		registerRender(fission_irradiator);
		for (int i = 0; i < MetaEnums.NeutronSourceType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fission_source), i, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fission_source.getRegistryName().getPath()), "active=false,facing=south,type=" + MetaEnums.NeutronSourceType.values()[i].getName()));
		}
		for (int i = 0; i < MetaEnums.NeutronShieldType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fission_shield), i, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fission_shield.getRegistryName().getPath()), "active=true,type=" + MetaEnums.NeutronShieldType.values()[i].getName()));
		}
		registerRender(fission_computer_port);
		
		registerRender(fission_irradiator_port);
		
		registerRender(fission_cell_port);
		
		registerRender(fission_vessel_port);
		for (int i = 0; i < MetaEnums.CoolantHeaterType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fission_heater_port), i, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fission_heater_port.getRegistryName().getPath()), "axis=z,type=" + MetaEnums.CoolantHeaterType.values()[i].getName()));
		}
		for (int i = 0; i < MetaEnums.CoolantHeaterType2.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fission_heater_port2), i, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fission_heater_port2.getRegistryName().getPath()), "axis=z,type=" + MetaEnums.CoolantHeaterType2.values()[i].getName()));
		}
		
		registerRender(fission_shield_manager);
		
		registerRender(solid_fission_controller);
		registerRender(solid_fission_cell);
		for (int i = 0; i < MetaEnums.HeatSinkType.values().length; i++) {
			registerRender(solid_fission_sink, i, MetaEnums.HeatSinkType.values()[i].getName());
		}
		for (int i = 0; i < MetaEnums.HeatSinkType2.values().length; i++) {
			registerRender(solid_fission_sink2, i, MetaEnums.HeatSinkType2.values()[i].getName());
		}
		
		registerRender(salt_fission_controller);
		registerRender(salt_fission_vessel);
		for (int i = 0; i < MetaEnums.CoolantHeaterType.values().length; i++) {
			registerRender(salt_fission_heater, i, MetaEnums.CoolantHeaterType.values()[i].getName());
		}
		for (int i = 0; i < MetaEnums.CoolantHeaterType2.values().length; i++) {
			registerRender(salt_fission_heater2, i, MetaEnums.CoolantHeaterType2.values()[i].getName());
		}
		
		registerRender(heat_exchanger_controller);
		registerRender(heat_exchanger_casing);
		registerRender(heat_exchanger_glass);
		registerRender(heat_exchanger_vent);
		registerRender(heat_exchanger_tube_copper);
		registerRender(heat_exchanger_tube_hard_carbon);
		registerRender(heat_exchanger_tube_thermoconducting);
		registerRender(heat_exchanger_computer_port);
		
		registerRender(condenser_controller);
		registerRender(condenser_tube_copper);
		registerRender(condenser_tube_hard_carbon);
		registerRender(condenser_tube_thermoconducting);
		
		registerRender(turbine_controller);
		registerRender(turbine_casing);
		registerRender(turbine_glass);
		registerRender(turbine_rotor_shaft);
		registerRender(turbine_rotor_blade_steel);
		registerRender(turbine_rotor_blade_extreme);
		registerRender(turbine_rotor_blade_sic_sic_cmc);
		registerRender(turbine_rotor_stator);
		registerRender(turbine_rotor_bearing);
		for (int i = 0; i < TurbineDynamoCoilType.values().length; i++) {
			registerRender(turbine_dynamo_coil, i, TurbineDynamoCoilType.values()[i].getName());
		}
		registerRender(turbine_coil_connector);
		registerRender(turbine_inlet);
		registerRender(turbine_outlet);
		registerRender(turbine_computer_port);
		
		if (NCConfig.register_passive[0]) {
			registerRender(cobblestone_generator);
			registerRender(cobblestone_generator_compact);
			registerRender(cobblestone_generator_dense);
		}
		
		if (NCConfig.register_passive[1]) {
			registerRender(water_source);
			registerRender(water_source_compact);
			registerRender(water_source_dense);
		}
		
		if (NCConfig.register_passive[2]) {
			registerRender(nitrogen_collector);
			registerRender(nitrogen_collector_compact);
			registerRender(nitrogen_collector_dense);
		}
		
		registerRender(radiation_scrubber);
		
		registerRender(geiger_block);
		
		registerRender(glowing_mushroom);
		registerRender(wasteland_earth);
		
		registerRender(tritium_lamp);
		
		if (NCConfig.register_quantum) {
			registerRender(quantum_computer_controller);
			registerRender(quantum_computer_qubit);
			
			for (int i = 0; i < QuantumComputerGateEnums.SingleType.values().length; i++) {
				registerRender(quantum_computer_gate_single, i, QuantumComputerGateEnums.SingleType.values()[i].getName());
			}
			for (int i = 0; i < QuantumComputerGateEnums.ControlType.values().length; i++) {
				registerRender(quantum_computer_gate_control, i, QuantumComputerGateEnums.ControlType.values()[i].getName());
			}
			for (int i = 0; i < QuantumComputerGateEnums.SwapType.values().length; i++) {
				registerRender(quantum_computer_gate_swap, i, QuantumComputerGateEnums.SwapType.values()[i].getName());
			}
			
			registerRender(quantum_computer_connector);
			//registerRender(quantum_computer_port);
		}
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
	
	public static void registerBlock(Block block, TextFormatting[] fixedColors, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, fixedColors, fixedTooltip, infoColor, tooltip).setRegistryName(block.getRegistryName()));
	}
	
	public static void registerBlock(Block block, TextFormatting fixedColor, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, fixedColor, fixedTooltip, infoColor, tooltip).setRegistryName(block.getRegistryName()));
	}
	
	public static void registerBlock(Block block, String... tooltip) {
		registerBlock(block, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, tooltip);
	}
	
	public static void registerBlock(Block block, ItemBlock itemBlock) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock.setRegistryName(block.getRegistryName()));
	}
	
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	public static void registerRender(Block block, int meta, String type) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, block.getRegistryName().getPath()), "type=" + type));
	}
}
