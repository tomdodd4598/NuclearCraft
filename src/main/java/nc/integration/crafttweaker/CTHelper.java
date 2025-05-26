package nc.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import it.unimi.dsi.fastutil.objects.*;
import nc.integration.crafttweaker.ingredient.*;
import nc.recipe.RecipeHelper;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.function.Function;

@ZenClass("mods.nuclearcraft.CTHelper")
@ZenRegister
public class CTHelper {
	
	public static ItemStack getItemStack(IItemStack item) {
		if (item == null) {
			return ItemStack.EMPTY;
		}
		
		Object internal = item.getInternal();
		if (!(internal instanceof ItemStack)) {
			CraftTweakerAPI.logError("Not a valid item stack: " + item);
		}
		return ((ItemStack) internal).copy();
	}
	
	public static FluidStack getFluidStack(ILiquidStack stack) {
		if (stack == null) {
			return null;
		}
		return (FluidStack) stack.getInternal();
	}
	
	public static IItemIngredient buildAdditionItemIngredient(IIngredient ctIngredient) {
		if (ctIngredient == null) {
			return new EmptyItemIngredient();
		}
		else if (ctIngredient instanceof CTChanceItemIngredient ctChanceIngredient) {
			return new ChanceItemIngredient(buildAdditionItemIngredient(ctChanceIngredient.getInternalIngredient()), ctChanceIngredient.getChancePercent(), ctChanceIngredient.getMinStackSize());
		}
		else if (ctIngredient instanceof IItemStack ctStack) {
			return RecipeHelper.buildItemIngredient(getItemStack(ctStack));
		}
		else if (ctIngredient instanceof IOreDictEntry ctOreStack) {
			return new OreIngredient(ctOreStack.getName(), ctOreStack.getAmount());
		}
		else if (ctIngredient instanceof IngredientStack ctStack) {
			return buildOreIngredientArray(ctStack, true);
		}
		else if (ctIngredient instanceof IngredientOr ctIngredientOr) {
			return buildIngredientArray(ctIngredientOr, RecipeHelper::buildItemIngredient, CTHelper::buildAdditionItemIngredient);
		}
		else {
			logInvalidIngredient(ctIngredient);
			return null;
		}
	}
	
	public static IFluidIngredient buildAdditionFluidIngredient(IIngredient ctIngredient) {
		if (ctIngredient == null) {
			return new EmptyFluidIngredient();
		}
		else if (ctIngredient instanceof CTChanceFluidIngredient ctChanceIngredient) {
			return new ChanceFluidIngredient(buildAdditionFluidIngredient(ctChanceIngredient.getInternalIngredient()), ctChanceIngredient.getChancePercent(), ctChanceIngredient.getStackDiff(), ctChanceIngredient.getMinStackSize());
		}
		else if (ctIngredient instanceof ILiquidStack ctStack) {
			return RecipeHelper.buildFluidIngredient(getFluidStack(ctStack));
		}
		else if (ctIngredient instanceof IngredientOr ctIngredientOr) {
			return buildIngredientArray(ctIngredientOr, RecipeHelper::buildFluidIngredient, CTHelper::buildAdditionFluidIngredient);
		}
		else {
			logInvalidIngredient(ctIngredient);
			return null;
		}
	}
	
	public static IItemIngredient buildRemovalItemIngredient(IIngredient ctIngredient) {
		if (ctIngredient == null) {
			return new EmptyItemIngredient();
		}
		else if (ctIngredient instanceof IItemStack ctStack) {
			return RecipeHelper.buildItemIngredient(CTHelper.getItemStack(ctStack));
		}
		else if (ctIngredient instanceof IOreDictEntry ctOreStack) {
			return new OreIngredient(ctOreStack.getName(), ctOreStack.getAmount());
		}
		else if (ctIngredient instanceof IngredientStack ctStack) {
			return buildOreIngredientArray(ctStack, false);
		}
		else if (ctIngredient instanceof IngredientOr ctIngredientOr) {
			return buildIngredientArray(ctIngredientOr, RecipeHelper::buildItemIngredient, CTHelper::buildRemovalItemIngredient);
		}
		else {
			logInvalidIngredient(ctIngredient);
			return null;
		}
	}
	
	public static IFluidIngredient buildRemovalFluidIngredient(IIngredient ctIngredient) {
		if (ctIngredient == null) {
			return new EmptyFluidIngredient();
		}
		else if (ctIngredient instanceof ILiquidStack ctStack) {
			return new FluidIngredient(ctStack.getName(), ctStack.getAmount());
		}
		else if (ctIngredient instanceof IngredientOr ctIngredientOr) {
			return buildIngredientArray(ctIngredientOr, RecipeHelper::buildFluidIngredient, CTHelper::buildRemovalFluidIngredient);
		}
		else {
			logInvalidIngredient(ctIngredient);
			return null;
		}
	}
	
	public static void logInvalidIngredient(IIngredient ctIngredient) {
		CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ctIngredient.getClass().getName(), ctIngredient));
	}
	
	// Array Ingredients
	
	public static IItemIngredient buildOreIngredientArray(IIngredient ctIngredient, boolean addition) {
		List<ItemStack> stackList = StreamHelper.map(ctIngredient.getItems(), x -> StackHelper.changeStackSize(getItemStack(x), ctIngredient.getAmount()));
		if (addition) {
			OreIngredient oreStack = RecipeHelper.getOreStackFromItems(stackList, ctIngredient.getAmount());
			if (oreStack != null) {
				return oreStack;
			}
		}
		return RecipeHelper.buildItemIngredient(stackList);
	}
	
	public static <T, V extends nc.recipe.ingredient.IIngredient<T>> V buildIngredientArray(IngredientOr ctIngredientOr, Function<List<?>, V> ncFunction, Function<IIngredient, V> ctFunction) {
		if (!(ctIngredientOr.getInternal() instanceof IIngredient[] ctIngredientArray)) {
			logInvalidIngredient(ctIngredientOr);
			return null;
		}
		return ncFunction.apply(StreamHelper.map(ctIngredientArray, ctFunction));
	}
	
	// ZenScript
	
	public static final ObjectSet<String> LOADED_SCRIPT_ADDONS = new ObjectOpenHashSet<>();
	
	@ZenMethod
	public static boolean isScriptAddonLoaded(String addonID) {
		return LOADED_SCRIPT_ADDONS.contains(addonID.toLowerCase(Locale.ROOT));
	}
}
