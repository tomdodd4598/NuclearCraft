package nc.integration.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.item.ItemStack;

public interface IJEIHandler {
	
	public JEICategoryAbstract getCategory(IGuiHelper guiHelper);
	
	public ProcessorRecipeHandler getRecipeHandler();
	
	public Class<? extends JEIRecipeWrapperAbstract> getJEIRecipeWrapper();
	
	public ArrayList<JEIRecipeWrapperAbstract> getJEIRecipes(IGuiHelper guiHelper);
	
	public String getUUID();
	
	public boolean getEnabled();
	
	public List<ItemStack> getCrafters();
	
	public String getTextureName();
}
