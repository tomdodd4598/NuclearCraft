package nc.crafting.nei;

import static codechicken.nei.NEIClientUtils.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.NuclearCraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class MetalFurnaceFuelRecipeHandler extends MetalFurnaceRecipeHandler
{
    public class CachedFuelRecipe extends CachedRecipe
    {
        public MetalFuelPair metalfuel;

        public CachedFuelRecipe(MetalFuelPair fuel) {
            this.metalfuel = fuel;
        }

        public PositionedStack getIngredient() {
            return mmetalfurnace.get(cycleticks / 24 % mmetalfurnace.size()).ingred;
        }

        public PositionedStack getResult() {
            return mmetalfurnace.get(cycleticks / 24 % mmetalfurnace.size()).result;
        }

        public PositionedStack getOtherStack() {
            return metalfuel.stack;
        }
    }

    private ArrayList<MetalSmeltingPair> mmetalfurnace = new ArrayList<MetalFurnaceRecipeHandler.MetalSmeltingPair>();

    public MetalFurnaceFuelRecipeHandler() {
        super();
        loadAllSmelting();
    }

    public String getRecipeName() {
        return "Metal Furnace Fuel";
    }

    private void loadAllSmelting() {
        @SuppressWarnings("unchecked") Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();

        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            mmetalfurnace.add(new MetalSmeltingPair(recipe.getKey(), recipe.getValue()));
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("metalfuel") && getClass() == MetalFurnaceFuelRecipeHandler.class)
            for (MetalFuelPair fuel : ametalfuels)
                arecipes.add(new CachedFuelRecipe(fuel));
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for (MetalFuelPair fuel : ametalfuels)
            if (fuel.stack.contains(ingredient))
                arecipes.add(new CachedFuelRecipe(fuel));
    }

    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedFuelRecipe crecipe = (CachedFuelRecipe) arecipes.get(recipe);
        MetalFuelPair fuel = crecipe.metalfuel;
        float burnTime = (float) ((double) (fuel.burnTime*NuclearCraft.metalFurnaceCookSpeed)/8000);

        if (gui.isMouseOver(fuel.stack, recipe) && burnTime < 1) {
            burnTime = 1F / burnTime;
            String s_time = Float.toString(burnTime);
            if (burnTime == Math.round(burnTime))
                s_time = Integer.toString((int) burnTime);

            currenttip.add(translate("recipe.fuel.required", s_time));
        } else if ((gui.isMouseOver(crecipe.getResult(), recipe) || gui.isMouseOver(crecipe.getIngredient(), recipe)) && burnTime > 1) {
            String s_time = Float.toString(burnTime);
            if (burnTime == Math.round(burnTime))
                s_time = Integer.toString((int) burnTime);

            currenttip.add(translate("recipe.fuel." + (gui.isMouseOver(crecipe.getResult(), recipe) ? "produced" : "processed"), s_time));
        }

        return currenttip;
    }
}
