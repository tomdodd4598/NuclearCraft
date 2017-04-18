package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IrradiatorRecipes extends NCRecipeHelper {

	private static final IrradiatorRecipes recipes = new IrradiatorRecipes();

	public IrradiatorRecipes(){
		super(2, 3);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		recipeNeutron(NCItems.fuel, 1, 41, NCItems.fuel, 2, 38, NCItems.fuel, 2, 40);
		//recipeNeutron2(NCItems.fuel, 1, 42, NCItems.fuel, 2, 38, NCItems.fuel, 2, 40);
		recipeNeutron(NCItems.fuel, 1, 43, NCItems.fuel, 2, 38, NCItems.fuel, 4, 40);
	}
	
	public void recipeNeutron(Item In, int noIn, int metaIn, Item Out, int noOut, int metaOut, Item Out2, int noOut2, int metaOut2) {
		addRecipe(new ItemStack(In, noIn, metaIn), new ItemStack(NCItems.fuel, 1, 47), new ItemStack(Out, noOut, metaOut), new ItemStack(Out2, noOut2, metaOut2), new ItemStack(NCItems.fuel, 1, 48));
	}
	
	public void recipeNeutron2(Item In, int noIn, int metaIn, Item Out, int noOut, int metaOut, Item Out2, int noOut2, int metaOut2) {
		addRecipe(new ItemStack(In, noIn, metaIn), new ItemStack(NCItems.fuel, 1, 47), new ItemStack(Out, noOut, metaOut), new ItemStack(Out2, noOut2, metaOut2), new ItemStack(NCItems.fuel, 1, 47));
	}
}