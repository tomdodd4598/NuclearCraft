package nc.recipe.processor;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.RegistryHelper;

public class FuelReprocessorRecipes extends ProcessorRecipeHandler {
	
	public FuelReprocessorRecipes() {
		super("fuel_reprocessor", 1, 0, 4, 0);
	}

	@Override
	public void addRecipes() {
		/** Tier 1 *//* TBU, LEU235, HEU235 *//** Products 2x */
		/* 8x */	addReprocessingRecipes("TBU", "Uranium233", 16, "Uranium235", 8, "Neptunium236", 8, "Neptunium237", 32);
		
		/* 8x */	addReprocessingRecipes("LEU235", "Uranium238", 40, "Neptunium237", 8, "Plutonium239", 8, "Plutonium241", 8);
		/* 2x */	addReprocessingRecipes("HEU235", "Uranium238", 20, "Neptunium237", 16, "Plutonium239", 4, "Plutonium242", 24);
		
		/** Tier 2 *//* LEU233, HEU233, LEN236, HEN236, MOX239 *//** Products 1x */
		/* 4x */	addReprocessingRecipes("LEU233", "Plutonium239", 4, "Plutonium241", 4, "Plutonium242", 32, "Americium243", 24);
		/* 1x */	addReprocessingRecipes("HEU233", "Neptunium236", 32, "Neptunium237", 8, "Plutonium242", 16, "Americium243", 8);
		
		/* 4x */	addReprocessingRecipes("LEN236", "Neptunium237", 4, "Plutonium242", 32, "Americium242", 8, "Americium243", 20);
		/* 1x */	addReprocessingRecipes("HEN236", "Uranium238", 16, "Plutonium238", 8, "Plutonium239", 8, "Plutonium242", 32);
		
		/* 2x */	addReprocessingRecipes("MOX239", "Uranium238", 40, "Plutonium242", 12, "Americium243", 8, "Curium243", 4);
		
		/** Tier 3 *//* LEP239, HEP239, LEP241, HEP241, MOX241 *//** Products 1/2x */
		/* 2x */	addReprocessingRecipes("LEP239", "Plutonium239", 8, "Plutonium242", 24, "Curium243", 4, "Curium246", 28);
		/* 1/2x */	addReprocessingRecipes("HEP239", "Americium241", 8, "Americium242", 24, "Curium245", 8, "Curium246", 24);
		
		/* 2x */	addReprocessingRecipes("LEP241", "Plutonium242", 4, "Americium242", 4, "Americium243", 8, "Curium246", 48);
		/* 1/2x */	addReprocessingRecipes("HEP241", "Americium241", 8, "Curium245", 8, "Curium246", 24, "Curium247", 24);
		
		/* 1x */	addReprocessingRecipes("MOX241", "Uranium238", 40, "Plutonium241", 8, "Plutonium242", 8, "Curium246", 8);
		
		/** Tier 4 *//* LEA242, HEA242 *//** Products x1/2 */
		/* 2x */	addReprocessingRecipes("LEA242", "Curium243", 8, "Curium245", 8, "Curium246", 40, "Curium247", 8);
		/* 1/2x */	addReprocessingRecipes("HEA242", "Curium245", 16, "Curium246", 32, "Curium247", 8, "Berkelium247", 8);
		
		/** Tier 5 *//* LECm243, HECm243, LECm245, HECm245, LECm247, HECm247 *//** Products 1/8x */
		/* 1/2x */	addReprocessingRecipes("LECm243", "Curium246", 32, "Berkelium247", 16, "Berkelium248", 8, "Californium249", 8);
		/* 1/8x */	addReprocessingRecipes("HECm243", "Curium246", 24, "Berkelium247", 24, "Berkelium248", 8, "Californium249", 8);
		
		/* 1/2x */	addReprocessingRecipes("LECm245", "Berkelium247", 40, "Berkelium248", 8, "Californium249", 4, "Californium252", 12);
		/* 1/8x */	addReprocessingRecipes("HECm245", "Berkelium247", 48, "Berkelium248", 4, "Californium249", 4, "Californium251", 8);
		
		/* 1/2x */	addReprocessingRecipes("LECm247", "Berkelium247", 20, "Berkelium248", 4, "Californium251", 8, "Californium252", 32);
		/* 1/8x */	addReprocessingRecipes("HECm247", "Berkelium248", 8, "Californium249", 8, "Californium251", 24, "Californium252", 24);
		
		/** Tier 6 *//* LEB248, HEB248 *//** Products 1/8x */
		/* 1/2x */	addReprocessingRecipes("LEB248", "Californium249", 4, "Californium251", 4, "Californium252", 28, "Californium252", 28);
		/* 1/8x */	addReprocessingRecipes("HEB248", "Californium250", 8, "Californium251", 8, "Californium252", 24, "Californium252", 24);
		
		/** Tier 7 *//* LECf249, HECf249, LECf251, HECf251 *//** Products 1/32x */
		/* 1/8x */	addReprocessingRecipes("LECf249", "Californium250", 16, "Californium251", 8, "Californium252", 20, "Californium252", 20);
		/* 1/32x */	addReprocessingRecipes("HECf249", "Californium250", 32, "Californium251", 16, "Californium252", 8, "Californium252", 8);
		
		/* 1/8x */	addReprocessingRecipes("LECf251", "Californium251", 4, "Californium252", 20, "Californium252", 20, "Californium252", 20);
		/* 1/32x */	addReprocessingRecipes("HECf251", "Californium251", 16, "Californium252", 16, "Californium252", 16, "Californium252", 16);
		
		// IC2
		addRecipe("depletedFuelIC2U", RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 2, 2), RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 1, 2), RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 1, 2), RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 1, 7), 1D, 1D);
		addRecipe("depletedFuelIC2MOX", RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 7, 7), RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 7, 7), RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 7, 7), RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 7, 7), 1D, 1D);
	}
	
	public void addReprocessingRecipes(String fuel, String out1, int n1, String out2, int n2, String out3, int n3, String out4, int n4) {
		addRecipe("depletedFuel" + fuel, oreStack("nugget" + out1, n1), oreStack("nugget" + out2, n2), oreStack("nugget" + out3, n3), oreStack("nugget" + out4, n4), 1D, 1D);
		addRecipe("depletedFuel" + fuel + "Oxide", oreStack("nugget" + out1 + "Oxide", n1), oreStack("nugget" + out2 + "Oxide", n2), oreStack("nugget" + out3 + "Oxide", n3), oreStack("nugget" + out4 + "Oxide", n4), 1D, 1D);
		addRecipe("depletedFuelRod" + fuel, oreStack("nugget" + out1, n1), oreStack("nugget" + out2, n2), oreStack("nugget" + out3, n3), oreStack("nugget" + out4, n4), 1D, 1D);
		addRecipe("depletedFuelRod" + fuel + "Oxide", oreStack("nugget" + out1 + "Oxide", n1), oreStack("nugget" + out2 + "Oxide", n2), oreStack("nugget" + out3 + "Oxide", n3), oreStack("nugget" + out4 + "Oxide", n4), 1D, 1D);
	}
}
