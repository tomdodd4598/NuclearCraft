package nc.crafting.processor;

import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;
import net.minecraftforge.oredict.OreDictionary;

public class ManufactoryRecipes extends ProcessorRecipeHandler {
	
	private static final ManufactoryRecipes RECIPES = new ManufactoryRecipes();

	public ManufactoryRecipes() {
		super(1, 0, 1, 0, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		oreProcess();
		
		addRecipe("gemCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("dustCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("gemDiamond", "dustDiamond", NCConfig.processor_time[0]*2);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", NCConfig.processor_time[0]*2);
		addRecipe("gemQuartz", "dustQuartz", NCConfig.processor_time[0]);
		addRecipe("blockObsidian", oreStack("dustObsidian", 4), NCConfig.processor_time[0]*2);
		addRecipe(oreStack("blockSand", 4), "itemSilicon", NCConfig.processor_time[0]);
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
}
