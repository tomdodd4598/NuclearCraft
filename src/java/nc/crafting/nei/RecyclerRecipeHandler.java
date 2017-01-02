package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.crafting.machine.RecyclerRecipes;
import nc.gui.machine.GuiRecycler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecyclerRecipeHandler extends TemplateRecipeHandler {
	public class SmeltingPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack input;
		PositionedStack result;
		PositionedStack result2;
		PositionedStack result3;
		PositionedStack result4;

		public SmeltingPair(Object input, Object result, Object result2, Object result3, Object result4) {
			super();
			// input.stackSize = 1;
			this.input = new PositionedStack(input, 36, 24);
			this.result = new PositionedStack(result, 125, 14);
			this.result2 = new PositionedStack(result2, 145, 14);
			this.result3 = new PositionedStack(result3, 125, 34);
			this.result4 = new PositionedStack(result4, 145, 34);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(
					RecyclerRecipeHandler.this.cycleticks / 24,
					Arrays.asList(new PositionedStack[] { this.input }));
		}

		public PositionedStack getResult() {
			return this.result;
		}

		public List<PositionedStack> getOtherStacks() {
            ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
            PositionedStack stack = getOtherStack();
            PositionedStack stack2 = getOtherStack2();
            PositionedStack stack3 = getOtherStack3();
            if (stack != null) stacks.add(stack);
            if (stack2 != null) stacks.add(stack2);
            if (stack3 != null) stacks.add(stack3);
            return stacks;
        }

        public PositionedStack getOtherStack() {
            return this.result2;
        }
        
        public PositionedStack getOtherStack2() {
            return this.result3;
        }
        
        public PositionedStack getOtherStack3() {
            return this.result4;
        }
	}

	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 14, 57, 34), "recycling", new Object[0]));
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiRecycler.class;
	}

	public String getRecipeName() {
		return "Reprocessing Plant";
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("recycling"))	&& (getClass() == RecyclerRecipeHandler.class)) {
			Map<Object[], Object[]> recipes = RecyclerRecipes.instance().getRecipes();
			for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2], recipe.getValue()[3]));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result) {
		Map<Object[], Object[]> recipes = RecyclerRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = RecyclerRecipes.instance().containsStack(result, recipe.getValue(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2], recipe.getValue()[3]));
			}
		}
	}

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if ((inputId.equals("recycling"))&& (getClass() == RecyclerRecipeHandler.class)) {
			loadCraftingRecipes("recycling", new Object[0]);
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		Map<Object[], Object[]> recipes = RecyclerRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = RecyclerRecipes.instance().containsStack(ingredient, recipe.getKey(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2], recipe.getValue()[3]));
			}
		}
	}

	public String getGuiTexture() {
		return "nc:textures/gui/recyclerNEI.png";
	}

	public void drawExtras(int recipe) {
		drawProgressBar(3, 15, 176, 31, 16, 34, 240, 7); //energy
		drawProgressBar(54, 13, 176, 65, 71, 38, 40, 0);
	}
}