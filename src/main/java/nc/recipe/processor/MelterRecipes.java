package nc.recipe.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MelterRecipes extends BaseRecipeHandler {
	
	private static final MelterRecipes RECIPES = new MelterRecipes();
	
	public MelterRecipes() {
		super(1, 0, 0, 1, false);
	}

	public static final MelterRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		oreProcess();
		
		addRecipe("ingotLithium6", fluidStack("lithium6", 144), NCConfig.processor_time[6]);
		addRecipe("tinyLithium6", fluidStack("lithium6", 16), NCConfig.processor_time[6]/8);
		addRecipe("ingotLithium7", fluidStack("lithium7", 144), NCConfig.processor_time[6]);
		addRecipe("tinyLithium7", fluidStack("lithium7", 16), NCConfig.processor_time[6]/8);
		addRecipe("ingotBoron10", fluidStack("boron10", 144), NCConfig.processor_time[6]);
		addRecipe("tinyBoron10", fluidStack("boron10", 16), NCConfig.processor_time[6]/8);
		addRecipe("ingotBoron11", fluidStack("boron11", 144), NCConfig.processor_time[6]);
		addRecipe("tinyBoron11", fluidStack("boron11", 16), NCConfig.processor_time[6]/8);
		addRecipe(Blocks.ICE, fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		addRecipe(Blocks.FROSTED_ICE, fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		addRecipe(Blocks.PACKED_ICE, fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		
		// Tinkers' Construct
		addRecipe("blockObsidian", fluidStack("obsidian", 288), NCConfig.processor_time[6]*4);
		addRecipe("ingotObsidian", fluidStack("obsidian", 144), NCConfig.processor_time[6]*2);
		addRecipe("blockGlass", fluidStack("glass", 1000), NCConfig.processor_time[6]);
		addRecipe("blockSand", fluidStack("glass", 1000), NCConfig.processor_time[6]);
		
		metalMelt("Manyullyn");
		metalMelt("Alubrass");
		metalMelt("Pigiron");
		metalMelt("Brass");
		metalMelt("Bronze");
		metalMelt("Electrum");
		metalMelt("Steel");
		
		// Thermal Expansion
		addRecipe("dustRedstone", fluidStack("redstone", 100), NCConfig.processor_time[6]/4);
		addRecipe("blockRedstone", fluidStack("redstone", 900), NCConfig.processor_time[6]*2);
		addRecipe("dustGlowstone", fluidStack("glowstone", 250), NCConfig.processor_time[6]/2);
		addRecipe("blockGlowstone", fluidStack("glowstone", 1000), NCConfig.processor_time[6]*4);
		addRecipe(Items.ENDER_PEARL, fluidStack("ender", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustPyrotheum", fluidStack("pyrotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustCryotheum", fluidStack("cryotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustAerotheum", fluidStack("aerotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustPetrotheum", fluidStack("petrotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustCoal", fluidStack("coal", 100), NCConfig.processor_time[6]/2);
		addRecipe("dustGraphite", fluidStack("coal", 100), NCConfig.processor_time[6]/4);
	}
	
	public void metalMelt(String metal) {
		addRecipe("ingot" + metal, fluidStack(metal.toLowerCase(), 144), NCConfig.processor_time[6]);
		addRecipe("dust" + metal, fluidStack(metal.toLowerCase(), 144), NCConfig.processor_time[6]);
		addRecipe("block" + metal, fluidStack(metal.toLowerCase(), 1296), NCConfig.processor_time[6]);
	}
	
	public void oreProcess() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		ArrayList<Fluid> fluidValueList = new ArrayList(FluidRegistry.getRegisteredFluids().values());
		ArrayList<String>fluidList = new ArrayList<String>();
		for (Fluid fluid : fluidValueList) {
			fluidList.add(fluid.getName());
		}
		for (String ore : oreList) {
			if (ore.startsWith("ore")) {
				String fluid = (ore.substring(3)).toLowerCase();
				if (fluidList.contains(fluid)) addRecipe(ore, fluidStack(fluid, 324), NCConfig.processor_time[6]*2);
			}
			if (ore.startsWith("ingot")) {
				String dust = "dust" + ore.substring(5);
				String block = "block" + ore.substring(5);
				String fluid = (ore.substring(5)).toLowerCase();
				if (oreList.contains(dust) && fluidList.contains(fluid)) {
					addRecipe(ore, fluidStack(fluid, 144), NCConfig.processor_time[6]);
					addRecipe(dust, fluidStack(fluid, 144), NCConfig.processor_time[6]);
					if (oreList.contains(block)) addRecipe(block, fluidStack(fluid, 1296), NCConfig.processor_time[6]*9);
				}
			}
		}
	}

	public String getRecipeName() {
		return "melter";
	}
}
