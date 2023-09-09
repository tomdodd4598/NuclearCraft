package nc.recipe.processor;

import static nc.util.FluidStackHelper.INGOT_VOLUME;

import com.google.common.collect.Lists;

import nc.init.NCItems;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;

public class ExtractorRecipes extends BasicProcessorRecipeHandler {
	
	public ExtractorRecipes() {
		super("extractor", 1, 0, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("turfMoon", RegistryHelper.getBlock("advancedrocketry:moonturf"), RegistryHelper.getBlock("advancedrocketry:moonturf_dark")), Blocks.GRAVEL, fluidStack("helium_3", 250), 0.5D, 1.5D);
		
		addRecipe(NCItems.ground_cocoa_nibs, NCItems.cocoa_solids, fluidStack("cocoa_butter", INGOT_VOLUME), 0.5D, 0.5D);
	}
}
