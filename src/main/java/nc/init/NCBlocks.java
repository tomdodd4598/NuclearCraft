package nc.init;

import nc.*;
import nc.block.*;
import nc.block.BlockMeta.BlockMachineSieveAssembly;
import nc.block.battery.*;
import nc.block.fission.*;
import nc.block.fission.manager.*;
import nc.block.fission.port.*;
import nc.block.hx.*;
import nc.block.item.*;
import nc.block.item.energy.ItemBlockBattery;
import nc.block.machine.*;
import nc.block.plant.*;
import nc.block.quantum.*;
import nc.block.rtg.BlockRTG;
import nc.block.tile.*;
import nc.block.tile.dummy.BlockMachineInterface;
import nc.block.tile.processor.*;
import nc.block.tile.radiation.*;
import nc.block.turbine.*;
import nc.enumm.*;
import nc.multiblock.hx.HeatExchangerTubeType;
import nc.multiblock.rtg.RTGType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.radiation.RadiationHelper;
import nc.tab.NCTabs;
import nc.util.*;
import nc.util.PrimitiveFunction.ObjIntFunction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.function.*;

import static nc.config.NCConfig.*;

public class NCBlocks {
	
	public static Block ore;
	public static Block ingot_block;
	public static Block ingot_block2;
	public static Block material_block;
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
	public static Block electric_furnace;
	
	public static Block machine_interface;
	
	public static Block machine_frame;
	public static Block machine_glass;
	public static Block machine_power_port;
	public static Block machine_process_port;
	public static Block machine_reservoir_port;
	public static Block machine_redstone_port;
	public static Block machine_computer_port;
	
	public static Block machine_diaphragm;
	public static Block machine_sieve_assembly;
	
	public static Block electrolyzer_controller;
	public static Block electrolyzer_cathode_terminal;
	public static Block electrolyzer_anode_terminal;
	
	public static Block distiller_controller;
	public static Block distiller_sieve_tray;
	public static Block distiller_reflux_unit;
	public static Block distiller_reboiling_unit;
	public static Block distiller_liquid_distributor;
	
	public static Block infiltrator_controller;
	public static Block infiltrator_pressure_chamber;
	public static Block infiltrator_heating_unit;
	
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
	
	public static Block fission_source_manager;
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
	public static Block condenser_controller;
	
	public static Block heat_exchanger_casing;
	public static Block heat_exchanger_glass;
	public static Block heat_exchanger_inlet;
	public static Block heat_exchanger_outlet;
	public static Block heat_exchanger_tube_copper;
	public static Block heat_exchanger_tube_hard_carbon;
	public static Block heat_exchanger_tube_thermoconducting;
	public static Block heat_exchanger_baffle;
	public static Block heat_exchanger_redstone_port;
	public static Block heat_exchanger_computer_port;
	
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
	public static Block turbine_redstone_port;
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
	public static Block glowing_mushroom_block;
	public static Block wasteland_earth;
	
	public static Block wasteland_portal;
	
	public static Block tritium_lamp;
	
	public static Block solidified_corium;
	
	public static Block quantum_computer_controller;
	public static Block quantum_computer_qubit;
	
	public static Block quantum_computer_gate_single;
	public static Block quantum_computer_gate_control;
	public static Block quantum_computer_gate_swap;
	
	public static Block quantum_computer_connector;
	
	public static Block quantum_computer_code_generator;
	
	public static List<BlockRegistrationInfo<?>> registrationList = new ArrayList<>();
	
	public static void init() {
		ore = addWithNameMeta(Global.MOD_ID, "ore", new BlockMeta.BlockOre());
		ingot_block = addWithNameMeta(Global.MOD_ID, "ingot_block", new BlockMeta.BlockIngot());
		ingot_block2 = addWithNameMeta(Global.MOD_ID, "ingot_block2", new BlockMeta.BlockIngot2());
		material_block = addWithNameMeta(Global.MOD_ID, "material_block", new BlockMeta.BlockMaterial());
		fertile_isotope = addWithNameMeta(Global.MOD_ID, "fertile_isotope", new BlockMeta.BlockFertileIsotope());
		
		supercold_ice = addWithName(Global.MOD_ID, "supercold_ice", new NCBlockIce(Math.nextAfter(1F, -1F)).setCreativeTab(NCTabs.material));
		
		heavy_water_moderator = addWithName(Global.MOD_ID, "heavy_water_moderator", new NCBlock(Material.IRON).setCreativeTab(NCTabs.misc));
		
		if (register_processor[0]) {
			nuclear_furnace = addWithName(Global.MOD_ID, "nuclear_furnace", new BlockNuclearFurnace());
		}
		if (register_processor[1]) {
			manufactory = addWithName(Global.MOD_ID, new BlockProcessor<>("manufactory"));
		}
		if (register_processor[2]) {
			separator = addWithName(Global.MOD_ID, new BlockProcessor<>("separator"));
		}
		if (register_processor[3]) {
			decay_hastener = addWithName(Global.MOD_ID, new BlockProcessor<>("decay_hastener"));
		}
		if (register_processor[4]) {
			fuel_reprocessor = addWithName(Global.MOD_ID, new BlockProcessor<>("fuel_reprocessor"));
		}
		if (register_processor[5]) {
			alloy_furnace = addWithName(Global.MOD_ID, new BlockProcessor<>("alloy_furnace"));
		}
		if (register_processor[6]) {
			infuser = addWithName(Global.MOD_ID, new BlockProcessor<>("infuser"));
		}
		if (register_processor[7]) {
			melter = addWithName(Global.MOD_ID, new BlockProcessor<>("melter"));
		}
		if (register_processor[8]) {
			supercooler = addWithName(Global.MOD_ID, new BlockProcessor<>("supercooler"));
		}
		if (register_processor[9]) {
			electrolyzer = addWithName(Global.MOD_ID, new BlockProcessor<>("electrolyzer"));
		}
		if (register_processor[10]) {
			assembler = addWithName(Global.MOD_ID, new BlockProcessor<>("assembler"));
		}
		if (register_processor[11]) {
			ingot_former = addWithName(Global.MOD_ID, new BlockProcessor<>("ingot_former"));
		}
		if (register_processor[12]) {
			pressurizer = addWithName(Global.MOD_ID, new BlockProcessor<>("pressurizer"));
		}
		if (register_processor[13]) {
			chemical_reactor = addWithName(Global.MOD_ID, new BlockProcessor<>("chemical_reactor"));
		}
		if (register_processor[14]) {
			salt_mixer = addWithName(Global.MOD_ID, new BlockProcessor<>("salt_mixer"));
		}
		if (register_processor[15]) {
			crystallizer = addWithName(Global.MOD_ID, new BlockProcessor<>("crystallizer"));
		}
		if (register_processor[16]) {
			enricher = addWithName(Global.MOD_ID, new BlockProcessor<>("enricher"));
		}
		if (register_processor[17]) {
			extractor = addWithName(Global.MOD_ID, new BlockProcessor<>("extractor"));
		}
		if (register_processor[18]) {
			centrifuge = addWithName(Global.MOD_ID, new BlockProcessor<>("centrifuge"));
		}
		if (register_processor[19]) {
			rock_crusher = addWithName(Global.MOD_ID, new BlockProcessor<>("rock_crusher"));
		}
		if (register_processor[20]) {
			electric_furnace = addWithName(Global.MOD_ID, new BlockProcessor<>("electric_furnace"));
		}
		
		machine_interface = addWithName(Global.MOD_ID, new BlockMachineInterface("machine_interface"));
		
		machine_frame = addWithName(Global.MOD_ID, "machine_frame", new BlockMachineFrame());
		machine_glass = addWithName(Global.MOD_ID, "machine_glass", new BlockMachineGlass());
		machine_power_port = addWithName(Global.MOD_ID, "machine_power_port", new BlockMachinePowerPort());
		machine_process_port = addWithName(Global.MOD_ID, "machine_process_port", new BlockMachineProcessPort());
		machine_reservoir_port = addWithName(Global.MOD_ID, "machine_reservoir_port", new BlockMachineReservoirPort());
		machine_redstone_port = addWithName(Global.MOD_ID, "machine_redstone_port", new BlockMachineRedstonePort());
		machine_computer_port = addWithName(Global.MOD_ID, "machine_computer_port", new BlockMachineComputerPort());
		
		machine_diaphragm = addWithNameMeta(Global.MOD_ID, "machine_diaphragm", new BlockMeta.BlockMachineDiaphragm());
		machine_sieve_assembly = addWithNameMeta(Global.MOD_ID, "machine_sieve_assembly", new BlockMachineSieveAssembly());
		
		electrolyzer_controller = addWithName(Global.MOD_ID, "electrolyzer_controller", new BlockElectrolyzerController());
		electrolyzer_cathode_terminal = addWithName(Global.MOD_ID, "electrolyzer_cathode_terminal", new BlockElectrolyzerCathodeTerminal());
		electrolyzer_anode_terminal = addWithName(Global.MOD_ID, "electrolyzer_anode_terminal", new BlockElectrolyzerAnodeTerminal());
		
		distiller_controller = addWithName(Global.MOD_ID, "distiller_controller", new BlockDistillerController());
		distiller_sieve_tray = addWithName(Global.MOD_ID, "distiller_sieve_tray", new BlockDistillerSieveTray());
		distiller_reflux_unit = addWithName(Global.MOD_ID, "distiller_reflux_unit", new BlockDistillerRefluxUnit());
		distiller_reboiling_unit = addWithName(Global.MOD_ID, "distiller_reboiling_unit", new BlockDistillerReboilingUnit());
		distiller_liquid_distributor = addWithName(Global.MOD_ID, "distiller_liquid_distributor", new BlockDistillerLiquidDistributor());
		
		infiltrator_controller = addWithName(Global.MOD_ID, "infiltrator_controller", new BlockInfiltratorController());
		infiltrator_pressure_chamber = addWithName(Global.MOD_ID, "infiltrator_pressure_chamber", new BlockInfiltratorPressureChamber());
		infiltrator_heating_unit = addWithName(Global.MOD_ID, "infiltrator_heating_unit", new BlockInfiltratorHeatingUnit());
		
		rtg_uranium = addWithName(Global.MOD_ID, "rtg_uranium", new BlockRTG(RTGType.URANIUM), NCInfo.rtgInfo(rtg_power[0]));
		rtg_plutonium = addWithName(Global.MOD_ID, "rtg_plutonium", new BlockRTG(RTGType.PLUTONIUM), NCInfo.rtgInfo(rtg_power[1]));
		rtg_americium = addWithName(Global.MOD_ID, "rtg_americium", new BlockRTG(RTGType.AMERICIUM), NCInfo.rtgInfo(rtg_power[2]));
		rtg_californium = addWithName(Global.MOD_ID, "rtg_californium", new BlockRTG(RTGType.CALIFORNIUM), NCInfo.rtgInfo(rtg_power[3]));
		
		solar_panel_basic = addWithName(Global.MOD_ID, new BlockSimpleTile<>("solar_panel_basic"), NCInfo.rtgInfo(solar_power[0]));
		solar_panel_advanced = addWithName(Global.MOD_ID, new BlockSimpleTile<>("solar_panel_advanced"), NCInfo.rtgInfo(solar_power[1]));
		solar_panel_du = addWithName(Global.MOD_ID, new BlockSimpleTile<>("solar_panel_du"), NCInfo.rtgInfo(solar_power[2]));
		solar_panel_elite = addWithName(Global.MOD_ID, new BlockSimpleTile<>("solar_panel_elite"), NCInfo.rtgInfo(solar_power[3]));
		
		decay_generator = addWithName(Global.MOD_ID, new BlockSimpleTile<>("decay_generator"));
		
		Function<BlockBattery, ItemBlockBattery> itemBlockBatteryFunction = x -> new ItemBlockBattery(x, NCInfo.batteryInfo());
		if (register_battery[0]) {
			voltaic_pile_basic = addWithName(Global.MOD_ID, "voltaic_pile_basic", new BlockBattery(BatteryBlockType.VOLTAIC_PILE_BASIC), itemBlockBatteryFunction);
			voltaic_pile_advanced = addWithName(Global.MOD_ID, "voltaic_pile_advanced", new BlockBattery(BatteryBlockType.VOLTAIC_PILE_ADVANCED), itemBlockBatteryFunction);
			voltaic_pile_du = addWithName(Global.MOD_ID, "voltaic_pile_du", new BlockBattery(BatteryBlockType.VOLTAIC_PILE_DU), itemBlockBatteryFunction);
			voltaic_pile_elite = addWithName(Global.MOD_ID, "voltaic_pile_elite", new BlockBattery(BatteryBlockType.VOLTAIC_PILE_ELITE), itemBlockBatteryFunction);
		}
		if (register_battery[1]) {
			lithium_ion_battery_basic = addWithName(Global.MOD_ID, "lithium_ion_battery_basic", new BlockBattery(BatteryBlockType.LITHIUM_ION_BATTERY_BASIC), itemBlockBatteryFunction);
			lithium_ion_battery_advanced = addWithName(Global.MOD_ID, "lithium_ion_battery_advanced", new BlockBattery(BatteryBlockType.LITHIUM_ION_BATTERY_ADVANCED), itemBlockBatteryFunction);
			lithium_ion_battery_du = addWithName(Global.MOD_ID, "lithium_ion_battery_du", new BlockBattery(BatteryBlockType.LITHIUM_ION_BATTERY_DU), itemBlockBatteryFunction);
			lithium_ion_battery_elite = addWithName(Global.MOD_ID, "lithium_ion_battery_elite", new BlockBattery(BatteryBlockType.LITHIUM_ION_BATTERY_ELITE), itemBlockBatteryFunction);
		}
		
		bin = addWithName(Global.MOD_ID, new BlockSimpleTile<>("bin"));
		
		fission_casing = addWithName(Global.MOD_ID, "fission_casing", new BlockFissionCasing());
		fission_glass = addWithName(Global.MOD_ID, "fission_glass", new BlockFissionGlass());
		fission_conductor = addWithName(Global.MOD_ID, "fission_conductor", new BlockFissionConductor());
		fission_monitor = addWithName(Global.MOD_ID, "fission_monitor", new BlockFissionMonitor());
		fission_reflector = addWithNameMeta(Global.MOD_ID, "fission_reflector", new BlockMeta.BlockFissionReflector());
		fission_power_port = addWithName(Global.MOD_ID, "fission_power_port", new BlockFissionPowerPort());
		fission_vent = addWithName(Global.MOD_ID, "fission_vent", new BlockFissionVent());
		fission_irradiator = addWithName(Global.MOD_ID, "fission_irradiator", new BlockFissionIrradiator());
		fission_source = addWithNameMeta(Global.MOD_ID, "fission_source", new BlockFissionMetaSource(), x -> new ItemBlockMeta<>(x, TextFormatting.LIGHT_PURPLE, NCInfo.neutronSourceFixedInfo(), TextFormatting.AQUA, NCInfo.neutronSourceInfo()), x -> "active=false,facing=south,type=" + x);
		fission_shield = addWithNameMeta(Global.MOD_ID, "fission_shield", new BlockFissionMetaShield(), x -> new ItemBlockMeta<>(x, new TextFormatting[] {TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE}, NCInfo.neutronShieldFixedInfo(), TextFormatting.AQUA, NCInfo.neutronShieldInfo()), x -> "active=true,type=" + x);
		
		fission_computer_port = addWithName(Global.MOD_ID, "fission_computer_port", new BlockFissionComputerPort());
		
		fission_irradiator_port = addWithName(Global.MOD_ID, "fission_irradiator_port", new BlockFissionIrradiatorPort());
		fission_cell_port = addWithName(Global.MOD_ID, "fission_cell_port", new BlockFissionCellPort());
		
		fission_vessel_port = addWithName(Global.MOD_ID, "fission_vessel_port", new BlockFissionVesselPort());
		fission_heater_port = addWithNameMeta(Global.MOD_ID, "fission_heater_port", new BlockFissionHeaterPort(), ItemBlockMeta<MetaEnums.CoolantHeaterType>::new, x -> "active=false,axis=z,type=" + x);
		fission_heater_port2 = addWithNameMeta(Global.MOD_ID, "fission_heater_port2", new BlockFissionHeaterPort2(), ItemBlockMeta<MetaEnums.CoolantHeaterType2>::new, x -> "active=false,axis=z,type=" + x);
		
		fission_source_manager = addWithName(Global.MOD_ID, "fission_source_manager", new BlockFissionSourceManager());
		fission_shield_manager = addWithName(Global.MOD_ID, "fission_shield_manager", new BlockFissionShieldManager());
		
		solid_fission_controller = addWithName(Global.MOD_ID, "solid_fission_controller", new BlockSolidFissionController());
		solid_fission_cell = addWithName(Global.MOD_ID, "solid_fission_cell", new BlockSolidFissionCell());
		solid_fission_sink = addWithNameMeta(Global.MOD_ID, "solid_fission_sink", new BlockSolidFissionMetaSink(), x -> new ItemBlockMeta<>(x, TextFormatting.BLUE, NCInfo.heatSinkFixedInfo(), TextFormatting.AQUA, InfoHelper.NULL_ARRAYS), x -> "type=" + x);
		solid_fission_sink2 = addWithNameMeta(Global.MOD_ID, "solid_fission_sink2", new BlockSolidFissionMetaSink2(), x -> new ItemBlockMeta<>(x, TextFormatting.BLUE, NCInfo.heatSinkFixedInfo2(), TextFormatting.AQUA, InfoHelper.NULL_ARRAYS), x -> "type=" + x);
		
		salt_fission_controller = addWithName(Global.MOD_ID, "salt_fission_controller", new BlockSaltFissionController());
		salt_fission_vessel = addWithName(Global.MOD_ID, "salt_fission_vessel", new BlockSaltFissionVessel());
		salt_fission_heater = addWithNameMeta(Global.MOD_ID, "salt_fission_heater", new BlockSaltFissionMetaHeater(), x -> new ItemBlockMeta<>(x, TextFormatting.BLUE, NCInfo.coolantHeaterFixedInfo(), TextFormatting.AQUA, InfoHelper.NULL_ARRAYS), x -> "type=" + x);
		salt_fission_heater2 = addWithNameMeta(Global.MOD_ID, "salt_fission_heater2", new BlockSaltFissionMetaHeater2(), x -> new ItemBlockMeta<>(x, TextFormatting.BLUE, NCInfo.coolantHeaterFixedInfo2(), TextFormatting.AQUA, InfoHelper.NULL_ARRAYS), x -> "type=" + x);
		
		heat_exchanger_controller = addWithName(Global.MOD_ID, "heat_exchanger_controller", new BlockHeatExchangerController());
		condenser_controller = addWithName(Global.MOD_ID, "condenser_controller", new BlockCondenserController());
		
		heat_exchanger_casing = addWithName(Global.MOD_ID, "heat_exchanger_casing", new BlockHeatExchangerCasing());
		heat_exchanger_glass = addWithName(Global.MOD_ID, "heat_exchanger_glass", new BlockHeatExchangerGlass());
		
		heat_exchanger_inlet = addWithName(Global.MOD_ID, "heat_exchanger_inlet", new BlockHeatExchangerInlet());
		heat_exchanger_outlet = addWithName(Global.MOD_ID, "heat_exchanger_outlet", new BlockHeatExchangerOutlet());
		
		ObjIntFunction<Block, NCItemBlock> hxTubeItemBlockFunction = (x, y) -> new NCItemBlock(x, new TextFormatting[] {TextFormatting.YELLOW, TextFormatting.BLUE}, NCInfo.hxTubeFixedInfo(heat_exchanger_heat_transfer_coefficient[y], heat_exchanger_heat_retention_mult[y]), TextFormatting.AQUA, NCInfo.hxTubeInfo());
		heat_exchanger_tube_copper = addWithName(Global.MOD_ID, "heat_exchanger_tube_copper", new BlockHeatExchangerTube(HeatExchangerTubeType.COPPER), x -> hxTubeItemBlockFunction.apply(x, 0));
		heat_exchanger_tube_hard_carbon = addWithName(Global.MOD_ID, "heat_exchanger_tube_hard_carbon", new BlockHeatExchangerTube(HeatExchangerTubeType.HARD_CARBON), x -> hxTubeItemBlockFunction.apply(x, 1));
		heat_exchanger_tube_thermoconducting = addWithName(Global.MOD_ID, "heat_exchanger_tube_thermoconducting", new BlockHeatExchangerTube(HeatExchangerTubeType.THERMOCONDUCTING), x -> hxTubeItemBlockFunction.apply(x, 2));
		
		heat_exchanger_baffle = addWithName(Global.MOD_ID, "heat_exchanger_baffle", new BlockHeatExchangerBaffle());
		
		heat_exchanger_redstone_port = addWithName(Global.MOD_ID, "heat_exchanger_redstone_port", new BlockHeatExchangerRedstonePort());
		heat_exchanger_computer_port = addWithName(Global.MOD_ID, "heat_exchanger_computer_port", new BlockHeatExchangerComputerPort());
		
		turbine_controller = addWithName(Global.MOD_ID, "turbine_controller", new BlockTurbineController());
		turbine_casing = addWithName(Global.MOD_ID, "turbine_casing", new BlockTurbineCasing());
		turbine_glass = addWithName(Global.MOD_ID, "turbine_glass", new BlockTurbineGlass());
		turbine_rotor_shaft = addWithName(Global.MOD_ID, "turbine_rotor_shaft", new BlockTurbineRotorShaft());
		
		ObjIntFunction<Block, ItemBlock> turbineBladeItemBlockFunction = (x, y) -> new NCItemBlock(x, new TextFormatting[] {TextFormatting.LIGHT_PURPLE, TextFormatting.GRAY}, NCInfo.rotorBladeFixedInfo(turbine_blade_efficiency[y], turbine_blade_expansion[y]), TextFormatting.AQUA, NCInfo.rotorBladeInfo());
		turbine_rotor_blade_steel = addWithName(Global.MOD_ID, "turbine_rotor_blade_steel", new BlockTurbineRotorBlade(TurbineRotorBladeType.STEEL), x -> turbineBladeItemBlockFunction.apply(x, 0));
		turbine_rotor_blade_extreme = addWithName(Global.MOD_ID, "turbine_rotor_blade_extreme", new BlockTurbineRotorBlade(TurbineRotorBladeType.EXTREME), x -> turbineBladeItemBlockFunction.apply(x, 1));
		turbine_rotor_blade_sic_sic_cmc = addWithName(Global.MOD_ID, "turbine_rotor_blade_sic_sic_cmc", new BlockTurbineRotorBlade(TurbineRotorBladeType.SIC_SIC_CMC), x -> turbineBladeItemBlockFunction.apply(x, 2));
		
		turbine_rotor_stator = addWithName(Global.MOD_ID, "turbine_rotor_stator", new BlockTurbineRotorStator(TurbineRotorStatorType.STANDARD), x -> new NCItemBlock(x, TextFormatting.GRAY, NCInfo.rotorStatorFixedInfo(turbine_stator_expansion), TextFormatting.AQUA, NCInfo.rotorStatorInfo()));
		
		turbine_rotor_bearing = addWithName(Global.MOD_ID, "turbine_rotor_bearing", new BlockTurbineRotorBearing());
		turbine_dynamo_coil = addWithNameMeta(Global.MOD_ID, "turbine_dynamo_coil", new BlockTurbineMetaDynamoCoil(), x -> new ItemBlockMeta<>(x, TextFormatting.LIGHT_PURPLE, NCInfo.dynamoCoilFixedInfo(), TextFormatting.AQUA, InfoHelper.NULL_ARRAYS), x -> "type=" + x);
		turbine_coil_connector = addWithName(Global.MOD_ID, "turbine_coil_connector", new BlockTurbineCoilConnector());
		turbine_inlet = addWithName(Global.MOD_ID, "turbine_inlet", new BlockTurbineInlet());
		turbine_outlet = addWithName(Global.MOD_ID, "turbine_outlet", new BlockTurbineOutlet());
		
		turbine_redstone_port = addWithName(Global.MOD_ID, "turbine_redstone_port", new BlockTurbineRedstonePort());
		turbine_computer_port = addWithName(Global.MOD_ID, "turbine_computer_port", new BlockTurbineComputerPort());
		
		if (register_passive[0]) {
			ObjIntFunction<Block, ItemBlock> cobbleGeneratorItemBlockFunction = (x, y) -> {
				String rateString = NCMath.sigFigs(processor_passive_rate[0] * y, 5) + " C/t";
				return new NCItemBlock(x, cobble_gen_power > 0 ? InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_req_power", rateString, UnitHelper.prefix(cobble_gen_power * y, 5, "RF/t")) : InfoHelper.formattedInfo("tile.nuclearcraft.cobblestone_generator_no_req_power", rateString));
			};
			cobblestone_generator = addWithName(Global.MOD_ID, new BlockSimpleTile<>("cobblestone_generator"), x -> cobbleGeneratorItemBlockFunction.apply(x, 1));
			cobblestone_generator_compact = addWithName(Global.MOD_ID, new BlockSimpleTile<>("cobblestone_generator_compact"), x -> cobbleGeneratorItemBlockFunction.apply(x, 8));
			cobblestone_generator_dense = addWithName(Global.MOD_ID, new BlockSimpleTile<>("cobblestone_generator_dense"), x -> cobbleGeneratorItemBlockFunction.apply(x, 64));
		}
		
		if (register_passive[1]) {
			ObjIntFunction<Block, ItemBlock> waterSourceItemBlockFunction = (x, y) -> new NCItemBlock(x, InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "water_source"), UnitHelper.prefix(processor_passive_rate[1] * y, 5, "B/t", -1)));
			water_source = addWithName(Global.MOD_ID, new BlockSimpleTile<>("water_source"), x -> waterSourceItemBlockFunction.apply(x, 1));
			water_source_compact = addWithName(Global.MOD_ID, new BlockSimpleTile<>("water_source_compact"), x -> waterSourceItemBlockFunction.apply(x, 8));
			water_source_dense = addWithName(Global.MOD_ID, new BlockSimpleTile<>("water_source_dense"), x -> waterSourceItemBlockFunction.apply(x, 64));
		}
		
		if (register_passive[2]) {
			ObjIntFunction<Block, ItemBlock> nitrogenCollectorItemBlockFunction = (x, y) -> new NCItemBlock(x, InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "nitrogen_collector"), UnitHelper.prefix(processor_passive_rate[2] * y, 5, "B/t", -1)));
			nitrogen_collector = addWithName(Global.MOD_ID, new BlockSimpleTile<>("nitrogen_collector"), x -> nitrogenCollectorItemBlockFunction.apply(x, 1));
			nitrogen_collector_compact = addWithName(Global.MOD_ID, new BlockSimpleTile<>("nitrogen_collector_compact"), x -> nitrogenCollectorItemBlockFunction.apply(x, 8));
			nitrogen_collector_dense = addWithName(Global.MOD_ID, new BlockSimpleTile<>("nitrogen_collector_dense"), x -> nitrogenCollectorItemBlockFunction.apply(x, 64));
		}
		
		radiation_scrubber = addWithName(Global.MOD_ID, new BlockRadiationScrubber("radiation_scrubber"), x -> new NCItemBlock(x, radiation_scrubber_non_linear ? InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "radiation_scrubber_non_linear"), NCMath.pcDecimalPlaces(RadiationHelper.getAltScrubbingFraction(1D), 1), Lang.localize("nuclearcraft.one_hundred_percent")) : InfoHelper.formattedInfo(infoLine(Global.MOD_ID, "radiation_scrubber"), NCMath.pcDecimalPlaces(radiation_scrubber_fraction, 1), Lang.localize("nuclearcraft.one_hundred_percent"))));
		
		geiger_block = addWithName(Global.MOD_ID, new BlockGeigerCounter("geiger_block"));
		
		glowing_mushroom = addWithName(Global.MOD_ID, "glowing_mushroom", new BlockGlowingMushroom().setCreativeTab(NCTabs.radiation));
		glowing_mushroom_block = addWithName(Global.MOD_ID, "glowing_mushroom_block", new BlockHugeGlowingMushroom().setCreativeTab(NCTabs.radiation));
		wasteland_earth = addWithName(Global.MOD_ID, "wasteland_earth", new NCBlock(Material.ROCK).setCreativeTab(NCTabs.radiation));
		
		wasteland_portal = addWithName(Global.MOD_ID, "wasteland_portal", new BlockPortalWasteland().setCreativeTab(NCTabs.radiation));
		
		tritium_lamp = addWithName(Global.MOD_ID, "tritium_lamp", new NCBlock(Material.GLASS).setCreativeTab(NCTabs.misc).setLightLevel(1F));
		
		solidified_corium = addWithName(Global.MOD_ID, "solidified_corium", new NCBlockMagma(DamageSources.CORIUM_BURN, 1F).setCreativeTab(NCTabs.misc));
		
		if (register_quantum) {
			quantum_computer_controller = addWithName(Global.MOD_ID, "quantum_computer_controller", new BlockQuantumComputerController());
			quantum_computer_qubit = addWithName(Global.MOD_ID, "quantum_computer_qubit", new BlockQuantumComputerQubit());
			
			quantum_computer_gate_single = addWithNameMeta(Global.MOD_ID, "quantum_computer_gate_single", new BlockQuantumComputerGate.Single());
			quantum_computer_gate_control = addWithNameMeta(Global.MOD_ID, "quantum_computer_gate_control", new BlockQuantumComputerGate.Control());
			quantum_computer_gate_swap = addWithNameMeta(Global.MOD_ID, "quantum_computer_gate_swap", new BlockQuantumComputerGate.Swap());
			
			quantum_computer_connector = addWithName(Global.MOD_ID, "quantum_computer_connector", new BlockQuantumComputerConnector());
			
			quantum_computer_code_generator = addWithNameMeta(Global.MOD_ID, "quantum_computer_code_generator", new BlockQuantumComputerCodeGenerator());
		}
	}
	
	public static void register() {
		for (BlockRegistrationInfo<?> registration : registrationList) {
			registration.registerBlock.run();
		}
	}
	
	public static void registerRenders() {
		for (BlockRegistrationInfo<?> registration : registrationList) {
			registration.registerRender.run();
		}
	}
	
	// Factory
	
	public static <T extends Block> T addWithName(String modId, String name, T block, Consumer<BlockRegistrationInfo<T>> registerBlock, Consumer<BlockRegistrationInfo<T>> registerRender) {
		block = withName(modId, name, block);
		registrationList.add(new BlockRegistrationInfo<>(modId, name, block, registerBlock, registerRender));
		return block;
	}
	
	public static <T extends Block> T addWithName(String modId, String name, T block, String... tooltip) {
		return addWithName(modId, name, block, x -> registerBlock(x.block, tooltip), x -> registerRender(x.block));
	}
	
	public static <T extends Block & ITileType> T addWithName(String modId, T block, String... tooltip) {
		return addWithName(modId, block.getTileName(), block, x -> registerBlock(x.block, tooltip), x -> registerRender(x.block));
	}
	
	public static <T extends Block> T addWithName(String modId, String name, T block, Function<T, ? extends ItemBlock> itemBlockFunction) {
		return addWithName(modId, name, block, x -> registerBlock(x.block, itemBlockFunction.apply(x.block)), x -> registerRender(x.block));
	}
	
	public static <T extends Block & ITileType> T addWithName(String modId, T block, Function<T, ? extends ItemBlock> itemBlockFunction) {
		return addWithName(modId, block.getTileName(), block, x -> registerBlock(x.block, itemBlockFunction.apply(x.block)), x -> registerRender(x.block));
	}
	
	public static <T extends Block & IBlockMeta<V>, U extends ItemBlockMeta<V>, V extends Enum<V> & IStringSerializable & IMetaEnum> T addWithNameMeta(String modId, String name, T block, Function<T, U> itemBlockFunction, UnaryOperator<String> variantFunction) {
		return addWithName(modId, name, block, x -> registerBlock(x.block, itemBlockFunction.apply(x.block)), x -> registerRenderMeta(x.modId, x.block, variantFunction));
	}
	
	public static <T extends Block & IBlockMeta<V>, V extends Enum<V> & IStringSerializable & IMetaEnum> T addWithNameMeta(String modId, String name, T block) {
		return addWithNameMeta(modId, name, block, ItemBlockMeta<V>::new, x -> "type=" + x);
	}
	
	// Auxiliary
	
	public static String fixedLine(String modId, String name) {
		return "tile." + modId + "." + name + ".fixd";
	}
	
	public static String infoLine(String modId, String name) {
		return "tile." + modId + "." + name + ".desc";
	}
	
	public static <T extends Block> T withName(String modId, String name, T block) {
		block.setTranslationKey(modId + "." + name).setRegistryName(new ResourceLocation(modId, name));
		return block;
	}
	
	public static <T extends Block & ITileType> T withName(String modId, T block) {
		return withName(modId, block.getTileName(), block);
	}
	
	public static void registerBlock(Block block, String... tooltip) {
		registerBlock(block, new NCItemBlock(block, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, tooltip));
	}
	
	public static void registerBlock(Block block, ItemBlock itemBlock) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock.setRegistryName(block.getRegistryName()));
	}
	
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	public static <T extends Block & IBlockMeta<V>, V extends Enum<V> & IStringSerializable & IMetaEnum> void registerRenderMeta(String modId, T block, UnaryOperator<String> variantFunction) {
		registerRenderMeta(modId, block, StreamHelper.map(Arrays.asList(block.getValues()), IStringSerializable::getName), variantFunction);
	}
	
	public static <T extends IStringSerializable> void registerRenderMeta(String modId, Block block, List<String> types, UnaryOperator<String> variantFunction) {
		Item itemBlock = Item.getItemFromBlock(block);
		ResourceLocation blockLocation = new ResourceLocation(modId, block.getRegistryName().getPath());
		for (int i = 0; i < types.size(); ++i) {
			ModelLoader.setCustomModelResourceLocation(itemBlock, i, new ModelResourceLocation(blockLocation, variantFunction.apply(types.get(i))));
		}
	}
}
