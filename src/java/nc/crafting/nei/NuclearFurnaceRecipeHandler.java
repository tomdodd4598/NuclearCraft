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
import nc.gui.machine.GuiNuclearFurnace;
import nc.item.NCItems;
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

public class NuclearFurnaceRecipeHandler extends TemplateRecipeHandler
{
    public class NuclearSmeltingPair extends CachedRecipe
    {
        public NuclearSmeltingPair(ItemStack ingred, ItemStack result) {
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
            return anuclearnuclearfuels.get((cycleticks / 24) % anuclearnuclearfuels.size()).stack;
        }

        PositionedStack ingred;
        PositionedStack result;
    }

    public static class nuclearfuelPair
    {
        public nuclearfuelPair(ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }

        public PositionedStack stack;
        public int burnTime;
    }

    public static ArrayList<nuclearfuelPair> anuclearnuclearfuels;
    public static HashSet<Block> enuclearfuels;

    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "nuclearfuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "nuclearsmelting"));
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return GuiNuclearFurnace.class;
    }

    public String getRecipeName() {
    	return "Nuclear Furnace";
    }

    public TemplateRecipeHandler newInstance() {
        if (anuclearnuclearfuels == null)
            findnuclearfuels();
        return super.newInstance();
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("nuclearsmelting") && getClass() == NuclearFurnaceRecipeHandler.class) {//don't want subclasses getting a hold of this
            @SuppressWarnings("unchecked")
			Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
            for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
                arecipes.add(new NuclearSmeltingPair(recipe.getKey(), recipe.getValue()));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    public void loadCraftingRecipes(ItemStack result) {
        @SuppressWarnings("unchecked")
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameType(recipe.getValue(), result))
                arecipes.add(new NuclearSmeltingPair(recipe.getKey(), recipe.getValue()));
    }

    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("nuclearfuel") && getClass() == NuclearFurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
            loadCraftingRecipes("nuclearsmelting");
        else
            super.loadUsageRecipes(inputId, ingredients);
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        @SuppressWarnings("unchecked")
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
                NuclearSmeltingPair arecipe = new NuclearSmeltingPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
    }

    public String getGuiTexture() {
        return "nc:textures/gui/nuclearFurnaceNEI.png";
    }

    public void drawExtras(int recipe) {
        drawProgressBar(51, 25, 176, 0, 14, 14, 192, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 3, 0);
    }

    private static Set<Item> excludednuclearfuels() {
        Set<Item> enuclearfuels = new HashSet<Item>();
        enuclearfuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        enuclearfuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
        enuclearfuels.add(Item.getItemFromBlock(Blocks.standing_sign));
        enuclearfuels.add(Item.getItemFromBlock(Blocks.wall_sign));
        enuclearfuels.add(Item.getItemFromBlock(Blocks.wooden_door));
        enuclearfuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
        return enuclearfuels;
    }

    private static void findnuclearfuels() {
        anuclearnuclearfuels = new ArrayList<nuclearfuelPair>();
        Set<Item> enuclearfuels = excludednuclearfuels();
        for (ItemStack item : ItemList.items)
            if (!enuclearfuels.contains(item.getItem())) {
                int burnTime = getItemBurnTime(item);
                if (burnTime > 0)
                	anuclearnuclearfuels.add(new nuclearfuelPair(item.copy(), burnTime));
            }
    }
    
    public static int getItemBurnTime(ItemStack itemstack) {
		if (itemstack == null) {
			return 0;
		} else {
			Item item = itemstack.getItem();
			if(item == new ItemStack(NCItems.material, 1, 4).getItem() && item.getDamage(itemstack) == 4) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*Math.ceil(300/NuclearCraft.nuclearFurnaceCookSpeed));
			} else if(item == new ItemStack(NCItems.material, 1, 5).getItem() && item.getDamage(itemstack) == 5) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*Math.ceil(300/NuclearCraft.nuclearFurnaceCookSpeed));
			} else if(item == new ItemStack(NCItems.material, 1, 19).getItem() && item.getDamage(itemstack) == 19) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*Math.ceil(300/NuclearCraft.nuclearFurnaceCookSpeed));
			} else if(item == new ItemStack(NCItems.material, 1, 20).getItem() && item.getDamage(itemstack) == 20) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*Math.ceil(300/NuclearCraft.nuclearFurnaceCookSpeed));
			}
		return 0;
		}
	}
}
