package nc.tile.processor.info.builder;

import java.util.function.Supplier;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.handler.TileInfoHandler;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.UpgradableProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

public abstract class UpgradableProcessorContainerInfoBuilder<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends UpgradableProcessorContainerInfo<TILE, INFO>, BUILDER extends UpgradableProcessorContainerInfoBuilder<TILE, INFO, BUILDER>> extends ProcessorContainerInfoBuilder<TILE, INFO, BUILDER> {
	
	protected int[] speedUpgradeGuiXYWH = TileInfoHandler.standardSlot(132, 64);
	protected int[] energyUpgradeGuiXYWH = TileInfoHandler.standardSlot(152, 64);
	
	public UpgradableProcessorContainerInfoBuilder(String modId, String name, Supplier<TILE> tileSupplier, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction) {
		super(modId, name, tileSupplier, containerFunction, guiFunction, configContainerFunction, configGuiFunction);
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
