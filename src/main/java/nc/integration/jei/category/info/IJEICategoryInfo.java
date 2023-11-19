package nc.integration.jei.category.info;

import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.integration.jei.category.JEIRecipeCategory;
import nc.integration.jei.wrapper.JEIRecipeWrapper;
import nc.recipe.*;

public interface IJEICategoryInfo<WRAPPER extends JEIRecipeWrapper> {
	
	public String getModId();
	
	public String getName();
	
	public boolean isJEICategoryEnabled();
	
	public int getItemInputSize();
	
	public int getFluidInputSize();
	
	public int getItemOutputSize();
	
	public int getFluidOutputSize();
	
	public int[] getItemInputSlots();
	
	public int[] getItemOutputSlots();
	
	public int[] getFluidInputTanks();
	
	public int[] getFluidOutputTanks();
	
	public List<int[]> getItemInputGuiXYWH();
	
	public List<int[]> getFluidInputGuiXYWH();
	
	public List<int[]> getItemOutputGuiXYWH();
	
	public List<int[]> getFluidOutputGuiXYWH();
	
	public List<int[]> getItemInputStackXY();
	
	public List<int[]> getItemOutputStackXY();
	
	public BasicRecipeHandler getRecipeHandler();
	
	public String getJEICategoryUid();
	
	public String getJEITitle();
	
	public String getJEITexture();
	
	public int getJEIBackgroundX();
	
	public int getJEIBackgroundY();
	
	public int getJEIBackgroundW();
	
	public int getJEIBackgroundH();
	
	public JEIRecipeCategory<WRAPPER> getJEICategory(IGuiHelper guiHelper);
	
	public default void registerJEICategory(IModRegistry registry, IJeiHelpers jeiHelpers, IGuiHelper guiHelper, IRecipeTransferRegistry transferRegistry) {
		if (isJEICategoryEnabled()) {
			registry.addRecipes(getJEIRecipes(guiHelper));
			
			JEIRecipeCategory<WRAPPER> category = getJEICategory(guiHelper);
			registry.addRecipeCategories(category);
			registry.addRecipeHandlers(category);
			
			addJEIRecipeCatalysts(registry);
			
			addRecipeTransferHandlers(transferRegistry);
			addRecipeClickAreas(registry);
		}
	}
	
	public Class<WRAPPER> getJEIRecipeClass();
	
	public WRAPPER getJEIRecipe(IGuiHelper guiHelper, BasicRecipe recipe);
	
	public default List<WRAPPER> getJEIRecipes(IGuiHelper guiHelper) {
		return getRecipeHandler().getRecipeList().stream().map(x -> getJEIRecipe(guiHelper, x)).collect(Collectors.toList());
	}
	
	public List<Object> getJEICrafters();
	
	public default void addJEIRecipeCatalysts(IModRegistry registry) {
		for (Object crafter : getJEICrafters()) {
			if (crafter != null) {
				registry.addRecipeCatalyst(crafter, getJEICategoryUid());
			}
		}
	}
	
	public void addRecipeTransferHandlers(IRecipeTransferRegistry transferRegistry);
	
	public void addRecipeClickAreas(IModRegistry registry);
}
