package nc.tile.processor;

import java.util.List;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.gui.processor.BasicUpgradeProcessorGuiFunction;

public class BasicUpgradeProcessorContainerInfo<TILE extends TileBasicUpgradeProcessor<TILE>> extends ProcessorContainerInfo<TILE, BasicUpgradeProcessorContainerInfo<TILE>> {
	
	public final int speedUpgradeGuiX;
	public final int speedUpgradeGuiY;
	public final int energyUpgradeGuiX;
	public final int energyUpgradeGuiY;
	
	public final int energyBarGuiX;
	public final int energyBarGuiY;
	public final int energyBarGuiWidth;
	public final int energyBarGuiHeight;
	public final int energyBarGuiU;
	public final int energyBarGuiV;
	
	public final int progressBarGuiX;
	public final int progressBarGuiY;
	public final int progressBarGuiWidth;
	public final int progressBarGuiHeight;
	public final int progressBarGuiU;
	public final int progressBarGuiV;
	
	public final int machineConfigGuiX;
	public final int machineConfigGuiY;
	
	public final int redstoneControlGuiX;
	public final int redstoneControlGuiY;
	
	// speedUpgradeGuiX = 132 + playerGuiX
	// speedUpgradeGuiY = 64 + playerGuiY
	// energyUpgradeGuiX = 152 + playerGuiX
	// energyUpgradeGuiY = 64 + playerGuiY
	
	public BasicUpgradeProcessorContainerInfo(String modId, String name, ContainerFunction<TILE> containerFunction, BasicUpgradeProcessorGuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean consumesInputs, boolean losesProgress, int guiSizeX, int guiSizeY, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int playerGuiX, int playerGuiY, int jeiBackgroundX, int jeiBackgroundY, int jeiTooltipX, int jeiTooltipY, int jeiTooltipWidth, int jeiTooltipHeight, int speedUpgradeGuiX, int speedUpgradeGuiY, int energyUpgradeGuiX, int energyUpgradeGuiY, int energyBarGuiX, int energyBarGuiY, int energyBarGuiWidth, int energyBarGuiHeight, int energyBarGuiU, int energyBarGuiV, int progressBarGuiX, int progressBarGuiY, int progressBarGuiWidth, int progressBarGuiHeight, int progressBarGuiU, int progressBarGuiV, int machineConfigGuiX, int machineConfigGuiY, int redstoneControlGuiX, int redstoneControlGuiY) {
		super(modId, name, containerFunction, (player, tile) -> guiFunction.apply(containerFunction.apply(player, tile), player, tile, modId + ":textures/gui/container/" + name + ".png"), configContainerFunction, configGuiFunction, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, consumesInputs, losesProgress, guiSizeX, guiSizeY, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiX, playerGuiY, jeiBackgroundX, jeiBackgroundY, jeiTooltipX, jeiTooltipY, jeiTooltipWidth, jeiTooltipHeight);
		this.speedUpgradeGuiX = speedUpgradeGuiX;
		this.speedUpgradeGuiY = speedUpgradeGuiY;
		this.energyUpgradeGuiX = energyUpgradeGuiX;
		this.energyUpgradeGuiY = energyUpgradeGuiY;
		this.energyBarGuiX = energyBarGuiX;
		this.energyBarGuiY = energyBarGuiY;
		this.energyBarGuiWidth = energyBarGuiWidth;
		this.energyBarGuiHeight = energyBarGuiHeight;
		this.energyBarGuiU = energyBarGuiU;
		this.energyBarGuiV = energyBarGuiV;
		this.progressBarGuiX = progressBarGuiX;
		this.progressBarGuiY = progressBarGuiY;
		this.progressBarGuiWidth = progressBarGuiWidth;
		this.progressBarGuiHeight = progressBarGuiHeight;
		this.progressBarGuiU = progressBarGuiU;
		this.progressBarGuiV = progressBarGuiV;
		this.machineConfigGuiX = machineConfigGuiX;
		this.machineConfigGuiY = machineConfigGuiY;
		this.redstoneControlGuiX = redstoneControlGuiX;
		this.redstoneControlGuiY = redstoneControlGuiY;
	}
	
	@Override
	public int getInventorySize() {
		return itemInputSize + itemOutputSize + 2;
	}
	
	public int getSpeedUpgradeSlot() {
		return itemInputSize + itemOutputSize;
	}
	
	public int getEnergyUpgradeSlot() {
		return itemInputSize + itemOutputSize + 1;
	}
	
	public int getMachineConfigButtonID() {
		return fluidInputSize + fluidOutputSize;
	}
	
	public int getRedstoneControlButtonID() {
		return fluidInputSize + fluidOutputSize + 1;
	}
}
