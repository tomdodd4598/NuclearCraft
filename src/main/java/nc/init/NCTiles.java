package nc.init;

import nc.Global;
import nc.enumm.MetaEnums;
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
import nc.multiblock.fission.tile.TileFissionPort;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.multiblock.heatExchanger.tile.TileCondenserController;
import nc.multiblock.heatExchanger.tile.TileCondenserTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerCasing;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerComputerPort;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerGlass;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerVent;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.tile.TileTurbineCasing;
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
import nc.tile.energy.battery.TileBattery;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.generator.TileRTG;
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
		GameRegistry.registerTileEntity(TileFissionPort.class, Global.MOD_ID + ":fission_port");
		GameRegistry.registerTileEntity(TileFissionVent.class, Global.MOD_ID + ":fission_vent");
		GameRegistry.registerTileEntity(TileFissionIrradiator.class, Global.MOD_ID + ":fission_irradiator");
		GameRegistry.registerTileEntity(TileFissionSource.RadiumBeryllium.class, Global.MOD_ID + ":fission_source_" + MetaEnums.NeutronSourceType.RADIUM_BERYLLIUM.getName());
		GameRegistry.registerTileEntity(TileFissionSource.PoloniumBeryllium.class, Global.MOD_ID + ":fission_source_" + MetaEnums.NeutronSourceType.POLONIUM_BERYLLIUM.getName());
		GameRegistry.registerTileEntity(TileFissionSource.Californium.class, Global.MOD_ID + ":fission_source_" + MetaEnums.NeutronSourceType.CALIFORNIUM.getName());
		GameRegistry.registerTileEntity(TileFissionComputerPort.class, Global.MOD_ID + ":fission_computer_port");
		
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
		GameRegistry.registerTileEntity(TileSaltFissionHeater.class, Global.MOD_ID + ":salt_fission_heater");
		
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
		
		//GameRegistry.registerTileEntity(TileSpin.class, Global.MOD_ID + ":spin");
	}
}
