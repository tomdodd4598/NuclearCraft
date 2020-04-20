package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InfuserRecipes extends ProcessorRecipeHandler {
	
	public InfuserRecipes() {
		super("infuser", 1, 1, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addOxidizingRecipe("ingotThorium", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("dustThorium", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("ingotManganese", FluidStackHelper.BUCKET_VOLUME);
		addOxidizingRecipe("dustManganese", FluidStackHelper.BUCKET_VOLUME);
		addRecipe("ingotManganeseOxide", fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), "ingotManganeseDioxide", 1D, 1D);
		addRecipe("dustManganeseOxide", fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), "dustManganeseDioxide", 1D, 1D);
		
		addRecipe(Lists.newArrayList(Blocks.ICE, Blocks.PACKED_ICE), fluidStack("liquid_helium", 50), NCBlocks.supercold_ice, 0.2D, 0.5D);
		
		addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("liquid_nitrogen", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.fission_heater_port2, 1, 12), 1D, 1D);
		addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("liquid_helium", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.fission_heater_port2, 1, 13), 1D, 1D);
		if (!OreDictHelper.oreExists("ingotEnderium")) {
			addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("enderium", FluidStackHelper.INGOT_VOLUME*4), new ItemStack(NCBlocks.fission_heater_port2, 1, 14), 1D, 1D);
		}
		if (!OreDictHelper.oreExists("dustCryotheum")) {
			addRecipe(new ItemStack(NCBlocks.fission_heater_port, 1, 0), fluidStack("cryotheum", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.fission_heater_port2, 1, 15), 1D, 1D);
		}
		
		addRecipe("emptyHeatSink", fluidStack("water", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink, 1, 0), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("liquid_nitrogen", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 12), 1D, 1D);
		addRecipe("emptyHeatSink", fluidStack("liquid_helium", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 13), 1D, 1D);
		if (!OreDictHelper.oreExists("ingotEnderium")) {
			addRecipe("emptyHeatSink", fluidStack("enderium", FluidStackHelper.INGOT_VOLUME*4), new ItemStack(NCBlocks.solid_fission_sink2, 1, 14), 1D, 1D);
		}
		if (!OreDictHelper.oreExists("dustCryotheum")) {
			addRecipe("emptyHeatSink", fluidStack("cryotheum", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.solid_fission_sink2, 1, 15), 1D, 1D);
		}
		
		addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("liquid_nitrogen", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.salt_fission_heater2, 1, 12), 1D, 1D);
		addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("liquid_helium", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.salt_fission_heater2, 1, 13), 1D, 1D);
		if (!OreDictHelper.oreExists("ingotEnderium")) {
			addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("enderium", FluidStackHelper.INGOT_VOLUME*4), new ItemStack(NCBlocks.salt_fission_heater2, 1, 14), 1D, 1D);
		}
		if (!OreDictHelper.oreExists("dustCryotheum")) {
			addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("cryotheum", FluidStackHelper.BUCKET_VOLUME), new ItemStack(NCBlocks.salt_fission_heater2, 1, 15), 1D, 1D);
		}
		
		addRecipe(oreStack("bioplastic", 2), fluidStack("radaway", FluidStackHelper.BUCKET_VOLUME/4), NCItems.radaway, 1D, 0.5D);
		addRecipe(oreStack("bioplastic", 2), fluidStack("radaway_slow", FluidStackHelper.BUCKET_VOLUME/4), NCItems.radaway_slow, 1D, 0.5D);
		addRecipe(NCItems.radaway, fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*2), NCItems.radaway_slow, 1D, 0.5D);
		
		addRecipe("emptyFrame", fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), NCBlocks.water_source, 1D, 1D);
		addRecipe(NCBlocks.water_source, fluidStack("lava", FluidStackHelper.BUCKET_VOLUME), NCBlocks.cobblestone_generator, 1D, 1D);
		addRecipe("emptyFrame", fluidStackList(Lists.newArrayList("heavywater", "heavy_water"), FluidStackHelper.BUCKET_VOLUME), NCBlocks.heavy_water_moderator, 1D, 1D);
		
		addRecipe(OreDictHelper.oreExists("blockGlassHardened") ? "blockGlassHardened" : "blockGlass", fluidStack("tritium", FluidStackHelper.BUCKET_VOLUME), NCBlocks.tritium_lamp, 1D, 1D);
		
		addRecipe("sandstone", fluidStack("ender", FluidStackHelper.EUM_DUST_VOLUME), Blocks.END_STONE, 1D, 1D);
		
		for (int meta = 0; meta < 16; meta++) {
			addRecipe(new ItemStack(Blocks.CONCRETE_POWDER, 1, meta), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), new ItemStack(Blocks.CONCRETE, 1, meta), 0.5D, 0.5D);
		}
		
		// Immersive Engineering
		addRecipe("plankWood", fluidStack("creosote", 125), RegistryHelper.blockStackFromRegistry("immersiveengineering:treated_wood"), 0.25D, 0.5D);
		
		// Redstone Arsenal
		addIngotInfusionRecipes("Electrum", "redstone", FluidStackHelper.REDSTONE_DUST_VOLUME*2, "ElectrumFlux", 1D, 1D);
		
		// Thermal Foundation
		addIngotInfusionRecipes("Shibuichi", "redstone", FluidStackHelper.EUM_DUST_VOLUME, "Signalum", 1D, 1D);
		addIngotInfusionRecipes("TinSilver", "glowstone", FluidStackHelper.EUM_DUST_VOLUME, "Lumium", 1D, 1D);
		addIngotInfusionRecipes("LeadPlatinum", "ender", FluidStackHelper.EUM_DUST_VOLUME, "Enderium", 1D, 1D);
		
		// Mekanism
		addRecipe(Lists.newArrayList("dirt", "grass"), fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), Blocks.CLAY, 1D, 1D);
		addRecipe("ingotBrick", fluidStack("water", FluidStackHelper.BUCKET_VOLUME*2), Items.CLAY_BALL, 1D, 1D);
		addRecipe(Blocks.HARDENED_CLAY, fluidStack("water", FluidStackHelper.BUCKET_VOLUME*4), Blocks.CLAY, 4D, 1D);
		
		// Fission Materials
		addFissionInfusionRecipes();
	}
	
	public void addOxidizingRecipe(String name, int oxygenAmount) {
		addRecipe(name, fluidStack("oxygen", oxygenAmount), name + "Oxide", 1D, 1D);
	}
	
	public void addIngotInfusionRecipes(String in, String fluid, int amount, String out, double time, double power) {
		addRecipe("ingot" + in, fluidStack(fluid, amount), "ingot" + out, time, power);
		addRecipe("dust" + in, fluidStack(fluid, amount), "dust" + out, time, power);
	}
	
	public void addFissionInfusionRecipes() {
		for (int i = 0; i < FISSION_ORE_DICT.length; i++) {
			addRecipe("ingot" + FISSION_ORE_DICT[i], fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), "ingot" + FISSION_ORE_DICT[i] + "Oxide", 1D, 1D);
			addRecipe("ingot" + FISSION_ORE_DICT[i], fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME), "ingot" + FISSION_ORE_DICT[i] + "Nitride", 1D, 1D);
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
