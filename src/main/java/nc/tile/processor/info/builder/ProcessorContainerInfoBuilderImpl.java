package nc.tile.processor.info.builder;

import java.util.function.Supplier;

import nc.container.ContainerFunction;
import nc.container.processor.ContainerMachineConfig;
import nc.gui.GuiFunction;
import nc.gui.processor.*;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class ProcessorContainerInfoBuilderImpl {
	
	public static class BasicProcessorContainerInfoBuilder<TILE extends TileBasicProcessor<TILE>> extends ProcessorContainerInfoBuilder<TILE, BasicProcessorContainerInfo<TILE>, BasicProcessorContainerInfoBuilder<TILE>> {
		
		public BasicProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, BasicProcessorGuiFunction<TILE> guiFunction) {
			super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerMachineConfig::new, GuiFunction.of(modId, name, ContainerMachineConfig::new, GuiProcessor.SideConfig::new));
		}
		
		@Override
		protected BasicProcessorContainerInfoBuilder<TILE> getThis() {
			return this;
		}
		
		@Override
		public BasicProcessorContainerInfo<TILE> buildContainerInfo() {
			return new BasicProcessorContainerInfo<>(modId, name, tileClass, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
		}
	}
	
	public static class BasicUpgradableProcessorContainerInfoBuilder<TILE extends TileBasicUpgradableProcessor<TILE>> extends UpgradableProcessorContainerInfoBuilder<TILE, BasicUpgradableProcessorContainerInfo<TILE>, BasicUpgradableProcessorContainerInfoBuilder<TILE>> {
		
		public BasicUpgradableProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, BasicProcessorGuiFunction<TILE> guiFunction) {
			super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerMachineConfig::new, GuiFunction.of(modId, name, ContainerMachineConfig::new, GuiUpgradableProcessor.SideConfig::new));
		}
		
		@Override
		protected BasicUpgradableProcessorContainerInfoBuilder<TILE> getThis() {
			return this;
		}
		
		@Override
		public BasicUpgradableProcessorContainerInfo<TILE> buildContainerInfo() {
			return new BasicUpgradableProcessorContainerInfo<>(modId, name, tileClass, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH, speedUpgradeGuiXYWH, energyUpgradeGuiXYWH);
		}
	}
}
