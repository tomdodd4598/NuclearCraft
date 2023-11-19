package nc.handler;

import static nc.config.NCConfig.*;

import java.util.Arrays;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.block.tile.BlockSimpleTileInfo;
import nc.container.processor.ContainerNuclearFurnace;
import nc.container.processor.ContainerProcessorImpl.*;
import nc.gui.processor.GuiNuclearFurnace;
import nc.gui.processor.GuiProcessorImpl.*;
import nc.init.NCBlocks;
import nc.integration.jei.category.JEIRecipeCategoryImpl.JEIBasicUpgradableProcessorRecipeCategory;
import nc.integration.jei.category.info.JEICategoryInfoImpl.JEIBasicUpgradableProcessorCategoryInfo;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.integration.jei.wrapper.JEIProcessorRecipeWrapper;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl.ManufactoryRecipeWrapper;
import nc.tab.NCTabs;
import nc.tile.TileBin;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.generator.*;
import nc.tile.passive.TilePassive;
import nc.tile.processor.*;
import nc.tile.processor.TileNuclearFurnace.NuclearFurnaceContainerInfoBuilder;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.*;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilder;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilderImpl.*;
import nc.tile.radiation.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public class TileInfoHandler {
	
	private static final Object2ObjectMap<String, BlockSimpleTileInfo<?>> BLOCK_SIMPLE_TILE_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	private static final Object2ObjectMap<String, ProcessorBlockInfo<?>> PROCESSOR_BLOCK_INFO_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectMap<String, ProcessorContainerInfo<?, ?>> PROCESSOR_CONTAINER_INFO_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectMap<String, JEIProcessorCategoryInfo<?, ?, ?>> JEI_PROCESSOR_CATEGORY_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
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
		
		putProcessorInfo(new NuclearFurnaceContainerInfoBuilder(Global.MOD_ID, "nuclear_furnace", TileNuclearFurnace.class, TileNuclearFurnace::new, ContainerNuclearFurnace.class, ContainerNuclearFurnace::new, GuiNuclearFurnace.class, GuiNuclearFurnace::new));
		
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "manufactory", TileManufactory.class, TileManufactory::new, ContainerManufactory.class, ContainerManufactory::new, GuiManufactory.class, GuiManufactory::new).setParticles("crit", "reddust").setDefaultProcessTime(processor_time[0]).setDefaultProcessPower(processor_power[0]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[1]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "separator", TileSeparator.class, TileSeparator::new, ContainerSeparator.class, ContainerSeparator::new, GuiSeparator.class, GuiSeparator::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[1]).setDefaultProcessPower(processor_power[1]).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31), bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[2]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "decay_hastener", TileDecayHastener.class, TileDecayHastener::new, ContainerDecayHastener.class, ContainerDecayHastener::new, GuiDecayHastener.class, GuiDecayHastener::new).setParticles("reddust").setDefaultProcessTime(processor_time[2]).setDefaultProcessPower(processor_power[2]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[3]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "fuel_reprocessor", TileFuelReprocessor.class, TileFuelReprocessor::new, ContainerFuelReprocessor.class, ContainerFuelReprocessor::new, GuiFuelReprocessor.class, GuiFuelReprocessor::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[3]).setDefaultProcessPower(processor_power[3]).standardExtend(0, 12).setItemInputSlots(standardSlot(30, 41)).setItemOutputSlots(standardSlot(86, 31), standardSlot(106, 31), standardSlot(126, 31), standardSlot(146, 31), standardSlot(86, 51), standardSlot(106, 51), standardSlot(126, 51), standardSlot(146, 51)).setProgressBarGuiXYWHUV(48, 30, 37, 38, 176, 3).setJeiCategoryEnabled(register_processor[4]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "alloy_furnace", TileAlloyFurnace.class, TileAlloyFurnace::new, ContainerAlloyFurnace.class, ContainerAlloyFurnace::new, GuiAlloyFurnace.class, GuiAlloyFurnace::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[4]).setDefaultProcessPower(processor_power[4]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[5]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "infuser", TileInfuser.class, TileInfuser::new, ContainerInfuser.class, ContainerInfuser::new, GuiInfuser.class, GuiInfuser::new).setParticles("portal", "reddust").setDefaultProcessTime(processor_time[5]).setDefaultProcessPower(processor_power[5]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[6]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "melter", TileMelter.class, TileMelter::new, ContainerMelter.class, ContainerMelter::new, GuiMelter.class, GuiMelter::new).setParticles("flame", "lava").setDefaultProcessTime(processor_time[6]).setDefaultProcessPower(processor_power[6]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[7]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "supercooler", TileSupercooler.class, TileSupercooler::new, ContainerSupercooler.class, ContainerSupercooler::new, GuiSupercooler.class, GuiSupercooler::new).setParticles("smoke", "snowshovel").setDefaultProcessTime(processor_time[7]).setDefaultProcessPower(processor_power[7]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[8]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "electrolyzer", TileElectrolyzer.class, TileElectrolyzer::new, ContainerElectrolyzer.class, ContainerElectrolyzer::new, GuiElectrolyzer.class, GuiElectrolyzer::new).setParticles("reddust", "splash").setDefaultProcessTime(processor_time[8]).setDefaultProcessPower(processor_power[8]).setLosesProgress(true).standardExtend(0, 12).setFluidInputSlots(standardSlot(50, 41)).setFluidOutputSlots(standardSlot(106, 31), standardSlot(126, 31), standardSlot(106, 51), standardSlot(126, 51)).setProgressBarGuiXYWHUV(68, 30, 37, 38, 176, 3).setJeiCategoryEnabled(register_processor[9]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "assembler", TileAssembler.class, TileAssembler::new, ContainerAssembler.class, ContainerAssembler::new, GuiAssembler.class, GuiAssembler::new).setParticles("crit", "smoke").setDefaultProcessTime(processor_time[9]).setDefaultProcessPower(processor_power[9]).standardExtend(0, 12).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31), standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3).setJeiCategoryEnabled(register_processor[10]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "ingot_former", TileIngotFormer.class, TileIngotFormer::new, ContainerIngotFormer.class, ContainerIngotFormer::new, GuiIngotFormer.class, GuiIngotFormer::new).setParticles("smoke").setDefaultProcessTime(processor_time[10]).setDefaultProcessPower(processor_power[10]).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[11]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "pressurizer", TilePressurizer.class, TilePressurizer::new, ContainerPressurizer.class, ContainerPressurizer::new, GuiPressurizer.class, GuiPressurizer::new).setParticles("smoke").setDefaultProcessTime(processor_time[11]).setDefaultProcessPower(processor_power[11]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[12]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "chemical_reactor", TileChemicalReactor.class, TileChemicalReactor::new, ContainerChemicalReactor.class, ContainerChemicalReactor::new, GuiChemicalReactor.class, GuiChemicalReactor::new).setParticles("reddust").setDefaultProcessTime(processor_time[12]).setDefaultProcessPower(processor_power[12]).setLosesProgress(true).setFluidInputSlots(standardSlot(32, 35), standardSlot(52, 35)).setFluidOutputSlots(bigSlot(108, 31), bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[13]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "salt_mixer", TileSaltMixer.class, TileSaltMixer::new, ContainerSaltMixer.class, ContainerSaltMixer::new, GuiSaltMixer.class, GuiSaltMixer::new).setParticles("endRod", "reddust").setDefaultProcessTime(processor_time[13]).setDefaultProcessPower(processor_power[13]).setFluidInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[14]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "crystallizer", TileCrystallizer.class, TileCrystallizer::new, ContainerCrystallizer.class, ContainerCrystallizer::new, GuiCrystallizer.class, GuiCrystallizer::new).setParticles("depthsuspend").setDefaultProcessTime(processor_time[14]).setDefaultProcessPower(processor_power[14]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[15]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "enricher", TileEnricher.class, TileEnricher::new, ContainerEnricher.class, ContainerEnricher::new, GuiEnricher.class, GuiEnricher::new).setParticles("depthsuspend", "splash").setDefaultProcessTime(processor_time[15]).setDefaultProcessPower(processor_power[15]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[16]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "extractor", TileExtractor.class, TileExtractor::new, ContainerExtractor.class, ContainerExtractor::new, GuiExtractor.class, GuiExtractor::new).setParticles("depthsuspend", "reddust").setDefaultProcessTime(processor_time[16]).setDefaultProcessPower(processor_power[16]).setLosesProgress(true).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[17]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "centrifuge", TileCentrifuge.class, TileCentrifuge::new, ContainerCentrifuge.class, ContainerCentrifuge::new, GuiCentrifuge.class, GuiCentrifuge::new).setParticles("depthsuspend", "endRod").setDefaultProcessTime(processor_time[17]).setDefaultProcessPower(processor_power[17]).standardExtend(0, 12).setFluidInputSlots(standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3).setJeiCategoryEnabled(register_processor[18]));
		putProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "rock_crusher", TileRockCrusher.class, TileRockCrusher::new, ContainerRockCrusher.class, ContainerRockCrusher::new, GuiRockCrusher.class, GuiRockCrusher::new).setParticles("smoke").setDefaultProcessTime(processor_time[18]).setDefaultProcessPower(processor_power[18]).setItemInputSlots(standardSlot(38, 35)).setItemOutputSlots(standardSlot(94, 35), standardSlot(114, 35), standardSlot(134, 35)).setProgressBarGuiXYWHUV(56, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[19]));
		
		putProcessorInfo(new BasicProcessorContainerInfoBuilder<>(Global.MOD_ID, "radiation_scrubber", TileRadiationScrubber.class, TileRadiationScrubber::new, ContainerRadiationScrubber.class, ContainerRadiationScrubber::new, GuiRadiationScrubber.class, GuiRadiationScrubber::new).setCreativeTab(NCTabs.radiation).setParticles("reddust").setConsumesInputs(true).setItemInputSlots(standardSlot(32, 35)).setFluidInputSlots(standardSlot(52, 35)).setItemOutputSlots(bigSlot(108, 31)).setFluidOutputSlots(bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 35, 37, 16, 176, 3));
		
		putJEIProcessorCategoryInfo(new JEIBasicUpgradableProcessorCategoryInfo<>("manufactory", JEIBasicUpgradableProcessorRecipeCategory::new, ManufactoryRecipeWrapper.class, ManufactoryRecipeWrapper::new, Arrays.asList(NCBlocks.manufactory)));
	}
	
	public static <TILE extends TileEntity> void putBlockSimpleTileInfo(String modId, String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		BLOCK_SIMPLE_TILE_INFO_MAP.put(name, new BlockSimpleTileInfo<>(name, tileSupplier, creativeTab));
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends BlockSimpleTileInfo<TILE>> INFO getBlockSimpleTileInfo(String name) {
		return (INFO) BLOCK_SIMPLE_TILE_INFO_MAP.get(name);
	}
	
	public static <TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, INFO, BUILDER>> void putProcessorInfo(BUILDER builder) {
		PROCESSOR_BLOCK_INFO_MAP.put(builder.name, builder.buildBlockInfo());
		PROCESSOR_CONTAINER_INFO_MAP.put(builder.name, builder.buildContainerInfo());
	}
	
	public static <TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, INFO, WRAPPER>> void putJEIProcessorCategoryInfo(JEIProcessorCategoryInfo<TILE, INFO, WRAPPER> info) {
		JEI_PROCESSOR_CATEGORY_INFO_MAP.put(info.getName(), info);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends ProcessorBlockInfo<TILE>> INFO getProcessorBlockInfo(String name) {
		return (INFO) PROCESSOR_BLOCK_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> INFO getProcessorContainerInfo(String name) {
		return (INFO) PROCESSOR_CONTAINER_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, INFO, WRAPPER>> JEIProcessorCategoryInfo<TILE, INFO, WRAPPER> getJEIProcessorCategoryInfo(String name) {
		return (JEIProcessorCategoryInfo<TILE, INFO, WRAPPER>) JEI_PROCESSOR_CATEGORY_INFO_MAP.get(name);
	}
	
	public static int[] standardSlot(int x, int y) {
		return new int[] {x, y, 16, 16};
	}
	
	public static int[] bigSlot(int x, int y) {
		return new int[] {x, y, 24, 24};
	}
}
