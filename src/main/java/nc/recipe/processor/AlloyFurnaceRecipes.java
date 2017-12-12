package nc.recipe.processor;

import nc.config.NCConfig;
import nc.init.NCItems;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AlloyFurnaceRecipes extends BaseRecipeHandler {
	
	//private static final AlloyFurnaceRecipes RECIPES = new AlloyFurnaceRecipes();
	
	public AlloyFurnaceRecipes() {
		super(2, 0, 1, 0, true);
	}

	/*public static final AlloyFurnaceRecipes instance() {
		return RECIPES;
	}*/

	public void addRecipes() {
		addRecipe(oreStack("ingotCopper", 3), "ingotTin", new ItemStack(NCItems.alloy, 4, 0), NCConfig.processor_time[4]);
		addRecipe("ingotIron", oreStack("ingotGraphite", 2), new ItemStack(NCItems.alloy, 1, 5), NCConfig.processor_time[4]);
		addRecipe("ingotSteel", "ingotBoron", new ItemStack(NCItems.alloy, 2, 6), NCConfig.processor_time[4]);
		addRecipe("ingotFerroboron", "ingotLithium", new ItemStack(NCItems.alloy, 2, 1), NCConfig.processor_time[4]*2);
		addRecipe(oreStack("ingotGraphite", 2), "dustDiamond", new ItemStack(NCItems.alloy, 2, 2), NCConfig.processor_time[4]*2);
		addRecipe("ingotMagnesium", oreStack("ingotBoron", 2), new ItemStack(NCItems.alloy, 3, 3), NCConfig.processor_time[4]);
		addRecipe("ingotLithium", "ingotManganeseDioxide", new ItemStack(NCItems.alloy, 2, 4), NCConfig.processor_time[4]*2);
		
		// Tinkers' Construct
		addRecipe(oreStack("ingotAluminum", 3), "ingotCopper", oreStack("ingotAluminumBrass", 4), NCConfig.processor_time[4]);
		addRecipe(oreStack("ingotCobalt", 2), oreStack("ingotArdite", 2), "ingotManyullyn", NCConfig.processor_time[4]*4);
		
		// Thermal Foundation
		addRecipe("ingotGold", "ingotSilver", oreStack("ingotElectrum", 2), NCConfig.processor_time[4]);
		addRecipe(oreStack("ingotIron", 2), "dustNickel", oreStack("dustInvar", 3), NCConfig.processor_time[4]);
		
		// EnderIO
		addRecipe("ingotSteel", "itemSilicon", "ingotElectricalSteel", NCConfig.processor_time[4]*2);
		addRecipe("ingotEnergeticAlloy", Items.ENDER_PEARL, "ingotPhasedGold", NCConfig.processor_time[4]*4);
		addRecipe("itemSilicon", "dustRedstone", "ingotRedstoneAlloy", NCConfig.processor_time[4]);
		addRecipe("ingotIron", "dustRedstone", "ingotConductiveIron", NCConfig.processor_time[4]);
		addRecipe("ingotIron", Items.ENDER_PEARL, "ingotPhasedIron", NCConfig.processor_time[4]*2);
		addRecipe("ingotSteel", "dustObsidian", "ingotDarkSteel", NCConfig.processor_time[4]*4);
		addRecipe("ingotGold", Blocks.SOUL_SAND, "ingotSoularium", NCConfig.processor_time[4]*2);
		
		// Flaxbeard's Steam Power Mod
		addRecipe(oreStack("ingotCopper", 3), "ingotZinc", oreStack("ingotBrass", 4), NCConfig.processor_time[4]);
	}

	public String getRecipeName() {
		return "alloy_furnace";
	}
}
