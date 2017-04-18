package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
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
		addRecipe("U238Base", "Th230Base");
		addRecipe("U235Base", "dustLead");
		addRecipe("U233Base", "dustLead");
		addRecipe("Pu238Base", "Th230Base");
		addRecipe("Pu239Base", "U235Base");
		addRecipe("Pu242Base", "U238Base");
		addRecipe("Pu241Base", "Np237Base");
		addRecipe("Th232Base", "dustLead");
		addRecipe("Th230Base", "dustLead");
		
		addRecipe("tinyU238Base", "tinyTh230Base");
		addRecipe("tinyU235Base", "dustTinyLead");
		addRecipe("tinyU233Base", "dustTinyLead");
		addRecipe("tinyPu238Base", "tinyTh230Base");
		addRecipe("tinyPu239Base", "tinyU235Base");
		addRecipe("tinyPu242Base", "tinyU238Base");
		addRecipe("tinyPu241Base", "tinyNp237Base");
		addRecipe("tinyTh232Base", "dustTinyLead");
		addRecipe("tinyTh230Base", "dustTinyLead");
		
		addRecipe("U238Oxide", "Th230Oxide");
		addRecipe("U235Oxide", "dustLead");
		addRecipe("U233Oxide", "dustLead");
		addRecipe("Pu238Oxide", "Th230Oxide");
		addRecipe("Pu239Oxide", "U235Oxide");
		addRecipe("Pu242Oxide", "U238Oxide");
		addRecipe("Pu241Oxide", "Np237Oxide");
		addRecipe("Th232Oxide", "dustLead");
		addRecipe("Th230Oxide", "dustLead");
		
		addRecipe("tinyU238Oxide", "tinyTh230Oxide");
		addRecipe("tinyU235Oxide", "dustTinyLead");
		addRecipe("tinyU233Oxide", "dustTinyLead");
		addRecipe("tinyPu238Oxide", "tinyTh230Oxide");
		addRecipe("tinyPu239Oxide", "tinyU235Oxide");
		addRecipe("tinyPu242Oxide", "tinyU238Oxide");
		addRecipe("tinyPu241Oxide", "tinyNp237Oxide");
		addRecipe("tinyTh232Oxide", "dustTinyLead");
		addRecipe("tinyTh230Oxide", "dustTinyLead");
    	
		addRecipe("Np236Base", "dustLead");
		addRecipe("Np237Base", "U233Base");
		addRecipe("Am241Base", "Np237Base");
		addRecipe("Am242Base", "Th230Base");
		addRecipe("Am243Base", "Pu239Base");
		addRecipe("Cm243Base", "Pu239Base");
		addRecipe("Cm245Base", "Pu241Base");
		addRecipe("Cm246Base", "Pu242Base");
		addRecipe("Cm247Base", "Am243Base");
		addRecipe("Cf250Base", "Cm246Base");
		
		addRecipe("tinyNp236Base", "dustTinyLead");
		addRecipe("tinyNp237Base", "tinyU233Base");
		addRecipe("tinyAm241Base", "tinyNp237Base");
		addRecipe("tinyAm242Base", "tinyTh230Base");
		addRecipe("tinyAm243Base", "tinyPu239Base");
		addRecipe("tinyCm243Base", "tinyPu239Base");
		addRecipe("tinyCm245Base", "tinyPu241Base");
		addRecipe("tinyCm246Base", "tinyPu242Base");
		addRecipe("tinyCm247Base", "tinyAm243Base");
		addRecipe("tinyCf250Base", "tinyCm246Base");
		
		addRecipe("Np236Oxide", "dustLead");
		addRecipe("Np237Oxide", "U233Oxide");
		addRecipe("Am241Oxide", "Np237Oxide");
		addRecipe("Am242Oxide", "Th230Oxide");
		addRecipe("Am243Oxide", "Pu239Oxide");
		addRecipe("Cm243Oxide", "Pu239Oxide");
		addRecipe("Cm245Oxide", "Pu241Oxide");
		addRecipe("Cm246Oxide", "Pu242Oxide");
		addRecipe("Cm247Oxide", "Am243Oxide");
		addRecipe("Cf250Oxide", "Cm246Oxide");
		
		addRecipe("tinyNp236Oxide", "dustTinyLead");
		addRecipe("tinyNp237Oxide", "tinyU233Oxide");
		addRecipe("tinyAm241Oxide", "tinyNp237Oxide");
		addRecipe("tinyAm242Oxide", "tinyTh230Oxide");
		addRecipe("tinyAm243Oxide", "tinyPu239Oxide");
		addRecipe("tinyCm243Oxide", "tinyPu239Oxide");
		addRecipe("tinyCm245Oxide", "tinyPu241Oxide");
		addRecipe("tinyCm246Oxide", "tinyPu242Oxide");
		addRecipe("tinyCm247Oxide", "tinyAm243Oxide");
		addRecipe("tinyCf250Oxide", "tinyCm246Oxide");
    	
    	addRecipe(new ItemStack(NCItems.fuel, 1, 38), new ItemStack(NCItems.fuel, 1, 39));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 47), new ItemStack(NCItems.fuel, 1, 49));
	}
}