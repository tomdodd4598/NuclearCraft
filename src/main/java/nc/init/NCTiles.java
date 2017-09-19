package nc.init;

import nc.Global;
import nc.tile.dummy.TileFissionPort;
import nc.tile.dummy.TileFusionDummySide;
import nc.tile.dummy.TileFusionDummyTop;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.energyFluid.TileBuffer;
import nc.tile.energyStorage.Batteries.LithiumIonBatteryBasic;
import nc.tile.energyStorage.Batteries.VoltaicPileBasic;
import nc.tile.fluid.TileActiveCooler;
import nc.tile.generator.RTGs.AmericiumRTG;
import nc.tile.generator.RTGs.CaliforniumRTG;
import nc.tile.generator.RTGs.PlutoniumRTG;
import nc.tile.generator.RTGs.UraniumRTG;
import nc.tile.generator.SolarPanels.SolarPanelBasic;
import nc.tile.generator.TileFissionController;
import nc.tile.generator.TileFusionCore;
import nc.tile.passive.Passives.TileAcceleratorElectromagnet;
import nc.tile.passive.Passives.TileCobblestoneGenerator;
import nc.tile.passive.Passives.TileElectromagnetSupercooler;
import nc.tile.passive.Passives.TileFusionElectromagnet;
import nc.tile.passive.Passives.TileHeliumCollector;
import nc.tile.passive.Passives.TileNitrogenCollector;
import nc.tile.passive.Passives.TileWaterSource;
import nc.tile.processor.Processors.TileAlloyFurnace;
import nc.tile.processor.Processors.TileChemicalReactor;
import nc.tile.processor.Processors.TileCrystallizer;
import nc.tile.processor.Processors.TileDecayHastener;
import nc.tile.processor.Processors.TileElectrolyser;
import nc.tile.processor.Processors.TileFuelReprocessor;
import nc.tile.processor.Processors.TileInfuser;
import nc.tile.processor.Processors.TileIngotFormer;
import nc.tile.processor.Processors.TileIrradiator;
import nc.tile.processor.Processors.TileIsotopeSeparator;
import nc.tile.processor.Processors.TileManufactory;
import nc.tile.processor.Processors.TileMelter;
import nc.tile.processor.Processors.TilePressurizer;
import nc.tile.processor.Processors.TileSaltMixer;
import nc.tile.processor.Processors.TileSupercooler;
import nc.tile.processor.TileNuclearFurnace;
import nc.util.NCUtil;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCTiles {
	
	public static void register() {
		GameRegistry.registerTileEntity(TileNuclearFurnace.class, Global.MOD_ID + ":nuclear_furnace");
		GameRegistry.registerTileEntity(TileManufactory.class, Global.MOD_ID + ":manufactory");
		GameRegistry.registerTileEntity(TileIsotopeSeparator.class, Global.MOD_ID + ":isotope_separator");
		GameRegistry.registerTileEntity(TileDecayHastener.class, Global.MOD_ID + ":decay_hastener");
		GameRegistry.registerTileEntity(TileFuelReprocessor.class, Global.MOD_ID + ":fuel_reprocessor");
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, Global.MOD_ID + ":alloy_furnace");
		GameRegistry.registerTileEntity(TileInfuser.class, Global.MOD_ID + ":infuser");
		GameRegistry.registerTileEntity(TileMelter.class, Global.MOD_ID + ":melter");
		GameRegistry.registerTileEntity(TileSupercooler.class, Global.MOD_ID + ":supercooler");
		GameRegistry.registerTileEntity(TileElectrolyser.class, Global.MOD_ID + ":electrolyser");
		GameRegistry.registerTileEntity(TileIrradiator.class, Global.MOD_ID + ":irradiator");
		GameRegistry.registerTileEntity(TileIngotFormer.class, Global.MOD_ID + ":ingot_former");
		GameRegistry.registerTileEntity(TilePressurizer.class, Global.MOD_ID + ":pressurizer");
		GameRegistry.registerTileEntity(TileChemicalReactor.class, Global.MOD_ID + ":chemical_reactor");
		GameRegistry.registerTileEntity(TileSaltMixer.class, Global.MOD_ID + ":salt_mixer");
		GameRegistry.registerTileEntity(TileCrystallizer.class, Global.MOD_ID + ":crystallizer");
		
		GameRegistry.registerTileEntity(TileMachineInterface.class, Global.MOD_ID + ":machine_interface");
		
		GameRegistry.registerTileEntity(TileFissionController.class, Global.MOD_ID + ":fission_controller");
		GameRegistry.registerTileEntity(TileFissionPort.class, Global.MOD_ID + ":fission_port");
		
		GameRegistry.registerTileEntity(TileFusionCore.class, Global.MOD_ID + ":fusion_core");
		GameRegistry.registerTileEntity(TileFusionDummySide.class, Global.MOD_ID + ":fusion_dummy_side");
		GameRegistry.registerTileEntity(TileFusionDummyTop.class, Global.MOD_ID + ":fusion_dummy_top");
		
		GameRegistry.registerTileEntity(UraniumRTG.class, Global.MOD_ID + ":rtg_uranium");
		GameRegistry.registerTileEntity(PlutoniumRTG.class, Global.MOD_ID + ":rtg_plutonium");
		GameRegistry.registerTileEntity(AmericiumRTG.class, Global.MOD_ID + ":rtg_americium");
		GameRegistry.registerTileEntity(CaliforniumRTG.class, Global.MOD_ID + ":rtg_californium");

		GameRegistry.registerTileEntity(SolarPanelBasic.class, Global.MOD_ID + ":solar_panel_basic");
		
		GameRegistry.registerTileEntity(VoltaicPileBasic.class, Global.MOD_ID + ":voltaic_pile_basic");
		GameRegistry.registerTileEntity(LithiumIonBatteryBasic.class, Global.MOD_ID + ":lithium_ion_battery_basic");
		
		GameRegistry.registerTileEntity(TileBuffer.class, Global.MOD_ID + ":buffer");
		GameRegistry.registerTileEntity(TileActiveCooler.class, Global.MOD_ID + ":active_cooler");
		
		GameRegistry.registerTileEntity(TileFusionElectromagnet.class, Global.MOD_ID + ":fusion_electromagnet");
		GameRegistry.registerTileEntity(TileAcceleratorElectromagnet.class, Global.MOD_ID + ":accelerator_electromagnet");
		GameRegistry.registerTileEntity(TileElectromagnetSupercooler.class, Global.MOD_ID + ":electromagnet_supercooler");
		
		GameRegistry.registerTileEntity(TileHeliumCollector.class, Global.MOD_ID + ":helium_collector");
		GameRegistry.registerTileEntity(TileCobblestoneGenerator.class, Global.MOD_ID + ":cobblestone_generator");
		GameRegistry.registerTileEntity(TileWaterSource.class, Global.MOD_ID + ":water_source");
		GameRegistry.registerTileEntity(TileNitrogenCollector.class, Global.MOD_ID + ":nitrogen_collector");
		
		//GameRegistry.registerTileEntity(TileSpin.class, Global.MOD_ID + ":spin");
		
		NCUtil.getLogger().info("Registered tile entities");
	}
}
