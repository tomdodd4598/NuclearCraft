package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;

public class RecyclerRecipes extends NCRecipeHelper {

	private static final RecyclerRecipes recipes = new RecyclerRecipes();

	public RecyclerRecipes(){
		super(1, 4);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		recipe("dLEU235Cell", "tinyU238Base", 24, "tinyNp236Base", 4, "tinyNp237Base", 4, "tinyPu239Base", 8);
		recipe("dHEU235Cell", "tinyU238Base", 16, "tinyNp237Base", 8, "tinyPu239Base", 8, "tinyPu242Base", 8);
		recipe("dLEP239Cell", "tinyPu242Base", 24, "tinyPu241Base", 4, "tinyAm242Base", 4, "tinyAm243Base", 8);
		recipe("dHEP239Cell", "tinyPu242Base", 16, "tinyPu241Base", 8, "tinyAm242Base", 8, "tinyAm243Base", 8);
		recipe("dMOX239Cell", "tinyU238Oxide", 24, "tinyPu239Oxide", 4, "tinyPu241Oxide", 4, "tinyPu242Oxide", 8);
		recipe("dTBUCell", "tinyU233Base", 24, "tinyU235Base", 4, "tinyNp236Base", 4, "tinyNp237Base", 8);
		recipe("dLEU233Cell", "tinyU238Base", 24, "tinyU235Base", 4, "tinyNp236Base", 4, "tinyNp237Base", 8);
		recipe("dHEU233Cell", "tinyU238Base", 16, "tinyNp236Base", 8, "tinyNp237Base", 8, "tinyPu239Base", 8);
		recipe("dLEP241Cell", "tinyPu242Base", 24, "tinyAm241Base", 4, "tinyAm242Base", 4, "tinyCm243Base", 8);
		recipe("dHEP241Cell", "tinyPu242Base", 16, "tinyAm243Base", 8, "tinyCm243Base", 8, "tinyCm246Base", 8);
		recipe("dMOX241Cell", "tinyU238Oxide", 24, "tinyPu239Oxide", 4, "tinyPu241Oxide", 4, "tinyAm243Oxide", 8);
		
		recipe("dLEU235CellOxide", "tinyU238Oxide", 24, "tinyNp236Oxide", 4, "tinyNp237Oxide", 4, "tinyPu239Oxide", 8);
		recipe("dHEU235CellOxide", "tinyU238Oxide", 16, "tinyNp237Oxide", 8, "tinyPu239Oxide", 8, "tinyPu242Oxide", 8);
		recipe("dLEP239CellOxide", "tinyPu242Oxide", 24, "tinyPu241Oxide", 4, "tinyAm242Oxide", 4, "tinyAm243Oxide", 8);
		recipe("dHEP239CellOxide", "tinyPu242Oxide", 16, "tinyPu241Oxide", 8, "tinyAm242Oxide", 8, "tinyAm243Oxide", 8);
		recipe("dTBUCellOxide", "tinyU233Oxide", 24, "tinyU235Oxide", 4, "tinyNp236Oxide", 4, "tinyNp237Oxide", 8);
		recipe("dLEU233CellOxide", "tinyU238Oxide", 24, "tinyU235Oxide", 4, "tinyNp236Oxide", 4, "tinyNp237Oxide", 8);
		recipe("dHEU233CellOxide", "tinyU238Oxide", 16, "tinyNp236Oxide", 8, "tinyNp237Oxide", 8, "tinyPu239Oxide", 8);
		recipe("dLEP241CellOxide", "tinyPu242Oxide", 24, "tinyAm241Oxide", 4, "tinyAm242Oxide", 4, "tinyCm243Oxide", 8);
		recipe("dHEP241CellOxide", "tinyPu242Oxide", 16, "tinyAm243Oxide", 8, "tinyCm243Oxide", 8, "tinyCm246Oxide", 8);

		recipe("dLEN236Cell", "tinyNp237Base", 24, "tinyU238Base", 4, "tinyPu239Base", 4, "tinyPu241Base", 8);
		recipe("dHEN236Cell", "tinyNp237Base", 16, "tinyPu238Base", 8, "tinyPu239Base", 8, "tinyPu241Base", 8);
		recipe("dLEA242Cell", "tinyAm243Base", 24, "tinyCm243Base", 4, "tinyCm246Base", 4, "tinyCm247Base", 8);
		recipe("dHEA242Cell", "tinyAm243Base", 16, "tinyCm243Base", 8, "tinyCm245Base", 8, "tinyCm247Base", 8);
		recipe("dLEC243Cell", "tinyCm246Base", 24, "tinyAm243Base", 4, "tinyCm243Base", 4, "tinyCm245Base", 8);
		recipe("dHEC243Cell", "tinyCm246Base", 16, "tinyAm243Base", 8, "tinyCm243Base", 8, "tinyCm245Base", 8);
		recipe("dLEC245Cell", "tinyCm246Base", 24, "tinyCm245Base", 4, "tinyCm247Base", 4, "tinyCf250Base", 8);
		recipe("dHEC245Cell", "tinyCm246Base", 16, "tinyCm245Base", 8, "tinyCm247Base", 8, "tinyCf250Base", 8);
		recipe("dLEC247Cell", "tinyCm246Base", 24, "tinyCm247Base", 4, "tinyCm247Base", 4, "tinyCf250Base", 8);
		recipe("dHEC247Cell", "tinyCm246Base", 16, "tinyCm247Base", 8, "tinyCm247Base", 8, "tinyCf250Base", 8);
		
		recipe("dLEN236CellOxide", "tinyNp237Oxide", 24, "tinyU238Oxide", 4, "tinyPu239Oxide", 4, "tinyPu241Oxide", 8);
		recipe("dHEN236CellOxide", "tinyNp237Oxide", 16, "tinyPu238Oxide", 8, "tinyPu239Oxide", 8, "tinyPu241Oxide", 8);
		recipe("dLEA242CellOxide", "tinyAm243Oxide", 24, "tinyCm243Oxide", 4, "tinyCm246Oxide", 4, "tinyCm247Oxide", 8);
		recipe("dHEA242CellOxide", "tinyAm243Oxide", 16, "tinyCm243Oxide", 8, "tinyCm245Oxide", 8, "tinyCm247Oxide", 8);
		recipe("dLEC243CellOxide", "tinyCm246Oxide", 24, "tinyAm243Oxide", 4, "tinyCm243Oxide", 4, "tinyCm245Oxide", 8);
		recipe("dHEC243CellOxide", "tinyCm246Oxide", 16, "tinyAm243Oxide", 8, "tinyCm243Oxide", 8, "tinyCm245Oxide", 8);
		recipe("dLEC245CellOxide", "tinyCm246Oxide", 24, "tinyCm245Oxide", 4, "tinyCm247Oxide", 4, "tinyCf250Oxide", 8);
		recipe("dHEC245CellOxide", "tinyCm246Oxide", 16, "tinyCm245Oxide", 8, "tinyCm247Oxide", 8, "tinyCf250Oxide", 8);
		recipe("dLEC247CellOxide", "tinyCm246Oxide", 24, "tinyCm247Oxide", 4, "tinyCm247Oxide", 4, "tinyCf250Oxide", 8);
		recipe("dHEC247CellOxide", "tinyCm246Oxide", 16, "tinyCm247Oxide", 8, "tinyCm247Oxide", 8, "tinyCf250Oxide", 8);
	}
	
	public void recipe(String in, String out1, int num1, String out2, int num2, String out3, int num3, String out4, int num4) {
		addRecipe(in, oreStack(out1, num1), oreStack(out2, num2), oreStack(out3, num3), oreStack(out4, num4));
	}
}
