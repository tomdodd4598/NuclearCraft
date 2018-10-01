package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ExtractorRecipes extends ProcessorRecipeHandler {
	
	public ExtractorRecipes() {
		super("extractor", 1, 0, 1, 1);
	}

	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("turfMoon", RegistryHelper.getBlock("advancedrocketry:moonturf"), RegistryHelper.getBlock("advancedrocketry:moonturf_dark")), Blocks.GRAVEL, fluidStack("helium3", 250), 0.5D, 1.5D);
		
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 1), new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 8), new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("liquidhelium", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 10), new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("cryotheum", FluidStackHelper.BUCKET_VOLUME*2), 1D, 1D);
		addRecipe(NCItems.ground_cocoa_nibs, NCItems.cocoa_solids, fluidStack("cocoa_butter", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
	}
}
