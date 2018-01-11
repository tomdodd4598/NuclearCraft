package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class AlloyFurnaceRecipes extends BaseRecipeHandler {
	
	public AlloyFurnaceRecipes() {
		super(2, 0, 1, 0, true);
	}

	@Override
	public void addRecipes() {
		alloyRecipe("Copper", 3, "Tin", 1, oreStack("ingotBronze", 4), 1);
		alloyRecipe("Iron", 1, "Graphite", 2, "ingotSteel", 1);
		alloyRecipe("Steel", 3, "Boron", 1, oreStack("ingotFerroboron", 2), 1);
		alloyRecipe("Ferroboron", 1, "Lithium", 1, oreStack("ingotTough", 2), 2);
		alloyRecipe("Graphite", 2, "Diamond", 1, oreStack("ingotHardCarbon", 2), 2);
		alloyRecipe("Magnesium", 1, "Boron", 2, oreStack("ingotMagnesiumDiboride", 3), 1);
		alloyRecipe("Lithium", 1, "ManganeseDioxide", 1, oreStack("ingotLithiumManganeseDioxide", 2), 2);
		alloyRecipe("Copper", 3, "Silver", 1, oreStack("ingotShibuichi", 4), 2);
		alloyRecipe("Tin", 3, "Silver", 1, oreStack("ingotTinSilver", 4), 2);
		alloyRecipe("Lead", 3, "Platinum", 1, oreStack("ingotLeadPlatinum", 4), 2);
		
		// Tinkers' Construct
		alloyRecipe("Aluminum", 3, "Copper", 1, oreStack("ingotAluminumBrass", 4), 1);
		alloyRecipe("Aluminium", 3, "Copper", 1, oreStack("ingotAluminumBrass", 4), 1);
		alloyRecipe("Cobalt", 1, "Ardite", 1, "ingotManyullyn", 2);
		
		// Thermal Foundation
		alloyRecipe("Gold", 1, "Silver", 1, oreStack("ingotElectrum", 2), 1);
		alloyRecipe("Iron", 2, "Nickel", 1, oreStack("ingotInvar", 3), 1);
		alloyRecipe("Copper", 1, "Nickel", 1, oreStack("ingotConstantan", 2), 1);
		
		// EnderIO
		addRecipe("ingotSteel", "itemSilicon", "ingotElectricalSteel", NCConfig.processor_time[4]*2);
		addRecipe("dustSteel", "itemSilicon", "ingotElectricalSteel", NCConfig.processor_time[4]*2);
		addRecipe("ingotEnergeticAlloy", Items.ENDER_PEARL, "ingotPhasedGold", NCConfig.processor_time[4]*4);
		addRecipe("ingotEnergeticAlloy", "dustEnder", "ingotPhasedGold", NCConfig.processor_time[4]*4);
		addRecipe("dustEnergeticAlloy", Items.ENDER_PEARL, "ingotPhasedGold", NCConfig.processor_time[4]*4);
		addRecipe("dustEnergeticAlloy", "dustEnder", "ingotPhasedGold", NCConfig.processor_time[4]*4);
		addRecipe("itemSilicon", "dustRedstone", "ingotRedstoneAlloy", NCConfig.processor_time[4]);
		addRecipe("ingotIron", "dustRedstone", "ingotConductiveIron", NCConfig.processor_time[4]);
		addRecipe("dustIron", "dustRedstone", "ingotConductiveIron", NCConfig.processor_time[4]);
		addRecipe("ingotIron", Items.ENDER_PEARL, "ingotPhasedIron", NCConfig.processor_time[4]*2);
		addRecipe("ingotIron", "dustEnder", "ingotPhasedIron", NCConfig.processor_time[4]*2);
		addRecipe("dustIron", Items.ENDER_PEARL, "ingotPhasedIron", NCConfig.processor_time[4]*2);
		addRecipe("dustIron", "dustEnder", "ingotPhasedIron", NCConfig.processor_time[4]*2);
		addRecipe("ingotSteel", "dustObsidian", "ingotDarkSteel", NCConfig.processor_time[4]*4);
		addRecipe("dustSteel", "dustObsidian", "ingotDarkSteel", NCConfig.processor_time[4]*4);
		addRecipe("ingotGold", Blocks.SOUL_SAND, "ingotSoularium", NCConfig.processor_time[4]*2);
		addRecipe("dustGold", Blocks.SOUL_SAND, "ingotSoularium", NCConfig.processor_time[4]*2);
		
		// Flaxbeard's Steam Power Mod
		alloyRecipe("Copper", 3, "Zinc", 1, oreStack("ingotBrass", 4), 1);
		
		// Gadgetry
		addRecipe("ingotGold", oreStack("dustRedstone", 2), "ingotRedmetal", NCConfig.processor_time[4]);
		addRecipe("dustGold", oreStack("dustRedstone", 2), "ingotRedmetal", NCConfig.processor_time[4]);
		
		// Immersive Engineering
		addRecipe("ingotIron", "dustCoke", "ingotSteel", NCConfig.processor_time[4]);
		addRecipe("dustIron", "dustCoke", "ingotSteel", NCConfig.processor_time[4]);
		addRecipe("ingotIron", "fuelCoke", "ingotSteel", NCConfig.processor_time[4]);
		addRecipe("dustIron", "fuelCoke", "ingotSteel", NCConfig.processor_time[4]);
	}
	
	public void alloyRecipe(String in1, int inSize1, String in2, int inSize2, Object out, int time) {
		addRecipe(oreStack("ingot" + in1, inSize1), oreStack("ingot" + in2, inSize2), out, NCConfig.processor_time[4]*time);
		addRecipe(oreStack("ingot" + in1, inSize1), oreStack("dust" + in2, inSize2), out, NCConfig.processor_time[4]*time);
		addRecipe(oreStack("dust" + in1, inSize1), oreStack("ingot" + in2, inSize2), out, NCConfig.processor_time[4]*time);
		addRecipe(oreStack("dust" + in1, inSize1), oreStack("dust" + in2, inSize2), out, NCConfig.processor_time[4]*time);
	}

	@Override
	public String getRecipeName() {
		return "alloy_furnace";
	}
}
