package nc.recipe.processor;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class InfuserRecipes extends BaseRecipeHandler {
	
	public InfuserRecipes() {
		super(1, 1, 1, 0, false);
	}

	@Override
	public void addRecipes() {
		oxidize("ingotThorium", 1000);
		oxidize("ingotUranium", 1000);
		oxidize("ingotManganese", 1000);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", 1000), "ingotManganeseDioxide", NCConfig.processor_time[5]);
		oxidize("dustThorium", 1000);
		oxidize("dustUranium", 1000);
		oxidize("dustManganese", 1000);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", 1000), "dustManganeseDioxide", NCConfig.processor_time[5]);
		
		oxidizeFertile("Thorium230");
		oxidizeFissile("Thorium232");
		
		oxidizeFissile("Uranium233");
		oxidizeFissile("Uranium235");
		oxidizeFertile("Uranium238");
		
		oxidizeFissile("Neptunium236");
		oxidizeFertile("Neptunium237");
		
		oxidizeFertile("Plutonium238");
		oxidizeFissile("Plutonium239");
		oxidizeFissile("Plutonium241");
		oxidizeFertile("Plutonium242");
		
		oxidizeFertile("Americium241");
		oxidizeFissile("Americium242");
		oxidizeFertile("Americium243");
		
		oxidizeFissile("Curium243");
		oxidizeFissile("Curium245");
		oxidizeFertile("Curium246");
		oxidizeFissile("Curium247");
		
		oxidizeFertile("Berkelium247");
		oxidizeFissile("Berkelium248");
		
		oxidizeFissile("Californium249");
		oxidizeFertile("Californium250");
		oxidizeFissile("Californium251");
		oxidizeFertile("Californium252");
		
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
		
		addRecipe(Blocks.ICE, fluidStack("liquidhelium", 50), NCBlocks.block_ice, NCConfig.processor_time[5]/10);
		addRecipe(Blocks.PACKED_ICE, fluidStack("liquidhelium", 50), NCBlocks.block_ice, NCConfig.processor_time[5]/10);
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("liquidhelium", 1000), new ItemStack(NCBlocks.cooler, 1, 8), NCConfig.processor_time[5]*2);
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("water", 1000), new ItemStack(NCBlocks.cooler, 1, 1), NCConfig.processor_time[5]*2);
	}
	
	public void oxidize(String name, int oxygen) {
		addRecipe(name, fluidStack("oxygen", oxygen), name + "Oxide", NCConfig.processor_time[5]);
	}
	
	public void oxidizeFertile(String name) {
		addRecipe("ingot" + name + "Base", fluidStack("oxygen", 400), "ingot" + name + "Oxide", NCConfig.processor_time[5]/2);
		addRecipe("tiny" + name, fluidStack("oxygen", 40), "tiny" + name + "Oxide", NCConfig.processor_time[5]/16);
	}
	
	public void oxidizeFissile(String name) {
		addRecipe("ingot" + name, fluidStack("oxygen", 400), "ingot" + name + "Oxide", NCConfig.processor_time[5]/2);
		addRecipe("tiny" + name, fluidStack("oxygen", 40), "tiny" + name + "Oxide", NCConfig.processor_time[5]/16);
	}
	
	public void oxidizeFuel(String name) {
		addRecipe("fuel" + name, fluidStack("oxygen", 4000), "fuel" + name + "Oxide", NCConfig.processor_time[5]*4);
		addRecipe("fuelRod" + name, fluidStack("oxygen", 4000), "fuelRod" + name + "Oxide", NCConfig.processor_time[5]*4);
		addRecipe("depletedFuelRod" + name, fluidStack("oxygen", 3000), "depletedFuelRod" + name + "Oxide", NCConfig.processor_time[5]*4);
	}

	@Override
	public String getRecipeName() {
		return "infuser";
	}
}
