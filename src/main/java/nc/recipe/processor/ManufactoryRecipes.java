package nc.recipe.processor;

import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ManufactoryRecipes extends BaseRecipeHandler {
	
	//private static final ManufactoryRecipes RECIPES = new ManufactoryRecipes();
	
	public ManufactoryRecipes() {
		super(1, 0, 1, 0, false);
	}

	/*public static final ManufactoryRecipes instance() {
		return RECIPES;
	}*/

	public void addRecipes() {
		if (NCConfig.ore_processing) oreProcess();
		
		addRecipe("gemCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("dustCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("dustGraphite", "dustCoal", NCConfig.processor_time[0]);
		addRecipe("gemDiamond", "dustDiamond", NCConfig.processor_time[0]*2);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", NCConfig.processor_time[0]*2);
		addRecipe("gemQuartz", "dustQuartz", NCConfig.processor_time[0]);
		addRecipe("obsidian", oreStack("dustObsidian", 4), NCConfig.processor_time[0]*2);
		addRecipe(new ItemStack(Blocks.SAND, 4), "itemSilicon", NCConfig.processor_time[0]);
		addRecipe(new ItemStack(Items.ROTTEN_FLESH, 4), Items.LEATHER, NCConfig.processor_time[0]/2);
		addRecipe(new ItemStack(Items.REEDS, 4), "bioplastic", NCConfig.processor_time[0]);
		addRecipe("gemBoronNitride", "dustBoronNitride", NCConfig.processor_time[0]*2);
		addRecipe("gemFluorite", "dustFluorite", NCConfig.processor_time[0]*2);
	}
	
	public void oreProcess() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		for (String ore : oreList) {
			if (ore.startsWith("ore")) {
				String dust = "dust" + ore.substring(3);
				String ingot = "ingot" + ore.substring(3);
				if (oreList.contains(dust) && oreList.contains(ingot)) {
					addRecipe(oreStack(ore, 1), oreStack(dust, 2), NCConfig.processor_time[0]);
				}
			}
		}
	}

	public String getRecipeName() {
		return "manufactory";
	}
}
