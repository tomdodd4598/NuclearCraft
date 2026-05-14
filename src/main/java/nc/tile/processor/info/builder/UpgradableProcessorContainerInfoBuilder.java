package nc.tile.processor.info.builder;

import nc.container.ContainerFunction;
import nc.container.processor.ContainerSideConfig;
import nc.gui.*;
import nc.gui.processor.GuiUpgradableProcessor;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.UpgradableProcessorContainerInfo;
import nc.util.ContainerInfoHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

import static nc.NuclearCraft.proxy;

public abstract class UpgradableProcessorContainerInfoBuilder<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorContainerInfo<TILE, PACKET, INFO>, BUILDER extends UpgradableProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER>> extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER> {
	
	public int[] speedUpgradeGuiXYWH = ContainerInfoHelper.standardSlot(132, 64);
	public int[] energyUpgradeGuiXYWH = ContainerInfoHelper.standardSlot(152, 64);
	
	protected UpgradableProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction) {
		super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction);
	}
	
	protected UpgradableProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
		this(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerSideConfig::new, GuiFunction.of(modId, name, ContainerSideConfig::new, proxy.clientGet(() -> GuiUpgradableProcessor.SideConfig::new)));
	}
	
	public BUILDER setSpeedUpgradeSlot(int x, int y, int w, int h) {
		speedUpgradeGuiXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	public BUILDER setEnergyUpgradeSlot(int x, int y, int w, int h) {
		energyUpgradeGuiXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	@Override
	public BUILDER standardExtend(int x, int y) {
		super.standardExtend(x, y);
		
		speedUpgradeGuiXYWH[0] += x;
		speedUpgradeGuiXYWH[1] += y;
		
		energyUpgradeGuiXYWH[0] += x;
		energyUpgradeGuiXYWH[1] += y;
		
		return getThis();
	}
}
