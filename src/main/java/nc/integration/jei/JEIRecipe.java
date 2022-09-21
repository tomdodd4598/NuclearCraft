package nc.integration.jei;

import static nc.config.NCConfig.*;

import java.util.*;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class JEIRecipe {
	
	public static class Manufactory extends JEIProcessorRecipe<Manufactory> {
		
		public Manufactory(IGuiHelper guiHelper, IJEIHandler<Manufactory> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[0];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[0]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[0];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[0]);
		}
	}
	
	public static class Separator extends JEIProcessorRecipe<Separator> {
		
		public Separator(IGuiHelper guiHelper, IJEIHandler<Separator> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34, 59, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[1];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[1]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[1];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[1]);
		}
	}
	
	public static class DecayHastener extends JEIProcessorRecipe<DecayHastener> {
		
		public DecayHastener(IGuiHelper guiHelper, IJEIHandler<DecayHastener> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[2];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[2]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[2];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[2]);
		}
	}
	
	public static class FuelReprocessor extends JEIProcessorRecipe<FuelReprocessor> {
		
		public FuelReprocessor(IGuiHelper guiHelper, IJEIHandler<FuelReprocessor> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 29, 30, 176, 3, 37, 38, 48, 30, 47, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[3];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[3]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[3];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[3]);
		}
	}
	
	public static class AlloyFurnace extends JEIProcessorRecipe<AlloyFurnace> {
		
		public AlloyFurnace(IGuiHelper guiHelper, IJEIHandler<AlloyFurnace> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[4];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[4]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[4];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[4]);
		}
	}
	
	public static class Infuser extends JEIProcessorRecipe<Infuser> {
		
		public Infuser(IGuiHelper guiHelper, IJEIHandler<Infuser> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[5];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[5]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[5];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[5]);
		}
	}
	
	public static class Melter extends JEIProcessorRecipe<Melter> {
		
		public Melter(IGuiHelper guiHelper, IJEIHandler<Melter> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[6];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[6]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[6];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[6]);
		}
	}
	
	public static class Supercooler extends JEIProcessorRecipe<Supercooler> {
		
		public Supercooler(IGuiHelper guiHelper, IJEIHandler<Supercooler> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[7];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[7]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[7];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[7]);
		}
	}
	
	public static class Electrolyzer extends JEIProcessorRecipe<Electrolyzer> {
		
		public Electrolyzer(IGuiHelper guiHelper, IJEIHandler<Electrolyzer> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30, 67, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[8];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[8]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[8];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[8]);
		}
	}
	
	public static class Assembler extends JEIProcessorRecipe<Assembler> {
		
		public Assembler(IGuiHelper guiHelper, IJEIHandler<Assembler> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 36, 84, 31, 83, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[9];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[9]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[9];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[9]);
		}
	}
	
	public static class IngotFormer extends JEIProcessorRecipe<IngotFormer> {
		
		public IngotFormer(IGuiHelper guiHelper, IJEIHandler<IngotFormer> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[10];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[10]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[10];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[10]);
		}
	}
	
	public static class Pressurizer extends JEIProcessorRecipe<Pressurizer> {
		
		public Pressurizer(IGuiHelper guiHelper, IJEIHandler<Pressurizer> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[11];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[11]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[11];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[11]);
		}
	}
	
	public static class ChemicalReactor extends JEIProcessorRecipe<ChemicalReactor> {
		
		public ChemicalReactor(IGuiHelper guiHelper, IJEIHandler<ChemicalReactor> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 18, 70, 34, 69, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[12];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[12]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[12];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[12]);
		}
	}
	
	public static class SaltMixer extends JEIProcessorRecipe<SaltMixer> {
		
		public SaltMixer(IGuiHelper guiHelper, IJEIHandler<SaltMixer> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 18, 84, 34, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[13];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[13]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[13];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[13]);
		}
	}
	
	public static class Crystallizer extends JEIProcessorRecipe<Crystallizer> {
		
		public Crystallizer(IGuiHelper guiHelper, IJEIHandler<Crystallizer> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35, 73, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[14];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[14]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[14];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[14]);
		}
	}
	
	public static class Enricher extends JEIProcessorRecipe<Enricher> {
		
		public Enricher(IGuiHelper guiHelper, IJEIHandler<Enricher> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35, 83, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[15];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[15]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[15];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[15]);
		}
	}
	
	public static class Extractor extends JEIProcessorRecipe<Extractor> {
		
		public Extractor(IGuiHelper guiHelper, IJEIHandler<Extractor> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34, 59, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[16];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[16]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[16];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[16]);
		}
	}
	
	public static class Centrifuge extends JEIProcessorRecipe<Centrifuge> {
		
		public Centrifuge(IGuiHelper guiHelper, IJEIHandler<Centrifuge> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 39, 30, 176, 3, 37, 38, 58, 30, 57, 30, 37, 38);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[17];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[17]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[17];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[17]);
		}
	}
	
	public static class RockCrusher extends JEIProcessorRecipe<RockCrusher> {
		
		public RockCrusher(IGuiHelper guiHelper, IJEIHandler<RockCrusher> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 29, 30, 176, 3, 37, 16, 56, 35, 55, 34, 37, 18);
		}
		
		@Override
		protected double getBaseProcessTime() {
			if (recipe == null) {
				return processor_time_multiplier * processor_time[18];
			}
			return recipe.getBaseProcessTime(processor_time_multiplier * processor_time[18]);
		}
		
		@Override
		protected double getBaseProcessPower() {
			if (recipe == null) {
				return processor_power_multiplier * processor_power[18];
			}
			return recipe.getBaseProcessPower(processor_power_multiplier * processor_power[18]);
		}
	}
	
	public static class Collector extends JEIBasicRecipe<Collector> {
		
		public Collector(IGuiHelper guiHelper, IJEIHandler<Collector> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + PRODUCTION_RATE + " " + TextFormatting.WHITE + getCollectorProductionRate());
			}
			
			return tooltip;
		}
		
		private static final String PRODUCTION_RATE = Lang.localise("jei.nuclearcraft.collector_production_rate");
	}
	
	public static class DecayGenerator extends JEIBasicRecipe<DecayGenerator> {
		
		public DecayGenerator(IGuiHelper guiHelper, IJEIHandler<DecayGenerator> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + BLOCK_LIFETIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getDecayGeneratorLifetime(), 3, 1));
				tooltip.add(TextFormatting.LIGHT_PURPLE + BLOCK_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getDecayGeneratorPower(), 5, "RF/t"));
				double radiation = getDecayGeneratorRadiation();
				if (radiation > 0D) {
					tooltip.add(TextFormatting.GOLD + BLOCK_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
				}
			}
			
			return tooltip;
		}
		
		private static final String BLOCK_LIFETIME = Lang.localise("jei.nuclearcraft.decay_gen_lifetime");
		private static final String BLOCK_POWER = Lang.localise("jei.nuclearcraft.decay_gen_power");
		private static final String BLOCK_RADIATION = Lang.localise("jei.nuclearcraft.decay_gen_radiation");
	}
	
	public static class FissionModerator extends JEIBasicRecipe<FissionModerator> {
		
		public FissionModerator(IGuiHelper guiHelper, IJEIHandler<FissionModerator> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, -1, -1, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class FissionReflector extends JEIBasicRecipe<FissionReflector> {
		
		public FissionReflector(IGuiHelper guiHelper, IJEIHandler<FissionReflector> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, -1, -1, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return 1;
		}
	}
	
	public static class FissionIrradiator extends JEIBasicRecipe<FissionIrradiator> {
		
		public FissionIrradiator(IGuiHelper guiHelper, IJEIHandler<FissionIrradiator> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
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
		
		private static final String FLUX_REQUIRED = Lang.localise("jei.nuclearcraft.irradiator_flux_required");
		private static final String HEAT_PER_FLUX = Lang.localise("jei.nuclearcraft.irradiator_heat_per_flux");
		private static final String EFFICIENCY = Lang.localise("jei.nuclearcraft.irradiator_process_efficiency");
		private static final String VALID_FLUX_MINIMUM = Lang.localise("jei.nuclearcraft.irradiator_valid_flux_minimum");
		private static final String VALID_FLUX_MAXIMUM = Lang.localise("jei.nuclearcraft.irradiator_valid_flux_maximum");
		private static final String VALID_FLUX_RANGE = Lang.localise("jei.nuclearcraft.irradiator_valid_flux_range");
		private static final String RADIATION_PER_FLUX = Lang.localise("jei.nuclearcraft.radiation_per_flux");
	}
	
	public static class PebbleFission extends JEIBasicRecipe<PebbleFission> {
		
		public PebbleFission(IGuiHelper guiHelper, IJEIHandler<PebbleFission> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
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
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.pebble_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.pebble_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localise("jei.nuclearcraft.pebble_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localise("jei.nuclearcraft.pebble_fuel_criticality");
		private static final String FUEL_DECAY_FACTOR = Lang.localise("jei.nuclearcraft.pebble_fuel_decay_factor");
		private static final String FUEL_SELF_PRIMING = Lang.localise("jei.nuclearcraft.pebble_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.pebble_fuel_radiation");
	}
	
	public static class SolidFission extends JEIBasicRecipe<SolidFission> {
		
		public SolidFission(IGuiHelper guiHelper, IJEIHandler<SolidFission> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
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
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.solid_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.solid_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localise("jei.nuclearcraft.solid_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localise("jei.nuclearcraft.solid_fuel_criticality");
		private static final String FUEL_DECAY_FACTOR = Lang.localise("jei.nuclearcraft.solid_fuel_decay_factor");
		private static final String FUEL_SELF_PRIMING = Lang.localise("jei.nuclearcraft.solid_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.solid_fuel_radiation");
	}
	
	public static class FissionHeating extends JEIBasicRecipe<FissionHeating> {
		
		public FissionHeating(IGuiHelper guiHelper, IJEIHandler<FissionHeating> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.YELLOW + HEAT_PER_MB + " " + TextFormatting.WHITE + getFissionHeatingHeatPerInputMB() + " H/mB");
			}
			
			return tooltip;
		}
		
		private static final String HEAT_PER_MB = Lang.localise("jei.nuclearcraft.fission_heating_heat_per_mb");
	}
	
	public static class SaltFission extends JEIBasicRecipe<SaltFission> {
		
		public SaltFission(IGuiHelper guiHelper, IJEIHandler<SaltFission> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
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
		
		private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.salt_fuel_time");
		private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.salt_fuel_heat");
		private static final String FUEL_EFFICIENCY = Lang.localise("jei.nuclearcraft.salt_fuel_efficiency");
		private static final String FUEL_CRITICALITY = Lang.localise("jei.nuclearcraft.salt_fuel_criticality");
		private static final String FUEL_DECAY_FACTOR = Lang.localise("jei.nuclearcraft.salt_fuel_decay_factor");
		private static final String FUEL_SELF_PRIMING = Lang.localise("jei.nuclearcraft.salt_fuel_self_priming");
		private static final String FUEL_RADIATION = Lang.localise("jei.nuclearcraft.salt_fuel_radiation");
	}
	
	public static class CoolantHeater extends JEIBasicRecipe<CoolantHeater> {
		
		public CoolantHeater(IGuiHelper guiHelper, IJEIHandler<CoolantHeater> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.BLUE + COOLING + " " + TextFormatting.WHITE + UnitHelper.prefix(getCoolantHeaterCoolingRate(), 5, "H/t"));
				if (getCoolantHeaterJEIInfo() != null) {
					for (String posInfo : getCoolantHeaterJEIInfo()) {
						tooltip.add(TextFormatting.AQUA + posInfo);
					}
				}
			}
			
			return tooltip;
		}
		
		private static final String COOLING = Lang.localise("jei.nuclearcraft.coolant_heater_rate");
	}
	
	public static class FissionEmergencyCooling extends JEIBasicRecipe<FissionEmergencyCooling> {
		
		public FissionEmergencyCooling(IGuiHelper guiHelper, IJEIHandler<FissionEmergencyCooling> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.BLUE + COOLING_PER_MB + " " + TextFormatting.WHITE + NCMath.decimalPlaces(getEmergencyCoolingHeatPerInputMB(), 2) + " H/mB");
			}
			
			return tooltip;
		}
		
		private static final String COOLING_PER_MB = Lang.localise("jei.nuclearcraft.fission_emergency_cooling_per_mb");
	}
	
	public static class Fusion extends JEIBasicRecipe<Fusion> {
		
		public Fusion(IGuiHelper guiHelper, IJEIHandler<Fusion> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 55, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected int getProgressArrowTime() {
			return (int) (getFusionComboTime() / 4D);
		}
		
		protected double getFusionComboTime() {
			if (recipe == null) {
				return 1D;
			}
			return recipe.getFusionComboTime();
		}
		
		protected double getFusionComboHeat() {
			if (recipe == null) {
				return 0D;
			}
			return recipe.getFusionComboHeat();
		}
		
		protected double getFusionComboOptimalTemperature() {
			if (recipe == null) {
				return 1000D;
			}
			return recipe.getFusionComboOptimalTemperature();
		}
		
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			List<String> tooltip = new ArrayList<>();
			
			if (mouseX >= 73 - 55 && mouseY >= 34 - 30 && mouseX < 73 - 55 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + COMBO_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getFusionComboTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + COMBO_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getFusionComboHeat(), 5, "RF/t"));
				double optimalTemp = getFusionComboOptimalTemperature();
				tooltip.add(TextFormatting.YELLOW + COMBO_TEMP + " " + (optimalTemp < 20000000D / 1000D ? TextFormatting.WHITE : TextFormatting.GOLD) + UnitHelper.prefix(optimalTemp, 5, "K", 2));
			}
			
			return tooltip;
		}
		
		// private static final double R = 1.21875567483D;
		
		private static final String COMBO_TIME = Lang.localise("jei.nuclearcraft.fusion_time");
		private static final String COMBO_POWER = Lang.localise("jei.nuclearcraft.fusion_power");
		private static final String COMBO_TEMP = Lang.localise("jei.nuclearcraft.fusion_temp");
	}
	
	public static class HeatExchanger extends JEIBasicRecipe<HeatExchanger> {
		
		public HeatExchanger(IGuiHelper guiHelper, IJEIHandler<HeatExchanger> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, recipe != null && recipe.getHeatExchangerIsHeating() ? 3 : 19, 37, 16, 74, 35);
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
	
	public static class Condenser extends JEIBasicRecipe<Condenser> {
		
		public Condenser(IGuiHelper guiHelper, IJEIHandler<Condenser> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.YELLOW + CONDENSING_TEMPERATURE + TextFormatting.WHITE + " " + getCondenserCondensingTemperature() + "K");
				tooltip.add(TextFormatting.BLUE + HEAT_REMOVAL_REQUIRED + TextFormatting.WHITE + " " + NCMath.sigFigs(getCondenserProcessTime(), 5));
			}
			
			return tooltip;
		}
		
		private static final String CONDENSING_TEMPERATURE = Lang.localise("jei.nuclearcraft.condenser_condensing_temp");
		private static final String HEAT_REMOVAL_REQUIRED = Lang.localise("jei.nuclearcraft.condenser_heat_removal_req");
	}
	
	public static class Turbine extends JEIBasicRecipe<Turbine> {
		
		public Turbine(IGuiHelper guiHelper, IJEIHandler<Turbine> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
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
			
			if (mouseX >= 73 - 47 && mouseY >= 34 - 30 && mouseX < 73 - 47 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.LIGHT_PURPLE + ENERGY_DENSITY + " " + TextFormatting.WHITE + NCMath.decimalPlaces(getTurbinePowerPerMB(), 2) + " RF/mB");
				tooltip.add(TextFormatting.GRAY + EXPANSION + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getTurbineExpansionLevel(), 1));
				tooltip.add(TextFormatting.GREEN + SPIN_UP + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getTurbineSpinUpMultiplier(), 1));
			}
			
			return tooltip;
		}
		
		private static final String ENERGY_DENSITY = Lang.localise("jei.nuclearcraft.turbine_energy_density");
		private static final String EXPANSION = Lang.localise("jei.nuclearcraft.turbine_expansion");
		private static final String SPIN_UP = Lang.localise("jei.nuclearcraft.turbine_spin_up_multiplier");
	}
	
	public static class RadiationScrubber extends JEIBasicRecipe<RadiationScrubber> {
		
		public RadiationScrubber(IGuiHelper guiHelper, IJEIHandler<RadiationScrubber> jeiHandler, BasicRecipeHandler recipeHandler, BasicRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 16, 70, 35);
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
			
			if (mouseX >= 69 - 31 && mouseY >= 34 - 30 && mouseX < 69 - 31 + 37 + 1 && mouseY < 34 - 30 + 18 + 1) {
				tooltip.add(TextFormatting.GREEN + PROCESS_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getScrubberProcessTime(), 3));
				tooltip.add(TextFormatting.LIGHT_PURPLE + PROCESS_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getScrubberProcessPower(), 5, "RF/t"));
				tooltip.add(TextFormatting.RED + PROCESS_EFFICIENCY + " " + TextFormatting.WHITE + NCMath.pcDecimalPlaces(getScrubberProcessEfficiency(), 1));
			}
			
			return tooltip;
		}
		
		private static final String PROCESS_TIME = Lang.localise("jei.nuclearcraft.scrubber_process_time");
		private static final String PROCESS_POWER = Lang.localise("jei.nuclearcraft.scrubber_process_power");
		private static final String PROCESS_EFFICIENCY = Lang.localise("jei.nuclearcraft.scrubber_process_efficiency");
	}
}
