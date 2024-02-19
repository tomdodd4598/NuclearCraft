package nc.container.processor;

import nc.container.ContainerInfoTile;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerMachineConfig<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends ContainerInfoTile<TILE, PACKET, INFO> {
	
	public ContainerMachineConfig(EntityPlayer player, TILE tile) {
		super(tile);
		
		info.addPlayerSlots(this::addSlotToContainer, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < 0) {
				if (!mergeItemStack(itemstack1, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= 0) {
				if (index >= 0 && index < 27) {
					if (!mergeItemStack(itemstack1, 27, 36, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 27 && index < 36 && !mergeItemStack(itemstack1, 0, 27, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, 0, 36, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}
