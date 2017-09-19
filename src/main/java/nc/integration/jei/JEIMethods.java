package nc.integration.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IRecipe;
import nc.recipe.SorptionType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JEIMethods {

	public static List<IJEIRecipeBuilder> recipeBuilders = new ArrayList();

	public static void registerRecipeBuilder(IJEIRecipeBuilder builder) {
		recipeBuilders.add(builder);
	}

	public static Object createJEIRecipe(IRecipe recipe, BaseRecipeHandler<IRecipe> helper) {
		for (IJEIRecipeBuilder builder : recipeBuilders) {
			Object build = builder.buildRecipe(recipe, helper);
			if (build != null) {
				return build;
			}
		}
		return null;
	}

	public static class RecipeItemMapper {

		public Map<SorptionType, Map<Integer, RecipeItemMapping>> map = new HashMap();

		public RecipeItemMapper() {}

		public void map(SorptionType type, int recipePos, int slotPos, int xPos, int yPos) {
			this.map(type, recipePos, new RecipeItemMapping(slotPos, xPos, yPos));
		}

		public void map(SorptionType type, int recipePos, RecipeItemMapping mapping) {
			if (map.get(type) == null) {
				map.put(type, new HashMap());
			}
			map.get(type).put(recipePos, mapping);
		}

		public void mapItemsTo(IGuiItemStackGroup items, IIngredients ingredients) {
			for (Entry<SorptionType, Map<Integer, RecipeItemMapping>> entry : map.entrySet()) {
				List objects = entry.getKey() == SorptionType.INPUT ? ingredients.getInputs(ItemStack.class) : ingredients.getOutputs(ItemStack.class);
				for (Entry<Integer, RecipeItemMapping> mapping : entry.getValue().entrySet()) {
					RecipeItemMapping recipe = mapping.getValue();
					items.init(recipe.slotPos, entry.getKey() == SorptionType.INPUT, recipe.xPos, recipe.yPos);
					Object obj = objects.get(mapping.getKey());
					if (obj instanceof List) {
						items.set(recipe.slotPos, (List) obj);
					} else {
						items.set(recipe.slotPos, (ItemStack) obj);
					}
				}
			}
		}
	}
	
	public static class RecipeFluidMapper {

		public Map<SorptionType, Map<Integer, RecipeFluidMapping>> map = new HashMap();

		public RecipeFluidMapper() {}

		public void map(SorptionType type, int recipePos, int slotPos, int xPos, int yPos, int xSize, int ySize) {
			this.map(type, recipePos, new RecipeFluidMapping(slotPos, xPos, yPos, xSize, ySize));
		}

		public void map(SorptionType type, int recipePos, RecipeFluidMapping mapping) {
			if (map.get(type) == null) {
				map.put(type, new HashMap());
			}
			map.get(type).put(recipePos, mapping);
		}
		
		public void mapFluidsTo(IGuiFluidStackGroup fluids, IIngredients ingredients) {
			for (Entry<SorptionType, Map<Integer, RecipeFluidMapping>> entry : map.entrySet()) {
				List objects = entry.getKey() == SorptionType.INPUT ? ingredients.getInputs(FluidStack.class) : ingredients.getOutputs(FluidStack.class);
				for (Entry<Integer, RecipeFluidMapping> mapping : entry.getValue().entrySet()) {
					RecipeFluidMapping recipe = mapping.getValue();
					Object obj = objects.get(mapping.getKey());
					if (obj instanceof List) {
						fluids.init(recipe.slotPos, entry.getKey() == SorptionType.INPUT, recipe.xPos + 1, recipe.yPos + 1, recipe.xSize, recipe.ySize, ((FluidStack) ((List) obj).get(0)).amount, true, null);
						fluids.set(recipe.slotPos, (List) obj);
					} else {
						fluids.init(recipe.slotPos, entry.getKey() == SorptionType.INPUT, recipe.xPos + 1, recipe.yPos + 1, recipe.xSize, recipe.ySize, ((FluidStack) obj).amount, true, null);
						fluids.set(recipe.slotPos, (FluidStack) obj);
					}
				}
			}
		}
	}

	public static class RecipeItemMapping {
		public int slotPos, xPos, yPos;

		public RecipeItemMapping(int slotPos, int xPos, int yPos) {
			this.slotPos = slotPos;
			this.xPos = xPos;
			this.yPos = yPos;
		}
	}
	
	public static class RecipeFluidMapping {
		public int slotPos, xPos, yPos, xSize, ySize;

		public RecipeFluidMapping(int slotPos, int xPos, int yPos, int xSize, int ySize) {
			this.slotPos = slotPos;
			this.xPos = xPos;
			this.yPos = yPos;
			this.xSize = xSize;
			this.ySize = ySize;
		}
	}

	public static ArrayList<JEIRecipe> getJEIRecipes(BaseRecipeHandler recipeHelper, Class<? extends JEIRecipe> recipeClass) {
		ArrayList<JEIRecipe> recipes = new ArrayList();
		if (recipeHelper != null && recipeHelper instanceof BaseRecipeHandler) {
			BaseRecipeHandler helper = (BaseRecipeHandler) recipeHelper;
			for (IRecipe recipe : (ArrayList<IRecipe>) helper.getRecipes()) {
				try {
					recipes.add(recipeClass.getConstructor(BaseRecipeHandler.class, IRecipe.class).newInstance(helper, recipe));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return recipes;
	}
}
