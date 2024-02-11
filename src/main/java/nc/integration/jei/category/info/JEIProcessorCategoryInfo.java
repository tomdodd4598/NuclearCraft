package nc.integration.jei.category.info;

import java.util.*;

import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.handler.TileInfoHandler;
import nc.integration.jei.category.*;
import nc.integration.jei.wrapper.*;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public abstract class JEIProcessorCategoryInfo<TILE extends TileEntity & IProcessor<TILE, PACKET, CONTAINER_INFO>, PACKET extends ProcessorUpdatePacket, CONTAINER_INFO extends ProcessorContainerInfo<TILE, PACKET, CONTAINER_INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY extends JEIProcessorRecipeCategory<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEIProcessorCategoryInfo<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO>> implements IJEICategoryInfo<WRAPPER> {
	
	public final CONTAINER_INFO containerInfo;
	
	public final JEIProcessorRecipeCategoryFunction<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO> jeiCategoryFunction;
	
	public final Class<WRAPPER> jeiRecipeClass;
	public final JEIProcessorRecipeWrapperFunction<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO> jeiRecipeFunction;
	
	public final List<Object> jeiCrafters;
	
	protected JEIProcessorCategoryInfo(String name, JEIProcessorRecipeCategoryFunction<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO> jeiCategoryFunction, Class<WRAPPER> jeiRecipeClass, JEIProcessorRecipeWrapperFunction<TILE, PACKET, CONTAINER_INFO, WRAPPER, CATEGORY, CATEGORY_INFO> jeiRecipeFunction, List<Object> jeiCrafters) {
		this.containerInfo = TileInfoHandler.<TILE, PACKET, CONTAINER_INFO>getProcessorContainerInfo(name);
		
		this.jeiCategoryFunction = jeiCategoryFunction;
		
		this.jeiRecipeClass = jeiRecipeClass;
		this.jeiRecipeFunction = jeiRecipeFunction;
		
		this.jeiCrafters = jeiCrafters;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public JEIRecipeCategory<WRAPPER> getJEICategory(IGuiHelper guiHelper) {
		return jeiCategoryFunction.apply(guiHelper, (CATEGORY_INFO) this);
	}
	
	@Override
	public Class<WRAPPER> getJEIRecipeClass() {
		return jeiRecipeClass;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public WRAPPER getJEIRecipe(IGuiHelper guiHelper, BasicRecipe recipe) {
		return jeiRecipeFunction.apply(guiHelper, (CATEGORY_INFO) this, recipe);
	}
	
	@Override
	public List<Object> getJEICrafters() {
		return jeiCrafters;
	}
	
	public List<Class<? extends Container>> getContainerClasses() {
		return Arrays.asList(containerInfo.containerClass);
	}
	
	@Override
	public void addRecipeTransferHandlers(IRecipeTransferRegistry transferRegistry) {
		for (Class<? extends Container> clazz : getContainerClasses()) {
			transferRegistry.addRecipeTransferHandler(clazz, getJEICategoryUid(), 0, containerInfo.itemInputSize, containerInfo.getInventorySize(), 36);
		}
	}
	
	public List<Class<? extends GuiContainer>> getGuiClasses() {
		return Arrays.asList(containerInfo.guiClass);
	}
	
	@Override
	public void addRecipeClickAreas(IModRegistry registry) {
		for (Class<? extends GuiContainer> clazz : getGuiClasses()) {
			registry.addRecipeClickArea(clazz, containerInfo.jeiClickAreaX, containerInfo.jeiClickAreaY, containerInfo.jeiClickAreaW, containerInfo.jeiClickAreaH, getJEICategoryUid());
		}
	}
}
