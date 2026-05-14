package nc.tile.processor.info;

import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.builder.UpgradableProcessorContainerInfoBuilder;
import nc.util.ContainerInfoHelper;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public abstract class UpgradableProcessorContainerInfo<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorContainerInfo<TILE, PACKET, INFO>> extends ProcessorContainerInfo<TILE, PACKET, INFO> {
	
	public final int speedUpgradeSlot;
	public final int energyUpgradeSlot;
	
	public final int[] speedUpgradeGuiXYWH;
	public final int[] energyUpgradeGuiXYWH;
	
	public final int[] speedUpgradeStackXY;
	public final int[] energyUpgradeStackXY;
	
	public final int speedUpgradeSorptionButtonID;
	public final int energyUpgradeSorptionButtonID;
	
	protected UpgradableProcessorContainerInfo(UpgradableProcessorContainerInfoBuilder<TILE, PACKET, INFO, ?> builder) {
		super(builder);
		
		speedUpgradeSlot = itemInputSize + itemOutputSize;
		energyUpgradeSlot = speedUpgradeSlot + 1;
		
		speedUpgradeGuiXYWH = builder.speedUpgradeGuiXYWH;
		energyUpgradeGuiXYWH = builder.energyUpgradeGuiXYWH;
		
		speedUpgradeStackXY = ContainerInfoHelper.stackXY(speedUpgradeGuiXYWH);
		energyUpgradeStackXY = ContainerInfoHelper.stackXY(energyUpgradeGuiXYWH);
		
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
