package nc.tile.processor.info.builder;

import static nc.config.NCConfig.*;

import java.util.*;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tab.NCTabs;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.*;
import nc.util.NCUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public abstract class ProcessorContainerInfoBuilder<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER>> extends ContainerInfoBuilder<BUILDER> {
	
	protected final Class<TILE> tileClass;
	protected final Supplier<TILE> tileSupplier;
	
	protected CreativeTabs creativeTab = NCTabs.machine;
	
	protected List<String> particles = new ArrayList<>();
	
	protected final Class<? extends Container> containerClass;
	protected final ContainerFunction<TILE> containerFunction;
	
	protected final Class<? extends GuiContainer> guiClass;
	protected final GuiFunction<TILE> guiFunction;
	
	protected final ContainerFunction<TILE> configContainerFunction;
	protected final GuiFunction<TILE> configGuiFunction;
	
	protected int inputTankCapacity = 16000;
	protected int outputTankCapacity = 16000;
	
	protected double defaultProcessTime = processor_time_multiplier;
	protected double defaultProcessPower = 0;
	
	protected boolean isGenerator = false;
	
	protected boolean consumesInputs = false;
	protected boolean losesProgress = false;
	
	protected String ocComponentName;
	
	protected ProcessorContainerInfoFunction<TILE, PACKET, INFO> infoFunction = null;
	
	public ProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction) {
		super(modId, name);
		
		this.tileClass = tileClass;
		this.tileSupplier = tileSupplier;
		
		this.containerClass = containerClass;
		this.containerFunction = containerFunction;
		
		this.guiClass = guiClass;
		this.guiFunction = guiFunction;
		
		this.configContainerFunction = configContainerFunction;
		this.configGuiFunction = configGuiFunction;
		
		ocComponentName = NCUtil.getShortModId(modId) + "_" + name;
	}
	
	public ProcessorBlockInfo<TILE> buildBlockInfo() {
		return new ProcessorBlockInfo<>(name, tileSupplier, creativeTab, particles);
	}
	
	public INFO buildContainerInfo() {
		return infoFunction.get(modId, name, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
	}
	
	public BUILDER setCreativeTab(CreativeTabs tab) {
		creativeTab = tab;
		return getThis();
	}
	
	public BUILDER setCreativeTab(String tab) {
		return setCreativeTab(NCTabs.getCreativeTab(tab));
	}
	
	public BUILDER setParticles(String... names) {
		particles = Lists.newArrayList(names);
		return getThis();
	}
	
	public BUILDER setInputTankCapacity(int capacity) {
		inputTankCapacity = capacity;
		return getThis();
	}
	
	public BUILDER setOutputTankCapacity(int capacity) {
		outputTankCapacity = capacity;
		return getThis();
	}
	
	public BUILDER setDefaultProcessTime(double processTime) {
		defaultProcessTime = processor_time_multiplier * processTime;
		return getThis();
	}
	
	public BUILDER setDefaultProcessPower(double processPower) {
		defaultProcessPower = processor_power_multiplier * processPower;
		return getThis();
	}
	
	public BUILDER setIsGenerator(boolean isGenerator) {
		this.isGenerator = isGenerator;
		consumesInputs = true;
		losesProgress = false;
		return getThis();
	}
	
	public BUILDER setConsumesInputs(boolean consumesInputs) {
		this.consumesInputs = consumesInputs;
		return getThis();
	}
	
	public BUILDER setLosesProgress(boolean losesProgress) {
		this.losesProgress = losesProgress;
		return getThis();
	}
	
	public BUILDER setOCComponentName(String ocComponentName) {
		this.ocComponentName = ocComponentName;
		return getThis();
	}
}
