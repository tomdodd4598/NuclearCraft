package nc.tile.processor;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import net.minecraft.tileentity.TileEntity;

public class BasicUpgradeProcessorContainerInfo<TILE extends TileEntity> extends ProcessorContainerInfo<TILE> {
	
	public BasicUpgradeProcessorContainerInfo(String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, boolean consumesInputs, boolean losesProgress) {
		super(name, containerFunction, guiFunction, configContainerFunction, configGuiFunction, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, consumesInputs, losesProgress);
	}
	
	public int getSpeedUpgradeSlot() {
		return itemInputSize + itemOutputSize;
	}
	
	public int getEnergyUpgradeSlot() {
		return 1 + itemInputSize + itemOutputSize;
	}
}
