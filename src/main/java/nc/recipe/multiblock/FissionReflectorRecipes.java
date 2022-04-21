package nc.recipe.multiblock;

import static nc.config.NCConfig.*;

import java.util.*;

import nc.enumm.MetaEnums;
import nc.init.NCBlocks;
import nc.recipe.BasicRecipeHandler;
import net.minecraft.item.ItemStack;

public class FissionReflectorRecipes extends BasicRecipeHandler {
	
	public FissionReflectorRecipes() {
		super("fission_reflector", 1, 0, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < MetaEnums.NeutronReflectorType.values().length; ++i) {
			addRecipe(new ItemStack(NCBlocks.fission_reflector, 1, i), fission_reflector_efficiency[i], fission_reflector_reflectivity[i]);
		}
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>();
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 0D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 0D);
		return fixed;
	}
}
