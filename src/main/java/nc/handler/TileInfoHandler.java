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

public class TileInfoHandler {
	
	private static final Object2ObjectMap<String, BlockSimpleTileInfo<?>> BLOCK_SIMPLE_TILE_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	private static final Object2ObjectMap<String, ProcessorBlockInfo<?>> BLOCK_PROCESSOR_INFO_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectMap<String, ProcessorContainerInfo<?, ?>> GUI_PROCESSOR_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
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
		
		putProcessorInfo("manufactory", TileBasicProcessor.Manufactory::new, Lists.newArrayList("reddust", "crit"), ContainerManufactory::new, GuiManufactory::new, ContainerMachineConfig::new, GuiManufactory.SideConfig::new, 1, 0, 1, 0, false);
		putProcessorInfo("separator", TileBasicProcessor.Separator::new, Lists.newArrayList("reddust", "smoke"));
		putProcessorInfo("decay_hastener", TileBasicProcessor.DecayHastener::new, Lists.newArrayList("reddust"));
		putProcessorInfo("fuel_reprocessor", TileBasicProcessor.FuelReprocessor::new, Lists.newArrayList("reddust", "smoke"));
		putProcessorInfo("alloy_furnace", TileBasicProcessor.AlloyFurnace::new, Lists.newArrayList("smoke", "reddust"));
		putProcessorInfo("infuser", TileBasicProcessor.Infuser::new, Lists.newArrayList("portal", "reddust"));
		putProcessorInfo("melter", TileBasicProcessor.Melter::new, Lists.newArrayList("flame", "lava"));
		putProcessorInfo("supercooler", TileBasicProcessor.Supercooler::new, Lists.newArrayList("snowshovel", "smoke"));
		putProcessorInfo("electrolyzer", TileBasicProcessor.Electrolyzer::new, Lists.newArrayList("reddust", "splash"));
		putProcessorInfo("assembler", TileBasicProcessor.Assembler::new, Lists.newArrayList("smoke", "crit"));
		putProcessorInfo("ingot_former", TileBasicProcessor.IngotFormer::new, Lists.newArrayList("smoke"));
		putProcessorInfo("pressurizer", TileBasicProcessor.Pressurizer::new, Lists.newArrayList("smoke"));
		putProcessorInfo("chemical_reactor", TileBasicProcessor.ChemicalReactor::new, Lists.newArrayList("reddust"));
		putProcessorInfo("salt_mixer", TileBasicProcessor.SaltMixer::new, Lists.newArrayList("reddust", "endRod"));
		putProcessorInfo("crystallizer", TileBasicProcessor.Crystallizer::new, Lists.newArrayList("depthsuspend"));
		putProcessorInfo("enricher", TileBasicProcessor.Enricher::new, Lists.newArrayList("splash", "depthsuspend"));
		putProcessorInfo("extractor", TileBasicProcessor.Extractor::new, Lists.newArrayList("reddust", "depthsuspend"));
		putProcessorInfo("centrifuge", TileBasicProcessor.Centrifuge::new, Lists.newArrayList("endRod", "depthsuspend"));
		putProcessorInfo("rock_crusher", TileBasicProcessor.RockCrusher::new, Lists.newArrayList("smoke"));
	}
	
	public static <TILE extends TileEntity> void putBlockSimpleTileInfo(String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		BLOCK_SIMPLE_TILE_INFO_MAP.put(name, new BlockSimpleTileInfo<>(name, tileSupplier, creativeTab));
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends BlockSimpleTileInfo<TILE>> INFO getBlockSimpleTileInfo(String name) {
		return (INFO) BLOCK_SIMPLE_TILE_INFO_MAP.get(name);
	}
	
	public static <TILE extends TileEntity> void putProcessorInfo(String name, Supplier<TILE> tileSupplier, List<String> particles, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean consumesInputs, boolean losesProgress, int playerInventoryX, int playerInventoryY) {
		BLOCK_PROCESSOR_INFO_MAP.put(name, new ProcessorBlockInfo<>(name, tileSupplier, particles));
		GUI_PROCESSOR_INFO_MAP.put(name, new ProcessorContainerInfo<>(name, containerFunction, guiFunction, configContainerFunction, configGuiFunction, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, consumesInputs, losesProgress, playerInventoryX, playerInventoryY));
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends ProcessorBlockInfo<TILE>> INFO getBlockProcessorInfo(String name) {
		return (INFO) BLOCK_PROCESSOR_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> INFO getContainerInfoProcessorInfo(String name) {
		return (INFO) GUI_PROCESSOR_INFO_MAP.get(name);
	}
}
