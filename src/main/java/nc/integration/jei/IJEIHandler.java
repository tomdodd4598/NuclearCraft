package nc.integration.jei;

import java.util.ArrayList;

import mezz.jei.api.IGuiHelper;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.item.ItemStack;

public interface IJEIHandler {
	
	public JEICategory getCategory(IGuiHelper guiHelper);
	
	public String getTextureName();
	
	public String getTitle();
	
	public Class getRecipeClass();
	
	public String getUUID();
	
	public boolean getEnabled();
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public ArrayList<JEIProcessorRecipe> getJEIRecipes();
	
	public ItemStack getCrafterItemStack();
}
