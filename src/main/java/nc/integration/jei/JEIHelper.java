package nc.integration.jei;

import java.util.*;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.*;
import nc.util.NCUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JEIHelper {
	
	public static List<JEIBasicRecipeWrapper> getJEIRecipes(IGuiHelper guiHelper, IJEIHandler jeiHandler, BasicRecipeHandler recipeHandler, Class<? extends JEIBasicRecipeWrapper> recipeWrapper) {
		ArrayList<JEIBasicRecipeWrapper> recipes = new ArrayList<>();
		if (recipeHandler != null) {
			for (BasicRecipe recipe : recipeHandler.getRecipeList()) {
				try {
					recipes.add(recipeWrapper.getConstructor(IGuiHelper.class, IJEIHandler.class, BasicRecipeHandler.class, BasicRecipe.class).newInstance(guiHelper, jeiHandler, recipeHandler, recipe));
				}
				catch (Exception e) {
					NCUtil.getLogger().catching(e);
				}
			}
		}
		return recipes;
	}
	
	public static class RecipeItemMapper {
		
		public Object2ObjectMap<IngredientSorption, Int2ObjectMap<RecipeItemMapping>> map = new Object2ObjectOpenHashMap<>();
		
		public RecipeItemMapper() {}
		
		public void map(IngredientSorption type, int recipePos, int slotPos, int xPos, int yPos) {
			this.map(type, recipePos, new RecipeItemMapping(slotPos, xPos, yPos));
		}
		
		public void map(IngredientSorption type, int recipePos, RecipeItemMapping mapping) {
			if (map.get(type) == null) {
				map.put(type, new Int2ObjectOpenHashMap<>());
			}
			map.get(type).put(recipePos, mapping);
		}
		
		public void mapItemsTo(IGuiItemStackGroup items, IIngredients ingredients) {
			for (Object2ObjectMap.Entry<IngredientSorption, Int2ObjectMap<RecipeItemMapping>> entry : map.object2ObjectEntrySet()) {
				List objects = entry.getKey() == IngredientSorption.INPUT ? ingredients.getInputs(ItemStack.class) : ingredients.getOutputs(ItemStack.class);
				for (Int2ObjectMap.Entry<RecipeItemMapping> mapping : entry.getValue().int2ObjectEntrySet()) {
					RecipeItemMapping recipe = mapping.getValue();
					items.init(recipe.slotPos, entry.getKey() == IngredientSorption.INPUT, recipe.xPos, recipe.yPos);
					Object obj = objects.get(mapping.getIntKey());
					if (obj instanceof List) {
						items.set(recipe.slotPos, (List<ItemStack>) obj);
					}
					else {
						items.set(recipe.slotPos, (ItemStack) obj);
					}
				}
			}
		}
	}
	
	public static class RecipeFluidMapper {
		
		public Object2ObjectMap<IngredientSorption, Int2ObjectMap<RecipeFluidMapping>> map = new Object2ObjectOpenHashMap<>();
		
		public RecipeFluidMapper() {}
		
		public void map(IngredientSorption type, int recipePos, int slotPos, int xPos, int yPos, int xSize, int ySize) {
			this.map(type, recipePos, new RecipeFluidMapping(slotPos, xPos, yPos, xSize, ySize));
		}
		
		public void map(IngredientSorption type, int recipePos, RecipeFluidMapping mapping) {
			if (map.get(type) == null) {
				map.put(type, new Int2ObjectOpenHashMap<>());
			}
			map.get(type).put(recipePos, mapping);
		}
		
		public void mapFluidsTo(IGuiFluidStackGroup fluids, IIngredients ingredients) {
			for (Object2ObjectMap.Entry<IngredientSorption, Int2ObjectMap<RecipeFluidMapping>> entry : map.object2ObjectEntrySet()) {
				List objects = entry.getKey() == IngredientSorption.INPUT ? ingredients.getInputs(FluidStack.class) : ingredients.getOutputs(FluidStack.class);
				for (Int2ObjectMap.Entry<RecipeFluidMapping> mapping : entry.getValue().int2ObjectEntrySet()) {
					RecipeFluidMapping recipe = mapping.getValue();
					Object obj = objects.get(mapping.getIntKey());
					if (obj instanceof List) {
						List<FluidStack> list = (List<FluidStack>) obj;
						FluidStack stack = list.isEmpty() ? null : list.get(list.size() - 1);
						fluids.init(recipe.slotPos, entry.getKey() == IngredientSorption.INPUT, recipe.xPos + 1, recipe.yPos + 1, recipe.xSize, recipe.ySize, stack == null ? 1000 : Math.max(1, stack.amount), true, null);
						fluids.set(recipe.slotPos, stack == null ? null : (List<FluidStack>) obj);
					}
					else {
						FluidStack stack = (FluidStack) obj;
						fluids.init(recipe.slotPos, entry.getKey() == IngredientSorption.INPUT, recipe.xPos + 1, recipe.yPos + 1, recipe.xSize, recipe.ySize, stack == null ? 1000 : Math.max(1, stack.amount), true, null);
						fluids.set(recipe.slotPos, stack);
					}
				}
			}
		}
	}
	
	protected static class RecipeItemMapping {
		
		public int slotPos, xPos, yPos;
		
		public RecipeItemMapping(int slotPos, int xPos, int yPos) {
			this.slotPos = slotPos;
			this.xPos = xPos;
			this.yPos = yPos;
		}
	}
	
	protected static class RecipeFluidMapping {
		
		public int slotPos, xPos, yPos, xSize, ySize;
		
		public RecipeFluidMapping(int slotPos, int xPos, int yPos, int xSize, int ySize) {
			this.slotPos = slotPos;
			this.xPos = xPos;
			this.yPos = yPos;
			this.xSize = xSize;
			this.ySize = ySize;
		}
	}
}
