package nc.recipe.processor;

import nc.recipe.BasicRecipeHandler;

import java.util.ArrayList;
import java.util.List;

import static nc.util.FluidStackHelper.BUCKET_VOLUME;

public class SteamTransformerRecipes extends BasicRecipeHandler {

	public SteamTransformerRecipes() {
		super("steam_transformer", 1, 1, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("oreIron", fluidStack("steam", BUCKET_VOLUME), "dustIron", 1D, 1D);
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
