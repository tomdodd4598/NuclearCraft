package nc.container.processor;

import nc.container.slot.SlotSpecificInput;
import nc.init.NCItems;
import nc.network.tile.ProcessorUpdatePacket;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.UpgradableProcessorContainerInfo;
import nc.util.Lazy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerUpgradableProcessor<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorContainerInfo<TILE, PACKET, INFO>> extends ContainerProcessor<TILE, PACKET, INFO> {
	
	public ContainerUpgradableProcessor(EntityPlayer player, TILE tile) {
		super(player, tile);
	}
	
	@Override
	protected void addMachineSlots(EntityPlayer player) {
		super.addMachineSlots(player);
		
		int[] speedUpgradeStackXY = info.speedUpgradeStackXY, energyUpgradeStackXY = info.energyUpgradeStackXY;
		addSlotToContainer(new SlotSpecificInput(tile, info.speedUpgradeSlot, speedUpgradeStackXY[0], speedUpgradeStackXY[1], speed_upgrade.get()));
		addSlotToContainer(new SlotSpecificInput(tile, info.energyUpgradeSlot, energyUpgradeStackXY[0], energyUpgradeStackXY[1], energy_upgrade.get()));
	}
	
	@Override
	public ItemStack transferPlayerStack(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (stack.getItem() == NCItems.upgrade) {
			int speedUpgradeSlot = info.itemInputSize + info.itemOutputSize;
			int energyUpgradeSlot = info.itemInputSize + info.itemOutputSize + 1;
			
			if (tile.isItemValidForSlot(speedUpgradeSlot, stack)) {
				if (!mergeItemStack(stack, speedUpgradeSlot, speedUpgradeSlot + 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (tile.isItemValidForSlot(energyUpgradeSlot, stack)) {
				if (!mergeItemStack(stack, energyUpgradeSlot, energyUpgradeSlot + 1, false)) {
					return ItemStack.EMPTY;
				}
			}
		}
		return super.transferPlayerStack(player, index, invStart, invEnd, stack);
	}
	
	public static Lazy<ItemStack> speed_upgrade = new Lazy<>(() -> new ItemStack(NCItems.upgrade, 1, 0));
	public static Lazy<ItemStack> energy_upgrade = new Lazy<>(() -> new ItemStack(NCItems.upgrade, 1, 1));
}
