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

public class NuclearFurnaceFuelRecipeHandler extends NuclearFurnaceRecipeHandler
{
    public class CachednuclearfuelRecipe extends CachedRecipe
    {
        public nuclearfuelPair nuclearfuel;

        public CachednuclearfuelRecipe(nuclearfuelPair nuclearfuel) {
            this.nuclearfuel = nuclearfuel;
        }

        public PositionedStack getIngredient() {
            return mnuclearfurnace.get(cycleticks / 24 % mnuclearfurnace.size()).ingred;
        }

        public PositionedStack getResult() {
            return mnuclearfurnace.get(cycleticks / 24 % mnuclearfurnace.size()).result;
        }

        public PositionedStack getOtherStack() {
            return nuclearfuel.stack;
        }
    }

    private ArrayList<NuclearSmeltingPair> mnuclearfurnace = new ArrayList<NuclearFurnaceRecipeHandler.NuclearSmeltingPair>();

    public NuclearFurnaceFuelRecipeHandler() {
        super();
        loadAllSmelting();
    }

    public String getRecipeName() {
        return "Nuclear Furnace Fuel";
    }

    private void loadAllSmelting() {
        @SuppressWarnings("unchecked")
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();

        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            mnuclearfurnace.add(new NuclearSmeltingPair(recipe.getKey(), recipe.getValue()));
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("nuclearfuel") && getClass() == NuclearFurnaceFuelRecipeHandler.class)
            for (nuclearfuelPair nuclearfuel : anuclearnuclearfuels)
                arecipes.add(new CachednuclearfuelRecipe(nuclearfuel));
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for (nuclearfuelPair nuclearfuel : anuclearnuclearfuels)
            if (nuclearfuel.stack.contains(ingredient))
                arecipes.add(new CachednuclearfuelRecipe(nuclearfuel));
    }

    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachednuclearfuelRecipe crecipe = (CachednuclearfuelRecipe) arecipes.get(recipe);
        nuclearfuelPair nuclearfuel = crecipe.nuclearfuel;
        float burnTime = (float) ((double) (nuclearfuel.burnTime*NuclearCraft.nuclearFurnaceCookSpeed)/300);

        if (gui.isMouseOver(nuclearfuel.stack, recipe) && burnTime < 1) {
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
