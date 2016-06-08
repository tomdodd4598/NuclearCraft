package nc.crafting.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import nc.crafting.workspace.NuclearWorkspaceCraftingManager;
import nc.crafting.workspace.NuclearWorkspaceShapedOreRecipe;
import nc.crafting.workspace.NuclearWorkspaceShapedRecipes;
import nc.gui.crafting.GuiNuclearWorkspace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import org.lwjgl.opengl.GL11;

import codechicken.core.ReflectionManager;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NuclearWorkspaceRecipeHandler extends TemplateRecipeHandler
{
    public class CachedShapedRecipe extends CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedShapedRecipe(int width, int height, Object[] items, ItemStack out) {
            result = new PositionedStack(out, 135, 42);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients(width, height, items);
        }

        public CachedShapedRecipe(NuclearWorkspaceShapedRecipes recipe) {
            this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
        }

        /**
         * @param width
         * @param height
         * @param items  an ItemStack[] or ItemStack[][]
         */
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null)
                        continue;

                    PositionedStack stack = new PositionedStack(items[y * width + x], 3 + x * 18, 6 + y * 18, false);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                }
            }
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        public PositionedStack getResult() {
            return result;
        }

        public void computeVisuals() {
            for (PositionedStack p : ingredients)
                p.generatePermutations();
        }
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(97, 29, 29, 41), "nwcrafting", new Object[0]));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiNuclearWorkspace.class;
    }

    @Override
    public String getRecipeName() {
        return "Heavy Duty Workspace";
    }

	@SuppressWarnings("unchecked")
	@Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("nwcrafting") && getClass() == NuclearWorkspaceRecipeHandler.class) {
            for (IRecipe irecipe : (List<IRecipe>) NuclearWorkspaceCraftingManager.getInstance().getRecipeList()) {
                CachedShapedRecipe recipe = null;
                if (irecipe instanceof NuclearWorkspaceShapedRecipes)
                    recipe = new CachedShapedRecipe((NuclearWorkspaceShapedRecipes) irecipe);
                else if (irecipe instanceof NuclearWorkspaceShapedOreRecipe)
                    recipe = forgeShapedRecipe((NuclearWorkspaceShapedOreRecipe) irecipe);
                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : (List<IRecipe>) NuclearWorkspaceCraftingManager.getInstance().getRecipeList()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedShapedRecipe recipe = null;
                if (irecipe instanceof NuclearWorkspaceShapedRecipes)
                    recipe = new CachedShapedRecipe((NuclearWorkspaceShapedRecipes) irecipe);
                else if (irecipe instanceof NuclearWorkspaceShapedOreRecipe)
                    recipe = forgeShapedRecipe((NuclearWorkspaceShapedOreRecipe) irecipe);
                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : (List<IRecipe>) NuclearWorkspaceCraftingManager.getInstance().getRecipeList()) {
            CachedShapedRecipe recipe = null;
            if (irecipe instanceof NuclearWorkspaceShapedRecipes)
                recipe = new CachedShapedRecipe((NuclearWorkspaceShapedRecipes) irecipe);
            else if (irecipe instanceof NuclearWorkspaceShapedOreRecipe)
                recipe = forgeShapedRecipe((NuclearWorkspaceShapedOreRecipe) irecipe);
            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }
    
    public CachedShapedRecipe forgeShapedRecipe(NuclearWorkspaceShapedOreRecipe recipe) {
        try {
            int width = ReflectionManager.getField(NuclearWorkspaceShapedOreRecipe.class, Integer.class, recipe, 4);
            int height = ReflectionManager.getField(NuclearWorkspaceShapedOreRecipe.class, Integer.class, recipe, 5);

            Object[] items = recipe.getInput();
            for (Object item : items)
                if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                    return null;

            return new CachedShapedRecipe(width, height, items, recipe.getRecipeOutput());
        } catch (Exception e) {
            NEIClientConfig.logger.error("Error loading recipe: ", e);
            return null;
        }
    }

    @Override
    public String getGuiTexture() {
        return "nc:textures/gui/nuclearWorkspaceNEI.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "nwcrafting";
    }

    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return super.hasOverlay(gui, container, recipe) || RecipeInfo.hasDefaultOverlay(gui, "nwcrafting");
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
        if (renderer != null)
            return renderer;

        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "nwcrafting");
        if (positioner == null)
            return null;
        return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
    }
    
    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
        if (handler != null)
            return handler;

        return RecipeInfo.getOverlayHandler(gui, "nwcrafting");
    }
    
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, -1, -4, 166, 98);
    }
}
