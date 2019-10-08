package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InfuserRecipes extends ProcessorRecipeHandler {
	
	public InfuserRecipes() {
		super("infuser", 1, 1, 1, 0);
	}

	@Override
	public void addRecipes() {
		addOxidizingRecipe("ingotThorium", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("ingotUranium", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("ingotManganese", FluidStackHelper.BUCKET_VOLUME);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), "ingotManganeseDioxide", 1D, 1D);
		addOxidizingRecipe("dustThorium", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("dustUranium", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("dustManganese", FluidStackHelper.BUCKET_VOLUME);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), "dustManganeseDioxide", 1D, 1D);
		
		addRecipe(Lists.newArrayList(Blocks.ICE, Blocks.PACKED_ICE), fluidStack("liquid_helium", 50), NCBlocks.supercold_ice, 0.2D, 0.5D);
		
		addRecipe("emptyHeatSink", fluidStack("water", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink, 1, 0), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("liquid_nitrogen", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 12), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("liquid_helium", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 13), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("enderium", FluidStackHelper.INGOT_VOLUME*8), new ItemStack(NCBlocks.solid_fission_sink2, 1, 14), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("cryotheum", FluidStackHelper.BUCKET_VOLUME*2), new ItemStack(NCBlocks.solid_fission_sink2, 1, 15), 1D, 1D);
		
		addRecipe(oreStack("bioplastic", 2), fluidStack("radaway", FluidStackHelper.BUCKET_VOLUME/4), NCItems.radaway, 1D, 0.5D);
		addRecipe(oreStack("bioplastic", 2), fluidStack("radaway_slow", FluidStackHelper.BUCKET_VOLUME/4), NCItems.radaway_slow, 1D, 0.5D);
		addRecipe(NCItems.radaway, fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*2), NCItems.radaway_slow, 1D, 0.5D);
		
		addRecipe("emptyFrame", fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), NCBlocks.water_source, 1D, 1D);
		addRecipe(NCBlocks.water_source, fluidStack("lava", FluidStackHelper.BUCKET_VOLUME), NCBlocks.cobblestone_generator, 1D, 1D);
		addRecipe("emptyFrame", fluidStackList(Lists.newArrayList("heavywater", "heavy_water"), FluidStackHelper.BUCKET_VOLUME), NCBlocks.heavy_water_moderator, 1D, 1D);
		
		addRecipe(OreDictHelper.oreExists("blockGlassHardened") ? "blockGlassHardened" : "blockGlass", fluidStack("tritium", FluidStackHelper.BUCKET_VOLUME), NCBlocks.tritium_lamp, 1D, 1D);
		
		addRecipe("sandstone", fluidStack("ender", FluidStackHelper.EUM_DUST_VOLUME), Blocks.END_STONE, 1D, 1D);
		
		for (int meta = 0; meta < 16; meta++) {
			addRecipe(new ItemStack(Blocks.CONCRETE_POWDER, 1, meta), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), new ItemStack(Blocks.CONCRETE, 1, meta), 0.5D, 0.5D);
		}
		
		// Immersive Engineering
		addRecipe("plankWood", fluidStack("creosote", 125), RegistryHelper.blockStackFromRegistry("immersiveengineering:treated_wood"), 0.25D, 0.5D);
		
		// Redstone Arsenal
		addIngotInfusionRecipes("Electrum", "redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*2, "ElectrumFlux", 1D, 1D);
		
		// Thermal Foundation
		addIngotInfusionRecipes("Shibuichi", "redstone", FluidStackHelper.EUM_DUST_VOLUME, "Signalum", 1D, 1D);
		addIngotInfusionRecipes("TinSilver", "glowstone", FluidStackHelper.EUM_DUST_VOLUME, "Lumium", 1D, 1D);
		addIngotInfusionRecipes("LeadPlatinum", "ender", FluidStackHelper.EUM_DUST_VOLUME, "Enderium", 1D, 1D);
		
		// Mekanism
		addRecipe(Lists.newArrayList("dirt", "grass"), fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), Blocks.CLAY, 1D, 1D);
		addRecipe("ingotBrick", fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), Items.CLAY_BALL, 1D, 1D);
		addRecipe(Blocks.HARDENED_CLAY, fluidStack("water", FluidStackHelper.BUCKET_VOLUME*4), Blocks.CLAY, 4D, 1D);
		
		// Fission Materials
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
	}
	
	public void addOxidizingRecipe(String name, int oxygenAmount) {
		addRecipe(name, fluidStack("oxygen", oxygenAmount), name + "Oxide", 1D, 1D);
	}
	
	public void addFertileOxidizingRecipes(String name) {
		addRecipe("ingot" + name + "Base", fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME), "ingot" + name + "Oxide", 0.5D, 1D);
		addRecipe("nugget" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME/8), "nugget" + name + "Oxide", 1D/18D, 1D);
	}
	
	public void addFissileOxidizingRecipes(String name) {
		addRecipe("ingot" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME), "ingot" + name + "Oxide", 0.5D, 1D);
		addRecipe("nugget" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME/8), "nugget" + name + "Oxide", 1D/18D, 1D);
	}
	
	public void addFuelOxidizingRecipes(String name) {
		addRecipe("fuel" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME*10), "fuel" + name + "Oxide", 2D, 2D);
		addRecipe("fuelRod" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME*10), "fuelRod" + name + "Oxide", 2D, 2D);
		addRecipe("depletedFuel" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME*8), "depletedFuel" + name + "Oxide", 2D, 2D);
		addRecipe("depletedFuelRod" + name, fluidStack("oxygen", FluidStackHelper.OXIDIZING_VOLUME*8), "depletedFuelRod" + name + "Oxide", 2D, 2D);
	}
	
	public void addIngotInfusionRecipes(String in, String fluid, int amount, String out, double time, double power) {
		addRecipe("ingot" + in, fluidStack(fluid, amount), "ingot" + out, time, power);
		addRecipe("dust" + in, fluidStack(fluid, amount), "dust" + out, time, power);
	}
}
