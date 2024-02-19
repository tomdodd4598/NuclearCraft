package nc.tile.processor.info.builder;

import java.util.function.Supplier;

import nc.container.ContainerFunction;
import nc.container.processor.ContainerMachineConfig;
import nc.gui.*;
import nc.gui.processor.*;
import nc.network.tile.processor.EnergyProcessorUpdatePacket;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class ProcessorContainerInfoBuilderImpl {
	
	public static class BasicProcessorContainerInfoBuilder<TILE extends TileBasicProcessor<TILE>> extends ProcessorContainerInfoBuilder<TILE, EnergyProcessorUpdatePacket, BasicProcessorContainerInfo<TILE>, BasicProcessorContainerInfoBuilder<TILE>> {
		
		public BasicProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
			super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerMachineConfig::new, GuiFunction.of(modId, name, ContainerMachineConfig::new, GuiProcessor.SideConfig::new));
			infoFunction = BasicProcessorContainerInfo::new;
		}
	}
	
	public static class BasicUpgradableProcessorContainerInfoBuilder<TILE extends TileBasicUpgradableProcessor<TILE>> extends UpgradableProcessorContainerInfoBuilder<TILE, EnergyProcessorUpdatePacket, BasicUpgradableProcessorContainerInfo<TILE>, BasicUpgradableProcessorContainerInfoBuilder<TILE>> {
		
		public BasicUpgradableProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
			super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerMachineConfig::new, GuiFunction.of(modId, name, ContainerMachineConfig::new, GuiUpgradableProcessor.SideConfig::new));
			infoFunction = BasicUpgradableProcessorContainerInfo::new;
		}
	}
}
