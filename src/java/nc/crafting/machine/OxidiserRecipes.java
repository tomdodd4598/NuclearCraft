package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class OxidiserRecipes extends NCRecipeHelper {

	private static final OxidiserRecipes recipes = new OxidiserRecipes();

	public OxidiserRecipes(){
		super(2, 2);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		matMat(8, 4, 53);
		matMat(8, 19, 54);
		matMat(8, 5, 126);
		matMat(8, 20, 127);
		
		for (int i = 0; i < 7; i++) matMat(4, 24+(2*i), 55+(2*i));
		for (int i = 0; i < 7; i++) matMat(1, 25+(2*i), 56+(2*i));
		
		for (int i = 0; i < 2; i++) matMat(4, 38+(2*i), 82+(2*i));
		for (int i = 0; i < 2; i++) matMat(1, 39+(2*i), 83+(2*i));
		
		for (int i = 0; i < 9; i++) matMat(4, 86+(2*i), 104+(2*i));
		for (int i = 0; i < 9; i++) matMat(1, 87+(2*i), 105+(2*i));
		
		matMat(4, 122, 124);
		matMat(1, 123, 125);
		
		for (int i = 0; i < 4; i++) fuelFuel(32, i, 51+i);
		for (int i = 0; i < 4; i++) fuelFuel(32, 6+i, 55+i);
		for (int i = 0; i < 4; i++) fuelFuel(32, 11+i, 59+i);
		for (int i = 0; i < 4; i++) fuelFuel(32, 17+i, 63+i);
		for (int i = 0; i < 4; i++) fuelFuel(20, 22+i, 67+i);
		for (int i = 0; i < 4; i++) fuelFuel(20, 28+i, 71+i);
		
		fuelFuel(32, 5, 76);
		fuelFuel(32, 16, 77);
		fuelFuel(20, 27, 78);
		
		for (int i = 0; i < 10; i++) fuelFuel(32, 79+i, 89+i);
		for (int i = 0; i < 10; i++) fuelFuel(32, 99+i, 109+i);
		for (int i = 0; i < 10; i++) fuelFuel(20, 119+i, 129+i);
		
		oreDict(8, "dustManganese", "dustManganeseOxide");
		oreDict(8, "dustManganeseOxide", "dustManganeseDioxide");
	}
	
	public void fuelFuel(int oxyNum, int metaIn, int metaOut) {
		addRecipe(new ItemStack(NCItems.fuel, 1, metaIn), new ItemStack(NCItems.fuel, oxyNum, 35), new ItemStack(NCItems.fuel, 1, metaOut), new ItemStack(NCItems.fuel, oxyNum, 45));
	}
	
	public void matMat(int oxyNum, int metaIn, int metaOut) {
		addRecipe(new ItemStack(NCItems.material, 1, metaIn), new ItemStack(NCItems.fuel, oxyNum, 35), new ItemStack(NCItems.material, 1, metaOut), new ItemStack(NCItems.fuel, oxyNum, 45));
	}
	
	public void oreDict(int oxyNum, String in, String out) {
		addRecipe(oreStack(in, 1), new ItemStack(NCItems.fuel, oxyNum, 35), oreStack(out, 1), new ItemStack(NCItems.fuel, oxyNum, 45));
	}
}