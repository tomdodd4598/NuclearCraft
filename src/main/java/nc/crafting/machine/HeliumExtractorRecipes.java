package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class HeliumExtractorRecipes extends NCRecipeHelper {

	private static final HeliumExtractorRecipes recipes = new HeliumExtractorRecipes();

	public HeliumExtractorRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
    	addRecipe(new ItemStack(NCItems.fuel, 1, 75), new ItemStack(NCItems.fuel, 1, 45));
    }
}