package nc.container.processor;

import nc.container.slot.*;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.inventory.ITileFilteredInventory;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public abstract class ContainerFilteredProcessor<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends ContainerProcessor<TILE, PACKET, INFO> {
	
	public ContainerFilteredProcessor(EntityPlayer player, TILE tile) {
		super(player, tile);
	}
	
	@Override
	protected void addInputSlot(EntityPlayer player, int index, int xPosition, int yPosition) {
		addSlotToContainer(new SlotFiltered.ProcessorInput(tile, recipeHandler, index, xPosition, yPosition));
	}
	
	@Override
	public ItemStack transferPlayerStack(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (recipeHandler.isValidItemInput(stack)) {
			if (!mergeItemStack(stack, 0, info.itemInputSize, false)) {
				return ItemStack.EMPTY;
			}
		}
		else {
			NonNullList<ItemStack> filterStacks = tile.getFilterStacks();
			for (int i = 0; i < filterStacks.size(); ++i) {
				if (tile.canModifyFilter(i) && !filterStacks.get(i).isEmpty()) {
					filterStacks.set(i, ItemStack.EMPTY);
					tile.onFilterChanged(i);
					inventorySlots.get(i).onSlotChanged();
					return ItemStack.EMPTY;
				}
			}
		}
		return transferPlayerStackDefault(player, index, invStart, invEnd, stack);
	}
}
