package nc.recipe.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;
import nc.util.OreDictHelper;
import nc.util.StringHelper;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidRegistry;

public class IngotFormerRecipes extends ProcessorRecipeHandler {
	
	public IngotFormerRecipes() {
		super("ingot_former", 0, 1, 1, 0);
	}

	@Override
	public void addRecipes() {
		addIngotFormingRecipes();
		
		addIngotFormingRecipe("thorium", "ThoriumOxide");
		addIngotFormingRecipe("uranium", "UraniumOxide");
		
		addIngotFormingRecipe("hard_carbon", "HardCarbon");
		addIngotFormingRecipe("manganese_dioxide", "ManganeseDioxide");
		
		addRecipe(fluidStack("bas", FluidStackHelper.GEM_VOLUME), "gemBoronArsenide", 2D, 2D);
		
		if (OreDictHelper.oreExists("ingotObsidian")) addRecipe(fluidStack("obsidian", FluidStackHelper.SEARED_MATERIAL_VOLUME), "ingotObsidian", 0.5D, 2D);
		else addRecipe(fluidStack("obsidian", FluidStackHelper.SEARED_BLOCK_VOLUME), "obsidian", 2D, 2D);
		addRecipe(fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME), "ingotRedstone", 0.25D, 1D);
		addRecipe(fluidStack("glowstone", FluidStackHelper.GLOWSTONE_DUST_VOLUME), "ingotGlowstone", 0.25D, 1D);
		addRecipe(fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME), "ingotGraphite", 0.5D, 1D);
		addRecipe(fluidStack("prismarine", FluidStackHelper.INGOT_VOLUME), "gemPrismarine", 1D, 1D);
		
		if (OreDictHelper.oreExists("ingotSilicon")) addRecipe(fluidStack("silicon", FluidStackHelper.INGOT_VOLUME), "ingotSilicon", 1D, 1D);
		else addRecipe(fluidStack("silicon", FluidStackHelper.INGOT_VOLUME), "itemSilicon", 1D, 1D);
		
		// Tinkers' Construct
		addIngotFormingRecipe("Manyullyn");
		addIngotFormingRecipe("Alubrass");
		addIngotFormingRecipe("Pigiron");
		addIngotFormingRecipe("Brass");
		addIngotFormingRecipe("Bronze");
		addIngotFormingRecipe("Electrum");
		addIngotFormingRecipe("Steel");
		
		// Mekanism
		addRecipe(fluidStack("brine", 15), "dustSalt", 0.25D, 0.5D);
		
		// Sweets
		addRecipe(fluidStack("cocoa_butter", FluidStackHelper.INGOT_VOLUME), NCItems.cocoa_butter, 0.5D, 0.5D);
		addRecipe(fluidStack("unsweetened_chocolate", FluidStackHelper.INGOT_VOLUME), NCItems.unsweetened_chocolate, 0.5D, 0.5D);
		addRecipe(fluidStack("dark_chocolate", FluidStackHelper.INGOT_VOLUME), NCItems.dark_chocolate, 0.5D, 0.5D);
		addRecipe(fluidStack("milk_chocolate", FluidStackHelper.INGOT_VOLUME), NCItems.milk_chocolate, 0.5D, 0.5D);
		addRecipe(fluidStack("sugar", FluidStackHelper.INGOT_VOLUME), Items.SUGAR, 0.5D, 0.5D);
		addRecipe(fluidStack("gelatin", FluidStackHelper.INGOT_VOLUME), NCItems.gelatin, 0.5D, 0.5D);
		addRecipe(fluidStack("marshmallow", FluidStackHelper.INGOT_VOLUME), NCItems.marshmallow, 0.5D, 0.5D);
		
		// Fission Isotopes
		addIsotopeFormingRecipes("Thorium", 230);
		addIngotFormingRecipe("fuel_tbu", "Thorium232Base");
		addIsotopeFormingRecipes("Uranium", 233, 235, 238);
		addIsotopeFormingRecipes("Neptunium", 236, 237);
		addIsotopeFormingRecipes("Plutonium", 238, 239, 241, 242);
		addIsotopeFormingRecipes("Americium", 241, 242, 243);
		addIsotopeFormingRecipes("Curium", 243, 245, 246, 247);
		addIsotopeFormingRecipes("Berkelium", 247, 248);
		addIsotopeFormingRecipes("Californium", 249, 250, 251, 252);
		
		// Fission Fuels
		//addRecipe(fluidStack("fuel_tbu", FluidStackHelper.INGOT_BLOCK_VOLUME), "fuelTBU", NCConfig.processor_time[10]*9);
		addRecipe(fluidStack("depleted_fuel_tbu", FluidStackHelper.NUGGET_VOLUME*64), "depletedFuelTBU", 64D/9D, 1D);
		addFissionFuelFormingRecipes("uranium", "eu", 233, 235);
		addFissionFuelFormingRecipes("neptunium", "en", 236);
		addFissionFuelFormingRecipes("plutonium", "ep", 239, 241);
		addFissionFuelFormingRecipes("americium", "ea", 242);
		addFissionFuelFormingRecipes("curium", "ec", "m", 243, 245, 247);
		addFissionFuelFormingRecipes("berkelium", "eb", 248);
		addFissionFuelFormingRecipes("californium", "ec", "f", 249, 251);
	}
	
	public void addIngotFormingRecipe(String fluid, String metal) {
		addRecipe(fluidStack(fluid.toLowerCase(), FluidStackHelper.INGOT_VOLUME), "ingot" + metal, 1D, 1D);
	}
	
	public void addIngotFormingRecipe(String metal) {
		addIngotFormingRecipe(metal, metal);
	}
	
	public void addIsotopeFormingRecipes(String element, int... types) {
		for (int type : types) addIngotFormingRecipe(element.toLowerCase() + "_" + type, element + type + "Base");
	}
	
	public void addFissionFuelFormingRecipes(String element, String suffix, String suffixExtra, int... types) {
		for (int type : types) {
			addRecipe(fluidStack("fuel_l" + suffix + suffixExtra + "_" + type, FluidStackHelper.INGOT_BLOCK_VOLUME), "fuelL" + suffix.toUpperCase() + suffixExtra + type, 9D, 1D);
			addRecipe(fluidStack("fuel_h" + suffix + suffixExtra + "_" + type, FluidStackHelper.INGOT_BLOCK_VOLUME), "fuelH" + suffix.toUpperCase() + suffixExtra + type, 9D, 1D);
			addRecipe(fluidStack("depleted_fuel_l" + suffix + suffixExtra + "_" + type, FluidStackHelper.NUGGET_VOLUME*64), "depletedFuelL" + suffix.toUpperCase() + suffixExtra + type, 64D/9D, 1D);
			addRecipe(fluidStack("depleted_fuel_h" + suffix + suffixExtra + "_" + type, FluidStackHelper.NUGGET_VOLUME*64), "depletedFuelH" + suffix.toUpperCase() + suffixExtra + type, 64D/9D, 1D);
		}
	}
	
	public void addFissionFuelFormingRecipes(String element, String suffix, int... types) {
		addFissionFuelFormingRecipes(element, suffix, "", types);
	}
	
	private static final List<String> CASTING_BLACKLIST = Arrays.asList("glass", "coal", "redstone", "glowstone", "prismarine", "obsidian", "silicon");
	
	public void addIngotFormingRecipes() {
		ArrayList<String> fluidList = new ArrayList(FluidRegistry.getRegisteredFluids().keySet());
		for (String fluidName : fluidList) {
			if (CASTING_BLACKLIST.contains(fluidName)) continue;
			String materialName = StringHelper.capitalize(fluidName);
			String ingot = "ingot" + materialName;
			String gem = "gem" + materialName;
			if (OreDictHelper.oreExists(ingot)) addRecipe(fluidStack(fluidName, FluidStackHelper.INGOT_VOLUME), ingot, 1D, 1D);
			else if (OreDictHelper.oreExists(gem)) addRecipe(fluidStack(fluidName, FluidStackHelper.GEM_VOLUME), gem, 1D, 1D);
		}
	}
}
