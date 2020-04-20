package nc.integration.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import nc.config.NCConfig;
import nc.radiation.RadiationHelper;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.util.math.MathHelper;
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
	
	public static class Separator extends JEIRecipeWrapperProcessor<Separator> {
		
		public Separator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
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
			super(guiHelper, jeiHandler, recipeHandler, recipe, 39, 30, 176, 3, 37, 38, 58, 30, 57, 30, 37, 38);
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
	
	public static class Electrolyzer extends JEIRecipeWrapperProcessor<Electrolyzer> {
		
		public Electrolyzer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
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
	
	public static class Assembler extends JEIRecipeWrapperProcessor<Assembler> {
		
		public Assembler(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 36, 84, 31, 83, 30, 37, 38);
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
	
	public static class Enricher extends JEIRecipeWrapperProcessor<Enricher> {
		
		public Enricher(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
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
		
		protected String getCollectorProductionRate() {
			if (recipe == null) return null;
			return recipe.getCollectorProductionRate();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + PRODUCTION_RATE + " " + TextFormatting.WHITE + getCollectorProductionRate());
			}
			
			return tooltip;
		}
		
		private static final String PRODUCTION_RATE = Lang.localise("jei.nuclearcraft.collector_production_rate");
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
			if (recipe == null) return 1200D;
			return recipe.getDecayLifetime();
		}
		
		protected double getDecayPower() {
			if (recipe == null) return 0D;
			return recipe.getDecayPower();
		}
		
		protected double getDecayRadiation() {
			if (recipe == null) return 0D;
			return recipe.getDecayRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + BLOCK_LIFETIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getDecayLifetime(), 3, 1));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BLOCK_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getDecayPower(), 5, "RF/t"));
				double radiation = getDecayRadiation();
				if (radiation > 0D) tooltip.add(TextFormatting.GOLD + BLOCK_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
			
			return tooltip;
		}
		
		private static final String BLOCK_LIFETIME = Lang.localise("jei.nuclearcraft.decay_gen_lifetime");
		private static final String BLOCK_POWER = Lang.localise("jei.nuclearcraft.decay_gen_power");
		private static final String BLOCK_RADIATION = Lang.localise("jei.nuclearcraft.decay_gen_radiation");
	}
	
	public static class FissionModerator extends JEIRecipeWrapperAbstract<FissionModerator> {
		
		public FissionModerator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, -1, -1, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class FissionReflector extends JEIRecipeWrapperAbstract<FissionReflector> {
		
		public FissionReflector(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, -1, -1, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class FissionIrradiator extends JEIRecipeWrapperAbstract<FissionIrradiator> {
		
		public FissionIrradiator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, "_jei", 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getIrradiatorFluxRequired()/8000D);
		}
		
		protected int getIrradiatorFluxRequired() {
			if (recipe == null) return 1;
			return recipe.getIrradiatorFluxRequired();
		}
		
		protected double getIrradiatorHeatPerFlux() {
			if (recipe == null) return 0D;
			return recipe.getIrradiatorHeatPerFlux();
		}
		
		protected double getIrradiatorProcessEfficiency() {
			if (recipe == null) return 0D;
			return recipe.getIrradiatorProcessEfficiency();
		}
		
		protected double getIrradiatorBaseProcessRadiation() {
			if (recipe == null) return 0D;
			return recipe.getIrradiatorBaseProcessRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.RED + FLUX_REQUIRED + " " + TextFormatting.WHITE + UnitHelper.prefix(getIrradiatorFluxRequired(), 5, "N"));
				double heatPerFlux = getIrradiatorHeatPerFlux();
				if (heatPerFlux > 0D) tooltip.add(TextFormatting.YELLOW + HEAT_PER_FLUX + " " + TextFormatting.WHITE + UnitHelper.prefix(heatPerFlux, 5, "H/t/N"));
				double efficiency = getIrradiatorProcessEfficiency();
				if (efficiency > 0D) tooltip.add(TextFormatting.LIGHT_PURPLE + EFFICIENCY + " " + TextFormatting.WHITE + Math.round(100D*efficiency) + "%");
				double radiation = getIrradiatorBaseProcessRadiation();
				if (radiation > 0D) tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
			
			return tooltip;
		}
		
		private static final String FLUX_REQUIRED = Lang.localise("jei.nuclearcraft.irradiator_flux_required");
		private static final String HEAT_PER_FLUX = Lang.localise("jei.nuclearcraft.irradiator_heat_per_flux");
		private static final String EFFICIENCY = Lang.localise("jei.nuclearcraft.irradiator_process_efficiency");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.base_process_radiation");
	}
	
	public static class PebbleFission extends JEIRecipeWrapperAbstract<PebbleFission> {
		
		public PebbleFission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFissionFuelTime()/80D);
		}
		
		protected int getFissionFuelTime() {
			if (recipe == null) return 1;
			return recipe.getFissionFuelTime();
		}
		
		protected int getFissionFuelHeat() {
			if (recipe == null) return 0;
			return recipe.getFissionFuelHeat();
		}
		
		protected double getFissionFuelEfficiency() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelEfficiency();
		}
		
		protected int getFissionFuelCriticality() {
			if (recipe == null) return 1;
			return recipe.getFissionFuelCriticality();
		}
		
		protected boolean getFissionFuelSelfPriming() {
			if (recipe == null) return false;
			return recipe.getFissionFuelSelfPriming();
		}
		
		protected double getFissionFuelRadiation() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getFissionFuelTime()*NCConfig.fission_fuel_time_multiplier, 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat(), 5, "H/t"));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_EFFICIENCY + " " + TextFormatting.WHITE + Math.round(100D*getFissionFuelEfficiency()) + "%");
				tooltip.add(TextFormatting.RED + FUEL_CRITICALITY + " " + TextFormatting.WHITE + getFissionFuelCriticality() + " N/t");
				if (getFissionFuelSelfPriming()) tooltip.add(TextFormatting.DARK_AQUA + FUEL_SELF_PRIMING);
				double radiation = getFissionFuelRadiation();
				if (radiation > 0D) tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.pebble_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.pebble_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localise("jei.nuclearcraft.pebble_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localise("jei.nuclearcraft.pebble_fuel_criticality");
		private static final String FUEL_SELF_PRIMING = Lang.localise("jei.nuclearcraft.pebble_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.pebble_fuel_radiation");
	}
	
	public static class SolidFission extends JEIRecipeWrapperAbstract<SolidFission> {
		
		public SolidFission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFissionFuelTime()/80D);
		}
		
		protected int getFissionFuelTime() {
			if (recipe == null) return 1;
			return recipe.getFissionFuelTime();
		}
		
		protected int getFissionFuelHeat() {
			if (recipe == null) return 0;
			return recipe.getFissionFuelHeat();
		}
		
		protected double getFissionFuelEfficiency() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelEfficiency();
		}
		
		protected int getFissionFuelCriticality() {
			if (recipe == null) return 1;
			return recipe.getFissionFuelCriticality();
		}
		
		protected boolean getFissionFuelSelfPriming() {
			if (recipe == null) return false;
			return recipe.getFissionFuelSelfPriming();
		}
		
		protected double getFissionFuelRadiation() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getFissionFuelTime()*NCConfig.fission_fuel_time_multiplier, 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat(), 5, "H/t"));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_EFFICIENCY + " " + TextFormatting.WHITE + Math.round(100D*getFissionFuelEfficiency()) + "%");
				tooltip.add(TextFormatting.RED + FUEL_CRITICALITY + " " + TextFormatting.WHITE + getFissionFuelCriticality() + " N/t");
				if (getFissionFuelSelfPriming()) tooltip.add(TextFormatting.DARK_AQUA + FUEL_SELF_PRIMING);
				double radiation = getFissionFuelRadiation();
				if (radiation > 0D) tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.solid_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.solid_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localise("jei.nuclearcraft.solid_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localise("jei.nuclearcraft.solid_fuel_criticality");
		private static final String FUEL_SELF_PRIMING = Lang.localise("jei.nuclearcraft.solid_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.solid_fuel_radiation");
	}
	
	public static class FissionHeating extends JEIRecipeWrapperAbstract<FissionHeating> {
		
		public FissionHeating(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return getFissionHeatingHeatPerInputMB()/4;
		}
		
		protected int getFissionHeatingHeatPerInputMB() {
			if (recipe == null) return 64;
			return recipe.getFissionHeatingHeatPerInputMB();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.YELLOW + HEAT_PER_MB + " " + TextFormatting.WHITE + getFissionHeatingHeatPerInputMB() + " H/mB");
			}
			
			return tooltip;
		}
		
		private static final String HEAT_PER_MB = Lang.localise("jei.nuclearcraft.fission_heating_heat_per_mb");
	}
	
	public static class SaltFission extends JEIRecipeWrapperAbstract<SaltFission> {
		
		public SaltFission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getSaltFissionFuelTime()/80D);
		}
		
		protected double getSaltFissionFuelTime() {
			if (recipe == null) return 1D;
			return recipe.getSaltFissionFuelTime();
		}
		
		protected int getFissionFuelHeat() {
			if (recipe == null) return 0;
			return recipe.getFissionFuelHeat();
		}
		
		protected double getFissionFuelEfficiency() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelEfficiency();
		}
		
		protected int getFissionFuelCriticality() {
			if (recipe == null) return 1;
			return recipe.getFissionFuelCriticality();
		}
		
		protected boolean getFissionFuelSelfPriming() {
			if (recipe == null) return false;
			return recipe.getFissionFuelSelfPriming();
		}
		
		protected double getFissionFuelRadiation() {
			if (recipe == null) return 0D;
			return recipe.getFissionFuelRadiation();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getSaltFissionFuelTime()*NCConfig.fission_fuel_time_multiplier, 3));
				tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(getFissionFuelHeat(), 5, "H/t"));
				tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_EFFICIENCY + " " + TextFormatting.WHITE + Math.round(100D*getFissionFuelEfficiency()) + "%");
				tooltip.add(TextFormatting.RED + FUEL_CRITICALITY + " " + TextFormatting.WHITE + getFissionFuelCriticality() + " N/t");
				if (getFissionFuelSelfPriming()) tooltip.add(TextFormatting.DARK_AQUA + FUEL_SELF_PRIMING);
				double radiation = getFissionFuelRadiation();
				if (radiation > 0D) tooltip.add(TextFormatting.GOLD + FUEL_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
			
			return tooltip;
		}
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.salt_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.salt_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localise("jei.nuclearcraft.salt_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localise("jei.nuclearcraft.salt_fuel_criticality");
		private static final String FUEL_SELF_PRIMING = Lang.localise("jei.nuclearcraft.salt_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.salt_fuel_radiation");
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
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 55 && mouseY >= 34 - 30 && mouseX < 73 - 55 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + COMBO_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getFusionComboTime()/NCConfig.fusion_fuel_use, 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + COMBO_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(100D*getFusionComboPower()*NCConfig.fusion_base_power, 5, "RF/t"));
				double optimalTemp = NCMath.round(R*getFusionComboHeatVariable(), 2);
				tooltip.add(TextFormatting.YELLOW + COMBO_TEMP + " " + (optimalTemp < 20000000D/1000D ? TextFormatting.WHITE : TextFormatting.GOLD) + UnitHelper.prefix(optimalTemp, 5, "K", 2));
			}
			
			return tooltip;
		}
		
		private static final double R = 1.21875567483D;
		
		private static final String COMBO_TIME = Lang.localise("jei.nuclearcraft.fusion_time");
		private static final String COMBO_POWER = Lang.localise("jei.nuclearcraft.fusion_power");
		private static final String COMBO_TEMP = Lang.localise("jei.nuclearcraft.fusion_temp");
	}
	
	public static class CoolantHeater extends JEIRecipeWrapperAbstract<CoolantHeater> {
		
		public CoolantHeater(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 20;
		}
		
		protected int getCoolantHeaterCoolingRate() {
			if (recipe == null) return 40;
			return recipe.getCoolantHeaterCoolingRate();
		}
		
		protected String[] getCoolantHeaterJEIInfo() {
			if (recipe == null) return null;
			return recipe.getCoolantHeaterJEIInfo();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
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
			return recipe != null ? (int) (recipe.getHeatExchangerProcessTime()/400D) : 40;
		}
		
		protected int getHeatExchangerProcessTime() {
			if (recipe == null) return 16000;
			return (int) recipe.getHeatExchangerProcessTime();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
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
		
		protected double getCondenserProcessTime() {
			if (recipe == null) return 40D;
			return recipe.getCondenserProcessTime();
		}
		
		protected int getCondenserCondensingTemperature() {
			if (recipe == null) return 300;
			return recipe.getCondenserCondensingTemperature();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.YELLOW + CONDENSING_TEMPERATURE + TextFormatting.WHITE + " " + getCondenserCondensingTemperature() + "K");
				tooltip.add(TextFormatting.BLUE + HEAT_REMOVAL_REQUIRED + TextFormatting.WHITE + " " + NCMath.sigFigs(getCondenserProcessTime(), 3));
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
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.LIGHT_PURPLE + ENERGY_DENSITY + " " + TextFormatting.WHITE + NCMath.decimalPlaces(getTurbinePowerPerMB(), 2) + " RF/mB");
				tooltip.add(TextFormatting.GRAY + EXPANSION + " " + TextFormatting.WHITE + Math.round(100D*getTurbineExpansionLevel()) + "%");
			}
			
			return tooltip;
		}
		
		private static final String ENERGY_DENSITY = Lang.localise("jei.nuclearcraft.turbine_energy_density");
		private static final String EXPANSION = Lang.localise("jei.nuclearcraft.turbine_expansion");
	}
	
	public static class RadiationScrubber extends JEIRecipeWrapperAbstract<RadiationScrubber> {
		
		public RadiationScrubber(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 16, 70, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return MathHelper.ceil(getScrubberProcessTime()/120);
		}
		
		protected double getScrubberProcessTime() {
			if (recipe == null) return 1;
			return recipe.getScrubberProcessTime();
		}
		
		protected int getScrubberProcessPower() {
			if (recipe == null) return 0;
			return recipe.getScrubberProcessPower();
		}
		
		protected double getScrubberProcessEfficiency() {
			if (recipe == null) return 0D;
			return recipe.getScrubberProcessEfficiency();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 69 - 31 && mouseY >= 34 - 30 && mouseX < 69 - 31 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + PROCESS_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getScrubberProcessTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + PROCESS_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getScrubberProcessPower(), 5, "RF/t"));
				tooltip.add(TextFormatting.RED + PROCESS_EFFICIENCY + " " + TextFormatting.WHITE + Math.round(100D*getScrubberProcessEfficiency()) + "%");
			}
			
			return tooltip;
		}
		
		private static final String PROCESS_TIME = Lang.localise("jei.nuclearcraft.scrubber_process_time");
		private static final String PROCESS_POWER = Lang.localise("jei.nuclearcraft.scrubber_process_power");
		private static final String PROCESS_EFFICIENCY = Lang.localise("jei.nuclearcraft.scrubber_process_efficiency");
	}
}
