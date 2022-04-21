package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;
import static nc.util.FluidStackHelper.*;

import java.util.*;

import com.google.common.collect.Lists;

import nc.init.*;
import nc.recipe.BasicRecipeHandler;
import nc.util.*;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;

public class InfuserRecipes extends BasicRecipeHandler {
	
	public InfuserRecipes() {
		super("infuser", 1, 1, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addOxidizingRecipe("ingotThorium", BUCKET_VOLUME);
		addOxidizingRecipe("dustThorium", BUCKET_VOLUME);
		addOxidizingRecipe("ingotManganese", BUCKET_VOLUME);
		addOxidizingRecipe("dustManganese", BUCKET_VOLUME);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", BUCKET_VOLUME), "ingotManganeseDioxide", 1D, 1D);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", BUCKET_VOLUME), "dustManganeseDioxide", 1D, 1D);
		
		addRecipe(Lists.newArrayList(Blocks.ICE, Blocks.PACKED_ICE), fluidStack("liquid_helium", 50), NCBlocks.supercold_ice, 0.2D, 0.5D);
		
		addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("liquid_nitrogen", BUCKET_VOLUME), new ItemStack(NCBlocks.fission_heater_port2, 1, 12), 1D, 1D);
		addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("liquid_helium", BUCKET_VOLUME), new ItemStack(NCBlocks.fission_heater_port2, 1, 13), 1D, 1D);
		if (!OreDictHelper.oreExists("ingotEnderium")) {
			addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("enderium", INGOT_VOLUME * 4), new ItemStack(NCBlocks.fission_heater_port2, 1, 14), 1D, 1D);
		}
		if (!OreDictHelper.oreExists("dustCryotheum")) {
			addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("cryotheum", BUCKET_VOLUME), new ItemStack(NCBlocks.fission_heater_port2, 1, 15), 1D, 1D);
		}
		
		addRecipe("emptyHeatSink", fluidStack("water", BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink, 1, 0), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("liquid_nitrogen", BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 12), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("liquid_helium", BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 13), 1D, 1D);
		if (!OreDictHelper.oreExists("ingotEnderium")) {
			addRecipe("emptyHeatSink", fluidStack("enderium", INGOT_VOLUME * 4), new ItemStack(NCBlocks.solid_fission_sink2, 1, 14), 1D, 1D);
		}
		if (!OreDictHelper.oreExists("dustCryotheum")) {
			addRecipe("emptyHeatSink", fluidStack("cryotheum", BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 15), 1D, 1D);
		}
		
		addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("liquid_nitrogen", BUCKET_VOLUME), new ItemStack(NCBlocks.salt_fission_heater2, 1, 12), 1D, 1D);
		addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("liquid_helium", BUCKET_VOLUME), new ItemStack(NCBlocks.salt_fission_heater2, 1, 13), 1D, 1D);
		if (!OreDictHelper.oreExists("ingotEnderium")) {
			addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("enderium", INGOT_VOLUME * 4), new ItemStack(NCBlocks.salt_fission_heater2, 1, 14), 1D, 1D);
		}
		if (!OreDictHelper.oreExists("dustCryotheum")) {
			addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("cryotheum", BUCKET_VOLUME), new ItemStack(NCBlocks.salt_fission_heater2, 1, 15), 1D, 1D);
		}
		
		addRecipe(oreStack("bioplastic", 2), fluidStack("radaway", BUCKET_VOLUME / 4), NCItems.radaway, 1D, 0.5D);
		addRecipe(oreStack("bioplastic", 2), fluidStack("radaway_slow", BUCKET_VOLUME / 4), NCItems.radaway_slow, 1D, 0.5D);
		addRecipe(NCItems.radaway, fluidStack("redstone", REDSTONE_DUST_VOLUME * 2), NCItems.radaway_slow, 1D, 0.5D);
		
		addRecipe("emptyFrame", fluidStack("water", BUCKET_VOLUME * 2), NCBlocks.water_source, 1D, 1D);
		addRecipe(NCBlocks.water_source, fluidStack("lava", BUCKET_VOLUME), NCBlocks.cobblestone_generator, 1D, 1D);
		addRecipe("emptyFrame", fluidStackList(Lists.newArrayList("heavywater", "heavy_water"), BUCKET_VOLUME), NCBlocks.heavy_water_moderator, 1D, 1D);
		
		addRecipe(OreDictHelper.oreExists("blockGlassHardened") ? "blockGlassHardened" : "blockGlass", fluidStack("tritium", BUCKET_VOLUME), NCBlocks.tritium_lamp, 1D, 1D);
		
		addRecipe("sandstone", fluidStack("ender", EUM_DUST_VOLUME), Blocks.END_STONE, 1D, 1D);
		
		for (int meta = 0; meta < 16; ++meta) {
			addRecipe(new ItemStack(Blocks.CONCRETE_POWDER, 1, meta), fluidStack("water", BUCKET_VOLUME), new ItemStack(Blocks.CONCRETE, 1, meta), 0.5D, 0.5D);
		}
		
		// Immersive Engineering
		addRecipe("plankWood", fluidStack("creosote", 125), RegistryHelper.blockStackFromRegistry("immersiveengineering:treated_wood"), 0.25D, 0.5D);
		
		// Redstone Arsenal
		addIngotInfusionRecipes("Electrum", "redstone", REDSTONE_DUST_VOLUME * 2, "ElectrumFlux", 1D, 1D);
		
		// Thermal Foundation
		addIngotInfusionRecipes("Shibuichi", "redstone", EUM_DUST_VOLUME, "Signalum", 1D, 1D);
		addIngotInfusionRecipes("TinSilver", "glowstone", EUM_DUST_VOLUME, "Lumium", 1D, 1D);
		addIngotInfusionRecipes("LeadPlatinum", "ender", EUM_DUST_VOLUME, "Enderium", 1D, 1D);
		
		// Mekanism
		addRecipe(Lists.newArrayList("dirt", "grass"), fluidStack("water", BUCKET_VOLUME * 2), Blocks.CLAY, 1D, 1D);
		addRecipe("ingotBrick", fluidStack("water", BUCKET_VOLUME * 2), Items.CLAY_BALL, 1D, 1D);
		addRecipe(Blocks.HARDENED_CLAY, fluidStack("water", BUCKET_VOLUME * 4), Blocks.CLAY, 4D, 1D);
		
		// Fission Materials
		addFissionInfusionRecipes();
	}
	
	public void addOxidizingRecipe(String fluidName, int oxygenAmount) {
		addRecipe(fluidName, fluidStack("oxygen", oxygenAmount), fluidName + "Oxide", 1D, 1D);
	}
	
	public void addIngotInfusionRecipes(String in, String fluid, int amount, String out, double time, double power) {
		addRecipe("ingot" + in, fluidStack(fluid, amount), "ingot" + out, time, power);
		addRecipe("dust" + in, fluidStack(fluid, amount), "dust" + out, time, power);
	}
	
	public void addFissionInfusionRecipes() {
		for (String element : FISSION_ORE_DICT) {
			addRecipe("ingot" + element, fluidStack("oxygen", BUCKET_VOLUME), "ingot" + element + "Oxide", 1D, 1D);
			addRecipe("ingot" + element, fluidStack("nitrogen", BUCKET_VOLUME), "ingot" + element + "Nitride", 1D, 1D);
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
