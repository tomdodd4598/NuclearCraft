package nc.handler;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.*;
import nc.block.tile.BlockSimpleTileInfo;
import nc.container.ContainerFunction;
import nc.container.processor.*;
import nc.gui.GuiFunction;
import nc.gui.processor.GuiManufactory;
import nc.tab.NCTabs;
import nc.tile.TileBin;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.generator.*;
import nc.tile.passive.TilePassive;
import nc.tile.processor.*;
import nc.tile.radiation.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public class TileInfo {
	
	private static final Object2ObjectMap<String, BlockSimpleTileInfo<?>> BLOCK_SIMPLE_TILE_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	private static final Object2ObjectMap<String, ProcessorBlockInfo<?>> BLOCK_PROCESSOR_INFO_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectMap<String, ProcessorContainerInfo<?>> GUI_PROCESSOR_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	public static void init() {
		putBlockSimpleTileInfo("machine_interface", TileMachineInterface::new, NCTabs.machine());
		putBlockSimpleTileInfo("decay_generator", TileDecayGenerator::new, NCTabs.machine());
		putBlockSimpleTileInfo("bin", TileBin::new, NCTabs.machine());
		
		putBlockSimpleTileInfo("solar_panel_basic", TileSolarPanel.Basic::new, NCTabs.machine());
		putBlockSimpleTileInfo("solar_panel_advanced", TileSolarPanel.Advanced::new, NCTabs.machine());
		putBlockSimpleTileInfo("solar_panel_du", TileSolarPanel.DU::new, NCTabs.machine());
		putBlockSimpleTileInfo("solar_panel_elite", TileSolarPanel.Elite::new, NCTabs.machine());
		
		putBlockSimpleTileInfo("cobblestone_generator", TilePassive.CobblestoneGenerator::new, NCTabs.machine());
		putBlockSimpleTileInfo("cobblestone_generator_compact", TilePassive.CobblestoneGeneratorCompact::new, NCTabs.machine());
		putBlockSimpleTileInfo("cobblestone_generator_dense", TilePassive.CobblestoneGeneratorDense::new, NCTabs.machine());
		
		putBlockSimpleTileInfo("water_source", TilePassive.WaterSource::new, NCTabs.machine());
		putBlockSimpleTileInfo("water_source_compact", TilePassive.WaterSourceCompact::new, NCTabs.machine());
		putBlockSimpleTileInfo("water_source_dense", TilePassive.WaterSourceDense::new, NCTabs.machine());
		
		putBlockSimpleTileInfo("nitrogen_collector", TilePassive.NitrogenCollector::new, NCTabs.machine());
		putBlockSimpleTileInfo("nitrogen_collector_compact", TilePassive.NitrogenCollectorCompact::new, NCTabs.machine());
		putBlockSimpleTileInfo("nitrogen_collector_dense", TilePassive.NitrogenCollectorDense::new, NCTabs.machine());
		
		putBlockSimpleTileInfo("radiation_scrubber", TileRadiationScrubber::new, NCTabs.radiation());
		
		putBlockSimpleTileInfo("geiger_block", TileGeigerCounter::new, NCTabs.radiation());
		
		putProcessorInfo("manufactory", TileProcessor.Manufactory::new, Lists.newArrayList("reddust", "crit"), ContainerManufactory::new, GuiManufactory::new, ContainerMachineConfig::new, GuiManufactory.SideConfig::new, 1, 0, 1, 0, false);
		putProcessorInfo("separator", TileProcessor.Separator::new, Lists.newArrayList("reddust", "smoke"));
		putProcessorInfo("decay_hastener", TileProcessor.DecayHastener::new, Lists.newArrayList("reddust"));
		putProcessorInfo("fuel_reprocessor", TileProcessor.FuelReprocessor::new, Lists.newArrayList("reddust", "smoke"));
		putProcessorInfo("alloy_furnace", TileProcessor.AlloyFurnace::new, Lists.newArrayList("smoke", "reddust"));
		putProcessorInfo("infuser", TileProcessor.Infuser::new, Lists.newArrayList("portal", "reddust"));
		putProcessorInfo("melter", TileProcessor.Melter::new, Lists.newArrayList("flame", "lava"));
		putProcessorInfo("supercooler", TileProcessor.Supercooler::new, Lists.newArrayList("snowshovel", "smoke"));
		putProcessorInfo("electrolyzer", TileProcessor.Electrolyzer::new, Lists.newArrayList("reddust", "splash"));
		putProcessorInfo("assembler", TileProcessor.Assembler::new, Lists.newArrayList("smoke", "crit"));
		putProcessorInfo("ingot_former", TileProcessor.IngotFormer::new, Lists.newArrayList("smoke"));
		putProcessorInfo("pressurizer", TileProcessor.Pressurizer::new, Lists.newArrayList("smoke"));
		putProcessorInfo("chemical_reactor", TileProcessor.ChemicalReactor::new, Lists.newArrayList("reddust"));
		putProcessorInfo("salt_mixer", TileProcessor.SaltMixer::new, Lists.newArrayList("reddust", "endRod"));
		putProcessorInfo("crystallizer", TileProcessor.Crystallizer::new, Lists.newArrayList("depthsuspend"));
		putProcessorInfo("enricher", TileProcessor.Enricher::new, Lists.newArrayList("splash", "depthsuspend"));
		putProcessorInfo("extractor", TileProcessor.Extractor::new, Lists.newArrayList("reddust", "depthsuspend"));
		putProcessorInfo("centrifuge", TileProcessor.Centrifuge::new, Lists.newArrayList("endRod", "depthsuspend"));
		putProcessorInfo("rock_crusher", TileProcessor.RockCrusher::new, Lists.newArrayList("smoke"));
	}
	
	public static <TILE extends TileEntity> void putBlockSimpleTileInfo(String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		BLOCK_SIMPLE_TILE_INFO_MAP.put(name, new BlockSimpleTileInfo<>(name, tileSupplier, creativeTab));
	}
	
	public static BlockSimpleTileInfo<?> getBlockSimpleTileInfo(String name) {
		return BLOCK_SIMPLE_TILE_INFO_MAP.get(name);
	}
	
	public static <TILE extends TileEntity> void putProcessorInfo(String name, Supplier<TILE> tileSupplier, List<String> particles, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, boolean consumesInputs) {
		BLOCK_PROCESSOR_INFO_MAP.put(name, new ProcessorBlockInfo<>(name, tileSupplier, particles));
		GUI_PROCESSOR_INFO_MAP.put(name, new ProcessorContainerInfo<>(name, containerFunction, guiFunction, configContainerFunction, configGuiFunction, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, consumesInputs));
	}
	
	public static ProcessorBlockInfo<?> getBlockProcessorInfo(String name) {
		return BLOCK_PROCESSOR_INFO_MAP.get(name);
	}
	
	public static ProcessorContainerInfo<?> getContainerInfoProcessorInfo(String name) {
		return GUI_PROCESSOR_INFO_MAP.get(name);
	}
}
