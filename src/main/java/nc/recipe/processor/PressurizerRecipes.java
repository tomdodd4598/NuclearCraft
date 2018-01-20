package nc.recipe.processor;

import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public class PressurizerRecipes extends BaseRecipeHandler {
	
	public PressurizerRecipes() {
		super("pressurizer", 1, 0, 1, 0, false);
	}

	@Override
	public void addRecipes() {
		addPlatePressingRecipes();
		
		addRecipe("dustGraphite", "gemCoal", NCConfig.processor_time[11]);
		addRecipe(oreStack("ingotGraphite", 64), "gemDiamond", NCConfig.processor_time[11]*4);
		addRecipe("dustDiamond", "gemDiamond", NCConfig.processor_time[11]);
		addRecipe("dustRhodochrosite", "gemRhodochrosite", NCConfig.processor_time[11]);
		addRecipe("dustQuartz", "gemQuartz", NCConfig.processor_time[11]);
		addRecipe(oreStack("dustObsidian", 4), Blocks.OBSIDIAN, NCConfig.processor_time[11]*2);
		addRecipe("dustBoronNitride", "gemBoronNitride", NCConfig.processor_time[11]);
		addRecipe("dustFluorite", "gemFluorite", NCConfig.processor_time[11]);
		
		// IC2
		addRecipe(oreStack("dustClay", 4), "dustSiliconDioxide", NCConfig.processor_time[11]);
		
		// AE2
		addRecipe("dustEnder", Items.ENDER_PEARL, NCConfig.processor_time[11]);
	}
	
	public void addPlatePressingRecipes() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		for (String ore : oreList) {
			if (ore.startsWith("plate")) {
				String ingot = "ingot" + ore.substring(5);
				String gem = "gem" + ore.substring(5);
				if (oreList.contains(ingot)) {
					addRecipe(ingot, ore, NCConfig.processor_time[11]);
				} else if (oreList.contains(gem)) {
					addRecipe(gem, ore, NCConfig.processor_time[11]);
				}
			}
			if (ore.startsWith("plateDense")) {
				String plate = "plate" + ore.substring(10);
				if (oreList.contains(plate)) {
					addRecipe(oreStack(plate, 9), ore, NCConfig.processor_time[11]*4);
				}
			}
		}
	}
}
