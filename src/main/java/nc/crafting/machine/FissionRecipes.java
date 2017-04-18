package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;

public class FissionRecipes extends NCRecipeHelper {

	private static final FissionRecipes recipes = new FissionRecipes();

	public FissionRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		deplete2("LEU235");
		deplete2("HEU235");
		deplete2("LEP239");
		deplete2("HEP239");
		deplete("MOX239");
		deplete2("TBU");
		deplete2("LEU233");
		deplete2("HEU233");
		deplete2("LEP241");
		deplete2("HEP241");
		deplete("MOX241");
		
		deplete2("LEN236");
		deplete2("HEN236");
		deplete2("LEA242");
		deplete2("HEA242");
		deplete2("LEC243");
		deplete2("HEC243");
		deplete2("LEC245");
		deplete2("HEC245");
		deplete2("LEC247");
		deplete2("HEC247");
	}
	
	public void deplete(String fuel) {
		addRecipe(fuel + "Cell", "d" + fuel + "Cell");
	}
	
	public void deplete2(String fuel) {
		addRecipe(fuel + "Cell", "d" + fuel + "Cell");
		addRecipe(fuel + "CellOxide", "d" + fuel + "CellOxide");
	}
}