package nc.integration.jei.category;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.*;
import nc.integration.jei.JEIHelper.*;
import nc.integration.jei.category.info.JEICategoryInfo;
import nc.integration.jei.wrapper.JEIRecipeWrapper;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public abstract class JEIRecipeCategory<WRAPPER extends JEIRecipeWrapper, CATEGORY extends JEIRecipeCategory<WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEICategoryInfo<WRAPPER, CATEGORY, CATEGORY_INFO>> extends BlankRecipeCategory<WRAPPER> implements IRecipeHandler<WRAPPER> {
	
	protected final CATEGORY_INFO categoryInfo;
	
	protected final String localizedTitle;
	protected final IDrawable background;
	
	public JEIRecipeCategory(IGuiHelper guiHelper, CATEGORY_INFO categoryInfo) {
		this.categoryInfo = categoryInfo;
		localizedTitle = Lang.localize(categoryInfo.getJEITitle());
		background = guiHelper.createDrawable(new ResourceLocation(categoryInfo.getJEITexture()), categoryInfo.getJEIBackgroundX(), categoryInfo.getJEIBackgroundY(), categoryInfo.getJEIBackgroundW(), categoryInfo.getJEIBackgroundH());
	}
	
	// BlankRecipeCategory
	
	@Override
	public String getUid() {
		return categoryInfo.getJEICategoryUid();
	}
	
	@Override
	public String getTitle() {
		return localizedTitle;
	}
	
	@Override
	public String getModName() {
		return categoryInfo.getModId();
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, WRAPPER recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup items = recipeLayout.getItemStacks();
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		
		BasicRecipeHandler recipeHandler = recipeWrapper.recipeHandler;
		BasicRecipe recipe = recipeWrapper.recipe;
		
		List<IItemIngredient> itemProducts = recipe.getItemProducts();
		List<IFluidIngredient> fluidProducts = recipe.getFluidProducts();
		
		items.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			int outputIndex = slotIndex - recipeHandler.getItemInputSize();
			if (outputIndex >= 0 && outputIndex <= recipeHandler.getItemOutputSize()) {
				IItemIngredient product = itemProducts.get(outputIndex);
				if (product instanceof IChanceItemIngredient chanceProduct) {
                    tooltip.add(TextFormatting.WHITE + Lang.localize("jei.nuclearcraft.chance_output", chanceProduct.getMinStackSize(), chanceProduct.getMaxStackSize(0), NCMath.decimalPlaces(chanceProduct.getMeanStackSize(), 2)));
				}
			}
		});
		
		fluids.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			int outputIndex = slotIndex - recipeHandler.getFluidInputSize();
			if (outputIndex >= 0 && outputIndex <= recipeHandler.getFluidOutputSize()) {
				IFluidIngredient product = fluidProducts.get(outputIndex);
				if (product instanceof IChanceFluidIngredient chanceProduct) {
                    tooltip.add(TextFormatting.WHITE + Lang.localize("jei.nuclearcraft.chance_output", chanceProduct.getMinStackSize(), chanceProduct.getMaxStackSize(0), NCMath.decimalPlaces(chanceProduct.getMeanStackSize(), 2)));
				}
			}
		});
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		
		int backgroundX = categoryInfo.getJEIBackgroundX();
		int backgroundY = categoryInfo.getJEIBackgroundY();
		
		List<int[]> itemInputStackXY = categoryInfo.getItemInputStackXY();
		int[] itemInputSlots = categoryInfo.getItemInputSlots();
		for (int i = 0; i < categoryInfo.getItemInputSize(); ++i) {
			int[] stackXY = itemInputStackXY.get(i);
			itemMapper.put(IngredientSorption.INPUT, i, itemInputSlots[i], stackXY[0] - backgroundX, stackXY[1] - backgroundY);
		}
		
		List<int[]> fluidInputGuiXYWH = categoryInfo.getFluidInputGuiXYWH();
		int[] fluidInputTanks = categoryInfo.getFluidInputTanks();
		for (int i = 0; i < categoryInfo.getFluidInputSize(); ++i) {
			int[] tankXYWH = fluidInputGuiXYWH.get(i);
			fluidMapper.put(IngredientSorption.INPUT, i, fluidInputTanks[i], tankXYWH[0] - backgroundX, tankXYWH[1] - backgroundY, tankXYWH[2], tankXYWH[3]);
		}
		
		List<int[]> itemOutputStackXY = categoryInfo.getItemOutputStackXY();
		int[] itemOutputSlots = categoryInfo.getItemOutputSlots();
		for (int i = 0; i < categoryInfo.getItemOutputSize(); ++i) {
			int[] stackXY = itemOutputStackXY.get(i);
			itemMapper.put(IngredientSorption.OUTPUT, i, itemOutputSlots[i], stackXY[0] - backgroundX, stackXY[1] - backgroundY);
		}
		
		List<int[]> fluidOutputGuiXYWH = categoryInfo.getFluidOutputGuiXYWH();
		int[] fluidOutputTanks = categoryInfo.getFluidOutputTanks();
		for (int i = 0; i < categoryInfo.getFluidOutputSize(); ++i) {
			int[] tankXYWH = fluidOutputGuiXYWH.get(i);
			fluidMapper.put(IngredientSorption.OUTPUT, i, fluidOutputTanks[i], tankXYWH[0] - backgroundX, tankXYWH[1] - backgroundY, tankXYWH[2], tankXYWH[3]);
		}
		
		itemMapper.apply(items, ingredients);
		fluidMapper.apply(fluids, ingredients);
	}
	
	// IRecipeHandler
	
	@Override
	public Class<WRAPPER> getRecipeClass() {
		return categoryInfo.jeiRecipeClass;
	}
	
	@Override
	public String getRecipeCategoryUid(WRAPPER recipeWrapper) {
		return getUid();
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(WRAPPER recipeWrapper) {
		return recipeWrapper;
	}
	
	@Override
	public boolean isRecipeValid(WRAPPER recipeWrapper) {
		return true;
	}
}
