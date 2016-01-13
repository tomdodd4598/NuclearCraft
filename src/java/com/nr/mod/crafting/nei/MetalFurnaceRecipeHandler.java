package com.nr.mod.crafting.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.nr.mod.blocks.tileentities.TileEntityFurnace;
import com.nr.mod.gui.GuiFurnace;

public class MetalFurnaceRecipeHandler extends TemplateRecipeHandler
{
    public class MetalSmeltingPair extends CachedRecipe
    {
        public MetalSmeltingPair(ItemStack ingred, ItemStack result) {
            ingred.stackSize = 1;
            this.ingred = new PositionedStack(ingred, 51, 6);
            this.result = new PositionedStack(result, 111, 24);
        }

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 24, Arrays.asList(ingred));
        }

        public PositionedStack getResult() {
            return result;
        }

        public PositionedStack getOtherStack() {
            return ametalfuels.get((cycleticks / 24) % ametalfuels.size()).stack;
        }

        PositionedStack ingred;
        PositionedStack result;
    }

    public static class MetalFuelPair
    {
        public MetalFuelPair(ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }

        public PositionedStack stack;
        public int burnTime;
    }

    public static ArrayList<MetalFuelPair> ametalfuels;
    public static HashSet<Block> emetalfuels;

    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "metalfuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "metalsmelting"));
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFurnace.class;
    }

    public String getRecipeName() {
        return "Metal Furnace";
    }

    public TemplateRecipeHandler newInstance() {
        if (ametalfuels == null)
            findMetalFuels();
        return super.newInstance();
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("metalsmelting") && getClass() == MetalFurnaceRecipeHandler.class) {//don't want subclasses getting a hold of this
            @SuppressWarnings("unchecked") Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
            for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
                arecipes.add(new MetalSmeltingPair(recipe.getKey(), recipe.getValue()));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

   public void loadCraftingRecipes(ItemStack result) {
        @SuppressWarnings("unchecked") Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameType(recipe.getValue(), result))
                arecipes.add(new MetalSmeltingPair(recipe.getKey(), recipe.getValue()));
    }

    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("metalfuel") && getClass() == MetalFurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
            loadCraftingRecipes("metalsmelting");
        else
            super.loadUsageRecipes(inputId, ingredients);
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        @SuppressWarnings("unchecked") Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
                MetalSmeltingPair arecipe = new MetalSmeltingPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
    }

   public String getGuiTexture() {
        return "nr:textures/gui/furnace.png";
    }

    public void drawExtras(int recipe) {
        drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 40, 0);
    }

    private static Set<Item> excludedFuels() {
        Set<Item> emetalfuels = new HashSet<Item>();
        emetalfuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        emetalfuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
        emetalfuels.add(Item.getItemFromBlock(Blocks.standing_sign));
        emetalfuels.add(Item.getItemFromBlock(Blocks.wall_sign));
        emetalfuels.add(Item.getItemFromBlock(Blocks.wooden_door));
        emetalfuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
        return emetalfuels;
    }

    private static void findMetalFuels() {
        ametalfuels = new ArrayList<MetalFuelPair>();
        Set<Item> emetalfuels = excludedFuels();
        for (ItemStack item : ItemList.items)
            if (!emetalfuels.contains(item.getItem())) {
                int burnTime = TileEntityFurnace.getItemBurnTime(item);
                if (burnTime > 0)
                    ametalfuels.add(new MetalFuelPair(item.copy(), burnTime));
            }
    }

    public String getOverlayIdentifier() {
        return "metalsmelting";
    }
}
