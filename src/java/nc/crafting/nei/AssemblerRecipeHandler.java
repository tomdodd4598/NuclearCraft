package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.crafting.machine.AssemblerRecipes;
import nc.gui.machine.GuiAssembler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class AssemblerRecipeHandler extends TemplateRecipeHandler {
	public class SmeltingPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack input;
		PositionedStack input2;
		PositionedStack input3;
		PositionedStack input4;
		PositionedStack result;

		public SmeltingPair(Object input, Object input2, Object input3, Object input4, Object result) {
			super();
			// input.stackSize = 1;
			this.input = new PositionedStack(input, 36, 14);
			this.input2 = new PositionedStack(input2, 56, 14);
			this.input3 = new PositionedStack(input3, 36, 34);
			this.input4 = new PositionedStack(input4, 56, 34);
			this.result = new PositionedStack(result, 125, 24);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(
					AssemblerRecipeHandler.this.cycleticks / 24,
					Arrays.asList(new PositionedStack[] { this.input, this.input2, this.input3, this.input4 }));
		}

		public PositionedStack getResult() {
			return this.result;
		}
	}

	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(74, 23, 49, 41), "assembling", new Object[0]));
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiAssembler.class;
	}

	public String getRecipeName() {
		return "Assembler";
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("assembling"))	&& (getClass() == AssemblerRecipeHandler.class)) {
			Map<Object[], Object[]> recipes = AssemblerRecipes.instance().getRecipes();
			for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getKey()[1], recipe.getKey()[2], recipe.getKey()[3], recipe.getValue()[0]));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result) {
		Map<Object[], Object[]> recipes = AssemblerRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = AssemblerRecipes.instance().containsStack(result, recipe.getValue(), false);
			if (pos!=-1) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getKey()[1], recipe.getKey()[2], recipe.getKey()[3], recipe.getValue()[0]));				
			}
		}
	}

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if ((inputId.equals("assembling"))&& (getClass() == AssemblerRecipeHandler.class)) {
			loadCraftingRecipes("assembling", new Object[0]);
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		Map<Object[], Object[]> recipes = AssemblerRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = AssemblerRecipes.instance().containsStack(ingredient, recipe.getKey(), false);
			if (pos!=-1) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getKey()[1], recipe.getKey()[2], recipe.getKey()[3], recipe.getValue()[0]));
			}
		}
	}

	public String getGuiTexture() {
		return "nc:textures/gui/assemblerNEI.png";
	}

	public void drawExtras(int recipe) {
		drawProgressBar(3, 15, 176, 31, 16, 34, 240, 7); //energy
		drawProgressBar(74, 23, 176, 65, 51, 17, 40, 0);
	}
}