package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.init.NCItems;
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
		addRecipe("coal", "dustCoal", 0.5D, 1D);
		addRecipe("dustCoal", "dustGraphite", 0.25D, 0.5D);
		addRecipe("charcoal", "dustCharcoal", 0.5D, 0.5D);
		
		addRecipe("gemDiamond", "dustDiamond", 1.5D, 1.5D);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", 1.5D, 1.5D);
		addRecipe("gemQuartz", "dustQuartz", 1D, 1D);
		addRecipe("gemBoronNitride", "dustBoronNitride", 1.5D, 1.5D);
		addRecipe("gemFluorite", "dustFluorite", 1.5D, 1.5D);
		addRecipe("gemVilliaumite", "dustVilliaumite", 1.5D, 1.5D);
		addRecipe("gemCarobbiite", "dustCarobbiite", 1.5D, 1.5D);
		
		addRecipe("dustVilliaumite", "dustSodiumFluoride", 1D, 1D);
		addRecipe("dustCarobbiite", "dustPotassiumFluoride", 1D, 1D);
		
		if (OreDictHelper.oreExists("ingotSilicon")) {
			addRecipe(oreStack("sand", 4), "ingotSilicon", 1D, 0.5D);
			addRecipe("ingotSilicon", "itemSilicon", 1D, 0.5D);
		}
		else addRecipe(oreStack("sand", 4), "itemSilicon", 1D, 0.5D);
		
		addRecipe("obsidian", oreStack("dustObsidian", 4), 2D, 1.5D);
		addRecipe("cobblestone", Blocks.SAND, 1D, 1D);
		addRecipe("gravel", Items.FLINT, 1D, 1D);
		addRecipe(Items.BLAZE_ROD, new ItemStack(Items.BLAZE_POWDER, 4), 1D, 1D);
		addRecipe(new ItemStack(Items.ROTTEN_FLESH, 4), Items.LEATHER, 0.5D, 1D);
		addRecipe(new ItemStack(Items.REEDS, 2), "bioplastic", 1D, 0.5D);
		addRecipe("cropWheat", "dustWheat", 0.25D, 0.5D);
		addRecipe("cropBarley", "dustBarley", 0.25D, 0.5D);
		addRecipe("bone", new ItemStack(Items.DYE, 6, 15), 0.5D, 1D);
		addRecipe(NCItems.roasted_cocoa_beans, NCItems.ground_cocoa_nibs, 0.5D, 0.5D);
		addRecipe(Lists.newArrayList(Items.PORKCHOP, Items.FISH, new ItemStack(Items.FISH, 1, 1), new ItemStack(Items.FISH, 1, 2), new ItemStack(Items.FISH, 1, 3)), NCItems.gelatin, 0.5D, 0.5D);
		
		// Immersive Engineering
		addRecipe(oreStack("dustCoke", 8), "dustHOPGraphite", 2D, 2D);
		
		// IC2
		addRecipe(Blocks.CLAY, oreStack("dustClay", 2), 1D, 0.5D);
		addRecipe("stone", oreStack("dustStone", 2), 1D, 1D);
		addRecipe("sandstone", oreStack("dustSaltpeter", 2), 1D, 1D);
		
		// Advanced Rocketry
		if (NCConfig.ore_processing) {
			addRecipe("oreDilithium", oreStack("dustDilithium", 2), 1.25D, 1D);
			addRecipe("ingotDilithium", "dustDilithium", 1D, 1D);
		}
		
		// AE2
		addRecipe(Items.ENDER_PEARL, oreStackList(Lists.newArrayList("dustEnder", "dustEnderPearl"), 2), 0.5D, 1D);
		
		if (NCConfig.ore_processing) addMetalProcessingRecipes();
	}
	
	public void addMetalProcessingRecipes() {
		for (String ingot : OreDictionary.getOreNames()) {
			if (ingot.startsWith("ingot")) {
				String ore = "ore" + ingot.substring(5);
				String dust = "dust" + ingot.substring(5);
				if (OreDictHelper.oreExists(dust)) {
					addRecipe(ore, oreStack(dust, 2), 1.25D, 1D);
					addRecipe(ingot, dust, 1D, 1D);
				}
			}
		}
	}
}
