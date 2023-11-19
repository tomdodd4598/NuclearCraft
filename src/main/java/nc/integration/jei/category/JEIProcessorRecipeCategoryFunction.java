package nc.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.integration.jei.wrapper.JEIProcessorRecipeWrapper;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface JEIProcessorRecipeCategoryFunction<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, INFO, WRAPPER>> {
	
	JEIProcessorRecipeCategory<TILE, INFO, WRAPPER> apply(IGuiHelper guiHelper, JEIProcessorCategoryInfo<TILE, INFO, WRAPPER> categoryInfo);
}
