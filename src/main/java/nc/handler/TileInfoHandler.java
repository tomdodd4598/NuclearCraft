package nc.handler;

import static nc.config.NCConfig.*;
import static nc.tile.TileContainerInfoHelper.*;

import java.util.*;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.block.tile.BlockSimpleTileInfo;
import nc.container.multiblock.port.*;
import nc.container.processor.ContainerNuclearFurnace;
import nc.container.processor.ContainerProcessorImpl.*;
import nc.gui.multiblock.*;
import nc.gui.multiblock.port.*;
import nc.gui.processor.GuiNuclearFurnace;
import nc.gui.processor.GuiProcessorImpl.*;
import nc.init.NCBlocks;
import nc.integration.jei.NCJEI;
import nc.integration.jei.category.info.*;
import nc.integration.jei.category.info.builder.JEISimpleCategoryInfoBuilder;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl.*;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tab.NCTabs;
import nc.tile.*;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.fission.*;
import nc.tile.fission.TileFissionIrradiator.FissionIrradiatorContainerInfoBuilder;
import nc.tile.fission.TileSaltFissionHeater.SaltFissionHeaterContainerInfoBuilder;
import nc.tile.fission.TileSaltFissionVessel.SaltFissionVesselContainerInfoBuilder;
import nc.tile.fission.TileSolidFissionCell.SolidFissionCellContainerInfoBuilder;
import nc.tile.fission.port.TileFissionCellPort.FissionCellPortContainerInfo;
import nc.tile.fission.port.TileFissionHeaterPort.FissionHeaterPortContainerInfo;
import nc.tile.fission.port.TileFissionIrradiatorPort.FissionIrradiatorPortContainerInfo;
import nc.tile.fission.port.TileFissionVesselPort.FissionVesselPortContainerInfo;
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
	
	public static final Object2ObjectMap<String, BlockTileInfo<?>> BLOCK_TILE_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	public static final Object2ObjectMap<String, TileContainerInfo<?>> TILE_CONTAINER_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	public static final Object2ObjectMap<String, JEICategoryInfo<?, ?, ?>> JEI_CATEGORY_INFO_MAP = new Object2ObjectOpenHashMap<>();
	
	public static void init() {
		registerBlockSimpleTileInfo(Global.MOD_ID, "machine_interface", TileMachineInterface::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "decay_generator", TileDecayGenerator::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "bin", TileBin::new, NCTabs.machine);
		
		registerBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_basic", TileSolarPanel.Basic::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_advanced", TileSolarPanel.Advanced::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_du", TileSolarPanel.DU::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "solar_panel_elite", TileSolarPanel.Elite::new, NCTabs.machine);
		
		registerBlockSimpleTileInfo(Global.MOD_ID, "cobblestone_generator", TilePassive.CobblestoneGenerator::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "cobblestone_generator_compact", TilePassive.CobblestoneGeneratorCompact::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "cobblestone_generator_dense", TilePassive.CobblestoneGeneratorDense::new, NCTabs.machine);
		
		registerBlockSimpleTileInfo(Global.MOD_ID, "water_source", TilePassive.WaterSource::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "water_source_compact", TilePassive.WaterSourceCompact::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "water_source_dense", TilePassive.WaterSourceDense::new, NCTabs.machine);
		
		registerBlockSimpleTileInfo(Global.MOD_ID, "nitrogen_collector", TilePassive.NitrogenCollector::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "nitrogen_collector_compact", TilePassive.NitrogenCollectorCompact::new, NCTabs.machine);
		registerBlockSimpleTileInfo(Global.MOD_ID, "nitrogen_collector_dense", TilePassive.NitrogenCollectorDense::new, NCTabs.machine);
		
		registerBlockSimpleTileInfo(Global.MOD_ID, "geiger_block", TileGeigerCounter::new, NCTabs.radiation);
		
		registerProcessorInfo(new NuclearFurnaceContainerInfoBuilder(Global.MOD_ID, "nuclear_furnace", TileNuclearFurnace.class, TileNuclearFurnace::new, ContainerNuclearFurnace.class, ContainerNuclearFurnace::new, GuiNuclearFurnace.class, GuiNuclearFurnace::new));
		
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "manufactory", TileManufactory.class, TileManufactory::new, ContainerManufactory.class, ContainerManufactory::new, GuiManufactory.class, GuiManufactory::new).setParticles("crit", "reddust").setDefaultProcessTime(processor_time[0]).setDefaultProcessPower(processor_power[0]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[1]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "separator", TileSeparator.class, TileSeparator::new, ContainerSeparator.class, ContainerSeparator::new, GuiSeparator.class, GuiSeparator::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[1]).setDefaultProcessPower(processor_power[1]).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31), bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[2]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "decay_hastener", TileDecayHastener.class, TileDecayHastener::new, ContainerDecayHastener.class, ContainerDecayHastener::new, GuiDecayHastener.class, GuiDecayHastener::new).setParticles("reddust").setDefaultProcessTime(processor_time[2]).setDefaultProcessPower(processor_power[2]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[3]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "fuel_reprocessor", TileFuelReprocessor.class, TileFuelReprocessor::new, ContainerFuelReprocessor.class, ContainerFuelReprocessor::new, GuiFuelReprocessor.class, GuiFuelReprocessor::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[3]).setDefaultProcessPower(processor_power[3]).standardExtend(0, 12).setItemInputSlots(standardSlot(30, 41)).setItemOutputSlots(standardSlot(86, 31), standardSlot(106, 31), standardSlot(126, 31), standardSlot(146, 31), standardSlot(86, 51), standardSlot(106, 51), standardSlot(126, 51), standardSlot(146, 51)).setProgressBarGuiXYWHUV(48, 30, 37, 38, 176, 3).setJeiCategoryEnabled(register_processor[4]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "alloy_furnace", TileAlloyFurnace.class, TileAlloyFurnace::new, ContainerAlloyFurnace.class, ContainerAlloyFurnace::new, GuiAlloyFurnace.class, GuiAlloyFurnace::new).setParticles("reddust", "smoke").setDefaultProcessTime(processor_time[4]).setDefaultProcessPower(processor_power[4]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[5]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "infuser", TileInfuser.class, TileInfuser::new, ContainerInfuser.class, ContainerInfuser::new, GuiInfuser.class, GuiInfuser::new).setParticles("portal", "reddust").setDefaultProcessTime(processor_time[5]).setDefaultProcessPower(processor_power[5]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[6]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "melter", TileMelter.class, TileMelter::new, ContainerMelter.class, ContainerMelter::new, GuiMelter.class, GuiMelter::new).setParticles("flame", "lava").setDefaultProcessTime(processor_time[6]).setDefaultProcessPower(processor_power[6]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[7]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "supercooler", TileSupercooler.class, TileSupercooler::new, ContainerSupercooler.class, ContainerSupercooler::new, GuiSupercooler.class, GuiSupercooler::new).setParticles("smoke", "snowshovel").setDefaultProcessTime(processor_time[7]).setDefaultProcessPower(processor_power[7]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[8]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "electrolyzer", TileElectrolyzer.class, TileElectrolyzer::new, ContainerElectrolyzer.class, ContainerElectrolyzer::new, GuiElectrolyzer.class, GuiElectrolyzer::new).setParticles("reddust", "splash").setDefaultProcessTime(processor_time[8]).setDefaultProcessPower(processor_power[8]).setLosesProgress(true).standardExtend(0, 12).setFluidInputSlots(standardSlot(50, 41)).setFluidOutputSlots(standardSlot(106, 31), standardSlot(126, 31), standardSlot(106, 51), standardSlot(126, 51)).setProgressBarGuiXYWHUV(68, 30, 37, 38, 176, 3).setJeiCategoryEnabled(register_processor[9]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "assembler", TileAssembler.class, TileAssembler::new, ContainerAssembler.class, ContainerAssembler::new, GuiAssembler.class, GuiAssembler::new).setParticles("crit", "smoke").setDefaultProcessTime(processor_time[9]).setDefaultProcessPower(processor_power[9]).standardExtend(0, 12).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31), standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3).setJeiCategoryEnabled(register_processor[10]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "ingot_former", TileIngotFormer.class, TileIngotFormer::new, ContainerIngotFormer.class, ContainerIngotFormer::new, GuiIngotFormer.class, GuiIngotFormer::new).setParticles("smoke").setDefaultProcessTime(processor_time[10]).setDefaultProcessPower(processor_power[10]).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[11]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "pressurizer", TilePressurizer.class, TilePressurizer::new, ContainerPressurizer.class, ContainerPressurizer::new, GuiPressurizer.class, GuiPressurizer::new).setParticles("smoke").setDefaultProcessTime(processor_time[11]).setDefaultProcessPower(processor_power[11]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[12]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "chemical_reactor", TileChemicalReactor.class, TileChemicalReactor::new, ContainerChemicalReactor.class, ContainerChemicalReactor::new, GuiChemicalReactor.class, GuiChemicalReactor::new).setParticles("reddust").setDefaultProcessTime(processor_time[12]).setDefaultProcessPower(processor_power[12]).setLosesProgress(true).setFluidInputSlots(standardSlot(32, 35), standardSlot(52, 35)).setFluidOutputSlots(bigSlot(108, 31), bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[13]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "salt_mixer", TileSaltMixer.class, TileSaltMixer::new, ContainerSaltMixer.class, ContainerSaltMixer::new, GuiSaltMixer.class, GuiSaltMixer::new).setParticles("endRod", "reddust").setDefaultProcessTime(processor_time[13]).setDefaultProcessPower(processor_power[13]).setFluidInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[14]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "crystallizer", TileCrystallizer.class, TileCrystallizer::new, ContainerCrystallizer.class, ContainerCrystallizer::new, GuiCrystallizer.class, GuiCrystallizer::new).setParticles("depthsuspend").setDefaultProcessTime(processor_time[14]).setDefaultProcessPower(processor_power[14]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setJeiCategoryEnabled(register_processor[15]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "enricher", TileEnricher.class, TileEnricher::new, ContainerEnricher.class, ContainerEnricher::new, GuiEnricher.class, GuiEnricher::new).setParticles("depthsuspend", "splash").setDefaultProcessTime(processor_time[15]).setDefaultProcessPower(processor_power[15]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[16]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "extractor", TileExtractor.class, TileExtractor::new, ContainerExtractor.class, ContainerExtractor::new, GuiExtractor.class, GuiExtractor::new).setParticles("depthsuspend", "reddust").setDefaultProcessTime(processor_time[16]).setDefaultProcessPower(processor_power[16]).setLosesProgress(true).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3).setJeiCategoryEnabled(register_processor[17]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "centrifuge", TileCentrifuge.class, TileCentrifuge::new, ContainerCentrifuge.class, ContainerCentrifuge::new, GuiCentrifuge.class, GuiCentrifuge::new).setParticles("depthsuspend", "endRod").setDefaultProcessTime(processor_time[17]).setDefaultProcessPower(processor_power[17]).standardExtend(0, 12).setFluidInputSlots(standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3).setJeiCategoryEnabled(register_processor[18]));
		registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>(Global.MOD_ID, "rock_crusher", TileRockCrusher.class, TileRockCrusher::new, ContainerRockCrusher.class, ContainerRockCrusher::new, GuiRockCrusher.class, GuiRockCrusher::new).setParticles("smoke").setDefaultProcessTime(processor_time[18]).setDefaultProcessPower(processor_power[18]).setItemInputSlots(standardSlot(38, 35)).setItemOutputSlots(standardSlot(94, 35), standardSlot(114, 35), standardSlot(134, 35)).setProgressBarGuiXYWHUV(56, 35, 37, 16, 176, 3).setJeiCategoryEnabled(register_processor[19]));
		
		registerProcessorInfo(new BasicProcessorContainerInfoBuilder<>(Global.MOD_ID, "radiation_scrubber", TileRadiationScrubber.class, TileRadiationScrubber::new, ContainerRadiationScrubber.class, ContainerRadiationScrubber::new, GuiRadiationScrubber.class, GuiRadiationScrubber::new).setCreativeTab(NCTabs.radiation).setParticles("reddust").setConsumesInputs(true).setItemInputSlots(standardSlot(32, 35)).setFluidInputSlots(standardSlot(52, 35)).setItemOutputSlots(bigSlot(108, 31)).setFluidOutputSlots(bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 35, 37, 16, 176, 3));
		
		registerContainerInfo(new FissionIrradiatorContainerInfoBuilder(Global.MOD_ID, "fission_irradiator", TileFissionIrradiator.class, TileFissionIrradiator::new, ContainerFissionIrradiator.class, ContainerFissionIrradiator::new, GuiFissionIrradiator.class, GuiFissionIrradiator::new).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTexture().buildContainerInfo());
		registerContainerInfo(new SolidFissionCellContainerInfoBuilder(Global.MOD_ID, "solid_fission", TileSolidFissionCell.class, TileSolidFissionCell::new, ContainerSolidFissionCell.class, ContainerSolidFissionCell::new, GuiSolidFissionCell.class, GuiSolidFissionCell::new).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).buildContainerInfo());
		registerContainerInfo(new SaltFissionVesselContainerInfoBuilder(Global.MOD_ID, "salt_fission", TileSaltFissionVessel.class, TileSaltFissionVessel::new, ContainerSaltFissionVessel.class, ContainerSaltFissionVessel::new, GuiSaltFissionVessel.class, GuiSaltFissionVessel::new).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildContainerInfo());
		registerContainerInfo(new SaltFissionHeaterContainerInfoBuilder(Global.MOD_ID, "coolant_heater", TileSaltFissionHeater.class, TileSaltFissionHeater::new, ContainerSaltFissionHeater.class, ContainerSaltFissionHeater::new, GuiSaltFissionHeater.class, GuiSaltFissionHeater::new).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildContainerInfo());
		
		registerContainerInfo(new FissionIrradiatorPortContainerInfo(Global.MOD_ID, "fission_irradiator_port", ContainerFissionIrradiatorPort.class, ContainerFissionIrradiatorPort::new, GuiFissionIrradiatorPort.class, GuiFissionIrradiatorPort::new));
		registerContainerInfo(new FissionCellPortContainerInfo(Global.MOD_ID, "fission_cell_port", ContainerFissionCellPort.class, ContainerFissionCellPort::new, GuiFissionCellPort.class, GuiFissionCellPort::new));
		registerContainerInfo(new FissionVesselPortContainerInfo(Global.MOD_ID, "fission_vessel_port", ContainerFissionVesselPort.class, ContainerFissionVesselPort::new, GuiFissionVesselPort.class, GuiFissionVesselPort::new));
		registerContainerInfo(new FissionHeaterPortContainerInfo(Global.MOD_ID, "fission_heater_port", ContainerFissionHeaterPort.class, ContainerFissionHeaterPort::new, GuiFissionHeaterPort.class, GuiFissionHeaterPort::new));
		
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "collector", CollectorRecipeWrapper.class, CollectorRecipeWrapper::new, NCJEI.registeredCollectors()).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "decay_generator", DecayGeneratorRecipeWrapper.class, DecayGeneratorRecipeWrapper::new, Arrays.asList(NCBlocks.decay_generator)).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("manufactory", ManufactoryRecipeWrapper.class, ManufactoryRecipeWrapper::new, Arrays.asList(NCBlocks.manufactory)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("separator", SeparatorRecipeWrapper.class, SeparatorRecipeWrapper::new, Arrays.asList(NCBlocks.separator)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("decay_hastener", DecayHastenerRecipeWrapper.class, DecayHastenerRecipeWrapper::new, Arrays.asList(NCBlocks.decay_hastener)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("fuel_reprocessor", FuelReprocessorRecipeWrapper.class, FuelReprocessorRecipeWrapper::new, Arrays.asList(NCBlocks.fuel_reprocessor)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("alloy_furnace", AlloyFurnaceRecipeWrapper.class, AlloyFurnaceRecipeWrapper::new, Arrays.asList(NCBlocks.alloy_furnace)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("infuser", InfuserRecipeWrapper.class, InfuserRecipeWrapper::new, Arrays.asList(NCBlocks.infuser)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("melter", MelterRecipeWrapper.class, MelterRecipeWrapper::new, Arrays.asList(NCBlocks.melter)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("supercooler", SupercoolerRecipeWrapper.class, SupercoolerRecipeWrapper::new, Arrays.asList(NCBlocks.supercooler)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("electrolyzer", ElectrolyzerRecipeWrapper.class, ElectrolyzerRecipeWrapper::new, Arrays.asList(NCBlocks.electrolyzer)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("assembler", AssemblerRecipeWrapper.class, AssemblerRecipeWrapper::new, Arrays.asList(NCBlocks.assembler)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("ingot_former", IngotFormerRecipeWrapper.class, IngotFormerRecipeWrapper::new, Arrays.asList(NCBlocks.ingot_former)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("pressurizer", PressurizerRecipeWrapper.class, PressurizerRecipeWrapper::new, Arrays.asList(NCBlocks.pressurizer)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("chemical_reactor", ChemicalReactorRecipeWrapper.class, ChemicalReactorRecipeWrapper::new, Arrays.asList(NCBlocks.chemical_reactor)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("salt_mixer", SaltMixerRecipeWrapper.class, SaltMixerRecipeWrapper::new, Arrays.asList(NCBlocks.salt_mixer)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("crystallizer", CrystallizerRecipeWrapper.class, CrystallizerRecipeWrapper::new, Arrays.asList(NCBlocks.crystallizer)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("enricher", EnricherRecipeWrapper.class, EnricherRecipeWrapper::new, Arrays.asList(NCBlocks.enricher)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("extractor", ExtractorRecipeWrapper.class, ExtractorRecipeWrapper::new, Arrays.asList(NCBlocks.extractor)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("centrifuge", CentrifugeRecipeWrapper.class, CentrifugeRecipeWrapper::new, Arrays.asList(NCBlocks.centrifuge)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("rock_crusher", RockCrusherRecipeWrapper.class, RockCrusherRecipeWrapper::new, Arrays.asList(NCBlocks.rock_crusher)));
		
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("radiation_scrubber", RadiationScrubberRecipeWrapper.class, RadiationScrubberRecipeWrapper::new, Arrays.asList(NCBlocks.radiation_scrubber)));
		
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("fission_irradiator", FissionIrradiatorRecipeWrapper.class, FissionIrradiatorRecipeWrapper::new, Arrays.asList(NCBlocks.fission_irradiator)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("solid_fission", SolidFissionRecipeWrapper.class, SolidFissionRecipeWrapper::new, Arrays.asList(NCBlocks.solid_fission_controller, NCBlocks.solid_fission_cell)));
		registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("salt_fission", SaltFissionRecipeWrapper.class, SaltFissionRecipeWrapper::new, Arrays.asList(NCBlocks.salt_fission_controller, NCBlocks.salt_fission_vessel)));
		
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_moderator", FissionModeratorRecipeWrapper.class, FissionModeratorRecipeWrapper::new, Arrays.asList(NCBlocks.heavy_water_moderator)).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_reflector", FissionReflectorRecipeWrapper.class, FissionReflectorRecipeWrapper::new, Arrays.asList(NCBlocks.fission_reflector)).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "pebble_fission", PebbleFissionRecipeWrapper.class, PebbleFissionRecipeWrapper::new, Arrays.asList()).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_heating", FissionHeatingRecipeWrapper.class, FissionHeatingRecipeWrapper::new, Arrays.asList(NCBlocks.fission_vent)).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "coolant_heater", CoolantHeaterRecipeWrapper.class, CoolantHeaterRecipeWrapper::new, NCJEI.getCoolantHeaters()).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_emergency_cooling", FissionEmergencyCoolingRecipeWrapper.class, FissionEmergencyCoolingRecipeWrapper::new, Arrays.asList(NCBlocks.fission_vent)).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "heat_exchanger", HeatExchangerRecipeWrapper.class, HeatExchangerRecipeWrapper::new, Arrays.asList(NCBlocks.heat_exchanger_tube_copper, NCBlocks.heat_exchanger_tube_hard_carbon, NCBlocks.heat_exchanger_tube_thermoconducting)).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "condenser", CondenserRecipeWrapper.class, CondenserRecipeWrapper::new, Arrays.asList(NCBlocks.condenser_tube_copper, NCBlocks.condenser_tube_hard_carbon, NCBlocks.condenser_tube_thermoconducting)).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "turbine", TurbineRecipeWrapper.class, TurbineRecipeWrapper::new, Arrays.asList(NCBlocks.turbine_controller)).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildCategoryInfo());
		
	}
	
	public static <T> void register(Map<String, T> map, String name, T value) {
		T prev = map.put(name, value);
		if (prev != null) {
			throw new IllegalArgumentException("Registry name \"" + name + "\" already taken for type \"" + value.getClass().getSimpleName() + "\"!");
		}
	}
	
	public static <TILE extends TileEntity> void registerBlockSimpleTileInfo(String modId, String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		register(BLOCK_TILE_INFO_MAP, name, new BlockSimpleTileInfo<>(name, tileSupplier, creativeTab));
	}
	
	public static void registerContainerInfo(TileContainerInfo<?> info) {
		register(TILE_CONTAINER_INFO_MAP, info.name, info);
	}
	
	public static void registerProcessorInfo(ProcessorContainerInfoBuilder<?, ?, ?, ?> builder) {
		register(BLOCK_TILE_INFO_MAP, builder.name, builder.buildBlockInfo());
		register(TILE_CONTAINER_INFO_MAP, builder.name, builder.buildContainerInfo());
	}
	
	public static void registerJEICategoryInfo(JEICategoryInfo<?, ?, ?> info) {
		register(JEI_CATEGORY_INFO_MAP, info.getName(), info);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends BlockSimpleTileInfo<TILE>> INFO getBlockSimpleTileInfo(String name) {
		return (INFO) BLOCK_TILE_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends ProcessorBlockInfo<TILE>> INFO getProcessorBlockInfo(String name) {
		return (INFO) BLOCK_TILE_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends TileContainerInfo<TILE>> INFO getTileContainerInfo(String name) {
		return (INFO) TILE_CONTAINER_INFO_MAP.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> INFO getProcessorContainerInfo(String name) {
		return (INFO) TILE_CONTAINER_INFO_MAP.get(name);
	}
}
