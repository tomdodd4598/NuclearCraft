package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;

public class SeparatorRecipes extends NCRecipeHelper {

	private static final SeparatorRecipes recipes = new SeparatorRecipes();

	public SeparatorRecipes(){
		super(1, 2);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		fuel("U235", "U238", "U235");
		fuel("P239", "Pu242", "Pu239");
		fuel("U233", "U238", "U233");
		fuel("P241", "Pu242", "Pu241");
		
		recipe("MOX239", "U238Oxide", 8, "Pu239Oxide", 1);
		recipe("MOX239Cell", "U238Oxide", 8, "Pu239Oxide", 1);
		recipe("MOX241", "U238Oxide", 8, "Pu241Oxide", 1);
		recipe("MOX241Cell", "U238Oxide", 8, "Pu241Oxide", 1);
		recipe("TBU", "Th232Base", 5, "Th232Base", 4);
		recipe("TBUCell", "Th232Base", 5, "Th232Base", 4);
		recipe("TBUOxide", "Th232Oxide", 5, "Th232Oxide", 4);
		recipe("TBUCellOxide", "Th232Oxide", 5, "Th232Oxide", 4);
		
		fuel("N236", "Np237", "Np236");
		fuel("A242", "Am243", "Am242");
		fuel("C243", "Cm246", "Cm243");
		fuel("C245", "Cm246", "Cm245");
		fuel("C247", "Cm246", "Cm247");
		
		recipe("ingotUranium", "U238Base", 2, "tinyU235Base", 2);
		recipe("dustUranium", "U238Base", 2, "tinyU235Base", 2);
		
		recipe("ingotYellorium", "U238Base", "tinyU235Base");
		recipe("dustYellorium", "U238Base", "tinyU235Base");
		recipe("ingotYellorite", "U238Base", "tinyU235Base");
		recipe("dustYellorite", "U238Base", "tinyU235Base");
		
		recipe("ingotThorium", "Th232Base", 2, "tinyTh230Base", 2);
		recipe("dustThorium", "Th232Base", 2, "tinyTh230Base", 2);
		
		recipe("ingotUraniumOxide", "U238Oxide", 2, "tinyU235Oxide", 2);
		recipe("dustUraniumOxide", "U238Oxide", 2, "tinyU235Oxide", 2);
		
		recipe("ingotThoriumOxide", "Th232Oxide", 2, "tinyTh230Oxide", 2);
		recipe("dustThoriumOxide", "Th232Oxide", 2, "tinyTh230Oxide", 2);
		
		recipe("ingotLithium", "Li7", "tinyLi6", 2);
		recipe("dustLithium", "Li7", "tinyLi6", 2);
		
		recipe("ingotBoron", "B11", "tinyB10", 3);
		recipe("dustBoron", "B11", "tinyB10", 3);
	}
	
	public void recipe(String in, String out1, int num1, String out2, int num2) {
		addRecipe(in, oreStack(out1, num1), oreStack(out2, num2));
	}
	
	public void recipe(String in, String out1, String out2, int num2) {
		addRecipe(in, out1, oreStack(out2, num2));
	}
	
	public void recipe(String in, String out1, int num1, String out2) {
		addRecipe(in, oreStack(out1, num1), out2);
	}
	
	public void recipe(String in, String out1, String out2) {
		addRecipe(in, out1, out2);
	}
	
	public void fuel(String fuel, String fertile, String fissile) {
		recipe("LE" + fuel, fertile + "Base", 8, fissile + "Base", 1);
		recipe("HE" + fuel, fertile + "Base", 5, fissile + "Base", 4);
		recipe("LE" + fuel + "Cell", fertile + "Base", 8, fissile + "Base", 1);
		recipe("HE" + fuel + "Cell", fertile + "Base", 5, fissile + "Base", 4);
		
		recipe("LE" + fuel + "Oxide", fertile + "Oxide", 8, fissile + "Oxide", 1);
		recipe("HE" + fuel + "Oxide", fertile + "Oxide", 5, fissile + "Oxide", 4);
		recipe("LE" + fuel + "CellOxide", fertile + "Oxide", 8, fissile + "Oxide", 1);
		recipe("HE" + fuel + "CellOxide", fertile + "Oxide", 5, fissile + "Oxide", 4);
	}
}
