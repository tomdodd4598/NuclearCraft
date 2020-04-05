package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.OreIngredient;
import nc.util.OreDictHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class AlloyFurnaceRecipes extends ProcessorRecipeHandler {
	
	public AlloyFurnaceRecipes() {
		super("alloy_furnace", 2, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addAlloyIngotIngotRecipes("Copper", 3, "Tin", 1, "Bronze", 4, 1D, 1D);
		if (OreDictHelper.oreExists("dustCoke") || OreDictHelper.oreExists("fuelCoke")) {
			addAlloyIngotCoalRecipes("Iron", 1, 4, "Steel", 1, 1D, 1D);
			addAlloyIngotIngotRecipes("Iron", 1, "Graphite", 4, "Steel", 1, 1D, 1D);
			addAlloyIngotFuelRecipes("Iron", 1, "Coke", 1, "Steel", 1, 1D, 1D);
		}
		else {
			addAlloyIngotCoalRecipes("Iron", 1, 2, "Steel", 1, 1D, 1D);
			addAlloyIngotIngotRecipes("Iron", 1, "Graphite", 2, "Steel", 1, 1D, 1D);
		}
		addAlloyIngotIngotRecipes("Steel", 1, "Boron", 1, "Ferroboron", 2, 1D, 1.5D);
		addAlloyIngotIngotRecipes("Ferroboron", 1, "Lithium", 1, "Tough", 2, 1.5D, 1.5D);
		addAlloyIngotGemRecipes("Graphite", 2, "Diamond", 1, "HardCarbon", 2, 1D, 2D);
		addAlloyIngotIngotRecipes("Magnesium", 1, "Boron", 2, "MagnesiumDiboride", 3, 1D, 1D);
		addAlloyIngotIngotRecipes("Lithium", 1, "ManganeseDioxide", 1, "LithiumManganeseDioxide", 2, 1.5D, 1D);
		addAlloyIngotIngotRecipes("Copper", 3, "Silver", 1, "Shibuichi", 4, 1.5D, 0.5D);
		addAlloyIngotIngotRecipes("Tin", 3, "Silver", 1, "TinSilver", 4, 1.5D, 0.5D);
		addAlloyIngotIngotRecipes("Lead", 3, "Platinum", 1, "LeadPlatinum", 4, 1.5D, 0.5D);
		addAlloyIngotIngotRecipes("Tough", 1, "HardCarbon", 1, "Extreme", 1, 2D, 2D);
		addAlloyIngotGemRecipes("Extreme", 1, "BoronArsenide", 1, "Thermoconducting", 2, 1.5D, 1.5D);
		addAlloyIngotIngotRecipes("Zirconium", 7, "Tin", 1, "Zircaloy", 8, 4D, 1D);
		addRecipe(SILICON, metalList("Graphite"), oreStack("ingotSiliconCarbide", 2), 2D, 2D);
		addAlloyIngotIngotRecipes("Iron", 15, "CarbonManganese", 1, "HSLASteel", 16, 8D, 2D);
		
		// Tinkers' Construct
		addAlloyIngotIngotRecipes("Aluminum", 3, "Copper", 1, "AluminumBrass", 4, 1D, 1D);
		addAlloyIngotIngotRecipes("Aluminium", 3, "Copper", 1, "AluminumBrass", 4, 1D, 1D);
		addAlloyIngotIngotRecipes("Cobalt", 1, "Ardite", 1, "Manyullyn", 1, 2D, 2D);
		
		// Thermal Foundation
		addAlloyIngotIngotRecipes("Gold", 1, "Silver", 1, "Electrum", 2, 1D, 0.5D);
		addAlloyIngotIngotRecipes("Iron", 2, "Nickel", 1, "Invar", 3, 1D, 1.5D);
		addAlloyIngotIngotRecipes("Copper", 1, "Nickel", 1, "Constantan", 2, 1D, 1D);
		
		// EnderIO
		addRecipe(metalList("Steel"), SILICON, "ingotElectricalSteel", 1.5D, 1.5D);
		addRecipe(metalList("Gold"), oreStack("dustEnergetic", 2), "ingotEnergeticAlloy", 1D, 1.5D);
		addRecipe(metalList("EnergeticAlloy"), ENDER_PEARL, "ingotVibrantAlloy", 1.5D, 2D);
		addRecipe(SILICON, "dustRedstone", "ingotRedstoneAlloy", 1D, 1D);
		addRecipe(metalList("Iron"), "dustRedstone", "ingotConductiveIron", 1D, 1D);
		addRecipe(metalList("Iron"), ENDER_PEARL, "ingotPulsatingIron", 1.5D, 1D);
		addRecipe(metalList("Steel"), "obsidian", OreDictHelper.oreExists("ingotDarkSteel") ? "ingotDarkSteel" : "ingotObsidianSteel", 1.5D, 2D);
		addRecipe(metalList("Gold"), Blocks.SOUL_SAND, "ingotSoularium", 1.5D, 0.5D);
		addRecipe("ingotDarkSteel", oreStack("dustDimensional", 2), "ingotEndSteel", 2D, 2D);
		addRecipe(oreStackList(metalList("Lead"), 2), metalList("Iron"), oreStack("ingotConstructionAlloy", 3), 1D, 1D);
		
		addRecipe(gemList("Diamond"), oreStack("nuggetPulsatingIron", 8), "itemPulsatingCrystal", 1D, 1.5D);
		addRecipe(gemList("Emerald"), oreStack("nuggetVibrantAlloy", 8), "itemVibrantCrystal", 1D, 1.5D);
		
		// Endergy
		addRecipe("itemClay", "gravel", "ingotCrudeSteel", 0.5D, 1D);
		addRecipe("ingotGold", "itemPulsatingPowder", "ingotCrystallineAlloy", 1D, 1D);
		addRecipe("ingotEndSteel", Items.CHORUS_FRUIT_POPPED, "ingotMelodicAlloy", 2D, 1.5D);
		addRecipe("ingotMelodicAlloy", "netherStar", oreStack("ingotStellarAlloy", 2), 2D, 2D);
		addRecipe(metalList("Silver"), oreStack("dustEnergetic", 2), "ingotEnergeticSilver", 1D, 1.5D);
		addRecipe(metalList("EnergeticSilver"), ENDER_PEARL, "ingotVividAlloy", 1.5D, 2D);
		
		// Flaxbeard's Steam Power Mod
		addAlloyIngotIngotRecipes("Copper", 3, "Zinc", 1, "Brass", 4, 1D, 1D);
		
		// Gadgetry
		addAlloyIngotDustRecipes("Gold", 1, "Redstone", 2, "ingotRedmetal", 1, 1D, 1D);
		
		// Advanced Rocketry
		addAlloyIngotIngotRecipes("Aluminum", 7, "Titanium", 3, "TitaniumAluminide", 3, 3D, 1D);
		addAlloyIngotIngotRecipes("Aluminium", 7, "Titanium", 3, "TitaniumAluminide", 3, 3D, 1D);
		addAlloyIngotIngotRecipes("Titanium", 1, "Iridium", 1, "TitaniumIridium", 2, 1.5D, 2D);
		
		// Fission Materials
		addFissionAlloyRecipes();
	}
	
	public void addAlloyIngotIngotRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, power, OreDictHelper.NUGGET_VOLUME_TYPES, OreDictHelper.INGOT_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES, OreDictHelper.NUGGET_VOLUME_TYPES, OreDictHelper.INGOT_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotDustRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, power, OreDictHelper.NUGGET_VOLUME_TYPES, OreDictHelper.INGOT_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES, OreDictHelper.TINYDUST_VOLUME_TYPES, OreDictHelper.DUST_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotFuelRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, power, OreDictHelper.NUGGET_VOLUME_TYPES, OreDictHelper.INGOT_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES, OreDictHelper.TINYDUST_VOLUME_TYPES, OreDictHelper.FUEL_VOLUME_TYPES, OreDictHelper.FUEL_BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotGemRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, power, OreDictHelper.NUGGET_VOLUME_TYPES, OreDictHelper.INGOT_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES, OreDictHelper.TINYDUST_VOLUME_TYPES, OreDictHelper.GEM_VOLUME_TYPES, OreDictHelper.BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power, List<String> inNuggets1, List<String> inIngots1, List<String> inBlocks1, List<String> inNuggets2, List<String> inIngots2, List<String> inBlocks2) {
		addRecipe(typeStackList(in1, inIngots1, inSize1), typeStackList(in2, inIngots2, inSize2), oreStack("ingot" + out, outSize), time, power);
		addRecipe(typeStackList(in1, inNuggets1, inSize1), typeStackList(in2, inNuggets2, inSize2), oreStack("nugget" + out, outSize), time/9D, power);
		addRecipe(typeStackList(in1, inBlocks1, inSize1), typeStackList(in2, inBlocks2, inSize2), oreStack("block" + out, outSize), time*9D, power);
	}
	
	public void addAlloyIngotCoalRecipes(String in1, int inSize1, int inSize2, String out, int outSize, double time, double power) {
		addRecipe(typeStackList(in1, OreDictHelper.INGOT_VOLUME_TYPES, inSize1), typeStackList("", OreDictHelper.COAL_TYPES, inSize2), oreStack("ingot" + out, outSize), time, power);
		addRecipe(typeStackList(in1, OreDictHelper.NUGGET_VOLUME_TYPES, inSize1), typeStackList("Coal", OreDictHelper.TINYDUST_VOLUME_TYPES, inSize2), oreStack("nugget" + out, outSize), time/9D, power);
		addRecipe(typeStackList(in1, OreDictHelper.BLOCK_VOLUME_TYPES, inSize1), typeStackList("Coal", OreDictHelper.BLOCK_VOLUME_TYPES, inSize2), oreStack("block" + out, outSize), time*9D, power);
	}
	
	public void addFissionAlloyRecipes() {
		for (int i = 0; i < FISSION_ORE_DICT.length; i++) {
			addAlloyIngotIngotRecipes(FISSION_ORE_DICT[i], 1, "Zirconium", 1, FISSION_ORE_DICT[i] + "ZA", 1, 1D, 1D);
			addAlloyIngotIngotRecipes(FISSION_ORE_DICT[i], 1, "Graphite", 1, FISSION_ORE_DICT[i] + "Carbide", 1, 1D, 1D);
		}
	}
	
	private static ArrayList<OreIngredient> typeStackList(String type, List<String> forms, int size) {
		ArrayList<OreIngredient> list = new ArrayList<>();
		for (String form : forms) list.add(oreStack(form + type, size));
		return list;
	}
	
	private static final List<String> SILICON = Lists.newArrayList("itemSilicon", "ingotSilicon");
	private static final List ENDER_PEARL = Lists.newArrayList(Items.ENDER_PEARL, "dustEnder");
	
	private static List<String> metalList(String name) {
		return Lists.newArrayList("ingot" + name, "dust" + name);
	}
	
	private static List<String> gemList(String name) {
		return Lists.newArrayList("gem" + name, "dust" + name);
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
