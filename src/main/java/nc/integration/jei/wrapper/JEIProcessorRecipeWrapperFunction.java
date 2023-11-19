package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.recipe.BasicRecipe;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface JEIProcessorRecipeWrapperFunction<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, INFO, WRAPPER>> {
	
	WRAPPER apply(IGuiHelper guiHelper, JEIProcessorCategoryInfo<TILE, INFO, WRAPPER> categoryInfo, BasicRecipe recipe);
}
