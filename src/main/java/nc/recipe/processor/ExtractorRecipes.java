package nc.recipe.processor;

import static nc.util.FluidStackHelper.INGOT_VOLUME;

import java.util.*;

import com.google.common.collect.Lists;

import nc.init.NCItems;
import nc.recipe.BasicRecipeHandler;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;

public class ExtractorRecipes extends BasicRecipeHandler {
	
	public ExtractorRecipes() {
		super("extractor", 1, 0, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("turfMoon", RegistryHelper.getBlock("advancedrocketry:moonturf"), RegistryHelper.getBlock("advancedrocketry:moonturf_dark")), Blocks.GRAVEL, fluidStack("helium_3", 250), 0.5D, 1.5D);
		
		// addRecipe(new ItemStack(NCBlocks.cooler, 1, 1), new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("water", BUCKET_VOLUME), 1D, 1D);
		// addRecipe(new ItemStack(NCBlocks.cooler, 1, 8), new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("liquid_helium", BUCKET_VOLUME), 1D, 1D);
		// addRecipe(new ItemStack(NCBlocks.cooler, 1, 10), new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("cryotheum", BUCKET_VOLUME*2), 1D, 1D);
		addRecipe(NCItems.ground_cocoa_nibs, NCItems.cocoa_solids, fluidStack("cocoa_butter", INGOT_VOLUME), 0.5D, 0.5D);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
