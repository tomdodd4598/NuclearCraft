package nc;

import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;

public final class IC2Recipes {
	
	public boolean IC2Loaded = false;
	public boolean TELoaded = false;
	
	public void hook() {
		if(Loader.isModLoaded("IC2")) IC2Loaded = true;
		if(IC2Loaded) addIC2Recipes();
	}
	
	@Method(modid = "IC2")
	public static void addIC2Recipes() {
		
		// Mass Fab
		try {RecipesHelp.addMFAmplifier("universalReactant", 1, 16000);} catch(Exception e) {}
		try {RecipesHelp.addMFAmplifier(new ItemStack (NCItems.parts, 1, 4), 1, 16000);} catch(Exception e) {}
		
		// Enriching
		try {Recipes.cannerEnrich.addRecipe(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName(), 1000), new RecipeInputOreDict("universalReactant", 8), FluidRegistry.getFluidStack("ic2coolant", 1000));} catch(Exception e) {}
		try {Recipes.cannerEnrich.addRecipe(FluidRegistry.getFluidStack("distilledWater", 1000), new RecipeInputOreDict("universalReactant", 1), FluidRegistry.getFluidStack("ic2coolant", 1000));} catch(Exception e) {}
		
		// Macerator
		try {RecipesHelp.addMaceratorRecipe("oreThorium", 1, new ItemStack(NCItems.material, 2, 20));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("orePlutonium", 1, new ItemStack(NCItems.material, 2, 33));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("oreLithium", 1, new ItemStack(NCItems.material, 2, 44));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("oreBoron", 1, new ItemStack(NCItems.material, 2, 45));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotUranium", 1, new ItemStack(NCItems.material, 1, 19));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotThorium", 1, new ItemStack(NCItems.material, 1, 20));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotTough", 1, new ItemStack(NCItems.material, 1, 22));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotLithium", 1, new ItemStack(NCItems.material, 1, 44));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotBoron", 1, new ItemStack(NCItems.material, 1, 45));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotMagnesium", 1, new ItemStack(NCItems.material, 1, 51));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotUraniumOxide", 1, new ItemStack(NCItems.material, 1, 54));} catch(Exception e) {}
		try {RecipesHelp.addMaceratorRecipe("ingotMagnesiumDiboride", 1, new ItemStack(NCItems.material, 1, 72));} catch(Exception e) {}
		
		// Compressor
		try {RecipesHelp.addCompressorRecipe(new ItemStack(NCItems.parts, 1, 0), NuclearCraft.workspace ? 4 : 8, new ItemStack(NCItems.parts, 1, 3));} catch(Exception e) {}
		try {RecipesHelp.addCompressorRecipe("ingotTough", 1, new ItemStack(NCItems.parts, 2, 0));} catch(Exception e) {}

		// Thermal Centrifuge
		try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotLithium", 1), 250, new ItemStack(NCItems.material, 1, 47), new ItemStack(NCItems.material, 1, 69));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotBoron", 1), 250, new ItemStack(NCItems.material, 1, 49), new ItemStack(NCItems.material, 3, 70));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustLithium", 1), 250, new ItemStack(NCItems.material, 1, 47), new ItemStack(NCItems.material, 1, 69));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustBoron", 1), 250, new ItemStack(NCItems.material, 1, 49), new ItemStack(NCItems.material, 3, 70));} catch(Exception e) {}
    
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 41), 1, 1000, new ItemStack(NCItems.material, 1, 46));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 42), 1, 1000, new ItemStack(NCItems.material, 1, 47));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 43), 1, 1000, new ItemStack(NCItems.material, 1, 48));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 44), 1, 1000, new ItemStack(NCItems.material, 1, 49));} catch(Exception e) {}
	}
}

