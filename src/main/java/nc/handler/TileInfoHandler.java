package nc.handler;

import static nc.config.NCConfig.*;

import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.block.tile.BlockSimpleTileInfo;
import nc.container.processor.ContainerProcessorImpl.*;
import nc.gui.processor.GuiProcessorImpl.*;
import nc.tab.NCTabs;
import nc.tile.TileBin;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.generator.*;
import nc.tile.passive.TilePassive;
import nc.tile.processor.IProcessor;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.*;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilder;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicUpgradableProcessorContainerInfoBuilder;
import nc.tile.radiation.TileGeigerCounter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public class TileInfoHandler {
	
	private static final Object2ObjectMap<String, BlockSimpleTileInfo<?>> BLOCK_SIMPLE_TILE_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	private static final Object2ObjectMap<String, ProcessorBlockInfo<?>> BLOCK_PROCESSOR_INFO_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectMap<String, ProcessorContainerInfo<?, ?>> GUI_PROCESSOR_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	public static void init() {
		putBlockSimpleTileInfo(Global.MOD_ID, "machine_interface", TileMachineInterface::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "decay_generator", TileDecayGenerator::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "bin", TileBin::new, NCTabs.machine);
		
		putBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_basic", TileSolarPanel.Basic::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_advanced", TileSolarPanel.Advanced::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_du", TileSolarPanel.DU::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_elite", TileSolarPanel.Elite::new, NCTabs.machine);
		
		putBlockSimpleTileInfo(Global.MOD_ID, "cobblestone_generator", TilePassive.CobblestoneGenerator::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "cobblestone_generator_compact", TilePassive.CobblestoneGeneratorCompact::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "cobblestone_generator_dense", TilePassive.CobblestoneGeneratorDense::new, NCTabs.machine);
		
		putBlockSimpleTileInfo(Global.MOD_ID, "water_source", TilePassive.WaterSource::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "water_source_compact", TilePassive.WaterSourceCompact::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "water_source_dense", TilePassive.WaterSourceDense::new, NCTabs.machine);
		
		putBlockSimpleTileInfo(Global.MOD_ID, "nitrogen_collector", TilePassive.NitrogenCollector::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "nitrogen_collector_compact", TilePassive.NitrogenCollectorCompact::new, NCTabs.machine);
		putBlockSimpleTileInfo(Global.MOD_ID, "nitrogen_collector_dense", TilePassive.NitrogenCollectorDense::new, NCTabs.machine);
		
		putBlockSimpleTileInfo(Global.MOD_ID, "geiger_block", TileGeigerCounter::new, NCTabs.radiation);
		
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "manufactory", Manufactory::new, ContainerManufactory::new, GuiManufactory::new).setParticles("crit", "reddust").setDefaultProcessTime(processor_time[0]).setDefaultProcessPower(processor_power[0]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "separator", Separator::new, ContainerSeparator::new, GuiSeparator::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[1]).setDefaultProcessPower(processor_power[1]).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31), bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "decay_hastener", DecayHastener::new, ContainerDecayHastener::new, GuiDecayHastener::new).setParticles("reddust").setDefaultProcessTime(processor_time[2]).setDefaultProcessPower(processor_power[2]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "fuel_reprocessor", FuelReprocessor::new, ContainerFuelReprocessor::new, GuiFuelReprocessor::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[3]).setDefaultProcessPower(processor_power[3]).standardExtend(0, 12).setItemInputSlots(standardSlot(30, 41)).setItemOutputSlots(standardSlot(86, 31), standardSlot(106, 31), standardSlot(126, 31), standardSlot(146, 31), standardSlot(86, 51), standardSlot(106, 51), standardSlot(126, 51), standardSlot(146, 51)).setProgressBarGuiXYWHUV(48, 30, 37, 38, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "alloy_furnace", AlloyFurnace::new, ContainerAlloyFurnace::new, GuiAlloyFurnace::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[4]).setDefaultProcessPower(processor_power[4]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "infuser", Infuser::new, ContainerInfuser::new, GuiInfuser::new).setParticles("portal", "reddust").setDefaultProcessTime(processor_time[5]).setDefaultProcessPower(processor_power[5]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "melter", Melter::new, ContainerMelter::new, GuiMelter::new).setParticles("flame", "lava").setDefaultProcessTime(processor_time[6]).setDefaultProcessPower(processor_power[6]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "supercooler", Supercooler::new, ContainerSupercooler::new, GuiSupercooler::new).setParticles("smoke", "snowshovel").setDefaultProcessTime(processor_time[7]).setDefaultProcessPower(processor_power[7]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "electrolyzer", Electrolyzer::new, ContainerElectrolyzer::new, GuiElectrolyzer::new).setParticles("reddust", "splash").setDefaultProcessTime(processor_time[8]).setDefaultProcessPower(processor_power[8]).setLosesProgress(true).standardExtend(0, 12).setFluidInputSlots(standardSlot(50, 41)).setFluidOutputSlots(standardSlot(106, 31), standardSlot(126, 31), standardSlot(106, 51), standardSlot(126, 51)).setProgressBarGuiXYWHUV(68, 30, 37, 38, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "assembler", Assembler::new, ContainerAssembler::new, GuiAssembler::new).setParticles("crit", "smoke").setDefaultProcessTime(processor_time[9]).setDefaultProcessPower(processor_power[9]).standardExtend(0, 12).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31), standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "ingot_former", IngotFormer::new, ContainerIngotFormer::new, GuiIngotFormer::new).setParticles("smoke").setDefaultProcessTime(processor_time[10]).setDefaultProcessPower(processor_power[10]).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "pressurizer", Pressurizer::new, ContainerPressurizer::new, GuiPressurizer::new).setParticles("smoke").setDefaultProcessTime(processor_time[11]).setDefaultProcessPower(processor_power[11]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "chemical_reactor", ChemicalReactor::new, ContainerChemicalReactor::new, GuiChemicalReactor::new).setParticles("reddust").setDefaultProcessTime(processor_time[12]).setDefaultProcessPower(processor_power[12]).setLosesProgress(true).setFluidInputSlots(standardSlot(32, 35), standardSlot(52, 35)).setFluidOutputSlots(bigSlot(108, 31), bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 34, 37, 18, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "salt_mixer", SaltMixer::new, ContainerSaltMixer::new, GuiSaltMixer::new).setParticles("endRod", "reddust").setDefaultProcessTime(processor_time[13]).setDefaultProcessPower(processor_power[13]).setFluidInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 34, 37, 18, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "crystallizer", Crystallizer::new, ContainerCrystallizer::new, GuiCrystallizer::new).setParticles("depthsuspend").setDefaultProcessTime(processor_time[14]).setDefaultProcessPower(processor_power[14]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "enricher", Enricher::new, ContainerEnricher::new, GuiEnricher::new).setParticles("depthsuspend", "splash").setDefaultProcessTime(processor_time[15]).setDefaultProcessPower(processor_power[15]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "extractor", Extractor::new, ContainerExtractor::new, GuiExtractor::new).setParticles("depthsuspend", "reddust").setDefaultProcessTime(processor_time[16]).setDefaultProcessPower(processor_power[16]).setLosesProgress(true).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "centrifuge", Centrifuge::new, ContainerCentrifuge::new, GuiCentrifuge::new).setParticles("depthsuspend", "endRod").setDefaultProcessTime(processor_time[17]).setDefaultProcessPower(processor_power[17]).standardExtend(0, 12).setFluidInputSlots(standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "rock_crusher", RockCrusher::new, ContainerRockCrusher::new, GuiRockCrusher::new).setParticles("smoke").setDefaultProcessTime(processor_time[18]).setDefaultProcessPower(processor_power[18]).setItemInputSlots(standardSlot(38, 35)).setItemOutputSlots(standardSlot(94, 35), standardSlot(114, 35), standardSlot(134, 35)).setProgressBarGuiXYWHUV(56, 35, 37, 16, 176, 3));
		
		// putProcessorInfo(Global.MOD_ID, "radiation_scrubber", TileRadiationScrubber::new, NCTabs.radiation);
	}
	
	public static <TILE extends TileEntity> void putBlockSimpleTileInfo(String modId, String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		BLOCK_SIMPLE_TILE_INFO_MAP.put(name, new BlockSimpleTileInfo<>(name, tileSupplier, creativeTab));
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends BlockSimpleTileInfo<TILE>> INFO getBlockSimpleTileInfo(String name) {
		return (INFO) BLOCK_SIMPLE_TILE_INFO_MAP.get(name);
	}
	
	public static <TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, INFO, BUILDER>> void putProcessorInfo(BUILDER builder) {
		BLOCK_PROCESSOR_INFO_MAP.put(builder.name, builder.buildBlockInfo());
		GUI_PROCESSOR_INFO_MAP.put(builder.name, builder.buildContainerInfo());
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends ProcessorBlockInfo<TILE>> INFO getBlockProcessorInfo(String name) {
		return (INFO) BLOCK_PROCESSOR_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> INFO getContainerProcessorInfo(String name) {
		return (INFO) GUI_PROCESSOR_INFO_MAP.get(name);
	}
	
	public static int[] standardSlot(int x, int y) {
		return new int[] {x, y, 16, 16};
	}
	
	public static int[] bigSlot(int x, int y) {
		return new int[] {x, y, 24, 24};
	}
}
