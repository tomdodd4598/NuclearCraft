package nc.recipe.processor;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;
import net.minecraft.item.ItemStack;

public class DissolverRecipes extends BaseRecipeHandler {
	
	public DissolverRecipes() {
		super("dissolver", 1, 1, 0, 1);
	}

	@Override
	public void addRecipes() {
		addRecipe("dustBoronNitride", fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("boron_nitride_solution", FluidHelper.GEM_VOLUME), NCConfig.processor_time[15]);
		addRecipe("dustFluorite", fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("fluorite_water", FluidHelper.GEM_VOLUME), NCConfig.processor_time[15]);
		addRecipe("dustCalciumSulfate", fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("calcium_sulfate_solution", FluidHelper.GEM_VOLUME), NCConfig.processor_time[15]);
		
		addRecipe(new ItemStack(NCBlocks.glowing_mushroom, 3), fluidStack("ethanol", FluidHelper.BUCKET_VOLUME/4), fluidStack("radaway", FluidHelper.BUCKET_VOLUME/4), NCConfig.processor_time[15]);
	}
}
