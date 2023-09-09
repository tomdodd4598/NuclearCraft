package nc.tile.processor.info;

import java.util.List;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.tile.processor.TileProcessorImpl.*;

public class ProcessorContainerInfoImpl {
	
	public static class BasicProcessorContainerInfo<TILE extends TileBasicProcessor<TILE>> extends ProcessorContainerInfo<TILE, BasicProcessorContainerInfo<TILE>> {
		
		public BasicProcessorContainerInfo(String modId, String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiAlternateTexture, int[] jeiBackgroundXY, int[] jeiTooltipXYWH) {
			super(modId, name, containerFunction, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiAlternateTexture, jeiBackgroundXY, jeiTooltipXYWH);
		}
	}
	
	public static class BasicUpgradableProcessorContainerInfo<TILE extends TileBasicUpgradableProcessor<TILE>> extends UpgradableProcessorContainerInfo<TILE, BasicUpgradableProcessorContainerInfo<TILE>> {
		
		public BasicUpgradableProcessorContainerInfo(String modId, String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiAlternateTexture, int[] jeiBackgroundXY, int[] jeiTooltipXYWH, int[] speedUpgradeGuiXYWH, int[] energyUpgradeGuiXYWH) {
			super(modId, name, containerFunction, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiAlternateTexture, jeiBackgroundXY, jeiTooltipXYWH, speedUpgradeGuiXYWH, energyUpgradeGuiXYWH);
		}
	}
}
