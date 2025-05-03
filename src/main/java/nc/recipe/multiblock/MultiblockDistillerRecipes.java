package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;

import java.util.*;

import static nc.util.FluidStackHelper.BUCKET_VOLUME;

public class MultiblockDistillerRecipes extends BasicRecipeHandler {
	
	public MultiblockDistillerRecipes() {
		super("multiblock_distiller", 0, 2, 0, 8);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", BUCKET_VOLUME / 4), fluidStack("hydrogen_sulfide", BUCKET_VOLUME / 4), fluidStack("le_water", BUCKET_VOLUME / 4), fluidStack("depleted_hydrogen_sulfide", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 0.25D);
		addRecipe(fluidStack("le_water", BUCKET_VOLUME / 4), fluidStack("hydrogen_sulfide", BUCKET_VOLUME / 4), fluidStack("he_water", BUCKET_VOLUME / 4), fluidStack("depleted_hydrogen_sulfide", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 0.25D);
		addRecipe(fluidStack("he_water", BUCKET_VOLUME / 4), fluidStack("hydrogen_sulfide", BUCKET_VOLUME / 4), fluidStack("heavy_water", BUCKET_VOLUME / 4), fluidStack("depleted_hydrogen_sulfide", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 0.25D);
		addRecipe(fluidStack("water", BUCKET_VOLUME / 4), fluidStack("depleted_hydrogen_sulfide", BUCKET_VOLUME / 4), fluidStack("preheated_water", BUCKET_VOLUME / 4), fluidStack("hydrogen_sulfide", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 1D);
		fixer.add(Double.class, 1D);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
