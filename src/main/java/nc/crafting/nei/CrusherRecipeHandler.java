package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.NuclearCraft;
import nc.crafting.machine.CrusherRecipesOld;
import nc.gui.machine.GuiCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CrusherRecipeHandler extends TemplateRecipeHandler
{
    public class CrushingPair extends CachedRecipe
    {
        public CrushingPair(ItemStack ingred, ItemStack result) {
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
            return acrushfuels.get((cycleticks / 24) % acrushfuels.size()).stack;
        }

        PositionedStack ingred;
        PositionedStack result;
    }

    public static class CrusherFuelPair
    {
        public CrusherFuelPair(ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }

        public PositionedStack stack;
        public int burnTime;
    }

    public static ArrayList<CrusherFuelPair> acrushfuels;
    public static HashSet<Block> ecrushfuels;

    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "crushfuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "crushing"));
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrusher.class;
    }

    public String getRecipeName() {
        return "Crusher";
    }

    public TemplateRecipeHandler newInstance() {
        if (ecrushfuels == null)
            findCrushFuels();
        return super.newInstance();
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crushing") && getClass() == CrusherRecipeHandler.class) {//don't want subclasses getting a hold of this
            Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) CrusherRecipesOld.smelting().getSmeltingList();
            for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
                arecipes.add(new CrushingPair(recipe.getKey(), recipe.getValue()));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    public void loadCraftingRecipes(ItemStack result) {
        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) CrusherRecipesOld.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameType(recipe.getValue(), result))
                arecipes.add(new CrushingPair(recipe.getKey(), recipe.getValue()));
    }

    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("crushfuel") && getClass() == CrusherRecipeHandler.class)//don't want subclasses getting a hold of this
            loadCraftingRecipes("crushing");
        else
            super.loadUsageRecipes(inputId, ingredients);
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) CrusherRecipesOld.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
                CrushingPair arecipe = new CrushingPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
    }

    public String getGuiTexture() {
        return "nc:textures/gui/crusher.png";
    }

    public void drawExtras(int recipe) {
        drawProgressBar(51, 25, 176, 0, 14, 14, 24, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }

    private static Set<Item> excludedFuels() {
        Set<Item> ecrushfuels = new HashSet<Item>();
        ecrushfuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        ecrushfuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
        ecrushfuels.add(Item.getItemFromBlock(Blocks.standing_sign));
        ecrushfuels.add(Item.getItemFromBlock(Blocks.wall_sign));
        ecrushfuels.add(Item.getItemFromBlock(Blocks.wooden_door));
        ecrushfuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
        return ecrushfuels;
    }

    private static void findCrushFuels() {
        acrushfuels = new ArrayList<CrusherFuelPair>();
        Set<Item> ecrushfuels = excludedFuels();
        for (ItemStack item : ItemList.items)
            if (!ecrushfuels.contains(item.getItem())) {
                int burnTime = getItemBurnTime(item);
                if (burnTime > 0) acrushfuels.add(new CrusherFuelPair(item.copy(), burnTime));
            }
    }
    
    public static int getItemBurnTime(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            Item item = itemstack.getItem();
            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
                Block block = Block.getBlockFromItem(item);
                if (block == Blocks.wooden_slab) {
                	return 8000/NuclearCraft.crusherCrushEfficiency;
                }
                if (block.getMaterial() == Material.wood) {
                	return 16000/NuclearCraft.crusherCrushEfficiency;
                }
                if (block == Blocks.coal_block) {
                	return 960000/NuclearCraft.crusherCrushEfficiency;
                }
            }
            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD"))
            	return 8000/NuclearCraft.crusherCrushEfficiency;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD"))
            	return 8000/NuclearCraft.crusherCrushEfficiency;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD"))
            	return 8000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.stick) return 4000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.coal) return 96000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.lava_bucket) return 1200000/NuclearCraft.crusherCrushEfficiency;
            if (item == Item.getItemFromBlock(Blocks.sapling))
            	return 4000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.blaze_rod) return 144000/NuclearCraft.crusherCrushEfficiency;
            return (GameRegistry.getFuelValue(itemstack)*48)/NuclearCraft.crusherCrushEfficiency;
        }
    }
}
