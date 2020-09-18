package nc.integration.tconstruct;

import static nc.util.FluidStackHelper.*;

import java.util.Locale;

import nc.init.NCItems;
import nc.util.OreDictHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class TConstructExtras {
	
	public static void init() {
		registerGemSmelteryRecipes("Diamond");
		
		registerMelting("coal", "coal", COAL_DUST_VOLUME);
		registerMelting("dustCoal", "coal", COAL_DUST_VOLUME);
		registerMelting("blockCoal", "coal", COAL_BLOCK_VOLUME);
		registerMelting("ingotGraphite", "coal", COAL_DUST_VOLUME);
		registerMelting("dustGraphite", "coal", COAL_DUST_VOLUME);
		registerMelting("blockGraphite", "coal", COAL_BLOCK_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "ingotGraphite"), TinkerSmeltery.castIngot, "coal", COAL_DUST_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "coal"), TinkerSmeltery.castGem, "coal", COAL_DUST_VOLUME);
		registerBasinCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "blockCoal"), null, "coal", COAL_BLOCK_VOLUME);
		
		TinkerRegistry.registerMelting(new ItemStack(NCItems.ground_cocoa_nibs), FluidRegistry.getFluid("chocolate_liquor"), INGOT_VOLUME);
		TinkerRegistry.registerMelting(new ItemStack(Items.SUGAR), FluidRegistry.getFluid("sugar"), INGOT_VOLUME);
		TinkerRegistry.registerMelting(new ItemStack(NCItems.gelatin), FluidRegistry.getFluid("gelatin"), INGOT_VOLUME);
	}
	
	private static void registerGemSmelteryRecipes(String name) {
		registerMelting("gem" + name, name, GEM_VOLUME);
		registerMelting("dust" + name, name, GEM_VOLUME);
		registerMelting("nugget" + name, name, GEM_NUGGET_VOLUME);
		registerMelting("block" + name, name, GEM_BLOCK_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "gem" + name), TinkerSmeltery.castGem, name, GEM_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "nugget" + name), TinkerSmeltery.castNugget, name, GEM_NUGGET_VOLUME);
		registerBasinCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "block" + name), null, name, GEM_BLOCK_VOLUME);
	}
	
	private static void registerMelting(String inputName, String fluidName, int fluidAmount) {
		if (FluidRegistry.getFluid(fluidName.toLowerCase(Locale.ROOT)) == null) {
			return;
		}
		TinkerRegistry.registerMelting(inputName, FluidRegistry.getFluid(fluidName.toLowerCase(Locale.ROOT)), fluidAmount);
	}
	
	private static void registerTableCasting(ItemStack output, ItemStack cast, String fluidIn, int fluidAmount) {
		if (output == null || output.isEmpty() || FluidRegistry.getFluid(fluidIn.toLowerCase(Locale.ROOT)) == null) {
			return;
		}
		TinkerRegistry.registerTableCasting(new CastingRecipe(output, cast == null ? null : RecipeMatch.of(cast), FluidRegistry.getFluid(fluidIn.toLowerCase(Locale.ROOT)), fluidAmount));
	}
	
	private static void registerBasinCasting(ItemStack output, ItemStack cast, String fluidIn, int fluidAmount) {
		if (output == null || output.isEmpty() || FluidRegistry.getFluid(fluidIn.toLowerCase(Locale.ROOT)) == null) {
			return;
		}
		TinkerRegistry.registerBasinCasting(new CastingRecipe(output, cast == null ? null : RecipeMatch.of(cast), FluidRegistry.getFluid(fluidIn.toLowerCase(Locale.ROOT)), fluidAmount));
	}
}
