package nc.recipe.processor;

import nc.util.*;

import static nc.config.NCConfig.*;

public class FuelReprocessorRecipes extends BasicProcessorRecipeHandler {
	
	public FuelReprocessorRecipes() {
		super("fuel_reprocessor", 1, 0, 8, 0);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[3]) {
			return;
		}
		
		addReprocessingRecipes("TBU", "Uranium233", 1, "Uranium238", 5, "Neptunium236", 1, "Neptunium237", 1, "Strontium90", "Caesium137", 0.5D, 50);
		
		addReprocessingRecipes("LEU233", "Uranium238", 5, "Plutonium241", 1, "Plutonium242", 1, "Americium243", 1, "Strontium90", "Caesium137", 0.5D, 50);
		addReprocessingRecipes("HEU233", "Uranium235", 1, "Uranium238", 2, "Plutonium242", 3, "Americium243", 1, "Strontium90", "Caesium137", 1.5D, 50);
		addReprocessingRecipes("LEU235", "Uranium238", 4, "Plutonium239", 1, "Plutonium242", 2, "Americium243", 1, "Molybdenum", "Caesium137", 0.5D, 50);
		addReprocessingRecipes("HEU235", "Uranium238", 3, "Neptunium236", 1, "Plutonium242", 2, "Americium243", 1, "Molybdenum", "Caesium137", 1.5D, 50);
		
		addReprocessingRecipes("LEN236", "Uranium238", 4, "Neptunium237", 1, "Plutonium241", 1, "Plutonium242", 2, "Molybdenum", "Caesium137", 0.5D, 50);
		addReprocessingRecipes("HEN236", "Uranium238", 4, "Plutonium238", 1, "Plutonium241", 1, "Plutonium242", 1, "Molybdenum", "Caesium137", 1.5D, 50);
		
		addReprocessingRecipes("LEP239", "Plutonium242", 5, "Americium242", 1, "Americium243", 1, "Curium246", 1, "Strontium90", "Promethium147", 0.5D, 50);
		addReprocessingRecipes("HEP239", "Plutonium241", 1, "Americium242", 1, "Americium243", 4, "Curium243", 1, "Strontium90", "Promethium147", 1.5D, 50);
		addReprocessingRecipes("LEP241", "Plutonium242", 5, "Americium243", 1, "Curium246", 1, "Berkelium247", 1, "Strontium90", "Promethium147", 0.5D, 50);
		addReprocessingRecipes("HEP241", "Americium241", 1, "Americium242", 1, "Americium243", 3, "Curium246", 2, "Strontium90", "Promethium147", 1.5D, 50);
		
		addReprocessingRecipes("MIX239", "Uranium238", 4, "Plutonium241", 1, "Plutonium242", 2, "Americium243", 1, "Strontium90", "Promethium147", 0.5D, 50);
		addReprocessingRecipes("MIX241", "Uranium238", 3, "Plutonium241", 1, "Plutonium242", 3, "Americium243", 1, "Strontium90", "Promethium147", 0.5D, 50);
		
		addReprocessingRecipes("LEA242", "Americium243", 3, "Curium245", 1, "Curium246", 3, "Berkelium248", 1, "Molybdenum", "Promethium147", 0.5D, 50);
		addReprocessingRecipes("HEA242", "Americium243", 3, "Curium243", 1, "Curium246", 2, "Berkelium247", 1, "Molybdenum", "Promethium147", 1.5D, 50);
		
		addReprocessingRecipes("LECm243", "Curium246", 4, "Curium247", 1, "Berkelium247", 2, "Berkelium248", 1, "Molybdenum", "Promethium147", 0.5D, 50);
		addReprocessingRecipes("HECm243", "Curium245", 1, "Curium246", 3, "Berkelium247", 2, "Berkelium248", 1, "Molybdenum", "Promethium147", 1.5D, 50);
		addReprocessingRecipes("LECm245", "Curium246", 4, "Curium247", 1, "Berkelium247", 2, "Californium249", 1, "Molybdenum", "Europium155", 0.5D, 60);
		addReprocessingRecipes("HECm245", "Curium246", 3, "Curium247", 1, "Berkelium247", 2, "Californium249", 1, "Molybdenum", "Europium155", 1.5D, 60);
		addReprocessingRecipes("LECm247", "Curium246", 5, "Berkelium247", 1, "Berkelium248", 1, "Californium249", 1, "Molybdenum", "Europium155", 0.5D, 60);
		addReprocessingRecipes("HECm247", "Berkelium247", 4, "Berkelium248", 1, "Californium249", 1, "Californium251", 1, "Molybdenum", "Europium155", 1.5D, 60);
		
		addReprocessingRecipes("LEB248", "Berkelium247", 5, "Berkelium248", 1, "Californium249", 1, "Californium251", 1, "Ruthenium106", "Promethium147", 0.5D, 60);
		addReprocessingRecipes("HEB248", "Berkelium248", 1, "Californium249", 1, "Californium251", 2, "Californium252", 3, "Ruthenium106", "Promethium147", 1.5D, 60);
		
		addReprocessingRecipes("LECf249", "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 2, "Ruthenium106", "Promethium147", 0.5D, 60);
		addReprocessingRecipes("HECf249", "Californium250", 1, "Californium252", 2, "Californium252", 2, "Californium252", 2, "Ruthenium106", "Promethium147", 1.5D, 60);
		addReprocessingRecipes("LECf251", "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 2, "Ruthenium106", "Europium155", 0.5D, 60);
		addReprocessingRecipes("HECf251", "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 1, "Ruthenium106", "Europium155", 1.5D, 60);
		
		// IC2
		addRecipe("depletedFuelIC2U", RegistryHelper.itemStackFromRegistry("ic2:nuclear:2", 2), RegistryHelper.itemStackFromRegistry("ic2:nuclear:2", 1), RegistryHelper.itemStackFromRegistry("ic2:nuclear:2", 1), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 1), emptyItemStack(), emptyItemStack(), emptyItemStack(), emptyItemStack(), 1D, 1D);
		addRecipe("depletedFuelIC2MOX", RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), emptyItemStack(), emptyItemStack(), emptyItemStack(), emptyItemStack(), 1D, 1D);
	}
	
	public void addReprocessingRecipes(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4, String waste1, String waste2, double m, int r) {
		int extraReturn = 9 - n1 - n2 - n3 - n4;
		addRecipe(oreStack("ingotDepleted" + fuel + "TRISO", 9), oreStack("ingot" + out1 + "Carbide", n1), oreStack("ingot" + out2 + "Carbide", n2), chanceOreStack("dust" + waste1, 1, NCMath.toInt(m * r)), oreStack("dustGraphite", extraReturn + 2), oreStack("ingot" + out3 + "Carbide", n3), oreStack("ingot" + out4 + "Carbide", n4), chanceOreStack("dust" + waste2, 1, NCMath.toInt(m * (100 - r))), oreStack(OreDictHelper.oreExists("dustSiliconCarbide") ? "dustSiliconCarbide" : "ingotSiliconCarbide", 1), 1D, 1D);
		addRecipe(oreStack("ingotDepleted" + fuel + "Oxide", 9), oreStack("ingot" + out1 + "Oxide", n1), oreStack("ingot" + out2 + "Oxide", n2), chanceOreStack("dust" + waste1, 1, NCMath.toInt(m * r)), emptyItemStack(), oreStack("ingot" + out3 + "Oxide", n3), oreStack("ingot" + out4 + "Oxide", n4), chanceOreStack("dust" + waste2, 1, NCMath.toInt(m * (100 - r))), emptyItemStack(), 1D, 1D);
		addRecipe(oreStack("ingotDepleted" + fuel + "Nitride", 9), oreStack("ingot" + out1 + "Nitride", n1), oreStack("ingot" + out2 + "Nitride", n2), chanceOreStack("dust" + waste1, 1, NCMath.toInt(m * r)), emptyItemStack(), oreStack("ingot" + out3 + "Nitride", n3), oreStack("ingot" + out4 + "Nitride", n4), chanceOreStack("dust" + waste2, 1, NCMath.toInt(m * (100 - r))), emptyItemStack(), 1D, 1D);
		addRecipe(oreStack("ingotDepleted" + fuel + "ZA", 9), oreStack("ingot" + out1 + "ZA", n1), oreStack("ingot" + out2 + "ZA", n2), chanceOreStack("dust" + waste1, 1, NCMath.toInt(m * r)), oreStack("dustZirconium", extraReturn), oreStack("ingot" + out3 + "ZA", n3), oreStack("ingot" + out4 + "ZA", n4), chanceOreStack("dust" + waste2, 1, NCMath.toInt(m * (100 - r))), emptyItemStack(), 1D, 1D);
	}
}
