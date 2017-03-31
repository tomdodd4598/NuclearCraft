package nc.init;

import nc.Global;
import nc.tile.energy.generator.TileFissionController;
import nc.tile.energy.generator.TileRTGs.AmericiumRTG;
import nc.tile.energy.generator.TileRTGs.CaliforniumRTG;
import nc.tile.energy.generator.TileRTGs.PlutoniumRTG;
import nc.tile.energy.generator.TileRTGs.UraniumRTG;
import nc.tile.energy.generator.TileSolarPanels.SolarPanelBasic;
import nc.tile.energy.processor.TileEnergyProcessors.TileDecayHastener;
import nc.tile.energy.processor.TileEnergyProcessors.TileFuelReprocessor;
import nc.tile.energy.processor.TileEnergyProcessors.TileIsotopeSeparator;
import nc.tile.energy.processor.TileEnergyProcessors.TileManufactory;
import nc.tile.energy.storage.TileBatteries.LithiumIonBatteryBasic;
import nc.tile.energy.storage.TileBatteries.VoltaicPileBasic;
import nc.tile.processor.TileNuclearFurnace;
import nc.util.NCUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCTiles {
	
	public static void register() {
		GameRegistry.registerTileEntity(TileNuclearFurnace.class, Global.MOD_ID + ":nuclear_furnace");
		GameRegistry.registerTileEntity(TileManufactory.class, Global.MOD_ID + ":manufactory");
		GameRegistry.registerTileEntity(TileIsotopeSeparator.class, Global.MOD_ID + ":isotope_separator");
		GameRegistry.registerTileEntity(TileDecayHastener.class, Global.MOD_ID + ":decay_hastener");
		GameRegistry.registerTileEntity(TileFuelReprocessor.class, Global.MOD_ID + ":fuel_reprocessor");
		
		GameRegistry.registerTileEntity(TileFissionController.class, Global.MOD_ID + ":fission_controller");
		
		GameRegistry.registerTileEntity(UraniumRTG.class, Global.MOD_ID + ":rtg_uranium");
		GameRegistry.registerTileEntity(PlutoniumRTG.class, Global.MOD_ID + ":rtg_plutonium");
		GameRegistry.registerTileEntity(AmericiumRTG.class, Global.MOD_ID + ":rtg_americium");
		GameRegistry.registerTileEntity(CaliforniumRTG.class, Global.MOD_ID + ":rtg_californium");

		GameRegistry.registerTileEntity(SolarPanelBasic.class, Global.MOD_ID + ":solar_panel_basic");
		
		GameRegistry.registerTileEntity(VoltaicPileBasic.class, Global.MOD_ID + ":voltaic_pile_basic");
		GameRegistry.registerTileEntity(LithiumIonBatteryBasic.class, Global.MOD_ID + ":lithium_ion_battery_basic");
		
		NCUtils.getLogger().info("Registered tile entities");
	}
}
