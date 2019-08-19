package nc.init;

import nc.Global;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerComputerPort;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerCondenserTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerFrame;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerGlass;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerVent;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerWall;
import nc.multiblock.saltFission.tile.TileSaltFissionBeam;
import nc.multiblock.saltFission.tile.TileSaltFissionComputerPort;
import nc.multiblock.saltFission.tile.TileSaltFissionController;
import nc.multiblock.saltFission.tile.TileSaltFissionDistributor;
import nc.multiblock.saltFission.tile.TileSaltFissionFrame;
import nc.multiblock.saltFission.tile.TileSaltFissionGlass;
import nc.multiblock.saltFission.tile.TileSaltFissionHeater;
import nc.multiblock.saltFission.tile.TileSaltFissionModerator;
import nc.multiblock.saltFission.tile.TileSaltFissionRedstonePort;
import nc.multiblock.saltFission.tile.TileSaltFissionRetriever;
import nc.multiblock.saltFission.tile.TileSaltFissionVent;
import nc.multiblock.saltFission.tile.TileSaltFissionVessel;
import nc.multiblock.saltFission.tile.TileSaltFissionWall;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.tile.TileTurbineComputerPort;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.multiblock.turbine.tile.TileTurbineFrame;
import nc.multiblock.turbine.tile.TileTurbineGlass;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import nc.multiblock.turbine.tile.TileTurbineWall;
import nc.tile.TileBin;
import nc.tile.dummy.TileFissionPort;
import nc.tile.dummy.TileFusionDummy;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.energy.battery.TileBattery;
import nc.tile.energyFluid.TileBuffer;
import nc.tile.fluid.TileActiveCooler;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.generator.TileFissionController;
import nc.tile.generator.TileFusionCore;
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
		GameRegistry.registerTileEntity(TileProcessor.IsotopeSeparator.class, Global.MOD_ID + ":isotope_separator");
		GameRegistry.registerTileEntity(TileProcessor.DecayHastener.class, Global.MOD_ID + ":decay_hastener");
		GameRegistry.registerTileEntity(TileProcessor.FuelReprocessor.class, Global.MOD_ID + ":fuel_reprocessor");
		GameRegistry.registerTileEntity(TileProcessor.AlloyFurnace.class, Global.MOD_ID + ":alloy_furnace");
		GameRegistry.registerTileEntity(TileProcessor.Infuser.class, Global.MOD_ID + ":infuser");
		GameRegistry.registerTileEntity(TileProcessor.Melter.class, Global.MOD_ID + ":melter");
		GameRegistry.registerTileEntity(TileProcessor.Supercooler.class, Global.MOD_ID + ":supercooler");
		GameRegistry.registerTileEntity(TileProcessor.Electrolyser.class, Global.MOD_ID + ":electrolyser");
		GameRegistry.registerTileEntity(TileProcessor.Irradiator.class, Global.MOD_ID + ":irradiator");
		GameRegistry.registerTileEntity(TileProcessor.IngotFormer.class, Global.MOD_ID + ":ingot_former");
		GameRegistry.registerTileEntity(TileProcessor.Pressurizer.class, Global.MOD_ID + ":pressurizer");
		GameRegistry.registerTileEntity(TileProcessor.ChemicalReactor.class, Global.MOD_ID + ":chemical_reactor");
		GameRegistry.registerTileEntity(TileProcessor.SaltMixer.class, Global.MOD_ID + ":salt_mixer");
		GameRegistry.registerTileEntity(TileProcessor.Crystallizer.class, Global.MOD_ID + ":crystallizer");
		GameRegistry.registerTileEntity(TileProcessor.Dissolver.class, Global.MOD_ID + ":dissolver");
		GameRegistry.registerTileEntity(TileProcessor.Extractor.class, Global.MOD_ID + ":extractor");
		GameRegistry.registerTileEntity(TileProcessor.Centrifuge.class, Global.MOD_ID + ":centrifuge");
		GameRegistry.registerTileEntity(TileProcessor.RockCrusher.class, Global.MOD_ID + ":rock_crusher");
		
		GameRegistry.registerTileEntity(TileMachineInterface.class, Global.MOD_ID + ":machine_interface");
		
		GameRegistry.registerTileEntity(TileFissionController.Old.class, Global.MOD_ID + ":fission_controller_old");
		GameRegistry.registerTileEntity(TileFissionController.New.class, Global.MOD_ID + ":fission_controller_new");
		GameRegistry.registerTileEntity(TileFissionPort.class, Global.MOD_ID + ":fission_port");
		
		GameRegistry.registerTileEntity(TileFusionCore.class, Global.MOD_ID + ":fusion_core");
		GameRegistry.registerTileEntity(TileFusionDummy.Side.class, Global.MOD_ID + ":fusion_dummy_side");
		GameRegistry.registerTileEntity(TileFusionDummy.Top.class, Global.MOD_ID + ":fusion_dummy_top");
		
		GameRegistry.registerTileEntity(TileSaltFissionController.class, Global.MOD_ID + ":salt_fission_controller");
		GameRegistry.registerTileEntity(TileSaltFissionWall.class, Global.MOD_ID + ":salt_fission_wall");
		GameRegistry.registerTileEntity(TileSaltFissionGlass.class, Global.MOD_ID + ":salt_fission_glass");
		GameRegistry.registerTileEntity(TileSaltFissionFrame.class, Global.MOD_ID + ":salt_fission_frame");
		GameRegistry.registerTileEntity(TileSaltFissionBeam.class, Global.MOD_ID + ":salt_fission_beam");
		GameRegistry.registerTileEntity(TileSaltFissionVent.class, Global.MOD_ID + ":salt_fission_vent");
		GameRegistry.registerTileEntity(TileSaltFissionVessel.class, Global.MOD_ID + ":salt_fission_vessel");
		GameRegistry.registerTileEntity(TileSaltFissionHeater.class, Global.MOD_ID + ":salt_fission_heater");
		GameRegistry.registerTileEntity(TileSaltFissionModerator.class, Global.MOD_ID + ":salt_fission_moderator");
		GameRegistry.registerTileEntity(TileSaltFissionDistributor.class, Global.MOD_ID + ":salt_fission_distributor");
		GameRegistry.registerTileEntity(TileSaltFissionRetriever.class, Global.MOD_ID + ":salt_fission_retriever");
		GameRegistry.registerTileEntity(TileSaltFissionRedstonePort.class, Global.MOD_ID + ":salt_fission_redstone_port");
		GameRegistry.registerTileEntity(TileSaltFissionComputerPort.class, Global.MOD_ID + ":salt_fission_computer_port");
		
		GameRegistry.registerTileEntity(TileHeatExchangerController.class, Global.MOD_ID + ":heat_exchanger_controller");
		GameRegistry.registerTileEntity(TileHeatExchangerWall.class, Global.MOD_ID + ":heat_exchanger_wall");
		GameRegistry.registerTileEntity(TileHeatExchangerGlass.class, Global.MOD_ID + ":heat_exchanger_glass");
		GameRegistry.registerTileEntity(TileHeatExchangerFrame.class, Global.MOD_ID + ":heat_exchanger_frame");
		GameRegistry.registerTileEntity(TileHeatExchangerVent.class, Global.MOD_ID + ":heat_exchanger_vent");
		GameRegistry.registerTileEntity(TileHeatExchangerTube.Copper.class, Global.MOD_ID + ":heat_exchanger_tube_" + HeatExchangerTubeType.COPPER.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerTube.HardCarbon.class, Global.MOD_ID + ":heat_exchanger_tube_" + HeatExchangerTubeType.HARD_CARBON.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerTube.Thermoconducting.class, Global.MOD_ID + ":heat_exchanger_tube_" + HeatExchangerTubeType.THERMOCONDUCTING.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerCondenserTube.Copper.class, Global.MOD_ID + ":heat_exchanger_condenser_tube_" + HeatExchangerTubeType.COPPER.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerCondenserTube.HardCarbon.class, Global.MOD_ID + ":heat_exchanger_condenser_tube_" + HeatExchangerTubeType.HARD_CARBON.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerCondenserTube.Thermoconducting.class, Global.MOD_ID + ":heat_exchanger_condenser_tube_" + HeatExchangerTubeType.THERMOCONDUCTING.toString());
		GameRegistry.registerTileEntity(TileHeatExchangerComputerPort.class, Global.MOD_ID + ":heat_exchanger_computer_port");
		
		GameRegistry.registerTileEntity(TileTurbineController.class, Global.MOD_ID + ":turbine_controller");
		GameRegistry.registerTileEntity(TileTurbineWall.class, Global.MOD_ID + ":turbine_wall");
		GameRegistry.registerTileEntity(TileTurbineGlass.class, Global.MOD_ID + ":turbine_glass");
		GameRegistry.registerTileEntity(TileTurbineFrame.class, Global.MOD_ID + ":turbine_frame");
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
		
		GameRegistry.registerTileEntity(TileBuffer.class, Global.MOD_ID + ":buffer");
		GameRegistry.registerTileEntity(TileActiveCooler.class, Global.MOD_ID + ":active_cooler");
		GameRegistry.registerTileEntity(TileBin.class, Global.MOD_ID + ":bin");
		
		GameRegistry.registerTileEntity(TilePassive.FusionElectromagnet.class, Global.MOD_ID + ":fusion_electromagnet");
		GameRegistry.registerTileEntity(TilePassive.AcceleratorElectromagnet.class, Global.MOD_ID + ":accelerator_electromagnet");
		GameRegistry.registerTileEntity(TilePassive.ElectromagnetSupercooler.class, Global.MOD_ID + ":electromagnet_supercooler");
		
		GameRegistry.registerTileEntity(TilePassive.HeliumCollector.class, Global.MOD_ID + ":helium_collector");
		GameRegistry.registerTileEntity(TilePassive.HeliumCollectorCompact.class, Global.MOD_ID + ":helium_collector_compact");
		GameRegistry.registerTileEntity(TilePassive.HeliumCollectorDense.class, Global.MOD_ID + ":helium_collector_dense");
		
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
