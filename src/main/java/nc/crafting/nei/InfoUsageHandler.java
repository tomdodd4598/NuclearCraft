package nc.crafting.nei;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class InfoUsageHandler extends TemplateRecipeHandler {
	public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	public static ArrayList<InfoPair> ainfo;
	public InfoUsageHandler() {}
  
	public class InfoPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack input;
		public String info;
    
		public InfoPair(ItemStack input, String info) {
			super();
			this.info = info;
			input.stackSize = 1;
			this.input = new PositionedStack(input, 147, 5);
		}
    
		public PositionedStack getResult() {
			return this.input;
		}
	}

	public String getRecipeName() {
		return "NuclearCraft Info";
	}
	
	public void loadCraftingRecipes(ItemStack result) {
        @SuppressWarnings("unchecked")
        Map<ItemStack, String> recipes = InfoRecipes.info().getInfoList();
        for (Entry<ItemStack, String> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameType((ItemStack)recipe.getKey(), result)) {
            	InfoPair arecipe = new InfoPair(recipe.getKey(), (String)recipe.getValue());
                this.arecipes.add(arecipe);
			}
    }
  
	public void loadUsageRecipes(ItemStack ingredient) {
		@SuppressWarnings("unchecked")
		Map<ItemStack, String> recipes = InfoRecipes.info().getInfoList();
		for (Map.Entry<ItemStack, String> recipe : recipes.entrySet())
			if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack)recipe.getKey(), ingredient)) {
				InfoPair arecipe = new InfoPair(ingredient, (String)recipe.getValue());
				arecipe.setIngredientPermutation(Arrays.asList(new PositionedStack[] { arecipe.input }), ingredient);
				this.arecipes.add(arecipe);
		}
	}
  
	public String getGuiTexture() {
		return "nc:textures/gui/itemInfoNEI.png";
	}
  
	public void drawExtras(int recipe) {
		ItemStack stack = ((TemplateRecipeHandler.CachedRecipe)this.arecipes.get(recipe)).getResult().item;
		String info = InfoRecipes.info().getInfo(stack);
		String[] parts = info.split("-");
		int length = parts.length;
    
		fontRenderer.drawString(stack.getDisplayName(), 5, 4, 4210752);
		for (int i = 1; i <= length; i++) {
			fontRenderer.drawString(parts[i-1], 5, 4+(10*i), 4210752);
		}
	}

	public int recipiesPerPage() {
		return 1;
	}
}