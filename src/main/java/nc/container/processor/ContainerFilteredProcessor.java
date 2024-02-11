package nc.container.processor;

import nc.network.tile.ProcessorUpdatePacket;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerFilteredProcessor<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends ContainerProcessor<TILE, PACKET, INFO> {
	
	public ContainerFilteredProcessor(EntityPlayer player, TILE tile) {
		super(player, tile);
	}
	
	@Override
	public ItemStack transferPlayerStack(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (recipeHandler.isValidItemInput(stack)) {
			if (!mergeItemStack(stack, 0, info.itemInputSize, false)) {
				return ItemStack.EMPTY;
			}
		}
		else if (tile.canModifyFilter(0) && !tile.getFilterStacks().get(0).isEmpty()) {
			tile.getFilterStacks().set(0, ItemStack.EMPTY);
			tile.onFilterChanged(0);
			inventorySlots.get(0).onSlotChanged();
			return ItemStack.EMPTY;
		}
		else if (index >= invStart && index < invEnd - 9) {
			if (!mergeItemStack(stack, invEnd - 9, invEnd, false)) {
				return ItemStack.EMPTY;
			}
		}
		else if (index >= invEnd - 9 && index < invEnd && !mergeItemStack(stack, invStart, invEnd - 9, false)) {
			return ItemStack.EMPTY;
		}
		return null;
	}
}
