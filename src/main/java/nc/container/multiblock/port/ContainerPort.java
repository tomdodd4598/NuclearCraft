package nc.container.multiblock.port;

import nc.container.ContainerInfoTile;
import nc.multiblock.*;
import nc.network.tile.TileUpdatePacket;
import nc.recipe.BasicRecipeHandler;
import nc.tile.*;
import nc.tile.inventory.ITileFilteredInventory;
import nc.tile.multiblock.ITileLogicMultiblockPart;
import nc.tile.multiblock.port.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public abstract class ContainerPort<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PORT extends TileEntity & ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileGui<PORT, PACKET, INFO>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET>, PACKET extends TileUpdatePacket, INFO extends TileContainerInfo<PORT>> extends ContainerInfoTile<PORT, PACKET, INFO> {
	
	protected final PORT tile;
	
	public ContainerPort(EntityPlayer player, PORT tile) {
		super(tile);
		this.tile = tile;
		tile.addTileUpdatePacketListener(player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.removeTileUpdatePacketListener(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stackCopy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		int invStart = tile instanceof ITileFilteredInventory ? 2 : 0;
		int invEnd = 36 + invStart;
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			stackCopy = stack.copy();
			
			if (index >= (tile instanceof ITileFilteredInventory ? 1 : 0) && index < invStart) {
				if (!mergeItemStack(stack, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack, stackCopy);
			}
			else if (index >= invStart) {
				ItemStack transfer = transferPlayerStack(player, index, invStart, invEnd, stack);
				if (transfer != null) {
					return transfer;
				}
			}
			else if (!mergeItemStack(stack, invStart, invEnd, false)) {
				return ItemStack.EMPTY;
			}
			
			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (stack.getCount() == stackCopy.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, stack);
		}
		return stackCopy;
	}
	
	public ItemStack transferPlayerStack(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (getRecipeHandler().isValidItemInput(stack)) {
			if (!mergeItemStack(stack, 0, tile instanceof ITileFilteredInventory ? 1 : 0, false)) {
				return ItemStack.EMPTY;
			}
		}
		else if (tile instanceof ITileFilteredInventory tileFiltered) {
            NonNullList<ItemStack> filterStacks = tileFiltered.getFilterStacks();
			for (int i = 0; i < filterStacks.size(); ++i) {
				if (tileFiltered.canModifyFilter(i) && !filterStacks.get(i).isEmpty()) {
					filterStacks.set(i, ItemStack.EMPTY);
					tileFiltered.onFilterChanged(i);
					inventorySlots.get(i).onSlotChanged();
					return ItemStack.EMPTY;
				}
			}
		}
		return transferPlayerStackDefault(player, index, invStart, invEnd, stack);
	}
	
	protected abstract BasicRecipeHandler getRecipeHandler();
}
