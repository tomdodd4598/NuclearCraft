package nc.integration.jei;

import static nc.config.NCConfig.*;

import java.util.*;

import com.google.common.collect.Lists;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.*;
import nc.container.processor.*;
import nc.enumm.MetaEnums;
import nc.gui.processor.*;
import nc.init.*;
import nc.integration.jei.generator.DecayGeneratorCategory;
import nc.integration.jei.multiblock.*;
import nc.integration.jei.other.*;
import nc.integration.jei.processor.*;
import nc.multiblock.container.*;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.gui.*;
import nc.recipe.*;
import nc.recipe.ingredient.IItemIngredient;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.item.*;

@JEIPlugin
public class NCJEI implements IModPlugin {
	
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		for (IJEIHandler<?> handler : JEIHandler.values()) {
			if (handler.getEnabled()) {
				registry.addRecipes(handler.getJEIRecipes(guiHelper));
				JEIBasicCategory<?> category = handler.getCategory(guiHelper);
				registry.addRecipeCategories(category);
				registry.addRecipeHandlers(category);
				if (handler.getCrafters() != null) {
					for (ItemStack crafter : handler.getCrafters()) {
						if (crafter != null) {
							registry.addRecipeCatalyst(crafter, handler.getUid());
						}
					}
				}
			}
		}
		
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		
		if (register_processor[1]) {
			registry.addRecipeClickArea(GuiManufactory.class, 73, 34, 37, 18, JEIHandler.MANUFACTORY.getUid());
			registry.addRecipeClickArea(GuiManufactory.SideConfig.class, 73, 34, 37, 18, JEIHandler.MANUFACTORY.getUid());
		}
		if (register_processor[2]) {
			registry.addRecipeClickArea(GuiSeparator.class, 59, 34, 37, 18, JEIHandler.SEPARATOR.getUid());
			registry.addRecipeClickArea(GuiSeparator.SideConfig.class, 59, 34, 37, 18, JEIHandler.SEPARATOR.getUid());
		}
		if (register_processor[3]) {
			registry.addRecipeClickArea(GuiDecayHastener.class, 73, 34, 37, 18, JEIHandler.DECAY_HASTENER.getUid());
			registry.addRecipeClickArea(GuiDecayHastener.SideConfig.class, 73, 34, 37, 18, JEIHandler.DECAY_HASTENER.getUid());
		}
		if (register_processor[4]) {
			registry.addRecipeClickArea(GuiFuelReprocessor.class, 47, 30, 37, 38, JEIHandler.FUEL_REPROCESSOR.getUid());
			registry.addRecipeClickArea(GuiFuelReprocessor.SideConfig.class, 47, 30, 37, 38, JEIHandler.FUEL_REPROCESSOR.getUid());
		}
		if (register_processor[5]) {
			registry.addRecipeClickArea(GuiAlloyFurnace.class, 83, 34, 37, 18, JEIHandler.ALLOY_FURNACE.getUid());
			registry.addRecipeClickArea(GuiAlloyFurnace.SideConfig.class, 83, 34, 37, 18, JEIHandler.ALLOY_FURNACE.getUid());
		}
		if (register_processor[6]) {
			registry.addRecipeClickArea(GuiInfuser.class, 83, 34, 37, 18, JEIHandler.INFUSER.getUid());
			registry.addRecipeClickArea(GuiInfuser.SideConfig.class, 83, 34, 37, 18, JEIHandler.INFUSER.getUid());
		}
		if (register_processor[7]) {
			registry.addRecipeClickArea(GuiMelter.class, 73, 34, 37, 18, JEIHandler.MELTER.getUid());
			registry.addRecipeClickArea(GuiMelter.SideConfig.class, 73, 34, 37, 18, JEIHandler.MELTER.getUid());
		}
		if (register_processor[8]) {
			registry.addRecipeClickArea(GuiSupercooler.class, 73, 34, 37, 18, JEIHandler.SUPERCOOLER.getUid());
			registry.addRecipeClickArea(GuiSupercooler.SideConfig.class, 73, 34, 37, 18, JEIHandler.SUPERCOOLER.getUid());
		}
		if (register_processor[9]) {
			registry.addRecipeClickArea(GuiElectrolyzer.class, 67, 30, 37, 38, JEIHandler.ELECTROLYZER.getUid());
			registry.addRecipeClickArea(GuiElectrolyzer.SideConfig.class, 67, 30, 37, 38, JEIHandler.ELECTROLYZER.getUid());
		}
		if (register_processor[10]) {
			registry.addRecipeClickArea(GuiAssembler.class, 83, 30, 37, 38, JEIHandler.ASSEMBLER.getUid());
			registry.addRecipeClickArea(GuiAssembler.SideConfig.class, 83, 30, 37, 38, JEIHandler.ASSEMBLER.getUid());
		}
		if (register_processor[11]) {
			registry.addRecipeClickArea(GuiIngotFormer.class, 73, 34, 37, 18, JEIHandler.INGOT_FORMER.getUid());
			registry.addRecipeClickArea(GuiIngotFormer.SideConfig.class, 73, 34, 37, 18, JEIHandler.INGOT_FORMER.getUid());
		}
		if (register_processor[12]) {
			registry.addRecipeClickArea(GuiPressurizer.class, 73, 34, 37, 18, JEIHandler.PRESSURIZER.getUid());
			registry.addRecipeClickArea(GuiPressurizer.SideConfig.class, 73, 34, 37, 18, JEIHandler.PRESSURIZER.getUid());
		}
		if (register_processor[13]) {
			registry.addRecipeClickArea(GuiChemicalReactor.class, 69, 34, 37, 18, JEIHandler.CHEMICAL_REACTOR.getUid());
			registry.addRecipeClickArea(GuiChemicalReactor.SideConfig.class, 69, 34, 37, 18, JEIHandler.CHEMICAL_REACTOR.getUid());
		}
		if (register_processor[14]) {
			registry.addRecipeClickArea(GuiSaltMixer.class, 83, 34, 37, 18, JEIHandler.SALT_MIXER.getUid());
			registry.addRecipeClickArea(GuiSaltMixer.SideConfig.class, 83, 34, 37, 18, JEIHandler.SALT_MIXER.getUid());
		}
		if (register_processor[15]) {
			registry.addRecipeClickArea(GuiCrystallizer.class, 73, 34, 37, 18, JEIHandler.CRYSTALLIZER.getUid());
			registry.addRecipeClickArea(GuiCrystallizer.SideConfig.class, 73, 34, 37, 18, JEIHandler.CRYSTALLIZER.getUid());
		}
		if (register_processor[16]) {
			registry.addRecipeClickArea(GuiEnricher.class, 83, 34, 37, 18, JEIHandler.ENRICHER.getUid());
			registry.addRecipeClickArea(GuiEnricher.SideConfig.class, 83, 34, 37, 18, JEIHandler.ENRICHER.getUid());
		}
		if (register_processor[17]) {
			registry.addRecipeClickArea(GuiExtractor.class, 59, 34, 37, 18, JEIHandler.EXTRACTOR.getUid());
			registry.addRecipeClickArea(GuiExtractor.SideConfig.class, 59, 34, 37, 18, JEIHandler.EXTRACTOR.getUid());
		}
		if (register_processor[18]) {
			registry.addRecipeClickArea(GuiCentrifuge.class, 57, 30, 37, 38, JEIHandler.CENTRIFUGE.getUid());
			registry.addRecipeClickArea(GuiCentrifuge.SideConfig.class, 57, 30, 37, 38, JEIHandler.CENTRIFUGE.getUid());
		}
		if (register_processor[19]) {
			registry.addRecipeClickArea(GuiRockCrusher.class, 55, 34, 37, 18, JEIHandler.ROCK_CRUSHER.getUid());
			registry.addRecipeClickArea(GuiRockCrusher.SideConfig.class, 55, 34, 37, 18, JEIHandler.ROCK_CRUSHER.getUid());
		}
		
		registry.addRecipeClickArea(GuiFissionIrradiator.class, 73, 34, 37, 18, JEIHandler.FISSION_IRRADIATOR.getUid());
		// registry.addRecipeClickArea(GuiPebbleFissionChamber.class, 73, 34, 37, 18, JEIHandler.PEBBLE_FISSION.getUid());
		registry.addRecipeClickArea(GuiSolidFissionCell.class, 73, 34, 37, 18, JEIHandler.SOLID_FISSION.getUid());
		registry.addRecipeClickArea(GuiSaltFissionVessel.class, 73, 34, 37, 18, JEIHandler.SALT_FISSION.getUid());
		registry.addRecipeClickArea(GuiSaltFissionHeater.class, 73, 34, 37, 18, JEIHandler.COOLANT_HEATER.getUid());
		
		recipeTransferRegistry.addRecipeTransferHandler(ContainerManufactory.class, JEIHandler.MANUFACTORY.getUid(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSeparator.class, JEIHandler.SEPARATOR.getUid(), 0, 1, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerDecayHastener.class, JEIHandler.DECAY_HASTENER.getUid(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFuelReprocessor.class, JEIHandler.FUEL_REPROCESSOR.getUid(), 0, 1, 9, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, JEIHandler.ALLOY_FURNACE.getUid(), 0, 2, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerInfuser.class, JEIHandler.INFUSER.getUid(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerMelter.class, JEIHandler.MELTER.getUid(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSupercooler.class, JEIHandler.SUPERCOOLER.getUid(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerElectrolyzer.class, JEIHandler.ELECTROLYZER.getUid(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerAssembler.class, JEIHandler.ASSEMBLER.getUid(), 0, 4, 7, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerIngotFormer.class, JEIHandler.INGOT_FORMER.getUid(), 0, 0, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerPressurizer.class, JEIHandler.PRESSURIZER.getUid(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerChemicalReactor.class, JEIHandler.CHEMICAL_REACTOR.getUid(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltMixer.class, JEIHandler.SALT_MIXER.getUid(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerCrystallizer.class, JEIHandler.CRYSTALLIZER.getUid(), 0, 0, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerEnricher.class, JEIHandler.ENRICHER.getUid(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerExtractor.class, JEIHandler.EXTRACTOR.getUid(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerCentrifuge.class, JEIHandler.CENTRIFUGE.getUid(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerRockCrusher.class, JEIHandler.ROCK_CRUSHER.getUid(), 0, 1, 6, 36);
		
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFissionIrradiator.class, JEIHandler.FISSION_IRRADIATOR.getUid(), 0, 1, 2, 36);
		// recipeTransferRegistry.addRecipeTransferHandler(ContainerPebbleFissionChamber.class, JEIHandler.PEBBLE_FISSION.getUid(), 0, 1, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSolidFissionCell.class, JEIHandler.SOLID_FISSION.getUid(), 0, 1, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltFissionVessel.class, JEIHandler.SALT_FISSION.getUid(), 0, 0, 0, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltFissionHeater.class, JEIHandler.COOLANT_HEATER.getUid(), 0, 0, 0, 36);
		
		for (int i = 0; i < MetaEnums.OreType.values().length; ++i) {
			if (!ore_gen[i] && ore_hide_disabled) {
				blacklist(jeiHelpers, new ItemStack(NCBlocks.ore, 1, i), new ItemStack(NCBlocks.ingot_block, 1, i), new ItemStack(NCItems.ingot, 1, i), new ItemStack(NCItems.dust, 1, i));
			}
		}
		
		if (!ModCheck.openComputersLoaded()) {
			blacklist(jeiHelpers, NCBlocks.fission_computer_port);
			blacklist(jeiHelpers, NCBlocks.heat_exchanger_computer_port);
			blacklist(jeiHelpers, NCBlocks.turbine_computer_port);
		}
		
		if (!radiation_enabled_public) {
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
			if (item == null) {
				return;
			}
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(StackHelper.fixItemStack(item));
		}
	}
	
	private static <T extends Enum<T>> void blacklistAll(IJeiHelpers jeiHelpers, Class<T> enumm, Item item) {
		if (item == null) {
			return;
		}
		for (int i = 0; i < enumm.getEnumConstants().length; ++i) {
			blacklist(jeiHelpers, new ItemStack(item, 1, i));
		}
	}
	
	public enum JEIHandler implements IJEIHandler {
		
		MANUFACTORY(NCRecipes.manufactory, NCBlocks.manufactory, "manufactory", JEIRecipe.Manufactory.class, 1),
		SEPARATOR(NCRecipes.separator, NCBlocks.separator, "separator", JEIRecipe.Separator.class, 2),
		DECAY_HASTENER(NCRecipes.decay_hastener, NCBlocks.decay_hastener, "decay_hastener", JEIRecipe.DecayHastener.class, 3),
		FUEL_REPROCESSOR(NCRecipes.fuel_reprocessor, NCBlocks.fuel_reprocessor, "fuel_reprocessor", JEIRecipe.FuelReprocessor.class, 4),
		ALLOY_FURNACE(NCRecipes.alloy_furnace, NCBlocks.alloy_furnace, "alloy_furnace", JEIRecipe.AlloyFurnace.class, 5),
		INFUSER(NCRecipes.infuser, NCBlocks.infuser, "infuser", JEIRecipe.Infuser.class, 6),
		MELTER(NCRecipes.melter, NCBlocks.melter, "melter", JEIRecipe.Melter.class, 7),
		SUPERCOOLER(NCRecipes.supercooler, NCBlocks.supercooler, "supercooler", JEIRecipe.Supercooler.class, 8),
		ELECTROLYZER(NCRecipes.electrolyzer, NCBlocks.electrolyzer, "electrolyzer", JEIRecipe.Electrolyzer.class, 9),
		ASSEMBLER(NCRecipes.assembler, NCBlocks.assembler, "assembler", JEIRecipe.Assembler.class, 10),
		INGOT_FORMER(NCRecipes.ingot_former, NCBlocks.ingot_former, "ingot_former", JEIRecipe.IngotFormer.class, 11),
		PRESSURIZER(NCRecipes.pressurizer, NCBlocks.pressurizer, "pressurizer", JEIRecipe.Pressurizer.class, 12),
		CHEMICAL_REACTOR(NCRecipes.chemical_reactor, NCBlocks.chemical_reactor, "chemical_reactor", JEIRecipe.ChemicalReactor.class, 13),
		SALT_MIXER(NCRecipes.salt_mixer, NCBlocks.salt_mixer, "salt_mixer", JEIRecipe.SaltMixer.class, 14),
		CRYSTALLIZER(NCRecipes.crystallizer, NCBlocks.crystallizer, "crystallizer", JEIRecipe.Crystallizer.class, 15),
		ENRICHER(NCRecipes.enricher, NCBlocks.enricher, "enricher", JEIRecipe.Enricher.class, 16),
		EXTRACTOR(NCRecipes.extractor, NCBlocks.extractor, "extractor", JEIRecipe.Extractor.class, 17),
		CENTRIFUGE(NCRecipes.centrifuge, NCBlocks.centrifuge, "centrifuge", JEIRecipe.Centrifuge.class, 18),
		ROCK_CRUSHER(NCRecipes.rock_crusher, NCBlocks.rock_crusher, "rock_crusher", JEIRecipe.RockCrusher.class, 19),
		COLLECTOR(NCRecipes.collector, registeredCollectors(), "collector", JEIRecipe.Collector.class),
		DECAY_GENERATOR(NCRecipes.decay_generator, NCBlocks.decay_generator, "decay_generator", JEIRecipe.DecayGenerator.class),
		FISSION_MODERATOR(NCRecipes.fission_moderator, NCBlocks.heavy_water_moderator, "fission_moderator", JEIRecipe.FissionModerator.class),
		FISSION_REFLECTOR(NCRecipes.fission_reflector, NCBlocks.fission_reflector, "fission_reflector", JEIRecipe.FissionReflector.class),
		FISSION_IRRADIATOR(NCRecipes.fission_irradiator, NCBlocks.fission_irradiator, "fission_irradiator_jei", JEIRecipe.FissionIrradiator.class),
		PEBBLE_FISSION(NCRecipes.pebble_fission, Lists.newArrayList(), "pebble_fission", JEIRecipe.PebbleFission.class),
		SOLID_FISSION(NCRecipes.solid_fission, Lists.newArrayList(NCBlocks.solid_fission_controller, NCBlocks.solid_fission_cell), "solid_fission", JEIRecipe.SolidFission.class),
		FISSION_HEATING(NCRecipes.fission_heating, NCBlocks.fission_vent, "fission_heating", JEIRecipe.FissionHeating.class),
		SALT_FISSION(NCRecipes.salt_fission, Lists.newArrayList(NCBlocks.salt_fission_controller, NCBlocks.salt_fission_vessel), "salt_fission", JEIRecipe.SaltFission.class),
		COOLANT_HEATER(NCRecipes.coolant_heater, getCoolantHeaters(), "coolant_heater", JEIRecipe.CoolantHeater.class),
		FISSION_EMERGENCY_COOLING(NCRecipes.fission_emergency_cooling, NCBlocks.fission_vent, "fission_emergency_cooling", JEIRecipe.FissionEmergencyCooling.class),
		HEAT_EXCHANGER(NCRecipes.heat_exchanger, Lists.newArrayList(NCBlocks.heat_exchanger_tube_copper, NCBlocks.heat_exchanger_tube_hard_carbon, NCBlocks.heat_exchanger_tube_thermoconducting), "heat_exchanger", JEIRecipe.HeatExchanger.class),
		CONDENSER(NCRecipes.condenser, Lists.newArrayList(NCBlocks.condenser_tube_copper, NCBlocks.condenser_tube_hard_carbon, NCBlocks.condenser_tube_thermoconducting), "condenser", JEIRecipe.Condenser.class),
		TURBINE(NCRecipes.turbine, NCBlocks.turbine_controller, "turbine", JEIRecipe.Turbine.class),
		RADIATION_SCRUBBER(NCRecipes.radiation_scrubber, NCBlocks.radiation_scrubber, "radiation_scrubber", JEIRecipe.RadiationScrubber.class);
		
		private final BasicRecipeHandler recipeHandler;
		private final Class<? extends JEIBasicRecipe> recipeWrapper;
		private final boolean enabled;
		private final List<ItemStack> crafters;
		private final String textureName;
		
		JEIHandler(BasicRecipeHandler recipeHandler, Object crafter, String textureName, Class<? extends JEIBasicRecipe> recipeWrapper) {
			this(recipeHandler, crafter, textureName, recipeWrapper, -1);
		}
		
		JEIHandler(BasicRecipeHandler recipeHandler, List<?> crafters, String textureName, Class<? extends JEIBasicRecipe> recipeWrapper) {
			this(recipeHandler, crafters, textureName, recipeWrapper, -1);
		}
		
		JEIHandler(BasicRecipeHandler recipeHandler, Object crafter, String textureName, Class<? extends JEIBasicRecipe> recipeWrapper, int enabled) {
			this(recipeHandler, Lists.newArrayList(crafter), textureName, recipeWrapper, enabled);
		}
		
		JEIHandler(BasicRecipeHandler recipeHandler, List<?> crafters, String textureName, Class<? extends JEIBasicRecipe> recipeWrapper, int enabled) {
			this.recipeHandler = recipeHandler;
			this.recipeWrapper = recipeWrapper;
			this.enabled = enabled < 0 ? true : register_processor[enabled];
			this.crafters = this.enabled ? fixStacks(crafters) : new ArrayList<>();
			this.textureName = textureName;
		}
		
		@Override
		public JEIBasicCategory<?> getCategory(IGuiHelper guiHelper) {
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
				case COOLANT_HEATER:
					return new CoolantHeaterCategory(guiHelper, this);
				case FISSION_EMERGENCY_COOLING:
					return new FissionEmergencyCoolingCategory(guiHelper, this);
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
		public BasicRecipeHandler getRecipeHandler() {
			return recipeHandler;
		}
		
		@Override
		public Class<? extends JEIBasicRecipe> getRecipeWrapperClass() {
			return recipeWrapper;
		}
		
		@Override
		public List<? extends JEIBasicRecipe> getJEIRecipes(IGuiHelper guiHelper) {
			return JEIHelper.getJEIRecipes(guiHelper, this, getRecipeHandler(), getRecipeWrapperClass());
		}
		
		@Override
		public String getUid() {
			return Global.MOD_ID + "_" + getRecipeHandler().getName();
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
	
	protected static List<ItemStack> fixStacks(List<?> list) {
		List<ItemStack> stacks = new ArrayList<>();
		for (Object obj : list) {
			stacks.add(StackHelper.fixItemStack(obj));
		}
		return stacks;
	}
	
	protected static List<Block> registeredCollectors() {
		List<Block> list = new ArrayList<>();
		if (register_passive[0]) {
			list.add(NCBlocks.cobblestone_generator);
			list.add(NCBlocks.cobblestone_generator_compact);
			list.add(NCBlocks.cobblestone_generator_dense);
		}
		if (register_passive[1]) {
			list.add(NCBlocks.water_source);
			list.add(NCBlocks.water_source_compact);
			list.add(NCBlocks.water_source_dense);
		}
		if (register_passive[2]) {
			list.add(NCBlocks.nitrogen_collector);
			list.add(NCBlocks.nitrogen_collector_compact);
			list.add(NCBlocks.nitrogen_collector_dense);
		}
		return list;
	}
	
	protected static List<ItemStack> getCoolantHeaters() {
		List<ItemStack> list = new ArrayList<>();
		for (BasicRecipe recipe : FissionPlacement.recipe_handler.getRecipeList()) {
			if (recipe.getPlacementRuleID().endsWith("_heater")) for (IItemIngredient ingredient : recipe.getItemIngredients()) {
				for (ItemStack stack : ingredient.getInputStackList()) {
					list.add(stack);
				}
			}
		}
		return list;
	}
	
	public static interface IJEIHandler<WRAPPER extends JEIBasicRecipe<WRAPPER>> {
		
		public JEIBasicCategory<?> getCategory(IGuiHelper guiHelper);
		
		public BasicRecipeHandler getRecipeHandler();
		
		public Class<WRAPPER> getRecipeWrapperClass();
		
		public List<WRAPPER> getJEIRecipes(IGuiHelper guiHelper);
		
		public String getUid();
		
		public boolean getEnabled();
		
		public List<ItemStack> getCrafters();
		
		public String getTextureName();
	}
}
