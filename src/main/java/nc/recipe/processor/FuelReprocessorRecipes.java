package nc.recipe.processor;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.RegistryHelper;

public class FuelReprocessorRecipes extends ProcessorRecipeHandler {
	
	public FuelReprocessorRecipes() {
		super("fuel_reprocessor", 1, 0, 4, 0);
	}

	@Override
	public void addRecipes() {
		addReprocessingRecipes("TBU", "Uranium233", 1, "Uranium238", 5, "Neptunium236", 1, "Neptunium237", 1);
		
		addReprocessingRecipes("LEU233", "Uranium238", 5, "Plutonium241", 1, "Plutonium242", 1, "Americium243", 1);
		addReprocessingRecipes("HEU233", "Uranium235", 1, "Uranium238", 2, "Plutonium242", 3, "Americium243", 1);
		addReprocessingRecipes("LEU235", "Uranium238", 4, "Plutonium239", 1, "Plutonium242", 2, "Americium243", 1);
		addReprocessingRecipes("HEU235", "Uranium238", 3, "Neptunium236", 1, "Plutonium242", 2, "Americium243", 1);
		
		addReprocessingRecipes("LEN236", "Uranium238", 4, "Neptunium237", 1, "Plutonium241", 1, "Plutonium242", 2);
		addReprocessingRecipes("HEN236", "Uranium238", 4, "Plutonium238", 1, "Plutonium241", 1, "Plutonium242", 1);
		
		addReprocessingRecipes("LEP239", "Plutonium242", 5, "Americium242", 1, "Americium243", 1, "Curium246", 1);
		addReprocessingRecipes("HEP239", "Plutonium241", 1, "Americium242", 1, "Americium243", 4, "Curium243", 1);
		addReprocessingRecipes("LEP241", "Plutonium242", 5, "Americium243", 1, "Curium246", 1, "Berkelium247", 1);
		addReprocessingRecipes("HEP241", "Americium241", 1, "Americium242", 1, "Americium243", 3, "Curium246", 2);
		
		addReprocessingRecipes("MIX239", "Uranium238", 4, "Plutonium241", 1, "Plutonium242", 2, "Americium243", 1);
		addReprocessingRecipes("MIX241", "Uranium238", 4, "Plutonium241", 1, "Plutonium242", 2, "Americium243", 1);
		
		addReprocessingRecipes("LEA242", "Americium243", 4, "Curium245", 1, "Curium246", 2, "Berkelium247", 1);
		addReprocessingRecipes("HEA242", "Americium243", 3, "Curium243", 1, "Curium246", 4, "Berkelium247", 1);
		
		addReprocessingRecipes("LECm243", "Curium246", 4, "Curium247", 1, "Berkelium247", 2, "Berkelium248", 1);
		addReprocessingRecipes("HECm243", "Curium245", 1, "Curium246", 3, "Berkelium247", 2, "Berkelium248", 1);
		addReprocessingRecipes("LECm245", "Curium246", 4, "Curium247", 1, "Berkelium247", 2, "Californium249", 1);
		addReprocessingRecipes("HECm245", "Curium246", 3, "Curium247", 1, "Berkelium247", 2, "Californium249", 1);
		addReprocessingRecipes("LECm247", "Curium246", 5, "Berkelium247", 1, "Berkelium248", 1, "Californium249", 1);
		addReprocessingRecipes("HECm247", "Berkelium247", 4, "Berkelium248", 1, "Californium249", 1, "Californium251", 1);
		
		addReprocessingRecipes("LEB248", "Berkelium247", 5, "Berkelium248", 1, "Californium249", 1, "Californium251", 1);
		addReprocessingRecipes("HEB248", "Berkelium248", 1, "Californium249", 1, "Californium251", 2, "Californium252", 3);
		
		addReprocessingRecipes("LECf249", "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 2);
		addReprocessingRecipes("HECf249", "Californium250", 1, "Californium252", 2, "Californium252", 2, "Californium252", 2);
		addReprocessingRecipes("LECf251", "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 2);
		addReprocessingRecipes("HECf251", "Californium252", 2, "Californium252", 2, "Californium252", 2, "Californium252", 1);
		
		// IC2
		addRecipe("depletedFuelIC2U", RegistryHelper.itemStackFromRegistry("ic2:nuclear:2", 2), RegistryHelper.itemStackFromRegistry("ic2:nuclear:2", 1), RegistryHelper.itemStackFromRegistry("ic2:nuclear:2", 1), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 1), 1D, 1D);
		addRecipe("depletedFuelIC2MOX", RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), RegistryHelper.itemStackFromRegistry("ic2:nuclear:7", 7), 1D, 1D);
	}
	
	public void addReprocessingRecipes(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4) {
		for (String type : new String[] {"Oxide", "Nitride", "ZA"}) {
			addRecipe(oreStack("depletedFuel" + fuel + type, 9), oreStack("ingot" + out1 + type, n1), oreStack("ingot" + out2 + type, n2), oreStack("ingot" + out3 + type, n3), oreStack("ingot" + out4 + type, n4), 1D, 1D);
		}
	}
}
