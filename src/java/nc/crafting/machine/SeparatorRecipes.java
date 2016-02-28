package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class SeparatorRecipes extends NCRecipeHelper {

	private static final SeparatorRecipes recipes = new SeparatorRecipes();

	public SeparatorRecipes(){
		super(1, 2);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(new ItemStack(NCItems.fuel, 1, 0), new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 26));
		addRecipe(new ItemStack(NCItems.fuel, 1, 6), new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 28));
		addRecipe(new ItemStack(NCItems.fuel, 1, 1), new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 26));
		addRecipe(new ItemStack(NCItems.fuel, 1, 7), new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 28));
		addRecipe(new ItemStack(NCItems.fuel, 1, 2), new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 32));
		addRecipe(new ItemStack(NCItems.fuel, 1, 8), new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 36));
		addRecipe(new ItemStack(NCItems.fuel, 1, 3), new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 32));
		addRecipe(new ItemStack(NCItems.fuel, 1, 9), new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 36));
		addRecipe(new ItemStack(NCItems.fuel, 1, 4), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 63));
		addRecipe(new ItemStack(NCItems.fuel, 1, 10), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 67));
		addRecipe(new ItemStack(NCItems.fuel, 1, 5), new ItemStack(NCItems.material, 5, 38), new ItemStack(NCItems.material, 4, 38));
		
		addRecipe(new ItemStack(NCItems.fuel, 1, 11), new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 26));
		addRecipe(new ItemStack(NCItems.fuel, 1, 17), new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 28));
		addRecipe(new ItemStack(NCItems.fuel, 1, 12), new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 26));
		addRecipe(new ItemStack(NCItems.fuel, 1, 18), new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 28));
		addRecipe(new ItemStack(NCItems.fuel, 1, 13), new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 32));
		addRecipe(new ItemStack(NCItems.fuel, 1, 19), new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 36));
		addRecipe(new ItemStack(NCItems.fuel, 1, 14), new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 32));
		addRecipe(new ItemStack(NCItems.fuel, 1, 20), new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 36));
		addRecipe(new ItemStack(NCItems.fuel, 1, 15), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 63));
		addRecipe(new ItemStack(NCItems.fuel, 1, 21), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 67));
		addRecipe(new ItemStack(NCItems.fuel, 1, 16), new ItemStack(NCItems.material, 5, 38), new ItemStack(NCItems.material, 4, 38));
		
		addRecipe(new ItemStack(NCItems.fuel, 1, 22), new ItemStack(NCItems.material, 32, 25), new ItemStack(NCItems.material, 4, 33));
		addRecipe(new ItemStack(NCItems.fuel, 1, 28), new ItemStack(NCItems.material, 28, 25), new ItemStack(NCItems.material, 8, 33));
		addRecipe(new ItemStack(NCItems.fuel, 1, 23), new ItemStack(NCItems.material, 20, 35), new ItemStack(NCItems.material, 16, 25));
		addRecipe(new ItemStack(NCItems.fuel, 1, 29), new ItemStack(NCItems.material, 24, 35), new ItemStack(NCItems.material, 12, 25));
		addRecipe(new ItemStack(NCItems.fuel, 1, 24), new ItemStack(NCItems.material, 28, 35), new ItemStack(NCItems.material, 8, 37));
		addRecipe(new ItemStack(NCItems.fuel, 1, 30), new ItemStack(NCItems.material, 24, 35), new ItemStack(NCItems.material, 12, 33));
		addRecipe(new ItemStack(NCItems.fuel, 1, 25), new ItemStack(NCItems.material, 32, 35), new ItemStack(NCItems.material, 4, 31));
		addRecipe(new ItemStack(NCItems.fuel, 1, 31), new ItemStack(NCItems.material, 28, 35), new ItemStack(NCItems.material, 8, 31));
		addRecipe(new ItemStack(NCItems.fuel, 1, 26), new ItemStack(NCItems.material, 20, 64), new ItemStack(NCItems.material, 16, 68));
		addRecipe(new ItemStack(NCItems.fuel, 1, 32), new ItemStack(NCItems.material, 20, 64), new ItemStack(NCItems.material, 16, 66));
		addRecipe(new ItemStack(NCItems.fuel, 1, 27), new ItemStack(NCItems.material, 24, 29), new ItemStack(NCItems.material, 12, 39));
		
		addRecipe(new ItemStack(NCItems.fuel, 1, 51), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 57));
		addRecipe(new ItemStack(NCItems.fuel, 1, 55), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 59));
		addRecipe(new ItemStack(NCItems.fuel, 1, 52), new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 57));
		addRecipe(new ItemStack(NCItems.fuel, 1, 56), new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 59));
		addRecipe(new ItemStack(NCItems.fuel, 1, 53), new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 63));
		addRecipe(new ItemStack(NCItems.fuel, 1, 57), new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 67));
		addRecipe(new ItemStack(NCItems.fuel, 1, 54), new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 63));
		addRecipe(new ItemStack(NCItems.fuel, 1, 58), new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 67));
		
		addRecipe(new ItemStack(NCItems.fuel, 1, 59), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 57));
		addRecipe(new ItemStack(NCItems.fuel, 1, 63), new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 59));
		addRecipe(new ItemStack(NCItems.fuel, 1, 60), new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 57));
		addRecipe(new ItemStack(NCItems.fuel, 1, 64), new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 59));
		addRecipe(new ItemStack(NCItems.fuel, 1, 61), new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 63));
		addRecipe(new ItemStack(NCItems.fuel, 1, 65), new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 67));
		addRecipe(new ItemStack(NCItems.fuel, 1, 62), new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 63));
		addRecipe(new ItemStack(NCItems.fuel, 1, 66), new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 67));
		
		addRecipe(new ItemStack(NCItems.fuel, 1, 67), new ItemStack(NCItems.material, 32, 56), new ItemStack(NCItems.material, 4, 64));
		addRecipe(new ItemStack(NCItems.fuel, 1, 71), new ItemStack(NCItems.material, 28, 56), new ItemStack(NCItems.material, 8, 64));
		addRecipe(new ItemStack(NCItems.fuel, 1, 68), new ItemStack(NCItems.material, 20, 66), new ItemStack(NCItems.material, 16, 56));
		addRecipe(new ItemStack(NCItems.fuel, 1, 72), new ItemStack(NCItems.material, 24, 66), new ItemStack(NCItems.material, 12, 56));
		addRecipe(new ItemStack(NCItems.fuel, 1, 69), new ItemStack(NCItems.material, 28, 66), new ItemStack(NCItems.material, 8, 68));
		addRecipe(new ItemStack(NCItems.fuel, 1, 73), new ItemStack(NCItems.material, 24, 66), new ItemStack(NCItems.material, 12, 64));
		addRecipe(new ItemStack(NCItems.fuel, 1, 70), new ItemStack(NCItems.material, 32, 66), new ItemStack(NCItems.material, 4, 62));
		addRecipe(new ItemStack(NCItems.fuel, 1, 74), new ItemStack(NCItems.material, 28, 66), new ItemStack(NCItems.material, 8, 62));
		
		addRecipe(oreStack("dustUranium", 1), new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));
		addRecipe(oreStack("dustYellorium", 1), new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));
		addRecipe(oreStack("dustYellorite", 1), new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));
		addRecipe(oreStack("dustThorium", 1), new ItemStack(NCItems.material, 2, 38), new ItemStack(NCItems.material, 1, 41));
		addRecipe(oreStack("ingotUranium", 1), new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));
		addRecipe(oreStack("ingotYellorium", 1), new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));
		addRecipe(oreStack("ingotYellorite", 1), new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));
		addRecipe(oreStack("ingotThorium", 1), new ItemStack(NCItems.material, 2, 38), new ItemStack(NCItems.material, 1, 41));
		
		addRecipe(oreStack("dustUraniumOxide", 1), new ItemStack(NCItems.material, 2, 55), new ItemStack(NCItems.material, 2, 58));
		addRecipe(oreStack("ingotUraniumOxide", 1), new ItemStack(NCItems.material, 2, 55), new ItemStack(NCItems.material, 2, 58));
		
		addRecipe(oreStack("dustLithium", 1), new ItemStack(NCItems.material, 1, 47), new ItemStack(NCItems.material, 1, 69));
		addRecipe(oreStack("dustBoron", 1), new ItemStack(NCItems.material, 1, 49), new ItemStack(NCItems.material, 3, 70));
		addRecipe(oreStack("ingotLithium", 1), new ItemStack(NCItems.material, 1, 47), new ItemStack(NCItems.material, 1, 69));
		addRecipe(oreStack("ingotBoron", 1), new ItemStack(NCItems.material, 1, 49), new ItemStack(NCItems.material, 3, 70));
		
		//addRecipe(new ItemStack(Items.record_11), new ItemStack(NCItems.dominoes, 1), new ItemStack(NCItems.boiledEgg, 1));
	}
}
