package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.init.NCBlocks;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.item.ItemStack;

public class FissionReflectorRecipes extends ProcessorRecipeHandler {
	
	public FissionReflectorRecipes() {
		super("fission_reflector", 1, 0, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < MetaEnums.NeutronReflectorType.values().length; i++) {
			addRecipe(new ItemStack(NCBlocks.fission_reflector, 1, i), NCConfig.fission_reflector_efficiency[i], NCConfig.fission_reflector_reflectivity[i]);
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 0D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 0D);
		return fixed;
	}
}
