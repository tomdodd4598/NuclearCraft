package nc.recipe.processor;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeHelper;
import nc.util.ItemStackHelper;
import nc.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.NonNullList;
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
		addRecipe("gemLapis", "dustLapis", 1D, 1D);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", 1.5D, 1.5D);
		addRecipe("gemQuartz", "dustQuartz", 1D, 1D);
		addRecipe("gemPrismarine", "dustPrismarine", 1D, 1D);
		addRecipe("gemBoronNitride", "dustBoronNitride", 1.5D, 1.5D);
		addRecipe("gemFluorite", "dustFluorite", 1.5D, 1.5D);
		addRecipe("gemVilliaumite", "dustVilliaumite", 1.5D, 1.5D);
		addRecipe("gemCarobbiite", "dustCarobbiite", 1.5D, 1.5D);
		
		addRecipe("dustVilliaumite", "dustSodiumFluoride", 1D, 1D);
		addRecipe("dustCarobbiite", "dustPotassiumFluoride", 1D, 1D);
		
		if (OreDictHelper.oreExists("ingotSilicon")) {
			addRecipe("sand", "ingotSilicon", 1D, 1D);
			addRecipe("ingotSilicon", "itemSilicon", 0.5D, 0.5D);
		}
		else addRecipe("sand", "itemSilicon", 1D, 1D);
		
		addRecipe("obsidian", oreStack("dustObsidian", 4), 2D, 1D);
		addRecipe("cobblestone", Blocks.SAND, 1D, 1D);
		addRecipe("gravel", Items.FLINT, 1D, 1D);
		addRecipe("endstone", "dustEndstone", 1D, 1D);
		addRecipe(Items.BLAZE_ROD, new ItemStack(Items.BLAZE_POWDER, 4), 1D, 1D);
		addRecipe(new ItemStack(Items.ROTTEN_FLESH, 4), Items.LEATHER, 0.5D, 1D);
		addRecipe(new ItemStack(Items.REEDS, 2), "bioplastic", 1D, 0.5D);
		addRecipe("cropWheat", "dustWheat", 0.25D, 0.5D);
		addRecipe("cropBarley", "dustBarley", 0.25D, 0.5D);
		addRecipe("bone", new ItemStack(Items.DYE, 6, 15), 0.5D, 1D);
		addRecipe(NCItems.roasted_cocoa_beans, NCItems.ground_cocoa_nibs, 0.5D, 0.5D);
		addRecipe(Items.PORKCHOP, new ItemStack(NCItems.gelatin, 8), 0.5D, 0.5D);
		addRecipe(Lists.newArrayList(Items.FISH, new ItemStack(Items.FISH, 1, 1), new ItemStack(Items.FISH, 1, 2), new ItemStack(Items.FISH, 1, 3)), new ItemStack(NCItems.gelatin, 4), 0.5D, 0.5D);
		
		// EnderIO
		addRecipe("itemPrecientCrystal", "itemPrecientPowder", 1D, 1D);
		addRecipe("itemVibrantCrystal", "itemVibrantPowder", 1D, 1D);
		addRecipe("itemPulsatingCrystal", "itemPulsatingPowder", 1D, 1D);
		addRecipe("itemPulsatingCrystal", "itemPulsatingPowder", 1D, 1D);
		addRecipe("itemEnderCrystal", "itemEnderCrystalPowder", 1D, 1D);
		
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
		addRecipe("crystalCertusQuartz", "dustCertusQuartz", 0.5D, 1D);
		addRecipe("crystalFluix", "dustFluix", 0.5D, 1D);
		
		if (NCConfig.ore_processing) addMetalProcessingRecipes();
		
		addRecipe("plankWood", new ItemStack(Items.STICK, NCConfig.manufactory_wood[1]), 0.25D, 0.5D);
		addLogRecipes();
	}
	
	private static final List<String> BLACKLIST = Lists.newArrayList("silicon");
	
	public void addMetalProcessingRecipes() {
		for (String ingot : OreDictionary.getOreNames()) {
			if (ingot.startsWith("ingot")) {
				String type = ingot.substring(5);
				if (BLACKLIST.contains(type)) continue;
				String ore = "ore" + type, dust = "dust" + type;
				if (OreDictHelper.oreExists(dust)) {
					addRecipe(ore, oreStack(dust, 2), 1.25D, 1D);
					addRecipe(ingot, dust, 1D, 1D);
				}
			}
		}
	}
	
	/* Originally from KingLemming's Thermal Expansion: cofh.thermalexpansion.util.managers.machine.SawmillManager */
	public void addLogRecipes() {
		InventoryCrafting fakeCrafter = RecipeHelper.fakeCrafter(3, 3);
		for (ItemStack logWood : OreDictionary.getOres("logWood", false)) {
			Block logBlock = Block.getBlockFromItem(logWood.getItem());
			
			if (ItemStackHelper.getMetadata(logWood) == OreDictionary.WILDCARD_VALUE) {
				NonNullList<ItemStack> logVariants = NonNullList.create();
				logBlock.getSubBlocks(logBlock.getCreativeTab(), logVariants);
				
				for (ItemStack log : logVariants) {
					fakeCrafter.setInventorySlotContents(0, log);
					ItemStack plankWood = CraftingManager.findMatchingResult(fakeCrafter, null);
					
					if (!plankWood.isEmpty()) {
						plankWood.setCount(NCConfig.manufactory_wood[0]);
						addRecipe(log, plankWood, 0.5D, 0.5D);
					}
				}
			} else {
				fakeCrafter.setInventorySlotContents(0, logWood);
				ItemStack plankWood = CraftingManager.findMatchingResult(fakeCrafter, null);
				
				if (!plankWood.isEmpty()) {
					plankWood.setCount(NCConfig.manufactory_wood[0]);
					addRecipe(logWood, plankWood, 0.5D, 0.5D);
				}
			}
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
