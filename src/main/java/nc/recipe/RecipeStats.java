package nc.recipe;

import static nc.config.NCConfig.*;

import it.unimi.dsi.fastutil.objects.*;
import nc.config.NCConfig;
import nc.util.NCMath;

public class RecipeStats {
	
	private static final Object2DoubleMap<String> PROCESSOR_MAX_BASE_PROCESS_TIME_MAP = new Object2DoubleOpenHashMap<>();
	private static final Object2DoubleMap<String> PROCESSOR_MAX_BASE_PROCESS_POWER_MAP = new Object2DoubleOpenHashMap<>();
	
	private static int decay_generator_max_power;
	private static int fission_max_moderator_line_flux;
	private static int scrubber_max_process_power;
	private static double block_mutation_threshold;
	private static double block_purification_threshold;
	
	public static void init() {
		setProcessorMaxStats();
		setDecayGeneratorMaxPower();
		setFissionMaxModeratorLineFlux();
		setScrubberMaxProcessPower();
		setBlockMutationThreshold();
		setBlockPurificationThreshold();
	}
	
	public static double getProcessorMaxBaseProcessTime(String name) {
		return PROCESSOR_MAX_BASE_PROCESS_TIME_MAP.getDouble(name);
	}
	
	public static double getProcessorMaxBaseProcessPower(String name) {
		return PROCESSOR_MAX_BASE_PROCESS_POWER_MAP.getDouble(name);
	}
	
	private static void setProcessorMaxStats() {
		for (int i = 0; i < NCRecipes.processor_recipe_handlers.length; ++i) {
			processor_max_base_process_time[i] = 1D;
			processor_max_base_process_power[i] = 0D;
			for (BasicRecipe recipe : NCRecipes.processor_recipe_handlers[i].getRecipeList()) {
				if (recipe != null) {
					processor_max_base_process_time[i] = Math.max(processor_max_base_process_time[i], recipe.getBaseProcessTime(processor_time_multiplier * processor_time[i]));
					processor_max_base_process_power[i] = Math.max(processor_max_base_process_power[i], recipe.getBaseProcessPower(processor_power_multiplier * NCConfig.processor_power[i]));
				}
			}
		}
	}
	
	public static int getDecayGeneratorMaxPower() {
		return decay_generator_max_power;
	}
	
	private static void setDecayGeneratorMaxPower() {
		double max = 0D;
		for (BasicRecipe recipe : NCRecipes.getRecipeList("decay_generator")) {
			if (recipe != null) {
				max = Math.max(max, recipe.getDecayGeneratorPower());
			}
		}
		decay_generator_max_power = NCMath.toInt(machine_update_rate * max);
	}
	
	public static int getFissionMaxModeratorLineFlux() {
		return fission_max_moderator_line_flux;
	}
	
	private static void setFissionMaxModeratorLineFlux() {
		fission_max_moderator_line_flux = 0;
		for (BasicRecipe recipe : NCRecipes.getRecipeList("fission_moderator")) {
			if (recipe != null) {
				fission_max_moderator_line_flux = Math.max(fission_max_moderator_line_flux, recipe.getFissionModeratorFluxFactor());
			}
		}
		fission_max_moderator_line_flux *= NCConfig.fission_neutron_reach;
	}
	
	public static int getScrubberMaxProcessPower() {
		return scrubber_max_process_power;
	}
	
	private static void setScrubberMaxProcessPower() {
		scrubber_max_process_power = 0;
		for (BasicRecipe recipe : NCRecipes.getRecipeList("radiation_scrubber")) {
			if (recipe != null) {
				scrubber_max_process_power = Math.max(scrubber_max_process_power, recipe.getScrubberProcessPower());
			}
		}
	}
	
	public static double getBlockMutationThreshold() {
		return block_mutation_threshold;
	}
	
	private static void setBlockMutationThreshold() {
		block_mutation_threshold = Double.MAX_VALUE;
		for (BasicRecipe recipe : NCRecipes.getRecipeList("radiation_block_mutation")) {
			if (recipe != null) {
				block_mutation_threshold = Math.min(block_mutation_threshold, recipe.getBlockMutationThreshold());
			}
		}
	}
	
	public static double getBlockPurificationThreshold() {
		return block_purification_threshold;
	}
	
	private static void setBlockPurificationThreshold() {
		block_purification_threshold = 0D;
		for (BasicRecipe recipe : NCRecipes.getRecipeList("radiation_block_purification")) {
			if (recipe != null) {
				block_purification_threshold = Math.max(block_purification_threshold, recipe.getBlockMutationThreshold());
			}
		}
	}
}
