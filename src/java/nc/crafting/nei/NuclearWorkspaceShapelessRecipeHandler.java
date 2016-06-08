package nc.crafting.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.crafting.workspace.NuclearWorkspaceCraftingManager;
import nc.crafting.workspace.NuclearWorkspaceShapelessOreRecipe;
import nc.crafting.workspace.NuclearWorkspaceShapelessRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;

public class NuclearWorkspaceShapelessRecipeHandler extends NuclearWorkspaceRecipeHandler
{
    	public int[][] stackorder = new int[][]{
                {0, 0},
                {1, 0},
                {0, 1},
                {1, 1},
                {0, 2},
                {1, 2},
                {2, 0},
                {2, 1},
                {2, 2}};

        public class CachedShapelessRecipe extends CachedRecipe
        {
            public CachedShapelessRecipe() {
                ingredients = new ArrayList<PositionedStack>();
            }

            public CachedShapelessRecipe(ItemStack output) {
                this();
                setResult(output);
            }

            public CachedShapelessRecipe(Object[] input, ItemStack output) {
                this(Arrays.asList(input), output);
            }

            public CachedShapelessRecipe(List<?> input, ItemStack output) {
                this(output);
                setIngredients(input);
            }

            public void setIngredients(List<?> items) {
                ingredients.clear();
                for (int ingred = 0; ingred < items.size(); ingred++) {
                    PositionedStack stack = new PositionedStack(items.get(ingred), 25 + stackorder[ingred][0] * 18, 6 + stackorder[ingred][1] * 18);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                }
            }

            public void setResult(ItemStack output) {
                result = new PositionedStack(output, 119, 24);
            }

            @Override
            public List<PositionedStack> getIngredients() {
                return getCycledIngredients(cycleticks / 20, ingredients);
            }

            @Override
            public PositionedStack getResult() {
                return result;
            }

            public ArrayList<PositionedStack> ingredients;
            public PositionedStack result;
        }

        public void loadCraftingRecipes(String outputId, Object... results) {
            if (outputId.equals("nwcrafting") && getClass() == NuclearWorkspaceShapelessRecipeHandler.class) {
                @SuppressWarnings("unchecked")
				List<IRecipe> allrecipes = NuclearWorkspaceCraftingManager.getInstance().getRecipeList();
                for (IRecipe irecipe : allrecipes) {
                    CachedShapelessRecipe recipe = null;
                    if (irecipe instanceof NuclearWorkspaceShapelessRecipes)
                        recipe = shapelessRecipe((NuclearWorkspaceShapelessRecipes) irecipe);
                    else if (irecipe instanceof NuclearWorkspaceShapelessOreRecipe)
                        recipe = forgeShapelessRecipe((NuclearWorkspaceShapelessOreRecipe) irecipe);

                    if (recipe == null)
                        continue;

                    arecipes.add(recipe);
                }
            } else {
                super.loadCraftingRecipes(outputId, results);
            }
        }

        @Override
        public void loadCraftingRecipes(ItemStack result) {
            @SuppressWarnings("unchecked")
			List<IRecipe> allrecipes = NuclearWorkspaceCraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                    CachedShapelessRecipe recipe = null;
                    if (irecipe instanceof NuclearWorkspaceShapelessRecipes)
                        recipe = shapelessRecipe((NuclearWorkspaceShapelessRecipes) irecipe);
                    else if (irecipe instanceof NuclearWorkspaceShapelessOreRecipe)
                        recipe = forgeShapelessRecipe((NuclearWorkspaceShapelessOreRecipe) irecipe);

                    if (recipe == null)
                        continue;

                    arecipes.add(recipe);
                }
            }
        }

        @Override
        public void loadUsageRecipes(ItemStack ingredient) {
            @SuppressWarnings("unchecked")
			List<IRecipe> allrecipes = NuclearWorkspaceCraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof NuclearWorkspaceShapelessRecipes)
                    recipe = shapelessRecipe((NuclearWorkspaceShapelessRecipes) irecipe);
                else if (irecipe instanceof NuclearWorkspaceShapelessOreRecipe)
                    recipe = forgeShapelessRecipe((NuclearWorkspaceShapelessOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                if (recipe.contains(recipe.ingredients, ingredient)) {
                    recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                    arecipes.add(recipe);
                }
            }
        }

        private CachedShapelessRecipe shapelessRecipe(NuclearWorkspaceShapelessRecipes recipe) {
            if(recipe.recipeItems == null) //because some mod subclasses actually do this
                return null;

            try {
                return new CachedShapelessRecipe(recipe.recipeItems, recipe.getRecipeOutput());
            } catch (ClassCastException e) { //because some mod subclasses put whatever they want in recipeItems
                return null;
            }
        }

        public CachedShapelessRecipe forgeShapelessRecipe(NuclearWorkspaceShapelessOreRecipe recipe) {
            ArrayList<Object> items = recipe.getInput();

            for (Object item : items)
                if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                    return null;

            try {
                return new CachedShapelessRecipe(items, recipe.getRecipeOutput());
            } catch (Exception e) {
                NEIClientConfig.logger.error("Error loading recipe: ", e);
                return null;
            }
        }
    }