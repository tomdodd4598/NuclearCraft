package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class FissionRecipes extends NCRecipeHelper {

	private static final FissionRecipes recipes = new FissionRecipes();

	public FissionRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(new ItemStack(NCItems.fuel, 1, 11), new ItemStack(NCItems.fuel, 1, 22));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 17), new ItemStack(NCItems.fuel, 1, 28));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 12), new ItemStack(NCItems.fuel, 1, 23));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 18), new ItemStack(NCItems.fuel, 1, 29));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 13), new ItemStack(NCItems.fuel, 1, 24));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 19), new ItemStack(NCItems.fuel, 1, 30));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 14), new ItemStack(NCItems.fuel, 1, 25));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 20), new ItemStack(NCItems.fuel, 1, 31));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 15), new ItemStack(NCItems.fuel, 1, 26));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 21), new ItemStack(NCItems.fuel, 1, 32));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 16), new ItemStack(NCItems.fuel, 1, 27));
    	
    	addRecipe(new ItemStack(NCItems.fuel, 1, 59), new ItemStack(NCItems.fuel, 1, 67));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 60), new ItemStack(NCItems.fuel, 1, 68));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 61), new ItemStack(NCItems.fuel, 1, 69));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 62), new ItemStack(NCItems.fuel, 1, 70));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 63), new ItemStack(NCItems.fuel, 1, 71));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 64), new ItemStack(NCItems.fuel, 1, 72));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 65), new ItemStack(NCItems.fuel, 1, 73));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 66), new ItemStack(NCItems.fuel, 1, 74));
	}
}