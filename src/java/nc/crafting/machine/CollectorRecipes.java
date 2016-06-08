package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class CollectorRecipes extends NCRecipeHelper {

	private static final CollectorRecipes recipes = new CollectorRecipes();

	public CollectorRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(new ItemStack(NCItems.fuel, 1, 45), new ItemStack(NCItems.fuel, 1, 40));
	}
}