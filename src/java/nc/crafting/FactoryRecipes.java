package nc.crafting;

import nc.item.NCItems;
import net.minecraft.item.ItemStack;

public class FactoryRecipes extends NCRecipeHelper {

	private static final FactoryRecipes recipes = new FactoryRecipes();

	public FactoryRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
    	addRecipe(oreStack("ingotIron", 4), new ItemStack(NCItems.parts, 1, 10));
    	addRecipe(oreStack("ingotGold", 1), new ItemStack(NCItems.parts, 1, 11));
    	addRecipe(oreStack("ingotCopper", 2), new ItemStack(NCItems.parts, 1, 12));
    	addRecipe(oreStack("ingotTin", 2), new ItemStack(NCItems.parts, 1, 13));
    	addRecipe(oreStack("ingotLead", 1), new ItemStack(NCItems.parts, 1, 14));
    	addRecipe(oreStack("ingotSilver", 2), new ItemStack(NCItems.parts, 1, 15));
    	addRecipe(oreStack("ingotBronze", 1), new ItemStack(NCItems.parts, 1, 16));
    	addRecipe(oreStack("ingotMagnesiumDiboride", 2), new ItemStack(NCItems.parts, 1, 17));
    	
    	oreIngot("Iron", 2);
    	oreIngot("Gold", 2);
    	oreIngot("Copper", 2);
    	oreIngot("Lead", 2);
    	oreIngot("Tin", 2);
    	oreIngot("Silver", 2);
    	oreIngot("Lead", 2);
    	oreIngot("Uranium", 2);
    	addRecipe(oreStack("oreYellorite", 1), oreStack("ingotUranium", 2));
    	addRecipe(oreStack("oreYellorium", 1), oreStack("ingotUranium", 2));
    	oreIngot("Thorium", 2);
    	oreIngot("Lithium", 2);
    	oreIngot("Boron", 2);
    	oreIngot("Aluminium", 2);
    	oreIngot("Aluminum", 2);
    	oreIngot("Zinc", 2);
    	oreIngot("Platinum", 2);
    	oreIngot("Shiny", 2);
    	oreIngot("Osmium", 2);
    	oreIngot("Titanium", 2);
    	oreIngot("Desh", 2);
    	oreIngot("Nickel", 2);
    	oreIngot("ManaInfused", 2);
    	oreIngot("Magnesium", 2);
    }
	
	public void oreIngot(String type, int amount) {
		addRecipe(oreStack("ore" + type, 1), oreStack("ingot" + type, amount));
	}
}