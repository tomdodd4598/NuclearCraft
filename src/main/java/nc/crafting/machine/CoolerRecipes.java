package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class CoolerRecipes extends NCRecipeHelper {

	private static final CoolerRecipes recipes = new CoolerRecipes();

	public CoolerRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(new ItemStack(NCItems.fuel, 1, 40), new ItemStack(NCItems.fuel, 1, 75));
	}
}