package nc.crafting.nei;

import static codechicken.nei.NEIClientUtils.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.NuclearCraft;
import nc.crafting.machine.CrusherRecipesOld;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class CrusherFuelRecipeHandler extends CrusherRecipeHandler
{
    public class CachedFuelRecipe extends CachedRecipe
    {
        public CrusherFuelPair crushfuel;

        public CachedFuelRecipe(CrusherFuelPair fuel) {
            this.crushfuel = fuel;
        }

        public PositionedStack getIngredient() {
            return mfurnace.get(cycleticks / 24 % mfurnace.size()).ingred;
        }

        public PositionedStack getResult() {
            return mfurnace.get(cycleticks / 24 % mfurnace.size()).result;
        }

        public PositionedStack getOtherStack() {
            return crushfuel.stack;
        }
    }

    private ArrayList<CrushingPair> mfurnace = new ArrayList<CrusherRecipeHandler.CrushingPair>();

    public CrusherFuelRecipeHandler() {
        super();
        loadAllCrushing();
    }

    public String getRecipeName() {
        return "Crusher Fuel";
    }

    private void loadAllCrushing() {
        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) CrusherRecipesOld.smelting().getSmeltingList();

        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            mfurnace.add(new CrushingPair(recipe.getKey(), recipe.getValue()));
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crushfuel") && getClass() == CrusherFuelRecipeHandler.class)
            for (CrusherFuelPair fuel : acrushfuels)
                arecipes.add(new CachedFuelRecipe(fuel));
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for (CrusherFuelPair crusherfuel : acrushfuels)
            if (crusherfuel.stack.contains(ingredient))
                arecipes.add(new CachedFuelRecipe(crusherfuel));
    }

    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedFuelRecipe crecipe = (CachedFuelRecipe) arecipes.get(recipe);
        CrusherFuelPair fuel = crecipe.crushfuel;
        float burnTime = (float) ((double) (fuel.burnTime*NuclearCraft.crusherCrushSpeed)/16000);

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
