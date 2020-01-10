package nc.recipe.multiblock;

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
}
