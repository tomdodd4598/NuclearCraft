package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.crafting.machine.SeparatorRecipes;
import nc.gui.machine.GuiSeparator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class SeparatorRecipeHandler extends TemplateRecipeHandler {
	public class SmeltingPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack input;
		PositionedStack result;
		PositionedStack result2;

		public SmeltingPair(Object input, Object result, Object result2) {
			super();
			// input.stackSize = 1;
			this.input = new PositionedStack(input, 36, 24);
			this.result = new PositionedStack(result, 125, 14);
			this.result2 = new PositionedStack(result2, 125, 34);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(
					SeparatorRecipeHandler.this.cycleticks / 24,
					Arrays.asList(new PositionedStack[] { this.input }));
		}

		public PositionedStack getResult() {
			return this.result;
		}

		public PositionedStack getOtherStack() {
			return this.result2;
		}
	}

	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 14, 57, 34), "separating", new Object[0]));
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiSeparator.class;
	}

	public String getRecipeName() {
		return "Isotope Separator";
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("separating"))	&& (getClass() == SeparatorRecipeHandler.class)) {
			Map<Object[], Object[]> recipes = SeparatorRecipes.instance().getRecipes();
			for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0], recipe.getValue()[1]));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result) {
		Map<Object[], Object[]> recipes = SeparatorRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = SeparatorRecipes.instance().containsStack(result, recipe.getValue(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0], recipe.getValue()[1]));				
			}
		}
	}

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if ((inputId.equals("separating"))&& (getClass() == SeparatorRecipeHandler.class)) {
			loadCraftingRecipes("separating", new Object[0]);
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		Map<Object[], Object[]> recipes = SeparatorRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = SeparatorRecipes.instance().containsStack(ingredient, recipe.getKey(), false);
			if (pos!=-1) {
			this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getValue()[0], recipe.getValue()[1]));				
			}
		}
	}

	public String getGuiTexture() {
		return "nc:textures/gui/separatorNEI.png";
	}

	public void drawExtras(int recipe) {
		drawProgressBar(3, 15, 176, 31, 16, 34, 240, 7); //energy
		drawProgressBar(54, 15, 176, 65, 71, 34, 40, 0);
	}
}