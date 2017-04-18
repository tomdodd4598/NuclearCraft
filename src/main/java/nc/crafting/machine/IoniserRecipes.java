package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class IoniserRecipes extends NCRecipeHelper {

	private static final IoniserRecipes recipes = new IoniserRecipes();

	public IoniserRecipes(){
		super(2, 2);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		fuelFuel(2, 48, 4, 36, 1, 49, 1, 50);
	}
	
	public void fuelFuel(int noIn, int metaIn, int noIn2, int metaIn2, int noOut, int metaOut, int noOut2, int metaOut2) {
		addRecipe(new ItemStack(NCItems.fuel, noIn, metaIn), new ItemStack(NCItems.fuel, noIn2, metaIn2), new ItemStack(NCItems.fuel, noOut, metaOut), new ItemStack(NCItems.fuel, noOut2, metaOut2));
	}
}