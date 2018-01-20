package nc.recipe.processor;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class InfuserRecipes extends BaseRecipeHandler {
	
	public InfuserRecipes() {
		super("infuser", 1, 1, 1, 0, false);
	}

	@Override
	public void addRecipes() {
		addOxidizingRecipe("ingotThorium", FluidHelper.BUCKET_VOLUME);
		addOxidizingRecipe("ingotUranium", FluidHelper.BUCKET_VOLUME);
		addOxidizingRecipe("ingotManganese", FluidHelper.BUCKET_VOLUME);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", FluidHelper.BUCKET_VOLUME), "ingotManganeseDioxide", NCConfig.processor_time[5]);
		addOxidizingRecipe("dustThorium", FluidHelper.BUCKET_VOLUME);
		addOxidizingRecipe("dustUranium", FluidHelper.BUCKET_VOLUME);
		addOxidizingRecipe("dustManganese", FluidHelper.BUCKET_VOLUME);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", FluidHelper.BUCKET_VOLUME), "dustManganeseDioxide", NCConfig.processor_time[5]);
		
		addFertileOxidizingRecipes("Thorium230");
		addFissileOxidizingRecipes("Thorium232");
		
		addFissileOxidizingRecipes("Uranium233");
		addFissileOxidizingRecipes("Uranium235");
		addFertileOxidizingRecipes("Uranium238");
		
		addFissileOxidizingRecipes("Neptunium236");
		addFertileOxidizingRecipes("Neptunium237");
		
		addFertileOxidizingRecipes("Plutonium238");
		addFissileOxidizingRecipes("Plutonium239");
		addFissileOxidizingRecipes("Plutonium241");
		addFertileOxidizingRecipes("Plutonium242");
		
		addFertileOxidizingRecipes("Americium241");
		addFissileOxidizingRecipes("Americium242");
		addFertileOxidizingRecipes("Americium243");
		
		addFissileOxidizingRecipes("Curium243");
		addFissileOxidizingRecipes("Curium245");
		addFertileOxidizingRecipes("Curium246");
		addFissileOxidizingRecipes("Curium247");
		
		addFertileOxidizingRecipes("Berkelium247");
		addFissileOxidizingRecipes("Berkelium248");
		
		addFissileOxidizingRecipes("Californium249");
		addFertileOxidizingRecipes("Californium250");
		addFissileOxidizingRecipes("Californium251");
		addFertileOxidizingRecipes("Californium252");
		
		addFuelOxidizingRecipes("TBU");
		
		addFuelOxidizingRecipes("LEU233");
		addFuelOxidizingRecipes("HEU233");
		addFuelOxidizingRecipes("LEU235");
		addFuelOxidizingRecipes("HEU235");
		
		addFuelOxidizingRecipes("LEN236");
		addFuelOxidizingRecipes("HEN236");
		
		addFuelOxidizingRecipes("LEP239");
		addFuelOxidizingRecipes("HEP239");
		addFuelOxidizingRecipes("LEP241");
		addFuelOxidizingRecipes("HEP241");
		
		addFuelOxidizingRecipes("LEA242");
		addFuelOxidizingRecipes("HEA242");
		
		addFuelOxidizingRecipes("LECm243");
		addFuelOxidizingRecipes("HECm243");
		addFuelOxidizingRecipes("LECm245");
		addFuelOxidizingRecipes("HECm245");
		addFuelOxidizingRecipes("LECm247");
		addFuelOxidizingRecipes("HECm247");
		
		addFuelOxidizingRecipes("LEB248");
		addFuelOxidizingRecipes("HEB248");
		
		addFuelOxidizingRecipes("LECf249");
		addFuelOxidizingRecipes("HECf249");
		addFuelOxidizingRecipes("LECf251");
		addFuelOxidizingRecipes("HECf251");
		
		addRecipe(Blocks.ICE, fluidStack("liquidhelium", 50), NCBlocks.block_ice, NCConfig.processor_time[5]/10);
		addRecipe(Blocks.PACKED_ICE, fluidStack("liquidhelium", 50), NCBlocks.block_ice, NCConfig.processor_time[5]/10);
		
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("liquidhelium", FluidHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.cooler, 1, 8), NCConfig.processor_time[5]);
		addRecipe(new ItemStack(NCBlocks.cooler, 1, 0), fluidStack("water", FluidHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.cooler, 1, 1), NCConfig.processor_time[5]);
		
		// Immersive Engineering
		addRecipe("plankWood", fluidStack("creosote", 100), "plankTreatedWood", NCConfig.processor_time[5]/10);
		
		// Redstone Arsenal
		addIngotInfusionRecipes("Electrum", "redstone", FluidHelper.REDSTONE_DUST_VOLUME*2, "ElectrumFlux", 1);
		
		// Thermal Foundation
		addIngotInfusionRecipes("Shibuichi", "redstone", FluidHelper.EUM_DUST_VOLUME, "Signalum", 1);
		addIngotInfusionRecipes("TinSilver", "glowstone", FluidHelper.EUM_DUST_VOLUME, "Lumium", 1);
		addIngotInfusionRecipes("LeadPlatinum", "ender", FluidHelper.EUM_DUST_VOLUME, "Enderium", 1);
	}
	
	public void addOxidizingRecipe(String name, int oxygenAmount) {
		addRecipe(name, fluidStack("oxygen", oxygenAmount), name + "Oxide", NCConfig.processor_time[5]);
	}
	
	public void addFertileOxidizingRecipes(String name) {
		addRecipe("ingot" + name + "Base", fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME), "ingot" + name + "Oxide", NCConfig.processor_time[5]/2);
		addRecipe("nugget" + name, fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME/8), "nugget" + name + "Oxide", NCConfig.processor_time[5]/18);
	}
	
	public void addFissileOxidizingRecipes(String name) {
		addRecipe("ingot" + name, fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME), "ingot" + name + "Oxide", NCConfig.processor_time[5]/2);
		addRecipe("nugget" + name, fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME/8), "nugget" + name + "Oxide", NCConfig.processor_time[5]/18);
	}
	
	public void addFuelOxidizingRecipes(String name) {
		addRecipe("fuel" + name, fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME*10), "fuel" + name + "Oxide", NCConfig.processor_time[5]*4);
		addRecipe("fuelRod" + name, fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME*10), "fuelRod" + name + "Oxide", NCConfig.processor_time[5]*4);
		addRecipe("depletedFuelRod" + name, fluidStack("oxygen", FluidHelper.OXIDIZING_VOLUME*8), "depletedFuelRod" + name + "Oxide", NCConfig.processor_time[5]*4);
	}
	
	public void addIngotInfusionRecipes(String in, String fluid, int amount, String out, int time) {
		addRecipe("ingot" + in, fluidStack(fluid, amount), "ingot" + out, NCConfig.processor_time[5]*time);
		addRecipe("dust" + in, fluidStack(fluid, amount), "dust" + out, NCConfig.processor_time[5]*time);
	}
}
