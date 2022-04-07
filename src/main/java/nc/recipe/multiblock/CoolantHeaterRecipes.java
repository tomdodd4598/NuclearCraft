package nc.recipe.multiblock;

import static nc.config.NCConfig.fission_heater_cooling_rate;
import static nc.init.NCCoolantFluids.COOLANTS;

import java.util.*;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import it.unimi.dsi.fastutil.objects.*;
import nc.init.NCBlocks;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.Tank;
import nc.util.PermutationHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CoolantHeaterRecipes extends BasicRecipeHandler {
	
	public CoolantHeaterRecipes() {
		super("coolant_heater", 1, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("nak", 1), fluidStack("nak_hot", 1), fission_heater_cooling_rate[0], "standard_heater");
		for (int i = 1; i < COOLANTS.size(); ++i) {
			ItemStack heater = new ItemStack(i < 16 ? NCBlocks.salt_fission_heater : NCBlocks.salt_fission_heater2, 1, i % 16);
			String ruleName = COOLANTS.get(i) + "_heater";
			addRecipe(heater, fluidStack(COOLANTS.get(i) + "_nak", 1), fluidStack(COOLANTS.get(i) + "_nak_hot", 1), fission_heater_cooling_rate[i], ruleName);
		}
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 0);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof String ? (String) extras.get(1) : "");
		return fixed;
	}
	
	public @Nullable RecipeInfo<BasicRecipe> getRecipeInfoFromHeaterInputs(String heaterType, List<Tank> fluidInputs) {
		long hash = 31L * (heaterType + "_heater").hashCode() + RecipeHelper.hashMaterialsRaw(new ArrayList<>(), fluidInputs);
		if (recipeCache.containsKey(hash)) {
			ObjectSet<BasicRecipe> set = recipeCache.get(hash);
			for (BasicRecipe recipe : set) {
				if (recipe instanceof CoolantHeaterRecipe) {
					CoolantHeaterRecipe heaterRecipe = (CoolantHeaterRecipe) recipe;
					RecipeMatchResult matchResult = heaterRecipe.matchHeaterInputs(heaterType, fluidInputs);
					if (matchResult.matches()) {
						return new RecipeInfo<>(heaterRecipe, matchResult);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected void fillHashCache() {
		for (BasicRecipe recipe : recipeList) {
			List<Pair<List<ItemStack>, List<FluidStack>>> materialListTuples = new ArrayList<>();
			
			if (!prepareMaterialListTuples(recipe, materialListTuples)) {
				continue;
			}
			
			for (Pair<List<ItemStack>, List<FluidStack>> materials : materialListTuples) {
				for (List<FluidStack> fluids : PermutationHelper.permutations(materials.getRight())) {
					long hash = 31L * recipe.getCoolantHeaterPlacementRule().hashCode() + RecipeHelper.hashMaterials(new ArrayList<>(), fluids);
					if (recipeCache.containsKey(hash)) {
						recipeCache.get(hash).add(recipe);
					}
					else {
						ObjectSet<BasicRecipe> set = new ObjectOpenHashSet<>();
						set.add(recipe);
						recipeCache.put(hash, set);
					}
				}
			}
		}
	}
	
	@Override
	public BasicRecipe newRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
		return new CoolantHeaterRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
	}
	
	public static class CoolantHeaterRecipe extends BasicRecipe {
		
		public CoolantHeaterRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
			super(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
		}
		
		public RecipeMatchResult matchHeaterInputs(String heaterType, List<Tank> fluidInputs) {
			if (!getCoolantHeaterPlacementRule().equals(heaterType + "_heater")) {
				return RecipeMatchResult.FAIL;
			}
			return RecipeHelper.matchIngredients(IngredientSorption.INPUT, new ArrayList<>(), fluidIngredients, new ArrayList<>(), fluidInputs, isShapeless);
		}
	}
}
