package nc.tile.processor.info;

import java.util.List;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.processor.IProcessor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public abstract class UpgradableProcessorContainerInfo<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends UpgradableProcessorContainerInfo<TILE, INFO>> extends ProcessorContainerInfo<TILE, INFO> {
	
	public final int speedUpgradeSlot;
	public final int energyUpgradeSlot;
	
	public final int[] speedUpgradeGuiXYWH;
	public final int[] energyUpgradeGuiXYWH;
	
	public final int[] speedUpgradeStackXY;
	public final int[] energyUpgradeStackXY;
	
	public final int speedUpgradeSorptionButtonID;
	public final int energyUpgradeSorptionButtonID;
	
	protected UpgradableProcessorContainerInfo(String modId, String name, Class<TILE> tileClass, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH, int[] speedUpgradeGuiXYWH, int[] energyUpgradeGuiXYWH) {
		super(modId, name, tileClass, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
		
		speedUpgradeSlot = itemInputSize + itemOutputSize;
		energyUpgradeSlot = speedUpgradeSlot + 1;
		
		this.speedUpgradeGuiXYWH = speedUpgradeGuiXYWH;
		this.energyUpgradeGuiXYWH = energyUpgradeGuiXYWH;
		
		speedUpgradeStackXY = stackXY(speedUpgradeGuiXYWH);
		energyUpgradeStackXY = stackXY(energyUpgradeGuiXYWH);
		
		speedUpgradeSorptionButtonID = itemInputSize + fluidInputSize + itemOutputSize + fluidOutputSize;
		energyUpgradeSorptionButtonID = speedUpgradeSorptionButtonID + 1;
	}
	
	@Override
	public int getInventorySize() {
		return itemInputSize + itemOutputSize + 2;
	}
	
	@Override
	public List<ItemSorption> defaultItemSorptions() {
		List<ItemSorption> itemSorptions = super.defaultItemSorptions();
		itemSorptions.add(ItemSorption.IN);
		itemSorptions.add(ItemSorption.IN);
		return itemSorptions;
	}
}
