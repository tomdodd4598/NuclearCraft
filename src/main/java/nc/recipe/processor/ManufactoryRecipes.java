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
	
	public ManufactoryRecipes() {
		super("manufactory", 1, 0, 1, 0, false);
	}

	@Override
	public void addRecipes() {
		if (NCConfig.ore_processing) addOreProcessingRecipes();
		
		addRecipe("gemCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("dustCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("dustGraphite", "dustCoal", NCConfig.processor_time[0]);
		
		addRecipe("gemDiamond", "dustDiamond", NCConfig.processor_time[0]*2);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", NCConfig.processor_time[0]*2);
		addRecipe("gemQuartz", "dustQuartz", NCConfig.processor_time[0]);
		addRecipe("gemBoronNitride", "dustBoronNitride", NCConfig.processor_time[0]*2);
		addRecipe("gemFluorite", "dustFluorite", NCConfig.processor_time[0]*2);
		
		addRecipe("obsidian", oreStack("dustObsidian", 4), NCConfig.processor_time[0]*2);
		addRecipe(oreStack("sand", 4), "itemSilicon", NCConfig.processor_time[0]);
		addRecipe("cobblestone", Blocks.SAND, NCConfig.processor_time[0]);
		addRecipe(new ItemStack(Items.ROTTEN_FLESH, 4), Items.LEATHER, NCConfig.processor_time[0]/2);
		addRecipe(new ItemStack(Items.REEDS, 4), "bioplastic", NCConfig.processor_time[0]);
		
		// Immersive Engineering
		addRecipe(oreStack("dustCoke", 8), "dustHOPGraphite", NCConfig.processor_time[0]*4);
		
		// IC2
		addRecipe(Blocks.CLAY, oreStack("dustClay", 2), NCConfig.processor_time[0]);
		addRecipe("stone", oreStack("dustStone", 2), NCConfig.processor_time[0]);
		addRecipe("sandstone", oreStack("dustSaltpeter", 2), NCConfig.processor_time[0]);
		
		// Advanced Rocketry
		addRecipe("oreDilithium", oreStack("dustDilithium", 2), NCConfig.processor_time[0]);
		addRecipe("ingotDilithium", "dustDilithium", NCConfig.processor_time[0]);
		addRecipe("oreTitanium", oreStack("dustTitanium", 2), NCConfig.processor_time[0]);
		addRecipe("ingotTitanium", "dustTitanium", NCConfig.processor_time[0]);
		
		// AE2
		addRecipe(Items.ENDER_PEARL, "dustEnder", NCConfig.processor_time[0]);
	}
	
	public void addOreProcessingRecipes() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		for (String ore : oreList) {
			if (ore.startsWith("ore")) {
				String dust = "dust" + ore.substring(3);
				String ingot = "ingot" + ore.substring(3);
				if (oreList.contains(dust) && oreList.contains(ingot)) {
					addRecipe(ore, oreStack(dust, 2), NCConfig.processor_time[0]);
					addRecipe(ingot, dust, NCConfig.processor_time[0]);
				}
			}
		}
	}
}
