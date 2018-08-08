package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.OreDictHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ManufactoryRecipes extends ProcessorRecipeHandler {
	
	public ManufactoryRecipes() {
		super("manufactory", 1, 0, 1, 0);
	}

	@Override
	public void addRecipes() {
		addRecipe("gemCoal", "dustCoal", 0.5D, 1D);
		addRecipe("dustCoal", "dustGraphite", 0.25D, 0.5D);
		
		addRecipe("gemDiamond", "dustDiamond", 1.5D, 1.5D);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", 1.5D, 1.5D);
		addRecipe("gemQuartz", "dustQuartz", 1D, 1D);
		addRecipe("gemBoronNitride", "dustBoronNitride", 1.5D, 1.5D);
		addRecipe("gemFluorite", "dustFluorite", 1.5D, 1.5D);
		addRecipe("gemVilliaumite", "dustVilliaumite", 1.5D, 1.5D);
		addRecipe("gemCarobbiite", "dustCarobbiite", 1.5D, 1.5D);
		
		addRecipe("dustVilliaumite", "dustSodiumFluoride", 1D, 1D);
		addRecipe("dustCarobbiite", "dustPotassiumFluoride", 1D, 1D);
		
		addRecipe("obsidian", oreStack("dustObsidian", 4), 2D, 1.5D);
		addRecipe(oreStack("sand", 4), "itemSilicon", 1D, 0.5D);
		addRecipe("cobblestone", Blocks.SAND, 1D, 1D);
		addRecipe("gravel", Items.FLINT, 1D, 1D);
		addRecipe(new ItemStack(Items.ROTTEN_FLESH, 4), Items.LEATHER, 0.5D, 1D);
		addRecipe(new ItemStack(Items.REEDS, 2), "bioplastic", 1D, 0.5D);
		addRecipe("cropWheat", "dustWheat", 0.25D, 0.5D);
		addRecipe("cropBarley", "dustBarley", 0.25D, 0.5D);
		
		// Immersive Engineering
		addRecipe(oreStack("dustCoke", 8), "dustHOPGraphite", 2D, 2D);
		
		// IC2
		addRecipe(Blocks.CLAY, oreStack("dustClay", 2), 1D, 0.5D);
		addRecipe("stone", oreStack("dustStone", 2), 1D, 1D);
		addRecipe("sandstone", oreStack("dustSaltpeter", 2), 1D, 1D);
		
		// Advanced Rocketry
		addRecipe("oreDilithium", oreStack("dustDilithium", 2), 2D, 1D);
		addRecipe("ingotDilithium", "dustDilithium", 1D, 1D);
		
		// AE2
		addRecipe(Items.ENDER_PEARL, "dustEnder", 1D, 1D);
		
		if (NCConfig.ore_processing) addOreProcessingRecipes();
	}
	
	public void addOreProcessingRecipes() {
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.startsWith("ore")) {
				String dust = "dust" + ore.substring(3);
				String ingot = "ingot" + ore.substring(3);
				if (OreDictHelper.oreExists(dust) && OreDictHelper.oreExists(ingot)) {
					addRecipe(ore, oreStack(dust, 2), 1D, 1D);
					addRecipe(ingot, dust, 1D, 1D);
				}
			}
		}
	}
}
