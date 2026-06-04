package nc.recipe.processor;

import com.google.common.collect.Lists;
import nc.init.NCItems;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;

import static nc.config.NCConfig.*;
import static nc.util.FluidStackHelper.INGOT_VOLUME;

public class ExtractorRecipes extends BasicProcessorRecipeHandler {
	
	public ExtractorRecipes() {
		super("extractor", 1, 0, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[16]) {
			return;
		}
		
		addRecipe(Blocks.SOUL_SAND, "blockSoullessSand", fluidStack("soul", 100), 1D, 2D);
		
		addRecipe(Lists.newArrayList("turfMoon", RegistryHelper.getBlock("advancedrocketry:moonturf"), RegistryHelper.getBlock("advancedrocketry:moonturf_dark")), Blocks.GRAVEL, fluidStack("helium_3", 250), 1D, 1D);
		
		addRecipe(NCItems.ground_cocoa_nibs, NCItems.cocoa_solids, fluidStack("cocoa_butter", INGOT_VOLUME), 0.5D, 0.5D);
	}
}
