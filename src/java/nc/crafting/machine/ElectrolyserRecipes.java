package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class ElectrolyserRecipes extends NCRecipeHelper {

	private static final ElectrolyserRecipes recipes = new ElectrolyserRecipes();

	public ElectrolyserRecipes(){
		super(1, 4);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		//addRecipe(new ItemStack(NCItems.fuel, 12, 34), new ItemStack(NCItems.fuel, 4, 35), new ItemStack(NCItems.fuel, 4, 36), new ItemStack(NCItems.fuel, 3, 36), new ItemStack(NCItems.fuel, 1, 37));
		addRecipe(new ItemStack(NCItems.fuel, 12, 45), new ItemStack(NCItems.fuel, 4, 35), new ItemStack(NCItems.fuel, 4, 36), new ItemStack(NCItems.fuel, 3, 36), new ItemStack(NCItems.fuel, 1, 37));
	}
}
