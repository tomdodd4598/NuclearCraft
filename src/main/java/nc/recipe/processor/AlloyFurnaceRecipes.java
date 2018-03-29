package nc.recipe.processor;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.RecipeOreStack;
import nc.util.OreStackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class AlloyFurnaceRecipes extends BaseRecipeHandler {
	
	public AlloyFurnaceRecipes() {
		super("alloy_furnace", 2, 0, 1, 0);
	}

	@Override
	public void addRecipes() {
		addAlloyIngotIngotRecipes("Copper", 3, "Tin", 1, "Bronze", 4, 1);
		addAlloyIngotIngotRecipes("Iron", 1, "Graphite", 1, "Steel", 1, 1);
		addAlloyIngotGemRecipes("Iron", 1, "Coal", 2, "Steel", 1, 1);
		addAlloyIngotIngotRecipes("Steel", 1, "Boron", 1, "Ferroboron", 2, 1);
		addAlloyIngotIngotRecipes("Ferroboron", 1, "Lithium", 1, "Tough", 2, 2);
		addAlloyIngotGemRecipes("Graphite", 2, "Diamond", 1, "HardCarbon", 2, 2);
		addAlloyIngotIngotRecipes("Magnesium", 1, "Boron", 2, "MagnesiumDiboride", 3, 1);
		addAlloyIngotIngotRecipes("Lithium", 1, "ManganeseDioxide", 1, "LithiumManganeseDioxide", 2, 2);
		addAlloyIngotIngotRecipes("Copper", 3, "Silver", 1, "Shibuichi", 4, 2);
		addAlloyIngotIngotRecipes("Tin", 3, "Silver", 1, "TinSilver", 4, 2);
		addAlloyIngotIngotRecipes("Lead", 3, "Platinum", 1, "LeadPlatinum", 4, 2);
		
		// Tinkers' Construct
		addAlloyIngotIngotRecipes("Aluminum", 3, "Copper", 1, "AluminumBrass", 4, 1);
		addAlloyIngotIngotRecipes("Aluminium", 3, "Copper", 1, "AluminumBrass", 4, 1);
		addAlloyIngotIngotRecipes("Cobalt", 1, "Ardite", 1, "Manyullyn", 1, 2);
		
		// Thermal Foundation
		addAlloyIngotIngotRecipes("Gold", 1, "Silver", 1, "Electrum", 2, 1);
		addAlloyIngotIngotRecipes("Iron", 2, "Nickel", 1, "Invar", 3, 1);
		addAlloyIngotIngotRecipes("Copper", 1, "Nickel", 1, "Constantan", 2, 1);
		
		// EnderIO
		addRecipe(Lists.newArrayList("ingotSteel", "itemSilicon"), "ingotElectricalSteel", NCConfig.processor_time[4]*2);
		addRecipe(Lists.newArrayList("ingotEnergeticAlloy", "dustEnergeticAlloy"), Lists.newArrayList(Items.ENDER_PEARL, "dustEnder"), "ingotPhasedGold", NCConfig.processor_time[4]*4);
		addRecipe("itemSilicon", "dustRedstone", "ingotRedstoneAlloy", NCConfig.processor_time[4]);
		addRecipe(Lists.newArrayList("ingotIron", "dustIron"), "dustRedstone", "ingotConductiveIron", NCConfig.processor_time[4]);
		addRecipe(Lists.newArrayList("ingotIron", "dustIron"), Lists.newArrayList(Items.ENDER_PEARL, "dustEnder"), "ingotPhasedIron", NCConfig.processor_time[4]*2);
		addRecipe(Lists.newArrayList("ingotSteel", "dustSteel"), "dustObsidian", "ingotDarkSteel", NCConfig.processor_time[4]*4);
		addRecipe(Lists.newArrayList("ingotGold", "dustGold"), Blocks.SOUL_SAND, "ingotSoularium", NCConfig.processor_time[4]*2);
		
		// Flaxbeard's Steam Power Mod
		addAlloyIngotIngotRecipes("Copper", 3, "Zinc", 1, "Brass", 4, 1);
		
		// Gadgetry
		addAlloyIngotDustRecipes("Gold", 1, "Redstone", 2, "ingotRedmetal", 1, 1);
		
		// Immersive Engineering
		addAlloyIngotFuelRecipes("Iron", 2, "Coke", 1, "Steel", 2, 2);
	}
	
	public void addAlloyIngotIngotRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, int time) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, OreStackHelper.NUGGET_VOLUME_TYPES, OreStackHelper.INGOT_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES, OreStackHelper.NUGGET_VOLUME_TYPES, OreStackHelper.INGOT_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotDustRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, int time) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, OreStackHelper.NUGGET_VOLUME_TYPES, OreStackHelper.INGOT_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES, OreStackHelper.TINYDUST_VOLUME_TYPES, OreStackHelper.DUST_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotFuelRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, int time) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, OreStackHelper.NUGGET_VOLUME_TYPES, OreStackHelper.INGOT_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES, OreStackHelper.TINYDUST_VOLUME_TYPES, OreStackHelper.FUEL_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotGemRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, int time) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, OreStackHelper.NUGGET_VOLUME_TYPES, OreStackHelper.INGOT_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES, OreStackHelper.TINYDUST_VOLUME_TYPES, OreStackHelper.GEM_VOLUME_TYPES, OreStackHelper.BLOCK_VOLUME_TYPES);
	}
	
	public ArrayList<RecipeOreStack> typeStackList(String type, String[] forms, int size) {
		ArrayList<RecipeOreStack> list = new ArrayList<RecipeOreStack>();
		for (String form : forms) list.add(oreStack(form + type, size));
		return list;
	}
	
	public void addAlloyRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, int time, String[] inNuggets1, String[] inIngots1, String[] inBlocks1, String[] inNuggets2, String[] inIngots2, String[] inBlocks2) {
		addRecipe(typeStackList(in1, inIngots1, inSize1), typeStackList(in2, inIngots2, inSize2), oreStack("ingot" + out, outSize), NCConfig.processor_time[4]*time);
		addRecipe(typeStackList(in1, inNuggets1, inSize1), typeStackList(in2, inNuggets2, inSize2), oreStack("nugget" + out, outSize), (NCConfig.processor_time[4]*time)/9);
		addRecipe(typeStackList(in1, inBlocks1, inSize1), typeStackList(in2, inBlocks2, inSize2), oreStack("block" + out, outSize), NCConfig.processor_time[4]*time*9);
	}
}
