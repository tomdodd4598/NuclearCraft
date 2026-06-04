package nc.integration.jei.category.info;

import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import nc.handler.TileInfoHandler;
import nc.integration.jei.category.JEIProcessorRecipeCategory;
import nc.integration.jei.wrapper.*;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class JEIProcessorCategoryInfo<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, INFO, WRAPPER>> extends JEICategoryInfo<WRAPPER, JEIProcessorRecipeCategory<TILE, PACKET, INFO, WRAPPER>, JEIProcessorCategoryInfo<TILE, PACKET, INFO, WRAPPER>> {
	
	public final INFO containerInfo;
	
	public final JEIProcessorRecipeWrapperFunction<TILE, PACKET, INFO, WRAPPER> jeiRecipeWrapperFunction;
	
	public JEIProcessorCategoryInfo(String name, Class<WRAPPER> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, PACKET, INFO, WRAPPER> jeiRecipeWrapperFunction, List<Object> jeiCrafters) {
		this(TileInfoHandler.<TILE, PACKET, INFO>getProcessorContainerInfo(name), jeiRecipeClass, jeiRecipeWrapperFunction, jeiCrafters);
	}
	
	private JEIProcessorCategoryInfo(INFO containerInfo, Class<WRAPPER> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, PACKET, INFO, WRAPPER> jeiRecipeWrapperFunction, List<Object> jeiCrafters) {
		super(containerInfo.modId, containerInfo.recipeHandlerName, JEIProcessorRecipeCategory::new, jeiRecipeClass, null, jeiCrafters, Lists.newArrayList(containerInfo.getJEIContainerConnection()));
		
		this.containerInfo = containerInfo;
		this.jeiRecipeWrapperFunction = jeiRecipeWrapperFunction;
	}
	
	@Override
	public boolean isJEICategoryEnabled() {
		return containerInfo.jeiCategoryEnabled;
	}
	
	@Override
	public int getItemInputSize() {
		return containerInfo.itemInputSize;
	}
	
	@Override
	public int getFluidInputSize() {
		return containerInfo.fluidInputSize;
	}
	
	@Override
	public int getItemOutputSize() {
		return containerInfo.itemOutputSize;
	}
	
	@Override
	public int getFluidOutputSize() {
		return containerInfo.fluidOutputSize;
	}
	
	@Override
	public int[] getItemInputSlots() {
		return containerInfo.itemInputSlots;
	}
	
	@Override
	public int[] getItemOutputSlots() {
		return containerInfo.itemOutputSlots;
	}
	
	@Override
	public int[] getFluidInputTanks() {
		return containerInfo.fluidInputTanks;
	}
	
	@Override
	public int[] getFluidOutputTanks() {
		return containerInfo.fluidOutputTanks;
	}
	
	@Override
	public List<int[]> getItemInputGuiXYWH() {
		return containerInfo.itemInputGuiXYWH;
	}
	
	@Override
	public List<int[]> getFluidInputGuiXYWH() {
		return containerInfo.fluidInputGuiXYWH;
	}
	
	@Override
	public List<int[]> getItemOutputGuiXYWH() {
		return containerInfo.itemOutputGuiXYWH;
	}
	
	@Override
	public List<int[]> getFluidOutputGuiXYWH() {
		return containerInfo.fluidOutputGuiXYWH;
	}
	
	@Override
	public List<int[]> getItemInputStackXY() {
		return containerInfo.itemInputStackXY;
	}
	
	@Override
	public List<int[]> getItemOutputStackXY() {
		return containerInfo.itemOutputStackXY;
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return containerInfo.getRecipeHandler();
	}
	
	@Override
	public String getJEICategoryUid() {
		return containerInfo.jeiCategoryUid;
	}
	
	@Override
	public String getJEITitle() {
		return containerInfo.jeiTitle;
	}
	
	@Override
	public String getJEITexture() {
		return containerInfo.jeiTexture;
	}
	
	@Override
	public int getJEIBackgroundX() {
		return containerInfo.jeiBackgroundX;
	}
	
	@Override
	public int getJEIBackgroundY() {
		return containerInfo.jeiBackgroundY;
	}
	
	@Override
	public int getJEIBackgroundW() {
		return containerInfo.jeiBackgroundW;
	}
	
	@Override
	public int getJEIBackgroundH() {
		return containerInfo.jeiBackgroundH;
	}
	
	@Override
	public int getJEITooltipX() {
		return containerInfo.jeiTooltipX;
	}
	
	@Override
	public int getJEITooltipY() {
		return containerInfo.jeiTooltipY;
	}
	
	@Override
	public int getJEITooltipW() {
		return containerInfo.jeiTooltipW;
	}
	
	@Override
	public int getJEITooltipH() {
		return containerInfo.jeiTooltipH;
	}
	
	@Override
	public WRAPPER getJEIRecipe(IGuiHelper guiHelper, BasicRecipe recipe) {
		return jeiRecipeWrapperFunction.apply(getName(), guiHelper, recipe);
	}
}
