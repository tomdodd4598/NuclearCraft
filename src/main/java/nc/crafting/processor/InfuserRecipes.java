package nc.crafting.processor;

import nc.handler.ProcessorRecipeHandler;

public class InfuserRecipes extends ProcessorRecipeHandler {
	
	private static final InfuserRecipes RECIPES = new InfuserRecipes();

	public InfuserRecipes() {
		super(1, 1, 1, 0, false);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		oxidize("ingotThorium", 1000);
		oxidize("ingotUranium", 1000);
		oxidize("ingotManganese", 1000);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", 1000), "ingotManganeseDioxide");
		oxidize("dustThorium", 1000);
		oxidize("dustUranium", 1000);
		oxidize("dustManganese", 1000);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", 1000), "dustManganeseDioxide");
		
		oxidizeBase("Thorium230");
		oxidizeBase("Thorium232");
		
		oxidizeBase("Uranium233");
		oxidizeBase("Uranium235");
		oxidizeBase("Uranium238");
		
		oxidizeBase("Neptunium236");
		oxidizeBase("Neptunium237");
		
		oxidizeBase("Plutonium238");
		oxidizeBase("Plutonium239");
		oxidizeBase("Plutonium241");
		oxidizeBase("Plutonium242");
		
		oxidizeBase("Americium241");
		oxidizeBase("Americium242");
		oxidizeBase("Americium243");
		
		oxidizeBase("Curium243");
		oxidizeBase("Curium245");
		oxidizeBase("Curium246");
		oxidizeBase("Curium247");
		
		oxidizeBase("Berkelium247");
		oxidizeBase("Berkelium248");
		
		oxidizeBase("Californium249");
		oxidizeBase("Californium250");
		oxidizeBase("Californium251");
		oxidizeBase("Californium252");
		
		oxidizeFuel("TBU");
		
		oxidizeFuel("LEU233");
		oxidizeFuel("HEU233");
		oxidizeFuel("LEU235");
		oxidizeFuel("HEU235");
		
		oxidizeFuel("LEN236");
		oxidizeFuel("HEN236");
		
		oxidizeFuel("LEP239");
		oxidizeFuel("HEP239");
		oxidizeFuel("LEP241");
		oxidizeFuel("HEP241");
		
		oxidizeFuel("LEA242");
		oxidizeFuel("HEA242");
		
		oxidizeFuel("LECm243");
		oxidizeFuel("HECm243");
		oxidizeFuel("LECm245");
		oxidizeFuel("HECm245");
		oxidizeFuel("LECm247");
		oxidizeFuel("HECm247");
		
		oxidizeFuel("LEB248");
		oxidizeFuel("HEB248");
		
		oxidizeFuel("LECf249");
		oxidizeFuel("HECf249");
		oxidizeFuel("LECf251");
		oxidizeFuel("HECf251");
	}
	
	public void oxidize(String name, int oxygen) {
		addRecipe(name, fluidStack("oxygen", oxygen), name + "Oxide");
	}
	
	public void oxidizeBase(String name) {
		addRecipe("ingot" + name + "Base", fluidStack("oxygen", 400), "ingot" + name + "Oxide");
		addRecipe("tiny" + name + "Base", fluidStack("oxygen", 40), "tiny" + name + "Oxide");
	}
	
	public void oxidizeFuel(String name) {
		addRecipe("fuel" + name, fluidStack("oxygen", 4000), "fuel" + name + "Oxide");
		addRecipe("fuelRod" + name, fluidStack("oxygen", 4000), "fuelRod" + name + "Oxide");
		addRecipe("depletedFuelRod" + name, fluidStack("oxygen", 3000), "depletedFuelRod" + name + "Oxide");
	}
}
