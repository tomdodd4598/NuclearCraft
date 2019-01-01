package nc.recipe.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;
import nc.util.OreDictHelper;
import nc.util.StringHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidRegistry;

public class MelterRecipes extends ProcessorRecipeHandler {
	
	public MelterRecipes() {
		super("melter", 1, 0, 0, 1);
	}

	@Override
	public void addRecipes() {
		addRecipe("dustSulfur", fluidStack("sulfur", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumHydroxide", fluidStack("naoh", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		addRecipe("dustPotassiumHydroxide", fluidStack("koh", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		addRecipe("dustArsenic", fluidStack("arsenic", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		
		addRecipe(Lists.newArrayList("gemPrismarine", "dustPrismarine"), fluidStack("prismarine", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addIngotMeltingRecipes("boron10");
		addIngotMeltingRecipes("boron11");
		addIngotMeltingRecipes("lithium6");
		addIngotMeltingRecipes("lithium7");
		
		addIngotMeltingRecipes("ferroboron");
		addIngotMeltingRecipes("tough");
		addIngotMeltingRecipes("hardCarbon", "hard_carbon");
		
		addRecipe(Lists.newArrayList("ingotSilicon", "itemSilicon"), fluidStack("silicon", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addRecipe(NCItems.ground_cocoa_nibs, fluidStack("chocolate_liquor", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(NCItems.cocoa_butter, fluidStack("cocoa_butter", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(NCItems.unsweetened_chocolate, fluidStack("unsweetened_chocolate", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(NCItems.dark_chocolate, fluidStack("dark_chocolate", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(NCItems.milk_chocolate, fluidStack("milk_chocolate", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(Items.SUGAR, fluidStack("sugar", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe(NCItems.gelatin, fluidStack("gelatin", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe(NCItems.marshmallow, fluidStack("marshmallow", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
		
		// Tinkers' Construct
		addRecipe("obsidian", fluidStack("obsidian", FluidStackHelper.SEARED_BLOCK_VOLUME), 2D, 2D);
		addRecipe(Lists.newArrayList("ingotObsidian", "dustObsidian"), fluidStack("obsidian", FluidStackHelper.SEARED_MATERIAL_VOLUME), 0.5D, 2D);
		addRecipe(Lists.newArrayList("nuggetObsidian", "tinyDustObsidian"), fluidStack("obsidian", FluidStackHelper.SEARED_MATERIAL_VOLUME/9), 0.5D/9D, 2D);
		addRecipe("sand", fluidStack("glass", FluidStackHelper.GLASS_VOLUME), 1.5D, 1.5D);
		addRecipe("blockGlass", fluidStack("glass", FluidStackHelper.GLASS_VOLUME), 1.5D, 1.5D);
		addRecipe(Blocks.CLAY, fluidStack("clay", FluidStackHelper.BRICK_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe(Items.CLAY_BALL, fluidStack("clay", FluidStackHelper.BRICK_VOLUME), 0.5D, 1.5D);
		addRecipe(Blocks.BRICK_BLOCK, fluidStack("clay", FluidStackHelper.BRICK_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe("ingotBrick", fluidStack("clay", FluidStackHelper.BRICK_VOLUME), 0.5D, 1.5D);
		addRecipe(Blocks.HARDENED_CLAY, fluidStack("clay", FluidStackHelper.BRICK_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe("stone", fluidStack("stone", FluidStackHelper.SEARED_MATERIAL_VOLUME), 1.5D, 1.5D);
		addRecipe("cobblestone", fluidStack("stone", FluidStackHelper.SEARED_MATERIAL_VOLUME), 1.5D, 1.5D);
		addRecipe(Lists.newArrayList("dirt", "grass"), fluidStack("dirt", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		// Thermal Expansion
		addRecipe(Lists.newArrayList("dustRedstone", "ingotRedstone"), fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME), 0.25D, 1D);
		addRecipe("blockRedstone", fluidStack("redstone", FluidStackHelper.REDSTONE_BLOCK_VOLUME), 2D, 1D);
		addRecipe("dustGlowstone", fluidStack("glowstone", FluidStackHelper.GLOWSTONE_DUST_VOLUME), 0.25D, 1D);
		addRecipe("glowstone", fluidStack("glowstone", FluidStackHelper.GLOWSTONE_BLOCK_VOLUME), 2D, 1D);
		addRecipe(Lists.newArrayList(Items.ENDER_PEARL, "dustEnder"), fluidStack("ender", FluidStackHelper.ENDER_PEARL_VOLUME), 0.5D, 1.5D);
		addRecipe("dustPyrotheum", fluidStack("pyrotheum", FluidStackHelper.EUM_DUST_VOLUME), 1D, 1D);
		addRecipe("dustCryotheum", fluidStack("cryotheum", FluidStackHelper.EUM_DUST_VOLUME), 1D, 1D);
		addRecipe("dustAerotheum", fluidStack("aerotheum", FluidStackHelper.EUM_DUST_VOLUME), 1D, 1D);
		addRecipe("dustPetrotheum", fluidStack("petrotheum", FluidStackHelper.EUM_DUST_VOLUME), 1D, 1D);
		addRecipe(Lists.newArrayList("coal", "dustCoal"), fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME), 0.5D, 1D);
		addRecipe(Lists.newArrayList("ingotGraphite", "dustGraphite"), fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME), 0.5D, 1D);
		addRecipe("blockCoal", fluidStack("coal", FluidStackHelper.COAL_BLOCK_VOLUME), 4.5D, 1D);
		addRecipe("blockGraphite", fluidStack("coal", FluidStackHelper.COAL_BLOCK_VOLUME), 4.5D, 1D);
		
		// Advanced Rocketry
		addIngotMeltingRecipes("dilithium");
		addIngotMeltingRecipes("titanium");
		
		// PlusTIC - Botania
		addIngotMeltingRecipes("manasteel");
		addIngotMeltingRecipes("terrasteel");
		addIngotMeltingRecipes("elementium");
		
		// PlusTIC - Mekanism
		addIngotMeltingRecipes("refinedObsidian");
		addIngotMeltingRecipes("refinedGlowstone");
		
		// PlusTIC - Psi
		addIngotMeltingRecipes("psi");
		
		// JAOPCA - AstralSorcery
		addIngotMeltingRecipes("astralStarmetal");
		
		// JAOPCA - AstralSorcery
		addIngotMeltingRecipes("baseEssence");
		addIngotMeltingRecipes("inferium");
		addIngotMeltingRecipes("prudentium");
		addIngotMeltingRecipes("intermedium");
		addIngotMeltingRecipes("superium");
		addIngotMeltingRecipes("supremium");
		
		// Fission Isotopes
		addIsotopeMeltingRecipes("thorium", 230);
		addIngotMeltingRecipes("thorium232", "fuel_tbu");
		addIsotopeMeltingRecipes("uranium", 233, 235, 238);
		addIsotopeMeltingRecipes("neptunium", 236, 237);
		addIsotopeMeltingRecipes("plutonium", 238, 239, 241, 242);
		addIsotopeMeltingRecipes("americium", 241, 242, 243);
		addIsotopeMeltingRecipes("curium", 243, 245, 246, 247);
		addIsotopeMeltingRecipes("berkelium", 247, 248);
		addIsotopeMeltingRecipes("californium", 249, 250, 251, 252);
		
		// Fission Fuels
		for (String suffix : new String[] {"", "Oxide"}) {
			for (String prefix : new String[] {"fuel", "fuelRod"}) addRecipe(prefix + "TBU" + suffix, fluidStack("fuel_tbu", FluidStackHelper.INGOT_BLOCK_VOLUME), 9D);
			for (String prefix : new String[] {"depletedFuel", "depletedFuelRod"}) addRecipe(prefix + "TBU" + suffix, fluidStack("depleted_fuel_tbu", FluidStackHelper.NUGGET_VOLUME*64), 64D/9D);
		}
		addFissionFuelMeltingRecipes("uranium", "eu", 233, 235);
		addFissionFuelMeltingRecipes("neptunium", "en", 236);
		addFissionFuelMeltingRecipes("plutonium", "ep", 239, 241);
		addFissionFuelMeltingRecipes("americium", "ea", 242);
		addFissionFuelMeltingRecipes("curium", "ec", "m", 243, 245, 247);
		addFissionFuelMeltingRecipes("berkelium", "eb", 248);
		addFissionFuelMeltingRecipes("californium", "ec", "f", 249, 251);
		
		addRecipe(Blocks.ICE, fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 0.25D, 0.5D);
		addRecipe(Blocks.PACKED_ICE, fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 0.5D, 0.5D);
		
		addOreMeltingRecipes();
		
		addRecipe("blockQuartz", fluidStack("quartz", FluidStackHelper.GEM_VOLUME*4), 4D, 1D);
		addRecipe("blockLapis", fluidStack("lapis", FluidStackHelper.GEM_BLOCK_VOLUME), 9D, 1D);
		addRecipe("blockDiamond", fluidStack("diamond", FluidStackHelper.GEM_BLOCK_VOLUME), 9D, 1D);
		addRecipe("blockEmerald", fluidStack("emerald", FluidStackHelper.GEM_BLOCK_VOLUME), 9D, 1D);
	}
	
	public void addIngotMeltingRecipes(String oreName, String fluidName) {
		oreName = StringHelper.capitalize(oreName);
		addRecipe("ore" + oreName, fluidStack(fluidName, FluidStackHelper.INGOT_ORE_VOLUME), 1.25D, 1.5D);
		addRecipe(Lists.newArrayList("ingot" + oreName, "dust" + oreName), fluidStack(fluidName, FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(Lists.newArrayList("nugget" + oreName, "tinyDust" + oreName), fluidStack(fluidName, FluidStackHelper.NUGGET_VOLUME), 1D/9D, 1D);
		addRecipe("block" + oreName, fluidStack(fluidName, FluidStackHelper.INGOT_BLOCK_VOLUME), 9D, 1D);
	}
	
	public void addIngotMeltingRecipes(String name) {
		addIngotMeltingRecipes(name, name);
	}
	
	public void addGemMeltingRecipes(String name) {
		String oreName = StringHelper.capitalize(name);
		addRecipe("ore" + oreName, fluidStack(name, FluidStackHelper.GEM_ORE_VOLUME), 1.25D, 1.5D);
		addRecipe(Lists.newArrayList("gem" + oreName, "dust" + oreName), fluidStack(name, FluidStackHelper.GEM_VOLUME), 1D, 1D);
		addRecipe(Lists.newArrayList("nugget" + oreName, "tinyDust" + oreName), fluidStack(name, FluidStackHelper.GEM_NUGGET_VOLUME), 1D/9D, 1D);
	}
	
	public void addIsotopeMeltingRecipes(String element, int... types) {
		for (int type : types) addIngotMeltingRecipes(element + type, element + "_" + type);
	}
	
	public void addFissionFuelMeltingRecipes(String element, String suffix, String suffixExtra, int... types) {
		for (int type : types) for (String oxide : new String[] {"", "Oxide"}) {
			for (String prefix : new String[] {"fuel", "fuelRod"}) {
				addRecipe(prefix + "L" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("fuel_l" + suffix + suffixExtra + "_" + type, FluidStackHelper.INGOT_BLOCK_VOLUME), 9D, 1D);
				addRecipe(prefix + "H" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("fuel_h" + suffix + suffixExtra + "_" + type, FluidStackHelper.INGOT_BLOCK_VOLUME), 9D, 1D);
			}
			for (String prefix : new String[] {"depletedFuel", "depletedFuelRod"}) {
				addRecipe(prefix + "L" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("depleted_fuel_l" + suffix + suffixExtra + "_" + type, FluidStackHelper.NUGGET_VOLUME*64), 64D/9D, 1D);
				addRecipe(prefix + "H" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("depleted_fuel_h" + suffix + suffixExtra + "_" + type, FluidStackHelper.NUGGET_VOLUME*64), 64D/9D, 1D);
			}
		}
	}
	
	public void addFissionFuelMeltingRecipes(String element, String suffix, int... types) {
		addFissionFuelMeltingRecipes(element, suffix, "", types);
	}
	
	private static final List<String> MELTING_BLACKLIST = Arrays.asList("coal", "redstone", "glowstone", "prismarine", "obsidian", "silicon");
	
	public void addOreMeltingRecipes() {
		ArrayList<String> fluidList = new ArrayList(FluidRegistry.getRegisteredFluids().keySet());
		for (String fluidName : fluidList) {
			if (MELTING_BLACKLIST.contains(fluidName)) continue;
			String materialName = StringHelper.capitalize(fluidName);
			String ore = "ore" + materialName;
			String ingot = "ingot" + materialName;
			String gem = "gem" + materialName;
			String dust = "dust" + materialName;
			
			if (OreDictHelper.oreExists(ingot) && OreDictHelper.oreExists(dust)) addIngotMeltingRecipes(fluidName);
			else if (OreDictHelper.oreExists(gem) && OreDictHelper.oreExists(dust)) addGemMeltingRecipes(fluidName);
		}
	}
}
