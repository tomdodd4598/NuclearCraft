package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.crafting.machine.HastenerRecipes;
import nc.gui.machine.GuiHastener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class HastenerRecipeHandler extends TemplateRecipeHandler {
	public class SmeltingPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack input;
		PositionedStack result;

		public SmeltingPair(Object input, Object result) {
			super();
			// input.stackSize = 1;
			this.input = new PositionedStack(input, 51, 24);
			this.result = new PositionedStack(result, 111, 24);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(
					HastenerRecipeHandler.this.cycleticks / 24,
					Arrays.asList(new PositionedStack[] { this.input }));
		}

		public PositionedStack getResult() {
			return this.result;
		}
	}

	public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "hastening", new Object[0]));
    }

	public Class<? extends GuiContainer> getGuiClass() {
        return GuiHastener.class;
    }

    public String getRecipeName() {
        return "Decay Hastener";
    }

	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("hastening"))	&& (getClass() == HastenerRecipeHandler.class)) {
			Map<Object[], Object[]> recipes = HastenerRecipes.instance().getRecipes();
			for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0]));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result) {
		Map<Object[], Object[]> recipes = HastenerRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = HastenerRecipes.instance().containsStack(result, recipe.getValue(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0]));				
			}
		}
	}

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if ((inputId.equals("hastening"))&& (getClass() == HastenerRecipeHandler.class)) {
			loadCraftingRecipes("hastening", new Object[0]);
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		Map<Object[], Object[]> recipes = HastenerRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = HastenerRecipes.instance().containsStack(ingredient, recipe.getKey(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0]));				
			}
		}
	}

	public String getGuiTexture() {
        return "nc:textures/gui/hastenerNEI.png";
    }

    public void drawExtras(int recipe) {
        drawProgressBar(3, 15, 176, 31, 16, 34, 240, 7); //energy
        drawProgressBar(74, 23, 176, 14, 24, 16, 40, 0);
    }
}
