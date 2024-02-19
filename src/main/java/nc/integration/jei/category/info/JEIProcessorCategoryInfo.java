package nc.integration.jei.category.info;

import java.util.*;

import mezz.jei.api.IGuiHelper;
import nc.handler.TileInfoHandler;
import nc.integration.jei.category.JEIProcessorRecipeCategory;
import nc.integration.jei.wrapper.*;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class JEIProcessorCategoryInfo<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, INFO, WRAPPER>> extends JEICategoryInfo<WRAPPER, JEIProcessorRecipeCategory<TILE, PACKET, INFO, WRAPPER>, JEIProcessorCategoryInfo<TILE, PACKET, INFO, WRAPPER>> {
	
	public final INFO containerInfo;
	
	public final JEIProcessorRecipeWrapperFunction<TILE, PACKET, INFO, WRAPPER> jeiRecipeWrapperFunction;
	
	public JEIProcessorCategoryInfo(String name, Class<WRAPPER> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, PACKET, INFO, WRAPPER> jeiRecipeWrapperFunction, List<Object> jeiCrafters) {
		super(JEIProcessorRecipeCategory::new, jeiRecipeClass, null, jeiCrafters);
		this.containerInfo = TileInfoHandler.<TILE, PACKET, INFO>getProcessorContainerInfo(name);
		this.jeiRecipeWrapperFunction = jeiRecipeWrapperFunction;
	}
	
	@Override
	public String getModId() {
		return containerInfo.modId;
	}
	
	@Override
	public String getName() {
		return containerInfo.name;
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
	public int getJEIClickAreaX() {
		return containerInfo.jeiClickAreaX;
	}
	
	@Override
	public int getJEIClickAreaY() {
		return containerInfo.jeiClickAreaY;
	}
	
	@Override
	public int getJEIClickAreaW() {
		return containerInfo.jeiClickAreaW;
	}
	
	@Override
	public int getJEIClickAreaH() {
		return containerInfo.jeiClickAreaH;
	}
	
	@Override
	public WRAPPER getJEIRecipe(IGuiHelper guiHelper, BasicRecipe recipe) {
		return jeiRecipeWrapperFunction.apply(getName(), guiHelper, recipe);
	}
	
	@Override
	public List<Class<? extends Container>> getContainerClasses() {
		return Arrays.asList(containerInfo.containerClass);
	}
	
	@Override
	public List<Class<? extends GuiContainer>> getGuiClasses() {
		return Arrays.asList(containerInfo.guiClass);
	}
}
