package nc.integration.jei;

import java.util.ArrayList;

import mezz.jei.api.IGuiHelper;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.item.ItemStack;

public interface IJEIHandler {
	
	public JEICategory getCategory(IGuiHelper guiHelper);
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public Class<? extends JEIRecipeWrapperAbstract> getJEIRecipeWrapper();
	
	public ArrayList<JEIRecipeWrapperAbstract> getJEIRecipes(IGuiHelper guiHelper);
	
	public String getUUID();
	
	public boolean getEnabled();
	
	public ItemStack getCrafterItemStack();
	
	public String getTextureName();
}
