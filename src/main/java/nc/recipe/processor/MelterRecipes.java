package nc.recipe.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.NCUtil;
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
		metalMelt();
		
		addRecipe("ingotLithium6", fluidStack("lithium6", 144), NCConfig.processor_time[6]);
		addRecipe("tinyLithium6", fluidStack("lithium6", 16), NCConfig.processor_time[6]/8);
		addRecipe("ingotLithium7", fluidStack("lithium7", 144), NCConfig.processor_time[6]);
		addRecipe("tinyLithium7", fluidStack("lithium7", 16), NCConfig.processor_time[6]/8);
		addRecipe("ingotBoron10", fluidStack("boron10", 144), NCConfig.processor_time[6]);
		addRecipe("tinyBoron10", fluidStack("boron10", 16), NCConfig.processor_time[6]/8);
		addRecipe("ingotBoron11", fluidStack("boron11", 144), NCConfig.processor_time[6]);
		addRecipe("tinyBoron11", fluidStack("boron11", 16), NCConfig.processor_time[6]/8);
		addRecipe("dustSulfur", fluidStack("sulfur", 666), NCConfig.processor_time[6]);
		addRecipe(Blocks.ICE, fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		addRecipe(Blocks.FROSTED_ICE, fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		addRecipe(Blocks.PACKED_ICE, fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		
		// Tinkers' Construct
		addRecipe("obsidian", fluidStack("obsidian", 288), NCConfig.processor_time[6]*4);
		addRecipe("ingotObsidian", fluidStack("obsidian", 144), NCConfig.processor_time[6]*2);
		addRecipe(Blocks.SAND, fluidStack("glass", 1000), NCConfig.processor_time[6]);
		addRecipe("blockGlass", fluidStack("glass", 1000), NCConfig.processor_time[6]);
		
		// Thermal Expansion
		addRecipe("dustRedstone", fluidStack("redstone", 100), NCConfig.processor_time[6]/4);
		addRecipe("blockRedstone", fluidStack("redstone", 900), NCConfig.processor_time[6]*2);
		addRecipe("dustGlowstone", fluidStack("glowstone", 250), NCConfig.processor_time[6]/2);
		addRecipe("glowstone", fluidStack("glowstone", 1000), NCConfig.processor_time[6]*4);
		addRecipe(Items.ENDER_PEARL, fluidStack("ender", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustPyrotheum", fluidStack("pyrotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustCryotheum", fluidStack("cryotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustAerotheum", fluidStack("aerotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustPetrotheum", fluidStack("petrotheum", 250), NCConfig.processor_time[6]/2);
		addRecipe("dustCoal", fluidStack("coal", 100), NCConfig.processor_time[6]/2);
		addRecipe("dustGraphite", fluidStack("coal", 100), NCConfig.processor_time[6]/4);
	}
	
	public void metalMelt() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		ArrayList<Fluid> fluidValueList = new ArrayList(FluidRegistry.getRegisteredFluids().values());
		ArrayList<String>fluidList = new ArrayList<String>();
		for (Fluid fluid : fluidValueList) {
			fluidList.add(fluid.getName());
		}
		for (String fluidName : fluidList) {
			String ore = "ore" + NCUtil.capitalize(fluidName);
			String ingot = "ingot" + NCUtil.capitalize(fluidName);
			String dust = "dust" + NCUtil.capitalize(fluidName);
			if (NCConfig.ore_processing) {
				if (oreList.contains(ore)) addRecipe(ore, fluidStack(fluidName, 324), NCConfig.processor_time[6]*2);
			}
			if (oreList.contains(ingot) && oreList.contains(dust)) {
				addRecipe(ingot, fluidStack(fluidName, 144), NCConfig.processor_time[6]);
				addRecipe(dust, fluidStack(fluidName, 144), NCConfig.processor_time[6]);
			}
		}
	}

	public String getRecipeName() {
		return "melter";
	}
}
