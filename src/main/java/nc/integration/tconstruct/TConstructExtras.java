package nc.integration.tconstruct;

import static nc.util.FluidStackHelper.*;

import java.util.*;

import nc.init.NCItems;
import nc.util.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class TConstructExtras {
	
	public static void init() {
		registerGemSmelteryRecipes("diamond");
		
		registerMelting("coal", "coal", COAL_DUST_VOLUME);
		registerMelting("dustCoal", "coal", COAL_DUST_VOLUME);
		registerMelting("blockCoal", "coal", COAL_BLOCK_VOLUME);
		registerMelting("ingotGraphite", "coal", COAL_DUST_VOLUME);
		registerMelting("dustGraphite", "coal", COAL_DUST_VOLUME);
		registerMelting("blockGraphite", "coal", COAL_BLOCK_VOLUME);
		
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "coal"), TinkerSmeltery.castGem, "coal", COAL_DUST_VOLUME);
		registerBasinCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "blockCoal"), null, "coal", COAL_BLOCK_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "ingotGraphite"), TinkerSmeltery.castIngot, "coal", COAL_DUST_VOLUME);
		
		TinkerRegistry.registerMelting(new ItemStack(NCItems.ground_cocoa_nibs), FluidRegistry.getFluid("chocolate_liquor"), INGOT_VOLUME);
		TinkerRegistry.registerMelting(new ItemStack(Items.SUGAR), FluidRegistry.getFluid("sugar"), INGOT_VOLUME);
		TinkerRegistry.registerMelting(new ItemStack(NCItems.gelatin), FluidRegistry.getFluid("gelatin"), INGOT_VOLUME);
		
		registerAlloyRecipe("steel", 9, "iron", 9, "coal", 25);
		registerAlloyRecipe("ferroboron", 2, "steel", 1, "boron", 1);
		registerAlloyRecipe("tough", 2, "ferroboron", 1, "lithium", 1);
		registerAlloyRecipe("hard_carbon", 144, "coal", 100, "diamond", 333);
		registerAlloyRecipe("magnesium_diboride", 3, "magnesium", 1, "boron", 2);
		registerAlloyRecipe("lithium_manganese_dioxide", 2, "lithium", 1, "manganese_dioxide", 1);
		registerAlloyRecipe("extreme", 1, "tough", 1, "hard_carbon", 1);
		registerAlloyRecipe("thermoconducting", 16, "extreme", 8, "boron_arsenide", 37);
		registerAlloyRecipe("lead_platinum", 4, "lead", 3, "platinum", 1);
		registerAlloyRecipe("enderium", 72, "lead_platinum", 72, "ender", 125);
		
		registerAlloyRecipe("unsweetened_chocolate", 2, "chocolate_liquor", 1, "cocoa_butter", 1);
		registerAlloyRecipe("dark_chocolate", 2, "unsweetened_chocolate", 2, "sugar", 1);
		registerAlloyRecipe("milk_chocolate", 144, "dark_chocolate", 72, "milk", 125);
		registerAlloyRecipe("hydrated_gelatin", 36, "gelatin", 36, "water", 125);
		registerAlloyRecipe("marshmallow", 2, "hydrated_gelatin", 2, "sugar", 1);
	}
	
	public static void registerGemSmelteryRecipes(String name) {
		registerGemSmelteryRecipes(StringHelper.capitalize(name), name);
	}
	
	public static void registerGemSmelteryRecipes(String oreSuffix, String fluidName) {
		registerGemMelting(oreSuffix, fluidName);
		registerGemCasting(oreSuffix, fluidName);
	}
	
	public static void registerGemMelting(String oreSuffix, String fluidName) {
		registerMelting("gem" + oreSuffix, fluidName, GEM_VOLUME);
		registerMelting("dust" + oreSuffix, fluidName, GEM_VOLUME);
		registerMelting("nugget" + oreSuffix, fluidName, GEM_NUGGET_VOLUME);
		registerMelting("block" + oreSuffix, fluidName, GEM_BLOCK_VOLUME);
	}
	
	public static void registerMelting(String inputName, String fluidName, int fluidAmount) {
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if (fluid != null) {
			TinkerRegistry.registerMelting(inputName, fluid, fluidAmount);
		}
	}
	
	public static void registerGemCasting(String oreSuffix, String fluidName) {
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "gem" + oreSuffix), TinkerSmeltery.castGem, fluidName, GEM_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "nugget" + oreSuffix), TinkerSmeltery.castNugget, fluidName, GEM_NUGGET_VOLUME);
		registerBasinCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "block" + oreSuffix), null, fluidName, GEM_BLOCK_VOLUME);
	}
	
	public static void registerTableCasting(ItemStack output, ItemStack cast, String fluidName, int fluidAmount) {
		if (output != null && !output.isEmpty()) {
			Fluid fluid = FluidRegistry.getFluid(fluidName);
			if (fluid != null) {
				TinkerRegistry.registerTableCasting(new CastingRecipe(output, cast == null ? null : RecipeMatch.of(cast), fluid, fluidAmount));
			}
		}
	}
	
	public static void registerBasinCasting(ItemStack output, ItemStack cast, String fluidName, int fluidAmount) {
		if (output != null && !output.isEmpty()) {
			Fluid fluid = FluidRegistry.getFluid(fluidName);
			if (fluid != null) {
				TinkerRegistry.registerBasinCasting(new CastingRecipe(output, cast == null ? null : RecipeMatch.of(cast), fluid, fluidAmount));
			}
		}
	}
	
	public static void registerAlloyRecipe(Object... objects) {
		if (objects.length >= 6 && (objects.length & 1) == 0) {
			Fluid fluid = FluidRegistry.getFluid((String) objects[0]);
			if (fluid != null) {
				FluidStack result = new FluidStack(fluid, (int) objects[1]);
				List<FluidStack> inputs = new ArrayList<>();
				
				for (int i = 1; i < objects.length / 2; ++i) {
					fluid = FluidRegistry.getFluid((String) objects[2 * i]);
					if (fluid == null) {
						return;
					}
					inputs.add(new FluidStack(fluid, (int) objects[2 * i + 1]));
				}
				
				TinkerRegistry.registerAlloy(result, inputs.toArray(new FluidStack[inputs.size()]));
			}
		}
	}
}
