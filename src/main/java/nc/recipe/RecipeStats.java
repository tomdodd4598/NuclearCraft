package nc.recipe;

import static nc.config.NCConfig.*;

import nc.handler.TileInfoHandler;
import nc.recipe.processor.BasicProcessorRecipeHandler;
import nc.tile.TileContainerInfo;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.util.NCMath;

public class RecipeStats {
	
	private static int decay_generator_max_power;
	private static int fission_max_moderator_line_flux;
	private static double block_mutation_threshold;
	private static double block_purification_threshold;
	
	public static void init() {
		setBasicProcessorMaxStats();
		setScrubberMaxStats();
		setDecayGeneratorMaxPower();
		setFissionMaxModeratorLineFlux();
		setBlockMutationThreshold();
		setBlockPurificationThreshold();
	}
	
	private static void setBasicProcessorMaxStats() {
		for (TileContainerInfo<?> info : TileInfoHandler.TILE_CONTAINER_INFO_MAP.values()) {
			if (info instanceof ProcessorContainerInfo<?, ?, ?> processorInfo) {
				if (processorInfo.getRecipeHandler() instanceof BasicProcessorRecipeHandler processorRecipeHandler) {
					double maxProcessTimeMultiplier = 1D, maxProcessPowerMultiplier = 0D;
					for (BasicRecipe recipe : processorRecipeHandler.getRecipeList()) {
						maxProcessTimeMultiplier = Math.max(maxProcessTimeMultiplier, recipe.getProcessTimeMultiplier());
						maxProcessPowerMultiplier = Math.max(maxProcessPowerMultiplier, recipe.getProcessPowerMultiplier());
					}
					processorInfo.maxBaseProcessTime = maxProcessTimeMultiplier * processor_time_multiplier * processorInfo.defaultProcessTime;
					processorInfo.maxBaseProcessPower = maxProcessPowerMultiplier * processor_power_multiplier * processorInfo.defaultProcessPower;
				}
			}
		}
	}

	private static void setScrubberMaxStats() {
		ProcessorContainerInfo<?, ?, ?> info = TileInfoHandler.getProcessorContainerInfo("radiation_scrubber");
		info.maxBaseProcessTime = 1D;
		info.maxBaseProcessPower = 0D;
		for (BasicRecipe recipe : info.getRecipeHandler().getRecipeList()) {
			info.maxBaseProcessTime = Math.max(info.maxBaseProcessTime, recipe.getScrubberProcessTime());
			info.maxBaseProcessPower = Math.max(info.maxBaseProcessPower, recipe.getScrubberProcessPower());
		}
	}
	
	public static int getDecayGeneratorMaxPower() {
		return decay_generator_max_power;
	}
	
	private static void setDecayGeneratorMaxPower() {
		double max = 0D;
		for (BasicRecipe recipe : NCRecipes.decay_generator.recipeList) {
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
		for (BasicRecipe recipe : NCRecipes.fission_moderator.recipeList) {
			if (recipe != null) {
				fission_max_moderator_line_flux = Math.max(fission_max_moderator_line_flux, recipe.getFissionModeratorFluxFactor());
			}
		}
		fission_max_moderator_line_flux *= fission_neutron_reach;
	}
	
	public static double getBlockMutationThreshold() {
		return block_mutation_threshold;
	}
	
	private static void setBlockMutationThreshold() {
		block_mutation_threshold = Double.MAX_VALUE;
		for (BasicRecipe recipe : NCRecipes.radiation_block_mutation.recipeList) {
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
		for (BasicRecipe recipe : NCRecipes.radiation_block_purification.recipeList) {
			if (recipe != null) {
				block_purification_threshold = Math.max(block_purification_threshold, recipe.getBlockMutationThreshold());
			}
		}
	}
}
