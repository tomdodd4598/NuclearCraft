package nc.recipe.processor;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;
import nc.util.OreDictHelper;
import nc.util.StringHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidRegistry;

public class MelterRecipes extends BaseRecipeHandler {
	
	public MelterRecipes() {
		super("melter", 1, 0, 0, 1);
	}

	@Override
	public void addRecipes() {
		addRecipe("dustSulfur", fluidStack("sulfur", FluidHelper.GEM_VOLUME), NCConfig.processor_time[6]);
		addRecipe("dustSodiumHydroxide", fluidStack("naoh", FluidHelper.GEM_VOLUME), NCConfig.processor_time[6]);
		addRecipe("dustPotassiumHydroxide", fluidStack("koh", FluidHelper.GEM_VOLUME), NCConfig.processor_time[6]);
		
		addIngotMeltingRecipes("boron10");
		addIngotMeltingRecipes("boron11");
		addIngotMeltingRecipes("lithium6");
		addIngotMeltingRecipes("lithium7");
		
		// Tinkers' Construct
		addRecipe("obsidian", fluidStack("obsidian", FluidHelper.SEARED_BLOCK_VOLUME), NCConfig.processor_time[6]*4);
		addRecipe("sand", fluidStack("glass", FluidHelper.GLASS_VOLUME), NCConfig.processor_time[6]);
		addRecipe("blockGlass", fluidStack("glass", FluidHelper.GLASS_VOLUME), NCConfig.processor_time[6]);
		addRecipe(Blocks.CLAY, fluidStack("clay", FluidHelper.BRICK_BLOCK_VOLUME), NCConfig.processor_time[6]*4);
		addRecipe(Items.CLAY_BALL, fluidStack("clay", FluidHelper.BRICK_VOLUME), NCConfig.processor_time[6]);
		addRecipe(Blocks.BRICK_BLOCK, fluidStack("clay", FluidHelper.BRICK_BLOCK_VOLUME), NCConfig.processor_time[6]*4);
		addRecipe("ingotBrick", fluidStack("clay", FluidHelper.BRICK_VOLUME), NCConfig.processor_time[6]);
		addRecipe(Blocks.HARDENED_CLAY, fluidStack("clay", FluidHelper.BRICK_BLOCK_VOLUME), NCConfig.processor_time[6]*4);
		addRecipe("stone", fluidStack("stone", FluidHelper.SEARED_MATERIAL_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe("cobblestone", fluidStack("stone", FluidHelper.SEARED_MATERIAL_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe("dirt", fluidStack("dirt", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[6]);
		addRecipe("grass", fluidStack("dirt", FluidHelper.INGOT_VOLUME), NCConfig.processor_time[6]);
		
		// Thermal Expansion
		addRecipe("dustRedstone", fluidStack("redstone", FluidHelper.REDSTONE_DUST_VOLUME), NCConfig.processor_time[6]/4);
		addRecipe("blockRedstone", fluidStack("redstone", FluidHelper.REDSTONE_BLOCK_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe("dustGlowstone", fluidStack("glowstone", FluidHelper.GLOWSTONE_DUST_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("glowstone", fluidStack("glowstone", FluidHelper.GLOWSTONE_BLOCK_VOLUME), NCConfig.processor_time[6]*4);
		addRecipe(Items.ENDER_PEARL, fluidStack("ender", FluidHelper.ENDER_PEARL_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustEnder", fluidStack("ender", FluidHelper.ENDER_PEARL_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustPyrotheum", fluidStack("pyrotheum", FluidHelper.EUM_DUST_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustCryotheum", fluidStack("cryotheum", FluidHelper.EUM_DUST_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustAerotheum", fluidStack("aerotheum", FluidHelper.EUM_DUST_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustPetrotheum", fluidStack("petrotheum", FluidHelper.EUM_DUST_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustCoal", fluidStack("coal", FluidHelper.COAL_DUST_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe("dustGraphite", fluidStack("coal", FluidHelper.COAL_DUST_VOLUME), NCConfig.processor_time[6]/4);
		
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
			for (String prefix : new String[] {"fuel", "fuelRod"}) addRecipe(prefix + "TBU" + suffix, fluidStack("fuel_tbu", FluidHelper.INGOT_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
			for (String prefix : new String[] {"depletedFuel", "depletedFuelRod"}) addRecipe(prefix + "TBU" + suffix, fluidStack("depleted_fuel_tbu", FluidHelper.NUGGET_VOLUME*64), NCConfig.processor_time[6]*64/9);
		}
		addFissionFuelMeltingRecipes("uranium", "eu", 233, 235);
		addFissionFuelMeltingRecipes("neptunium", "en", 236);
		addFissionFuelMeltingRecipes("plutonium", "ep", 239, 241);
		addFissionFuelMeltingRecipes("americium", "ea", 242);
		addFissionFuelMeltingRecipes("curium", "ec", "m", 243, 245, 247);
		addFissionFuelMeltingRecipes("berkelium", "eb", 248);
		addFissionFuelMeltingRecipes("californium", "ec", "f", 249, 251);
		
		addRecipe(Blocks.ICE, fluidStack("water", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[6]/2);
		addRecipe(Blocks.PACKED_ICE, fluidStack("water", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[6]/2);
		
		addOreMeltingRecipes();
		
		addRecipe("blockQuartz", fluidStack("quartz", FluidHelper.GEM_VOLUME*4), NCConfig.processor_time[6]*4);
		addRecipe("blockLapis", fluidStack("lapis", FluidHelper.GEM_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
		addRecipe("blockDiamond", fluidStack("diamond", FluidHelper.GEM_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
		addRecipe("blockEmerald", fluidStack("emerald", FluidHelper.GEM_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
	}
	
	public void addIngotMeltingRecipes(String oreName, String fluidName) {
		oreName = StringHelper.capitalize(oreName);
		addRecipe("ore" + oreName, fluidStack(fluidName, FluidHelper.INGOT_ORE_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe(Lists.newArrayList("ingot" + oreName, "dust" + oreName), fluidStack(fluidName, FluidHelper.INGOT_VOLUME), NCConfig.processor_time[6]);
		addRecipe(Lists.newArrayList("nugget" + oreName, "tinyDust" + oreName), fluidStack(fluidName, FluidHelper.NUGGET_VOLUME), NCConfig.processor_time[6]/9);
		addRecipe("block" + oreName, fluidStack(fluidName, FluidHelper.INGOT_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
	}
	
	public void addIngotMeltingRecipes(String name) {
		addIngotMeltingRecipes(name, name);
	}
	
	public void addIngotOreMeltingRecipes(String name) {
		String oreName = StringHelper.capitalize(name);
		addRecipe("ore" + oreName, fluidStack(name, FluidHelper.INGOT_ORE_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe("ingot" + oreName, fluidStack(name, FluidHelper.INGOT_VOLUME), NCConfig.processor_time[6]);
		addRecipe("nugget" + oreName, fluidStack(name, FluidHelper.NUGGET_VOLUME), NCConfig.processor_time[6]/9);
		addRecipe("block" + oreName, fluidStack(name, FluidHelper.INGOT_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
	}
	
	public void addGemMeltingRecipes(String name) {
		String oreName = StringHelper.capitalize(name);
		addRecipe("ore" + oreName, fluidStack(name, FluidHelper.GEM_ORE_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe(Lists.newArrayList("gem" + oreName, "dust" + oreName), fluidStack(name, FluidHelper.GEM_VOLUME), NCConfig.processor_time[6]);
		addRecipe(Lists.newArrayList("nugget" + oreName, "tinyDust" + oreName), fluidStack(name, FluidHelper.GEM_NUGGET_VOLUME), NCConfig.processor_time[6]/9);
	}
	
	public void addGemOreMeltingRecipes(String name) {
		String oreName = StringHelper.capitalize(name);
		addRecipe("ore" + oreName, fluidStack(name, FluidHelper.GEM_ORE_VOLUME), NCConfig.processor_time[6]*2);
		addRecipe("gem" + oreName, fluidStack(name, FluidHelper.GEM_VOLUME), NCConfig.processor_time[6]);
		addRecipe("nugget" + oreName, fluidStack(name, FluidHelper.GEM_NUGGET_VOLUME), NCConfig.processor_time[6]/9);
	}
	
	public void addIsotopeMeltingRecipes(String element, int... types) {
		for (int type : types) addIngotMeltingRecipes(element + type, element + "_" + type);
	}
	
	public void addFissionFuelMeltingRecipes(String element, String suffix, String suffixExtra, int... types) {
		for (int type : types) for (String oxide : new String[] {"", "Oxide"}) {
			for (String prefix : new String[] {"fuel", "fuelRod"}) {
				addRecipe(prefix + "L" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("fuel_l" + suffix + suffixExtra + "_" + type, FluidHelper.INGOT_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
				addRecipe(prefix + "H" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("fuel_h" + suffix + suffixExtra + "_" + type, FluidHelper.INGOT_BLOCK_VOLUME), NCConfig.processor_time[6]*9);
			}
			for (String prefix : new String[] {"depletedFuel", "depletedFuelRod"}) {
				addRecipe(prefix + "L" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("depleted_fuel_l" + suffix + suffixExtra + "_" + type, FluidHelper.NUGGET_VOLUME*64), NCConfig.processor_time[6]*64/9);
				addRecipe(prefix + "H" + suffix.toUpperCase() + suffixExtra + type + oxide, fluidStack("depleted_fuel_h" + suffix + suffixExtra + "_" + type, FluidHelper.NUGGET_VOLUME*64), NCConfig.processor_time[6]*64/9);
			}
		}
	}
	
	public void addFissionFuelMeltingRecipes(String element, String suffix, int... types) {
		addFissionFuelMeltingRecipes(element, suffix, "", types);
	}
	
	public void addOreMeltingRecipes() {
		ArrayList<String> fluidList = new ArrayList(FluidRegistry.getRegisteredFluids().keySet());
		for (String fluidName : fluidList) {
			if (fluidName == "coal") continue;
			String materialName = StringHelper.capitalize(fluidName);
			String ore = "ore" + materialName;
			String ingot = "ingot" + materialName;
			String gem = "gem" + materialName;
			String dust = "dust" + materialName;
			if (OreDictHelper.oreExists(ingot) && OreDictHelper.oreExists(dust)) addIngotMeltingRecipes(fluidName);
			else if (OreDictHelper.oreExists(ingot) && OreDictHelper.oreExists(ore)) addIngotOreMeltingRecipes(fluidName);
			else if (OreDictHelper.oreExists(gem) && OreDictHelper.oreExists(dust)) addGemMeltingRecipes(fluidName);
			else if (OreDictHelper.oreExists(gem) && OreDictHelper.oreExists(ore)) addGemOreMeltingRecipes(fluidName);
		}
	}
}
