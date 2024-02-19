package nc.multiblock.info.builder;

import java.util.function.Supplier;

import nc.container.ContainerFunction;
import nc.container.processor.ContainerMachineConfig;
import nc.gui.*;
import nc.gui.processor.*;
import nc.multiblock.Multiblock;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.multiblock.ITileMultiblockPart;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public abstract class MultiblockProcessorContainerInfoBuilder<MULTIBLOCK extends Multiblock<MULTIBLOCK, TILE>, TILE extends TileEntity & IProcessor<TILE, PACKET, INFO> & ITileMultiblockPart<MULTIBLOCK, TILE>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, BUILDER extends MultiblockProcessorContainerInfoBuilder<MULTIBLOCK, TILE, PACKET, INFO, BUILDER>> extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER> {
	
	public MultiblockProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
		super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerMachineConfig::new, GuiFunction.of(modId, name, ContainerMachineConfig::new, GuiProcessor.SideConfig::new));
	}
}
