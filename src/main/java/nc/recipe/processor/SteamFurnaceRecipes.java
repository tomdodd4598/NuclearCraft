package nc.recipe.processor;

import com.google.common.collect.Lists;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.recipe.BasicRecipeHandler;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static nc.util.FissionHelper.FISSION_ORE_DICT;
import static nc.util.FluidStackHelper.*;

public class SteamFurnaceRecipes extends BasicRecipeHandler {

	public SteamFurnaceRecipes() {
		super("steam_furnace", 1, 1, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("dustIron", fluidStack("steam", BUCKET_VOLUME), "ingotIron", 1D, 1D);
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
