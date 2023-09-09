package nc.tile.processor.info.builder;

import static nc.config.NCConfig.*;

import java.util.*;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.tab.NCTabs;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.*;
import nc.util.MinMax.MinMaxInt;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public abstract class ProcessorContainerInfoBuilder<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, INFO, BUILDER>> {
	
	public final String modId;
	public final String name;
	
	protected final Supplier<TILE> tileSupplier;
	
	protected CreativeTabs creativeTab = NCTabs.machine;
	
	protected List<String> particles = Lists.newArrayList();
	
	protected final ContainerFunction<TILE> containerFunction;
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
	
	protected int[] guiWH = new int[] {176, 166};
	
	protected List<int[]> itemInputGuiXYWH = Lists.newArrayList();
	protected List<int[]> fluidInputGuiXYWH = Lists.newArrayList();
	protected List<int[]> itemOutputGuiXYWH = Lists.newArrayList();
	protected List<int[]> fluidOutputGuiXYWH = Lists.newArrayList();
	
	protected int[] playerGuiXY = new int[] {8, 84};
	
	protected int[] progressBarGuiXYWHUV = new int[] {74, 35, 37, 16, 176, 3};
	protected int[] energyBarGuiXYWHUV = new int[] {8, 6, 16, 74, 176, 90};
	
	protected int[] machineConfigGuiXY = new int[] {27, 63};
	protected int[] redstoneControlGuiXY = new int[] {47, 63};
	
	protected boolean jeiAlternateTexture = false;
	
	protected int[] jeiBackgroundXYWH = new int[] {51, 30, 86, 26};
	protected int[] jeiTooltipXYWH = new int[] {73, 34, 38, 18};
	
	public ProcessorContainerInfoBuilder(String modId, String name, Supplier<TILE> tileSupplier, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction) {
		this.modId = modId;
		this.name = name;
		
		this.tileSupplier = tileSupplier;
		
		this.containerFunction = containerFunction;
		this.guiFunction = guiFunction;
		
		this.configContainerFunction = configContainerFunction;
		this.configGuiFunction = configGuiFunction;
	}
	
	protected abstract BUILDER getThis();
	
	public ProcessorBlockInfo<TILE> buildBlockInfo() {
		return new ProcessorBlockInfo<>(name, tileSupplier, creativeTab, particles);
	}
	
	public abstract INFO buildContainerInfo();
	
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
	
	public BUILDER setGuiWH(int w, int h) {
		guiWH = new int[] {w, h};
		return getThis();
	}
	
	public BUILDER setItemInputSlots(int[]... slots) {
		itemInputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setFluidInputSlots(int[]... slots) {
		fluidInputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setItemOutputSlots(int[]... slots) {
		itemOutputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setFluidOutputSlots(int[]... slots) {
		fluidOutputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setPlayerGuiXY(int x, int y) {
		playerGuiXY = new int[] {x, y};
		return getThis();
	}
	
	public BUILDER setProgressBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
		progressBarGuiXYWHUV = new int[] {x, y, w, h, u, v};
		jeiTooltipXYWH = new int[] {x - 1, y - 1, w + 1, h + 2};
		return getThis();
	}
	
	public BUILDER setEnergyBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
		energyBarGuiXYWHUV = new int[] {x, y, w, h, u, v};
		return getThis();
	}
	
	public BUILDER setMachineConfigGuiXY(int x, int y) {
		machineConfigGuiXY = new int[] {x, y};
		return getThis();
	}
	
	public BUILDER setRedstoneControlGuiXY(int x, int y) {
		redstoneControlGuiXY = new int[] {x, y};
		return getThis();
	}
	
	public BUILDER setJeiAlternateTexture(boolean jeiAlternateTexture) {
		this.jeiAlternateTexture = jeiAlternateTexture;
		return getThis();
	}
	
	public BUILDER setJeiBackgroundXYWH(int x, int y, int w, int h) {
		jeiBackgroundXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	public BUILDER setJeiTooltipXYWH(int x, int y, int w, int h) {
		jeiTooltipXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	public BUILDER standardExtend(int x, int y) {
		guiWH[0] += x;
		guiWH[1] += y;
		
		playerGuiXY[0] += x;
		playerGuiXY[1] += y;
		
		energyBarGuiXYWHUV[3] += y;
		
		machineConfigGuiXY[0] += x;
		machineConfigGuiXY[1] += y;
		
		redstoneControlGuiXY[0] += x;
		redstoneControlGuiXY[1] += y;
		
		return getThis();
	}
	
	public void autoJeiBackgroundXY() {
		MinMaxInt minMaxCenterX = new MinMaxInt(), minMaxCenterY = new MinMaxInt(), minMaxRadialW = new MinMaxInt(), minMaxRadialH = new MinMaxInt();
		for (List<int[]> xywhList : Arrays.asList(itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH)) {
			for (int[] xywh : xywhList) {
				int halfW = xywh[2] / 2, halfH = xywh[3] / 2;
				minMaxCenterX.update(xywh[0] + halfW);
				minMaxCenterY.update(xywh[1] + halfH);
				minMaxRadialW.update(halfW + 1);
				minMaxRadialH.update(halfH + 1);
			}
		}
		
		jeiBackgroundXYWH = new int[] {minMaxCenterX.getMin() - minMaxRadialW.getMax(), minMaxCenterY.getMin() - minMaxRadialH.getMax(), minMaxCenterX.getMax() + minMaxRadialW.getMax(), minMaxCenterY.getMax() + minMaxRadialH.getMax()};
	}
}
