package nc.crafting.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MelterRecipes extends ProcessorRecipeHandler {
	
	private static final MelterRecipes RECIPES = new MelterRecipes();

	public MelterRecipes() {
		super(1, 0, 0, 1, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		oreProcess();
		
		addRecipe("ingotLithium6", fluidStack("lithium6", 108), NCConfig.processor_time[6]);
		addRecipe("tinyLithium6", fluidStack("lithium6", 12), NCConfig.processor_time[6]/8);
		addRecipe("ingotLithium7", fluidStack("lithium7", 108), NCConfig.processor_time[6]);
		addRecipe("tinyLithium7", fluidStack("lithium7", 12), NCConfig.processor_time[6]/8);
		addRecipe("ingotBoron10", fluidStack("boron10", 108), NCConfig.processor_time[6]);
		addRecipe("tinyBoron10", fluidStack("boron10", 12), NCConfig.processor_time[6]/8);
		addRecipe("ingotBoron11", fluidStack("boron11", 108), NCConfig.processor_time[6]);
		addRecipe("tinyBoron11", fluidStack("boron11", 12), NCConfig.processor_time[6]/8);
		addRecipe("blockIce", fluidStack("water", 1000), NCConfig.processor_time[6]/2);
		
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
	}
	
	public void oreProcess() {
		List<String> oreList = Arrays.asList(OreDictionary.getOreNames());
		List<String> fluidList = new ArrayList<String>(FluidRegistry.getRegisteredFluids().keySet());
		for (String ore : oreList) {
			if (ore.startsWith("ore")) {
				String dust = "dust" + ore.substring(3);
				String ingot = "ingot" + ore.substring(3);
				String fluid = ore.substring(3).toLowerCase();
				if (oreList.contains(dust) && oreList.contains(ingot) && fluidList.contains(fluid)) {
					addRecipe(ore, fluidStack(fluid, 324), NCConfig.processor_time[6]*2);
					addRecipe(dust, fluidStack(fluid, 144), NCConfig.processor_time[6]);
					addRecipe(ingot, fluidStack(fluid, 144), NCConfig.processor_time[6]);
				}
			}
		}
	}
}
