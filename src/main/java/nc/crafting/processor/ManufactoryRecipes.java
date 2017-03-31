package nc.crafting.processor;

import nc.handlers.ProcessorRecipeHandler;
import net.minecraft.init.Items;

public class ManufactoryRecipes extends ProcessorRecipeHandler {
	
	private static final ManufactoryRecipes RECIPES = new ManufactoryRecipes();

	public ManufactoryRecipes() {
		super(1, 1);
	}
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe("gemCoal", "dustGraphite");
		addRecipe("dustCoal", "dustGraphite");
		addRecipe("gemDiamond", "dustDiamond");
		addRecipe("gemRhodochrosite", "dustRhodochrosite");
		addRecipe("gemQuartz", "dustQuartz");
		addRecipe(oreStack("gemLapis", 3), Items.PRISMARINE_SHARD);
	}
}
