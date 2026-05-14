package nc.tile.processor.info.builder;

import com.google.common.collect.Lists;
import nc.container.ContainerFunction;
import nc.container.processor.ContainerSideConfig;
import nc.gui.*;
import nc.gui.processor.GuiProcessor;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tab.NCTabs;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.*;
import nc.util.NCUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import java.util.*;
import java.util.function.Supplier;

import static nc.NuclearCraft.proxy;
import static nc.config.NCConfig.*;

public abstract class ProcessorContainerInfoBuilder<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER>> extends ContainerInfoBuilder<BUILDER> {
	
	public final Class<TILE> tileClass;
	public final Supplier<TILE> tileSupplier;
	
	public final Class<? extends Container> containerClass;
	public final ContainerFunction<TILE> containerFunction;
	
	public final Class<? extends GuiContainer> guiClass;
	public final GuiFunction<TILE> guiFunction;
	
	public final ContainerFunction<TILE> configContainerFunction;
	public final GuiFunction<TILE> configGuiFunction;
	
	public CreativeTabs creativeTab = NCTabs.machine;
	
	public List<String> particles = new ArrayList<>();
	
	public int inputTankCapacity = 16000;
	public int outputTankCapacity = 16000;
	
	public double defaultProcessTime = processor_time_multiplier;
	public double defaultProcessPower = 0;
	
	public boolean isGenerator = false;
	
	public boolean consumesInputs = false;
	public boolean losesProgress = false;
	
	public String ocComponentName;
	
	protected ProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction) {
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
	
	protected ProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
		this(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerSideConfig::new, GuiFunction.of(modId, name, ContainerSideConfig::new, proxy.clientGet(() -> GuiProcessor.SideConfig::new)));
	}
	
	public ProcessorBlockInfo<TILE> buildBlockInfo() {
		return new ProcessorBlockInfo<>(modId, name, tileClass, tileSupplier, creativeTab, particles);
	}
	
	public abstract INFO buildContainerInfo();
	
	public BUILDER setCreativeTab(CreativeTabs tab) {
		creativeTab = tab;
		return getThis();
	}
	
	public BUILDER setCreativeTab(String tab) {
		return setCreativeTab(NCTabs.getCreativeTab(tab));
	}
	
	public BUILDER setParticles(String... particles) {
		this.particles = Lists.newArrayList(particles);
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
		if (isGenerator) {
			consumesInputs = true;
			losesProgress = false;
		}
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
