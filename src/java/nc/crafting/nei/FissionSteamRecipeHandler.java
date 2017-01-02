package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.crafting.machine.FissionRecipes;
import nc.gui.generator.GuiFissionReactorSteam;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class FissionSteamRecipeHandler extends TemplateRecipeHandler {
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
					FissionSteamRecipeHandler.this.cycleticks / 24,
					Arrays.asList(new PositionedStack[] { this.input }));
		}

		public PositionedStack getResult() {
			return this.result;
		}
	}

	public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(81, 56, 56, 26), "fission", new Object[0]));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 35, 24, 18), "fission", new Object[0]));
    }

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiFissionReactorSteam.class;
	}

	public String getRecipeName() {
        return "Fission Reactor";
    }

	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("fission"))	&& (getClass() == FissionSteamRecipeHandler.class)) {
			Map<Object[], Object[]> recipes = FissionRecipes.instance().getRecipes();
			for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0]));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result) {
		Map<Object[], Object[]> recipes = FissionRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = FissionRecipes.instance().containsStack(result, recipe.getValue(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0]));				
			}
		}
	}

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if ((inputId.equals("fission"))&& (getClass() == FissionSteamRecipeHandler.class)) {
			loadCraftingRecipes("fission", new Object[0]);
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		Map<Object[], Object[]> recipes = FissionRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = FissionRecipes.instance().containsStack(ingredient, recipe.getKey(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0]));				
			}
		}
	}

	public String getGuiTexture() {
        return "nc:textures/gui/fissionReactorNEI.png";
    }

	public void drawExtras(int recipe) {
        drawProgressBar(3, 14, 176, 31, 9, 35, 40, 3);
        drawProgressBar(16, 14, 185, 31, 9, 35, 240, 7);
        drawProgressBar(29, 14, 194, 31, 9, 35, 160, 3);
    }
}