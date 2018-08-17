package nc.integration.tconstruct;

import nc.util.FluidStackHelper;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class TConstructExtras {
	
	public static void init() {
		registerGemSmelteryRecipes("Diamond");
		
		registerMelting("gemCoal", "coal", FluidStackHelper.COAL_DUST_VOLUME);
		registerMelting("dustCoal", "coal", FluidStackHelper.COAL_DUST_VOLUME);
		registerMelting("blockCoal", "coal", FluidStackHelper.COAL_BLOCK_VOLUME);
		registerMelting("ingotGraphite", "coal", FluidStackHelper.COAL_DUST_VOLUME);
		registerMelting("dustGraphite", "coal", FluidStackHelper.COAL_DUST_VOLUME);
		registerMelting("blockGraphite", "coal", FluidStackHelper.COAL_BLOCK_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "ingotGraphite"), TinkerSmeltery.castIngot, "coal", FluidStackHelper.COAL_DUST_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "gemCoal"), TinkerSmeltery.castGem, "coal", FluidStackHelper.COAL_DUST_VOLUME);
		registerBasinCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "blockCoal"), null, "coal", FluidStackHelper.COAL_BLOCK_VOLUME);
	}
	
	private static void registerGemSmelteryRecipes(String name) {
		registerMelting("gem" + name, name, FluidStackHelper.GEM_VOLUME);
		registerMelting("dust" + name, name, FluidStackHelper.GEM_VOLUME);
		registerMelting("nugget" + name, name, FluidStackHelper.GEM_NUGGET_VOLUME);
		registerMelting("block" + name, name, FluidStackHelper.GEM_BLOCK_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "gem" + name), TinkerSmeltery.castGem, name, FluidStackHelper.GEM_VOLUME);
		registerTableCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "nugget" + name), TinkerSmeltery.castNugget, name, FluidStackHelper.GEM_NUGGET_VOLUME);
		registerBasinCasting(OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, "block" + name), null, name, FluidStackHelper.GEM_BLOCK_VOLUME);
	}
	
	private static void registerMelting(String inputName, String fluidName, int fluidAmount) {
		if (FluidRegistry.getFluid(fluidName.toLowerCase()) == null) return;
		TinkerRegistry.registerMelting(inputName, FluidRegistry.getFluid(fluidName.toLowerCase()), fluidAmount);
	}
	
	private static void registerTableCasting(ItemStack output, ItemStack cast, String fluidIn, int fluidAmount) {
		if (output == null || output.isEmpty() || FluidRegistry.getFluid(fluidIn.toLowerCase()) == null) return;
		TinkerRegistry.registerTableCasting(new CastingRecipe(output, cast == null ? null : RecipeMatch.of(cast), FluidRegistry.getFluid(fluidIn.toLowerCase()), fluidAmount));
	}
	
	private static void registerBasinCasting(ItemStack output, ItemStack cast, String fluidIn, int fluidAmount) {
		if (output == null || output.isEmpty() || FluidRegistry.getFluid(fluidIn.toLowerCase()) == null) return;
		TinkerRegistry.registerBasinCasting(new CastingRecipe(output, cast == null ? null : RecipeMatch.of(cast), FluidRegistry.getFluid(fluidIn.toLowerCase()), fluidAmount));
	}
}
