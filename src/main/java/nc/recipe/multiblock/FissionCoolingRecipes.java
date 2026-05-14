package nc.recipe.multiblock;

import it.unimi.dsi.fastutil.objects.*;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.Tank;
import nc.util.PermutationHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.*;
import java.util.*;

public abstract class FissionCoolingRecipes extends BasicRecipeHandler {
	
	protected final @Nonnull String coolerTypeSuffix;
	
	public FissionCoolingRecipes(@Nonnull String name, @Nonnull String coolerTypeSuffix) {
		super(name, 1, 1, 0, 1);
		this.coolerTypeSuffix = coolerTypeSuffix;
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 0);
		fixer.add(String.class, "");
		return fixer.fixed;
	}
	
	public @Nullable RecipeInfo<BasicRecipe> getRecipeInfoFromInputs(String coolerType, List<Tank> fluidInputs) {
		long hash = 31L * (coolerType + coolerTypeSuffix).hashCode() + RecipeHelper.hashMaterialsRaw(Collections.emptyList(), fluidInputs);
		if (recipeCache.containsKey(hash)) {
			ObjectSet<BasicRecipe> set = recipeCache.get(hash);
			for (BasicRecipe recipe : set) {
				if (recipe instanceof FissionCoolingRecipe coolingRecipe) {
					RecipeMatchResult matchResult = coolingRecipe.matchCoolerInputs(coolerType, fluidInputs);
					if (matchResult.isMatch) {
						return new RecipeInfo<>(coolingRecipe, matchResult);
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
					long hash = 31L * recipe.getFissionCoolingPlacementRule().hashCode() + RecipeHelper.hashMaterials(Collections.emptyList(), fluids);
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
		return new FissionCoolingRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
	}
	
	public class FissionCoolingRecipe extends BasicRecipe {
		
		public FissionCoolingRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
			super(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
		}
		
		public RecipeMatchResult matchCoolerInputs(String coolerType, List<Tank> fluidInputs) {
			if (!getFissionCoolingPlacementRule().equals(coolerType + coolerTypeSuffix)) {
				return RecipeMatchResult.FAIL;
			}
			return RecipeHelper.matchIngredients(IngredientSorption.INPUT, Collections.emptyList(), fluidIngredients, Collections.emptyList(), fluidInputs, isShapeless);
		}
	}
}
