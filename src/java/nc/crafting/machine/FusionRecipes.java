package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class FusionRecipes extends NCRecipeHelper {

	private static final FusionRecipes recipes = new FusionRecipes();

	public FusionRecipes(){
		super(2, 6);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		recipe(36, 36, 0, 0, 16, 0, 0, 0);
		recipe(36, 37, 0, 0, 0, 0, 16, 0);
		recipe(36, 38, 2, 0, 0, 0, 16, 0);
		recipe(36, 39, 0, 0, 0, 0, 0, 16);
		recipe(36, 44, 0, 0, 0, 0, 0, 48);
		recipe(36, 41, 0, 0, 0, 16, 0, 16);
		recipe(36, 42, 0, 0, 0, 0, 0, 32);
		
		recipe(37, 37, 1, 8, 0, 8, 8, 0);
		recipe(37, 38, 2, 0, 0, 0, 0, 16);
		recipe(37, 39, 0, 16, 0, 0, 0, 16);
		recipe(37, 44, 2, 0, 0, 0, 0, 48);
		recipe(37, 41, 0, 0, 0, 0, 0, 32);
		recipe(37, 42, 2, 0, 0, 0, 0, 32);
		
		recipe(38, 38, 4, 0, 0, 0, 0, 16);
		recipe(38, 39, 2, 16, 0, 0, 0, 16);
		recipe(38, 44, 4, 0, 0, 0, 0, 48);
		recipe(38, 41, 2, 0, 0, 0, 0, 32);
		recipe(38, 42, 4, 0, 0, 0, 0, 32);
		
		recipe(39, 39, 0, 32, 0, 0, 0, 16);
		recipe(39, 44, 0, 0, 16, 0, 0, 48);
		recipe(39, 41, 0, 16, 0, 0, 0, 32);
		recipe(39, 42, 0, 0, 16, 0, 0, 32);
		
		recipe(44, 44, 4, 0, 0, 0, 0, 80);
		recipe(44, 41, 2, 0, 0, 0, 0, 64);
		recipe(44, 42, 4, 0, 0, 0, 0, 64);
		
		recipe(41, 41, 0, 0, 0, 0, 0, 48);
		recipe(41, 42, 2, 0, 0, 0, 0, 48);
		
		recipe(42, 42, 4, 0, 0, 0, 0, 48);
	}
	
	public void recipe(int in1, int in2, int out1, int out2, int out3, int out4, int out5, int out6) {
		addRecipe(new ItemStack(NCItems.fuel, 16, in1), new ItemStack(NCItems.fuel, 16, in2), out1 != 0 ? new ItemStack(NCItems.fuel, out1, 47) : new ItemStack(NCItems.blank, 1), out2 != 0 ? new ItemStack(NCItems.fuel, out2, 36) : new ItemStack(NCItems.blank, 1), out3 != 0 ? new ItemStack(NCItems.fuel, out3, 37) : new ItemStack(NCItems.blank, 1), out4 != 0 ? new ItemStack(NCItems.fuel, out4, 38) : new ItemStack(NCItems.blank, 1), out5 != 0 ? new ItemStack(NCItems.fuel, out5, 39) : new ItemStack(NCItems.blank, 1), out6 != 0 ? new ItemStack(NCItems.fuel, out6, 40) : new ItemStack(NCItems.blank, 1));
	}
}