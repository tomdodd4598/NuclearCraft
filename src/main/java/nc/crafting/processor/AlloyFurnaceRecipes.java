package nc.crafting.processor;

import nc.handler.ProcessorRecipeHandler;
import nc.init.NCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AlloyFurnaceRecipes extends ProcessorRecipeHandler {
	
	private static final AlloyFurnaceRecipes RECIPES = new AlloyFurnaceRecipes();

	public AlloyFurnaceRecipes() {
		super(2, 0, 1, 0, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe(oreStack("ingotCopper", 3), "ingotTin", new ItemStack(NCItems.alloy, 4, 0));
		addRecipe("ingotIron", oreStack("ingotGraphite", 2), new ItemStack(NCItems.alloy, 1, 5));
		addRecipe("ingotIron", oreStack("dustGraphite", 2), new ItemStack(NCItems.alloy, 1, 5));
		addRecipe("ingotSteel", "ingotBoron", new ItemStack(NCItems.alloy, 2, 6));
		addRecipe("ingotFerroboron", "ingotLithium", new ItemStack(NCItems.alloy, 2, 1));
		addRecipe(oreStack("ingotGraphite", 2), "dustDiamond", new ItemStack(NCItems.alloy, 2, 2));
		addRecipe(oreStack("dustGraphite", 2), "dustDiamond", new ItemStack(NCItems.alloy, 2, 2));
		addRecipe("ingotMagnesium", oreStack("ingotBoron", 2), new ItemStack(NCItems.alloy, 3, 3));
		addRecipe("ingotLithium", "ingotManganeseDioxide", new ItemStack(NCItems.alloy, 2, 4));
		
		// Tinkers' Construct
		addRecipe(oreStack("ingotAluminum", 3), "ingotCopper", oreStack("ingotAluminumBrass", 4));
		addRecipe(oreStack("ingotCobalt", 2), oreStack("ingotArdite", 2), "ingotManyullyn");
		
		// Thermal Foundation
		addRecipe("ingotGold", "ingotSilver", oreStack("ingotElectrum", 2));
		addRecipe(oreStack("ingotIron", 2), "dustNickel", oreStack("dustInvar", 3));
		
		// EnderIO
		addRecipe("ingotSteel", "itemSilicon", "ingotElectricalSteel");
		addRecipe("ingotEnergeticAlloy", Items.ENDER_PEARL, "ingotPhasedGold");
		addRecipe("itemSilicon", "dustRedstone", "ingotRedstoneAlloy");
		addRecipe("ingotIron", "dustRedstone", "ingotConductiveIron");
		addRecipe("ingotIron", Items.ENDER_PEARL, "ingotPhasedIron");
		addRecipe("ingotSteel", "dustObsidian", "ingotDarkSteel");
		addRecipe("ingotGold", Blocks.SOUL_SAND, "ingotSoularium");
		
		// Project Red
		addRecipe("ingotIron", oreStack("dustElectrotine", 8), "ingotElectrotineAlloy");
		addRecipe("ingotIron", oreStack("dustRedstone", 8), "ingotRedAlloy");
		
		// Flaxbeard's Steam Power Mod
		addRecipe(oreStack("ingotCopper", 3), "ingotZinc", oreStack("ingotBrass", 4));
	}
}
