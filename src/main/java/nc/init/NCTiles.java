package nc.init;

import nc.Global;
import nc.block.quantum.BlockQuantumComputerCodeGenerator;
import nc.enumm.*;
import nc.multiblock.hx.HeatExchangerTubeType;
import nc.multiblock.quantum.QuantumGateEnums;
import nc.multiblock.turbine.*;
import nc.tile.TileBin;
import nc.tile.battery.TileBattery;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.fission.*;
import nc.tile.fission.manager.*;
import nc.tile.fission.port.*;
import nc.tile.generator.*;
import nc.tile.hx.*;
import nc.tile.machine.*;
import nc.tile.passive.TilePassive;
import nc.tile.processor.TileNuclearFurnace;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.quantum.*;
import nc.tile.radiation.*;
import nc.tile.rtg.TileRTG;
import nc.tile.turbine.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class NCTiles {
	
	public static void register() {
		registerTile(Global.MOD_ID, "nuclear_furnace", TileNuclearFurnace.class);
		registerTile(Global.MOD_ID, "manufactory", TileManufactory.class);
		registerTile(Global.MOD_ID, "separator", TileSeparator.class);
		registerTile(Global.MOD_ID, "decay_hastener", TileDecayHastener.class);
		registerTile(Global.MOD_ID, "fuel_reprocessor", TileFuelReprocessor.class);
		registerTile(Global.MOD_ID, "alloy_furnace", TileAlloyFurnace.class);
		registerTile(Global.MOD_ID, "infuser", TileInfuser.class);
		registerTile(Global.MOD_ID, "melter", TileMelter.class);
		registerTile(Global.MOD_ID, "supercooler", TileSupercooler.class);
		registerTile(Global.MOD_ID, "electrolyzer", TileElectrolyzer.class);
		registerTile(Global.MOD_ID, "assembler", TileAssembler.class);
		registerTile(Global.MOD_ID, "ingot_former", TileIngotFormer.class);
		registerTile(Global.MOD_ID, "pressurizer", TilePressurizer.class);
		registerTile(Global.MOD_ID, "chemical_reactor", TileChemicalReactor.class);
		registerTile(Global.MOD_ID, "salt_mixer", TileSaltMixer.class);
		registerTile(Global.MOD_ID, "crystallizer", TileCrystallizer.class);
		registerTile(Global.MOD_ID, "enricher", TileEnricher.class);
		registerTile(Global.MOD_ID, "extractor", TileExtractor.class);
		registerTile(Global.MOD_ID, "centrifuge", TileCentrifuge.class);
		registerTile(Global.MOD_ID, "rock_crusher", TileRockCrusher.class);
		registerTile(Global.MOD_ID, "electric_furnace", TileElectricFurnace.class);
		
		registerTile(Global.MOD_ID, "basic_energy_processor_dyn", TileBasicEnergyProcessorDyn.class);
		registerTile(Global.MOD_ID, "basic_upgradable_energy_processor_dyn", TileBasicUpgradableEnergyProcessorDyn.class);
		
		registerTile(Global.MOD_ID, "machine_interface", TileMachineInterface.class);
		
		registerTile(Global.MOD_ID, "machine_frame", TileMachineFrame.class);
		registerTile(Global.MOD_ID, "machine_glass", TileMachineGlass.class);
		registerTile(Global.MOD_ID, "machine_power_port", TileMachinePowerPort.class);
		registerTile(Global.MOD_ID, "machine_process_port", TileMachineProcessPort.class);
		registerTile(Global.MOD_ID, "machine_reservoir_port", TileMachineReservoirPort.class);
		registerTile(Global.MOD_ID, "machine_redstone_port", TileMachineRedstonePort.class);
		registerTile(Global.MOD_ID, "machine_computer_port", TileMachineComputerPort.class);
		
		registerTile(Global.MOD_ID, "electrolyzer_controller", TileElectrolyzerController.class);
		registerTile(Global.MOD_ID, "electrolyzer_cathode_terminal", TileElectrolyzerCathodeTerminal.class);
		registerTile(Global.MOD_ID, "electrolyzer_anode_terminal", TileElectrolyzerAnodeTerminal.class);
		
		registerTile(Global.MOD_ID, "distiller_controller", TileDistillerController.class);
		registerTile(Global.MOD_ID, "distiller_sieve_tray", TileDistillerSieveTray.class);
		registerTile(Global.MOD_ID, "distiller_reflux_unit", TileDistillerRefluxUnit.class);
		registerTile(Global.MOD_ID, "distiller_reboiling_unit", TileDistillerReboilingUnit.class);
		registerTile(Global.MOD_ID, "distiller_liquid_distributor", TileDistillerLiquidDistributor.class);
		
		registerTile(Global.MOD_ID, "infiltrator_controller", TileInfiltratorController.class);
		registerTile(Global.MOD_ID, "infiltrator_pressure_chamber", TileInfiltratorPressureChamber.class);
		registerTile(Global.MOD_ID, "infiltrator_heating_unit", TileInfiltratorHeatingUnit.class);
		
		registerTile(Global.MOD_ID, "fission_casing", TileFissionCasing.class);
		registerTile(Global.MOD_ID, "fission_glass", TileFissionGlass.class);
		registerTile(Global.MOD_ID, "fission_conductor", TileFissionConductor.class);
		registerTile(Global.MOD_ID, "fission_monitor", TileFissionMonitor.class);
		registerTile(Global.MOD_ID, "fission_power_port", TileFissionPowerPort.class);
		registerTile(Global.MOD_ID, "fission_vent", TileFissionVent.class);
		registerTile(Global.MOD_ID, "fission_irradiator", TileFissionIrradiator.class);
		
		registerTile(Global.MOD_ID, "fission_source", TileFissionSource.class);
		registerTileVariants(Global.MOD_ID, "fission_source", MetaEnums.NeutronSourceType.class);
		
		registerTile(Global.MOD_ID, "fission_shield", TileFissionShield.class);
		registerTileVariants(Global.MOD_ID, "fission_shield", MetaEnums.NeutronShieldType.class);
		
		registerTile(Global.MOD_ID, "fission_computer_port", TileFissionComputerPort.class);
		
		registerTile(Global.MOD_ID, "fission_irradiator_port", TileFissionIrradiatorPort.class);
		registerTile(Global.MOD_ID, "fission_cell_port", TileFissionCellPort.class);
		
		registerTile(Global.MOD_ID, "fission_vessel_port", TileFissionVesselPort.class);
		
		registerTile(Global.MOD_ID, "fission_heater_port", TileFissionHeaterPort.class);
		registerTileVariants(Global.MOD_ID, "fission_heater_port", MetaEnums.CoolantHeaterType.class, x -> Pair.of(x.toString(), x.getPortClass()));
		registerTileVariants(Global.MOD_ID, "fission_heater_port", MetaEnums.CoolantHeaterType2.class, x -> Pair.of(x.toString(), x.getPortClass()));
		
		registerTile(Global.MOD_ID, "fission_source_manager", TileFissionSourceManager.class);
		registerTile(Global.MOD_ID, "fission_shield_manager", TileFissionShieldManager.class);
		
		registerTile(Global.MOD_ID, "solid_fission_controller", TileSolidFissionController.class);
		registerTile(Global.MOD_ID, "solid_fission_cell", TileSolidFissionCell.class);
		
		registerTile(Global.MOD_ID, "solid_fission_sink", TileSolidFissionSink.class);
		registerTileVariants(Global.MOD_ID, "solid_fission_sink", MetaEnums.HeatSinkType.class);
		registerTileVariants(Global.MOD_ID, "solid_fission_sink", MetaEnums.HeatSinkType2.class);
		
		registerTile(Global.MOD_ID, "salt_fission_controller", TileSaltFissionController.class);
		registerTile(Global.MOD_ID, "salt_fission_vessel", TileSaltFissionVessel.class);
		
		registerTile(Global.MOD_ID, "salt_fission_heater", TileSaltFissionHeater.class);
		registerTileVariants(Global.MOD_ID, "salt_fission_heater", MetaEnums.CoolantHeaterType.class);
		registerTileVariants(Global.MOD_ID, "salt_fission_heater", MetaEnums.CoolantHeaterType2.class);
		
		registerTile(Global.MOD_ID, "heat_exchanger_controller", TileHeatExchangerController.class);
		registerTile(Global.MOD_ID, "condenser_controller", TileCondenserController.class);
		
		registerTile(Global.MOD_ID, "heat_exchanger_casing", TileHeatExchangerCasing.class);
		registerTile(Global.MOD_ID, "heat_exchanger_glass", TileHeatExchangerGlass.class);
		
		registerTile(Global.MOD_ID, "heat_exchanger_inlet", TileHeatExchangerInlet.class);
		registerTile(Global.MOD_ID, "heat_exchanger_outlet", TileHeatExchangerOutlet.class);
		
		registerTile(Global.MOD_ID, "heat_exchanger_tube", TileHeatExchangerTube.class);
		registerTileVariants(Global.MOD_ID, "heat_exchanger_tube", HeatExchangerTubeType.class);
		
		registerTile(Global.MOD_ID, "heat_exchanger_baffle", TileHeatExchangerBaffle.class);
		
		registerTile(Global.MOD_ID, "heat_exchanger_redstone_port", TileHeatExchangerRedstonePort.class);
		registerTile(Global.MOD_ID, "heat_exchanger_computer_port", TileHeatExchangerComputerPort.class);
		
		registerTile(Global.MOD_ID, "turbine_controller", TileTurbineController.class);
		registerTile(Global.MOD_ID, "turbine_casing", TileTurbineCasing.class);
		registerTile(Global.MOD_ID, "turbine_glass", TileTurbineGlass.class);
		registerTile(Global.MOD_ID, "turbine_rotor_shaft", TileTurbineRotorShaft.class);
		
		registerTile(Global.MOD_ID, "turbine_rotor_blade_", TileTurbineRotorBlade.class);
		registerTileVariants(Global.MOD_ID, "turbine_rotor_blade", TurbineRotorBladeUtil.TurbineRotorBladeType.class);
		
		registerTile(Global.MOD_ID, "turbine_rotor_stator_", TileTurbineRotorStator.class);
		registerTileVariants(Global.MOD_ID, "turbine_rotor_stator", TurbineRotorBladeUtil.TurbineRotorStatorType.class);
		
		registerTile(Global.MOD_ID, "turbine_rotor_bearing", TileTurbineRotorBearing.class);
		
		registerTile(Global.MOD_ID, "turbine_dynamo_coil", TileTurbineDynamoCoil.class);
		registerTileVariants(Global.MOD_ID, "turbine_dynamo_coil", TurbineDynamoCoilType.class);
		
		registerTile(Global.MOD_ID, "turbine_coil_connector", TileTurbineCoilConnector.class);
		registerTile(Global.MOD_ID, "turbine_inlet", TileTurbineInlet.class);
		registerTile(Global.MOD_ID, "turbine_outlet", TileTurbineOutlet.class);
		registerTile(Global.MOD_ID, "turbine_redstone_port", TileTurbineRedstonePort.class);
		registerTile(Global.MOD_ID, "turbine_computer_port", TileTurbineComputerPort.class);
		
		registerTile(Global.MOD_ID, "rtg", TileRTG.class);
		registerTile(Global.MOD_ID, "rtg_uranium", TileRTG.Uranium.class);
		registerTile(Global.MOD_ID, "rtg_plutonium", TileRTG.Plutonium.class);
		registerTile(Global.MOD_ID, "rtg_americium", TileRTG.Americium.class);
		registerTile(Global.MOD_ID, "rtg_californium", TileRTG.Californium.class);
		
		registerTile(Global.MOD_ID, "solar_panel", TileSolarPanel.class);
		registerTile(Global.MOD_ID, "solar_panel_basic", TileSolarPanel.Basic.class);
		registerTile(Global.MOD_ID, "solar_panel_advanced", TileSolarPanel.Advanced.class);
		registerTile(Global.MOD_ID, "solar_panel_du", TileSolarPanel.DU.class);
		registerTile(Global.MOD_ID, "solar_panel_elite", TileSolarPanel.Elite.class);
		
		registerTile(Global.MOD_ID, "decay_generator", TileDecayGenerator.class);
		
		registerTile(Global.MOD_ID, "battery", TileBattery.class);
		registerTile(Global.MOD_ID, "voltaic_pile_basic", TileBattery.VoltaicPileBasic.class);
		registerTile(Global.MOD_ID, "voltaic_pile_advanced", TileBattery.VoltaicPileAdvanced.class);
		registerTile(Global.MOD_ID, "voltaic_pile_du", TileBattery.VoltaicPileDU.class);
		registerTile(Global.MOD_ID, "voltaic_pile_elite", TileBattery.VoltaicPileElite.class);
		registerTile(Global.MOD_ID, "lithium_ion_battery_basic", TileBattery.LithiumIonBatteryBasic.class);
		registerTile(Global.MOD_ID, "lithium_ion_battery_advanced", TileBattery.LithiumIonBatteryAdvanced.class);
		registerTile(Global.MOD_ID, "lithium_ion_battery_du", TileBattery.LithiumIonBatteryDU.class);
		registerTile(Global.MOD_ID, "lithium_ion_battery_elite", TileBattery.LithiumIonBatteryElite.class);
		
		registerTile(Global.MOD_ID, "bin", TileBin.class);
		
		registerTile(Global.MOD_ID, "cobblestone_generator", TilePassive.CobblestoneGenerator.class);
		registerTile(Global.MOD_ID, "cobblestone_generator_compact", TilePassive.CobblestoneGeneratorCompact.class);
		registerTile(Global.MOD_ID, "cobblestone_generator_dense", TilePassive.CobblestoneGeneratorDense.class);
		registerTile(Global.MOD_ID, "water_source", TilePassive.WaterSource.class);
		registerTile(Global.MOD_ID, "water_source_compact", TilePassive.WaterSourceCompact.class);
		registerTile(Global.MOD_ID, "water_source_dense", TilePassive.WaterSourceDense.class);
		registerTile(Global.MOD_ID, "nitrogen_collector", TilePassive.NitrogenCollector.class);
		registerTile(Global.MOD_ID, "nitrogen_collector_compact", TilePassive.NitrogenCollectorCompact.class);
		registerTile(Global.MOD_ID, "nitrogen_collector_dense", TilePassive.NitrogenCollectorDense.class);
		
		registerTile(Global.MOD_ID, "radiation_scrubber", TileRadiationScrubber.class);
		
		registerTile(Global.MOD_ID, "geiger_block", TileGeigerCounter.class);
		
		registerTile(Global.MOD_ID, "quantum_computer_controller", TileQuantumComputerController.class);
		registerTile(Global.MOD_ID, "quantum_computer_qubit", TileQuantumComputerQubit.class);
		
		registerTileVariants(Global.MOD_ID, "quantum_computer_gate_single", QuantumGateEnums.SingleType.class);
		registerTileVariants(Global.MOD_ID, "quantum_computer_gate_control", QuantumGateEnums.ControlType.class);
		registerTileVariants(Global.MOD_ID, "quantum_computer_gate_swap", QuantumGateEnums.SwapType.class);
		
		registerTile(Global.MOD_ID, "quantum_computer_connector", TileQuantumComputerConnector.class);
		registerTile(Global.MOD_ID, "quantum_computer_port", TileQuantumComputerPort.class);
		
		registerTileVariants(Global.MOD_ID, "quantum_computer_code_generator", BlockQuantumComputerCodeGenerator.Type.class);
	}
	
	public static void registerTile(String modId, String name, Class<? extends TileEntity> clazz) {
		GameRegistry.registerTileEntity(clazz, modId + ":" + name);
	}
	
	public static <T extends ITileEnum<?>> void registerTileVariants(String modId, String name, Class<T> enumm, Function<T, Pair<String, Class<? extends TileEntity>>> function) {
		for (T type : enumm.getEnumConstants()) {
			Pair<String, Class<? extends TileEntity>> pair = function.apply(type);
			registerTile(modId, name + "_" + pair.getLeft(), pair.getRight());
		}
	}
	
	public static <T extends TileEntity, V extends ITileEnum<T>> void registerTileVariants(String modId, String name, Class<V> enumm) {
		registerTileVariants(modId, name, enumm, x -> Pair.of(x.toString(), x.getTileClass()));
	}
}
