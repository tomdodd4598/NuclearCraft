package nc.integration.jei;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.container.processor.ContainerAlloyFurnace;
import nc.container.processor.ContainerAssembler;
import nc.container.processor.ContainerCentrifuge;
import nc.container.processor.ContainerChemicalReactor;
import nc.container.processor.ContainerCrystallizer;
import nc.container.processor.ContainerDecayHastener;
import nc.container.processor.ContainerElectrolyzer;
import nc.container.processor.ContainerEnricher;
import nc.container.processor.ContainerExtractor;
import nc.container.processor.ContainerFuelReprocessor;
import nc.container.processor.ContainerInfuser;
import nc.container.processor.ContainerIngotFormer;
import nc.container.processor.ContainerManufactory;
import nc.container.processor.ContainerMelter;
import nc.container.processor.ContainerPressurizer;
import nc.container.processor.ContainerRockCrusher;
import nc.container.processor.ContainerSaltMixer;
import nc.container.processor.ContainerSeparator;
import nc.container.processor.ContainerSupercooler;
import nc.enumm.MetaEnums;
import nc.gui.processor.GuiAlloyFurnace;
import nc.gui.processor.GuiAssembler;
import nc.gui.processor.GuiCentrifuge;
import nc.gui.processor.GuiChemicalReactor;
import nc.gui.processor.GuiCrystallizer;
import nc.gui.processor.GuiDecayHastener;
import nc.gui.processor.GuiElectrolyzer;
import nc.gui.processor.GuiEnricher;
import nc.gui.processor.GuiExtractor;
import nc.gui.processor.GuiFuelReprocessor;
import nc.gui.processor.GuiInfuser;
import nc.gui.processor.GuiIngotFormer;
import nc.gui.processor.GuiManufactory;
import nc.gui.processor.GuiMelter;
import nc.gui.processor.GuiPressurizer;
import nc.gui.processor.GuiRockCrusher;
import nc.gui.processor.GuiSaltMixer;
import nc.gui.processor.GuiSeparator;
import nc.gui.processor.GuiSupercooler;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.integration.jei.generator.DecayGeneratorCategory;
import nc.integration.jei.multiblock.CondenserCategory;
import nc.integration.jei.multiblock.CoolantHeaterCategory;
import nc.integration.jei.multiblock.FissionHeatingCategory;
import nc.integration.jei.multiblock.FissionIrradiatorCategory;
import nc.integration.jei.multiblock.FissionModeratorCategory;
import nc.integration.jei.multiblock.FissionReflectorCategory;
import nc.integration.jei.multiblock.HeatExchangerCategory;
import nc.integration.jei.multiblock.PebbleFissionCategory;
import nc.integration.jei.multiblock.SaltFissionCategory;
import nc.integration.jei.multiblock.SolidFissionCategory;
import nc.integration.jei.multiblock.TurbineCategory;
import nc.integration.jei.other.CollectorCategory;
import nc.integration.jei.other.RadiationScrubberCategory;
import nc.integration.jei.processor.AlloyFurnaceCategory;
import nc.integration.jei.processor.AssemblerCategory;
import nc.integration.jei.processor.CentrifugeCategory;
import nc.integration.jei.processor.ChemicalReactorCategory;
import nc.integration.jei.processor.CrystallizerCategory;
import nc.integration.jei.processor.DecayHastenerCategory;
import nc.integration.jei.processor.ElectrolyzerCategory;
import nc.integration.jei.processor.EnricherCategory;
import nc.integration.jei.processor.ExtractorCategory;
import nc.integration.jei.processor.FuelReprocessorCategory;
import nc.integration.jei.processor.InfuserCategory;
import nc.integration.jei.processor.IngotFormerCategory;
import nc.integration.jei.processor.ManufactoryCategory;
import nc.integration.jei.processor.MelterCategory;
import nc.integration.jei.processor.PressurizerCategory;
import nc.integration.jei.processor.RockCrusherCategory;
import nc.integration.jei.processor.SaltMixerCategory;
import nc.integration.jei.processor.SeparatorCategory;
import nc.integration.jei.processor.SupercoolerCategory;
import nc.multiblock.container.ContainerFissionIrradiator;
import nc.multiblock.container.ContainerSaltFissionHeater;
import nc.multiblock.container.ContainerSaltFissionVessel;
import nc.multiblock.container.ContainerSolidFissionCell;
import nc.multiblock.gui.GuiFissionIrradiator;
import nc.multiblock.gui.GuiSaltFissionHeater;
import nc.multiblock.gui.GuiSaltFissionVessel;
import nc.multiblock.gui.GuiSolidFissionCell;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.ItemStackHelper;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class NCJEI implements IModPlugin {
	
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		for (IJEIHandler handler : JEIHandler.values()) {
			if (!handler.getEnabled()) continue;
			registry.addRecipes(handler.getJEIRecipes(guiHelper));
			JEICategoryAbstract category = handler.getCategory(guiHelper);
			registry.addRecipeCategories(category);
			registry.addRecipeHandlers(category);
			if (handler.getCrafters() != null) for (ItemStack crafter : handler.getCrafters()) {
				if (crafter != null) registry.addRecipeCatalyst(crafter, handler.getUUID());
			}
		}
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		
		if (NCConfig.register_processor[1]) {
			registry.addRecipeClickArea(GuiManufactory.class, 73, 34, 37, 18, JEIHandler.MANUFACTORY.getUUID());
			registry.addRecipeClickArea(GuiManufactory.SideConfig.class, 73, 34, 37, 18, JEIHandler.MANUFACTORY.getUUID());
		}
		if (NCConfig.register_processor[2]) {
			registry.addRecipeClickArea(GuiSeparator.class, 59, 34, 37, 18, JEIHandler.SEPARATOR.getUUID());
			registry.addRecipeClickArea(GuiSeparator.SideConfig.class, 59, 34, 37, 18, JEIHandler.SEPARATOR.getUUID());
		}
		if (NCConfig.register_processor[3]) {
			registry.addRecipeClickArea(GuiDecayHastener.class, 73, 34, 37, 18, JEIHandler.DECAY_HASTENER.getUUID());
			registry.addRecipeClickArea(GuiDecayHastener.SideConfig.class, 73, 34, 37, 18, JEIHandler.DECAY_HASTENER.getUUID());
		}
		if (NCConfig.register_processor[4]) {
			registry.addRecipeClickArea(GuiFuelReprocessor.class, 57, 30, 37, 38, JEIHandler.FUEL_REPROCESSOR.getUUID());
			registry.addRecipeClickArea(GuiFuelReprocessor.SideConfig.class, 57, 30, 37, 38, JEIHandler.FUEL_REPROCESSOR.getUUID());
		}
		if (NCConfig.register_processor[5]) {
			registry.addRecipeClickArea(GuiAlloyFurnace.class, 83, 34, 37, 18, JEIHandler.ALLOY_FURNACE.getUUID());
			registry.addRecipeClickArea(GuiAlloyFurnace.SideConfig.class, 83, 34, 37, 18, JEIHandler.ALLOY_FURNACE.getUUID());
		}
		if (NCConfig.register_processor[6]) {
			registry.addRecipeClickArea(GuiInfuser.class, 83, 34, 37, 18, JEIHandler.INFUSER.getUUID());
			registry.addRecipeClickArea(GuiInfuser.SideConfig.class, 83, 34, 37, 18, JEIHandler.INFUSER.getUUID());
		}
		if (NCConfig.register_processor[7]) {
			registry.addRecipeClickArea(GuiMelter.class, 73, 34, 37, 18, JEIHandler.MELTER.getUUID());
			registry.addRecipeClickArea(GuiMelter.SideConfig.class, 73, 34, 37, 18, JEIHandler.MELTER.getUUID());
		}
		if (NCConfig.register_processor[8]) {
			registry.addRecipeClickArea(GuiSupercooler.class, 73, 34, 37, 18, JEIHandler.SUPERCOOLER.getUUID());
			registry.addRecipeClickArea(GuiSupercooler.SideConfig.class, 73, 34, 37, 18, JEIHandler.SUPERCOOLER.getUUID());
		}
		if (NCConfig.register_processor[9]) {
			registry.addRecipeClickArea(GuiElectrolyzer.class, 67, 30, 37, 38, JEIHandler.ELECTROLYZER.getUUID());
			registry.addRecipeClickArea(GuiElectrolyzer.SideConfig.class, 67, 30, 37, 38, JEIHandler.ELECTROLYZER.getUUID());
		}
		if (NCConfig.register_processor[10]) {
			registry.addRecipeClickArea(GuiAssembler.class, 83, 30, 37, 38, JEIHandler.ASSEMBLER.getUUID());
			registry.addRecipeClickArea(GuiAssembler.SideConfig.class, 83, 30, 37, 38, JEIHandler.ASSEMBLER.getUUID());
		}
		if (NCConfig.register_processor[11]) {
			registry.addRecipeClickArea(GuiIngotFormer.class, 73, 34, 37, 18, JEIHandler.INGOT_FORMER.getUUID());
			registry.addRecipeClickArea(GuiIngotFormer.SideConfig.class, 73, 34, 37, 18, JEIHandler.INGOT_FORMER.getUUID());
		}
		if (NCConfig.register_processor[12]) {
			registry.addRecipeClickArea(GuiPressurizer.class, 73, 34, 37, 18, JEIHandler.PRESSURIZER.getUUID());
			registry.addRecipeClickArea(GuiPressurizer.SideConfig.class, 73, 34, 37, 18, JEIHandler.PRESSURIZER.getUUID());
		}
		if (NCConfig.register_processor[13]) {
			registry.addRecipeClickArea(GuiChemicalReactor.class, 69, 34, 37, 18, JEIHandler.CHEMICAL_REACTOR.getUUID());
			registry.addRecipeClickArea(GuiChemicalReactor.SideConfig.class, 69, 34, 37, 18, JEIHandler.CHEMICAL_REACTOR.getUUID());
		}
		if (NCConfig.register_processor[14]) {
			registry.addRecipeClickArea(GuiSaltMixer.class, 83, 34, 37, 18, JEIHandler.SALT_MIXER.getUUID());
			registry.addRecipeClickArea(GuiSaltMixer.SideConfig.class, 83, 34, 37, 18, JEIHandler.SALT_MIXER.getUUID());
		}
		if (NCConfig.register_processor[15]) {
			registry.addRecipeClickArea(GuiCrystallizer.class, 73, 34, 37, 18, JEIHandler.CRYSTALLIZER.getUUID());
			registry.addRecipeClickArea(GuiCrystallizer.SideConfig.class, 73, 34, 37, 18, JEIHandler.CRYSTALLIZER.getUUID());
		}
		if (NCConfig.register_processor[16]) {
			registry.addRecipeClickArea(GuiEnricher.class, 83, 34, 37, 18, JEIHandler.ENRICHER.getUUID());
			registry.addRecipeClickArea(GuiEnricher.SideConfig.class, 83, 34, 37, 18, JEIHandler.ENRICHER.getUUID());
		}
		if (NCConfig.register_processor[17]) {
			registry.addRecipeClickArea(GuiExtractor.class, 59, 34, 37, 18, JEIHandler.EXTRACTOR.getUUID());
			registry.addRecipeClickArea(GuiExtractor.SideConfig.class, 59, 34, 37, 18, JEIHandler.EXTRACTOR.getUUID());
		}
		if (NCConfig.register_processor[18]) {
			registry.addRecipeClickArea(GuiCentrifuge.class, 67, 30, 37, 38, JEIHandler.CENTRIFUGE.getUUID());
			registry.addRecipeClickArea(GuiCentrifuge.SideConfig.class, 67, 30, 37, 38, JEIHandler.CENTRIFUGE.getUUID());
		}
		if (NCConfig.register_processor[19]) {
			registry.addRecipeClickArea(GuiRockCrusher.class, 55, 34, 37, 18, JEIHandler.ROCK_CRUSHER.getUUID());
			registry.addRecipeClickArea(GuiRockCrusher.SideConfig.class, 55, 34, 37, 18, JEIHandler.ROCK_CRUSHER.getUUID());
		}
		
		registry.addRecipeClickArea(GuiFissionIrradiator.class, 73, 34, 37, 18, JEIHandler.FISSION_IRRADIATOR.getUUID());
		//registry.addRecipeClickArea(GuiPebbleFissionChamber.class, 73, 34, 37, 18, JEIHandler.PEBBLE_FISSION.getUUID());
		registry.addRecipeClickArea(GuiSolidFissionCell.class, 73, 34, 37, 18, JEIHandler.SOLID_FISSION.getUUID());
		registry.addRecipeClickArea(GuiSaltFissionVessel.class, 73, 34, 37, 18, JEIHandler.SALT_FISSION.getUUID());
		registry.addRecipeClickArea(GuiSaltFissionHeater.class, 73, 34, 37, 18, JEIHandler.COOLANT_HEATER.getUUID());
		
		recipeTransferRegistry.addRecipeTransferHandler(ContainerManufactory.class, JEIHandler.MANUFACTORY.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSeparator.class, JEIHandler.SEPARATOR.getUUID(), 0, 1, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerDecayHastener.class, JEIHandler.DECAY_HASTENER.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFuelReprocessor.class, JEIHandler.FUEL_REPROCESSOR.getUUID(), 0, 1, 7, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, JEIHandler.ALLOY_FURNACE.getUUID(), 0, 2, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerInfuser.class, JEIHandler.INFUSER.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerMelter.class, JEIHandler.MELTER.getUUID(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSupercooler.class, JEIHandler.SUPERCOOLER.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerElectrolyzer.class, JEIHandler.ELECTROLYZER.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerAssembler.class, JEIHandler.ASSEMBLER.getUUID(), 0, 4, 7, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerIngotFormer.class, JEIHandler.INGOT_FORMER.getUUID(), 0, 0, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerPressurizer.class, JEIHandler.PRESSURIZER.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerChemicalReactor.class, JEIHandler.CHEMICAL_REACTOR.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltMixer.class, JEIHandler.SALT_MIXER.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerCrystallizer.class, JEIHandler.CRYSTALLIZER.getUUID(), 0, 0, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerEnricher.class, JEIHandler.ENRICHER.getUUID(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerExtractor.class, JEIHandler.EXTRACTOR.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerCentrifuge.class, JEIHandler.CENTRIFUGE.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerRockCrusher.class, JEIHandler.ROCK_CRUSHER.getUUID(), 0, 1, 6, 36);
		
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFissionIrradiator.class, JEIHandler.FISSION_IRRADIATOR.getUUID(), 0, 1, 2, 36);
		//recipeTransferRegistry.addRecipeTransferHandler(ContainerPebbleFissionChamber.class, JEIHandler.PEBBLE_FISSION.getUUID(), 0, 1, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSolidFissionCell.class, JEIHandler.SOLID_FISSION.getUUID(), 0, 1, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltFissionVessel.class, JEIHandler.SALT_FISSION.getUUID(), 0, 0, 0, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltFissionHeater.class, JEIHandler.COOLANT_HEATER.getUUID(), 0, 0, 0, 36);
		
		for (int i = 0; i < MetaEnums.OreType.values().length; i++) {
			if (!NCConfig.ore_gen[i] && NCConfig.ore_hide_disabled) {
				blacklist(jeiHelpers, new ItemStack(NCBlocks.ore, 1, i), new ItemStack(NCBlocks.ingot_block, 1, i), new ItemStack(NCItems.ingot, 1, i), new ItemStack(NCItems.dust, 1, i));
			}
		}
		
		if (!ModCheck.openComputersLoaded()) {
			blacklist(jeiHelpers, NCBlocks.fission_computer_port);
			blacklist(jeiHelpers, NCBlocks.heat_exchanger_computer_port);
			blacklist(jeiHelpers, NCBlocks.turbine_computer_port);
		}
		
		if (!NCConfig.radiation_enabled_public) {
			blacklist(jeiHelpers, NCBlocks.radiation_scrubber);
			blacklist(jeiHelpers, NCBlocks.geiger_block, NCItems.geiger_counter);
			blacklistAll(jeiHelpers, MetaEnums.RadShieldingType.class, NCItems.rad_shielding);
			blacklist(jeiHelpers, NCItems.radiation_badge);
			blacklist(jeiHelpers, NCItems.radaway, NCItems.radaway_slow);
			blacklist(jeiHelpers, NCItems.rad_x);
			if (!ModCheck.ic2Loaded()) {
				blacklist(jeiHelpers, NCArmor.helm_hazmat, NCArmor.chest_hazmat, NCArmor.legs_hazmat, NCArmor.boots_hazmat);
			}
		}
		
		if (!ModCheck.ic2Loaded()) {
			blacklistAll(jeiHelpers, MetaEnums.IC2DepletedFuelType.class, NCItems.depleted_fuel_ic2);
		}
		
		blacklist(jeiHelpers, NCItems.foursmore);
		
		NCUtil.getLogger().info("JEI integration complete!");
	}
	
	private static void blacklist(IJeiHelpers jeiHelpers, Object... items) {
		for (Object item : items) {
			if (item == null) return;
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(ItemStackHelper.fixItemStack(item));
		}
	}
	
	/*private static <T extends Enum<T>> void blacklistAll(IJeiHelpers jeiHelpers, Class<T> enumm, Block block) {
		if (block == null) return;
		for (int i = 0; i < enumm.getEnumConstants().length; i++) blacklist(jeiHelpers, new ItemStack(block, 1, i));
	}*/
	
	private static <T extends Enum<T>> void blacklistAll(IJeiHelpers jeiHelpers, Class<T> enumm, Item item) {
		if (item == null) return;
		for (int i = 0; i < enumm.getEnumConstants().length; i++) blacklist(jeiHelpers, new ItemStack(item, 1, i));
	}
	
	public enum JEIHandler implements IJEIHandler {
		MANUFACTORY(NCRecipes.manufactory, NCBlocks.manufactory, "manufactory", JEIRecipeWrapper.Manufactory.class, 1),
		SEPARATOR(NCRecipes.separator, NCBlocks.separator, "separator", JEIRecipeWrapper.Separator.class, 2),
		DECAY_HASTENER(NCRecipes.decay_hastener, NCBlocks.decay_hastener, "decay_hastener", JEIRecipeWrapper.DecayHastener.class, 3),
		FUEL_REPROCESSOR(NCRecipes.fuel_reprocessor, NCBlocks.fuel_reprocessor, "fuel_reprocessor", JEIRecipeWrapper.FuelReprocessor.class, 4),
		ALLOY_FURNACE(NCRecipes.alloy_furnace, NCBlocks.alloy_furnace, "alloy_furnace", JEIRecipeWrapper.AlloyFurnace.class, 5),
		INFUSER(NCRecipes.infuser, NCBlocks.infuser, "infuser", JEIRecipeWrapper.Infuser.class, 6),
		MELTER(NCRecipes.melter, NCBlocks.melter, "melter", JEIRecipeWrapper.Melter.class, 7),
		SUPERCOOLER(NCRecipes.supercooler, NCBlocks.supercooler, "supercooler", JEIRecipeWrapper.Supercooler.class, 8),
		ELECTROLYZER(NCRecipes.electrolyzer, NCBlocks.electrolyzer, "electrolyzer", JEIRecipeWrapper.Electrolyzer.class, 9),
		ASSEMBLER(NCRecipes.assembler, NCBlocks.assembler, "assembler", JEIRecipeWrapper.Assembler.class, 10),
		INGOT_FORMER(NCRecipes.ingot_former, NCBlocks.ingot_former, "ingot_former", JEIRecipeWrapper.IngotFormer.class, 11),
		PRESSURIZER(NCRecipes.pressurizer, NCBlocks.pressurizer, "pressurizer", JEIRecipeWrapper.Pressurizer.class, 12),
		CHEMICAL_REACTOR(NCRecipes.chemical_reactor, NCBlocks.chemical_reactor, "chemical_reactor", JEIRecipeWrapper.ChemicalReactor.class, 13),
		SALT_MIXER(NCRecipes.salt_mixer, NCBlocks.salt_mixer, "salt_mixer", JEIRecipeWrapper.SaltMixer.class, 14),
		CRYSTALLIZER(NCRecipes.crystallizer, NCBlocks.crystallizer, "crystallizer", JEIRecipeWrapper.Crystallizer.class, 15),
		ENRICHER(NCRecipes.enricher, NCBlocks.enricher, "enricher", JEIRecipeWrapper.Enricher.class, 16),
		EXTRACTOR(NCRecipes.extractor, NCBlocks.extractor, "extractor", JEIRecipeWrapper.Extractor.class, 17),
		CENTRIFUGE(NCRecipes.centrifuge, NCBlocks.centrifuge, "centrifuge", JEIRecipeWrapper.Centrifuge.class, 18),
		ROCK_CRUSHER(NCRecipes.rock_crusher, NCBlocks.rock_crusher, "rock_crusher", JEIRecipeWrapper.RockCrusher.class, 19),
		COLLECTOR(NCRecipes.collector, registeredCollectors(), "collector", JEIRecipeWrapper.Collector.class),
		DECAY_GENERATOR(NCRecipes.decay_generator, NCBlocks.decay_generator, "decay_generator", JEIRecipeWrapper.DecayGenerator.class),
		FISSION_MODERATOR(NCRecipes.fission_moderator, NCBlocks.heavy_water_moderator, "fission_moderator", JEIRecipeWrapper.FissionModerator.class),
		FISSION_REFLECTOR(NCRecipes.fission_reflector, NCBlocks.fission_reflector, "fission_reflector", JEIRecipeWrapper.FissionReflector.class),
		FISSION_IRRADIATOR(NCRecipes.fission_irradiator, NCBlocks.fission_irradiator, "fission_irradiator", JEIRecipeWrapper.FissionIrradiator.class),
		PEBBLE_FISSION(NCRecipes.pebble_fission, Lists.newArrayList(), "pebble_fission", JEIRecipeWrapper.PebbleFission.class),
		SOLID_FISSION(NCRecipes.solid_fission, Lists.newArrayList(NCBlocks.solid_fission_controller, NCBlocks.solid_fission_cell), "solid_fission", JEIRecipeWrapper.SolidFission.class),
		FISSION_HEATING(NCRecipes.fission_heating, NCBlocks.fission_vent, "fission_heating", JEIRecipeWrapper.FissionHeating.class),
		SALT_FISSION(NCRecipes.salt_fission, Lists.newArrayList(NCBlocks.salt_fission_controller, NCBlocks.salt_fission_vessel), "salt_fission", JEIRecipeWrapper.SaltFission.class),
		//FUSION(NCRecipes.fusion, NCBlocks.fusion_core, "fusion_core", JEIRecipeWrapper.Fusion.class),
		COOLANT_HEATER(NCRecipes.coolant_heater, coolantHeaters(), "coolant_heater", JEIRecipeWrapper.CoolantHeater.class),
		HEAT_EXCHANGER(NCRecipes.heat_exchanger, Lists.newArrayList(NCBlocks.heat_exchanger_tube_copper, NCBlocks.heat_exchanger_tube_hard_carbon, NCBlocks.heat_exchanger_tube_thermoconducting), "heat_exchanger", JEIRecipeWrapper.HeatExchanger.class),
		CONDENSER(NCRecipes.condenser, Lists.newArrayList(NCBlocks.condenser_tube_copper, NCBlocks.condenser_tube_hard_carbon, NCBlocks.condenser_tube_thermoconducting), "condenser", JEIRecipeWrapper.Condenser.class),
		TURBINE(NCRecipes.turbine, NCBlocks.turbine_controller, "turbine", JEIRecipeWrapper.Turbine.class),
		RADIATION_SCRUBBER(NCRecipes.radiation_scrubber, NCBlocks.radiation_scrubber, "radiation_scrubber", JEIRecipeWrapper.RadiationScrubber.class);
		
		private ProcessorRecipeHandler recipeHandler;
		private Class<? extends JEIRecipeWrapperAbstract> recipeWrapper;
		private boolean enabled;
		private List<ItemStack> crafters;
		private String textureName;
		
		JEIHandler(ProcessorRecipeHandler recipeHandler, Object crafter, String textureName, Class<? extends JEIRecipeWrapperAbstract> recipeWrapper) {
			this(recipeHandler, crafter, textureName, recipeWrapper, -1);
		}
		
		JEIHandler(ProcessorRecipeHandler recipeHandler, List<?> crafters, String textureName, Class<? extends JEIRecipeWrapperAbstract> recipeWrapper) {
			this(recipeHandler, crafters, textureName, recipeWrapper, -1);
		}
		
		JEIHandler(ProcessorRecipeHandler recipeHandler, Object crafter, String textureName, Class<? extends JEIRecipeWrapperAbstract> recipeWrapper, int enabled) {
			this(recipeHandler, Lists.newArrayList(crafter), textureName, recipeWrapper, enabled);
		}
		
		JEIHandler(ProcessorRecipeHandler recipeHandler, List<?> crafters, String textureName, Class<? extends JEIRecipeWrapperAbstract> recipeWrapper, int enabled) {
			this.recipeHandler = recipeHandler;
			this.recipeWrapper = recipeWrapper;
			this.enabled = enabled < 0 ? true : NCConfig.register_processor[enabled];
			this.crafters = this.enabled ? fixStacks(crafters) : new ArrayList<>();
			this.textureName = textureName;
		}
		
		@Override
		public JEICategoryAbstract getCategory(IGuiHelper guiHelper) {
			switch (this) {
			case MANUFACTORY:
				return new ManufactoryCategory(guiHelper, this);
			case SEPARATOR:
				return new SeparatorCategory(guiHelper, this);
			case DECAY_HASTENER:
				return new DecayHastenerCategory(guiHelper, this);
			case FUEL_REPROCESSOR:
				return new FuelReprocessorCategory(guiHelper, this);
			case ALLOY_FURNACE:
				return new AlloyFurnaceCategory(guiHelper, this);
			case INFUSER:
				return new InfuserCategory(guiHelper, this);
			case MELTER:
				return new MelterCategory(guiHelper, this);
			case SUPERCOOLER:
				return new SupercoolerCategory(guiHelper, this);
			case ELECTROLYZER:
				return new ElectrolyzerCategory(guiHelper, this);
			case ASSEMBLER:
				return new AssemblerCategory(guiHelper, this);
			case INGOT_FORMER:
				return new IngotFormerCategory(guiHelper, this);
			case PRESSURIZER:
				return new PressurizerCategory(guiHelper, this);
			case CHEMICAL_REACTOR:
				return new ChemicalReactorCategory(guiHelper, this);
			case SALT_MIXER:
				return new SaltMixerCategory(guiHelper, this);
			case CRYSTALLIZER:
				return new CrystallizerCategory(guiHelper, this);
			case ENRICHER:
				return new EnricherCategory(guiHelper, this);
			case EXTRACTOR:
				return new ExtractorCategory(guiHelper, this);
			case CENTRIFUGE:
				return new CentrifugeCategory(guiHelper, this);
			case ROCK_CRUSHER:
				return new RockCrusherCategory(guiHelper, this);
			case COLLECTOR:
				return new CollectorCategory(guiHelper, this);
			case DECAY_GENERATOR:
				return new DecayGeneratorCategory(guiHelper, this);
			case FISSION_MODERATOR:
				return new FissionModeratorCategory(guiHelper, this);
			case FISSION_REFLECTOR:
				return new FissionReflectorCategory(guiHelper, this);
			case FISSION_IRRADIATOR:
				return new FissionIrradiatorCategory(guiHelper, this);
			case PEBBLE_FISSION:
				return new PebbleFissionCategory(guiHelper, this);
			case SOLID_FISSION:
				return new SolidFissionCategory(guiHelper, this);
			case FISSION_HEATING:
				return new FissionHeatingCategory(guiHelper, this);
			case SALT_FISSION:
				return new SaltFissionCategory(guiHelper, this);
			/*case FUSION:
				return new FusionCategory(guiHelper, this);*/
			case COOLANT_HEATER:
				return new CoolantHeaterCategory(guiHelper, this);
			case HEAT_EXCHANGER:
				return new HeatExchangerCategory(guiHelper, this);
			case TURBINE:
				return new TurbineCategory(guiHelper, this);
			case CONDENSER:
				return new CondenserCategory(guiHelper, this);
			case RADIATION_SCRUBBER:
				return new RadiationScrubberCategory(guiHelper, this);
			default:
				return null;
			}
		}
		
		@Override
		public ProcessorRecipeHandler getRecipeHandler() {
			return recipeHandler;
		}
		
		@Override
		public Class<? extends JEIRecipeWrapperAbstract> getJEIRecipeWrapper() {
			return recipeWrapper;
		}
		
		@Override
		public ArrayList<JEIRecipeWrapperAbstract> getJEIRecipes(IGuiHelper guiHelper) {
			return JEIMethods.getJEIRecipes(guiHelper, this, getRecipeHandler(), getJEIRecipeWrapper());
		}

		
		@Override
		public String getUUID() {
			return getRecipeHandler().getRecipeName();
		}
		
		@Override
		public boolean getEnabled() {
			return enabled;
		}
		
		@Override
		public List<ItemStack> getCrafters() {
			return crafters;
		}
		
		@Override
		public String getTextureName() {
			return textureName;
		}
	}
	
	private static List<ItemStack> fixStacks(List<?> list) {
		List<ItemStack> stacks = new ArrayList<>();
		for (Object obj : list) {
			stacks.add(ItemStackHelper.fixItemStack(obj));
		}
		return stacks;
	}
	
	private static List<Block> registeredCollectors() {
		List<Block> list = new ArrayList<>();
		if (NCConfig.register_passive[0]) {
			list.add(NCBlocks.cobblestone_generator);
			list.add(NCBlocks.cobblestone_generator_compact);
			list.add(NCBlocks.cobblestone_generator_dense);
		}
		if (NCConfig.register_passive[1]) {
			list.add(NCBlocks.water_source);
			list.add(NCBlocks.water_source_compact);
			list.add(NCBlocks.water_source_dense);
		}
		if (NCConfig.register_passive[2]) {
			list.add(NCBlocks.nitrogen_collector);
			list.add(NCBlocks.nitrogen_collector_compact);
			list.add(NCBlocks.nitrogen_collector_dense);
		}
		return list;
	}
	
	private static List<ItemStack> coolantHeaters() {
		List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < MetaEnums.CoolantHeaterType.values().length; i++) {
			list.add(new ItemStack(NCBlocks.salt_fission_heater, 1, i));
		}
		for (int i = 0; i < MetaEnums.CoolantHeaterType2.values().length; i++) {
			list.add(new ItemStack(NCBlocks.salt_fission_heater2, 1, i));
		}
		return list;
	}
}
