package nc.integration.jei.wrapper;

import static nc.config.NCConfig.*;

import java.util.*;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEISimpleCategoryInfo;
import nc.network.tile.multiblock.*;
import nc.network.tile.processor.EnergyProcessorUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.tile.fission.*;
import nc.tile.fission.TileFissionIrradiator.FissionIrradiatorContainerInfo;
import nc.tile.fission.TileSaltFissionVessel.SaltFissionVesselContainerInfo;
import nc.tile.fission.TileSolidFissionCell.SolidFissionCellContainerInfo;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import nc.tile.radiation.TileRadiationScrubber;
import nc.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class JEIRecipeWrapperImpl {
	
	public static class JEIBasicProcessorRecipeWrapper<TILE extends TileBasicProcessor<TILE>, WRAPPER extends JEIBasicProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorRecipeWrapper<TILE, EnergyProcessorUpdatePacket, BasicProcessorContainerInfo<TILE>, WRAPPER> {
		
		public JEIBasicProcessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicUpgradableProcessorRecipeWrapper<TILE extends TileBasicUpgradableProcessor<TILE>, WRAPPER extends JEIBasicUpgradableProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIProcessorRecipeWrapper<TILE, EnergyProcessorUpdatePacket, BasicUpgradableProcessorContainerInfo<TILE>, WRAPPER> {
		
		public JEIBasicUpgradableProcessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicProcessorRecipeWrapperDyn extends JEIBasicProcessorRecipeWrapper<TileBasicProcessorDyn, JEIBasicProcessorRecipeWrapperDyn> {
		
		public JEIBasicProcessorRecipeWrapperDyn(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicUpgradableProcessorRecipeWrapperDyn extends JEIBasicUpgradableProcessorRecipeWrapper<TileBasicUpgradableProcessorDyn, JEIBasicUpgradableProcessorRecipeWrapperDyn> {
		
		public JEIBasicUpgradableProcessorRecipeWrapperDyn(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ManufactoryRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileManufactory, ManufactoryRecipeWrapper> {
		
		public ManufactoryRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class SeparatorRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileSeparator, SeparatorRecipeWrapper> {
		
		public SeparatorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class DecayHastenerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileDecayHastener, DecayHastenerRecipeWrapper> {
		
		public DecayHastenerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class FuelReprocessorRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileFuelReprocessor, FuelReprocessorRecipeWrapper> {
		
		public FuelReprocessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class AlloyFurnaceRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileAlloyFurnace, AlloyFurnaceRecipeWrapper> {
		
		public AlloyFurnaceRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class InfuserRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileInfuser, InfuserRecipeWrapper> {
		
		public InfuserRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class MelterRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileMelter, MelterRecipeWrapper> {
		
		public MelterRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class SupercoolerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileSupercooler, SupercoolerRecipeWrapper> {
		
		public SupercoolerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ElectrolyzerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileElectrolyzer, ElectrolyzerRecipeWrapper> {
		
		public ElectrolyzerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class AssemblerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileAssembler, AssemblerRecipeWrapper> {
		
		public AssemblerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class IngotFormerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileIngotFormer, IngotFormerRecipeWrapper> {
		
		public IngotFormerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class PressurizerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TilePressurizer, PressurizerRecipeWrapper> {
		
		public PressurizerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ChemicalReactorRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileChemicalReactor, ChemicalReactorRecipeWrapper> {
		
		public ChemicalReactorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class SaltMixerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileSaltMixer, SaltMixerRecipeWrapper> {
		
		public SaltMixerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class CrystallizerRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileCrystallizer, CrystallizerRecipeWrapper> {
		
		public CrystallizerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class EnricherRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileEnricher, EnricherRecipeWrapper> {
		
		public EnricherRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ExtractorRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileExtractor, ExtractorRecipeWrapper> {
		
		public ExtractorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class CentrifugeRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileCentrifuge, CentrifugeRecipeWrapper> {
		
		public CentrifugeRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class RockCrusherRecipeWrapper extends JEIBasicUpgradableProcessorRecipeWrapper<TileRockCrusher, RockCrusherRecipeWrapper> {
		
		public RockCrusherRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class RadiationScrubberRecipeWrapper extends JEIBasicProcessorRecipeWrapper<TileRadiationScrubber, RadiationScrubberRecipeWrapper> {
		
		public RadiationScrubberRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return MathHelper.ceil(getScrubberProcessTime() / 120);
		}
		
		protected double getScrubberProcessTime() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getScrubberProcessTime();
		}
		
		protected int getScrubberProcessPower() {
			if (recipe == null) {
				return 0;
			}
			return recipe.getScrubberProcessPower();
		}
		
		protected double getScrubberProcessEfficiency() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getScrubberProcessEfficiency();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + PROCESS_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getScrubberProcessTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + PROCESS_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getScrubberProcessPower(), 5, "RF/t"));
				tooltip.add(TextFormatting.RED + PROCESS_EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getScrubberProcessEfficiency(), 1));
			}
			
			return tooltip;
		}
		
		private static final String PROCESS_TIME = Lang.localize("jei.nuclearcraft.scrubber_process_time");
		private static final String PROCESS_POWER = Lang.localize("jei.nuclearcraft.scrubber_process_power");
		private static final String PROCESS_EFFICIENCY = Lang.localize("jei.nuclearcraft.scrubber_process_efficiency");
	}
	
	public static class CollectorRecipeWrapper extends JEISimpleRecipeWrapper<CollectorRecipeWrapper> {
		
		public CollectorRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<CollectorRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return machine_update_rate;
		}
		
		protected String getCollectorProductionRate() {
			if (recipe == null) {
				return null;
			}
			return recipe.getCollectorProductionRate();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + PRODUCTION_RATE + " " + TextFormatting.WHITE + getCollectorProductionRate());
			}
			
			return tooltip;
		}
		
		private static final String PRODUCTION_RATE = Lang.localize("jei.nuclearcraft.collector_production_rate");
	}
	
	public static class DecayGeneratorRecipeWrapper extends JEISimpleRecipeWrapper<DecayGeneratorRecipeWrapper> {
		
		public DecayGeneratorRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<DecayGeneratorRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getDecayGeneratorLifetime() / 20D);
		}
		
		protected double getDecayGeneratorLifetime() {
			if (recipe == null) {
				return 1200D;
			}
			return recipe.getDecayGeneratorLifetime();
		}
		
		protected double getDecayGeneratorPower() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getDecayGeneratorPower();
		}
		
		protected double getDecayGeneratorRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getDecayGeneratorRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + BLOCK_LIFETIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getDecayGeneratorLifetime(), 3, 1));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BLOCK_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getDecayGeneratorPower(), 5, "RF/t"));
				double radiation = getDecayGeneratorRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + BLOCK_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		private static final String BLOCK_LIFETIME = Lang.localize("jei.nuclearcraft.decay_gen_lifetime");
		private static final String BLOCK_POWER = Lang.localize("jei.nuclearcraft.decay_gen_power");
		private static final String BLOCK_RADIATION = Lang.localize("jei.nuclearcraft.decay_gen_radiation");
	}
	
	public static class FissionModeratorRecipeWrapper extends JEISimpleRecipeWrapper<FissionModeratorRecipeWrapper> {
		
		public FissionModeratorRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<FissionModeratorRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class FissionReflectorRecipeWrapper extends JEISimpleRecipeWrapper<FissionReflectorRecipeWrapper> {
		
		public FissionReflectorRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<FissionReflectorRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class FissionIrradiatorRecipeWrapper extends JEIProcessorRecipeWrapper<TileFissionIrradiator, FissionIrradiatorUpdatePacket, FissionIrradiatorContainerInfo, FissionIrradiatorRecipeWrapper> {
		
		public FissionIrradiatorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getIrradiatorFluxRequired() / 8000D);
		}
		
		protected int getIrradiatorFluxRequired() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getIrradiatorFluxRequired();
		}
		
		protected double getIrradiatorHeatPerFlux() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getIrradiatorHeatPerFlux();
		}
		
		protected double getIrradiatorProcessEfficiency() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getIrradiatorProcessEfficiency();
		}
		
		protected int getIrradiatorMinFluxPerTick() {
			if (recipe == null) {
				return 0;
			}
			return recipe.getIrradiatorMinFluxPerTick();
		}
		
		protected int getIrradiatorMaxFluxPerTick() {
			if (recipe == null) {
				return -1;
			}
			return recipe.getIrradiatorMaxFluxPerTick();
		}
		
		protected double getIrradiatorBaseProcessRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getIrradiatorBaseProcessRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.RED + FLUX_REQUIRED + " " + TextFormatting.WHITE + UnitHelper.prefix(getIrradiatorFluxRequired(), 5, "N"));
				double heatPerFlux = getIrradiatorHeatPerFlux();
				if (heatPerFlux > 0D) {
					tooltip.add(TextFormatting.YELLOW + HEAT_PER_FLUX + " " + TextFormatting.WHITE + UnitHelper.prefix(heatPerFlux, 5, "H/N"));
				}
				double efficiency = getIrradiatorProcessEfficiency();
				if (efficiency > 0D) {
					tooltip.add(TextFormatting.LIGHT_PURPLE + EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(efficiency, 1));
				}
				int minFluxPerTick = getIrradiatorMinFluxPerTick(), maxFluxPerTick = getIrradiatorMaxFluxPerTick();
				if (minFluxPerTick > 0 || maxFluxPerTick >= 0) {
					if (minFluxPerTick <= 0) {
						tooltip.add(TextFormatting.RED + VALID_FLUX_MAXIMUM + " " + TextFormatting.WHITE + minFluxPerTick + " N/t");
					}
					else if (maxFluxPerTick < 0) {
						tooltip.add(TextFormatting.RED + VALID_FLUX_MINIMUM + " " + TextFormatting.WHITE + maxFluxPerTick + " N/t");
					}
					else {
						tooltip.add(TextFormatting.RED + VALID_FLUX_RANGE + " " + TextFormatting.WHITE + minFluxPerTick + " - " + maxFluxPerTick + " N/t");
					}
				}
				double radiation = getIrradiatorBaseProcessRadiation() / RecipeStats.getFissionMaxModeratorLineFlux();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + RADIATION_PER_FLUX + " " + RadiationHelper.getRadiationTextColor(radiation) + UnitHelper.prefix(radiation, 3, "Rad/N"));
				}
			}
			
			return tooltip;
		}
		
		private static final String FLUX_REQUIRED = Lang.localize("jei.nuclearcraft.irradiator_flux_required");
		private static final String HEAT_PER_FLUX = Lang.localize("jei.nuclearcraft.irradiator_heat_per_flux");
		private static final String EFFICIENCY = Lang.localize("jei.nuclearcraft.irradiator_process_efficiency");
		private static final String VALID_FLUX_MINIMUM = Lang.localize("jei.nuclearcraft.irradiator_valid_flux_minimum");
		private static final String VALID_FLUX_MAXIMUM = Lang.localize("jei.nuclearcraft.irradiator_valid_flux_maximum");
		private static final String VALID_FLUX_RANGE = Lang.localize("jei.nuclearcraft.irradiator_valid_flux_range");
		private static final String RADIATION_PER_FLUX = Lang.localize("jei.nuclearcraft.radiation_per_flux");
	}
	
	public static class PebbleFissionRecipeWrapper extends JEISimpleRecipeWrapper<PebbleFissionRecipeWrapper> {
		
		public PebbleFissionRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<PebbleFissionRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFissionFuelTime() / 80D);
		}
		
		protected int getFissionFuelTime() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getFissionFuelTime();
		}
		
		protected int getFissionFuelHeat() {
			if (recipe == null) {
				return 0;
			}
			return recipe.getFissionFuelHeat();
		}
		
		protected double getFissionFuelEfficiency() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelEfficiency();
		}
		
		protected int getFissionFuelCriticality() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getFissionFuelCriticality();
		}
		
		protected double getFissionFuelDecayFactor() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelDecayFactor();
		}
		
		protected boolean getFissionFuelSelfPriming() {
			if (recipe == null) {
				return false;
			}
			return recipe.getFissionFuelSelfPriming();
		}
		
		protected double getFissionFuelRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getFissionFuelTime(), 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat(), 5, "H/t"));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getFissionFuelEfficiency(), 1));
				tooltip.add(TextFormatting.RED + FUEL_CRITICALITY + " " + TextFormatting.WHITE + getFissionFuelCriticality() + " N/t");
				if (fission_decay_mechanics) {
					tooltip.add(TextFormatting.GRAY + FUEL_DECAY_FACTOR + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getFissionFuelDecayFactor(), 1));
				}
				if (getFissionFuelSelfPriming()) {
					tooltip.add(TextFormatting.DARK_AQUA + FUEL_SELF_PRIMING);
				}
				double radiation = getFissionFuelRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localize("jei.nuclearcraft.pebble_fuel_time");
		private static final String FUEL_HEAT = Lang.localize("jei.nuclearcraft.pebble_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localize("jei.nuclearcraft.pebble_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localize("jei.nuclearcraft.pebble_fuel_criticality");
		private static final String FUEL_DECAY_FACTOR = Lang.localize("jei.nuclearcraft.pebble_fuel_decay_factor");
		private static final String FUEL_SELF_PRIMING = Lang.localize("jei.nuclearcraft.pebble_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localize("jei.nuclearcraft.pebble_fuel_radiation");
	}
	
	public static class SolidFissionRecipeWrapper extends JEIProcessorRecipeWrapper<TileSolidFissionCell, SolidFissionCellUpdatePacket, SolidFissionCellContainerInfo, SolidFissionRecipeWrapper> {
		
		public SolidFissionRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFissionFuelTime() / 80D);
		}
		
		protected int getFissionFuelTime() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getFissionFuelTime();
		}
		
		protected int getFissionFuelHeat() {
			if (recipe == null) {
				return 0;
			}
			return recipe.getFissionFuelHeat();
		}
		
		protected double getFissionFuelEfficiency() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelEfficiency();
		}
		
		protected int getFissionFuelCriticality() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getFissionFuelCriticality();
		}
		
		protected double getFissionFuelDecayFactor() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelDecayFactor();
		}
		
		protected boolean getFissionFuelSelfPriming() {
			if (recipe == null) {
				return false;
			}
			return recipe.getFissionFuelSelfPriming();
		}
		
		protected double getFissionFuelRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getFissionFuelTime(), 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat(), 5, "H/t"));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getFissionFuelEfficiency(), 1));
				tooltip.add(TextFormatting.RED + FUEL_CRITICALITY + " " + TextFormatting.WHITE + getFissionFuelCriticality() + " N/t");
				if (fission_decay_mechanics) {
					tooltip.add(TextFormatting.GRAY + FUEL_DECAY_FACTOR + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getFissionFuelDecayFactor(), 1));
				}
				if (getFissionFuelSelfPriming()) {
					tooltip.add(TextFormatting.DARK_AQUA + FUEL_SELF_PRIMING);
				}
				double radiation = getFissionFuelRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localize("jei.nuclearcraft.solid_fuel_time");
		private static final String FUEL_HEAT = Lang.localize("jei.nuclearcraft.solid_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localize("jei.nuclearcraft.solid_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localize("jei.nuclearcraft.solid_fuel_criticality");
		private static final String FUEL_DECAY_FACTOR = Lang.localize("jei.nuclearcraft.solid_fuel_decay_factor");
		private static final String FUEL_SELF_PRIMING = Lang.localize("jei.nuclearcraft.solid_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localize("jei.nuclearcraft.solid_fuel_radiation");
	}
	
	public static class FissionHeatingRecipeWrapper extends JEISimpleRecipeWrapper<FissionHeatingRecipeWrapper> {
		
		public FissionHeatingRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<FissionHeatingRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return getFissionHeatingHeatPerInputMB() / 4;
		}
		
		protected int getFissionHeatingHeatPerInputMB() {
			if (recipe == null) {
				return 64;
			}
			return recipe.getFissionHeatingHeatPerInputMB();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.YELLOW + HEAT_PER_MB + " " + TextFormatting.WHITE + getFissionHeatingHeatPerInputMB() + " H/mB");
			}
			
			return tooltip;
		}
		
		private static final String HEAT_PER_MB = Lang.localize("jei.nuclearcraft.fission_heating_heat_per_mb");
	}
	
	public static class SaltFissionRecipeWrapper extends JEIProcessorRecipeWrapper<TileSaltFissionVessel, SaltFissionVesselUpdatePacket, SaltFissionVesselContainerInfo, SaltFissionRecipeWrapper> {
		
		public SaltFissionRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (144D * getSaltFissionFuelTime() / 80D);
		}
		
		protected double getSaltFissionFuelTime() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getSaltFissionFuelTime();
		}
		
		protected int getFissionFuelHeat() {
			if (recipe == null) {
				return 0;
			}
			return recipe.getFissionFuelHeat();
		}
		
		protected double getFissionFuelEfficiency() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelEfficiency();
		}
		
		protected int getFissionFuelCriticality() {
			if (recipe == null) {
				return 1;
			}
			return recipe.getFissionFuelCriticality();
		}
		
		protected double getFissionFuelDecayFactor() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelDecayFactor();
		}
		
		protected boolean getFissionFuelSelfPriming() {
			if (recipe == null) {
				return false;
			}
			return recipe.getFissionFuelSelfPriming();
		}
		
		protected double getFissionFuelRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFissionFuelRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getSaltFissionFuelTime(), 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat(), 5, "H/t"));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getFissionFuelEfficiency(), 1));
				tooltip.add(TextFormatting.RED + FUEL_CRITICALITY + " " + TextFormatting.WHITE + getFissionFuelCriticality() + " N/t");
				if (fission_decay_mechanics) {
					tooltip.add(TextFormatting.GRAY + FUEL_DECAY_FACTOR + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getFissionFuelDecayFactor(), 1));
				}
				if (getFissionFuelSelfPriming()) {
					tooltip.add(TextFormatting.DARK_AQUA + FUEL_SELF_PRIMING);
				}
				double radiation = getFissionFuelRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localize("jei.nuclearcraft.salt_fuel_time");
		private static final String FUEL_HEAT = Lang.localize("jei.nuclearcraft.salt_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localize("jei.nuclearcraft.salt_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localize("jei.nuclearcraft.salt_fuel_criticality");
		private static final String FUEL_DECAY_FACTOR = Lang.localize("jei.nuclearcraft.salt_fuel_decay_factor");
		private static final String FUEL_SELF_PRIMING = Lang.localize("jei.nuclearcraft.salt_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localize("jei.nuclearcraft.salt_fuel_radiation");
	}
	
	public static class CoolantHeaterRecipeWrapper extends JEISimpleRecipeWrapper<CoolantHeaterRecipeWrapper> {
		
		public CoolantHeaterRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<CoolantHeaterRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 20;
		}
		
		protected int getCoolantHeaterCoolingRate() {
			if (recipe == null) {
				return 40;
			}
			return recipe.getCoolantHeaterCoolingRate();
		}
		
		protected String[] getCoolantHeaterJEIInfo() {
			if (recipe == null) {
				return null;
			}
			return recipe.getCoolantHeaterJEIInfo();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.BLUE + COOLING + " " + TextFormatting.WHITE + UnitHelper.prefix(getCoolantHeaterCoolingRate(), 5, "H/t"));
				if (getCoolantHeaterJEIInfo() != null) {
					for (String posInfo : getCoolantHeaterJEIInfo()) {
						tooltip.add(TextFormatting.AQUA + posInfo);
					}
				}
			}
			
			return tooltip;
		}
		
		private static final String COOLING = Lang.localize("jei.nuclearcraft.coolant_heater_rate");
	}
	
	public static class FissionEmergencyCoolingRecipeWrapper extends JEISimpleRecipeWrapper<FissionEmergencyCoolingRecipeWrapper> {
		
		public FissionEmergencyCoolingRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<FissionEmergencyCoolingRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (16D / getEmergencyCoolingHeatPerInputMB());
		}
		
		public double getEmergencyCoolingHeatPerInputMB() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getEmergencyCoolingHeatPerInputMB();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.BLUE + COOLING_PER_MB + " " + TextFormatting.WHITE + NCMath.decimalPlaces(getEmergencyCoolingHeatPerInputMB(), 2) + " H/mB");
			}
			
			return tooltip;
		}
		
		private static final String COOLING_PER_MB = Lang.localize("jei.nuclearcraft.fission_emergency_cooling_per_mb");
	}
	
	public static class HeatExchangerRecipeWrapper extends JEISimpleRecipeWrapper<HeatExchangerRecipeWrapper> {
		
		public HeatExchangerRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<HeatExchangerRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return recipe != null ? (int) (recipe.getHeatExchangerProcessTime() / 400D) : 40;
		}
		
		protected int getHeatExchangerProcessTime() {
			if (recipe == null) {
				return 16000;
			}
			return (int) recipe.getHeatExchangerProcessTime();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				boolean heating = recipe.getHeatExchangerIsHeating();
				int inputTemp = recipe.getHeatExchangerInputTemperature();
				int outputTemp = recipe.getHeatExchangerOutputTemperature();
				
				tooltip.add((heating ? TextFormatting.AQUA : TextFormatting.RED) + TEMPERATURE + TextFormatting.WHITE + " " + inputTemp + "K");
				tooltip.add((heating ? TextFormatting.RED : TextFormatting.AQUA) + TEMPERATURE + TextFormatting.WHITE + " " + outputTemp + "K");
				
				tooltip.add((heating ? TextFormatting.AQUA + COOLING_PROVIDED : TextFormatting.RED + HEATING_PROVIDED) + TextFormatting.WHITE + " " + Math.abs(inputTemp - outputTemp) + "/t");
				tooltip.add((heating ? TextFormatting.RED + HEATING_REQUIRED : TextFormatting.AQUA + COOLING_REQUIRED) + TextFormatting.WHITE + " " + getHeatExchangerProcessTime());
			}
			
			return tooltip;
		}
		
		private static final String TEMPERATURE = Lang.localize("jei.nuclearcraft.exchanger_fluid_temp");
		private static final String HEATING_PROVIDED = Lang.localize("jei.nuclearcraft.exchanger_heating_provided");
		private static final String COOLING_PROVIDED = Lang.localize("jei.nuclearcraft.exchanger_cooling_provided");
		private static final String HEATING_REQUIRED = Lang.localize("jei.nuclearcraft.exchanger_heating_required");
		private static final String COOLING_REQUIRED = Lang.localize("jei.nuclearcraft.exchanger_cooling_required");
	}
	
	public static class CondenserRecipeWrapper extends JEISimpleRecipeWrapper<CondenserRecipeWrapper> {
		
		public CondenserRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<CondenserRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getCondenserProcessTime() / 2D);
		}
		
		protected double getCondenserProcessTime() {
			if (recipe == null) {
				return 40D;
			}
			return recipe.getCondenserProcessTime();
		}
		
		protected int getCondenserCondensingTemperature() {
			if (recipe == null) {
				return 300;
			}
			return recipe.getCondenserCondensingTemperature();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.YELLOW + CONDENSING_TEMPERATURE + TextFormatting.WHITE + " " + getCondenserCondensingTemperature() + "K");
				tooltip.add(TextFormatting.BLUE + HEAT_REMOVAL_REQUIRED + TextFormatting.WHITE + " " + NCMath.sigFigs(getCondenserProcessTime(), 5));
			}
			
			return tooltip;
		}
		
		private static final String CONDENSING_TEMPERATURE = Lang.localize("jei.nuclearcraft.condenser_condensing_temp");
		private static final String HEAT_REMOVAL_REQUIRED = Lang.localize("jei.nuclearcraft.condenser_heat_removal_req");
	}
	
	public static class TurbineRecipeWrapper extends JEISimpleRecipeWrapper<TurbineRecipeWrapper> {
		
		public TurbineRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<TurbineRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 20;
		}
		
		protected double getTurbinePowerPerMB() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getTurbinePowerPerMB();
		}
		
		protected double getTurbineExpansionLevel() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getTurbineExpansionLevel();
		}
		
		protected double getTurbineSpinUpMultiplier() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getTurbineSpinUpMultiplier();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.LIGHT_PURPLE + ENERGY_DENSITY + " " + TextFormatting.WHITE + NCMath.decimalPlaces(getTurbinePowerPerMB(), 2) + " RF/mB");
				tooltip.add(TextFormatting.GRAY + EXPANSION + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getTurbineExpansionLevel(), 1));
				tooltip.add(TextFormatting.GREEN + SPIN_UP + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getTurbineSpinUpMultiplier(), 1));
			}
			
			return tooltip;
		}
		
		private static final String ENERGY_DENSITY = Lang.localize("jei.nuclearcraft.turbine_energy_density");
		private static final String EXPANSION = Lang.localize("jei.nuclearcraft.turbine_expansion");
		private static final String SPIN_UP = Lang.localize("jei.nuclearcraft.turbine_spin_up_multiplier");
	}
}
