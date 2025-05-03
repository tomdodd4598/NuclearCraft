package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import nc.gui.element.GuiFluidRenderer;
import nc.integration.jei.category.info.JEISimpleCategoryInfo;
import nc.network.tile.multiblock.*;
import nc.network.tile.processor.*;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.multiblock.ElectrolyzerElectrolyteRecipeHandler;
import nc.tile.fission.*;
import nc.tile.processor.*;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl;
import nc.tile.radiation.TileRadiationScrubber;
import nc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static nc.config.NCConfig.*;

public class JEIRecipeWrapperImpl {
	
	public static class JEIBasicProcessorRecipeWrapper<TILE extends TileEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket, WRAPPER extends JEIBasicProcessorRecipeWrapper<TILE, PACKET, WRAPPER>> extends JEIProcessorRecipeWrapper<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>, WRAPPER> {
		
		public JEIBasicProcessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicUpgradableProcessorRecipeWrapper<TILE extends TileEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket, WRAPPER extends JEIBasicUpgradableProcessorRecipeWrapper<TILE, PACKET, WRAPPER>> extends JEIProcessorRecipeWrapper<TILE, PACKET, ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, PACKET>, WRAPPER> {
		
		public JEIBasicUpgradableProcessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicEnergyProcessorRecipeWrapper<TILE extends TileBasicEnergyProcessor<TILE>, WRAPPER extends JEIBasicEnergyProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIBasicProcessorRecipeWrapper<TILE, EnergyProcessorUpdatePacket, WRAPPER> {
		
		public JEIBasicEnergyProcessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicUpgradableEnergyProcessorRecipeWrapper<TILE extends TileBasicUpgradableEnergyProcessor<TILE>, WRAPPER extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TILE, WRAPPER>> extends JEIBasicUpgradableProcessorRecipeWrapper<TILE, EnergyProcessorUpdatePacket, WRAPPER> {
		
		public JEIBasicUpgradableEnergyProcessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicEnergyProcessorRecipeWrapperDyn extends JEIBasicEnergyProcessorRecipeWrapper<TileBasicEnergyProcessorDyn, JEIBasicEnergyProcessorRecipeWrapperDyn> {
		
		public JEIBasicEnergyProcessorRecipeWrapperDyn(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class JEIBasicUpgradableEnergyProcessorRecipeWrapperDyn extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileBasicUpgradableEnergyProcessorDyn, JEIBasicUpgradableEnergyProcessorRecipeWrapperDyn> {
		
		public JEIBasicUpgradableEnergyProcessorRecipeWrapperDyn(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ManufactoryRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileManufactory, ManufactoryRecipeWrapper> {
		
		public ManufactoryRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class SeparatorRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileSeparator, SeparatorRecipeWrapper> {
		
		public SeparatorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class DecayHastenerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileDecayHastener, DecayHastenerRecipeWrapper> {
		
		public DecayHastenerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class FuelReprocessorRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileFuelReprocessor, FuelReprocessorRecipeWrapper> {
		
		public FuelReprocessorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class AlloyFurnaceRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileAlloyFurnace, AlloyFurnaceRecipeWrapper> {
		
		public AlloyFurnaceRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class InfuserRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileInfuser, InfuserRecipeWrapper> {
		
		public InfuserRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class MelterRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileMelter, MelterRecipeWrapper> {
		
		public MelterRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class SupercoolerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileSupercooler, SupercoolerRecipeWrapper> {
		
		public SupercoolerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ElectrolyzerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileElectrolyzer, ElectrolyzerRecipeWrapper> {
		
		public ElectrolyzerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class AssemblerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileAssembler, AssemblerRecipeWrapper> {
		
		public AssemblerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class IngotFormerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileIngotFormer, IngotFormerRecipeWrapper> {
		
		public IngotFormerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class PressurizerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TilePressurizer, PressurizerRecipeWrapper> {
		
		public PressurizerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ChemicalReactorRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileChemicalReactor, ChemicalReactorRecipeWrapper> {
		
		public ChemicalReactorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class SaltMixerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileSaltMixer, SaltMixerRecipeWrapper> {
		
		public SaltMixerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class CrystallizerRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileCrystallizer, CrystallizerRecipeWrapper> {
		
		public CrystallizerRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class EnricherRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileEnricher, EnricherRecipeWrapper> {
		
		public EnricherRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ExtractorRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileExtractor, ExtractorRecipeWrapper> {
		
		public ExtractorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class CentrifugeRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileCentrifuge, CentrifugeRecipeWrapper> {
		
		public CentrifugeRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class RockCrusherRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileRockCrusher, RockCrusherRecipeWrapper> {
		
		public RockCrusherRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class ElectricFurnaceRecipeWrapper extends JEIBasicUpgradableEnergyProcessorRecipeWrapper<TileElectricFurnace, ElectricFurnaceRecipeWrapper> {
		
		public ElectricFurnaceRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
	}
	
	public static class RadiationScrubberRecipeWrapper extends JEIBasicEnergyProcessorRecipeWrapper<TileRadiationScrubber, RadiationScrubberRecipeWrapper> {
		
		public RadiationScrubberRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(getScrubberProcessTime() / 120D);
		}
		
		protected double getScrubberProcessTime() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getScrubberProcessTime();
		}
		
		protected long getScrubberProcessPower() {
			if (recipe == null) {
				return 0L;
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
			return NCMath.toInt(getDecayGeneratorLifetime() / 20D);
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
	
	public static class MachineDiaphragmRecipeWrapper extends JEISimpleRecipeWrapper<MachineDiaphragmRecipeWrapper> {
		
		public MachineDiaphragmRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<MachineDiaphragmRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class MachineSieveAssemblyRecipeWrapper extends JEISimpleRecipeWrapper<MachineSieveAssemblyRecipeWrapper> {
		
		public MachineSieveAssemblyRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<MachineSieveAssemblyRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class MultiblockElectrolyzerRecipeWrapper extends JEISimpleRecipeWrapper<MultiblockElectrolyzerRecipeWrapper> {
		
		protected final int electrolyteX;
		protected final int electrolyteY;
		protected final int electrolyteW;
		protected final int electrolyteH;
		
		public MultiblockElectrolyzerRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<MultiblockElectrolyzerRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
			electrolyteX = 64 - categoryInfo.jeiBackgroundX;
			electrolyteY = 41 - categoryInfo.jeiBackgroundY;
			electrolyteW = 16;
			electrolyteH = 16;
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(getBaseProcessTime());
		}
		
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return machine_electrolyzer_time;
			}
			return recipe.getBaseProcessTime(machine_electrolyzer_time);
		}
		
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return machine_electrolyzer_power;
			}
			return recipe.getBaseProcessPower(machine_electrolyzer_power);
		}
		
		protected double getBaseProcessRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getBaseProcessRadiation();
		}
		
		protected List<Pair<Fluid, Double>> getElectrolyteList() {
			if (recipe != null) {
				ElectrolyzerElectrolyteRecipeHandler handler = recipe.getElectrolyzerElectrolyteRecipeHandler();
				if (handler != null) {
					return handler.electrolyteList;
				}
			}
			return new ArrayList<>();
		}
		
		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
			
			List<Pair<Fluid, Double>> electrolyteList = getElectrolyteList();
			if (!electrolyteList.isEmpty()) {
				Pair<Fluid, Double> electrolyte = getEnumerationElement(electrolyteList, 1000L);
				GlStateManager.pushAttrib();
				GuiFluidRenderer.renderGuiTank(new FluidStack(electrolyte.getLeft(), 1), 1, 1, electrolyteX, electrolyteY, 50D, electrolyteW, electrolyteH, 255);
				GlStateManager.popAttrib();
			}
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			int x1 = electrolyteX - 1, y1 = electrolyteY, x2 = electrolyteX + electrolyteW, y2 = electrolyteY + electrolyteH + 1;
			if (mouseX > x1 && mouseY > y1 && mouseX < x2 && mouseY < y2) {
				List<Pair<Fluid, Double>> electrolyteList = getElectrolyteList();
				if (electrolyteList.isEmpty()) {
					tooltip.add(TextFormatting.AQUA + ELECTROLYTE + " " + TextFormatting.WHITE + "null");
				}
				else {
					Pair<Fluid, Double> electrolyte = getEnumerationElement(electrolyteList, 1000L);
					tooltip.add(TextFormatting.AQUA + ELECTROLYTE + " " + TextFormatting.WHITE + Lang.localize(electrolyte.getLeft().getUnlocalizedName()));
					tooltip.add(TextFormatting.LIGHT_PURPLE + ELECTROLYTE_EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(electrolyte.getRight(), 1));
				}
			}
			else if (mouseX != x1 && mouseY != y1 && mouseX != x2 && mouseY != y2 && showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + BASE_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getBaseProcessTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BASE_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getBaseProcessPower(), 5, "RF/t"));
				double radiation = getBaseProcessRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + BASE_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		public static final String ELECTROLYTE = Lang.localize("jei.nuclearcraft.electrolyte");
		public static final String ELECTROLYTE_EFFICIENCY = Lang.localize("jei.nuclearcraft.electrolyte_efficiency");
		public static final String BASE_TIME = Lang.localize("jei.nuclearcraft.base_process_time");
		public static final String BASE_POWER = Lang.localize("jei.nuclearcraft.base_process_power");
		public static final String BASE_RADIATION = Lang.localize("jei.nuclearcraft.base_process_radiation");
	}
	
	public static class ElectrolyzerCathodeRecipeWrapper extends JEISimpleRecipeWrapper<ElectrolyzerCathodeRecipeWrapper> {
		
		public ElectrolyzerCathodeRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<ElectrolyzerCathodeRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class ElectrolyzerAnodeRecipeWrapper extends JEISimpleRecipeWrapper<ElectrolyzerAnodeRecipeWrapper> {
		
		public ElectrolyzerAnodeRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<ElectrolyzerAnodeRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class MultiblockDistillerRecipeWrapper extends JEISimpleRecipeWrapper<MultiblockDistillerRecipeWrapper> {
		
		public MultiblockDistillerRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<MultiblockDistillerRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(getBaseProcessTime() / 4D);
		}
		
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return machine_distiller_time;
			}
			return recipe.getBaseProcessTime(machine_distiller_time);
		}
		
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return machine_distiller_power;
			}
			return recipe.getBaseProcessPower(machine_distiller_power);
		}
		
		protected long getDistillerSieveTrayCount() {
			if (recipe == null) {
				return 0L;
			}
			return recipe.getDistillerSieveTrayCount();
		}
		
		protected double getBaseProcessRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getBaseProcessRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + BASE_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getBaseProcessTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BASE_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getBaseProcessPower(), 5, "RF/t"));
				tooltip.add(TextFormatting.GRAY + SIEVE_TRAY_COUNT + " " + TextFormatting.WHITE + getDistillerSieveTrayCount());
				double radiation = getBaseProcessRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + BASE_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		public static final String BASE_TIME = Lang.localize("jei.nuclearcraft.base_process_time");
		public static final String BASE_POWER = Lang.localize("jei.nuclearcraft.base_process_power");
		public static final String SIEVE_TRAY_COUNT = Lang.localize("jei.nuclearcraft.distiller_sieve_tray_count");
		public static final String BASE_RADIATION = Lang.localize("jei.nuclearcraft.base_process_radiation");
	}
	
	public static class MultiblockInfiltratorRecipeWrapper extends JEISimpleRecipeWrapper<MultiblockInfiltratorRecipeWrapper> {
		
		public MultiblockInfiltratorRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<MultiblockInfiltratorRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(getBaseProcessTime() / 4D);
		}
		
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return machine_infiltrator_time;
			}
			return recipe.getBaseProcessTime(machine_infiltrator_time);
		}
		
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return machine_infiltrator_power;
			}
			return recipe.getBaseProcessPower(machine_infiltrator_power);
		}
		
		protected double getInfiltratorHeatingFactor() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getInfiltratorHeatingFactor();
		}
		
		protected double getBaseProcessRadiation() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getBaseProcessRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (showTooltip(mouseX, mouseY)) {
				tooltip.add(TextFormatting.GREEN + BASE_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getBaseProcessTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BASE_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getBaseProcessPower(), 5, "RF/t"));
				tooltip.add(TextFormatting.RED + HEATING_FACTOR + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getInfiltratorHeatingFactor(), 1));
				double radiation = getBaseProcessRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + BASE_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		public static final String BASE_TIME = Lang.localize("jei.nuclearcraft.base_process_time");
		public static final String BASE_POWER = Lang.localize("jei.nuclearcraft.base_process_power");
		public static final String HEATING_FACTOR = Lang.localize("jei.nuclearcraft.infiltrator_heating_factor");
		public static final String BASE_RADIATION = Lang.localize("jei.nuclearcraft.base_process_radiation");
	}
	
	public static class InfiltratorPressureFluidRecipeWrapper extends JEISimpleRecipeWrapper<InfiltratorPressureFluidRecipeWrapper> {
		
		public InfiltratorPressureFluidRecipeWrapper(IGuiHelper guiHelper, JEISimpleCategoryInfo<InfiltratorPressureFluidRecipeWrapper> categoryInfo, BasicRecipe recipe) {
			super(guiHelper, categoryInfo, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
		
		protected double getInfiltratorPressureFluidEfficiency() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getInfiltratorPressureFluidEfficiency();
		}
		
		@Override
		public void addFluidIngredientTooltip(List<String> tooltip, IFluidIngredient ingredient) {
			tooltip.add(TextFormatting.LIGHT_PURPLE + EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getInfiltratorPressureFluidEfficiency(), 1));
		}
		
		private static final String EFFICIENCY = Lang.localize("jei.nuclearcraft.infiltrator_pressure_fluid_efficiency");
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
	
	public static class FissionIrradiatorRecipeWrapper extends JEIBasicProcessorRecipeWrapper<TileFissionIrradiator, FissionIrradiatorUpdatePacket, FissionIrradiatorRecipeWrapper> {
		
		public FissionIrradiatorRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(getIrradiatorFluxRequired() / 8000D);
		}
		
		protected long getIrradiatorFluxRequired() {
			if (recipe == null) {
				return 1L;
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
		
		protected long getIrradiatorMinFluxPerTick() {
			if (recipe == null) {
				return 0L;
			}
			return recipe.getIrradiatorMinFluxPerTick();
		}
		
		protected long getIrradiatorMaxFluxPerTick() {
			if (recipe == null) {
				return -1L;
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
				long minFluxPerTick = getIrradiatorMinFluxPerTick(), maxFluxPerTick = getIrradiatorMaxFluxPerTick();
				if (minFluxPerTick > 0 || (maxFluxPerTick >= 0 && maxFluxPerTick < Long.MAX_VALUE)) {
					if (minFluxPerTick <= 0) {
						tooltip.add(TextFormatting.RED + VALID_FLUX_MAXIMUM + " " + TextFormatting.WHITE + minFluxPerTick + " N/t");
					}
					else if (maxFluxPerTick < 0 || maxFluxPerTick == Long.MAX_VALUE) {
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
			return NCMath.toInt(getFissionFuelTime() / 16D);
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
	
	public static class SolidFissionRecipeWrapper extends JEIBasicProcessorRecipeWrapper<TileSolidFissionCell, SolidFissionCellUpdatePacket, SolidFissionRecipeWrapper> {
		
		public SolidFissionRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(getFissionFuelTime() / 16D);
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
	
	public static class SaltFissionRecipeWrapper extends JEIBasicProcessorRecipeWrapper<TileSaltFissionVessel, SaltFissionVesselUpdatePacket, SaltFissionRecipeWrapper> {
		
		public SaltFissionRecipeWrapper(String name, IGuiHelper guiHelper, BasicRecipe recipe) {
			super(name, guiHelper, recipe);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCMath.toInt(9D * getSaltFissionFuelTime());
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
			return NCMath.toInt(16D / getEmergencyCoolingHeatPerInputMB());
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
			return recipe != null ? NCMath.toInt(recipe.getHeatExchangerProcessTime() / 400D) : 40;
		}
		
		protected int getHeatExchangerProcessTime() {
			if (recipe == null) {
				return 16000;
			}
			return NCMath.toInt(recipe.getHeatExchangerProcessTime());
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
				
				tooltip.add((heating ? TextFormatting.AQUA + COOLING_PROVIDED : TextFormatting.RED + HEATING_PROVIDED) + TextFormatting.WHITE + " " + Math.abs(inputTemp - outputTemp) + " H/t");
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
			return NCMath.toInt(getCondenserProcessTime() / 2D);
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
