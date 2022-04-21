package nc.recipe.processor;

import static nc.config.NCConfig.ore_processing;
import static nc.util.FissionHelper.*;
import static nc.util.FluidStackHelper.*;

import java.util.*;

import com.google.common.collect.*;

import nc.init.NCItems;
import nc.recipe.BasicRecipeHandler;
import nc.util.*;
import net.minecraft.init.*;
import net.minecraftforge.fluids.FluidRegistry;

public class MelterRecipes extends BasicRecipeHandler {
	
	public MelterRecipes() {
		super("melter", 1, 0, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("dustSulfur", fluidStack("sulfur", GEM_VOLUME), 1D, 1D);
		addRecipe("dustSodiumHydroxide", fluidStack("naoh", GEM_VOLUME), 1D, 1D);
		addRecipe("dustPotassiumHydroxide", fluidStack("koh", GEM_VOLUME), 1D, 1D);
		addRecipe("dustArsenic", fluidStack("arsenic", GEM_VOLUME), 1D, 1D);
		addRecipe("gemBoronArsenide", fluidStack("bas", GEM_VOLUME), 1D, 1D);
		
		addRecipe(Lists.newArrayList("gemPrismarine", "dustPrismarine"), fluidStack("prismarine", INGOT_VOLUME), 1D, 1D);
		addRecipe(Items.SLIME_BALL, fluidStack("slime", INGOT_VOLUME), 1D, 0.5D);
		addRecipe(Lists.newArrayList(Blocks.SLIME_BLOCK, RegistryHelper.blockStackFromRegistry("tconstruct:slime:0")), fluidStack("slime", INGOT_BLOCK_VOLUME), 9D, 0.5D);
		addRecipe(RegistryHelper.blockStackFromRegistry("tconstruct:slime_congealed:0"), fluidStack("slime", INGOT_VOLUME * 4), 4D, 0.5D);
		
		addRecipe(Lists.newArrayList("ingotSilicon", "itemSilicon"), fluidStack("silicon", INGOT_VOLUME), 1D, 1D);
		
		addIngotMeltingRecipes("Boron10", "boron_10");
		addIngotMeltingRecipes("Boron11", "boron_11");
		addIngotMeltingRecipes("Lithium6", "lithium_6");
		addIngotMeltingRecipes("Lithium7", "lithium_7");
		
		addIngotMeltingRecipes("HardCarbon", "hard_carbon");
		addIngotMeltingRecipes("ManganeseDioxide", "manganese_dioxide");
		addIngotMeltingRecipes("alugentum");
		addIngotMeltingRecipes("LeadPlatinum", "lead_platinum");
		
		addRecipe("blockQuartz", fluidStack("quartz", GEM_VOLUME * 4), 4D, 1D);
		addRecipe("blockLapis", fluidStack("lapis", GEM_BLOCK_VOLUME), 9D, 1D);
		addRecipe("blockDiamond", fluidStack("diamond", GEM_BLOCK_VOLUME), 9D, 1D);
		addRecipe("blockEmerald", fluidStack("emerald", GEM_BLOCK_VOLUME), 9D, 1D);
		
		addRecipe("obsidian", fluidStack("obsidian", SEARED_BLOCK_VOLUME), 2D, 2D);
		addRecipe(Lists.newArrayList("ingotObsidian", "dustObsidian"), fluidStack("obsidian", SEARED_MATERIAL_VOLUME), 0.5D, 2D);
		addRecipe(Lists.newArrayList("nuggetObsidian", "tinyDustObsidian"), fluidStack("obsidian", SEARED_MATERIAL_VOLUME / 9), 0.5D / 9D, 2D);
		addRecipe("netherrack", fluidStack("nether_brick", SEARED_MATERIAL_VOLUME), 0.5D, 1.5D);
		addRecipe(Blocks.NETHER_BRICK, fluidStack("nether_brick", SEARED_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe(Lists.newArrayList("ingotBrickNether", "dustBrickNether"), fluidStack("nether_brick", SEARED_MATERIAL_VOLUME), 0.5D, 1.5D);
		addRecipe(Lists.newArrayList("endstone", Blocks.END_BRICKS), fluidStack("end_stone", SEARED_BLOCK_VOLUME), 2D, 2D);
		addRecipe(Lists.newArrayList("ingotEndstone", "dustEndstone"), fluidStack("end_stone", SEARED_MATERIAL_VOLUME), 0.5D, 2D);
		addRecipe(Blocks.PURPUR_BLOCK, fluidStack("purpur", SEARED_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe(Items.CHORUS_FRUIT_POPPED, fluidStack("purpur", SEARED_MATERIAL_VOLUME), 0.5D, 1.5D);
		
		// Tinkers' Construct
		addRecipe("sand", fluidStack("glass", GLASS_VOLUME), 1.5D, 1.5D);
		addRecipe("blockGlass", fluidStack("glass", GLASS_VOLUME), 1.5D, 1.5D);
		addRecipe(Blocks.CLAY, fluidStack("clay", BRICK_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe(Items.CLAY_BALL, fluidStack("clay", BRICK_VOLUME), 0.5D, 1.5D);
		addRecipe(Blocks.BRICK_BLOCK, fluidStack("clay", BRICK_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe("ingotBrick", fluidStack("clay", BRICK_VOLUME), 0.5D, 1.5D);
		addRecipe(Blocks.HARDENED_CLAY, fluidStack("clay", BRICK_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe("stone", fluidStack("stone", SEARED_MATERIAL_VOLUME), 1.5D, 1.5D);
		addRecipe("cobblestone", fluidStack("stone", SEARED_MATERIAL_VOLUME), 1.5D, 1.5D);
		addRecipe(Lists.newArrayList("dirt", "grass"), fluidStack("dirt", INGOT_VOLUME), 1D, 1D);
		
		// Thermal Expansion
		addRecipe(Lists.newArrayList("dustRedstone", "ingotRedstone"), fluidStack("redstone", REDSTONE_DUST_VOLUME), 0.25D, 1D);
		addRecipe("blockRedstone", fluidStack("redstone", REDSTONE_BLOCK_VOLUME), 2D, 1D);
		addRecipe("dustGlowstone", fluidStack("glowstone", GLOWSTONE_DUST_VOLUME), 0.25D, 1D);
		addRecipe("glowstone", fluidStack("glowstone", GLOWSTONE_BLOCK_VOLUME), 2D, 1D);
		addRecipe(Lists.newArrayList(Items.ENDER_PEARL, "dustEnder"), fluidStack("ender", ENDER_PEARL_VOLUME), 0.5D, 1.5D);
		addRecipe("dustPyrotheum", fluidStack("pyrotheum", EUM_DUST_VOLUME), 1D, 1D);
		addRecipe("dustCryotheum", fluidStack("cryotheum", EUM_DUST_VOLUME), 1D, 1D);
		addRecipe("dustAerotheum", fluidStack("aerotheum", EUM_DUST_VOLUME), 1D, 1D);
		addRecipe("dustPetrotheum", fluidStack("petrotheum", EUM_DUST_VOLUME), 1D, 1D);
		addRecipe(Lists.newArrayList("coal", "dustCoal"), fluidStack("coal", COAL_DUST_VOLUME), 0.5D, 1D);
		addRecipe(Lists.newArrayList("ingotGraphite", "dustGraphite"), fluidStack("coal", COAL_DUST_VOLUME), 0.5D, 1D);
		addRecipe("blockCoal", fluidStack("coal", COAL_BLOCK_VOLUME), 4.5D, 1D);
		addRecipe("blockGraphite", fluidStack("coal", COAL_BLOCK_VOLUME), 4.5D, 1D);
		
		// EnderIO
		addIngotMeltingRecipes("ElectricalSteel", "electrical_steel");
		addIngotMeltingRecipes("EnergeticAlloy", "energetic_alloy");
		addIngotMeltingRecipes("VibrantAlloy", "vibrant_alloy");
		addIngotMeltingRecipes("RedstoneAlloy", "redstone_alloy");
		addIngotMeltingRecipes("ConductiveIron", "conductive_iron");
		addIngotMeltingRecipes("PulsatingIron", "pulsating_iron");
		addIngotMeltingRecipes("DarkSteel", "dark_steel");
		addIngotMeltingRecipes("Soularium", "soularium");
		addIngotMeltingRecipes("EndSteel", "end_steel");
		addIngotMeltingRecipes("ConstructionAlloy", "construction_alloy");
		
		// Endergy
		addIngotMeltingRecipes("CrudeSteel", "crude_steel");
		addIngotMeltingRecipes("CrystallineAlloy", "crystalline_alloy");
		addIngotMeltingRecipes("MelodicAlloy", "melodic_alloy");
		addIngotMeltingRecipes("StellarAlloy", "stellar_alloy");
		addIngotMeltingRecipes("CrystallinePinkSlime", "crystalline_pink_slime");
		addIngotMeltingRecipes("EnergeticSilver", "energetic_silver");
		addIngotMeltingRecipes("VividAlloy", "vivid_alloy");
		
		// Mekanism
		addRecipe(Lists.newArrayList("itemSalt", "dustSalt"), fluidStack("brine", 15), 0.25D, 0.5D);
		
		// Advanced Rocketry
		addIngotMeltingRecipes("dilithium");
		// addIngotMeltingRecipes("titanium");
		
		// PlusTiC - Botania
		addIngotMeltingRecipes("manasteel");
		addIngotMeltingRecipes("terrasteel");
		addIngotMeltingRecipes("elementium");
		
		// PlusTiC - Mekanism
		addIngotMeltingRecipes("RefinedObsidian", "refinedobsidian");
		addIngotMeltingRecipes("RefinedGlowstone", "refinedglowstone");
		
		// PlusTiC - Psi
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
		
		addRecipe(Blocks.ICE, fluidStack("water", BUCKET_VOLUME), 0.25D, 0.5D);
		addRecipe(Blocks.PACKED_ICE, fluidStack("water", BUCKET_VOLUME), 0.5D, 0.5D);
		
		// Sweets
		addRecipe(NCItems.ground_cocoa_nibs, fluidStack("chocolate_liquor", INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotCocoaButter", fluidStack("cocoa_butter", INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotUnsweetenedChocolate", fluidStack("unsweetened_chocolate", INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotDarkChocolate", fluidStack("dark_chocolate", INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotChocolate", fluidStack("milk_chocolate", INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(Items.SUGAR, fluidStack("sugar", INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe(NCItems.gelatin, fluidStack("gelatin", INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe("ingotMarshmallow", fluidStack("marshmallow", INGOT_VOLUME), 0.5D, 0.5D);
		
		// Ores
		addOreMeltingRecipes();
		
		// Fission Materials
		addFissionMeltingRecipes();
		
		addIngotMeltingRecipes("Strontium90", "strontium_90");
		addIngotMeltingRecipes("Molybdenum", "molybdenum");
		addIngotMeltingRecipes("Ruthenium106", "ruthenium_106");
		addIngotMeltingRecipes("Caesium137", "caesium_137");
		addIngotMeltingRecipes("Promethium147", "promethium_147");
		addIngotMeltingRecipes("Europium155", "europium_155");
	}
	
	private static final Set<String> ORE_PREFIX_LIST = Sets.newHashSet("ore", "oreGravel", "oreNetherrack", "oreEndstone", "oreSand", "oreBlackgranite", "oreRedgranite", "oreMarble", "oreBasalt");
	
	public void addIngotMeltingRecipes(String oreName, String fluidName) {
		if (ore_processing) {
			for (String prefix : ORE_PREFIX_LIST) {
				addRecipe(prefix + oreName, fluidStack(fluidName, INGOT_ORE_VOLUME), 1.25D, 1.5D);
			}
		}
		addRecipe(Lists.newArrayList("ingot" + oreName, "dust" + oreName), fluidStack(fluidName, INGOT_VOLUME), 1D, 1D);
		addRecipe(Lists.newArrayList("nugget" + oreName, "tinyDust" + oreName), fluidStack(fluidName, NUGGET_VOLUME), 1D / 9D, 1D);
		addRecipe("block" + oreName, fluidStack(fluidName, INGOT_BLOCK_VOLUME), 9D, 1D);
	}
	
	public void addIngotMeltingRecipes(String fluidName) {
		addIngotMeltingRecipes(StringHelper.capitalize(fluidName), fluidName);
	}
	
	public void addGemMeltingRecipes(String oreName, String fluidName) {
		if (ore_processing) {
			for (String prefix : ORE_PREFIX_LIST) {
				addRecipe(prefix + oreName, fluidStack(fluidName, GEM_ORE_VOLUME), 1.25D, 1.5D);
			}
		}
		addRecipe(Lists.newArrayList("gem" + oreName, "dust" + oreName), fluidStack(fluidName, GEM_VOLUME), 1D, 1D);
		addRecipe(Lists.newArrayList("nugget" + oreName, "tinyDust" + oreName), fluidStack(fluidName, GEM_NUGGET_VOLUME), 1D / 9D, 1D);
	}
	
	public void addGemMeltingRecipes(String fluidName) {
		addGemMeltingRecipes(StringHelper.capitalize(fluidName), fluidName);
	}
	
	public void addFissionMeltingRecipes() {
		for (int i = 0; i < FISSION_ORE_DICT.length; ++i) {
			addRecipe("ingot" + FISSION_ORE_DICT[i], fluidStack(FISSION_FLUID[i], INGOT_VOLUME), 1D, 1D);
		}
	}
	
	private static final Set<String> MELTING_BLACKLIST = Sets.newHashSet("coal", "redstone", "glowstone", "prismarine", "obsidian", "silicon", "marshmallow");
	
	public void addOreMeltingRecipes() {
		ArrayList<String> fluidList = new ArrayList<>(FluidRegistry.getRegisteredFluids().keySet());
		for (String fluidName : fluidList) {
			if (MELTING_BLACKLIST.contains(fluidName)) {
				continue;
			}
			String oreSuffix = StringHelper.capitalize(fluidName);
			String ingot = "ingot" + oreSuffix;
			String gem = "gem" + oreSuffix;
			String dust = "dust" + oreSuffix;
			
			if (OreDictHelper.oreExists(ingot) && OreDictHelper.oreExists(dust)) {
				addIngotMeltingRecipes(fluidName);
			}
			else if (OreDictHelper.oreExists(gem) && OreDictHelper.oreExists(dust)) {
				addGemMeltingRecipes(fluidName);
			}
		}
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
