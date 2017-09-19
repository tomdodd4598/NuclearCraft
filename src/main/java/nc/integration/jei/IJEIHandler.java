package nc.integration.jei;

import java.util.ArrayList;

import mezz.jei.api.IGuiHelper;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.item.ItemStack;

public interface IJEIHandler {
	
	public JEICategory getCategory(IGuiHelper guiHelper);
	
	public String getTextureName();
	
	public String getTitle();
	
	public Class getRecipeClass();
	
	public String getUUID();
	
	public BaseRecipeHandler getRecipeHandler();
	
	public ArrayList<JEIRecipe> getJEIRecipes();
	
	public ItemStack getCrafterItemStack();
}
