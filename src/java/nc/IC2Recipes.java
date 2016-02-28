package nc;

import ic2.api.recipe.RecipeInputOreDict;
import nc.item.NCItems;
import net.minecraft.item.ItemStack;
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
		
		//Mass Fab
		try {RecipesHelp.addMFAmplifier("universalReactant", 1, 16000);} catch(Exception e) {}
		try {RecipesHelp.addMFAmplifier(new ItemStack (NCItems.parts, 1, 4), 1, 16000);} catch(Exception e) {}
		
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
		
		// Compressor
		try {RecipesHelp.addCompressorRecipe(new ItemStack(NCItems.parts, 1, 0), 6, new ItemStack(NCItems.parts, 1, 3));} catch(Exception e) {}
		try {RecipesHelp.addCompressorRecipe("ingotTough", 1, new ItemStack(NCItems.parts, 2, 0));} catch(Exception e) {}

		// Thermal Centrifuge
		try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 0), 1, 250, new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 26));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 6), 1, 250, new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 28));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 1), 1, 250, new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 26));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 7), 1, 250, new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 28));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 2), 1, 250, new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 32));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 8), 1, 250, new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 36));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 3), 1, 250, new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 32));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 9), 1, 250, new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 36));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 4), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 63));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 10), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 67));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 5), 1, 250, new ItemStack(NCItems.material, 5, 38), new ItemStack(NCItems.material, 4, 38));} catch(Exception e) {}
	   
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 11), 1, 250, new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 26));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 17), 1, 250, new ItemStack(NCItems.material, 8, 24), new ItemStack(NCItems.material, 1, 28));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 12), 1, 250, new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 26));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 18), 1, 250, new ItemStack(NCItems.material, 5, 24), new ItemStack(NCItems.material, 4, 28));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 13), 1, 250, new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 32));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 19), 1, 250, new ItemStack(NCItems.material, 8, 34), new ItemStack(NCItems.material, 1, 36));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 14), 1, 250, new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 32));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 20), 1, 250, new ItemStack(NCItems.material, 5, 34), new ItemStack(NCItems.material, 4, 36));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 15), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 63));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 21), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 67));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 16), 1, 250, new ItemStack(NCItems.material, 5, 38), new ItemStack(NCItems.material, 4, 38));} catch(Exception e) {}
	   
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 22), 1, 250, new ItemStack(NCItems.material, 32, 25), new ItemStack(NCItems.material, 4, 33));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 28), 1, 250, new ItemStack(NCItems.material, 28, 25), new ItemStack(NCItems.material, 8, 33));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 23), 1, 250, new ItemStack(NCItems.material, 20, 35), new ItemStack(NCItems.material, 16, 25));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 29), 1, 250, new ItemStack(NCItems.material, 24, 35), new ItemStack(NCItems.material, 12, 25));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 24), 1, 250, new ItemStack(NCItems.material, 28, 35), new ItemStack(NCItems.material, 8, 37));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 30), 1, 250, new ItemStack(NCItems.material, 24, 35), new ItemStack(NCItems.material, 12, 33));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 25), 1, 250, new ItemStack(NCItems.material, 32, 35), new ItemStack(NCItems.material, 4, 31));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 31), 1, 250, new ItemStack(NCItems.material, 28, 35), new ItemStack(NCItems.material, 8, 31));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 26), 1, 250, new ItemStack(NCItems.material, 20, 64), new ItemStack(NCItems.material, 16, 68));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 32), 1, 250, new ItemStack(NCItems.material, 20, 64), new ItemStack(NCItems.material, 16, 66));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 27), 1, 250, new ItemStack(NCItems.material, 24, 29), new ItemStack(NCItems.material, 12, 39));} catch(Exception e) {}
	   	
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 51), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 57));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 55), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 59));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 52), 1, 250, new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 57));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 56), 1, 250, new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 59));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 53), 1, 250, new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 63));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 57), 1, 250, new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 67));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 54), 1, 250, new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 63));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 58), 1, 250, new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 67));} catch(Exception e) {}
	   	
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 59), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 57));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 63), 1, 250, new ItemStack(NCItems.material, 8, 55), new ItemStack(NCItems.material, 1, 59));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 60), 1, 250, new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 57));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 64), 1, 250, new ItemStack(NCItems.material, 5, 55), new ItemStack(NCItems.material, 4, 59));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 61), 1, 250, new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 63));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 65), 1, 250, new ItemStack(NCItems.material, 8, 65), new ItemStack(NCItems.material, 1, 67));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 62), 1, 250, new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 63));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 66), 1, 250, new ItemStack(NCItems.material, 5, 65), new ItemStack(NCItems.material, 4, 67));} catch(Exception e) {}
	   	
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 67), 1, 250, new ItemStack(NCItems.material, 32, 56), new ItemStack(NCItems.material, 4, 64));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 71), 1, 250, new ItemStack(NCItems.material, 28, 56), new ItemStack(NCItems.material, 8, 64));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 68), 1, 250, new ItemStack(NCItems.material, 20, 66), new ItemStack(NCItems.material, 16, 56));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 72), 1, 250, new ItemStack(NCItems.material, 24, 66), new ItemStack(NCItems.material, 12, 56));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 69), 1, 250, new ItemStack(NCItems.material, 28, 66), new ItemStack(NCItems.material, 8, 68));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 73), 1, 250, new ItemStack(NCItems.material, 24, 66), new ItemStack(NCItems.material, 12, 64));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 70), 1, 250, new ItemStack(NCItems.material, 32, 66), new ItemStack(NCItems.material, 4, 62));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 74), 1, 250, new ItemStack(NCItems.material, 28, 66), new ItemStack(NCItems.material, 8, 62));} catch(Exception e) {}

	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustUranium", 1), 250, new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustYellorium", 1), 250, new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustYellorite", 1), 250, new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustThorium", 1), 250, new ItemStack(NCItems.material, 2, 38), new ItemStack(NCItems.material, 1, 41));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotUranium", 1), 250, new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotYellorite", 1), 250, new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotYellorium", 1), 250, new ItemStack(NCItems.material, 2, 24), new ItemStack(NCItems.material, 2, 27));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotThorium", 1), 250, new ItemStack(NCItems.material, 2, 38), new ItemStack(NCItems.material, 1, 41));} catch(Exception e) {}
	   	
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustUraniumOxide", 1), 250, new ItemStack(NCItems.material, 2, 55), new ItemStack(NCItems.material, 2, 58));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotUraniumOxide", 1), 250, new ItemStack(NCItems.material, 2, 55), new ItemStack(NCItems.material, 2, 58));} catch(Exception e) {}
	   	
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotLithium", 1), 250, new ItemStack(NCItems.material, 1, 47), new ItemStack(NCItems.material, 1, 69));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("ingotBoron", 1), 250, new ItemStack(NCItems.material, 1, 49), new ItemStack(NCItems.material, 3, 70));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustLithium", 1), 250, new ItemStack(NCItems.material, 1, 47), new ItemStack(NCItems.material, 1, 69));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new RecipeInputOreDict("dustBoron", 1), 250, new ItemStack(NCItems.material, 1, 49), new ItemStack(NCItems.material, 3, 70));} catch(Exception e) {}
    
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 41), 1, 250, new ItemStack(NCItems.material, 1, 46));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 42), 1, 250, new ItemStack(NCItems.material, 1, 47));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 43), 1, 250, new ItemStack(NCItems.material, 1, 48));} catch(Exception e) {}
	   	try {RecipesHelp.addCentrifugeRecipe(new ItemStack(NCItems.fuel, 1, 44), 1, 250, new ItemStack(NCItems.material, 1, 49));} catch(Exception e) {}
	   	
	   	// Canning Machine
	   	try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack (NCItems.fuel, 1, 0), new ItemStack(NCItems.fuel, 1, 11));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 6), new ItemStack (NCItems.fuel, 1, 17));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 1), new ItemStack (NCItems.fuel, 1, 12));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 7), new ItemStack (NCItems.fuel, 1, 18));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 2), new ItemStack (NCItems.fuel, 1, 13));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 8), new ItemStack (NCItems.fuel, 1, 19));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 3), new ItemStack (NCItems.fuel, 1, 14));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 9), new ItemStack (NCItems.fuel, 1, 20));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 4), new ItemStack (NCItems.fuel, 1, 15));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 10), new ItemStack (NCItems.fuel, 1, 21));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 5), new ItemStack (NCItems.fuel, 1, 16));} catch(Exception e) {}
		
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack (NCItems.fuel, 1, 51), new ItemStack(NCItems.fuel, 1, 59));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 55), new ItemStack (NCItems.fuel, 1, 63));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 52), new ItemStack (NCItems.fuel, 1, 60));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 56), new ItemStack (NCItems.fuel, 1, 64));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 53), new ItemStack (NCItems.fuel, 1, 61));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 57), new ItemStack (NCItems.fuel, 1, 65));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 54), new ItemStack (NCItems.fuel, 1, 62));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.fuel, 1, 58), new ItemStack (NCItems.fuel, 1, 66));} catch(Exception e) {}
		
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.material, 1, 46), new ItemStack (NCItems.fuel, 1, 41));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.material, 1, 47), new ItemStack (NCItems.fuel, 1, 42));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.material, 1, 48), new ItemStack (NCItems.fuel, 1, 43));} catch(Exception e) {}
		try {RecipesHelp.addBottleRecipe(new ItemStack (NCItems.fuel, 1, 33), new ItemStack(NCItems.material, 1, 49), new ItemStack (NCItems.fuel, 1, 44));} catch(Exception e) {}
	}
}

