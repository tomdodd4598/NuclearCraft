package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;

import java.util.List;

public class MultiblockDecayPoolRecipes extends BasicRecipeHandler {
	
	public MultiblockDecayPoolRecipes() {
		super("multiblock_decay_pool", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", 1), fluidStack("preheated_water", 1), 32);
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 64);
		return fixer.fixed;
	}
}
