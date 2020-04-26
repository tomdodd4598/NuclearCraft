package nc.init;

import nc.Global;
import nc.enumm.MetaEnums;
import nc.multiblock.battery.tile.TileBattery;
import nc.multiblock.fission.salt.tile.TileSaltFissionController;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.TileFissionCasing;
import nc.multiblock.fission.tile.TileFissionComputerPort;
import nc.multiblock.fission.tile.TileFissionConductor;
import nc.multiblock.fission.tile.TileFissionGlass;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.fission.tile.TileFissionMonitor;
import nc.multiblock.fission.tile.TileFissionPowerPort;
import nc.multiblock.fission.tile.TileFissionShield;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.multiblock.fission.tile.port.TileFissionIrradiatorPort;
import nc.multiblock.fission.tile.port.TileFissionVesselPort;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.multiblock.heatExchanger.tile.TileCondenserController;
import nc.multiblock.heatExchanger.tile.TileCondenserTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerCasing;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerComputerPort;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerGlass;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerVent;
import nc.multiblock.qComputer.QuantumComputerGateEnums;
import nc.multiblock.qComputer.tile.TileQuantumComputerConnector;
import nc.multiblock.qComputer.tile.TileQuantumComputerController;
import nc.multiblock.qComputer.tile.TileQuantumComputerGate;
import nc.multiblock.qComputer.tile.TileQuantumComputerPort;
import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.multiblock.rtg.tile.TileRTG;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.tile.TileTurbineCasing;
import nc.multiblock.turbine.tile.TileTurbineCoilConnector;
import nc.multiblock.turbine.tile.TileTurbineComputerPort;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.multiblock.turbine.tile.TileTurbineGlass;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import nc.tile.TileBin;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.generator.TileSolarPanel;
import nc.tile.passive.TilePassive;
import nc.tile.processor.TileNuclearFurnace;
import nc.tile.processor.TileProcessor;
import nc.tile.radiation.TileGeigerCounter;
import nc.tile.radiation.TileRadiationScrubber;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCTiles {
	
	public static void register() {
		GameRegistry.registerTileEntity(TileNuclearFurnace.class, Global.MOD_ID + ":nuclear_furnace");
		GameRegistry.registerTileEntity(TileProcessor.Manufactory.class, Global.MOD_ID + ":manufactory");
		GameRegistry.registerTileEntity(TileProcessor.Separator.class, Global.MOD_ID + ":separator");
		GameRegistry.registerTileEntity(TileProcessor.DecayHastener.class, Global.MOD_ID + ":decay_hastener");
		GameRegistry.registerTileEntity(TileProcessor.FuelReprocessor.class, Global.MOD_ID + ":fuel_reprocessor");
		GameRegistry.registerTileEntity(TileProcessor.AlloyFurnace.class, Global.MOD_ID + ":alloy_furnace");
		GameRegistry.registerTileEntity(TileProcessor.Infuser.class, Global.MOD_ID + ":infuser");
		GameRegistry.registerTileEntity(TileProcessor.Melter.class, Global.MOD_ID + ":melter");
		GameRegistry.registerTileEntity(TileProcessor.Supercooler.class, Global.MOD_ID + ":supercooler");
		GameRegistry.registerTileEntity(TileProcessor.Electrolyzer.class, Global.MOD_ID + ":electrolyzer");
		GameRegistry.registerTileEntity(TileProcessor.Assembler.class, Global.MOD_ID + ":assembler");
		GameRegistry.registerTileEntity(TileProcessor.IngotFormer.class, Global.MOD_ID + ":ingot_former");
		GameRegistry.registerTileEntity(TileProcessor.Pressurizer.class, Global.MOD_ID + ":pressurizer");
		GameRegistry.registerTileEntity(TileProcessor.ChemicalReactor.class, Global.MOD_ID + ":chemical_reactor");
		GameRegistry.registerTileEntity(TileProcessor.SaltMixer.class, Global.MOD_ID + ":salt_mixer");
		GameRegistry.registerTileEntity(TileProcessor.Crystallizer.class, Global.MOD_ID + ":crystallizer");
		GameRegistry.registerTileEntity(TileProcessor.Enricher.class, Global.MOD_ID + ":enricher");
		GameRegistry.registerTileEntity(TileProcessor.Extractor.class, Global.MOD_ID + ":extractor");
		GameRegistry.registerTileEntity(TileProcessor.Centrifuge.class, Global.MOD_ID + ":centrifuge");
		GameRegistry.registerTileEntity(TileProcessor.RockCrusher.class, Global.MOD_ID + ":rock_crusher");
		
		GameRegistry.registerTileEntity(TileMachineInterface.class, Global.MOD_ID + ":machine_interface");
		
		GameRegistry.registerTileEntity(TileFissionCasing.class, Global.MOD_ID + ":fission_casing");
		GameRegistry.registerTileEntity(TileFissionGlass.class, Global.MOD_ID + ":fission_glass");
		GameRegistry.registerTileEntity(TileFissionConductor.class, Global.MOD_ID + ":fission_conductor");
		GameRegistry.registerTileEntity(TileFissionMonitor.class, Global.MOD_ID + ":fission_monitor");
		GameRegistry.registerTileEntity(TileFissionPowerPort.class, Global.MOD_ID + ":fission_power_port");
		GameRegistry.registerTileEntity(TileFissionVent.class, Global.MOD_ID + ":fission_vent");
		GameRegistry.registerTileEntity(TileFissionIrradiator.class, Global.MOD_ID + ":fission_irradiator");
		
		GameRegistry.registerTileEntity(TileFissionSource.RadiumBeryllium.class, Global.MOD_ID + ":fission_source_" + MetaEnums.NeutronSourceType.RADIUM_BERYLLIUM.getName());
		GameRegistry.registerTileEntity(TileFissionSource.PoloniumBeryllium.class, Global.MOD_ID + ":fission_source_" + MetaEnums.NeutronSourceType.POLONIUM_BERYLLIUM.getName());
		GameRegistry.registerTileEntity(TileFissionSource.Californium.class, Global.MOD_ID + ":fission_source_" + MetaEnums.NeutronSourceType.CALIFORNIUM.getName());
		
		GameRegistry.registerTileEntity(TileFissionShield.BoronSilver.class, Global.MOD_ID + ":fission_shield_" + MetaEnums.NeutronShieldType.BORON_SILVER.getName());
		
		GameRegistry.registerTileEntity(TileFissionComputerPort.class, Global.MOD_ID + ":fission_computer_port");
		
		GameRegistry.registerTileEntity(TileFissionIrradiatorPort.class, Global.MOD_ID + ":fission_irradiator_port");
		
		GameRegistry.registerTileEntity(TileFissionCellPort.class, Global.MOD_ID + ":fission_cell_port");
		
		GameRegistry.registerTileEntity(TileFissionVesselPort.class, Global.MOD_ID + ":fission_vessel_port");
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Standard.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.STANDARD.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Iron.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.IRON.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Redstone.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.REDSTONE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Quartz.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.QUARTZ.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Obsidian.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.OBSIDIAN.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.NetherBrick.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.NETHER_BRICK.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Glowstone.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.GLOWSTONE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Lapis.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.LAPIS.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Gold.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.GOLD.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Prismarine.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.PRISMARINE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Slime.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.SLIME.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.EndStone.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.END_STONE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Purpur.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.PURPUR.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Diamond.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.DIAMOND.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Emerald.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.EMERALD.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Copper.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType.COPPER.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Tin.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.TIN.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Lead.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.LEAD.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Boron.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.BORON.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Lithium.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.LITHIUM.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Magnesium.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.MAGNESIUM.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Manganese.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.MANGANESE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Aluminum.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.ALUMINUM.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Silver.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.SILVER.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Fluorite.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.FLUORITE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Villiaumite.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.VILLIAUMITE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Carobbiite.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.CAROBBIITE.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Arsenic.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.ARSENIC.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.LiquidNitrogen.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.LIQUID_NITROGEN.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.LiquidHelium.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.LIQUID_HELIUM.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Enderium.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.ENDERIUM.getName());
		GameRegistry.registerTileEntity(TileFissionHeaterPort.Cryotheum.class, Global.MOD_ID + ":fission_heater_port_" + MetaEnums.CoolantHeaterType2.CRYOTHEUM.getName());
		
		GameRegistry.registerTileEntity(TileFissionShieldManager.class, Global.MOD_ID + ":fission_shield_manager");
		
		GameRegistry.registerTileEntity(TileSolidFissionController.class, Global.MOD_ID + ":solid_fission_controller");
		GameRegistry.registerTileEntity(TileSolidFissionCell.class, Global.MOD_ID + ":solid_fission_cell");
		GameRegistry.registerTileEntity(TileSolidFissionSink.Water.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.WATER.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Iron.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.IRON.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Redstone.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.REDSTONE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Quartz.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.QUARTZ.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Obsidian.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.OBSIDIAN.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.NetherBrick.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.NETHER_BRICK.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Glowstone.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.GLOWSTONE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Lapis.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.LAPIS.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Gold.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.GOLD.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Prismarine.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.PRISMARINE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Slime.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.SLIME.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.EndStone.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.END_STONE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Purpur.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.PURPUR.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Diamond.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.DIAMOND.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Emerald.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.EMERALD.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Copper.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType.COPPER.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Tin.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.TIN.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Lead.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.LEAD.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Boron.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.BORON.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Lithium.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.LITHIUM.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Magnesium.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.MAGNESIUM.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Manganese.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.MANGANESE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Aluminum.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.ALUMINUM.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Silver.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.SILVER.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Fluorite.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.FLUORITE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Villiaumite.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.VILLIAUMITE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Carobbiite.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.CAROBBIITE.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Arsenic.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.ARSENIC.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.LiquidNitrogen.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.LIQUID_NITROGEN.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.LiquidHelium.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.LIQUID_HELIUM.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Enderium.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.ENDERIUM.getName());
		GameRegistry.registerTileEntity(TileSolidFissionSink.Cryotheum.class, Global.MOD_ID + ":solid_fission_sink_" + MetaEnums.HeatSinkType2.CRYOTHEUM.getName());
		
		GameRegistry.registerTileEntity(TileSaltFissionController.class, Global.MOD_ID + ":salt_fission_controller");
		GameRegistry.registerTileEntity(TileSaltFissionVessel.class, Global.MOD_ID + ":salt_fission_vessel");
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Standard.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.STANDARD.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Iron.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.IRON.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Redstone.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.REDSTONE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Quartz.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.QUARTZ.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Obsidian.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.OBSIDIAN.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.NetherBrick.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.NETHER_BRICK.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Glowstone.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.GLOWSTONE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Lapis.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.LAPIS.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Gold.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.GOLD.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Prismarine.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.PRISMARINE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Slime.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.SLIME.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.EndStone.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.END_STONE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Purpur.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.PURPUR.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Diamond.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.DIAMOND.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Emerald.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.EMERALD.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Copper.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType.COPPER.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Tin.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.TIN.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Lead.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.LEAD.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Boron.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.BORON.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Lithium.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.LITHIUM.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Magnesium.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.MAGNESIUM.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Manganese.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.MANGANESE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Aluminum.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.ALUMINUM.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Silver.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.SILVER.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Fluorite.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.FLUORITE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Villiaumite.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.VILLIAUMITE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Carobbiite.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.CAROBBIITE.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Arsenic.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.ARSENIC.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.LiquidNitrogen.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.LIQUID_NITROGEN.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.LiquidHelium.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.LIQUID_HELIUM.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Enderium.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.ENDERIUM.getName());
		GameRegistry.registerTileEntity(TileSaltFissionHeater.Cryotheum.class, Global.MOD_ID + ":salt_fission_heater_" + MetaEnums.CoolantHeaterType2.CRYOTHEUM.getName());
		
		GameRegistry.registerTileEntity(TileHeatExchangerController.class, Global.MOD_ID + ":heat_exchanger_controller");
		GameRegistry.registerTileEntity(TileHeatExchangerCasing.class, Global.MOD_ID + ":heat_exchanger_casing");
		GameRegistry.registerTileEntity(TileHeatExchangerGlass.class, Global.MOD_ID + ":heat_exchanger_glass");
		GameRegistry.registerTileEntity(TileHeatExchangerVent.class, Global.MOD_ID + ":heat_exchanger_vent");
		GameRegistry.registerTileEntity(TileHeatExchangerTube.Copper.class, Global.MOD_ID + ":heat_exchanger_tube_" + HeatExchangerTubeType.COPPER.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerTube.HardCarbon.class, Global.MOD_ID + ":heat_exchanger_tube_" + HeatExchangerTubeType.HARD_CARBON.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerTube.Thermoconducting.class, Global.MOD_ID + ":heat_exchanger_tube_" + HeatExchangerTubeType.THERMOCONDUCTING.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerComputerPort.class, Global.MOD_ID + ":heat_exchanger_computer_port");
		
		GameRegistry.registerTileEntity(TileCondenserController.class, Global.MOD_ID + ":condenser_controller");
		GameRegistry.registerTileEntity(TileCondenserTube.Copper.class, Global.MOD_ID + ":condenser_tube_" + HeatExchangerTubeType.COPPER.toString());
		GameRegistry.registerTileEntity(TileCondenserTube.HardCarbon.class, Global.MOD_ID + ":condenser_tube_" + HeatExchangerTubeType.HARD_CARBON.toString());
		GameRegistry.registerTileEntity(TileCondenserTube.Thermoconducting.class, Global.MOD_ID + ":condenser_tube_" + HeatExchangerTubeType.THERMOCONDUCTING.toString());
		
		GameRegistry.registerTileEntity(TileTurbineController.class, Global.MOD_ID + ":turbine_controller");
		GameRegistry.registerTileEntity(TileTurbineCasing.class, Global.MOD_ID + ":turbine_casing");
		GameRegistry.registerTileEntity(TileTurbineGlass.class, Global.MOD_ID + ":turbine_glass");
		GameRegistry.registerTileEntity(TileTurbineRotorShaft.class, Global.MOD_ID + ":turbine_rotor_shaft");
		GameRegistry.registerTileEntity(TileTurbineRotorBlade.Steel.class, Global.MOD_ID + ":turbine_rotor_blade_" + TurbineRotorBladeType.STEEL.toString());
		GameRegistry.registerTileEntity(TileTurbineRotorBlade.Extreme.class, Global.MOD_ID + ":turbine_rotor_blade_" + TurbineRotorBladeType.EXTREME.toString());
		GameRegistry.registerTileEntity(TileTurbineRotorBlade.SicSicCMC.class, Global.MOD_ID + ":turbine_rotor_blade_" + TurbineRotorBladeType.SIC_SIC_CMC.toString());
		GameRegistry.registerTileEntity(TileTurbineRotorStator.class, Global.MOD_ID + ":turbine_rotor_stator");
		GameRegistry.registerTileEntity(TileTurbineRotorBearing.class, Global.MOD_ID + ":turbine_rotor_bearing");
		GameRegistry.registerTileEntity(TileTurbineDynamoCoil.Magnesium.class, Global.MOD_ID + ":turbine_dynamo_coil_" + TurbineDynamoCoilType.MAGNESIUM.toString());
		GameRegistry.registerTileEntity(TileTurbineDynamoCoil.Beryllium.class, Global.MOD_ID + ":turbine_dynamo_coil_" + TurbineDynamoCoilType.BERYLLIUM.toString());
		GameRegistry.registerTileEntity(TileTurbineDynamoCoil.Aluminum.class, Global.MOD_ID + ":turbine_dynamo_coil_" + TurbineDynamoCoilType.ALUMINUM.toString());
		GameRegistry.registerTileEntity(TileTurbineDynamoCoil.Gold.class, Global.MOD_ID + ":turbine_dynamo_coil_" + TurbineDynamoCoilType.GOLD.toString());
		GameRegistry.registerTileEntity(TileTurbineDynamoCoil.Copper.class, Global.MOD_ID + ":turbine_dynamo_coil_" + TurbineDynamoCoilType.COPPER.toString());
		GameRegistry.registerTileEntity(TileTurbineDynamoCoil.Silver.class, Global.MOD_ID + ":turbine_dynamo_coil_" + TurbineDynamoCoilType.SILVER.toString());
		GameRegistry.registerTileEntity(TileTurbineCoilConnector.class, Global.MOD_ID + ":turbine_coil_connector");
		GameRegistry.registerTileEntity(TileTurbineInlet.class, Global.MOD_ID + ":turbine_inlet");
		GameRegistry.registerTileEntity(TileTurbineOutlet.class, Global.MOD_ID + ":turbine_outlet");
		GameRegistry.registerTileEntity(TileTurbineComputerPort.class, Global.MOD_ID + ":turbine_computer_port");
		
		GameRegistry.registerTileEntity(TileRTG.Uranium.class, Global.MOD_ID + ":rtg_uranium");
		GameRegistry.registerTileEntity(TileRTG.Plutonium.class, Global.MOD_ID + ":rtg_plutonium");
		GameRegistry.registerTileEntity(TileRTG.Americium.class, Global.MOD_ID + ":rtg_americium");
		GameRegistry.registerTileEntity(TileRTG.Californium.class, Global.MOD_ID + ":rtg_californium");

		GameRegistry.registerTileEntity(TileSolarPanel.Basic.class, Global.MOD_ID + ":solar_panel_basic");
		GameRegistry.registerTileEntity(TileSolarPanel.Advanced.class, Global.MOD_ID + ":solar_panel_advanced");
		GameRegistry.registerTileEntity(TileSolarPanel.DU.class, Global.MOD_ID + ":solar_panel_du");
		GameRegistry.registerTileEntity(TileSolarPanel.Elite.class, Global.MOD_ID + ":solar_panel_elite");
		
		GameRegistry.registerTileEntity(TileDecayGenerator.class, Global.MOD_ID + ":decay_generator");
		
		GameRegistry.registerTileEntity(TileBattery.VoltaicPileBasic.class, Global.MOD_ID + ":voltaic_pile_basic");
		GameRegistry.registerTileEntity(TileBattery.VoltaicPileAdvanced.class, Global.MOD_ID + ":voltaic_pile_advanced");
		GameRegistry.registerTileEntity(TileBattery.VoltaicPileDU.class, Global.MOD_ID + ":voltaic_pile_du");
		GameRegistry.registerTileEntity(TileBattery.VoltaicPileElite.class, Global.MOD_ID + ":voltaic_pile_elite");
		
		GameRegistry.registerTileEntity(TileBattery.LithiumIonBatteryBasic.class, Global.MOD_ID + ":lithium_ion_battery_basic");
		GameRegistry.registerTileEntity(TileBattery.LithiumIonBatteryAdvanced.class, Global.MOD_ID + ":lithium_ion_battery_advanced");
		GameRegistry.registerTileEntity(TileBattery.LithiumIonBatteryDU.class, Global.MOD_ID + ":lithium_ion_battery_du");
		GameRegistry.registerTileEntity(TileBattery.LithiumIonBatteryElite.class, Global.MOD_ID + ":lithium_ion_battery_elite");
		
		//GameRegistry.registerTileEntity(TileBuffer.class, Global.MOD_ID + ":buffer");
		GameRegistry.registerTileEntity(TileBin.class, Global.MOD_ID + ":bin");
		
		//GameRegistry.registerTileEntity(TilePassive.FusionElectromagnet.class, Global.MOD_ID + ":fusion_electromagnet");
		//GameRegistry.registerTileEntity(TilePassive.AcceleratorElectromagnet.class, Global.MOD_ID + ":accelerator_electromagnet");
		//GameRegistry.registerTileEntity(TilePassive.ElectromagnetSupercooler.class, Global.MOD_ID + ":electromagnet_supercooler");
		
		GameRegistry.registerTileEntity(TilePassive.CobblestoneGenerator.class, Global.MOD_ID + ":cobblestone_generator");
		GameRegistry.registerTileEntity(TilePassive.CobblestoneGeneratorCompact.class, Global.MOD_ID + ":cobblestone_generator_compact");
		GameRegistry.registerTileEntity(TilePassive.CobblestoneGeneratorDense.class, Global.MOD_ID + ":cobblestone_generator_dense");
		
		GameRegistry.registerTileEntity(TilePassive.WaterSource.class, Global.MOD_ID + ":water_source");
		GameRegistry.registerTileEntity(TilePassive.WaterSourceCompact.class, Global.MOD_ID + ":water_source_compact");
		GameRegistry.registerTileEntity(TilePassive.WaterSourceDense.class, Global.MOD_ID + ":water_source_dense");
		
		GameRegistry.registerTileEntity(TilePassive.NitrogenCollector.class, Global.MOD_ID + ":nitrogen_collector");
		GameRegistry.registerTileEntity(TilePassive.NitrogenCollectorCompact.class, Global.MOD_ID + ":nitrogen_collector_compact");
		GameRegistry.registerTileEntity(TilePassive.NitrogenCollectorDense.class, Global.MOD_ID + ":nitrogen_collector_dense");
		
		GameRegistry.registerTileEntity(TileRadiationScrubber.class, Global.MOD_ID + ":radiation_scrubber");
		
		GameRegistry.registerTileEntity(TileGeigerCounter.class, Global.MOD_ID + ":geiger_block");
		
		GameRegistry.registerTileEntity(TileQuantumComputerController.class, Global.MOD_ID + ":quantum_computer_controller");
		GameRegistry.registerTileEntity(TileQuantumComputerQubit.class, Global.MOD_ID + ":quantum_computer_qubit");
		
		GameRegistry.registerTileEntity(TileQuantumComputerGate.X.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.X.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.Y.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.Y.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.Z.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.Z.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.H.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.H.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.S.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.S.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.Sdg.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.Sdg.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.T.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.T.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.Tdg.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.Tdg.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.P.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.P.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.RX.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.RX.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.RY.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.RY.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.RZ.class, Global.MOD_ID + ":quantum_computer_gate_single_" + QuantumComputerGateEnums.SingleType.RZ.getName());
		
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CX.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CX.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CY.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CY.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CZ.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CZ.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CH.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CH.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CS.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CS.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CSdg.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CSdg.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CT.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CT.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CTdg.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CTdg.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CP.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CP.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CRX.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CRX.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CRY.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CRY.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.CRZ.class, Global.MOD_ID + ":quantum_computer_gate_control_" + QuantumComputerGateEnums.ControlType.CRZ.getName());
		
		GameRegistry.registerTileEntity(TileQuantumComputerGate.Swap.class, Global.MOD_ID + ":quantum_computer_gate_swap_" + QuantumComputerGateEnums.SwapType.SWAP.getName());
		GameRegistry.registerTileEntity(TileQuantumComputerGate.ControlSwap.class, Global.MOD_ID + ":quantum_computer_gate_swap_" + QuantumComputerGateEnums.SwapType.CSWAP.getName());
		
		GameRegistry.registerTileEntity(TileQuantumComputerConnector.class, Global.MOD_ID + ":quantum_computer_connector");
		GameRegistry.registerTileEntity(TileQuantumComputerPort.class, Global.MOD_ID + ":quantum_computer_port");
	}
}
