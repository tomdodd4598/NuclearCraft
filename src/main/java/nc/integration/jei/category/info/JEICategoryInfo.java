package nc.integration.jei.category.info;

import java.util.List;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.integration.jei.category.*;
import nc.integration.jei.wrapper.*;
import nc.recipe.*;
import nc.util.StreamHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class JEICategoryInfo<WRAPPER extends JEIRecipeWrapper, CATEGORY extends JEIRecipeCategory<WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEICategoryInfo<WRAPPER, CATEGORY, CATEGORY_INFO>> {
	
	public final JEIRecipeCategoryFunction<WRAPPER, CATEGORY, CATEGORY_INFO> jeiCategoryFunction;
	
	public final Class<WRAPPER> jeiRecipeClass;
	public final JEIRecipeWrapperFunction<WRAPPER, CATEGORY, CATEGORY_INFO> jeiRecipeWrapperFunction;
	
	public final List<Object> jeiCrafters;
	
	protected JEICategoryInfo(JEIRecipeCategoryFunction<WRAPPER, CATEGORY, CATEGORY_INFO> jeiCategoryFunction, Class<WRAPPER> jeiRecipeClass, JEIRecipeWrapperFunction<WRAPPER, CATEGORY, CATEGORY_INFO> jeiRecipeWrapperFunction, List<Object> jeiCrafters) {
		this.jeiCategoryFunction = jeiCategoryFunction;
		
		this.jeiRecipeClass = jeiRecipeClass;
		this.jeiRecipeWrapperFunction = jeiRecipeWrapperFunction;
		
		this.jeiCrafters = jeiCrafters;
	}
	
	public abstract String getModId();
	
	public abstract String getName();
	
	public abstract boolean isJEICategoryEnabled();
	
	public abstract int getItemInputSize();
	
	public abstract int getFluidInputSize();
	
	public abstract int getItemOutputSize();
	
	public abstract int getFluidOutputSize();
	
	public int getInventorySize() {
		return getItemInputSize() + getItemOutputSize();
	}
	
	public int getCombinedInventorySize() {
		return 36 + getInventorySize();
	}
	
	public abstract int[] getItemInputSlots();
	
	public abstract int[] getItemOutputSlots();
	
	public abstract int[] getFluidInputTanks();
	
	public abstract int[] getFluidOutputTanks();
	
	public abstract List<int[]> getItemInputGuiXYWH();
	
	public abstract List<int[]> getFluidInputGuiXYWH();
	
	public abstract List<int[]> getItemOutputGuiXYWH();
	
	public abstract List<int[]> getFluidOutputGuiXYWH();
	
	public abstract List<int[]> getItemInputStackXY();
	
	public abstract List<int[]> getItemOutputStackXY();
	
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.getHandler(getName());
	}
	
	public abstract String getJEICategoryUid();
	
	public abstract String getJEITitle();
	
	public abstract String getJEITexture();
	
	public abstract int getJEIBackgroundX();
	
	public abstract int getJEIBackgroundY();
	
	public abstract int getJEIBackgroundW();
	
	public abstract int getJEIBackgroundH();
	
	public abstract int getJEITooltipX();
	
	public abstract int getJEITooltipY();
	
	public abstract int getJEITooltipW();
	
	public abstract int getJEITooltipH();
	
	public abstract int getJEIClickAreaX();
	
	public abstract int getJEIClickAreaY();
	
	public abstract int getJEIClickAreaW();
	
	public abstract int getJEIClickAreaH();
	
	@SuppressWarnings("unchecked")
	public CATEGORY getJEICategory(IGuiHelper guiHelper) {
		return jeiCategoryFunction.apply(guiHelper, (CATEGORY_INFO) this);
	}
	
	public void registerJEICategory(IModRegistry registry, IJeiHelpers jeiHelpers, IGuiHelper guiHelper, IRecipeTransferRegistry transferRegistry) {
		if (isJEICategoryEnabled()) {
			registry.addRecipes(getJEIRecipes(guiHelper));
			
			CATEGORY category = getJEICategory(guiHelper);
			registry.addRecipeCategories(category);
			registry.addRecipeHandlers(category);
			
			addJEIRecipeCatalysts(registry);
			
			addRecipeTransferHandlers(transferRegistry);
			addRecipeClickAreas(registry);
		}
	}
	
	@SuppressWarnings("unchecked")
	public WRAPPER getJEIRecipe(IGuiHelper guiHelper, BasicRecipe recipe) {
		return jeiRecipeWrapperFunction.apply(guiHelper, (CATEGORY_INFO) this, recipe);
	}
	
	public List<WRAPPER> getJEIRecipes(IGuiHelper guiHelper) {
		return StreamHelper.map(getRecipeHandler().getRecipeList(), x -> getJEIRecipe(guiHelper, x));
	}
	
	public void addJEIRecipeCatalysts(IModRegistry registry) {
		for (Object crafter : jeiCrafters) {
			if (crafter != null) {
				registry.addRecipeCatalyst(crafter, getJEICategoryUid());
			}
		}
	}
	
	public abstract List<Class<? extends Container>> getContainerClasses();
	
	public void addRecipeTransferHandlers(IRecipeTransferRegistry transferRegistry) {
		for (Class<? extends Container> clazz : getContainerClasses()) {
			transferRegistry.addRecipeTransferHandler(clazz, getJEICategoryUid(), 0, getItemInputSize(), getInventorySize(), 36);
		}
	}
	
	public abstract List<Class<? extends GuiContainer>> getGuiClasses();
	
	public void addRecipeClickAreas(IModRegistry registry) {
		for (Class<? extends GuiContainer> clazz : getGuiClasses()) {
			registry.addRecipeClickArea(clazz, getJEIClickAreaX(), getJEIClickAreaY(), getJEIClickAreaW(), getJEIClickAreaH(), getJEICategoryUid());
		}
	}
}
