package nc.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;
import nc.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class BaseCategory extends JEICategory {
	
	private final IDrawable background;
	protected final IDrawableAnimated arrow;
	protected final String recipeTitle;
	protected final int backPosX, backPosY;
	protected final int arrowDrawPosX, arrowDrawPosY;
	
	public BaseCategory(IGuiHelper guiHelper, IJEIHandler handler, String title, int time, int backX, int backY, int backWidth, int backHeight, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowPosX, int arrowPosY) {
		this(guiHelper, handler, title, "", time, backX, backY, backWidth, backHeight, arrowX, arrowY, arrowWidth, arrowHeight, arrowPosX, arrowPosY);
	}
	
	public BaseCategory(IGuiHelper guiHelper, IJEIHandler handler, String title, String guiExtra, int time, int backX, int backY, int backWidth, int backHeight, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowPosX, int arrowPosY) {
		super(handler);
		ResourceLocation location = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + handler.getTextureName() + guiExtra + ".png");
		background = guiHelper.createDrawable(location, backX, backY, backWidth, backHeight);
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, arrowX, arrowY, arrowWidth, arrowHeight);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 1 + time/4, IDrawableAnimated.StartDirection.LEFT, false);
		recipeTitle = title;
		backPosX = backX + 1;
		backPosY = backY + 1;
		arrowDrawPosX = arrowPosX - backX;
		arrowDrawPosY = arrowPosY - backY;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		arrow.draw(minecraft, arrowDrawPosX, arrowDrawPosY);
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public abstract void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients);
	
	@Override
	public String getTitle() {
		return Lang.localise("tile." + recipeTitle + ".name");
	}
}
