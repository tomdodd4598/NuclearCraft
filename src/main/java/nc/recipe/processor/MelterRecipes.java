package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_FLUID;
import static nc.util.FissionHelper.FISSION_ORE_DICT;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
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
		addRecipe("gemBoronArsenide", fluidStack("bas", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		
		addRecipe(Lists.newArrayList("gemPrismarine", "dustPrismarine"), fluidStack("prismarine", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		addRecipe(Items.SLIME_BALL, fluidStack("slime", FluidStackHelper.INGOT_VOLUME), 1D, 0.5D);
		addRecipe(Lists.newArrayList(Blocks.SLIME_BLOCK, RegistryHelper.blockStackFromRegistry("tconstruct:slime:0")), fluidStack("slime", FluidStackHelper.INGOT_BLOCK_VOLUME), 9D, 0.5D);
		addRecipe(RegistryHelper.blockStackFromRegistry("tconstruct:slime_congealed:0"), fluidStack("slime", FluidStackHelper.INGOT_VOLUME*4), 4D, 0.5D);
		
		addRecipe(Lists.newArrayList("ingotSilicon", "itemSilicon"), fluidStack("silicon", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		
		addIngotMeltingRecipes("boron10");
		addIngotMeltingRecipes("boron11");
		addIngotMeltingRecipes("lithium6");
		addIngotMeltingRecipes("lithium7");
		
		addIngotMeltingRecipes("hardCarbon", "hard_carbon");
		addIngotMeltingRecipes("manganeseDioxide", "manganese_dioxide");
		addIngotMeltingRecipes("alugentum");
		addIngotMeltingRecipes("leadPlatinum", "lead_platinum");
		
		addRecipe("blockQuartz", fluidStack("quartz", FluidStackHelper.GEM_VOLUME*4), 4D, 1D);
		addRecipe("blockLapis", fluidStack("lapis", FluidStackHelper.GEM_BLOCK_VOLUME), 9D, 1D);
		addRecipe("blockDiamond", fluidStack("diamond", FluidStackHelper.GEM_BLOCK_VOLUME), 9D, 1D);
		addRecipe("blockEmerald", fluidStack("emerald", FluidStackHelper.GEM_BLOCK_VOLUME), 9D, 1D);
		
		addRecipe("obsidian", fluidStack("obsidian", FluidStackHelper.SEARED_BLOCK_VOLUME), 2D, 2D);
		addRecipe(Lists.newArrayList("ingotObsidian", "dustObsidian"), fluidStack("obsidian", FluidStackHelper.SEARED_MATERIAL_VOLUME), 0.5D, 2D);
		addRecipe(Lists.newArrayList("nuggetObsidian", "tinyDustObsidian"), fluidStack("obsidian", FluidStackHelper.SEARED_MATERIAL_VOLUME/9), 0.5D/9D, 2D);
		addRecipe("netherrack", fluidStack("nether_brick", FluidStackHelper.SEARED_MATERIAL_VOLUME), 0.5D, 1.5D);
		addRecipe(Blocks.NETHER_BRICK, fluidStack("nether_brick", FluidStackHelper.SEARED_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe(Lists.newArrayList("ingotBrickNether", "dustBrickNether"), fluidStack("nether_brick", FluidStackHelper.SEARED_MATERIAL_VOLUME), 0.5D, 1.5D);
		addRecipe(Lists.newArrayList("endstone", Blocks.END_BRICKS), fluidStack("end_stone", FluidStackHelper.SEARED_BLOCK_VOLUME), 2D, 2D);
		addRecipe(Lists.newArrayList("ingotEndstone", "dustEndstone"), fluidStack("end_stone", FluidStackHelper.SEARED_MATERIAL_VOLUME), 0.5D, 2D);
		addRecipe(Blocks.PURPUR_BLOCK, fluidStack("purpur", FluidStackHelper.SEARED_BLOCK_VOLUME), 2D, 1.5D);
		addRecipe(Items.CHORUS_FRUIT_POPPED, fluidStack("purpur", FluidStackHelper.SEARED_MATERIAL_VOLUME), 0.5D, 1.5D);
		
		// Tinkers' Construct
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
		
		// EnderIO
		addIngotMeltingRecipes("electricalSteel", "electrical_steel");
		addIngotMeltingRecipes("energeticAlloy", "energetic_alloy");
		addIngotMeltingRecipes("vibrantAlloy", "vibrant_alloy");
		addIngotMeltingRecipes("redstoneAlloy", "redstone_alloy");
		addIngotMeltingRecipes("conductiveIron", "conductive_iron");
		addIngotMeltingRecipes("pulsatingIron", "pulsating_iron");
		addIngotMeltingRecipes("darkSteel", "dark_steel");
		addIngotMeltingRecipes("soularium", "soularium");
		addIngotMeltingRecipes("endSteel", "end_steel");
		addIngotMeltingRecipes("constructionAlloy", "construction_alloy");
		
		// Endergy
		addIngotMeltingRecipes("crudeSteel", "crude_steel");
		addIngotMeltingRecipes("crystallineAlloy", "crystalline_alloy");
		addIngotMeltingRecipes("melodicAlloy", "melodic_alloy");
		addIngotMeltingRecipes("stellarAlloy", "stellar_alloy");
		addIngotMeltingRecipes("crystallinePinkSlime", "crystalline_pink_slime");
		addIngotMeltingRecipes("energeticSilver", "energetic_silver");
		addIngotMeltingRecipes("vividAlloy", "vivid_alloy");
		
		
		// Mekanism
		addRecipe(Lists.newArrayList("itemSalt", "dustSalt"), fluidStack("brine", 15), 0.25D, 0.5D);
		
		// Advanced Rocketry
		addIngotMeltingRecipes("dilithium");
		//addIngotMeltingRecipes("titanium");
		
		// PlusTiC - Botania
		addIngotMeltingRecipes("manasteel");
		addIngotMeltingRecipes("terrasteel");
		addIngotMeltingRecipes("elementium");
		
		// PlusTiC - Mekanism
		addIngotMeltingRecipes("refinedObsidian", "refinedobsidian");
		addIngotMeltingRecipes("refinedGlowstone", "refinedglowstone");
		
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
		
		addRecipe(Blocks.ICE, fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 0.25D, 0.5D);
		addRecipe(Blocks.PACKED_ICE, fluidStack("water", FluidStackHelper.BUCKET_VOLUME), 0.5D, 0.5D);
		
		// Sweets
		addRecipe(NCItems.ground_cocoa_nibs, fluidStack("chocolate_liquor", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotCocoaButter", fluidStack("cocoa_butter", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotUnsweetenedChocolate", fluidStack("unsweetened_chocolate", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotDarkChocolate", fluidStack("dark_chocolate", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe("ingotChocolate", fluidStack("milk_chocolate", FluidStackHelper.INGOT_VOLUME), 0.25D, 0.5D);
		addRecipe(Items.SUGAR, fluidStack("sugar", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe(NCItems.gelatin, fluidStack("gelatin", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
		addRecipe("ingotMarshmallow", fluidStack("marshmallow", FluidStackHelper.INGOT_VOLUME), 0.5D, 0.5D);
		
		// Ores
		addOreMeltingRecipes();
		
		// Fission Materials
		addFissionMeltingRecipes();
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
	
	public void addFissionMeltingRecipes() {
		for (int i = 0; i < FISSION_ORE_DICT.length; i++) {
			addRecipe("ingot" + FISSION_ORE_DICT[i], fluidStack(FISSION_FLUID[i], FluidStackHelper.INGOT_VOLUME), 1D, 1D);
			addRecipe("ingot" + FISSION_ORE_DICT[i] + "ZA", fluidStack(FISSION_FLUID[i] + "_za", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
			addRecipe("ingotDepleted" + FISSION_ORE_DICT[i], fluidStack("depleted_" + FISSION_FLUID[i], FluidStackHelper.INGOT_VOLUME), 1D, 1D);
			addRecipe("ingotDepleted" + FISSION_ORE_DICT[i] + "ZA", fluidStack("depleted_" + FISSION_FLUID[i] + "_za", FluidStackHelper.INGOT_VOLUME), 1D, 1D);
		}
	}
	
	private static final List<String> MELTING_BLACKLIST = Lists.newArrayList("coal", "redstone", "glowstone", "prismarine", "obsidian", "silicon", "marshmallow");
	
	public void addOreMeltingRecipes() {
		ArrayList<String> fluidList = new ArrayList(FluidRegistry.getRegisteredFluids().keySet());
		for (String fluidName : fluidList) {
			if (MELTING_BLACKLIST.contains(fluidName)) continue;
			String materialName = StringHelper.capitalize(fluidName);
			String ingot = "ingot" + materialName;
			String gem = "gem" + materialName;
			String dust = "dust" + materialName;
			
			if (OreDictHelper.oreExists(ingot) && OreDictHelper.oreExists(dust)) addIngotMeltingRecipes(fluidName);
			else if (OreDictHelper.oreExists(gem) && OreDictHelper.oreExists(dust)) addGemMeltingRecipes(fluidName);
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
