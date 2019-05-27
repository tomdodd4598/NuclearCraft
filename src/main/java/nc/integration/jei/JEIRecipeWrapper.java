package nc.integration.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import nc.config.NCConfig;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.generator.TileFusionCore;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.util.text.TextFormatting;

public class JEIRecipeWrapper {
	
	public static class Manufactory extends JEIRecipeWrapperProcessor<Manufactory> {

		public Manufactory(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[0];
			return recipe.getBaseProcessTime(NCConfig.processor_time[0]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[0];
			return recipe.getBaseProcessPower(NCConfig.processor_power[0]);
		}
	}
	
	public static class IsotopeSeparator extends JEIRecipeWrapperProcessor<IsotopeSeparator> {

		public IsotopeSeparator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34, 59, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[1];
			return recipe.getBaseProcessTime(NCConfig.processor_time[1]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[1];
			return recipe.getBaseProcessPower(NCConfig.processor_power[1]);
		}
	}
	
	public static class DecayHastener extends JEIRecipeWrapperProcessor<DecayHastener> {

		public DecayHastener(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[2];
			return recipe.getBaseProcessTime(NCConfig.processor_time[2]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[2];
			return recipe.getBaseProcessPower(NCConfig.processor_power[2]);
		}
	}
	
	public static class FuelReprocessor extends JEIRecipeWrapperProcessor<FuelReprocessor> {

		public FuelReprocessor(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30, 67, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[3];
			return recipe.getBaseProcessTime(NCConfig.processor_time[3]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[3];
			return recipe.getBaseProcessPower(NCConfig.processor_power[3]);
		}
	}
	
	public static class AlloyFurnace extends JEIRecipeWrapperProcessor<AlloyFurnace> {

		public AlloyFurnace(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[4];
			return recipe.getBaseProcessTime(NCConfig.processor_time[4]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[4];
			return recipe.getBaseProcessPower(NCConfig.processor_power[4]);
		}
	}
	
	public static class Infuser extends JEIRecipeWrapperProcessor<Infuser> {

		public Infuser(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[5];
			return recipe.getBaseProcessTime(NCConfig.processor_time[5]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[5];
			return recipe.getBaseProcessPower(NCConfig.processor_power[5]);
		}
	}
	
	public static class Melter extends JEIRecipeWrapperProcessor<Melter> {

		public Melter(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[6];
			return recipe.getBaseProcessTime(NCConfig.processor_time[6]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[6];
			return recipe.getBaseProcessPower(NCConfig.processor_power[6]);
		}
	}
	
	public static class Supercooler extends JEIRecipeWrapperProcessor<Supercooler> {

		public Supercooler(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[7];
			return recipe.getBaseProcessTime(NCConfig.processor_time[7]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[7];
			return recipe.getBaseProcessPower(NCConfig.processor_power[7]);
		}
	}
	
	public static class Electrolyser extends JEIRecipeWrapperProcessor<Electrolyser> {

		public Electrolyser(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30, 67, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[8];
			return recipe.getBaseProcessTime(NCConfig.processor_time[8]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[8];
			return recipe.getBaseProcessPower(NCConfig.processor_power[8]);
		}
	}
	
	public static class Irradiator extends JEIRecipeWrapperProcessor<Irradiator> {

		public Irradiator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 16, 70, 35, 69, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[9];
			return recipe.getBaseProcessTime(NCConfig.processor_time[9]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[9];
			return recipe.getBaseProcessPower(NCConfig.processor_power[9]);
		}
	}
	
	public static class IngotFormer extends JEIRecipeWrapperProcessor<IngotFormer> {

		public IngotFormer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[10];
			return recipe.getBaseProcessTime(NCConfig.processor_time[10]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[10];
			return recipe.getBaseProcessPower(NCConfig.processor_power[10]);
		}
	}
	
	public static class Pressurizer extends JEIRecipeWrapperProcessor<Pressurizer> {

		public Pressurizer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[11];
			return recipe.getBaseProcessTime(NCConfig.processor_time[11]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[11];
			return recipe.getBaseProcessPower(NCConfig.processor_power[11]);
		}
	}
	
	public static class ChemicalReactor extends JEIRecipeWrapperProcessor<ChemicalReactor> {

		public ChemicalReactor(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 18, 70, 34, 69, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[12];
			return recipe.getBaseProcessTime(NCConfig.processor_time[12]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[12];
			return recipe.getBaseProcessPower(NCConfig.processor_power[12]);
		}
	}
	
	public static class SaltMixer extends JEIRecipeWrapperProcessor<SaltMixer> {

		public SaltMixer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 18, 84, 34, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[13];
			return recipe.getBaseProcessTime(NCConfig.processor_time[13]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[13];
			return recipe.getBaseProcessPower(NCConfig.processor_power[13]);
		}
	}
	
	public static class Crystallizer extends JEIRecipeWrapperProcessor<Crystallizer> {

		public Crystallizer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[14];
			return recipe.getBaseProcessTime(NCConfig.processor_time[14]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[14];
			return recipe.getBaseProcessPower(NCConfig.processor_power[14]);
		}
	}
	
	public static class Dissolver extends JEIRecipeWrapperProcessor<Dissolver> {

		public Dissolver(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[15];
			return recipe.getBaseProcessTime(NCConfig.processor_time[15]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[15];
			return recipe.getBaseProcessPower(NCConfig.processor_power[15]);
		}
	}
	
	public static class Extractor extends JEIRecipeWrapperProcessor<Extractor> {

		public Extractor(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34, 59, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[16];
			return recipe.getBaseProcessTime(NCConfig.processor_time[16]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[16];
			return recipe.getBaseProcessPower(NCConfig.processor_power[16]);
		}
	}
	
	public static class Centrifuge extends JEIRecipeWrapperProcessor<Centrifuge> {

		public Centrifuge(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30, 67, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[17];
			return recipe.getBaseProcessTime(NCConfig.processor_time[17]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[17];
			return recipe.getBaseProcessPower(NCConfig.processor_power[17]);
		}
	}
	
	public static class RockCrusher extends JEIRecipeWrapperProcessor<RockCrusher> {

		public RockCrusher(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 29, 30, 176, 3, 37, 16, 56, 35, 55, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) return NCConfig.processor_time[18];
			return recipe.getBaseProcessTime(NCConfig.processor_time[18]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) return NCConfig.processor_power[18];
			return recipe.getBaseProcessPower(NCConfig.processor_power[18]);
		}
	}
	
	public static class Collector extends JEIRecipeWrapperAbstract<Collector> {

		public Collector(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return NCConfig.machine_update_rate;
		}
	}
	
	public static class ActiveCooler extends JEIRecipeWrapperAbstract<ActiveCooler> {

		public ActiveCooler(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, -1, -1, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class DecayGenerator extends JEIRecipeWrapperAbstract<DecayGenerator> {

		public DecayGenerator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getDecayLifetime()/80D);
		}
		
		protected double getDecayLifetime() {
			if (recipe == null) return TileDecayGenerator.DEFAULT_LIFETIME;
			return recipe.getDecayLifetime();
		}
		
		protected int getDecayPower() {
			if (recipe == null) return 0;
			return recipe.getDecayPower();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + BLOCK_LIFETIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(NCMath.round(getDecayLifetime(), 2), 3, 1));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BLOCK_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getDecayPower(), 5, "RF/s"));
			}
			
			return tooltip;
		}
		
		private static final String BLOCK_LIFETIME = Lang.localise("jei.nuclearcraft.decay_gen_lifetime");
		private static final String BLOCK_POWER = Lang.localise("jei.nuclearcraft.decay_gen_power");
	}
	
	public static class Fission extends JEIRecipeWrapperAbstract<Fission> {

		public Fission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, "_jei", 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFissionFuelTime()/800D);
		}
		
		protected double getFissionFuelTime() {
			if (recipe == null) return 1D;
			return recipe.getFissionFuelTime();
		}
		
		protected double getFissionFuelPower() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelPower();
		}
		
		protected double getFissionFuelHeat() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelHeat();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(NCMath.round(getFissionFuelTime()/NCConfig.fission_fuel_use, 2), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelPower()*NCConfig.fission_heat_generation, 5, "RF/t"));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat()*NCConfig.fission_heat_generation, 5, "H/t"));
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.solid_fuel_time");
		private static final String FUEL_POWER = Lang.localise("jei.nuclearcraft.solid_fuel_power");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.solid_fuel_heat");
	}
	
	public static class Fusion extends JEIRecipeWrapperAbstract<Fusion> {

		public Fusion(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, "_jei", 55, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFusionComboTime()/4D);
		}
		
		protected double getFusionComboTime() {
			if (recipe == null) return 1D;
			return recipe.getFusionComboTime();
		}
		
		protected double getFusionComboPower() {
			if (recipe == null) return 0D;
			return recipe.getFusionComboPower();
		}
		
		protected double getFusionComboHeatVariable() {
			if (recipe == null) return 1000D;
			return recipe.getFusionComboHeatVariable();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 55 && mouseY >= 34 - 30 && mouseX < 73 - 55 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + COMBO_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(NCMath.round(getFusionComboTime()/NCConfig.fusion_fuel_use, 2), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + COMBO_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(100D*getFusionComboPower()*NCConfig.fusion_base_power, 5, "RF/t"));
				double optimalTemp = NCMath.round(R*getFusionComboHeatVariable(), 2);
				tooltip.add(TextFormatting.YELLOW + COMBO_TEMP + " " + (optimalTemp < TileFusionCore.getMaxHeat()/1000D ? TextFormatting.WHITE : TextFormatting.GOLD) + UnitHelper.prefix(optimalTemp, 5, "K", 2));
			}
			
			return tooltip;
		}
		
		private static final double R = 1.21875567483D;
		
		private static final String COMBO_TIME = Lang.localise("jei.nuclearcraft.fusion_time");
		private static final String COMBO_POWER = Lang.localise("jei.nuclearcraft.fusion_power");
		private static final String COMBO_TEMP = Lang.localise("jei.nuclearcraft.fusion_temp");
	}
	
	public static class SaltFission extends JEIRecipeWrapperAbstract<SaltFission> {

		public SaltFission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getSaltFissionFuelTime()/4D);
		}
		
		protected double getSaltFissionFuelTime() {
			if (recipe == null) return 1D;
			return recipe.getSaltFissionFuelTime();
		}
		
		protected double getSaltFissionFuelHeat() {
			if (recipe == null) return 0D;
			return recipe.getSaltFissionFuelHeat();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(NCMath.round(getSaltFissionFuelTime()/NCConfig.salt_fission_fuel_use, 2), 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getSaltFissionFuelHeat()*NCConfig.salt_fission_heat_generation, 5, "H/t"));
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.salt_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.salt_fuel_heat");
	}
	
	public static class CoolantHeater extends JEIRecipeWrapperAbstract<CoolantHeater> {

		public CoolantHeater(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 20;
		}
		
		protected double getCoolantHeaterCoolingRate() {
			if (recipe == null) return 10D;
			return recipe.getCoolantHeaterCoolingRate();
		}
		
		protected String[] getCoolantHeaterJEIInfo() {
			if (recipe == null) return null;
			return recipe.getCoolantHeaterJEIInfo();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.BLUE + COOLING + " " + TextFormatting.WHITE + UnitHelper.prefix(getCoolantHeaterCoolingRate(), 5, "H/t"));
				if (getCoolantHeaterJEIInfo() != null) {
					for (String posInfo : getCoolantHeaterJEIInfo()) tooltip.add(TextFormatting.AQUA + posInfo);
				}
			}
			
			return tooltip;
		}
		
		private static final String COOLING = Lang.localise("jei.nuclearcraft.coolant_heater_rate");
	}
	
	public static class HeatExchanger extends JEIRecipeWrapperAbstract<HeatExchanger> {

		public HeatExchanger(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, recipe != null && recipe.getHeatExchangerIsHeating() ? 3 : 19, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return recipe != null ? (int) (recipe.getHeatExchangerProcessTime(16000D)/400D) : 40;
		}
		
		protected int getHeatExchangerProcessTime() {
			if (recipe == null) return 16000;
			return (int) recipe.getHeatExchangerProcessTime(16000D);
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				boolean heating = recipe.getHeatExchangerIsHeating();
				int inputTemp = recipe.getHeatExchangerInputTemperature();
				int outputTemp = recipe.getHeatExchangerOutputTemperature();
				
				tooltip.add((heating ? TextFormatting.AQUA + COOLING_PROVIDED : TextFormatting.RED + HEATING_PROVIDED) + TextFormatting.WHITE + " " + Math.abs(inputTemp - outputTemp) + "/t");
				tooltip.add((heating ? TextFormatting.RED + HEATING_REQUIRED : TextFormatting.AQUA + COOLING_REQUIRED) + TextFormatting.WHITE + " " + getHeatExchangerProcessTime());
			}
			
			return tooltip;
		}
		
		private static final String HEATING_PROVIDED = Lang.localise("jei.nuclearcraft.exchanger_heating_provided");
		private static final String COOLING_PROVIDED = Lang.localise("jei.nuclearcraft.exchanger_cooling_provided");
		private static final String HEATING_REQUIRED = Lang.localise("jei.nuclearcraft.exchanger_heating_required");
		private static final String COOLING_REQUIRED = Lang.localise("jei.nuclearcraft.exchanger_cooling_required");
	}
	
	public static class Condenser extends JEIRecipeWrapperAbstract<Condenser> {

		public Condenser(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getCondenserProcessTime()/2D);
		}
		
		protected int getCondenserProcessTime() {
			if (recipe == null) return 16000;
			return (int) recipe.getCondenserProcessTime(16000D);
		}
		
		protected int getCondenserCondensingTemperature() {
			if (recipe == null) return 300;
			return recipe.getCondenserCondensingTemperature();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.YELLOW + CONDENSING_TEMPERATURE + TextFormatting.WHITE + " " + getCondenserCondensingTemperature() + "K");
				tooltip.add(TextFormatting.BLUE + HEAT_REMOVAL_REQUIRED + TextFormatting.WHITE + " " + getCondenserProcessTime());
			}
			
			return tooltip;
		}
		
		private static final String CONDENSING_TEMPERATURE = Lang.localise("jei.nuclearcraft.condenser_condensing_temp");
		private static final String HEAT_REMOVAL_REQUIRED = Lang.localise("jei.nuclearcraft.condenser_heat_removal_req");
	}
	
	public static class Turbine extends JEIRecipeWrapperAbstract<Turbine> {

		public Turbine(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 20;
		}
		
		protected double getTurbinePowerPerMB() {
			if (recipe == null) return 0D;
			return recipe.getTurbinePowerPerMB();
		}
		
		protected double getTurbineExpansionLevel() {
			if (recipe == null) return 1D;
			return recipe.getTurbineExpansionLevel();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<String>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.LIGHT_PURPLE + ENERGY_DENSITY + " " + TextFormatting.WHITE + UnitHelper.prefix(getTurbinePowerPerMB(), 3, "RF/mB"));
				tooltip.add(TextFormatting.GRAY + EXPANSION + " " + TextFormatting.WHITE + Math.round(100D*getTurbineExpansionLevel()) + "%");
			}
			
			return tooltip;
		}
		
		private static final String ENERGY_DENSITY = Lang.localise("jei.nuclearcraft.turbine_energy_density");
		private static final String EXPANSION = Lang.localise("jei.nuclearcraft.turbine_expansion");
	}
}
