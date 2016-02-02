package nc.crafting;

import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class HastenerRecipes extends NCRecipeHelper {

	private static final HastenerRecipes recipes = new HastenerRecipes();

	public HastenerRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe("U238", new ItemStack(NCItems.material, 1, 40));
    	addRecipe(new ItemStack(NCItems.material, 1, 26), new ItemStack(NCItems.material, 1, 17));
    	addRecipe(new ItemStack(NCItems.material, 1, 28), new ItemStack(NCItems.material, 1, 17));
    	addRecipe("Pu238", new ItemStack(NCItems.material, 1, 40));
    	addRecipe(new ItemStack(NCItems.material, 1, 32), new ItemStack(NCItems.material, 1, 26));
    	addRecipe(new ItemStack(NCItems.material, 1, 36), new ItemStack(NCItems.material, 1, 28));
    	addRecipe(new ItemStack(NCItems.material, 1, 34), new ItemStack(NCItems.material, 1, 24));
    	addRecipe(new ItemStack(NCItems.material, 1, 40), new ItemStack(NCItems.material, 1, 17));
    	
    	addRecipe("tinyU238", new ItemStack(NCItems.material, 1, 41));
    	addRecipe(new ItemStack(NCItems.material, 1, 27), new ItemStack(NCItems.material, 1, 23));
    	addRecipe(new ItemStack(NCItems.material, 1, 29), new ItemStack(NCItems.material, 1, 23));
    	addRecipe("tinyPu238", new ItemStack(NCItems.material, 1, 41));
    	addRecipe(new ItemStack(NCItems.material, 1, 33), new ItemStack(NCItems.material, 1, 27));
    	addRecipe(new ItemStack(NCItems.material, 1, 37), new ItemStack(NCItems.material, 1, 29));
    	addRecipe(new ItemStack(NCItems.material, 1, 35), new ItemStack(NCItems.material, 1, 25));
    	addRecipe(new ItemStack(NCItems.material, 1, 41), new ItemStack(NCItems.material, 1, 23));
    	
    	addRecipe(new ItemStack(NCItems.material, 1, 57), new ItemStack(NCItems.material, 1, 17));
    	addRecipe(new ItemStack(NCItems.material, 1, 59), new ItemStack(NCItems.material, 1, 17));
    	addRecipe(new ItemStack(NCItems.material, 1, 63), new ItemStack(NCItems.material, 1, 57));
    	addRecipe(new ItemStack(NCItems.material, 1, 67), new ItemStack(NCItems.material, 1, 59));
    	addRecipe(new ItemStack(NCItems.material, 1, 65), new ItemStack(NCItems.material, 1, 55));
    	
    	addRecipe(new ItemStack(NCItems.material, 1, 58), new ItemStack(NCItems.material, 1, 23));
    	addRecipe(new ItemStack(NCItems.material, 1, 60), new ItemStack(NCItems.material, 1, 23));
    	addRecipe(new ItemStack(NCItems.material, 1, 64), new ItemStack(NCItems.material, 1, 58));
    	addRecipe(new ItemStack(NCItems.material, 1, 68), new ItemStack(NCItems.material, 1, 60));
    	addRecipe(new ItemStack(NCItems.material, 1, 66), new ItemStack(NCItems.material, 1, 56));
    	
    	addRecipe(new ItemStack(NCItems.fuel, 1, 38), new ItemStack(NCItems.fuel, 1, 39));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 47), new ItemStack(NCItems.fuel, 1, 49));
	}
}