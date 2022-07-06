package nc.tile.processor;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import net.minecraft.tileentity.TileEntity;

public class BasicUpgradeProcessorContainerInfo<TILE extends TileEntity> extends ProcessorContainerInfo<TILE> {
	
	public BasicUpgradeProcessorContainerInfo(String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean consumesInputs, boolean losesProgress, int playerInventoryX, int playerInventoryY) {
		super(name, containerFunction, guiFunction, configContainerFunction, configGuiFunction, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, consumesInputs, losesProgress, playerInventoryX, playerInventoryY);
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
}
