package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.crafting.machine.FusionRecipes;
import nc.gui.generator.GuiFusionReactorSteam;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class FusionSteamRecipeHandler extends TemplateRecipeHandler {
	public class SmeltingPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack input;
		PositionedStack input2;
		PositionedStack result;
		PositionedStack result2;
		PositionedStack result3;
		PositionedStack result4;
		PositionedStack result5;
		PositionedStack result6;

		public SmeltingPair(Object input, Object input2, Object result, Object result2, Object result3, Object result4, Object result5, Object result6) {
			super();
			// input.stackSize = 1;
			this.input = new PositionedStack(input, 21, 24);
			this.input2 = new PositionedStack(input2, 41, 24);
			this.result = new PositionedStack(result, 95, 14);
			this.result2 = new PositionedStack(result2, 115, 14);
			this.result3 = new PositionedStack(result3, 135, 14);
			this.result4 = new PositionedStack(result4, 95, 34);
			this.result5 = new PositionedStack(result5, 115, 34);
			this.result6 = new PositionedStack(result6, 135, 34);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(
					FusionSteamRecipeHandler.this.cycleticks / 24,
					Arrays.asList(new PositionedStack[] { this.input, this.input2 }));
		}

		public List<PositionedStack> getOtherStacks() {
            ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
            PositionedStack stack = getOtherStack();
            PositionedStack stack2 = getOtherStack2();
            PositionedStack stack3 = getOtherStack3();
            PositionedStack stack4 = getOtherStack4();
            PositionedStack stack5 = getOtherStack5();
            if (stack != null) stacks.add(stack);
            if (stack2 != null) stacks.add(stack2);
            if (stack3 != null) stacks.add(stack3);
            if (stack4 != null) stacks.add(stack4);
            if (stack5 != null) stacks.add(stack5);
            return stacks;
        }

		public PositionedStack getResult() {
			return this.result;
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

		public PositionedStack getOtherStack4() {
			return this.result5;
		}
		
		public PositionedStack getOtherStack5() {
			return this.result6;
		}
	}

	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(40, 141, 124, 18), "fusing", new Object[0]));
		//this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(166, 219, 66, 18), "fusing", new Object[0]));
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiFusionReactorSteam.class;
	}

	public String getRecipeName() {
		return "Fusion Reactor";
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("fusing"))	&& (getClass() == FusionSteamRecipeHandler.class)) {
			Map<Object[], Object[]> recipes = FusionRecipes.instance().getRecipes();
			for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getKey()[1], recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2], recipe.getValue()[3], recipe.getValue()[4], recipe.getValue()[5]));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result) {
		Map<Object[], Object[]> recipes = FusionRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = FusionRecipes.instance().containsStack(result, recipe.getValue(), false);
			if (pos!=-1) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getKey()[1], recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2], recipe.getValue()[3], recipe.getValue()[4], recipe.getValue()[5]));	
			}
		}
	}

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if ((inputId.equals("fusing"))&& (getClass() == FusionSteamRecipeHandler.class)) {
			loadCraftingRecipes("fusing", new Object[0]);
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		Map<Object[], Object[]> recipes = FusionRecipes.instance().getRecipes();
		for (Map.Entry<Object[], Object[]> recipe : recipes.entrySet()) {
			int pos = FusionRecipes.instance().containsStack(ingredient, recipe.getKey(), false);
			if (pos!=-1) {
				this.arecipes.add(new SmeltingPair(recipe.getKey()[0], recipe.getKey()[1], recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2], recipe.getValue()[3], recipe.getValue()[4], recipe.getValue()[5]));
			}
		}
	}

	public String getGuiTexture() {
		return "nc:textures/gui/fusionReactorNEI.png";
	}

	public void drawExtras(int recipe) {
		drawProgressBar(64, 23, 176, 14, 24, 17, 40, 0);
	}
}