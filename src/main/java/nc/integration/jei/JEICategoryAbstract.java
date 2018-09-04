package nc.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;
import nc.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class JEICategoryAbstract extends JEICategory {
	
	private final IDrawable background;
	protected String recipeTitle;
	protected final int backPosX, backPosY;
	
	public JEICategoryAbstract(IGuiHelper guiHelper, IJEIHandler handler, String title, int backX, int backY, int backWidth, int backHeight) {
		this(guiHelper, handler, title, "", backX, backY, backWidth, backHeight);
	}
	
	public JEICategoryAbstract(IGuiHelper guiHelper, IJEIHandler handler, String title, String guiExtra, int backX, int backY, int backWidth, int backHeight) {
		super(handler);
		ResourceLocation location = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + handler.getTextureName() + guiExtra + ".png");
		background = guiHelper.createDrawable(location, backX, backY, backWidth, backHeight);
		recipeTitle = Lang.localise("tile." + Global.MOD_ID + "." + title + ".name");
		backPosX = backX + 1;
		backPosY = backY + 1;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public abstract void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients);
	
	@Override
	public String getTitle() {
		return recipeTitle;
	}
}
