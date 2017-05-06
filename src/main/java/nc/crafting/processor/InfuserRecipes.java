package nc.crafting.processor;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;
import nc.init.NCBlocks;
import net.minecraft.item.ItemStack;

public class InfuserRecipes extends ProcessorRecipeHandler {
	
	private static final InfuserRecipes RECIPES = new InfuserRecipes();

	public InfuserRecipes() {
		super(1, 1, 1, 0, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		oxidize("ingotThorium", 1000);
		oxidize("ingotUranium", 1000);
		oxidize("ingotManganese", 1000);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", 1000), "ingotManganeseDioxide", NCConfig.processor_time[5]);
		oxidize("dustThorium", 1000);
		oxidize("dustUranium", 1000);
		oxidize("dustManganese", 1000);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", 1000), "dustManganeseDioxide", NCConfig.processor_time[5]);
		
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
		
		addRecipe("blockIce", fluidStack("liquidhelium", 5), NCBlocks.block_ice, NCConfig.processor_time[5]/10);
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("liquidhelium", 50), new ItemStack(NCBlocks.cooler, 1, 8), NCConfig.processor_time[5]);
	}
	
	public void oxidize(String name, int oxygen) {
		addRecipe(name, fluidStack("oxygen", oxygen), name + "Oxide", NCConfig.processor_time[5]);
	}
	
	public void oxidizeBase(String name) {
		addRecipe("ingot" + name + "Base", fluidStack("oxygen", 400), "ingot" + name + "Oxide", NCConfig.processor_time[5]/2);
		addRecipe("tiny" + name + "Base", fluidStack("oxygen", 40), "tiny" + name + "Oxide", NCConfig.processor_time[5]/16);
	}
	
	public void oxidizeFuel(String name) {
		addRecipe("fuel" + name, fluidStack("oxygen", 4000), "fuel" + name + "Oxide", NCConfig.processor_time[5]*4);
		addRecipe("fuelRod" + name, fluidStack("oxygen", 4000), "fuelRod" + name + "Oxide", NCConfig.processor_time[5]*4);
		addRecipe("depletedFuelRod" + name, fluidStack("oxygen", 3000), "depletedFuelRod" + name + "Oxide", NCConfig.processor_time[5]*4);
	}
}
